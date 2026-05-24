package br.com.jzbreno.mapper;

import br.com.jzbreno.model.Veiculo;
import br.com.jzbreno.model.DTO.VeiculoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoMapper {

    public VeiculoDTO parseVeiculoToVeiculoDTO(Veiculo veiculo) {
        if (veiculo == null) {
            return null;
        }
        VeiculoDTO veiculoDTO = new VeiculoDTO();
        veiculoDTO.setId(veiculo.getId());
        veiculoDTO.setPlaca(veiculo.getPlaca());
        veiculoDTO.setMarca(veiculo.getMarca());
        veiculoDTO.setModelo(veiculo.getModelo());
        veiculoDTO.setAno(String.valueOf(veiculo.getAno()));
        veiculoDTO.setCor(veiculo.getCor());
        return veiculoDTO;
    }

    public Veiculo parseVeiculoDTOToVeiculo(VeiculoDTO veiculoDTO) {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(veiculoDTO.getId());
        veiculo.setPlaca(veiculoDTO.getPlaca());
        veiculo.setMarca(veiculoDTO.getMarca());
        veiculo.setModelo(veiculoDTO.getModelo());
        veiculo.setAno(Integer.parseInt(veiculoDTO.getAno()));
        veiculo.setCor(veiculoDTO.getCor());
        return veiculo;
    }

    public List<VeiculoDTO> parseVeiculoListToVeiculoDTOList(List<Veiculo> veiculos) {
        return veiculos.stream().map(this::parseVeiculoToVeiculoDTO).toList();
    }
}
