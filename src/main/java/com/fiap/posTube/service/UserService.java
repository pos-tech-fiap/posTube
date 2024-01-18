package com.fiap.posTube.service;

import com.fiap.posTube.repository.UserRepository;
import com.fiap.posTube.repository.VideoRepository;
import com.fiap.posTube.useCase.DTO.UserDTO;
import com.fiap.posTube.useCase.entity.FavoriteVideos;
import com.fiap.posTube.useCase.entity.User;
import com.fiap.posTube.useCase.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    VideoRepository videoRepository;

    public Mono<User> saveUser(String userDTO) {
        User user = new User(userDTO);
        return userRepository.save(user).onErrorMap(e -> new RuntimeException("Failed to save video", e));
    }

    public Mono<User> getUserById(String userId) {
        return userRepository.getById(userId).switchIfEmpty(Mono.error(new RuntimeException("Video not found with ID: " + userId))).flatMap(user -> Mono.justOrEmpty(user.orElse(null)));
    }

    public Mono<User> saveFavorite(String userId, String videoId) {
        var userFound = userRepository.getById(userId).switchIfEmpty(Mono.error(new RuntimeException("Video not found with ID: " + userId))).flatMap(user -> Mono.justOrEmpty(user.orElse(null)));
        var favorite = new FavoriteVideos(videoId);
        return userFound
                .flatMap(userSet -> {
                    userSet.addFavorites(favorite);
                    return userRepository.save(userSet)
                            .onErrorMap(e -> new RuntimeException("Failed to save user", e))
                            .thenReturn(userSet);
                });
    }

    public Mono<Video> getRecommendedVideosById(String userId) {
        var userFound = userRepository.getById(userId).switchIfEmpty(Mono.error(new RuntimeException("Video not found with ID: " + userId))).flatMap(user -> Mono.justOrEmpty(user.orElse(null)));
        var videoFound = userFound
                .flatMap(userSet -> {
                    var video1 = userSet.getFavorites().get(1);
                    var videoId = video1.getVideoId();
                    return videoRepository.getById(videoId).switchIfEmpty(Mono.error(new RuntimeException("Video not found with ID: " + videoId))).flatMap(video -> Mono.justOrEmpty(video.orElse(null)));
                });
        return videoFound;
    }
    public Mono<Long> getTotalFavoritesCount() {
        return userRepository.getTotalFavoritesCount();
    }

}
