package com.fiap.posTube.useCase.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public record UserDTO(
        @JsonProperty
        @NotBlank(message = "Name field is required!")
        String name
){
    public @NotBlank String name() { return name;}

}