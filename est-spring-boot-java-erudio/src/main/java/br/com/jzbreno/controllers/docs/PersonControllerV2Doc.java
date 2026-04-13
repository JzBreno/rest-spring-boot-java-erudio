package br.com.jzbreno.controllers.docs;

import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.DTO.PersonDTO2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PersonControllerV2Doc {

    @Operation(summary = "Find a Person by ID", tags = {"People"})
    ResponseEntity<EntityModel<PersonDTO2>> findByIdV2(@PathVariable(name = "id") String id);

    @Operation(summary = "Find a list of All Person", tags = {"People"})
    ResponseEntity<PagedModel<EntityModel<PersonDTO2>>> findAllV2(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "15") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sort", defaultValue = "firstName") String properties);

    @Operation(summary = "Find a list of All Person", tags = {"People"})
    ResponseEntity<Resource> generateExportPage(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "15") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sort", defaultValue = "firstName") String properties,
            @RequestParam(name = "jasper", defaultValue = "no") String jasper,
            HttpServletRequest request) throws Exception;

    @Operation(summary = "Find persons by name", tags = {"People"})
    ResponseEntity<PagedModel<EntityModel<PersonDTO2>>> findPersonByName(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "properties", defaultValue = "firstName") String properties,
            @PathVariable(name = "firstName") String firstName); // Corrigido para firstName

    @Operation(summary = "Create a new Person", tags = {"People"})
    ResponseEntity<EntityModel<PersonDTO2>> createV2(@RequestBody PersonDTO2 person);

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
            })
    List<PersonDTO2> massiveCreatre(@RequestParam("file")MultipartFile file);

    @Operation(summary = "Update an existing Person", tags = {"People"})
    ResponseEntity<PersonDTO2> update(@RequestBody PersonDTO2 person);

    @Operation(summary = "Delete a Person by ID", tags = {"People"})
    ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id);

    void teste();
}