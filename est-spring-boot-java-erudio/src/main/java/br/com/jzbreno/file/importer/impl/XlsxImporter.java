package br.com.jzbreno.file.importer.impl;


import br.com.jzbreno.file.importer.contract.FileImporter;
import br.com.jzbreno.model.DTO.PersonDTO;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
//anotando com component conseguimos mapear essas classes para injecao usando a interface FileImporter como referencia

@Component
public class XlsxImporter implements FileImporter {
    @Override
    public Boolean canHandle(String name) {
        return name.endsWith(".xlsx");
    }

    @Override
    public List<PersonDTO> importFile(InputStream stream) throws Exception {

        return List.of();
    }
}
