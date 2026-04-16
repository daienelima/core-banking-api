# Core Banking API

![CI](https://github.com/daienelima/core-banking-api/actions/workflows/ci.yml/badge.svg)

Uma API de banco central construída com Spring Boot 3.5, Java 21, PostgreSQL e Apache Kafka, implementando padrões de resiliência e arquitetura limpa.

## Visão Geral

A Core Banking API é uma aplicação que fornece funcionalidades essenciais para operações bancárias, incluindo gerenciamento de contas, processamento de transferências e envio de notificações assíncronas através de Apache Kafka.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.14-SNAPSHOT
- Spring Data JPA
- Spring Kafka
- PostgreSQL 15
- Apache Kafka 7.5.0
- Resilience4j
- Lombok
- Docker e Docker Compose
- JUnit 5
- Mockito
- SpringDoc OpenAPI (Swagger)

## Arquitetura

O projeto segue princípios de arquitetura limpa com as seguintes camadas:

- **Domain**: Modelos de domínio, entidades, exceções e interfaces de repositório
- **Application**: Casos de uso que orquestram a lógica de negócio
- **Entrypoint**: Controladores REST, DTOs e manipuladores de exceções
- **Infrastructure**: Implementações de repositórios, persistência e mensageria

## Funcionalidades Principais

- Gerenciamento de contas bancárias
- Processamento de transferências entre contas
- Validação de saldo insuficiente
- Persistência de transações
- Envio de notificações assíncronas via Kafka
- Tratamento de erros de notificação com retry automático
- Circuit breaker para proteção contra falhas em cascata

## Estrutura de Pastas

```
core-banking-api/
├── src/
│   ├── main/
│   │   ├── java/core/banking/api/
│   │   │   ├── CoreBankingApiApplication.java
│   │   │   ├── application/usecase/        # Casos de uso
│   │   │   ├── domain/                     # Domínio do negócio
│   │   │   │   ├── exception/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   ├── entrypoint/                 # Camada de apresentação
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   └── handler/
│   │   │   └── infrastructure/             # Implementações técnicas
│   │   │       ├── config/
│   │   │       ├── messaging/
│   │   │       └── persistence/
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/core/banking/api/
├── docker/
│   └── postgres/
│       └── init.sql
├── docker-compose.yml
└── pom.xml
```

## Modelos de Domínio

### Account
Representa uma conta bancária com identificador, nome e saldo.

### Transaction
Registra as transferências com contas de origem e destino, valor e timestamp.

### TransferEvent
Evento enviado para o Kafka quando uma transferência é processada.

## Exceções Customizadas

- `ContaNaoEncontradaException`: Lançada quando uma conta não é encontrada
- `SaldoInsuficienteException`: Lançada quando o saldo é insuficiente para transferência
- `ErroEnvioNotificacaoException`: Lançada quando o envio de notificação falha

## Configuração

### Variáveis de Ambiente

```
DB_USER=postgres                # Usuario do banco de dados
DB_PASSWORD=password            # Senha do banco de dados
```

### Aplicação

As configurações padrão estão em `src/main/resources/application.yml`:

- Banco de dados: PostgreSQL em localhost:5432
- Kafka: localhost:9092
- Porta da aplicação: 8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## Como Iniciar

### Pré-requisitos

- Java 21 instalado
- Maven instalado
- Docker e Docker Compose instalados (para executar o ambiente completo)

### Passo 1: Iniciar as dependências com Docker

```bash
docker-compose up -d
```

Este comando inicia:
- PostgreSQL (porta 5432)
- Zookeeper (porta 2181)
- Apache Kafka (porta 9092)

### Passo 2: Construir o projeto

```bash
./mvnw clean package
```

Ou no Windows:
```bash
mvnw.cmd clean package
```

### Passo 3: Executar a aplicação

```bash
./mvnw spring-boot:run
```

Ou no Windows:
```bash
mvnw.cmd spring-boot:run
```

A aplicação estará disponível em: http://localhost:8080

## API Endpoints

### Transferências

- `POST /transfers` - Realizar uma transferência entre contas
  - Request body:
    ```json
    {
      "fromAccountId": 1,
      "toAccountId": 2,
      "amount": 100.00
    }
    ```

## Documentação Interativa

Acesse a documentação interativa da API através do Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

## Testes

### Executar todos os testes

```bash
./mvnw test
```

### Executar testes de uma classe específica

```bash
./mvnw test -Dtest=AccountRepositoryImplTest
```

## Padrões de Resiliência

A aplicação implementa os seguintes padrões através do Resilience4j:

### Retry

O serviço de notificação tenta até 3 vezes com intervalo de 500ms entre tentativas.

### Circuit Breaker

Proteção contra falhas em cascata com:
- Janela deslizante de 10 eventos
- Taxa de falha limite: 50%
- Tempo de espera em estado aberto: 5 segundos

### Time Limiter

Limite de tempo para execução: 2 segundos

## Fluxo de Processamento

1. Cliente realiza uma solicitação de transferência via REST
2. O controlador valida e passa para o caso de uso
3. O caso de uso:
   - Busca as contas de origem e destino
   - Valida se há saldo suficiente
   - Debita a conta de origem
   - Credita a conta de destino
   - Persiste a transação
   - Publica evento no Kafka
4. O consumidor Kafka recebe o evento e processa a notificação
5. Em caso de falha, o retry e circuit breaker entram em ação

## Logging

Os níveis de log são configuráveis em `application.yml`. Por padrão:
- Nível raiz: INFO
- Nível de aplicação: DEBUG
- Queries SQL: DEBUG
- Transações: TRACE

Padrão de log: `%d{yyyy-MM-dd HH:mm:ss} - %msg%n`

## Banco de Dados

### Inicialização

O banco de dados é inicializado automaticamente pelo Docker através do script SQL:
`docker/postgres/init.sql`

### Configuração Hibernate

- Modo DDL: validate (não cria tabelas automaticamente)
- Dialect: PostgreSQL
- Pool de conexões: 5-20 conexões
- Batching: habilitado para inserts e updates
