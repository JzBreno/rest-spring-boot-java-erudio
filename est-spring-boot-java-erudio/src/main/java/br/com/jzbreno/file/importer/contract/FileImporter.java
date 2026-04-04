package br.com.jzbreno.file.importer.contract;

import br.com.jzbreno.model.DTO.BookDTO;
import br.com.jzbreno.model.DTO.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    boolean canHandle(String name);
    List<PersonDTO> importFile(InputStream stream) throws Exception;

}
