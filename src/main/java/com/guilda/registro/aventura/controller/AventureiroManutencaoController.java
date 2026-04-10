package com.guilda.registro.aventura.controller;

import com.guilda.registro.aventura.dto.CompanheiroRequest;
import com.guilda.registro.aventura.service.AventureiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aventura/aventureiros")
@RequiredArgsConstructor
public class AventureiroManutencaoController {

    private final AventureiroService aventureiroService;

    @PostMapping("/{id}/companheiro")
    @ResponseStatus(HttpStatus.OK)
    public AventureiroResponse adicionarCompanheiro(
            @PathVariable Long id,
            @Valid @RequestBody CompanheiroRequest request) {
        return aventureiroService.adicionarCompanheiro(id, request);
    }
}
