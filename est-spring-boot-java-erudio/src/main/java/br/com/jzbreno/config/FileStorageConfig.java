package br.com.jzbreno.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file") //dessa forma referenciamos as configuracoes do yml
public class FileStorageConfig {

    private String uploadDir;
    private String uploadDirTxt;
    private String uploadDirPdf;
    private String uploadDirVideo;
    private String uploadDirSong;
    private String uploadDirOther;

    public FileStorageConfig() {}

    public String getUploadDir() {
        return uploadDir;
    }
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getUploadDirTxt() {
        return uploadDirTxt;
    }

    public void setUploadDirTxt(String uploadDirTxt) {
        this.uploadDirTxt = uploadDirTxt;
    }

    public String getUploadDirPdf() {
        return uploadDirPdf;
    }

    public void setUploadDirPdf(String uploadDirPdf) {
        this.uploadDirPdf = uploadDirPdf;
    }

    public String getUploadDirVideo() {
        return uploadDirVideo;
    }

    public void setUploadDirVideo(String uploadDirVideo) {
        this.uploadDirVideo = uploadDirVideo;
    }

    public String getUploadDirSong() {
        return uploadDirSong;
    }

    public void setUploadDirSong(String uploadDirSong) {
        this.uploadDirSong = uploadDirSong;
    }

    public String getUploadDirOther() {
        return uploadDirOther;
    }

    public void setUploadDirOther(String uploadDirOther) {
        this.uploadDirOther = uploadDirOther;
    }
}
