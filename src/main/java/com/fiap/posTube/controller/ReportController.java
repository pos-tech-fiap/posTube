package com.fiap.posTube.controller;

import com.fiap.posTube.useCase.DTO.ReportDTO;
import com.fiap.posTube.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    ReportService reportService;
    @GetMapping
    public Mono<ReportDTO> getReport() {
        return reportService.getReport();
    }

}
