package br.com.jzbreno.services;

import br.com.jzbreno.mapper.ObjectMapper;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.Person;
import br.com.jzbreno.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PersonJasperReportService {

    static final String PEOPLE_TEMPLATE_CLASSPATH = "/templates/Relatorio_Pessoas.jrxml";

    private final PersonRepository personRepository;

    private volatile JasperReport cachedPeopleReport;

    public PersonJasperReportService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Resource generatePeopleReportPdf(Pageable pageable) throws Exception {
        JasperPrint print = buildPeopleJasperPrint(pageable);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(print, out);
            return new ByteArrayResource(out.toByteArray());
        }
    }

    public Resource generatePeopleReportXlsx(Pageable pageable) throws Exception {
        JasperPrint print = buildPeopleJasperPrint(pageable);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(false);
            configuration.setRemoveEmptySpaceBetweenRows(true);
            configuration.setDetectCellType(true);
            configuration.setWhitePageBackground(false);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            return new ByteArrayResource(out.toByteArray());
        }
    }

    private JasperPrint buildPeopleJasperPrint(Pageable pageable) throws Exception {
        List<PersonDTO> people = personRepository.findAll(pageable).stream()
                .map(entity -> ObjectMapper.parseObject(entity, PersonDTO.class))
                .toList();

        log.debug("Jasper fill: {} registros (página {}, tamanho {})",
                people.size(), pageable.getPageNumber(), pageable.getPageSize());

        JasperReport report = getOrCompilePeopleReport();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
        Map<String, Object> parameters = new HashMap<>();

        return JasperFillManager.fillReport(report, parameters, dataSource);
    }

    private JasperReport getOrCompilePeopleReport() throws Exception {
        JasperReport local = cachedPeopleReport;
        if (local != null) {
            return local;
        }
        synchronized (this) {
            if (cachedPeopleReport == null) {
                try (InputStream jrxml = getClass().getResourceAsStream(PEOPLE_TEMPLATE_CLASSPATH)) {
                    if (jrxml == null) {
                        throw new IllegalStateException(
                                "Template JRXML não encontrado no classpath: " + PEOPLE_TEMPLATE_CLASSPATH);
                    }
                    cachedPeopleReport = JasperCompileManager.compileReport(jrxml);
                    log.info("Template Jasper compilado e cacheado: {}", PEOPLE_TEMPLATE_CLASSPATH);
                }
            }
            return cachedPeopleReport;
        }
    }
}
