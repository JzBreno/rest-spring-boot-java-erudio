package br.com.jzbreno.file.exporter.impl;

import br.com.jzbreno.file.exporter.MediaTypes;
import br.com.jzbreno.file.exporter.contract.FileExporter;
import br.com.jzbreno.file.importer.contract.FileImporter;
import br.com.jzbreno.model.DTO.PersonDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class XlsxExporter implements FileExporter {

    private final DataFormatter formatter = new DataFormatter();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public boolean canHandle(String acceptHeader) {
        return  acceptHeader != null &&  acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE);
    }

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {

        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("People");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID","First Name","Last Name","Addres","Gender", "Enabled"};

            for(int i =0 ; i < headers.length; i++){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowIndex = 1;

            GenerateListToRow(people, sheet, rowIndex);

            for(int i =0; i< headers.length ; i++){
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }

    }

    private static void GenerateListToRow(List<PersonDTO> people, Sheet sheet, int rowIndex) {
        for(PersonDTO person : people){
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(person.getId());
            row.createCell(1).setCellValue(person.getFirstName());
            row.createCell(2).setCellValue(person.getLastName());
            row.createCell(3).setCellValue(person.getAddress());
            row.createCell(4).setCellValue(person.getGender());
            row.createCell(5).setCellValue(person.getEnabled() != null && person.getEnabled() ? "Yes" : "No");
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook){

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }

}