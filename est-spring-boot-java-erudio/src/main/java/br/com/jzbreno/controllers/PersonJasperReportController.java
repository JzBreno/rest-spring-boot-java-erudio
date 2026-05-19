package br.com.jzbreno.controllers;

import br.com.jzbreno.controllers.docs.PersonJasperReportControllerDoc;
import br.com.jzbreno.file.exporter.MediaTypes;
import br.com.jzbreno.services.PersonJasperReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/person/v2/reports/jasper")
@Tag(name = "Jasper Reports")
public class PersonJasperReportController implements PersonJasperReportControllerDoc {

    private final PersonJasperReportService jasperReportService;

    public PersonJasperReportController(PersonJasperReportService jasperReportService) {
        this.jasperReportService = jasperReportService;
    }

    @GetMapping(value = "/people/pdf", produces = MediaTypes.APPLICATION_PDF_VALUE)
    @Override
    public ResponseEntity<Resource> exportPeoplePdf(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "15") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sort", defaultValue = "firstName") String sort) throws Exception {

        Pageable pageable = buildPageable(page, size, direction, sort);
        Resource resource = jasperReportService.generatePeopleReportPdf(pageable);
        String fileName = "relatorio_pessoas_jasper_" + System.currentTimeMillis() + ".pdf";

        log.info("Relatório Jasper PDF gerado: {}, página {}, tamanho {}", fileName, page, size);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping(value = "/people/xlsx", produces = MediaTypes.APPLICATION_XLSX_VALUE)
    @Override
    public ResponseEntity<Resource> exportPeopleXlsx(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "15") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sort", defaultValue = "firstName") String sort) throws Exception {

        Pageable pageable = buildPageable(page, size, direction, sort);
        Resource resource = jasperReportService.generatePeopleReportXlsx(pageable);
        String fileName = "relatorio_pessoas_jasper_" + System.currentTimeMillis() + ".xlsx";

        log.info("Relatório Jasper XLSX gerado: {}, página {}, tamanho {}", fileName, page, size);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaTypes.APPLICATION_XLSX_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    private static Pageable buildPageable(Integer page, Integer size, String direction, String sort) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(sortDirection, sort));
    }
}
