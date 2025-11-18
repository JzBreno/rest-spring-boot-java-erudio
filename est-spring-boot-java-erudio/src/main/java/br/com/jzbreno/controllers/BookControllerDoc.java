package br.com.jzbreno.controllers;

import br.com.jzbreno.model.DTO.BookDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BookControllerDoc {

    // --- GET /find/{id} (Original - Ajustando apenas a descrição do parâmetro) ---
    @Operation(
            summary = "Find a Book by ID",
            description = "Returns the details of a specific Book, using its unique ID.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Success - Book found",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = BookDTO.class)
                                    ),
                                    @Content(
                                            mediaType = MediaType.APPLICATION_XML_VALUE,
                                            schema = @Schema(implementation = BookDTO.class)
                                    ),
                                    @Content(
                                            mediaType = "application/x-yaml",
                                            schema = @Schema(implementation = BookDTO.class)
                                    )
                            }),
                    @ApiResponse(
                            description = "Not Found - The Book with the provided ID does not exist.",
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
                            description = "ID of the **Book** to be retrieved (String format).", // Ajustado o nome do objeto para 'Book'
                            required = true,
                            example = "1"
                    )
            }
    )
    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"})
    ResponseEntity<BookDTO> getBookByID(@PathVariable(name = "id") String id);

    // --- GET /findAll ---
    @Operation(
            summary = "Find all Books",
            description = "Returns a list of all recorded Books.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Success - List of Books found",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = BookDTO.class)) // Uso de ArraySchema para listas
                                    ),
                                    @Content(
                                            mediaType = MediaType.APPLICATION_XML_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
                                    ),
                                    @Content(
                                            mediaType = "application/x-yaml",
                                            array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    @GetMapping(value = "/findAll",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"})
    ResponseEntity<List<BookDTO>> findAllBooks();

    // --- POST / (saveBook) ---
    @Operation(
            summary = "Add a new Book",
            description = "Adds a new Book object by passing JSON, XML, or YAML representation of the Book.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Created - Book successfully added",
                            responseCode = "201", // 201 Created é mais apropriado para criação via POST
                            content = @Content(schema = @Schema(implementation = BookDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request - Invalid input data", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            },
            requestBody = @RequestBody(content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookDTO.class)
                    ),
                    @Content(
                            mediaType = MediaType.APPLICATION_XML_VALUE,
                            schema = @Schema(implementation = BookDTO.class)
                    ),
                    @Content(
                            mediaType = "application/x-yaml",
                            schema = @Schema(implementation = BookDTO.class)
                    )
            })
    )
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, "application/x-yaml", MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"})
    ResponseEntity<BookDTO> saveBook(@org.springframework.web.bind.annotation.RequestBody BookDTO bookDTO);

    // --- POST /create (createBook) ---
    // Note: Esta anotação é idêntica à anterior (saveBook). Mantenho a documentação similar.
    @Operation(
            summary = "Add a new Book (Alternative route)",
            description = "Adds a new Book object by passing JSON, XML, or YAML representation of the Book.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Created - Book successfully added",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = BookDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request - Invalid input data", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            },
            requestBody = @RequestBody(content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookDTO.class)
                    ),
                    @Content(
                            mediaType = MediaType.APPLICATION_XML_VALUE,
                            schema = @Schema(implementation = BookDTO.class)
                    ),
                    @Content(
                            mediaType = "application/x-yaml",
                            schema = @Schema(implementation = BookDTO.class)
                    )
            })
    )
    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, "application/x-yaml", MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"})
    ResponseEntity<BookDTO> createBook(@org.springframework.web.bind.annotation.RequestBody BookDTO bookDTO);


    // --- PUT /update ---
    @Operation(
            summary = "Update an existing Book",
            description = "Updates the details of an existing Book by passing JSON, XML, or YAML representation.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Success - Book successfully updated",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BookDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request - Invalid input data", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found - Book does not exist", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            },
            requestBody = @RequestBody(content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookDTO.class)
                    ),
                    @Content(
                            mediaType = MediaType.APPLICATION_XML_VALUE,
                            schema = @Schema(implementation = BookDTO.class)
                    ),
                    @Content(
                            mediaType = "application/x-yaml",
                            schema = @Schema(implementation = BookDTO.class)
                    )
            })
    )
    @PutMapping(value = "/update",
            consumes = {MediaType.APPLICATION_JSON_VALUE, "application/x-yaml", MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"})
    ResponseEntity<BookDTO> updateBook(@org.springframework.web.bind.annotation.RequestBody BookDTO bookDTO);

    // --- DELETE /{id} ---
    @Operation(
            summary = "Delete a Book by ID",
            description = "Deletes a specific Book using its unique ID.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "No Content - Book successfully deleted",
                            responseCode = "204", // 204 No Content é padrão para DELETE bem-sucedido
                            content = @Content),
                    @ApiResponse(
                            description = "Not Found - The Book with the provided ID does not exist.",
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
                            description = "ID of the Book to be deleted (String format).",
                            required = true,
                            example = "1"
                    )
            }
    )
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteBook(@PathVariable(name = "id") String id);
}