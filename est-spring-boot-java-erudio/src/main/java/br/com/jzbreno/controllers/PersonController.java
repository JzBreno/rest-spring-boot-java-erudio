package br.com.jzbreno.controllers;

import br.com.jzbreno.Exceptions.RequiredObjectIsNullException;
import br.com.jzbreno.file.exporter.MediaTypes;
import br.com.jzbreno.mapper.PersonMapper;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.DTO.PersonDTO2;
import br.com.jzbreno.services.PersonServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.table.TableCellEditor;
import java.util.*;

@RestController
@RequestMapping("/person/v1")
@Tag(name = "API REST Person", description = "Enpoint for managing Persons, version 1")
public class PersonController{

//    @Autowired
//    private PersonServices personServicesAutowired;
//    assim seria a forma de injecao com anotacao, mas irei usar no construtor como boa pratica

    private final PersonServices personServices;

    public PersonController(PersonServices personServices, DefaultLifecycleProcessor defaultLifecycleProcessor) {
        this.personServices = personServices;
    }

    //adicionado informacoes no request
    @GetMapping(value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    //adicionando as anotations do Swagger para melhorar nossa documentacao da API
    @Operation(
            summary = "Find a Person by ID",
            description = "Returns the details of a specific person, including contact information, using their unique ID.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success - Person found",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class))
                            }),
                    @ApiResponse(
                            description = "Not Found - The person with the provided ID does not exist.",
                            responseCode = "404",
                            content = @Content),
                    @ApiResponse(
                            description = "Bad Request - The provided ID is invalid or malformed.",
                            responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID of the Person to be retrieved (String format).",
                            required = true,
                            example = "1"
                    )
            }
    )
    public ResponseEntity<PersonDTO> findById(@PathVariable(name = "id") String id){
        PersonDTO person = personServices.findById(id);
        if(id.equals(String.valueOf(1))){
            person.setEmail("josebrenosousa@gmail.com");
            person.setPhoneNumber("+55 85985511569");
        }

        if(id.equals(String.valueOf(3))){
            person.setEmail("clauid@gmail.com");
            person.setPhoneNumber("+55 85985597469");
        }
        if(person != null) return ResponseEntity.ok().body(person);
        else return ResponseEntity.notFound().build();
    }

    //adicionado informacoes no request
    @GetMapping(value = "/findAll",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find a list of All Person in Database",
            description = "Find a list of All Person in Database",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success - Person found",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))
                            }),
                    @ApiResponse(
                            description = "Not Found - The person with the provided ID does not exist.",
                            responseCode = "404",
                            content = @Content),
                    @ApiResponse(
                            description = "Bad Request - The provided ID is invalid or malformed.",
                            responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "15") Integer size,
                                                   @RequestParam(value = "direction", defaultValue = "asc") String direction,
                                                   @RequestParam(value = "properties", defaultValue = "firstName") String properties
                                                   ) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        //criando paginacao, pagerequest.of monta o pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, properties) );
        PagedModel<EntityModel<PersonDTO>> people = personServices.findAll(pageable);

        if (people.getContent().isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(people);
    }

    //adicionado informacoes no request
    @PostMapping(value = "/massCreate",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE})
    @Operation(
            summary = "Insert a list of People in Database",
            description = "Process an XLSX or CSV file to insert multiple Person records into the database at once.",
            tags = {"People", "File"},
            responses = {
                    @ApiResponse(
                            description = "Success - Records created successfully",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))
                            }),
                    @ApiResponse(
                            description = "Bad Request - The uploaded file is empty or the format is unsupported.",
                            responseCode = "400",
                            content = @Content),
                    @ApiResponse(
                            description = "Unauthorized - Authentication is required to access this endpoint.",
                            responseCode = "401",
                            content = @Content),
                    @ApiResponse(
                            description = "Unprocessable Entity - The file structure is correct, but the data is invalid (e.g., column length exceeded).",
                            responseCode = "422",
                            content = @Content),
                    @ApiResponse(
                            description = "Internal Server Error - An unexpected error occurred while processing the file.",
                            responseCode = "500",
                            content = @Content),
            }
    )
    public List<PersonDTO> massiveCreatePeople(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new RequiredObjectIsNullException("File cannot be empty");

        List<PersonDTO> people = personServices.massCreation(file);

        // Se a lista estiver vazia, o retorno ideal é 204 No Content
        if (people.isEmpty()) {
            return (List<PersonDTO>) ResponseEntity.noContent().build().getBody();
        }

        return ResponseEntity.ok(people).getBody();
    }

    @GetMapping(value = "/generateExportPage",
            produces = {
                        MediaTypes.APPLICATION_XLSX_VALUE,
                        MediaTypes.TEXT_CSV_VALUE,
                        MediaTypes.APPLICATION_PDF_VALUE})
    @Operation(
            summary = "Exportar lista de pessoas",
            description = "Gera um arquivo (XLSX ou CSV) contendo a lista de pessoas paginada e ordenada.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Arquivo gerado com sucesso",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaTypes.APPLICATION_XLSX_VALUE, schema = @Schema(type = "string", format = "binary")),
                                    @Content(mediaType = MediaTypes.TEXT_CSV_VALUE, schema = @Schema(type = "string", format = "binary"))
                            }),
                    @ApiResponse(description = "Requisição inválida", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Não autorizado", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Erro interno no servidor", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<Resource> generateExportPage(
            @Parameter(description = "Número da página (0..N)")
            @RequestParam(value = "page", defaultValue = "0") Integer page,

            @Parameter(description = "Quantidade de registros por página")
            @RequestParam(value = "size", defaultValue = "15") Integer size,

            @Parameter(description = "Direção da ordenação (asc ou desc)")
            @RequestParam(value = "direction", defaultValue = "asc") String direction,

            @Parameter(description = "Atributo pelo qual ordenar")
            @RequestParam(value = "properties", defaultValue = "firstName") String properties,

            HttpServletRequest request
    ) throws Exception {

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, properties));

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        Resource resource = personServices.generateExportPage(pageable, acceptHeader);

        var contentType = (acceptHeader != null && !acceptHeader.contains("*/*")) ? acceptHeader : MediaTypes.APPLICATION_XLSX_VALUE;

        var fileExtension = MediaTypes.APPLICATION_XLSX_VALUE.equalsIgnoreCase(contentType) ? ".xlsx" : ".csv";
        var fileName = "people_exported_" + System.currentTimeMillis() + fileExtension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }


    //adicionado informacoes no request
    @GetMapping(value = "/findByName/{name}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find a list of All Person in Database",
            description = "Find a list of All Person in Database",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success - Person found",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))
                            }),
                    @ApiResponse(
                            description = "Not Found - The person with the provided ID does not exist.",
                            responseCode = "404",
                            content = @Content),
                    @ApiResponse(
                            description = "Bad Request - The provided ID is invalid or malformed.",
                            responseCode = "400",
                            content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPersonByName(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "15") Integer size,
                                                   @RequestParam(value = "direction", defaultValue = "asc") String direction,
                                                   @RequestParam(value = "properties", defaultValue = "firstName") String properties,
                                                   @PathVariable(name = "name") String firstName) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        //criando paginacao, pagerequest.of monta o pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, properties) );
        PagedModel<EntityModel<PersonDTO>> people = personServices.findPersonByName(firstName, pageable);

        if (people.getContent().isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(people);
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new Person",
            description = "Creates a new person in the database with the provided information. Returns the created person with the generated ID.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Created - Person successfully created",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class))
                            }),
                    @ApiResponse(
                            description = "Bad Request - Invalid or incomplete data provided.",
                            responseCode = "400",
                            content = @Content),
                    @ApiResponse(
                            description = "Unauthorized - User not authenticated.",
                            responseCode = "401",
                            content = @Content),
                    @ApiResponse(
                            description = "Unprocessable Entity - Business rules validation failed.",
                            responseCode = "422",
                            content = @Content),
                    @ApiResponse(
                            description = "Internal Server Error - An unexpected error occurred.",
                            responseCode = "500",
                            content = @Content)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody()
    )
    public ResponseEntity<PersonDTO> createV1(@RequestBody PersonDTO person){
        return ResponseEntity.ok().body(personServices.createV1(person));
    }

    @PutMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update an existing Person",
            description = "Updates the information of an existing person in the database. All fields in the request body will update the corresponding person record.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success - Person successfully updated",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PersonDTO.class))
                            }),
                    @ApiResponse(
                            description = "Bad Request - Invalid data format or missing required fields.",
                            responseCode = "400",
                            content = @Content),
                    @ApiResponse(
                            description = "Unauthorized - User not authenticated.",
                            responseCode = "401",
                            content = @Content),
                    @ApiResponse(
                            description = "Not Found - The person with the provided ID does not exist.",
                            responseCode = "404",
                            content = @Content),
                    @ApiResponse(
                            description = "Unprocessable Entity - Business rules validation failed.",
                            responseCode = "422",
                            content = @Content),
                    @ApiResponse(
                            description = "Internal Server Error - An unexpected error occurred.",
                            responseCode = "500",
                            content = @Content)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody()
    )
    public ResponseEntity<PersonDTO> update(@RequestBody PersonDTO person){
        return ResponseEntity.ok().body(personServices.updating(person));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete a Person by ID",
            description = "Permanently removes a person from the database using their unique ID. This operation cannot be undone.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "No Content - Person successfully deleted",
                            responseCode = "204",
                            content = @Content),
                    @ApiResponse(
                            description = "Bad Request - The provided ID is invalid or malformed.",
                            responseCode = "400",
                            content = @Content),
                    @ApiResponse(
                            description = "Unauthorized - User not authenticated.",
                            responseCode = "401",
                            content = @Content),
                    @ApiResponse(
                            description = "Not Found - The person with the provided ID does not exist.",
                            responseCode = "404",
                            content = @Content),
                    @ApiResponse(
                            description = "Internal Server Error - An unexpected error occurred.",
                            responseCode = "500",
                            content = @Content)
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID of the Person to be deleted (String format).",
                            required = true,
                            example = "1"
                    )
            }
    )
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id){
        //tetse
        personServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE})
    @Operation(
            summary = "Disable a Person by ID",
            description = "Permanently removes a person from the database using their unique ID. This operation cannot be undone.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "No Content - Person successfully disable",
                            responseCode = "204",
                            content = @Content),
                    @ApiResponse(
                            description = "Bad Request - The provided ID is invalid or malformed.",
                            responseCode = "400",
                            content = @Content),
                    @ApiResponse(
                            description = "Unauthorized - User not authenticated.",
                            responseCode = "401",
                            content = @Content),
                    @ApiResponse(
                            description = "Not Found - The person with the provided ID does not exist.",
                            responseCode = "404",
                            content = @Content),
                    @ApiResponse(
                            description = "Internal Server Error - An unexpected error occurred.",
                            responseCode = "500",
                            content = @Content)
            },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID of the Person to be deleted (String format).",
                            required = true,
                            example = "1"
                    )
            }
    )
    public ResponseEntity<PersonDTO> disablePersonById(@PathVariable(name = "id") String id){
        return ResponseEntity.ok().body(personServices.disablePersonId(id));
    }

}
