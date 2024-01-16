package com.fiap.posTube.controller;

import com.fiap.posTube.useCase.DTO.VideoDTO;
import com.fiap.posTube.useCase.entity.Category;
import com.fiap.posTube.useCase.entity.Video;
import com.fiap.posTube.service.VideoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(controllers = VideoController.class)
public class VideoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private VideoService videoService;

    @Test
    void ShouldSaveVideo() {
        VideoDTO videoDTO = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);
        Video video = new Video(videoDTO);
        video.setId("65a00e098fb0445966684db7");
        video.setPublicationDate(java.time.LocalDateTime.now());

        Mockito.when(videoService.saveVideo(videoDTO)).thenReturn(Mono.just(video));

        webTestClient.post().uri("/videos/video")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(videoDTO))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Video.class)
                .value(videoReturn -> assertThat(videoReturn.getTitle()).isEqualTo("Video Title"));
    }

    @Test
    void ShouldListAllVideos() {
        VideoDTO videoDTO1 = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);
        VideoDTO videoDTO2 = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);

        List<Video> videoList = Arrays.asList(
                new Video(videoDTO1),
                new Video(videoDTO2));

        Mockito.when(videoService.getAllVideos()).thenReturn(Flux.fromIterable(videoList));

        webTestClient.get().uri("/videos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Video.class)
                .value(videos -> assertThat(videos).hasSize(2));
    }

//    @Test
//    void testGetAllVideosPaginated() {
//        int page = 0;
//        int size = 10;
//
//        Page<Video> videoPage = Mockito.mock(Page.class);
//
//        Mockito.when(videoService.findAllVideos(Mockito.any())).thenReturn(Mono.just(videoPage));
//
//        webTestClient.get().uri("/videos/order-publication-date?page={page}&size={size}", page, size)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(Page.class);
//    }

//    @Test
//    void testGetAllVideosPaginatedNew() {
//        int page = 0;
//        int size = 10;
//
//        Page<Video> videoPage = Mockito.mock(Page.class);
//
//        Mockito.when(videoService.findAllVideos(Mockito.any())).thenReturn(Mono.just(videoPage));
//
//        webTestClient.get().uri("/videos/order-title?page={page}&size={size}", page, size)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(Page.class);
//    }

    @Test
    void ShouldGetVideoById() {
        VideoDTO videoDTO = new VideoDTO("Video Title", "Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);
        Video video = new Video(videoDTO);
        video.setId("65a00e098fb0445966684db7");
        video.setPublicationDate(java.time.LocalDateTime.now());

        Mockito.when(videoService.getVideoById(video.getId())).thenReturn(Mono.just(video));

        webTestClient.get().uri("/videos/{id}", video.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Video.class)
                .value(videoReturn -> assertThat(videoReturn.getId()).isEqualTo(video.getId()));
    }

    @Test
    void ShouldUpdateVideo() {
        VideoDTO videoDTO = new VideoDTO("Update Video Title", "Update Video Description", "https://www.youtube.com/watch?v=b2TFUr5MR2E&list=PL8iIphQOyG-CyD9uuRTMiqxEut5QAKHga&index=6", Category.COMEDIA);
        Video video = new Video(videoDTO);
        video.setId("65a00e098fb0445966684db7");
        video.setPublicationDate(java.time.LocalDateTime.now());

        Mockito.when(videoService.getVideoById(video.getId())).thenReturn(Mono.just(video));

        Mockito.when(videoService.updateVideo(videoDTO, video.getId())).thenReturn(Mono.just(video));

        webTestClient.put().uri("/videos/{id}", video.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(videoDTO))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Video.class)
                .value(updatedVideo -> {
                    assertThat(updatedVideo.getId()).isEqualTo(video.getId());
                    assertThat(updatedVideo.getTitle()).isEqualTo("Update Video Title");
                    assertThat(updatedVideo.getDescription()).isEqualTo("Update Video Description");
                    assertThat(updatedVideo.getCategory()).isEqualTo(Category.COMEDIA);
                });
    }

    @Test
    void ShouldDeleteVideoById() {
        String videoId = "65a00e098fb0445966684db7";

        Mockito.when(videoService.deleteVideo(videoId)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/videos/video/{id}", videoId)
                .exchange()
                .expectStatus().isNoContent();
    }
}

