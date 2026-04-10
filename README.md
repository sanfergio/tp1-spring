
# TP2 Spring - Sistema de Registro de Aventureiros

API RESTful desenvolvida em Java com Spring Boot para gerenciar um sistema completo de registro de aventureiros, incluindo auditoria de usuários, gerenciamento de roles/permissions e operações com missões e aventureiros.

## Visão Geral

Este projeto integra dois esquemas principais no PostgreSQL:

- **`audit`**: Gerenciamento de usuários, organizações, roles e permissions
- **`aventura`**: Domínio operacional com aventureiros, missões, participações e companheiros

## Tecnologias Utilizadas

- **Java 17**: Versão da linguagem de programação
- **Spring Boot 3.1.0**: Framework principal
- **PostgreSQL 15+**: Banco de dados relacional (via Docker)
- **Docker**: Containerização da aplicação e banco de dados
- **Spring Data JPA**: ORM e acesso a dados
- **Hibernate 6.2.2**: Mapeamento objeto-relacional
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
```

## Configuração do Banco de Dados com Docker

O projeto utiliza a imagem Docker customizada **`leogloriainfnet/postgres-tp2-spring:2.0-win`** que contém o PostgreSQL pré-configurado com os esquemas `audit` e `aventura`, além de dados de exemplo.

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
```

## Como Executar

### Pré-requisitos:
- JDK 17 ou superior
- Docker
- Maven 3.9+

### Passos:

1. **Extrair a imagem Docker do PostgreSQL**:
   ```bash
   docker pull leogloriainfnet/postgres-tp2-spring:2.0-win
   ```

2. **Iniciar o container PostgreSQL**:
   ```bash
   docker run -d \
     --name postgres-tp3 \
     -e POSTGRES_DB=postgres \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=root \
     -p 5432:5432 \
     leogloriainfnet/postgres-tp2-spring:2.0-win
   ```

3. **Aguardar o banco ficar pronto** (geralmente 10-15 segundos):
   ```bash
   docker logs postgres-tp3
   ```

4. **Build do projeto**:
   ```bash
   mvn clean install
   ```

5. **Executar a aplicação**:
   ```bash
   mvn spring-boot:run
   ```
   
   Ou via Java diretamente:
   ```bash
   java -jar target/tp2-spring-1.0-SNAPSHOT.jar
   ```

A API estará disponível em `http://localhost:8080`.

### Parar o container:
```bash
docker stop postgres-tp3
docker rm postgres-tp3
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

### 2.5 Listar aventureiros de uma organização
```bash
curl -X GET "http://localhost:8080/aventureiros-consulta?page=0&size=10"
```
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

## Observações Importantes

- **Base URL:** `http://localhost:8080`
- **Métodos:** GET, POST, PUT, PATCH, DELETE conforme indicado
- **Headers:** `Content-Type: application/json` para requisições com corpo
- Para executar os comandos, utilize **Git Bash**, **WSL**, **PowerShell** (com `curl` instalado) ou ferramentas como **Postman/Insomnia**
- Os valores de exemplo (`organizacaoId=1`, `usuarioCadastroId=1`, `missaoId=1`, `aventureiroId=1`) devem ser substituídos pelos IDs reais existentes no seu banco de dados
- A aplicação requer um banco PostgreSQL rodando localmente em `localhost:5432` com credenciais padrão (`postgres`/`root`)
