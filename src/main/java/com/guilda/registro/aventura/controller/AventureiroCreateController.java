package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.dto.AventureiroCreateRequest;
import com.guilda.registro.aventura.service.AventureiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aventura/aventureiros")
@RequiredArgsConstructor
public class AventureiroCreateController {

    private final AventureiroService aventureiroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AventureiroResponse criarAventureiro(@Valid @RequestBody AventureiroCreateRequest request) {
        return aventureiroService.criarAventureiro(request);
    }
}
