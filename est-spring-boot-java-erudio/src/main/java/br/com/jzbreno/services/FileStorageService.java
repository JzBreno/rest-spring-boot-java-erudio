package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.FileNotFoundException;
import br.com.jzbreno.Exceptions.FileStorageException;
import br.com.jzbreno.config.FileStorageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;

@Service
public class FileStorageService {

    private final Path fileStoragePath;
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);


    public FileStorageService(FileStorageConfig fileStorageConfig) {

        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath();

        this.fileStoragePath = path;

        try {
            if (!Files.exists(path)) {
                logger.info("Creting Directory {}", path.toAbsolutePath().toString());
                Files.createDirectories(path);
                logger.info(MessageFormat.format("Folder {} no criado", path.toAbsolutePath()));
            }
        }catch (Exception e) {
            logger.error("Erro ao criar o arquivo {}", e.getMessage());
            throw new FileStorageException("Could not create the directory where files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                logger.warn("Invalid file name! File name contains invalid path sequence {}", fileName);
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            logger.info("Criando e Salvando arquivo {}", fileName);
            Path targetLocation = this.fileStoragePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File {} copiado com sucesso!", fileName);
            return file.getOriginalFilename();
        }catch (Exception e) {
            logger.error("Erro ao criar o arquivo {}", e.getMessage());
            throw new FileStorageException("Could not store file " + fileName + "!", e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        logger.debug("Attempting to load file: {}", fileName);

        try {
            Path file = this.fileStoragePath.resolve(fileName).normalize();
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

}
