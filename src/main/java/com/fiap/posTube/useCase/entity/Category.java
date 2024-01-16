package com.fiap.posTube.useCase.entity;

public enum Category {
    COMEDIA("Comédia"),
    DRAMA("Drama"),
    ACAO("Ação"),
    DOCUMENTARIO("Documentário"),
    MUSICAL("Musical"),
    TERROR("Terror");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
