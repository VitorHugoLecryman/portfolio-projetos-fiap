# InovaGAB — Backend (API REST)

Plataforma de inovação corporativa do Grupo Águia Branca.
Challenge FIAP — Primeiro Semestre 2026 — Sprint 2.

**Integrante:** Vitor Hugo — RM 559349

API REST em **Java 21 + Spring Boot 3.3 + MongoDB 7**, com autenticação JWT, autorização por
perfil, regras de negócio, dashboard agregado, trilha de auditoria e integração com IA.

---

## Pré-requisitos

- **JDK 21**
- **Maven 3.9** (ou o Maven embutido do IntelliJ / Android Studio)
- **Docker** (para subir o MongoDB)

---

## Passo 1 — Subir o banco de dados

Nesta pasta:

```
docker compose up -d
```

Sobe um MongoDB 7 em `127.0.0.1:27017`. Confira com `docker ps`.

> Se preferir não usar Docker, basta ter um MongoDB local na porta 27017.

---

## Passo 2 — Subir a API

Ainda nesta pasta:

```
mvn spring-boot:run
```

Aguarde a mensagem `Started InovaGabApplication`.

| Recurso | Endereço |
|---|---|
| API | http://127.0.0.1:8080 |
| Swagger (documentação interativa) | http://127.0.0.1:8080/swagger-ui.html |
| Health check | http://127.0.0.1:8080/actuator/health |

Na primeira execução, a carga inicial cria automaticamente os três usuários de teste e uma
orientação estratégica de exemplo.

---

## Credenciais de acesso

Todos os usuários usam a senha **123456**.

| Perfil | E-mail | O que faz |
|---|---|---|
| Operador | operador@inovagab.com | Envia ideias, vê ranking e pontos |
| Gestor | gestor@inovagab.com | Faz a curadoria das ideias e gerencia projetos |
| Líder | lider@inovagab.com | Define orientações estratégicas e acompanha o dashboard |

---

## Inteligência artificial

A API integra o **Google Gemini** em dois endpoints. Para ativá-los, basta definir uma chave da
API do Gemini (gratuita em [Google AI Studio](https://aistudio.google.com/apikey)).

| Endpoint | Perfil | O que faz |
|---|---|---|
| `POST /api/ideias/{id}/avaliacao-ia` | Gestor | Nota de 0 a 100 para a ideia, com justificativa do alinhamento à orientação estratégica |
| `POST /api/dashboard/insights-ia` | Líder | Análise do portfólio com sugestões acionáveis |

Por segurança, a chave não fica no código-fonte. Defina a variável de ambiente antes de subir
a API:

```
$env:GEMINI_API_KEY = "sua-chave"      # Windows (PowerShell)
export GEMINI_API_KEY="sua-chave"      # Linux / macOS
```

> A integração é isolada: se o provedor estiver fora do ar ou a cota for excedida, os dois
> endpoints respondem **HTTP 503 com mensagem clara** e todo o restante da API continua
> funcionando normalmente.

---

## Rodar os testes automatizados

```
mvn test
```

São **32 testes** cobrindo as regras de negócio: pontuação, autoria, dupla aprovação, cálculo de
prioridade, orientação inativa, ranking, origem e conclusão de projetos, lucro, ROI e os
agregados do dashboard. Não é necessário ter o banco no ar para executá-los.

---

## Organização por camadas

```
src/main/java/br/com/fiap/inovagab/
├── config/       AppProperties, DataSeeder, OpenApiConfig
├── controller/   Auth, Orientacao, Ideia, Ponto, Projeto, Dashboard, Auditoria
├── domain/       entidades e enums (Perfil, StatusIdeia, Nivel, EtapaProjeto)
├── dto/          records de request e response, com jakarta.validation
├── exception/    exceções de negócio e GlobalExceptionHandler
├── repository/   interfaces MongoRepository
├── security/     JwtService, JwtAuthenticationFilter, SecurityConfig
└── service/      regras de negócio (Ponto, Orientacao, Ideia, Projeto, Dashboard, Ia, Auditoria)
```

O fluxo é sempre **Controller → Service → Repository**. Os controllers não contêm regra de
negócio, e toda operação de escrita registra auditoria.

---

## Variáveis de ambiente

Todas, exceto `GEMINI_API_KEY`, possuem valor padrão e são opcionais para execução local.

| Variável | Padrão |
|---|---|
| `MONGODB_URI` | `mongodb://127.0.0.1:27017/inovagab` |
| `JWT_SECRET` | valor de desenvolvimento definido no `application.yml` |
| `JWT_EXPIRATION_MINUTES` | `480` |
| `SEED_ENABLED` / `SEED_SENHA` | `true` / `123456` |
| `GEMINI_API_KEY` | sem padrão — necessária apenas para os recursos de IA |
| `GEMINI_MODEL` | `gemini-3.6-flash` |

---

## Documentação completa

O arquivo **`InovaGAB - Documentação Técnica.pdf`**, entregue junto, traz o diagrama de
arquitetura, a especificação de todos os endpoints (rota, método, payload e resposta), o modelo
de dados, a matriz de permissões por perfil e as regras de negócio.
