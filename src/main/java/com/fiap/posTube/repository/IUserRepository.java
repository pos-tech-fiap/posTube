package com.fiap.posTube.repository;

import com.fiap.posTube.useCase.entity.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface IUserRepository extends ReactiveMongoRepository<User, String> {
    @Aggregation(pipeline = {
            "{$unwind: '$favorites'}",
            "{$group: { _id: null, totalFavorites: { $sum: 1 } }}"
    })
    Mono<Long> countFavoritesByFavoritesVideoIdIsNotNull();

}
