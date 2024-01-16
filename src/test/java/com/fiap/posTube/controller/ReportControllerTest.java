package com.fiap.posTube.controller;

import com.fiap.posTube.PosTubeApplication;
import com.fiap.posTube.service.ReportService;
import com.fiap.posTube.useCase.DTO.ReportDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(controllers = ReportController.class)
@SpringBootTest(classes = PosTubeApplication.class)
public class ReportControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReportService reportService;

    @Test
    void ShouldGetAllDataReport() {
        Mono<Long> totalDeVideos = Mono.just(100L);
        Mono<Long> totalFavoritesVideo = Mono.just(50L);
        int averageViews = 10;
        ReportDTO expectedReportDTO = new ReportDTO(totalDeVideos, totalFavoritesVideo, averageViews);
        Mockito.when(reportService.getReport()).thenReturn(Mono.just(expectedReportDTO));

        webTestClient.get().uri("/report")
                .exchange()
                .expectStatus();
    }
}
