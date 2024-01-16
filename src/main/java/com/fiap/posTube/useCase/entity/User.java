package com.fiap.posTube.useCase.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class User {
    @Id
    private String id;
    private String name;
    private List<FavoriteVideos> favorites;

    public User(String name) {

        this.name = name;
        this.favorites = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FavoriteVideos> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<FavoriteVideos> favorites) {
        this.favorites = favorites;
    }

    public void addFavorites(FavoriteVideos favorites) {
        this.favorites.add(favorites);
    }

}
