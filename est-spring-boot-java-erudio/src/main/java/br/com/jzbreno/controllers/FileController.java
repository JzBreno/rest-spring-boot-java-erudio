package br.com.jzbreno.controllers;

import br.com.jzbreno.model.DTO.UploadFileResponseDTO;
import br.com.jzbreno.services.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController()
@RequestMapping("/api/files/v1")
public class FileController implements FIleControllerDocs{

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/uploadFile")
    @Override
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
        fileStorageService.storeFile(file);
        UploadFileResponseDTO uploadFileResponseDTO = new UploadFileResponseDTO();
        uploadFileResponseDTO = generateResponse(file, uploadFileResponseDTO);
        return uploadFileResponseDTO;
    }

    @PostMapping(value = "/uploadFiles")
    @Override
    public List<UploadFileResponseDTO> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        List<MultipartFile> multipartFiles = Arrays.asList(files);
        return multipartFiles.stream().map(this::uploadFile).toList();
    }

    @GetMapping("/downloadfile/{fileName:.+}")
    @Override
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName, HttpServletRequest request) {
        Resource file = fileStorageService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (Exception e) {
            logger.error("Error during loading file {}", fileName, e);
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    private UploadFileResponseDTO generateResponse(MultipartFile file, UploadFileResponseDTO uploadFileResponseDTO) {
        // http://localhost:8080/api/files/v1/downloadfile/nomedoArquivo
        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath() //montando o caminho onde esta o arquivo para setar o download
                .path("/api/files/v1/downloadfile/")
                .path(file.getOriginalFilename())
                .toUriString();

        uploadFileResponseDTO.setFileName(file.getOriginalFilename());
        uploadFileResponseDTO.setFileSize(file.getSize());
        uploadFileResponseDTO.setFileType(file.getContentType());
        uploadFileResponseDTO.setFileName(file.getOriginalFilename());
        uploadFileResponseDTO.setFileDownloadUri(fileDownloadUri);
        return uploadFileResponseDTO;
    }
}
