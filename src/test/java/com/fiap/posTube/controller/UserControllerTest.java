package com.fiap.posTube.controller;

import com.fiap.posTube.service.UserService;
import com.fiap.posTube.service.VideoService;
import com.fiap.posTube.useCase.DTO.UserDTO;
import com.fiap.posTube.useCase.DTO.VideoDTO;
import com.fiap.posTube.useCase.entity.FavoriteVideos;
import com.fiap.posTube.useCase.entity.User;
import com.fiap.posTube.useCase.entity.Video;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.fiap.posTube.useCase.entity.Category.COMEDIA;

@WebFluxTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @MockBean
    private VideoService videoService;

    @Test
    void ShouldSaveNewUser() {
        UserDTO userdto = new UserDTO("John Doe");
        User userToSaveReq = new User(userdto.name());
        User userToSave = new User(userdto.name());
        userToSave.setId("65a00f64b8c2c36b231e4903");
        FavoriteVideos favoriteVideos = new FavoriteVideos("65a00f64b8c2c36b231e4903");
        userToSave.setId("65a00f64b8c2c36b231e4903");
        userToSave.addFavorites(favoriteVideos);

        Mockito.when(userService.saveUser(userdto.name())).thenReturn(Mono.just(userToSave));

        webTestClient.post().uri("/users/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userToSave)
                .exchange()
                .expectBody()
                .returnResult();
    }

    @Test
    void ShouldGetUserById() {
        UserDTO userdto = new UserDTO("John Doe");
        User userToSaveReq = new User(userdto.name());
        User userToSave = new User(userdto.name());
        userToSave.setId("65a00f64b8c2c36b231e4903");
        userToSave.addFavorites(null);

        Mockito.when(userService.saveUser(userdto.name())).thenReturn(Mono.just(userToSave));
        webTestClient.get().uri("/users/{id}", userToSave.getId())
                .exchange()
                .expectBody()
                .returnResult()
                .getResponseBody();
    }

    @Test
    void ShouldSaveFavorite() {
        UserDTO userdto = new UserDTO("John Doe");
        User userToSaveReq = new User(userdto.name());
        User userToSave = new User(userdto.name());
        FavoriteVideos favoriteVideos = new FavoriteVideos("65a00f64b8c2c36b231e4903");
        userToSave.setId("65a00f64b8c2c36b231e4903");
        userToSave.addFavorites(favoriteVideos);

        Mockito.when(userService.saveFavorite(userToSaveReq.getId(), "65a0205217705a260c0cb517")).thenReturn(Mono.just(userToSave));

        webTestClient.post().uri("users/favorite/65a0205217705a260c0cb517/65a00f64b8c2c36b231e4903")
                .exchange()
                .expectBody()
                .returnResult();
    }

    @Test
    void ShouldRecommendedVideosNew() {
        UserDTO userdto = new UserDTO("John Doe");
        User user = new User(userdto.name());
        VideoDTO videoDTO = new VideoDTO("William teste", "Nesse vídeo utilizamos .", "https://www.youtube.com/watch?v=TpYFd2uAd4M", COMEDIA);
        Video recommendedVideo = new Video(videoDTO);

        Mockito.when(userService.getRecommendedVideosById(user.getId())).thenReturn(Mono.just(recommendedVideo));
        Mockito.when(videoService.listByCategory(recommendedVideo.getCategory())).thenReturn(Flux.just(recommendedVideo));

        webTestClient.get().uri("/users/recommended-videos/{id}", user.getId())
                .exchange()
                .expectBody()
                .returnResult();
    }
}
