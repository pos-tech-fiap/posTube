package com.fiap.posTube.useCase.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiap.posTube.useCase.entity.Category;

import javax.validation.constraints.NotBlank;

public record VideoDTO(
        @JsonProperty
        @NotBlank(message = "Title field is required!")
        String title,

        @JsonProperty
        @NotBlank(message = "Description field is required!")
        String description,

        @JsonProperty
        @NotBlank(message = "URL field is required!")
        String urlVideo,

        @JsonProperty
        Category category

) {
    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public Category category() {
        return category;
    }

}
