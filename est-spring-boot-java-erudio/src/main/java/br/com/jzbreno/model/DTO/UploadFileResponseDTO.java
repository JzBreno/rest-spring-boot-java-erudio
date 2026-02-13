package br.com.jzbreno.model.DTO;

import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.util.Objects;

@Relation(value = "FileStoreDocs")
public class UploadFileResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long fileSize;

    public UploadFileResponseDTO() {}

    public UploadFileResponseDTO(String fileName, String fileDownloadUri, String fileType, long fileSize) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UploadFileResponseDTO that = (UploadFileResponseDTO) o;
        return fileSize == that.fileSize && Objects.equals(fileName, that.fileName) && Objects.equals(fileDownloadUri, that.fileDownloadUri) && Objects.equals(fileType, that.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileDownloadUri, fileType, fileSize);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
