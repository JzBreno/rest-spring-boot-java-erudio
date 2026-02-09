package br.com.jzbreno.repository;

import br.com.jzbreno.model.DTO.UploadFileResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UploadFileResponseDTO, Long> {
}
