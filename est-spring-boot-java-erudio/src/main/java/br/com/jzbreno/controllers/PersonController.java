package br.com.jzbreno.controllers;

import br.com.jzbreno.mapper.PersonMapper;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.DTO.PersonDTO2;
import br.com.jzbreno.services.PersonServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
@Tag(name = "API REST Person", description = "Enpoint for managing Persons, version 1")
public class PersonController{

//    @Autowired
//    private PersonServices personServicesAutowired;
//    assim seria a forma de injecao com anotacao, mas irei usar no construtor como boa pratica

    private final PersonServices personServices;

    public PersonController(PersonServices personServices, PersonMapper personMapper) {
        this.personServices = personServices;
    }

    //adicionado informacoes no request
    @GetMapping(value = "/v1/{id}",
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
    @GetMapping(value = "/v1/findAll",
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
            }
    )
    public ResponseEntity<List<PersonDTO>> findAll(){
        List<PersonDTO> people = personServices.findAll();
        if (people.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(people);
    }

    @PostMapping(value = "/v1",
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


}
