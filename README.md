
# Projeto Registro de Aventureiros API

Esta é uma API RESTful desenvolvida em Java com Spring Boot para gerenciar o registro de aventureiros de uma guilda. A aplicação permite criar, listar, buscar, atualizar e gerenciar o status de aventureiros, bem como seus companheiros.

## Estrutura do Projeto

O projeto segue a estrutura padrão de um projeto Maven e Spring Boot:

```
.
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── guilda
    │   │           └── registro
    │   │               ├── RegistroApplication.java
    │   │               ├── controller
    │   │               │   └── AventureiroController.java
    │   │               ├── dto
    │   │               │   ├── request
    │   │               │   │   ├── AventureiroCreateRequest.java
    │   │               │   │   ├── AventureiroUpdateRequest.java
    │   │               │   │   └── CompanheiroRequest.java
    │   │               │   └── response
    │   │               │       ├── AventureiroDetalhadoResponse.java
    │   │               │       ├── AventureiroResumoResponse.java
    │   │               │       ├── CompanheiroResponse.java
    │   │               │       └── ErroResponse.java
    │   │               ├── exception
    │   │               │   ├── GlobalExceptionHandler.java
    │   │               │   ├── RecursoNaoEncontradoException.java
    │   │               │   └── ValidacaoException.java
    │   │               ├── model
    │   │               │   ├── Aventureiro.java
    │   │               │   ├── ClasseEnum.java
    │   │               │   ├── Companheiro.java
    │   │               │   └── EspecieEnum.java
    │   │               ├── repository
    │   │               │   └── AventureiroRepository.java
    │   │               └── service
    │   │                   └── AventureiroService.java
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── com
                └── guilda
                    └── AppTest.java
```

## Tecnologias Utilizadas

*   **Java 17**: Versão da linguagem de programação.
*   **Spring Boot 3.1.0**: Framework principal para criação da aplicação.
*   **Maven**: Ferramenta de gerenciamento de dependências e build.
*   **Spring Web**: Para criação de APIs REST.
*   **Spring Validation**: Para validação de dados de entrada.
*   **Spring Data Commons**: Para suporte a funcionalidades de paginação.

## Arquivos Principais

### `pom.xml`

Define as dependências e configurações do projeto. As principais dependências são:
*   `spring-boot-starter-web`: Para construir aplicações web e APIs REST.
*   `spring-boot-starter-validation`: Para usar anotações de validação como `@Valid`, `@NotBlank`, etc.
*   `spring-data-commons`: Fornece abstrações como `Page` e `Pageable` para paginação, mesmo sem um banco de dados JPA completo.

### `RegistroApplication.java`

Classe principal que inicializa a aplicação Spring Boot.

### Modelos (`/model`)

*   **`Aventureiro.java`**: A entidade principal. Representa um aventureiro com os seguintes atributos:
    *   `id`: Identificador único.
    *   `nome`: Nome do aventureiro.
    *   `classe`: A classe do aventureiro (enum `ClasseEnum`).
    *   `nivel`: O nível de poder do aventureiro.
    *   `ativo`: Status que indica se o aventureiro está ativo na guilda.
    *   `companheiro`: Um objeto `Companheiro` opcional.
*   **`Companheiro.java`**: Representa o companheiro de um aventureiro.
    *   `nome`: Nome do companheiro.
    *   `especie`: A espécie do companheiro (enum `EspecieEnum`).
    *   `lealdade`: Nível de lealdade do companheiro (0 a 100).
*   **`ClasseEnum.java`**: Enum para as classes de aventureiro (`GUERREIRO`, `MAGO`, `ARQUEIRO`, `CLERIGO`, `LADINO`).
*   **`EspecieEnum.java`**: Enum para as espécies de companheiro (`LOBO`, `CORUJA`, `GOLEM`, `DRAGAO_MINIATURA`).

### Repositório (`/repository`)

*   **`AventureiroRepository.java`**: Simula um banco de dados em memória usando uma `ArrayList`.
    *   Inicializa com 100 aventureiros de exemplo.
    *   Fornece métodos para salvar (criar/atualizar), buscar por ID e listar todos os aventureiros.
    *   **Importante**: Não há persistência real de dados. Os dados são perdidos quando a aplicação é reiniciada.

### Serviço (`/service`)

*   **`AventureiroService.java`**: Contém a lógica de negócio da aplicação.
    *   Orquestra as operações recebidas do `AventureiroController`.
    *   Realiza a conversão entre DTOs e as entidades do modelo.
    *   Aplica filtros e paginação na listagem de aventureiros.

### Controlador (`/controller`)

*   **`AventureiroController.java`**: Define os endpoints da API REST.
    *   Mapeado para o caminho base `/aventureiros`.
    *   Recebe as requisições HTTP, valida os dados de entrada e chama os métodos correspondentes no `AventureiroService`.
    *   Formata e retorna as respostas HTTP com os códigos de status apropriados.

### DTOs (`/dto`)

*   **Request**: Classes que representam os dados esperados no corpo das requisições (`AventureiroCreateRequest`, `AventureiroUpdateRequest`, `CompanheiroRequest`).
*   **Response**: Classes que formatam os dados enviados nas respostas da API (`AventureiroResumoResponse`, `AventureiroDetalhadoResponse`, `CompanheiroResponse`, `ErroResponse`).

### Exceções (`/exception`)

*   **`GlobalExceptionHandler.java`**: Captura exceções lançadas pela aplicação e as transforma em respostas de erro HTTP padronizadas.
*   **`RecursoNaoEncontradoException.java`**: Exceção customizada para quando um recurso (como um aventureiro) não é encontrado.
*   **`ValidacaoException.java`**: Exceção para erros de validação de negócio.

## Endpoints da API

A seguir estão os endpoints disponíveis na API, todos sob o prefixo `/aventureiros`.

| Método | URI                               | Descrição                                     |
|--------|-----------------------------------|-----------------------------------------------|
| POST   | `/`                               | Registra um novo aventureiro.                 |
| GET    | `/`                               | Lista aventureiros com filtros e paginação.   |
| GET    | `/{id}`                           | Busca um aventureiro pelo seu ID.             |
| PUT    | `/{id}`                           | Atualiza os dados de um aventureiro.          |
| PATCH  | `/{id}/encerrar-vinculo`          | Desativa um aventureiro (`ativo = false`).    |
| PATCH  | `/{id}/recrutar`                  | Reativa um aventureiro (`ativo = true`).      |
| POST   | `/{id}/companheiro`               | Define ou substitui o companheiro de um aventureiro. |
| DELETE | `/{id}/companheiro`               | Remove o companheiro de um aventureiro.       |

## Como Executar

1.  **Pré-requisitos**:
    *   JDK 17 ou superior.
    *   Maven 3.x.

2.  **Build**:
    Navegue até a raiz do projeto e execute o comando Maven para compilar e empacotar a aplicação:
    ```bash
    mvn clean package
    ```

3.  **Execução**:
    Após o build, você pode iniciar a aplicação com o seguinte comando:
    ```bash
    java -jar target/tp1-spring-1.0-SNAPSHOT.jar
    ```
    A API estará disponível em `http://localhost:8080`.

## Testes

O projeto inclui um arquivo de teste de exemplo `AppTest.java`, que pode ser expandido para incluir testes unitários e de integração para os serviços e controladores. Para rodar os testes, use o comando:
```bash
mvn test
```
