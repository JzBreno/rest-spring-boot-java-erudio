package br.com.jzbreno.controllers;

import br.com.jzbreno.model.DTO.UploadFileResponseDTO;
import br.com.jzbreno.services.FileStorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @Override
    public ResponseEntity<ResponseEntity> downloadFile(String fileName, HttpServletResponse response) {
        return null;
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
