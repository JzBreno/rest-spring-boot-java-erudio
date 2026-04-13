package br.com.jzbreno.controllers;

import br.com.jzbreno.Exceptions.RequiredObjectIsNullException;
import br.com.jzbreno.controllers.docs.PersonControllerV2Doc;
import br.com.jzbreno.file.exporter.MediaTypes;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.DTO.PersonDTO2;
import br.com.jzbreno.services.PersonServiceV2;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/person/v2")
@Tag(name = "API REST Person Version 2")
public class PersonControllerV2 implements PersonControllerV2Doc {

    private final PersonServiceV2 personServices;

    public PersonControllerV2(PersonServiceV2 personServices) {
        this.personServices = personServices;
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<EntityModel<PersonDTO2>> findByIdV2(@PathVariable(name = "id") String id) {
        EntityModel<PersonDTO2> person = personServices.findByIdV2(id);
        return (person != null) ? ResponseEntity.ok(person) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/findAll",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO2>>> findAllV2(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "15") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sort", defaultValue = "firstName") String properties) {

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, properties));
        PagedModel<EntityModel<PersonDTO2>> people = personServices.findAllV2(pageable);

        return people.getContent().isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(people);
    }

    @GetMapping(value = "/generateExportPage",
            produces = {
                    MediaTypes.APPLICATION_XLSX_VALUE,
                    MediaTypes.TEXT_CSV_VALUE,
                    MediaTypes.APPLICATION_PDF_VALUE})
    @Override
    public ResponseEntity<Resource> generateExportPage(Integer page, Integer size, String direction, String properties, HttpServletRequest request) throws Exception {

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        log.info("Received export request. Page: {}, Size: {}, Format: {}, Sort: {} ({})",
                page, size, acceptHeader, properties, direction);

        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, properties));

        try {
            Resource resource = personServices.generateExportPage(pageable, acceptHeader);

            var contentType = (acceptHeader != null && !acceptHeader.contains("*/*")) ? acceptHeader : MediaTypes.APPLICATION_XLSX_VALUE;
            String fileExtension;

            if (contentType.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
                fileExtension = ".xlsx";
            } else {
                fileExtension = MediaTypes.APPLICATION_PDF_VALUE.equalsIgnoreCase(contentType) ? ".pdf" : ".csv";
            }

            var fileName = "people_exported_" + System.currentTimeMillis() + fileExtension;

            log.info("Export file '{}' generated successfully. Content-Type: {}", fileName, contentType);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("Error during file export for format {}: {}", acceptHeader, e.getMessage());
            throw e;
        }
    }


    @GetMapping(value = "/findbyname/{firstName}", // Corrigido erro de digitação: firtName -> firstName
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO2>>> findPersonByName(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "15") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "properties", defaultValue = "firstName") String properties,
            @PathVariable(name = "firstName") String firstName) { // Adicionado anotações explicitamente

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, properties));

        PagedModel<EntityModel<PersonDTO2>> people = personServices.findbyFirstName(firstName, pageable);

        return people.getContent().isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(people);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<EntityModel<PersonDTO2>> createV2(@RequestBody PersonDTO2 person) {
        return ResponseEntity.ok(personServices.createV2(person));
    }

    @PostMapping(value = "/massCreate",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<PersonDTO2> massiveCreatre(MultipartFile file) {
        log.info("Request received for massive creation. Filename: [{}], Content-Type: [{}], Size: [{} bytes]",
                file.getOriginalFilename(), file.getContentType(), file.getSize());

        if (file.isEmpty()) {
            log.warn("Massive creation failed: The uploaded file is empty.");
            throw new RequiredObjectIsNullException("File cannot be Empty");
        }

        long startTime = System.currentTimeMillis();
        List<PersonDTO2> people = personServices.massiveCreation(file);
        long duration = System.currentTimeMillis() - startTime;

        if (people.isEmpty()) {
            log.info("Massive creation completed, but no records were processed from file [{}]. Duration: {}ms",
                    file.getOriginalFilename(), duration);
            return (List<PersonDTO2>) ResponseEntity.noContent().build().getBody();
        }

        log.info("Massive creation successful. Processed {} records from file [{}] in {}ms",
                people.size(), file.getOriginalFilename(), duration);

        return ResponseEntity.ok(people).getBody();
    }


    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PersonDTO2> update(@RequestBody PersonDTO2 person) {
        return ResponseEntity.ok(personServices.updating(person));
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id) {
        personServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public void teste() {
        log.debug("Testando o logback");
    }
}