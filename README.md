# Biblioteca API — Check Point 2

API REST em Java + Spring Boot para gerenciar autores e livros, com persistência em **SQL Server** via Spring Data JPA.

## Como executar

Pré-requisito: **Docker Desktop** aberto.

```bash
docker compose up -d --build
```

Esse comando sobe o SQL Server, cria o banco e as tabelas (`database/schema.sql`) e inicia a API em **http://localhost:8080**.

Para parar: `docker compose down`

## Conexão com o SQL Server

Configurada em `src/main/resources/application.yml`. Pode ser alterada por variáveis de ambiente:

| Variável | Padrão |
|---|---|
| `DB_URL` | `jdbc:sqlserver://localhost:1433;databaseName=biblioteca;encrypt=true;trustServerCertificate=true` |
| `DB_USERNAME` | `sa` |
| `DB_PASSWORD` | `Biblioteca@2026` |

Para acessar o banco por um cliente SQL (SSMS, Azure Data Studio): servidor `localhost,1433`, usuário `sa`, senha `Biblioteca@2026`, banco `biblioteca`, com **Trust server certificate** marcado.

## Endpoints

Swagger: **http://localhost:8080/swagger-ui.html**

| Método | Rota | Descrição |
|---|---|---|
| GET | `/status/banco` | Testa a conexão com o SQL Server |
| GET | `/autores` | Lista autores (`?nome=`) |
| GET | `/autores/{id}` | Busca autor |
| POST | `/autores` | Cadastra autor |
| PUT | `/autores/{id}` | Atualiza autor |
| DELETE | `/autores/{id}` | Exclui autor |
| GET | `/livros` | Lista livros (`?titulo=` ou `?autorId=`) |
| GET | `/livros/{id}` | Busca livro |
| POST | `/livros` | Cadastra livro |
| PUT | `/livros/{id}` | Atualiza livro |
| DELETE | `/livros/{id}` | Exclui livro |

Exemplos de corpo:

```json
{ "nome": "Cecília Meireles", "nacionalidade": "Brasileira" }
```

```json
{ "titulo": "Romanceiro da Inconfidência", "isbn": "9788526012345", "anoPublicacao": 1953, "quantidadeDisponivel": 2, "autorId": 1 }
```

## Integrantes

- Felipe Maringoli Teixeira — RM556100
- Maria Eduarda de Araujo Fernandes — RM554593