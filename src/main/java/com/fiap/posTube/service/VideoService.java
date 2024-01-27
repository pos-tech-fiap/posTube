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
        Mono<Video> videoFound = videoRepository.getById(videoId).switchIfEmpty(Mono.error(new RuntimeException("Video not found with ID: " + videoId))).flatMap(video -> Mono.justOrEmpty(video.orElse(null)));
        return videoFound.flatMap(this::updateViews);
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
        return videoRepository.getAll();
    }

    public Mono<Double> getAverageViews() {
        return videoRepository.getAll().collectList().map(videos -> {
            Double totalViews = 0.0;
            for (Video video : videos) {
                totalViews += video.getViews();
            }
            return totalViews / videos.size();
        });
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

    private Mono<Video> updateViews(Video video) {
        video.setViews(video.getViews() + 1);
        return videoRepository.save(video)
                .onErrorMap(e -> new RuntimeException("Failed to update views", e))
                .thenReturn(video);
    }
}
