package br.com.jzbreno.controllers.docs;

import br.com.jzbreno.model.DTO.VeiculoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface VeiculoControllerDoc {

    @Operation(
            summary = "Buscar veículo por ID",
            description = "Retorna os detalhes de um veículo pelo identificador.",
            tags = {"Veiculos"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VeiculoDTO.class)),
                                    @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = VeiculoDTO.class)),
                                    @Content(mediaType = "application/x-yaml", schema = @Schema(implementation = VeiculoDTO.class))
                            }),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            },
            parameters = {
                    @Parameter(name = "id", description = "ID do veículo", required = true, example = "1")
            }
    )
    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"})
    ResponseEntity<VeiculoDTO> getVeiculoById(@PathVariable(name = "id") String id);

    @Operation(
            summary = "Listar todos os veículos",
            description = "Retorna a lista de veículos cadastrados.",
            tags = {"Veiculos"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = VeiculoDTO.class))),
                                    @Content(mediaType = MediaType.APPLICATION_XML_VALUE, array = @ArraySchema(schema = @Schema(implementation = VeiculoDTO.class))),
                                    @Content(mediaType = "application/x-yaml", array = @ArraySchema(schema = @Schema(implementation = VeiculoDTO.class)))
                            }),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    @GetMapping(value = "/findAll",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"})
    ResponseEntity<List<VeiculoDTO>> findAllVeiculos();

    @Operation(
            summary = "Cadastrar veículo",
            description = "Cadastra um novo veículo.",
            tags = {"Veiculos"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201", content = @Content(schema = @Schema(implementation = VeiculoDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            },
            requestBody = @RequestBody(content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VeiculoDTO.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = VeiculoDTO.class)),
                    @Content(mediaType = "application/x-yaml", schema = @Schema(implementation = VeiculoDTO.class))
            })
    )
    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, "application/x-yaml", MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"})
    ResponseEntity<VeiculoDTO> createVeiculo(@org.springframework.web.bind.annotation.RequestBody VeiculoDTO veiculoDTO);

    @Operation(
            summary = "Atualizar veículo",
            description = "Atualiza os dados de um veículo existente.",
            tags = {"Veiculos"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = VeiculoDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            },
            requestBody = @RequestBody(content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VeiculoDTO.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = VeiculoDTO.class)),
                    @Content(mediaType = "application/x-yaml", schema = @Schema(implementation = VeiculoDTO.class))
            })
    )
    @PutMapping(value = "/update",
            consumes = {MediaType.APPLICATION_JSON_VALUE, "application/x-yaml", MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "application/x-yaml"})
    ResponseEntity<VeiculoDTO> updateVeiculo(@org.springframework.web.bind.annotation.RequestBody VeiculoDTO veiculoDTO);

    @Operation(
            summary = "Excluir veículo por ID",
            description = "Remove um veículo do cadastro.",
            tags = {"Veiculos"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            },
            parameters = {
                    @Parameter(name = "id", description = "ID do veículo", required = true, example = "1")
            }
    )
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteVeiculo(@PathVariable(name = "id") String id);
}
