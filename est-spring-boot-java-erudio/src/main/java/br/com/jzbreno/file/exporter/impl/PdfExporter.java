package br.com.jzbreno.file.exporter.impl;

import br.com.jzbreno.file.exporter.MediaTypes;
import br.com.jzbreno.file.exporter.contract.FileExporter;
import br.com.jzbreno.model.DTO.PersonDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements FileExporter {

    private static final Logger log = LoggerFactory.getLogger(PdfExporter.class);

    @Override
    public boolean canHandle(String acceptHeader) {
        return acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_PDF_VALUE);
    }

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {

        log.info("Atualizacao deu Ok");

        InputStream inputStream = getClass().getResourceAsStream("/templates/Relatorio_Pessoas.jasper");

        if (inputStream == null) {
            throw new RuntimeException("Arquivo .jasper não encontrado em resources/templates/");
        }

        JasperReport jasperReport = (JasperReport) net.sf.jasperreports.engine.util.JRLoader.loadObject(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
        Map<String, Object> parameters = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }
}
