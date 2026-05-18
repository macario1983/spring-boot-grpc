# gRPC Client - REST Endpoints

Servidor REST roda em `http://localhost:8081`

## Pré-requisitos

- gRPC Server rodando em `localhost:8080`
- DynamoDB (LocalStack) rodando em `localhost:4566` com tabela `books` criada

## Endpoints

### Listar todos os livros

```shell
curl -s http://localhost:8081/books | jq
```

<details>
<summary>Exemplo de resposta</summary>

```json
[
  {
    "isbn": "978-0134685991",
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "pages": 416,
    "year": 2018
  },
  {
    "isbn": "978-1617294945",
    "title": "Spring in Action",
    "author": "Craig Walls",
    "pages": 520,
    "year": 2019
  }
]
```
</details>

### Buscar livro por ISBN

```shell
curl -s http://localhost:8081/books/978-0134685991 | jq
```

<details>
<summary>Exemplo de resposta</summary>

```json
{
  "isbn": "978-0134685991",
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "pages": 416,
  "year": 2018
}
```
</details>

### Criar um livro

```shell
curl -s -X POST http://localhost:8081/books \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "978-1492078005",
    "title": "Kubernetes Up and Running",
    "author": "Brendan Burns",
    "pages": 320,
    "year": 2022
  }' | jq
```

<details>
<summary>Exemplo de resposta</summary>

```json
{
  "isbn": "978-1492078005",
  "title": "Kubernetes Up and Running",
  "author": "Brendan Burns",
  "pages": 320,
  "year": 2022
}
```
</details>
