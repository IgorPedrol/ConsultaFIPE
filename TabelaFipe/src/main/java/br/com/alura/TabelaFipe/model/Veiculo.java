package br.com.alura.TabelaFipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Veiculo(
        @JsonAlias("price") String valor,
        @JsonAlias("brand") String marca,
        @JsonAlias("model") String model,
        @JsonAlias("modelYear") Integer ano,
        @JsonAlias("fuel") String tipoCombustivel) {
}
