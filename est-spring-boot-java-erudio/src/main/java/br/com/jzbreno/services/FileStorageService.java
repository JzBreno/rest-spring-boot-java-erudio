package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.FileStorageException;
import br.com.jzbreno.config.FileStorageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Path fileStorageService;
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);


    public FileStorageService(FileStorageConfig fileStorageConfig) {

        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath();

        this.fileStorageService = path;

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
            Path targetLocation = this.fileStorageService.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File {} copiado com sucesso!", fileName);
            return file.getOriginalFilename();
        }catch (Exception e) {
            logger.error("Erro ao criar o arquivo {}", e.getMessage());
            throw new FileStorageException("Could not store file " + fileName + "!", e);
        }
    }

}
