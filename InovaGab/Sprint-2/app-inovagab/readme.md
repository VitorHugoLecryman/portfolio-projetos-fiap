# InovaGAB — Aplicativo Android

Plataforma de inovação corporativa do Grupo Águia Branca.
Challenge FIAP — Primeiro Semestre 2026 — Sprint 2.

**Integrante:** Vitor Hugo — RM 559349

Aplicativo nativo em **Kotlin**, arquitetura **MVVM**, integrado ao backend por **Retrofit**
consumindo APIs REST.

> **Importante:** o aplicativo **não funciona sozinho**. Ele consome a API local, então o
> backend precisa estar no ar antes de abrir o app. As instruções do backend estão no `readme.md`
> do pacote `backend-inovagab`.

---

## Conteúdo deste pacote

| Item | O que é |
|---|---|
| `InovaGAB.apk` | Aplicativo compilado, pronto para instalar |
| `app/`, `gradle/`, `build.gradle.kts`, ... | Código-fonte completo do projeto Android |

---

## Opção A — Instalar o APK direto

O arquivo **`InovaGAB.apk`** está na raiz deste pacote.

| Propriedade | Valor |
|---|---|
| Pacote | `br.com.fiap.inovagab` |
| Versão | 1.0 |
| Android mínimo | 9.0 (API 28) |

**No emulador** (recomendado, pois o backend roda na mesma máquina):

```
adb install InovaGAB.apk
```

**Em celular físico:** copie o APK para o aparelho e instale, permitindo "fontes desconhecidas".
Depois conecte o celular por USB e execute, com o backend já no ar:

```
adb reverse tcp:8080 tcp:8080
```

Esse comando faz o `10.0.2.2:8080` do aplicativo alcançar o backend da sua máquina.

---

## Opção B — Abrir e rodar pelo Android Studio

1. Abra **esta pasta** no Android Studio
2. Aguarde o Gradle sincronizar as dependências
3. Inicie um emulador Android (API 28 ou superior)
4. Clique em **Run** (▶) ou pressione **Shift + F10**

O aplicativo já está configurado para acessar a API em `10.0.2.2:8080`, que é como o emulador
enxerga a máquina onde o backend está rodando. Não é necessário alterar nada.

> O arquivo `local.properties`, que aponta para o SDK do Android, **não acompanha o projeto**
> porque depende do caminho de instalação de cada máquina. O Android Studio o cria
> automaticamente ao abrir a pasta pela primeira vez.

---

## Credenciais de acesso

Todos os usuários usam a senha **123456**.

| Perfil | E-mail | O que faz no app |
|---|---|---|
| Operador | operador@inovagab.com | Envia ideias, vê ranking e histórico de pontos |
| Gestor | gestor@inovagab.com | Curadoria das ideias, avaliação por IA e gestão de projetos |
| Líder | lider@inovagab.com | Orientações estratégicas, dashboard e insights da IA |

---

## Roteiro rápido de demonstração

1. **Líder** — crie uma orientação estratégica (título, descrição, categoria e campanha)
2. **Operador** — envie uma ideia vinculada a essa orientação e receba +10 pontos
3. **Gestor** — avalie a ideia com IA (botão **Avaliar com IA**), aprove (o operador recebe +50
   pontos) e crie um projeto a partir dela
4. **Gestor** — mova o projeto de etapa e registre os resultados financeiros
5. **Líder** — acompanhe o ROI no dashboard e gere os **Insights da IA**

---

## Integração com o backend

Toda a comunicação passa pelo pacote `br.com.fiap.inovagab.network`:

| Arquivo | Responsabilidade |
|---|---|
| `ApiClient.kt` | Retrofit + OkHttp e o helper que converte `Call` em `onSuccess` / `onError` |
| `InovaGabApi.kt` | Interface Retrofit com todos os endpoints REST |
| `Dtos.kt` | DTOs espelhando o contrato da API |
| `Mappers.kt` | Conversão entre os rótulos do app e os enums da API |
| `SessionManager.kt` | Token JWT guardado em `EncryptedSharedPreferences` |
| `AuthInterceptor.kt` | Injeta o cabeçalho `Authorization: Bearer <token>` |

O aplicativo é **exclusivamente a camada de apresentação**: nenhuma regra de negócio, cálculo de
pontos ou agregação de dashboard acontece no dispositivo — tudo vem pronto do servidor.

A URL da API é definida em `app/build.gradle.kts`, no campo `API_BASE_URL`.

---

## Documentação completa

O arquivo **`InovaGAB - Documentação Técnica.pdf`**, entregue junto, traz o diagrama de
arquitetura, a especificação de todos os endpoints, o modelo de dados, a matriz de permissões e
as telas do aplicativo em funcionamento.
