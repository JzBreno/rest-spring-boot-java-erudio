package br.com.jzbreno.controllers;

import br.com.jzbreno.model.DTO.PcDTO;
import br.com.jzbreno.services.PcServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/pc")
public class PcController {

    private final PcServices pcServices;

    public PcController(PcServices pcServices) {
        this.pcServices = pcServices;
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PcDTO findById(@PathVariable(name = "id") String id){
        log.info("Finding Pc by id : " + id);
        return pcServices.findById(id);
    }

    @GetMapping(value = "/findAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<PcDTO>> findAll(){
        log.info("Finding all Pc");
        return ResponseEntity.ok().body(pcServices.findAll());
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PcDTO> create(@RequestBody PcDTO newComputer){
        return ResponseEntity.ok().body(pcServices.create(newComputer));
    }

    @PutMapping(value="/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PcDTO> updating(@RequestBody PcDTO newComputer){
        return ResponseEntity.ok().body(pcServices.updating(newComputer));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id){
        pcServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
