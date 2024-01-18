package com.fiap.posTube.controller;


import com.fiap.posTube.service.UserService;
import com.fiap.posTube.service.VideoService;
import com.fiap.posTube.useCase.DTO.UserDTO;
import com.fiap.posTube.useCase.entity.User;
import com.fiap.posTube.useCase.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @PostMapping("/user")
    public Mono<ResponseEntity<User>> save(@RequestBody @Valid UserDTO user) {
        return userService.saveUser(user.name())
                .map(savedUser -> ResponseEntity.status(HttpStatus.CREATED).body(savedUser));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok().body(user))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/favorite/{userId}/{favoriteId}")
    public Mono<ResponseEntity<User>> saveFavorite(@PathVariable("userId") String userId, @PathVariable("favoriteId") String favoriteId) {
        return userService.saveFavorite(userId, favoriteId)
                .map(user -> ResponseEntity.ok().body(user))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("recommended-videos/{id}")
    public Flux<Video> recommendedVideosNew(@PathVariable("id") String id) {
        return userService.getRecommendedVideosById(id)
                .flatMapMany(video -> videoService.listByCategory(video.getCategory()));
    }
}
