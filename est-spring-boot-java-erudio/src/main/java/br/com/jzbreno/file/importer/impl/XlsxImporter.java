package br.com.jzbreno.file.importer.impl;


import br.com.jzbreno.file.importer.contract.FileImporter;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.File;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//anotando com component conseguimos mapear essas classes para injecao usando a interface FileImporter como referencia

@Component
public class XlsxImporter implements FileImporter {
    @Override
    public boolean canHandle(String name) {
        return name.endsWith(".xlsx");
    }

    @Override
    public List<PersonDTO> importFile(InputStream stream) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook(stream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if(rowIterator.hasNext()) rowIterator.next();

            return parseRecordsToPersonDTOList(rowIterator);
        }
    }

    public List<PersonDTO> parseRecordsToPersonDTOList(Iterator<Row> rowIterator) throws Exception {
        List<PersonDTO> people = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if(rowIsValid(row)) {
                people.add(parseRowToPersonDTO(row));
            }
        };

        return people;
    }

    private PersonDTO parseRowToPersonDTO(Row row) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setEnabled(true);
        personDTO.setFirstName(row.getCell(0).getStringCellValue());
        personDTO.setLastName(row.getCell(1).getStringCellValue());
        personDTO.setGender(row.getCell(2).getStringCellValue());
        personDTO.setAddress(row.getCell(3).getStringCellValue());
        return personDTO;
    }

    private static boolean rowIsValid(Row row) {
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}


