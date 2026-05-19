package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.RequiredObjectIsNullException;
import br.com.jzbreno.Exceptions.ResourceNotFoundException;
import br.com.jzbreno.controllers.VeiculoController;
import br.com.jzbreno.mapper.VeiculoMapper;
import br.com.jzbreno.model.DTO.VeiculoDTO;
import br.com.jzbreno.model.Veiculo;
import br.com.jzbreno.repository.VeiculoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Slf4j
public class VeiculoServices {

    private final VeiculoRepository veiculoRepository;
    private final VeiculoMapper veiculoMapper;

    public VeiculoServices(VeiculoRepository veiculoRepository, VeiculoMapper veiculoMapper) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoMapper = veiculoMapper;
    }

    public VeiculoDTO findById(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado para o id :: " + id));
        VeiculoDTO veiculoDTO = veiculoMapper.parseVeiculoToVeiculoDTO(veiculo);
        implementsHateoasVeiculo(veiculoDTO);
        return veiculoDTO;
    }

    public List<VeiculoDTO> findAll() {
        List<VeiculoDTO> veiculos = veiculoMapper.parseVeiculoListToVeiculoDTOList(veiculoRepository.findAll());
        implementsHateoasVeiculo(veiculos);
        return veiculos;
    }

    public VeiculoDTO create(VeiculoDTO veiculoDTO) {
        validateVeiculoDTO(veiculoDTO);
        Veiculo veiculoSalvo = veiculoRepository.save(veiculoMapper.parseVeiculoDTOToVeiculo(veiculoDTO));
        VeiculoDTO veiculoCriado = veiculoMapper.parseVeiculoToVeiculoDTO(veiculoSalvo);
        implementsHateoasVeiculo(veiculoCriado);
        return veiculoCriado;
    }

    public VeiculoDTO updating(VeiculoDTO veiculoDTO) {
        validateVeiculoDTO(veiculoDTO);
        if (veiculoDTO.getId() == null) {
            throw new RequiredObjectIsNullException();
        }
        VeiculoDTO veiculoAtualizado = findById(veiculoDTO.getId());
        veiculoAtualizado.setPlaca(veiculoDTO.getPlaca());
        veiculoAtualizado.setMarca(veiculoDTO.getMarca());
        veiculoAtualizado.setModelo(veiculoDTO.getModelo());
        veiculoAtualizado.setAno(veiculoDTO.getAno());
        veiculoAtualizado.setCor(veiculoDTO.getCor());
        Veiculo veiculoSalvo = veiculoRepository.save(veiculoMapper.parseVeiculoDTOToVeiculo(veiculoAtualizado));
        VeiculoDTO resultado = veiculoMapper.parseVeiculoToVeiculoDTO(veiculoSalvo);
        implementsHateoasVeiculo(resultado);
        return resultado;
    }

    public void deleteById(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veículo não encontrado para o id :: " + id);
        }
        veiculoRepository.deleteById(id);
    }

    private void validateVeiculoDTO(VeiculoDTO veiculoDTO) {
        if (veiculoDTO == null
                || veiculoDTO.getPlaca() == null || veiculoDTO.getPlaca().isBlank()
                || veiculoDTO.getMarca() == null || veiculoDTO.getMarca().isBlank()
                || veiculoDTO.getModelo() == null || veiculoDTO.getModelo().isBlank()
                || veiculoDTO.getAno() == null || veiculoDTO.getAno().isBlank()
                || veiculoDTO.getCor() == null || veiculoDTO.getCor().isBlank()) {
            throw new RequiredObjectIsNullException();
        }
    }

    private static void implementsHateoasVeiculo(VeiculoDTO veiculoDTO) {
        if (veiculoDTO == null || veiculoDTO.getId() == null) {
            return;
        }
        veiculoDTO.add(linkTo(methodOn(VeiculoController.class).getVeiculoById(String.valueOf(veiculoDTO.getId()))).withSelfRel().withType("GET"));
        veiculoDTO.add(linkTo(methodOn(VeiculoController.class).findAllVeiculos()).withRel("findAllVeiculos").withType("GET"));
        veiculoDTO.add(linkTo(methodOn(VeiculoController.class).deleteVeiculo(String.valueOf(veiculoDTO.getId()))).withRel("deleteVeiculo").withType("DELETE"));
        veiculoDTO.add(linkTo(methodOn(VeiculoController.class).createVeiculo(veiculoDTO)).withRel("createVeiculo").withType("POST"));
        veiculoDTO.add(linkTo(methodOn(VeiculoController.class).updateVeiculo(veiculoDTO)).withRel("updateVeiculo").withType("PUT"));
    }

    private static void implementsHateoasVeiculo(List<VeiculoDTO> veiculoDTOList) {
        veiculoDTOList.forEach(VeiculoServices::implementsHateoasVeiculo);
    }
}
