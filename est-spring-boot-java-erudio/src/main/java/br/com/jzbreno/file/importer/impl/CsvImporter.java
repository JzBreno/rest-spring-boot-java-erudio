package br.com.jzbreno.file.importer.impl;

import br.com.jzbreno.file.importer.contract.FileImporter;
import br.com.jzbreno.model.DTO.PersonDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
//anotando com component conseguimos mapear essas classes para injecao usando a interface FileImporter como referencia
@Component
public class CsvImporter implements FileImporter {
    @Override
    public Boolean canHandle(String name) {
        return name.endsWith(".csv");
    }

    @Override
    public List<PersonDTO> importFile(InputStream stream) throws Exception {
        CSVFormat format = CSVFormat.Builder.create()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();

        Iterable<CSVRecord> records = format.parse(new InputStreamReader(stream));
        return parseRecordsToPersonDTO(records);
    }

    private List<PersonDTO> parseRecordsToPersonDTO(Iterable<CSVRecord> records) {
        List<PersonDTO> people = new ArrayList<>();
        for (CSVRecord record : records) {
            PersonDTO personDTO = new PersonDTO();
            personDTO.setFirstName(record.get("first_name"));
            personDTO.setLastName(record.get("last_name"));
            personDTO.setAddress(record.get("address"));
            personDTO.setGender(record.get("gender"));
            personDTO.setEnabled(true);
            people.add(personDTO);

        }
        return people;
    }
}
