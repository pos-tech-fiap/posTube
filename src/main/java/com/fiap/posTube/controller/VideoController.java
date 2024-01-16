package com.fiap.posTube.controller;

import com.fiap.posTube.useCase.DTO.VideoDTO;
import com.fiap.posTube.useCase.entity.Category;
import com.fiap.posTube.useCase.entity.Video;
import com.fiap.posTube.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RequestMapping("/videos")
@RestController
public class VideoController {
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private VideoService videoService;

    @GetMapping(value = "/video/load/{title}", produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable("title") String title) {
        try {
            Resource resource = this.resourceLoader.getResource("classpath:video/" + title + ".mp4");
            return Mono.just(ResponseEntity.ok().body(resource).getBody());
        } catch (Exception e) {
            return Mono.just((Resource) ResponseEntity.notFound().build());
        }
    }

    @PostMapping("/video")
    public Mono<ResponseEntity<Video>> save(@RequestBody @Valid VideoDTO videoDTO) {
        return videoService.saveVideo(videoDTO)
                .map(savedVideo -> ResponseEntity.status(HttpStatus.CREATED).body(savedVideo));
    }

    @GetMapping
    public Flux<Video> listAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/order-publication-date")
    public Mono<Page<Video>> getAllVideosPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "publicationDate") String sortBy) {
        return videoService.findAllVideos(PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy))));
    }

    @GetMapping("/order-title")
    public Mono<Page<Video>> getAllVideosPaginatedNew(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy) {
        return videoService.findAllVideos(PageRequest.of(page, size, Sort.by(Sort.Order.asc(sortBy))));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Video>> getVideoById(@PathVariable("id") String id) {
        return videoService.getVideoById(id)
                .map(video -> ResponseEntity.ok().body(video))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Video>> updateVideo(@PathVariable("id") String id, @RequestBody @Valid VideoDTO videoDTO) {
        return videoService.updateVideo(videoDTO, id)
                .map(video -> ResponseEntity.ok().body(video))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/video/{id}")
    public Mono<ResponseEntity<Object>> deleteVideoById(@PathVariable("id") String id) {
        return videoService.deleteVideo(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @GetMapping("/category/{category}")
    public Flux<Video> lastVideoByCategory(@PathVariable Category category) {
        return videoService.listByCategory(category);
    }

}
