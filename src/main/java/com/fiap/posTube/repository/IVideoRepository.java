package com.fiap.posTube.repository;

import com.fiap.posTube.useCase.entity.Category;
import com.fiap.posTube.useCase.entity.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IVideoRepository extends ReactiveMongoRepository<Video, String> {
    Mono<Video> save(Video video);
    Flux<Video> findAllBy(Pageable pagination);
    Flux<Video> findByCategory(Category category);
}
