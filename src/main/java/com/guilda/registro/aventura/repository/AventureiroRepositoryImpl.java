package com.guilda.registro.aventura.repository;

import com.guilda.registro.aventura.model.Aventureiro;
import com.guilda.registro.aventura.model.ClasseEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AventureiroRepositoryImpl implements AventureiroRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Aventureiro> findWithFilters(Boolean ativo, ClasseEnum classe, Integer nivelMinimo, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // --- Consulta principal (listagem) ---
        CriteriaQuery<Aventureiro> cq = cb.createQuery(Aventureiro.class);
        Root<Aventureiro> root = cq.from(Aventureiro.class);
        List<Predicate> predicates = new ArrayList<>();

        if (ativo != null) {
            predicates.add(cb.equal(root.get("ativo"), ativo));
        }
        if (classe != null) {
            predicates.add(cb.equal(root.get("classe"), classe));
        }
        if (nivelMinimo != null) {
            predicates.add(cb.ge(root.get("nivel"), nivelMinimo));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        // Ordenação
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                Path<Object> path = root.get(order.getProperty());
                if (order.isAscending()) {
                    orders.add(cb.asc(path));
                } else {
                    orders.add(cb.desc(path));
                }
            });
            cq.orderBy(orders);
        } else {
            cq.orderBy(cb.asc(root.get("id")));
        }

        List<Aventureiro> resultList = em.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // --- Consulta de contagem (separada, com seus próprios predicates) ---
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Aventureiro> countRoot = countQuery.from(Aventureiro.class);
        List<Predicate> countPredicates = new ArrayList<>();

        if (ativo != null) {
            countPredicates.add(cb.equal(countRoot.get("ativo"), ativo));
        }
        if (classe != null) {
            countPredicates.add(cb.equal(countRoot.get("classe"), classe));
        }
        if (nivelMinimo != null) {
            countPredicates.add(cb.ge(countRoot.get("nivel"), nivelMinimo));
        }

        countQuery.select(cb.count(countRoot));
        countQuery.where(countPredicates.toArray(new Predicate[0]));

        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public Page<Aventureiro> searchByNome(String nome, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // Consulta principal
        CriteriaQuery<Aventureiro> cq = cb.createQuery(Aventureiro.class);
        Root<Aventureiro> root = cq.from(Aventureiro.class);
        Predicate nomePredicate = cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        cq.where(nomePredicate);
        cq.orderBy(cb.asc(root.get("nome")));

        List<Aventureiro> resultList = em.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Consulta de contagem
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Aventureiro> countRoot = countQuery.from(Aventureiro.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(cb.like(cb.lower(countRoot.get("nome")), "%" + nome.toLowerCase() + "%"));

        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }
}