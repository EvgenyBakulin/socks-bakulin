package com.bakulinTasks.socks_bakulin.controller;

import com.bakulinTasks.socks_bakulin.dto.SocksDTO;
import com.bakulinTasks.socks_bakulin.service.SocksService;
import com.bakulinTasks.socks_bakulin.service.implementation.SockServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/socks/batch")
public class SocksExcelController {
    private final SocksService socksService;
    Logger log = LoggerFactory.getLogger(SocksExcelController.class);

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "Загрузить данные по партиям носок из Excel")
    public ResponseEntity<List<SocksDTO>> importOrganizationalUnits(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Эндпойнт выгрузки данных из файла Excel начал работу");
        return ResponseEntity.ok(socksService.readUsersFromExcel(file));
    }
}
