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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController()
@RequestMapping("/api/files/v1")
public class FileController implements FIleControllerDocs{

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/uploadfile")
    @Override
    public UploadFileResponseDTO uploadFile(MultipartFile file) {
        return null;
    }
    @PostMapping(value = "/uploadfiles")
    @Override
    public List<UploadFileResponseDTO> uploadFiles(List<MultipartFile> files) {
        return List.of();
    }

    @Override
    public ResponseEntity<ResponseEntity> downloadFile(String fileName, HttpServletResponse response) {
        return null;
    }
}
