package com.capstone.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/walkwithme")
public class ServerController {
    private final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);

    @PostMapping("/get")
    public ResponseEntity<String> getMyData(
            @Valid @RequestBody ValidRequestDto validRequestDto) {
        LOGGER.info(validRequestDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(validRequestDto.toString());
    }
}
