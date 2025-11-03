package br.com.jzbreno.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@JsonPropertyOrder({"id","title","author", "lauch_date", "price"})
@Data
public class BookDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("author")
    private String author;
    @JsonProperty("lauch_date")
    private String launch_date;
    @JsonProperty("price")
    private String price;
}
