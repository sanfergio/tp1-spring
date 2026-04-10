-- APENAS FAZER REFRESH NA MV (SEM ALTERAR ESTRUTURA)
-- A MV é gerenciada pelo DBA, não podemos apagar ou recriar
REFRESH MATERIALIZED VIEW operacoes.mv_painel_tatico_missao;
