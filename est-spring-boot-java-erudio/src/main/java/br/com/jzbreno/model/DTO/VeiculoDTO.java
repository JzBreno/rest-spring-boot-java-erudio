package br.com.jzbreno.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;

@JsonPropertyOrder({"id", "placa", "marca", "modelo", "ano", "cor"})
@Data
public class VeiculoDTO extends RepresentationModel<VeiculoDTO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("placa")
    private String placa;

    @JsonProperty("marca")
    private String marca;

    @JsonProperty("modelo")
    private String modelo;

    @JsonProperty("ano")
    private String ano;

    @JsonProperty("cor")
    private String cor;
}
