package com.fiap.posTube.service;

import com.fiap.posTube.repository.VideoRepository;
import com.fiap.posTube.useCase.DTO.VideoDTO;
import com.fiap.posTube.useCase.entity.Category;
import com.fiap.posTube.useCase.entity.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoService videoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ShouldSaveVideo() {
        VideoDTO videoDTO = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);

        Mockito.when(videoRepository.save((Video) Mockito.any())).thenReturn(Mono.just(new Video(videoDTO)));

        Mono<Video> savedVideoMono = videoService.saveVideo(videoDTO);

        Video savedVideo = savedVideoMono.block();

        assertThat(savedVideo).isNotNull();
        assertThat(savedVideo.getTitle()).isEqualTo("Video Title");
        assertThat(savedVideo.getDescription()).isEqualTo("Video Description");
        assertThat(savedVideo.getCategory()).isEqualTo( Category.COMEDIA);
    }

    @Test
    void ShouldGetVideoById() {
        VideoDTO videoDTO = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);
        Video video = new Video(videoDTO);
        video.setId("65a00e098fb0445966684db7");
        video.setPublicationDate(java.time.LocalDateTime.now());

        Mockito.when(videoRepository.getById(video.getId())).thenReturn(Mono.just(Optional.of(video)));
        Mono<Video> retrievedVideoMono = videoService.getVideoById(video.getId());
        Video retrievedVideo = retrievedVideoMono.block();

        assertThat(retrievedVideo).isNotNull();
        assertThat(retrievedVideo.getId()).isEqualTo(video.getId());
        assertThat(retrievedVideo.getTitle()).isEqualTo(video.getTitle());
        assertThat(retrievedVideo.getDescription()).isEqualTo(video.getDescription());
        assertThat(retrievedVideo.getCategory()).isEqualTo(video.getCategory());
    }

    @Test
    void ShouldUpdateVideo() {
        VideoDTO videoDTO = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);
        Video video = new Video(videoDTO);
        video.setId("65a00e098fb0445966684db7");
        video.setPublicationDate(java.time.LocalDateTime.now());

        Mockito.when(videoRepository.getById(video.getId())).thenReturn(Mono.just(Optional.of(video)));
        Mockito.when(videoRepository.save((Video) Mockito.any())).thenReturn(Mono.just(new Video(videoDTO)));

        Mono<Video> updatedVideoMono = videoService.updateVideo(videoDTO, video.getId());

        Video updatedVideo = updatedVideoMono.block();

        assertThat(updatedVideo).isNotNull();
        assertThat(updatedVideo.getTitle()).isEqualTo(video.getTitle());
        assertThat(updatedVideo.getDescription()).isEqualTo(video.getDescription());
        assertThat(updatedVideo.getCategory()).isEqualTo(video.getCategory());
    }

    @Test
    void ShouldGetAllVideos() {
        VideoDTO videoDTO = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);
        Video video = new Video(videoDTO);
        video.setId("65a00e098fb0445966684db7");
        video.setPublicationDate(java.time.LocalDateTime.now());

        Mockito.when(videoRepository.getAll()).thenReturn(Flux.just(video));

        Flux<Video> allVideosFlux = videoService.getAllVideos();

        Video retrievedVideo = allVideosFlux.blockFirst();

        assertThat(retrievedVideo).isNotNull();
        assertThat(retrievedVideo.getTitle()).isEqualTo(video.getTitle());
        assertThat(retrievedVideo.getDescription()).isEqualTo(video.getDescription());
        assertThat(retrievedVideo.getCategory()).isEqualTo(video.getCategory());
    }

    @Test
    void ShouldFindAllVideos() {
        int page = 0;
        int size = 10;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("title")));

        VideoDTO videoDTO = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);
        Video video = new Video(videoDTO);

        Mockito.when(videoRepository.findAllBy(pageable)).thenReturn(Mono.just(new PageImpl<>(Collections.singletonList(video))));

        Mono<Page<Video>> allVideosPageMono = videoService.findAllVideos(pageable);

        Page<Video> allVideosPage = allVideosPageMono.block();

        assertThat(allVideosPage).isNotNull();
        assertThat(allVideosPage.getTotalElements()).isEqualTo(1);
        assertThat(allVideosPage.getContent()).hasSize(1);
        assertThat(allVideosPage.getContent().get(0).getTitle()).isEqualTo(video.getTitle());
        assertThat(allVideosPage.getContent().get(0).getDescription()).isEqualTo(video.getDescription());
        assertThat(allVideosPage.getContent().get(0).getCategory()).isEqualTo(video.getCategory());
    }

    @Test
    void ShouldDeleteVideo() {
        String videoId = "65a00e098fb0445966684db7";

        Mockito.when(videoRepository.deleteById(videoId)).thenReturn(Mono.empty());

        Mono<Void> deleteResultMono = videoService.deleteVideo(videoId);

        assertThat(deleteResultMono).isNotNull();
    }

    @Test
    void ShouldListByCategory() {
        Category category = Category.ACAO;

        VideoDTO videoDTO = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.ACAO);
        Video video = new Video(videoDTO);

        Mockito.when(videoRepository.listByCategory(category)).thenReturn(Flux.just(video));

        Flux<Video> videosByCategoryFlux = videoService.listByCategory(category);

        Video retrievedVideo = videosByCategoryFlux.blockFirst();

        assertThat(retrievedVideo).isNotNull();
        assertThat(retrievedVideo.getTitle()).isEqualTo(video.getTitle());
        assertThat(retrievedVideo.getDescription()).isEqualTo(video.getDescription());
        assertThat(retrievedVideo.getCategory()).isEqualTo(video.getCategory());
    }
}
