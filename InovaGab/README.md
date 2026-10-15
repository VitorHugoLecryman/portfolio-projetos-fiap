# 📱 InovaGAB - Plataforma de Inovação Corporativa

> Solução Mobile desenvolvida para o Grupo Águia Branca focada em capturar e gerenciar ideias de inovação da linha de frente operacional.

## 📝 O Problema
Em operações logísticas de grande escala, as melhores ideias de otimização muitas vezes se perdem na burocracia. Identificamos:
* Falta de engajamento da base operacional.
* Dificuldade da liderança em mensurar o ROI (Retorno sobre Investimento) das inovações.
* Ausência de um canal rápido, estruturado e sem fricção para reporte de melhorias.

## 💡 A Solução
O InovaGAB atua sob o conceito de "Zero Fricção". É um aplicativo nativo que gamifica o processo de inovação:
* **Para o Operador:** Interface simplificada em 3 passos para envio de ideias (com suporte a áudio/vídeo).
* **Para o Gestor:** Curadoria das ideias, com avaliação automática por IA (Sprint 2).
* **Para o Líder:** Dashboard gerencial com análise de viabilidade, impacto e priorização das sugestões.

🎥 **[Clique aqui para assistir ao vídeo de apresentação do App (YouTube)](https://www.youtube.com/watch?v=nfGvV9cl3aY)**

---

## 🚀 Sprint 2 — Backend próprio e Inteligência Artificial

📂 **[Ver código-fonte e documentação da Sprint 2](./Sprint-2)**

A Sprint 1 validou o conceito com o app conectado direto ao Firebase, mas a regra de negócio morava no celular: um APK modificado poderia atribuir pontos a si mesmo. A Sprint 2 resolveu isso na raiz, criando uma API própria:

* **Backend:** Java 21 + Spring Boot 3.3 + MongoDB 7, com autenticação **JWT** e autorização por perfil (Operador, Gestor, Líder)
* **IA (Google Gemini):** nota de 0 a 100 para cada ideia com justificativa do alinhamento à estratégia, e insights automáticos sobre o portfólio de inovação
* **Regras de negócio no servidor:** pontuação, dupla aprovação, prioridade, ROI e trilha de auditoria
* **32 testes automatizados** e documentação da API com Swagger
* **App Android** em Kotlin (MVVM + Retrofit) consumindo a API REST, com APK pronto em `Sprint-2/app-inovagab/`

---

## 🧩 Sprint 1 — Protótipo com Firebase

* **Linguagem:** Kotlin (Android Nativo), com Firebase (Authentication e Cloud Firestore)
* **Design:** Figma
* **Documentação:** O arquivo `InovaGAB - Documentação Técnica.pdf` desta pasta detalha a arquitetura, fluxo de telas e modelo de dados da Sprint 1.

*Nota: O arquivo instalável (`.apk`) da Sprint 1 está nesta pasta, para testes em dispositivos Android reais.*
