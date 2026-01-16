package br.com.jzbreno.controllers;

import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.DTO.PersonDTO2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PersonControllerV2Doc {
    @Operation(
            summary = "Find a Person by ID",
            description = "Returns the details of a specific person, including contact information, using their unique ID.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success - Person found",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = PersonDTO.class)
                                    ),
                                    @Content(
                                            mediaType = MediaType.APPLICATION_XML_VALUE,
                                            schema = @Schema(implementation = PersonDTO.class)
                                    ),
                                    @Content(
                                            mediaType = "application/x-yaml",
                                            schema = @Schema(implementation = PersonDTO.class)
                                    )
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
    ResponseEntity<EntityModel<PersonDTO2>> findByIdV2(@PathVariable(name = "id") String id);

    @Operation(
            summary = "Find a list of All Person in Database",
            description = "Find a list of All Person in Database",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success - Person found",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                    ),
                                    @Content(
                                            mediaType = MediaType.APPLICATION_XML_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                    ),
                                    @Content(
                                            mediaType = "application/x-yaml",
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                    )
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
    ResponseEntity<PagedModel<EntityModel<PersonDTO2>>> findAllV2(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "size", defaultValue = "15") Integer size,
                                                                 @RequestParam(name = "direction", defaultValue = "firstName") String direction,
                                                                 @RequestParam(name = "sort", defaultValue = "asc") String properties);

    @Operation(
            summary = "Create a new Person",
            description = "Creates a new person in the database with the provided information. Returns the created person with the generated ID.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Created - Person successfully created",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = PersonDTO.class)
                                    ),
                                    @Content(
                                            mediaType = MediaType.APPLICATION_XML_VALUE,
                                            schema = @Schema(implementation = PersonDTO.class)
                                    ),
                                    @Content(
                                            mediaType = "application/x-yaml",
                                            schema = @Schema(implementation = PersonDTO.class)
                                    )
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
    ResponseEntity<PersonDTO2> createV2(@RequestBody PersonDTO2 person);

    @Operation(
            summary = "Update an existing Person",
            description = "Updates the information of an existing person in the database. All fields in the request body will update the corresponding person record.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success - Person successfully updated",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = PersonDTO.class)
                                    ),
                                    @Content(
                                            mediaType = MediaType.APPLICATION_XML_VALUE,
                                            schema = @Schema(implementation = PersonDTO.class)
                                    ),
                                    @Content(
                                            mediaType = "application/x-yaml",
                                            schema = @Schema(implementation = PersonDTO.class)
                                    )
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
    ResponseEntity<PersonDTO2> update(@RequestBody PersonDTO2 person);

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
    ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id);

    void teste();
}
