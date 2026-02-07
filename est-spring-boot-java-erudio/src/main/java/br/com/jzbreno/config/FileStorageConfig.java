package br.com.jzbreno.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file") //dessa forma referenciamos as configuracoes do yml
public class FileStorageConfig {

    private String uploadDir;

    public FileStorageConfig() {}

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
