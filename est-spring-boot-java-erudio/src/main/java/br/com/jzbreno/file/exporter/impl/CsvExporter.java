package br.com.jzbreno.file.exporter.impl;

import br.com.jzbreno.file.exporter.MediaTypes;
import br.com.jzbreno.file.exporter.contract.FileExporter;
import br.com.jzbreno.file.importer.contract.FileImporter;
import br.com.jzbreno.file.importer.impl.CsvImporter;
import br.com.jzbreno.model.DTO.PersonDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//anotando com component conseguimos mapear essas classes para injecao usando a interface FileImporter como referencia
@Component
public class CsvExporter implements FileExporter {
    @Override
    public boolean canHandle(String acceptHeader) {
        return  acceptHeader.equalsIgnoreCase(MediaTypes.TEXT_CSV_VALUE);
    }

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setHeader("ID", "FirstName", "LastName","Addres","Gender", "Enabled")
                .setSkipHeaderRecord(false)
                .build();

        try(CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);){
            people.forEach(p -> {
                try {
                    csvPrinter.printRecord(
                            p.getId(),
                            p.getFirstName(),
                            p.getLastName(),
                            p.getAddress(),
                            p.getGender(),
                            p.getEnabled()
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return new ByteArrayResource(outputStream.toByteArray());
    }

}
