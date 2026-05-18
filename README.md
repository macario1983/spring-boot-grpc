# Spring Boot gRPC

Projeto de estudo sobre comunicação gRPC entre microsserviços Spring Boot, aplicando arquitetura hexagonal (ports & adapters).

## Arquitetura

```
                    REST (8081)              gRPC (8080)          DynamoDB (4566)
┌─────────┐   HTTP   ┌──────────┐  protobuf  ┌──────────┐   SDK   ┌──────────┐
│  curl   │ ───────> │  Client  │ ────────>  │  Server  │ ──────> │  Floci   │
│ (Postman│          │ :8081    │            │ :8080    │         │ :4566    │
│  etc)   │ <─────── │          │ <────────  │          │ <────── │          │
└─────────┘   JSON   └──────────┘  protobuf  └──────────┘   SDK   └──────────┘
```

- **grpc-client** — aplicação Spring Boot Web (REST) que recebe chamadas HTTP e as traduz para chamadas gRPC
- **grpc-server** — aplicação Spring Boot gRPC que processa as chamadas e persiste os dados no DynamoDB
- **Floci** — emulador local de serviços AWS (DynamoDB)

## Stack

- Java 21
- Spring Boot 4.0.6
- Spring gRPC 1.0.3
- gRPC 1.77.1 / Protobuf 4.33.4
- DynamoDB (Floci)
- Maven + asdf

## Estrutura do projeto

```
spring-boot-grpc/
├── grpc-client/                  # Aplicação REST -> gRPC client
│   └── src/main/java/com/grpc/client/
│       ├── adapter/inbound/rest/     # BookController (REST)
│       ├── adapter/outbound/grpc/    # BookGrpcClient (gRPC stub)
│       ├── application/service/      # BookServiceImpl (use case)
│       ├── domain/model/             # Book (record)
│       ├── domain/port/inbound/      # BookUseCase (interface)
│       └── infrastructure/config/    # GrpcClientConfig
├── grpc-server/                  # Aplicação gRPC server
│   └── src/main/java/com/grpc/server/
│       ├── adapter/inbound/grpc/     # BookGrpcService
│       ├── adapter/outbound/dynamodb/# BookDynamoDbRepository
│       ├── domain/model/             # Book (record)
│       ├── domain/port/inbound/      # BookUseCase (interface)
│       ├── domain/port/outbound/     # BookRepository (interface)
│       └── domain/service/           # BookServiceImpl
└── README.md
```

## Como rodar

### 1. Subir o DynamoDB (Floci)

```shell
docker run -d --name floci -p 4566:4566 floci/floci:latest
```

Criar a tabela:

```shell
AWS_ACCESS_KEY_ID=fake AWS_SECRET_ACCESS_KEY=fake \
  aws dynamodb create-table \
    --table-name books \
    --attribute-definitions AttributeName=isbn,AttributeType=S \
    --key-schema AttributeName=isbn,KeyType=HASH \
    --billing-mode PAY_PER_REQUEST \
    --endpoint-url http://localhost:4566 \
    --region us-east-1
```

### 2. Subir o servidor gRPC

```shell
cd grpc-server
mvn spring-boot:run
```

O servidor sobe em `localhost:8080`.

### 3. Subir o client REST

```shell
cd grpc-client
mvn spring-boot:run
```

O client sobe em `localhost:8081`.

### 4. Testar

```shell
# Listar livros
curl -s http://localhost:8081/books | jq

# Buscar livro
curl -s http://localhost:8081/books/978-0134685991 | jq

# Criar livro
curl -s -X POST http://localhost:8081/books \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "978-0134685991",
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "pages": 416,
    "year": 2018
  }' | jq
```

## O que estudei aqui

- Definição de serviços e mensagens com **Protobuf**
- Geração de código Java com `protobuf-maven-plugin`
- Configuração de servidor gRPC com **Spring gRPC**
- Criação de **stubs bloqueantes** no client
- Tradução entre modelos de domínio e mensagens protobuf
- Integração com **DynamoDB** via `DynamoDbEnhancedClient`
- Arquitetura **hexagonal** (ports & adapters) aplicada a gRPC
