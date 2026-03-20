package br.com.jzbreno.file.importer.impl;

import br.com.jzbreno.file.importer.contract.FileImporter;
import br.com.jzbreno.model.DTO.PersonDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class XlsxImporter implements FileImporter {

    private final DataFormatter formatter = new DataFormatter();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public boolean canHandle(String name) {
        return name != null && name.toLowerCase().endsWith(".xlsx");
    }

    @Override
    public List<PersonDTO> importFile(InputStream stream) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook(stream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) rowIterator.next();

            return parseRecordsToPersonDTOList(rowIterator);
        }
    }

    public List<PersonDTO> parseRecordsToPersonDTOList(Iterator<Row> rowIterator) {
        List<PersonDTO> people = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (rowIsValid(row)) {
                people.add(parseRowToPersonDTO(row));
            }
        }
        return people;
    }

    private PersonDTO parseRowToPersonDTO(Row row) {
        PersonDTO personDTO = new PersonDTO();

        personDTO.setFirstName(formatter.formatCellValue(row.getCell(0)));
        personDTO.setLastName(formatter.formatCellValue(row.getCell(1)));
        personDTO.setBirthday(parseLocalDate(row.getCell(2)));
        personDTO.setAddress(formatter.formatCellValue(row.getCell(3)));

        String gender = formatter.formatCellValue(row.getCell(4)).trim();
        personDTO.setGender(gender.length() > 6 ? gender.substring(0, 6) : gender);

        String enabledStr = formatter.formatCellValue(row.getCell(5));
        personDTO.setEnabled(enabledStr.equalsIgnoreCase("true") || enabledStr.equals("1"));

        personDTO.setEmail(formatter.formatCellValue(row.getCell(6)));
        personDTO.setPhoneNumber(formatter.formatCellValue(row.getCell(7)));

        return personDTO;
    }

    private LocalDate parseLocalDate(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        try {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            return LocalDate.parse(formatter.formatCellValue(cell), dateFormatter);
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean rowIsValid(Row row) {
        return row != null && row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}