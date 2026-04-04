package br.com.jzbreno.file.exporter.contract;

import br.com.jzbreno.model.DTO.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileExporter {
    boolean canHandle(String name);
    Resource exportFile(List<PersonDTO> people) throws Exception;
}
