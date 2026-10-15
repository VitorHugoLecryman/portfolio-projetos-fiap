# InovaGAB — Plataforma de Inovação Corporativa

Challenge FIAP (primeiro semestre de 2026, Sprint 2) para o **Grupo Águia Branca**.

Os colaboradores da linha de frente (motoristas, mecânicos, operadores de pátio) enviam ideias de
melhoria pelo aplicativo. Os gestores fazem a curadoria e a liderança acompanha o retorno das
inovações em um painel estratégico. A participação é gamificada com pontos e ranking.

**Integrante:** Vitor Hugo — RM 559349

## O que tem neste repositório

| Pasta | Conteúdo |
|---|---|
| [`backend-inovagab/`](backend-inovagab) | API REST em **Java 21 + Spring Boot 3.3 + MongoDB 7** |
| [`app-inovagab/`](app-inovagab) | Aplicativo Android em **Kotlin** (MVVM + Retrofit), com o APK pronto para instalar |
| `InovaGAB - Documentação Técnica.pdf` | Documentação completa: arquitetura, contrato da API, regras de negócio e telas |

## Destaques

- **Autenticação JWT** e autorização por perfil (Operador, Gestor, Líder) com Spring Security
- **Integração com IA (Google Gemini):** nota de 0 a 100 para cada ideia, com justificativa do
  alinhamento à estratégia, e insights automáticos sobre o portfólio de inovação
- **Regras de negócio no servidor:** pontuação, dupla aprovação, cálculo de prioridade, ROI
- **Trilha de auditoria** em todas as operações de escrita
- **32 testes automatizados** cobrindo as regras de negócio
- Documentação da API com **Swagger / OpenAPI**

## Como executar

As instruções detalhadas estão no `readme.md` de cada pasta. Em resumo:

1. Suba o MongoDB com `docker compose up -d` em `backend-inovagab/`.
2. Defina a variável `GEMINI_API_KEY` (necessária só para os recursos de IA).
3. Rode a API com `mvn spring-boot:run`.
4. Abra o app no emulador Android, que já aponta para a API local.
