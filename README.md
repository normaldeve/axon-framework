# Axon Framework Study Project

## 📋 프로젝트 개요

이 프로젝트는 Axon Framework의 핵심 개념인 **CQRS**(Command Query Responsibility Segregation)와 **Event Sourcing** 패턴을 학습하기 위한 샘플 애플리케이션입니다. "코끼리를 냉장고에 넣기"라는 유머러스한 시나리오를 통해 Command, Event, Query의 흐름을 이해할 수 있도록 구성했습니다.

## 🎯 주요 기능

- 코끼리 생성 (Create)
- 코끼리를 냉장고에 넣기 (Enter)
- 코끼리를 냉장고에서 꺼내기 (Exit)
- 코끼리 정보 조회 (Query)
- Event Replay 기능

## 🛠 기술 스택

- Java
- Spring Boot
- Axon Framework
- JPA/Hibernate
- H2 Database
- Swagger/OpenAPI 3.0

## 🏗 아키텍처 구조

### CQRS 패턴 구현

프로젝트는 명령(Command)과 조회(Query)를 명확히 분리하여 구현했습니다.

#### Command Side

**Commands** - 시스템의 상태를 변경하는 명령

- `CreateElephantCommand`: 코끼리 생성
- `EnterElephantCommand`: 냉장고에 넣기
- `ExitElephantCommand`: 냉장고에서 꺼내기
- `BackToReadyCommand`: Ready 상태로 복귀

**Aggregate** - `ElephantAggregate`

- Command를 받아 비즈니스 로직을 실행하고 Event를 발행
- Event Sourcing을 통해 상태 복원

**Events** - 시스템에서 발생한 사실을 나타냄

- `CreatedElephantEvent`: 코끼리 생성됨
- `EnteredElephantEvent`: 냉장고에 넣음
- `ExitedElephantEvent`: 냉장고에서 꺼냄
- `FailedEnterElephantEvent`: 넣기 실패 (100kg 초과)
- `BackToReadyCompletedEvent`: Ready 상태 복귀 완료

#### Query Side

**Queries** - 데이터 조회 요청

- `GetElephantQuery`: 특정 코끼리 조회
- "list": 전체 코끼리 목록 조회

**Query Handler** - `ElephantQueryHandler`

- Repository를 통해 Read Model에서 데이터 조회

### Event Sourcing

모든 상태 변경은 Event로 기록되며, Aggregate는 이벤트를 재생(Replay)하여 상태를 복원할 수 있습니다.

## 📊 비즈니스 로직

### 상태 전이

코끼리는 다음 3가지 상태를 가집니다:

- **READY**: 준비 상태 (생성 직후)
- **ENTER**: 냉장고 안에 있는 상태
- **EXIT**: 냉장고에서 꺼낸 상태

### 비즈니스 규칙

1. 코끼리 생성 시 몸무게는 30kg 이상 200kg 이하여야 함
2. 100kg을 초과하는 코끼리는 냉장고에 넣을 수 없음
3. 이미 냉장고에 있는 코끼리는 다시 넣을 수 없음
4. 냉장고에 없는 코끼리는 꺼낼 수 없음
5. 100kg 초과 코끼리가 들어가려고 하면 `FailedEnterElephantEvent`가 발행되고, 자동으로 READY 상태로 복귀

## 🔌 API 엔드포인트

### 코끼리 관리 API (`/api/v1`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/create` | 코끼리 생성 |
| POST | `/enter/{id}` | 냉장고에 넣기 |
| POST | `/exit/{id}` | 냉장고에서 꺼내기 |
| GET | `/elephant/{id}` | 코끼리 정보 조회 |
| GET | `/elephants` | 전체 코끼리 목록 조회 |

### Event Replay API (`/api/admin`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/replay/{processingGroup}/{startDateTime}` | 특정 시점부터 이벤트 재생 |
| GET | `/replayAll` | 모든 이벤트 처음부터 재생 |

## 🎭 Event Handler 구조

### Processing Group

`ElephantEventHandler`는 "elephant" Processing Group으로 구성되어 있으며, `@AllowReplay` 어노테이션을 통해 이벤트 재생을 지원합니다.

### Event Handler 동작

1. Event가 발행되면 `ElephantEventHandler`가 이를 수신
2. Read Model(Database)을 업데이트
3. 필요시 추가 Command 발행 (예: FailedEnterElephantEvent → BackToReadyCommand)

### Reset Handler

`@ResetHandler`를 통해 이벤트 재생 전 데이터베이스를 초기화할 수 있습니다.

## ⚙️ 설정

### Axon Configuration

`AxonConfig` 클래스에서 XStream 설정을 통해 직렬화 보안 정책을 구성합니다.

### Swagger UI

OpenAPI 3.0 스펙으로 API 문서가 자동 생성되며, `/swagger-ui.html`에서 확인할 수 있습니다.

## 🚀 실행 방법

```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

애플리케이션 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## 📚 Axon Framework 핵심 개념 학습 포인트

### 1. Command와 Event의 분리

- Command는 의도를 나타내고 (넣으려고 함)
- Event는 사실을 나타냄 (넣어졌음)

### 2. Aggregate의 역할

- 비즈니스 로직의 중심
- Command를 받아 검증하고 Event 발행
- Event Sourcing Handler를 통해 상태 복원

### 3. Event Sourcing

- 모든 상태 변경을 Event로 저장
- Event를 재생하여 현재 상태 복원 가능
- 시간 여행(Time Travel) 가능

### 4. CQRS 패턴

- Command Side: 쓰기 작업, 비즈니스 로직
- Query Side: 읽기 작업, 성능 최적화
- 각각 독립적으로 확장 가능

### 5. Saga Pattern (간단한 형태)

- `FailedEnterElephantEvent` 발생 시 자동으로 `BackToReadyCommand` 발행
- Event-driven 방식의 보상 트랜잭션

## 📁 프로젝트 구조

```
src/main/java/com/junwoo/axonstudy/
├── aggregate/          # Aggregate Root
│   └── ElephantAggregate.java
├── command/            # Command 객체
│   ├── CreateElephantCommand.java
│   ├── EnterElephantCommand.java
│   ├── ExitElephantCommand.java
│   └── BackToReadyCommand.java
├── config/             # 설정 클래스
│   ├── AxonConfig.java
│   └── OpenAPIConfig.java
├── controller/         # REST API 컨트롤러
│   ├── APIController.java
│   └── ReplayEventsController.java
├── dto/                # Data Transfer Object
│   ├── ElephantDTO.java
│   └── StatusEnum.java
├── entity/             # JPA Entity (Read Model)
│   └── Elephant.java
├── events/             # Event 객체 및 Event Handler
│   ├── CreatedElephantEvent.java
│   ├── EnteredElephantEvent.java
│   ├── ExitedElephantEvent.java
│   ├── FailedEnterElephantEvent.java
│   ├── BackToReadyCompletedEvent.java
│   └── ElephantEventHandler.java
├── queries/            # Query 객체 및 Query Handler
│   ├── GetElephantQuery.java
│   └── ElephantQueryHandler.java
├── repository/         # JPA Repository
│   └── ElephantRepository.java
├── service/            # 비즈니스 서비스
│   └── ElephantService.java
└── vo/                 # Value Object
    └── ResultVO.java
```

## 🎓 학습 목표 달성

이 프로젝트를 통해 다음을 학습할 수 있습니다:

- Axon Framework의 기본 구조와 사용법
- CQRS 패턴의 실제 구현
- Event Sourcing의 동작 원리
- Command, Event, Query의 흐름
- Event Replay를 통한 시스템 복구
- Event-driven Architecture의 이해

## 📝 예제 시나리오

### 1. 코끼리 생성

```bash
POST /api/v1/create
{
  "name": "점보",
  "weight": 80
}
```

### 2. 냉장고에 넣기

```bash
POST /api/v1/enter/{elephantId}
```

### 3. 냉장고에서 꺼내기

```bash
POST /api/v1/exit/{elephantId}
```

### 4. 코끼리 정보 조회

```bash
GET /api/v1/elephant/{elephantId}
```

### 5. 이벤트 재생

```bash
GET /api/admin/replayAll
```

## 🔍 주요 학습 포인트

코드를 살펴볼 때 다음 부분에 주목하세요:

1. `ElephantAggregate`: Command가 어떻게 Event로 변환되는지
2. `ElephantEventHandler`: Event가 어떻게 Read Model을 업데이트하는지
3. `ElephantService`: Command Gateway와 Query Gateway의 사용법
4. `ReplayEventsController`: Event Sourcing의 강력한 기능인 재생(Replay)