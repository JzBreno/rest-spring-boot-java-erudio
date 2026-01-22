package br.com.jzbreno.controllers;

import br.com.jzbreno.model.DTO.PcDTO;
import br.com.jzbreno.services.PcServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/pc")
@Tag(name = "API REST PC", description = "Enpoint for managing Computers, version 1")
public class PcController {

    private final PcServices pcServices;

    public PcController(PcServices pcServices) {
        this.pcServices = pcServices;
    }

    @GetMapping(value = "/{id}",
            produces ={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE })
    @Operation(
            summary = "find a config of computer searching by id",
            description = "Search for the relevant computer using an ID received from the client.",
            tags = {"Computer"},
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
                            description = "ID of the Computer to be Search (String format).",
                            required = true,
                            example = "1"
                    )
            }
    )
    public PcDTO findById(@PathVariable(name = "id") String id){
        log.info("Finding Pc by id : " + id);
        return pcServices.findById(id);
    }

    @GetMapping(value = "/findAll",
            produces ={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE })
    public ResponseEntity<Iterable<PcDTO>> findAll(){
        log.info("Finding all Pc");
        return ResponseEntity.ok().body(pcServices.findAll());
    }

    @PostMapping(
            consumes ={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE },
            produces ={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE })
    public ResponseEntity<PcDTO> create(@RequestBody PcDTO newComputer){
        return ResponseEntity.ok().body(pcServices.create(newComputer));
    }

    @PutMapping(value="/{id}",
            consumes ={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE },
            produces ={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE })
    public ResponseEntity<PcDTO> updating(@RequestBody PcDTO newComputer){
        return ResponseEntity.ok().body(pcServices.updating(newComputer));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id){
        pcServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
