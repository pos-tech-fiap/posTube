package com.fiap.posTube.service;

import com.fiap.posTube.useCase.DTO.VideoDTO;
import com.fiap.posTube.useCase.entity.Category;
import com.fiap.posTube.useCase.entity.Video;
import com.fiap.posTube.repository.VideoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VideoService {
    @Autowired
    VideoRepository videoRepository;

    public Mono<Video> saveVideo(VideoDTO videoDTO) {
        var video = new Video(videoDTO);
        video.setPublicationDate(java.time.LocalDateTime.now());

        return videoRepository.save(video).onErrorMap(e -> new RuntimeException("Failed to save video", e));
    }

    public Mono<Video> getVideoById(String videoId) {
        return videoRepository.getById(videoId).switchIfEmpty(Mono.error(new RuntimeException("Video not found with ID: " + videoId))).flatMap(video -> Mono.justOrEmpty(video.orElse(null)));
    }

    public Mono<Video> updateVideo(VideoDTO videoDTO, String videoId) {
        var videoFound = videoRepository.getById(videoId).switchIfEmpty(Mono.error(new RuntimeException("Video not found with ID: " + videoId))).flatMap(video -> Mono.justOrEmpty(video.orElse(null)));
        return videoFound
                .flatMap(myObject -> {
                    BeanUtils.copyProperties(videoDTO, myObject);
                    return videoRepository.save(myObject)
                            .onErrorMap(e -> new RuntimeException("Failed to save video", e))
                            .thenReturn(myObject);
                });
    }

    public Flux<Video> getAllVideos() {
        System.out.println("Fetching all videos from the database");
        return videoRepository.getAll().doOnNext(video -> System.out.println());
    }

    public Mono<Page<Video>> findAllVideos(Pageable pageable) {
        return videoRepository.findAllBy(pageable);
    }

    public Mono<Void> deleteVideo(String videoId) {
        return videoRepository.deleteById(videoId).then();
    }

    public Flux<Video> listByCategory(Category category) {
        return videoRepository.listByCategory(category);
    }
}
