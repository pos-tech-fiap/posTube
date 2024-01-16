package com.fiap.posTube.repository;

import com.fiap.posTube.useCase.entity.Category;
import com.fiap.posTube.useCase.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public class VideoRepository {
    @Autowired
    IVideoRepository repository;

    public Mono<Video> save(Video video) {
        return repository.save(video)
                .onErrorMap(e -> new RuntimeException("Failed to save video", e));
    }

    public Mono<Optional<Video>> getById(String videoID) {
        return repository.findById(videoID)
                .map(Optional::ofNullable)
                .switchIfEmpty(Mono.error(new RuntimeException("Video not found")));
    }

    public Mono<Void> deleteById(String videoID) {
        return repository.deleteById(videoID)
                .then()
                .doOnError(error -> {
                    error.printStackTrace();
                });
    }

    public Flux<Video> getAll() {
        return repository.findAll();
    }
    public Mono<Page<Video>> findAllBy(Pageable pageable) {
        return this.repository.findAllBy(pageable)
                .collectList()
                .zipWith(this.repository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }
    public Mono<Video> save(Mono<Object> videoToSave) {
        return videoToSave.flatMap(video -> repository.save((Video) video)
                .onErrorMap(e -> new RuntimeException("Failed to save video", e)));
    }

    public Flux<Video> listByCategory(Category category) {
        return repository.findByCategory(category);
    }


}
