package com.fiap.posTube.service;

import com.fiap.posTube.repository.UserRepository;
import com.fiap.posTube.repository.VideoRepository;
import com.fiap.posTube.useCase.DTO.VideoDTO;
import com.fiap.posTube.useCase.entity.Category;
import com.fiap.posTube.useCase.entity.FavoriteVideos;
import com.fiap.posTube.useCase.entity.User;
import com.fiap.posTube.useCase.entity.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ShouldSaveUser() {
        User user = new User("John Doe");
        user.setId("65a00f64b8c2c36b231e4903");

        Mockito.when(userRepository.save(user)).thenReturn(Mono.just(user));

        Mono<User> savedUserMono = userService.saveUser(user);

        User savedUser = savedUserMono.block();

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(user.getId());
        assertThat(savedUser.getName()).isEqualTo(user.getName());
    }

    @Test
    void ShouldGetUserById() {
        User user = new User("John Doe");
        user.setId("65a00f64b8c2c36b231e4903");

        Mockito.when(userRepository.getById(user.getId())).thenReturn(Mono.just(Optional.of(user)));
        Mono<User> userMono = userService.getUserById(user.getId());

        User retrievedUser = userMono.block();

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getId()).isEqualTo(user.getId());
        assertThat(retrievedUser.getName()).isEqualTo(user.getName());
    }

    @Test
    void ShouldSaveFavorite() {

        User user = new User("John Doe");
        user.setId("65a00f64b8c2c36b231e4903");

        VideoDTO videoDTO = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);
        Video video = new Video(videoDTO);
        video.setId("65a00e098fb0445966684db7");
        video.setPublicationDate(java.time.LocalDateTime.now());

        Mockito.when(userRepository.getById(user.getId())).thenReturn(Mono.just(Optional.of(user)));
        Mockito.when(videoRepository.getById(video.getId())).thenReturn(Mono.just(Optional.of(video)));
        Mockito.when(userRepository.save(user)).thenReturn(Mono.just(user));

        Mono<User> savedUserMono = userService.saveFavorite(user.getId(), video.getId());

        User savedUser = savedUserMono.block();

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(user.getId());
        assertThat(savedUser.getFavorites()).hasSize(1);
        assertThat(savedUser.getFavorites().get(0).getVideoId()).isEqualTo(video.getId());
    }

    @Test
    void ShouldGetRecommendedVideosById() {
        User user = new User("John Doe");
        user.setId("65a00f64b8c2c36b231e4903");

        VideoDTO videoDTO = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);
        Video video = new Video(videoDTO);
        video.setId("65a00e098fb0445966684db7");

        FavoriteVideos favoriteVideos = new FavoriteVideos(video.getId());
        user.setFavorites(Collections.singletonList(favoriteVideos));

        Mockito.when(userRepository.getById(user.getId())).thenReturn(Mono.just(Optional.of(user)));
        Mockito.when(videoRepository.getById(video.getId())).thenReturn(Mono.just(Optional.of(video)));

        Mono<Video> recommendedVideoMono = userService.getRecommendedVideosById(user.getId());

        assertThat(recommendedVideoMono).isNotNull();
    }

    @Test
    void ShouldGetTotalFavoritesCount() {
        long totalFavorites = 5L;

        Mockito.when(userRepository.getTotalFavoritesCount()).thenReturn(Mono.just(totalFavorites));

        Mono<Long> totalFavoritesMono = userService.getTotalFavoritesCount();

        Long result = totalFavoritesMono.block();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(totalFavorites);
    }
}