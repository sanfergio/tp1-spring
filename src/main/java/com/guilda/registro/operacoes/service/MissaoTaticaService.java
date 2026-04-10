package com.guilda.registro.operacoes.service;

import com.guilda.registro.operacoes.model.MissaoPainelTatico;
import com.guilda.registro.operacoes.repository.MissaoPainelTaticoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MissaoTaticaService {

    private final MissaoPainelTaticoRepository repository;

    public MissaoTaticaService(MissaoPainelTaticoRepository repository) {
        this.repository = repository;
    }

    public List<MissaoPainelTatico> getTopMissoesUltimos15Dias() {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(15);
        Pageable topDez = PageRequest.of(0, 10);
        return repository.findTopMissoesUltimosDias(dataLimite, topDez);
    }
}