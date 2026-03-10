package com.guilda.registro.repository;

import com.guilda.registro.model.Aventureiro;
import com.guilda.registro.model.ClasseEnum;
import com.guilda.registro.model.Companheiro;
import com.guilda.registro.model.EspecieEnum;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class AventureiroRepository {

    private final List<Aventureiro> banco = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Inicialização com 100 registros (chamado no construtor ou via @PostConstruct)
    public AventureiroRepository() {
        inicializarDados();
    }

    private void inicializarDados() {
        for (int i = 1; i <= 100; i++) {
            ClasseEnum classe = ClasseEnum.values()[i % ClasseEnum.values().length];
            Aventureiro a = new Aventureiro(
                    idGenerator.getAndIncrement(),
                    "Aventureiro " + i,
                    classe,
                    1 + (i % 20)
            );
            // Define alguns como inativos (por exemplo, os múltiplos de 5)
            if (i % 5 == 0) {
                a.setAtivo(false);
            }
            // Adiciona companheiro a alguns (múltiplos de 7)
            if (i % 7 == 0) {
                a.setCompanheiro(new Companheiro("Comp " + i, EspecieEnum.values()[i % EspecieEnum.values().length], i % 101));
            }
            banco.add(a);
        }
    }

    public Aventureiro salvar(Aventureiro aventureiro) {
        if (aventureiro.getId() == null) {
            aventureiro.setId(idGenerator.getAndIncrement());
            banco.add(aventureiro);
        } else {
            // atualização: substitui o existente
            banco.removeIf(a -> a.getId().equals(aventureiro.getId()));
            banco.add(aventureiro);
        }
        return aventureiro;
    }

    public Optional<Aventureiro> buscarPorId(Long id) {
        return banco.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public List<Aventureiro> listarTodos() {
        return new ArrayList<>(banco); // cópia para evitar modificações externas
    }

    public void remover(Long id) {
        banco.removeIf(a -> a.getId().equals(id));
    }
}