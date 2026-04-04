package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.FileNotFoundException;
import br.com.jzbreno.Exceptions.FileStorageException;
import br.com.jzbreno.config.FileStorageConfig;
import br.com.jzbreno.model.File;
import br.com.jzbreno.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class FileStorageServiceV2 {

    private final Path pathDefault;
    private final Path pathPdf;
    private final Path pathVideo;
    private final Path pathSong;
    private final Path pathTxt;
    private final Path pathOther;

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceV2.class);
    private final FileRepository fileRepository;

    public FileStorageServiceV2(FileStorageConfig fileStorageConfig, FileRepository fileRepository) {

        this.fileRepository = fileRepository;
        this.pathDefault = resolvePath(fileStorageConfig.getUploadDir());
        this.pathPdf = resolvePath(fileStorageConfig.getUploadDirPdf());
        this.pathVideo = resolvePath(fileStorageConfig.getUploadDirVideo());
        this.pathSong = resolvePath(fileStorageConfig.getUploadDirSong());
        this.pathTxt = resolvePath(fileStorageConfig.getUploadDirTxt());
        this.pathOther = resolvePath(fileStorageConfig.getUploadDirOther());

        initDirectories();
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path targetLocation =  getPath(fileName);

        try {
            if (fileName.contains("..")) {
                logger.warn("Invalid file name! File name contains invalid path sequence {}", fileName);
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            logger.info("Criando e Salvando arquivo {}", fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            File filePath = new File();
            filePath.setFileName(fileName);
            filePath.setPath(targetLocation.toString());
            filePath.setFileSize(file.getSize());
            fileRepository.save(filePath);
            return file.getOriginalFilename();
        }catch (Exception e) {
            logger.error("Erro ao criar o arquivo {}", e.getMessage());
            throw new FileStorageException("Could not store file " + fileName + "!", e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        logger.debug("Attempting to load file: {}", fileName);

        try {
            Path file = getPath(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                logger.info("File successfully loaded: {}", fileName);
                return resource;
            } else {
                logger.error("File not found or not readable: {}", fileName);
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (Exception e) {
            logger.error("Critical error while loading file {}: ", fileName, e);
            throw new FileNotFoundException("Could not load file " + fileName, e);
        }
    }

    private Path getPath(String fileName) {
        Path targetLocation;
        if(!fileName.isEmpty() && fileName.toLowerCase().endsWith(".pdf")) {
            targetLocation = this.pathPdf.resolve(fileName);
            logger.info("File {} copiado com sucesso!", targetLocation);
        }else if(!fileName.isEmpty() && fileName.toLowerCase().endsWith(".mp3")) {
            targetLocation = this.pathSong.resolve(fileName);
            logger.info("File {} copiado com sucesso!", targetLocation);
        }else if(!fileName.isEmpty() && fileName.toLowerCase().matches(".*\\.mp.*")) {
            targetLocation = this.pathVideo.resolve(fileName);
            logger.info("File {} copiado com sucesso!", targetLocation);
        }else if(!fileName.isEmpty() && fileName.toLowerCase().endsWith(".txt")) {
            targetLocation = this.pathTxt.resolve(fileName);
            logger.info("File {} copiado com sucesso!", targetLocation);
        }else{
            targetLocation = this.pathOther.resolve(fileName);
            logger.info("File {} copiado com sucesso!", targetLocation);
        }
        return targetLocation;
    }

    private Path resolvePath(String dir) {
        return Paths.get(dir).toAbsolutePath().normalize();
    }

    private void initDirectories() {
        try {

            List<Path> allPaths = List.of(pathDefault, pathPdf, pathVideo, pathSong, pathTxt, pathOther);

            for (Path path : allPaths) {
                if (Files.notExists(path)) {
                    Files.createDirectories(path);
                }
            }
        } catch (IOException e) {
            throw new FileStorageException("Erro ao inicializar a estrutura de pastas do sistema.", e);
        }
    }

}
