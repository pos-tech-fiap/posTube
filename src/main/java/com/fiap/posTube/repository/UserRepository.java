package com.fiap.posTube.repository;

import com.fiap.posTube.useCase.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public class UserRepository {
    @Autowired
    IUserRepository userRepository;

    public Mono<User> save(User user) {
        return userRepository.save(user)
                .onErrorMap(e -> new RuntimeException("Failed to save user", e));
    }

    public Mono<Optional<User>> getById(String userId) {
        return userRepository.findById(userId)
                .map(Optional::ofNullable)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }
    public Mono<Long> getTotalFavoritesCount() {
        return userRepository.countFavoritesByFavoritesVideoIdIsNotNull();
    }
}
