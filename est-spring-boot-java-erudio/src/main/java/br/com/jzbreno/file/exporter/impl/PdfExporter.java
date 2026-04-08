package br.com.jzbreno.file.exporter.impl;

import br.com.jzbreno.file.exporter.MediaTypes;
import br.com.jzbreno.file.exporter.contract.FileExporter;
import br.com.jzbreno.model.DTO.PersonDTO;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PdfExporter implements FileExporter {

    @Override
    public boolean canHandle(String acceptHeader) {
        return acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_PDF_VALUE);
    }

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {
        return null;
    }
}
