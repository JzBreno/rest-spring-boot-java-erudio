package br.com.jzbreno.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PcDTO implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id;
    @JsonProperty("video_card")
    private String videoCard;
    @JsonProperty("cpu")
    private String processor;
    @JsonProperty("ramMemory")
    private String ramMemory;
    @JsonProperty("storage_unit")
    private String storageUnit;
}
