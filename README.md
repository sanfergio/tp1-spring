
# AT Spring - Sistema de Registro de Aventureiros

API RESTful desenvolvida em Java com Spring Boot para gerenciar um sistema completo de registro de aventureiros, incluindo auditoria de usuários, gerenciamento de roles/permissions, operações com missões e aventureiros e consultas no Elasticsearch.

## Visão Geral

Este projeto integra dois esquemas principais no PostgreSQL:

- **`audit`**: Gerenciamento de usuários, organizações, roles e permissions
- **`aventura`**: Domínio operacional com aventureiros, missões, participações e companheiros

E um módulo adicional:

- **`elastic`**: Consultas e agregações de produtos no Elasticsearch (índice `guilda_loja`)

## Tecnologias Utilizadas

- **Java 17**: Versão da linguagem de programação
- **Spring Boot 3.1.0**: Framework principal
- **PostgreSQL 15+**: Banco de dados relacional (via Docker)
- **Elasticsearch 8+**: Busca textual e agregações (via Docker)
- **Docker**: Containerização da aplicação e banco de dados
- **Spring Data JPA**: ORM e acesso a dados
- **Hibernate 6.2.2**: Mapeamento objeto-relacional
- **Spring Data Elasticsearch / Elasticsearch Java Client**: Integração com Elasticsearch
- **Maven**: Gerenciamento de dependências e build
- **Lombok**: Redução de código boilerplate
- **JUnit 5 / Testcontainers**: Testes com PostgreSQL real

## Estrutura do Projeto

```
src/main/java/com/guilda/registro/
├── RegistroApplication.java
├── audit/
│   ├── controller/
│   │   └── AuditController.java
│   ├── dto/
│   │   ├── NovoUsuarioRequest.java
│   │   ├── RoleComPermissoesResponse.java
│   │   └── UsuarioComRolesResponse.java
│   ├── model/
│   │   ├── ApiKey.java
│   │   ├── AuditEntry.java
│   │   ├── Organizacao.java
│   │   ├── Permission.java
│   │   ├── Role.java
│   │   └── Usuario.java
│   └── repository/
│       ├── OrganizacaoRepository.java
│       ├── RoleRepository.java
│       └── UsuarioRepository.java
└── aventura/
    ├── controller/
    │   ├── AventureiroConsultaController.java (GET com filtros, busca por nome, perfil completo)
    │   ├── MissaoController.java (POST criar missão, POST inscrever, GET listar por org)
    │   ├── RelatorioController.java (GET ranking, GET métricas)
    │   └── [DTOs de Response e Request no mesmo package]
    ├── dto/
    │   ├── AventureiroCreateRequest.java
    │   ├── AventureiroUpdateRequest.java
    │   ├── CompanheiroRequest.java
    │   ├── MissaoCreateRequest.java
    │   └── ParticipacaoRequest.java
    ├── model/
    │   ├── Aventureiro.java
    │   ├── ClasseEnum.java
    │   ├── Companheiro.java
    │   ├── EspecieEnum.java
    │   ├── Missao.java
    │   ├── NivelPerigoEnum.java
    │   ├── ParticipacaoMissao.java
    │   ├── PapelMissaoEnum.java
    │   └── StatusMissaoEnum.java
    ├── repository/
    │   ├── AventureiroRepository.java
    │   ├── AventureiroRepositoryImpl.java
    │   ├── CompanheiroRepository.java
    │   ├── MissaoRepository.java
    │   └── ParticipacaoMissaoRepository.java
    └── service/
        ├── AventureiroConsultaService.java
        ├── AventureiroService.java
        ├── MissaoConsultaService.java
        ├── MissaoService.java
        └── RelatorioService.java

  src/main/java/com/guilda/registro/elastic/
  ├── config/
  │   └── ElasticsearchConfig.java
  ├── controller/
  │   └── ProdutoController.java (buscas e agregações)
  ├── dto/
  │   └── [DTOs de agregação e resposta]
  ├── model/
  │   └── Produto.java (@Document indexName = "guilda_loja")
  ├── repository/
  │   └── ProdutoRepository.java
  └── service/
    └── ProdutoService.java (queries e agregações via `ElasticsearchClient`)
```

## Configuração de Infra (PostgreSQL + Elasticsearch) com Docker

O repositório utiliza **duas imagens Docker**, separadas por domínio:

- **PostgreSQL (schemas `audit` e `aventura`)**: `leogloriainfnet/postgres-tp2-spring:2.0-win`
- **Elasticsearch (módulo `elastic`)**: `leogloriainfnet/elastic-tp2-spring:1.0-windows`

> A aplicação espera PostgreSQL em `localhost:5432` e Elasticsearch em `localhost:9200`.

### Arquivos de Configuração

O arquivo `application.properties` contém as configurações de conexão:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.default_schema=audit
spring.jpa.properties.hibernate.hbm2ddl.create_namespaces=true

# Elasticsearch
spring.elasticsearch.uris=http://localhost:9200
spring.elasticsearch.connection-timeout=5s
spring.elasticsearch.socket-timeout=5s
```

## Como Executar

### Pré-requisitos:
- JDK 17 ou superior
- Docker
- Maven 3.9+

### Passos:

1. **Extrair as imagens Docker**:
   ```bash
docker pull leogloriainfnet/postgres-tp2-spring:2.0-win
docker pull leogloriainfnet/elastic-tp2-spring:1.0-windows
   ```

2. **Iniciar o container PostgreSQL (audit + aventura)**:
   ```bash
   docker run -d \
     --name postgres-tp2-spring \
     -e POSTGRES_DB=postgres \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=root \
     -p 5432:5432 \
     leogloriainfnet/postgres-tp2-spring:2.0-win
   ```

3. **(Se necessário) Resetar a senha do usuário `postgres` para `root` dentro do container**

Algumas máquinas/execuções podem subir o container com senha diferente da esperada pelo projeto. Se ao iniciar a aplicação você receber erro de autenticação, faça o reset pelo terminal do container:

```bash
docker exec -it postgres-tp2-spring psql -U postgres -d postgres
```

Dentro do `psql`, execute:

```sql
ALTER USER postgres WITH PASSWORD 'root';
```

E finalize com:

```sql
\q
```

> Dica: se você estiver reutilizando volumes antigos, trocar `POSTGRES_PASSWORD` no `docker run` não altera a senha já persistida. Nesse caso, apague o container/volume e recrie, ou use o `ALTER USER` acima.

4. **Iniciar o container Elasticsearch (módulo elastic)**:

```bash
docker run -d \
  --name elastic-tp2-spring \
  -p 9200:9200 \
  -p 9300:9300 \
  leogloriainfnet/elastic-tp2-spring:1.0-windows
```

5. **Aguardar os serviços ficarem prontos**:

PostgreSQL:
   ```bash
docker logs postgres-tp2-spring
   ```

Elasticsearch:

```bash
docker logs elastic-tp2-spring
```

6. **Build do projeto**:
   ```bash
   mvn clean install
   ```

7. **Executar a aplicação**:
   ```bash
   mvn spring-boot:run
   ```
   
   Ou via Java diretamente:
   ```bash
java -jar target/tp1-spring-1.0-SNAPSHOT.jar
   ```

A API estará disponível em `http://localhost:8080`.

### Parar o container:
```bash
docker stop postgres-tp2-spring elastic-tp2-spring
docker rm postgres-tp2-spring elastic-tp2-spring
```

## Testes

Execute todos os testes com:
```bash
mvn test
```

---

## Enums do Sistema

### ClasseEnum (Classe do Aventureiro)
- `GUERREIRO`
- `MAGO`
- `ARQUEIRO`
- `CLERIGO`
- `LADINO`

### EspecieEnum (Espécie do Companheiro)
- `LOBO`
- `CORUJA`
- `GOLEM`
- `DRAGAO_MINIATURA`

### StatusMissaoEnum (Status da Missão)
- `PLANEJADA`
- `EM_ANDAMENTO`
- `CONCLUIDA`
- `CANCELADA`

### NivelPerigoEnum (Nível de Perigo)
- `BAIXO`
- `MEDIO`
- `ALTO`
- `CRITICO`

### PapelMissaoEnum (Papel na Missão)
- `LIDER`
- `MEMBRO`
- `APOIO`

---

# API Endpoints e Curls

Abaixo estão **todos os comandos `curl`** para testar a API, organizados por categoria.

## 1. Endpoints de Auditoria (schema `audit`)

### 1.1 Listar usuários com suas roles
```bash
curl -X GET "http://localhost:8080/audit/usuarios" \
  -H "Content-Type: application/json"
```

### 1.2 Listar roles com suas permissions
```bash
curl -X GET "http://localhost:8080/audit/roles" \
  -H "Content-Type: application/json"
```

### 1.3 Criar novo usuário associado a uma organização existente
```bash
curl -X POST "http://localhost:8080/audit/usuarios" \
  -H "Content-Type: application/json" \
  -d '{
    "organizacaoId": 1,
    "nome": "Novo Aventureiro",
    "email": "novo@aventureiro.com",
    "senhaHash": "hash123456",
    "status": "ATIVO",
    "roleIds": [1, 2]
  }'
```

---

## 2. Endpoints do Domínio `aventura` (Operação)

### 2.1 Criar um novo aventureiro
```bash
curl -X POST "http://localhost:8080/aventura/aventureiros" \
  -H "Content-Type: application/json" \
  -d '{
    "organizacaoId": 1,
    "usuarioCadastroId": 1,
    "nome": "Aragorn",
    "classe": "GUERREIRO",
    "nivel": 5
  }'
```

### 2.2 Adicionar companheiro ao aventureiro (id=1)
```bash
curl -X POST "http://localhost:8080/aventura/aventureiros/1/companheiro" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Fenrir",
    "especie": "LOBO",
    "lealdade": 85
  }'
```

### 2.3 Criar uma missão
```bash
curl -X POST "http://localhost:8080/aventura/missoes" \
  -H "Content-Type: application/json" \
  -d '{
    "organizacaoId": 1,
    "titulo": "Resgate no Castelo",
    "nivelPerigo": "ALTO",
    "dataInicio": "2026-05-01",
    "dataTermino": "2026-05-10"
  }'
```

### 2.4 Inscrever aventureiro na missão (criar participação)
```bash
curl -X POST "http://localhost:8080/aventura/missoes/participacoes" \
  -H "Content-Type: application/json" \
  -d '{
    "missaoId": 1,
    "aventureiroId": 1,
    "papel": "LIDER",
    "recompensaOuro": 500,
    "destaque": true
  }'
```

### 2.5 Listar aventureiros (paginação básica)
```bash
curl -X GET "http://localhost:8080/aventureiros-consulta?page=0&size=10"
```

### 2.6 Testar inscrição duplicada na mesma missão
```bash
curl -X POST "http://localhost:8080/aventura/missoes/participacoes" \
  -H "Content-Type: application/json" \
  -d '{
    "missaoId": 1,
    "aventureiroId": 1,
    "papel": "MEMBRO"
  }'
```

> **Esperado:** `409 Conflict` com mensagem de duplicidade.

### 2.7 Testar restrição de aventureiro inativo (após desativá-lo)
```bash
curl -X POST "http://localhost:8080/aventura/missoes/participacoes" \
  -H "Content-Type: application/json" \
  -d '{
    "missaoId": 1,
    "aventureiroId": 1,
    "papel": "MEMBRO"
  }'
```

> **Esperado:** `400 Bad Request` informando que aventureiro inativo não pode participar.

### 2.8 Iniciar uma missão (id=1)
```bash
curl -X PATCH "http://localhost:8080/aventura/missoes/1/iniciar" \
  -H "Content-Type: application/json"
```

### 2.9 Concluir uma missão (id=1)
```bash
curl -X PATCH "http://localhost:8080/aventura/missoes/1/concluir" \
  -H "Content-Type: application/json"
```

### 2.10 Cancelar uma missão (id=1)
```bash
curl -X PATCH "http://localhost:8080/aventura/missoes/1/cancelar" \
  -H "Content-Type: application/json"
```

---

## 3. Consultas Operacionais e Relatórios

### 3.1 Listagem de aventureiros com filtros (paginação/ordenação)
```bash
curl -X GET "http://localhost:8080/aventureiros-consulta?ativo=true&classe=GUERREIRO&nivelMinimo=5&sort=nome,asc&page=0&size=10"
```

```bash
curl -X GET "http://localhost:8080/aventureiros-consulta?ativo=false&page=0&size=5"
```

### 3.2 Busca por nome (parcial)
```bash
curl -X GET "http://localhost:8080/aventureiros-consulta/busca?nome=aragorn&page=0&size=10"
```

### 3.3 Perfil completo do aventureiro (com participações e última missão)
```bash
curl -X GET "http://localhost:8080/aventureiros-consulta/1/perfil"
```

### 3.4 Listar missões de uma organização
```bash
curl -X GET "http://localhost:8080/aventura/missoes?orgId=1"
```

### 3.5 Top 10 missões dos últimos 15 dias
```bash
curl -X GET "http://localhost:8080/aventura/missoes/top15dias"
```

### 3.6 Ranking de participação (com filtros opcionais)
```bash
curl -X GET "http://localhost:8080/relatorios/ranking"
```

```bash
curl -X GET "http://localhost:8080/relatorios/ranking?dataInicio=2026-05-01&dataFim=2026-06-01&statusMissao=CONCLUIDA"
```

### 3.7 Relatório de missões com métricas (por período de criação)
```bash
curl -X GET "http://localhost:8080/relatorios/missoes-metricas?dataInicio=2026-01-01&dataFim=2026-12-31"
```

---

## 4. Endpoints do Elasticsearch (módulo `elastic`)

> Requer o Elasticsearch rodando em `http://localhost:9200` e o índice `guilda_loja` disponível.

### 4.1 Buscar produtos por nome (match)
```bash
curl -X GET "http://localhost:8080/produtos/busca/nome?termo=espada" \
  -H "Content-Type: application/json"
```

### 4.2 Buscar produtos por descrição (match)
```bash
curl -X GET "http://localhost:8080/produtos/busca/descricao?termo=encantada" \
  -H "Content-Type: application/json"
```

### 4.3 Buscar frase exata na descrição (match_phrase)
```bash
curl -X GET "http://localhost:8080/produtos/busca/frase?termo=forjada%20em%20mithril" \
  -H "Content-Type: application/json"
```

### 4.4 Buscar por nome com fuzzy
```bash
curl -X GET "http://localhost:8080/produtos/busca/fuzzy?termo=esapda" \
  -H "Content-Type: application/json"
```

### 4.5 Buscar em múltiplos campos (nome + descrição)
```bash
curl -X GET "http://localhost:8080/produtos/busca/multicampos?termo=an%C3%A9is" \
  -H "Content-Type: application/json"
```

### 4.6 Buscar por descrição e filtrar por categoria
```bash
curl -X GET "http://localhost:8080/produtos/busca/com-filtro?termo=po%C3%A7%C3%A3o&categoria=consumiveis" \
  -H "Content-Type: application/json"
```

### 4.7 Buscar por faixa de preço
```bash
curl -X GET "http://localhost:8080/produtos/busca/faixa-preco?min=100&max=300" \
  -H "Content-Type: application/json"
```

### 4.8 Busca avançada (parâmetros opcionais)
```bash
curl -X GET "http://localhost:8080/produtos/busca/avancada?categoria=armas&raridade=RARO&min=200&max=900" \
  -H "Content-Type: application/json"
```

### 4.9 Agregação: contagem por categoria
```bash
curl -X GET "http://localhost:8080/produtos/agregacoes/por-categoria" \
  -H "Content-Type: application/json"
```

### 4.10 Agregação: contagem por raridade
```bash
curl -X GET "http://localhost:8080/produtos/agregacoes/por-raridade" \
  -H "Content-Type: application/json"
```

### 4.11 Agregação: preço médio
```bash
curl -X GET "http://localhost:8080/produtos/agregacoes/preco-medio" \
  -H "Content-Type: application/json"
```

### 4.12 Agregação: faixas de preço
```bash
curl -X GET "http://localhost:8080/produtos/agregacoes/faixas-preco" \
  -H "Content-Type: application/json"
```

---

## Observações Importantes

- **Base URL:** `http://localhost:8080`
- **Métodos:** GET, POST, PUT, PATCH, DELETE conforme indicado
- **Headers:** `Content-Type: application/json` para requisições com corpo
- Para executar os comandos, utilize **Git Bash**, **WSL**, **PowerShell** (com `curl` instalado) ou ferramentas como **Postman/Insomnia**
- Os valores de exemplo (`organizacaoId=1`, `usuarioCadastroId=1`, `missaoId=1`, `aventureiroId=1`) devem ser substituídos pelos IDs reais existentes no seu banco de dados
- A aplicação requer PostgreSQL rodando em `localhost:5432` com credenciais padrão (`postgres`/`root`)
- As rotas do módulo `elastic` requerem Elasticsearch em `localhost:9200`
