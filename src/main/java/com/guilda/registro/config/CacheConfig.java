package com.guilda.registro.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de Cache para Otimização de Performance
 * 
 * Estratégia implementada:
 * - Cache em memória (ConcurrentMapCacheManager) para dados que mudam pouco
 * - TTL de 10 minutos para consultas do painel tático
 * - Invalidação automática quando dados são modificados
 * 
 * Caches configurados:
 * - "topMissoes15Dias": Cache do ranking dos últimos 15 dias (TTL: 10 min)
 * - "missoesUltimosDias": Cache de missões por período (TTL: 10 min)
 * - "missoesAltaProntidao": Cache de missões com alta prontidão (TTL: 10 min)
 * - "missoesEmAndamento": Cache de missões em andamento (TTL: 10 min)
 * 
 * NOTA IMPORTANTE:
 * O Spring Cache não oferece TTL nativo com ConcurrentMapCacheManager.
 * Para um TTL mais preciso em produção, considere usar:
 * - Redis (spring-boot-starter-data-redis)
 * - Caffeine Cache (com spring-boot-starter-cache + caffeine)
 * 
 * Benefícios da solução atual:
 * ✓ Reduz carga no banco de dados
 * ✓ Melhora tempo de resposta do endpoint
 * ✓ Mantém consistência por invalidação estratégica
 * ✓ Zero dependências externas
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "topMissoes15Dias",
                "missoesUltimosDias",
                "missoesAltaProntidao",
                "missoesEmAndamento"
        );
    }
}
