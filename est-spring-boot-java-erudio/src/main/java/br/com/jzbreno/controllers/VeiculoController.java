package br.com.jzbreno.controllers;

import br.com.jzbreno.controllers.docs.VeiculoControllerDoc;
import br.com.jzbreno.model.DTO.VeiculoDTO;
import br.com.jzbreno.services.VeiculoServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculo")
@Tag(name = "Veiculo", description = "Endpoints para cadastro e gestão de veículos")
public class VeiculoController implements VeiculoControllerDoc {

    private final VeiculoServices veiculoServices;

    public VeiculoController(VeiculoServices veiculoServices) {
        this.veiculoServices = veiculoServices;
    }

    @Override
    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<VeiculoDTO> getVeiculoById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(veiculoServices.findById(Long.valueOf(id)));
    }

    @Override
    @GetMapping(value = "/findAll",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<List<VeiculoDTO>> findAllVeiculos() {
        List<VeiculoDTO> veiculos = veiculoServices.findAll();
        return veiculos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(veiculos);
    }

    @Override
    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_YAML_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<VeiculoDTO> createVeiculo(@RequestBody VeiculoDTO veiculoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(veiculoServices.create(veiculoDTO));
    }

    @Override
    @PutMapping(value = "/update",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_YAML_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<VeiculoDTO> updateVeiculo(@RequestBody VeiculoDTO veiculoDTO) {
        return ResponseEntity.ok(veiculoServices.updating(veiculoDTO));
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteVeiculo(@PathVariable(name = "id") String id) {
        veiculoServices.deleteById(Long.valueOf(id));
        return ResponseEntity.noContent().build();
    }
}
