package com.bakulinTasks.socks_bakulin.controller;

import com.bakulinTasks.socks_bakulin.dto.SocksDTO;
import com.bakulinTasks.socks_bakulin.dto.SocksInfoDTO;
import com.bakulinTasks.socks_bakulin.service.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/socks")
public class SocksController {
    private final SocksService socksService;
    Logger log = LoggerFactory.getLogger(SocksController.class);

    @Operation(summary = "Добавить носки на склад")
    @PostMapping("/income")
    public ResponseEntity<SocksDTO> addSocksInStorage(@RequestBody SocksInfoDTO info) {
        log.info("Эндпойнт добавления данных в базу начал работу");
        return ResponseEntity.ok(socksService.addInStorage(info));
    }
    @Operation(summary = "Отпустить носки со склада")
    @PostMapping("/outcome")
    public ResponseEntity<SocksDTO> getSocksFromStorage(@RequestBody SocksInfoDTO info) {
        log.info("Эндпойнт отпуска продукции со склада начал работу");
        return ResponseEntity.ok(socksService.getFromStorage(info));
    }
    @Operation(summary = "Изменить запись на складе по id")
    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<SocksDTO> updateById(@PathVariable Integer id, @RequestBody SocksInfoDTO info) {
        log.info("Эндпойнт изменения данных по id начал работу");
        return ResponseEntity.ok(socksService.updateInStorage(id,info));
    }
    @Operation(summary = "Получить данные о количестве носков с фильтрацией по полям. Если запонены оба поля о большем и меньшем значении содержания хлопка, " +
            "будут выданы результаты партий с содержанием хлопка между этими значениями")
    @GetMapping()
    public ResponseEntity<Map<Integer,List<SocksDTO>>> getFilter(@RequestParam(value = "Цвет носков", required = false) String colour, @RequestParam(value = "Содержание хлопка", required = false) Double cottonContent,
                                                                 @RequestParam(value = "Партии с содержанием хлопка меньше, чем ввдённое значение", required = false) Double lessThanCottonContent,
                                                                 @RequestParam(value = "Партии с содержанием хлопка больше, чем ввдённое значение",required = false) Double moreThanCottonContent) {
        log.info("Эндпойнт получения данных с фильтрацией начал работу");
        return ResponseEntity.ok(socksService.getSocksQuantityWithFilter(colour,cottonContent,lessThanCottonContent,moreThanCottonContent));
    }
}
