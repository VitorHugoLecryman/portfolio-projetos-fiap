**FACULDADE DE INFORMÁTICA E ADMINISTRAÇÃO PAULISTA**

```
Vitor Hugo, RM: 559349 , 2TDSOA
```
## BioMonitor

```
São Paulo
2026
```

**FACULDADE DE INFORMÁTICA E ADMINISTRAÇÃO PAULISTA**

```
Vitor Hugo, RM: 559349 , 2TDSOA
```
## BioMonitor

```
Projeto de Atividade Avaliativa apresentado à
Faculdade de Informática e Administração
Paulista como requisito de nota para a
disciplina de Desenvolvimento Mobile
```
## (Android)

```
São Paulo
2026
```

## SUMÁRIO

- 1. OBJETIVO DO APLICATIVO
   - ● 1.1. Propósito Principal
   - ● 1.2. Contribuição Científica
   - ● 1.3. Impacto Ambiental e Social
   - ● 1.4. Público-Alvo
- 2. TECNOLOGIA ESCOLHIDA
- 3. APLICAÇÃO NO CONTEXTO ESG
   - ● 3.1. Environmental (Ambiental)
   - ● 3.2. Social
   - ● 3.3. Governance (Governança)
- 4. DESCRIÇÃO DETALHADA DAS TELAS
   - ● Tela 1: Splash Screen
   - ● Tela 2: Login
   - ● Tela 3: Cadastro
   - ● Tela 4: Início / Mapa
   - ● Tela 5: Registro de Novo Avistamento
   - ● Tela 6: Meus Registros
   - ● Tela 7: Detalhes do Avistamento
   - ● Tela 8: Perfil do Usuário
   - ● Tela 9: Gamificação e Ranking
- 5. ENDEREÇOS (HTTPS) DOS SERVIÇOS CONSUMIDOS
   - ● 5.1. iNaturalist API
   - ● 5.2. GBIF API
   - ● 5.3. Google Maps Platform
- 6. ARQUITETURA DO PROJETO
   - ● 6.1. Estrutura de Camadas (Data, Domain, Presentation)
   - ● 6.2. Injeção de Dependência e Utilitários
- 7. CONCLUSÕES E DIRECIONAMENTO FUTURO
- 8. PRÓXIMOS PASSOS ESTRATÉGICOS


## 1. OBJETIVO DO APLICATIVO

**1.1. Propósito Principal**

O **BioMonitor** é um aplicativo móvel desenvolvido em Android Nativo com o objetivo de
facilitar o registro e o monitoramento da biodiversidade local de maneira acessível e
intuitiva. Por meio de uma abordagem de **ciência cidadã** , o aplicativo capacita qualquer
indivíduo a contribuir ativamente para o mapeamento e a catalogação de espécies de fauna
e flora em sua região.

**1.2. Contribuição Científica**

Os dados georreferenciados e validados coletados pelos usuários são integrados a um
banco de dados compartilhado, servindo como recurso valioso para cientistas e
pesquisadores ambientais. Essa contribuição permite:

```
● Identificação de padrões de distribuição de espécies.
● Monitoramento longitudinal de populações biológicas.
● Detecção precoce de espécies invasoras ou em risco de extinção.
● Apoio à modelagem e à validação de estudos de longo prazo.
```
**1.3. Impacto Ambiental e Social**

A plataforma promove um impacto ambiental positivo significativo, atuando na:

```
● Conscientização sobre a importância da biodiversidade e dos ecossistemas locais.
● Geração de dados para subsidiar planos de conservação ambiental.
● Apoio a políticas públicas de preservação e manejo de áreas protegidas.
● Engajamento comunitário e senso de responsabilidade na proteção ambiental.
```

**1.4. Público-Alvo**

O BioMonitor destina-se a uma ampla gama de usuários interessados em biologia e
conservação:

```
● Cidadãos Comuns: Interessados em natureza, trilhas e atividades ao ar livre.
● Estudantes: De níveis fundamental a pós-graduação, como ferramenta de
aprendizado e pesquisa.
● Pesquisadores: Biólogos, ecólogos e cientistas ambientais que necessitam de
dados de campo.
● ONGs Ambientais: Organizações focadas em conservação, educação e ativismo
ambiental.
● Gestores Públicos: Responsáveis pela formulação e execução de políticas
ambientais.
```

## 2. TECNOLOGIA ESCOLHIDA

O projeto foi concebido para oferecer alto desempenho, manutenibilidade e uma experiência
de usuário moderna, adotando as seguintes tecnologias-chave:

```
Componente Tecnologia
```
```
Plataforma Android Nativo
```
```
Linguagem Kotlin
UI Framework Jetpack Compose
```
```
Arquitetura MVVM (Model-View-ViewModel) com
princípios de Clean Architecture
```
```
Persistência Local Room Database + SharedPreferences
Consumo de API Retrofit
```
```
Mapas Google Maps SDK
```
```
Câmera CameraX
```
```
Localização Google Location Services
Injeção de Dependência Hilt
```
```
Navegação Navigation Compose
```

## 3. APLICAÇÃO NO CONTEXTO ESG

O aplicativo BioMonitor possui um alinhamento estratégico e substancial com os três pilares
do _Environmental, Social and Governance_ (ESG), conforme detalhado a seguir:

**3.1. Environmental (Ambiental)**

A contribuição ambiental é central para o propósito do BioMonitor, manifestada através de:

```
● Registro e Catalogação de Espécies: Captura e validação colaborativa de
avistamentos de fauna e flora, incluindo fotografias georreferenciadas e histórico
temporal.
● Mapeamento de Áreas de Biodiversidade: Identificação e monitoramento de
hotspots e corredores ecológicos, permitindo a detecção de mudanças no
ecossistema local.
● Alertas sobre Espécies em Risco: Notificações e informações sobre o status de
conservação de espécies ameaçadas, conectando usuários a órgãos ambientais
para ações de proteção de habitats.
● Contribuição Direta para Pesquisas: Fornecimento de dados abertos para a
comunidade científica, integrando-se a bases de dados globais e suportando
estudos de longo prazo.
```
**3.2. Social**

O impacto social do projeto foca na educação, engajamento e inclusão, promovendo:

```
● Educação Ambiental para a Comunidade: Disponibilização de informações sobre
espécies locais, conteúdo educativo sobre ecossistemas e material didático,
promovendo a observação responsável.
● Engajamento Cidadão e Ciência Cidadã: Incentivo à participação ativa na coleta
de dados, fomentando um senso de pertencimento, responsabilidade e ações
coletivas de conservação.
● Conexão entre Comunidades e Pesquisadores: Estabelecimento de um canal de
comunicação direto, com feedback sobre as contribuições e participação em projetos
de pesquisa e eventos de observação.
● Gamificação para Incentivo: Utilização de um sistema de pontos, níveis, badges e
desafios semanais e mensais para motivar a contribuição contínua e aprimorar a
qualidade dos registros.
```

**3.3. Governance (Governança)**

O pilar de Governança é atendido pela garantia de transparência, parcerias e qualidade dos
dados:

```
● Dados Abertos para Políticas Públicas: Geração de relatórios e indicadores de
biodiversidade que podem subsidiar a criação de áreas protegidas e o
monitoramento de impactos ambientais por gestores.
● Transparência e Rastreabilidade: Metodologia de coleta clara, acesso público aos
dados agregados, rastreabilidade das contribuições e auditoria de qualidade dos
dados para garantir a integridade.
● Parcerias com Instituições de Pesquisa: Colaboração formal com universidades,
museus de história natural e institutos de pesquisa, assegurando a validação
científica das informações coletadas.
● Relatórios de Impacto: Produção de métricas de biodiversidade, tendências
temporais e comparativos regionais, demonstrando de forma clara a contribuição do
aplicativo para a conservação.
```
**Transparência e Qualidade dos Dados**

```
● Informações Coletadas:
○ Transparência total sobre as informações coletadas.
○ Metodologia de coleta clara e acessível.
● Acesso e Rastreabilidade:
○ Acesso público aos dados em formato agregado.
○ Rastreabilidade completa das contribuições.
● Garantia de Qualidade:
○ Auditoria contínua da qualidade dos dados.
```
**Parcerias e Validação Científica**

```
● Colaboração com universidades e institutos de pesquisa.
● Integração com museus de história natural.
● Validação científica dos dados.
```
**Relatórios de Impacto e Indicadores de Conservação**

```
● Elaboração de Relatórios de Impacto Ambiental.
● Monitoramento de métricas de biodiversidade.
● Análise de tendências temporais e comparativos regionais.
● Geração de indicadores de conservação.
```

## 4. DESCRIÇÃO DETALHADA DAS TELAS

**Tela 1: Splash Screen**

```
● Finalidade: Exibição inicial durante o carregamento do aplicativo, responsável por
preparar recursos essenciais e verificar a situação de login do usuário.
● Funcionalidades Principais:
○ Exibição animada do logotipo "BioMonitor".
○ Indicação visual de carregamento ( loading ).
○ Verificação de credenciais salvas ( SharedPreferences ).
○ Redirecionamento Automático:
■ Usuário autenticado: Acesso direto à Tela Home.
■ Usuário não autenticado: Redirecionamento para a Tela Login.
```
**Tela 2: Login**

```
● Finalidade: Autenticação para acesso ao aplicativo por usuários já registrados.
● Funcionalidades Principais:
○ Campo para inserção de e-mail com validação de formato.
○ Campo para inserção de senha (com opção de visualização/ocultação).
○ Verificação de preenchimento de campos obrigatórios.
○ Botão "Entrar" com feedback visual de processamento.
○ Links de Suporte: "Criar conta" (para novos cadastros) e "Esqueci minha
senha".
○ Autenticação por meio de credenciais salvas localmente
( SharedPreferences ).
○ Exibição de mensagens de erro claras.
● Regras de Validação:
○ E-mail: Formato de e-mail válido (utilizando expressão regular - regex).
○ Senha: Requisito de no mínimo 6 caracteres.
○ Verificação das credenciais armazenadas no Room Database.
```
**Tela 3: Cadastro**

```
● Finalidade: Criação de novas contas de acesso para novos usuários.
● Funcionalidades Principais:
○ Campos para: Nome Completo, E-mail (com validação de formato), Senha
(com indicador de força), Confirmação de Senha e Cidade/Região.
○ Checkbox para aceite dos termos de uso.
○ Botão "Cadastrar".
○ Armazenamento dos dados no Room Database.
○ Exibição de feedback de sucesso e subsequente redirecionamento.
● Regras de Validação: Nome: mínimo 3 caracteres Email: formato válido e único
Senha: mínimo 6 caracteres, com letras e números Confirmação de senha: deve
coincidir Cidade: campo obrigatório
```

**Regras de Validação para Cadastro:**

```
● Nome: Requer no mínimo 3 caracteres.
● Email: Deve estar em um formato válido e ser único (não pode estar em uso).
● Senha: Mínimo de 6 caracteres, incluindo letras e números.
● Confirmação de Senha: Deve ser idêntica à senha informada.
● Cidade: Preenchimento obrigatório.
```

**Tela 4: Início / Mapa**

```
Descrição:
```
```
É a tela principal do aplicativo SafeZone, que apresenta um mapa interativo com todos
os avistamentos de espécies registrados na área.
```
```
Funcionalidades Principais:
```
```
● Visualização do Mapa: Utiliza o Google Maps em tela cheia.
● Identificação de Espécies (Marcadores):
○ Os avistamentos são indicados por marcadores coloridos, cada cor
representando uma categoria:
■ Verde: Flora (plantas)
■ Azul: Aves
■ Laranja: Mamíferos
■ Roxo: Répteis/Anfíbios
■ Amarelo: Insetos
■ Branco: Outros
● Interatividade:
○ Permite a filtragem dos marcadores por categoria de espécie.
○ Agrupa os marcadores (cluster) quando o zoom está distante.
○ Exibe um popup com um preview das informações ao clicar em um marcador.
● Ações e Navegação:
○ Possui um Botão Flutuante (FAB) para iniciar um novo registro de
avistamento.
○ Inclui uma barra de navegação inferior.
● Localização:
○ Mostra a localização atual do usuário.
○ Dispõe de um botão para centralizar a visualização no usuário.
```

**Tela 5: Registro de Novo Avistamento**

```
Objetivo: Permitir o registro detalhado de um novo avistamento de espécie, incluindo
evidência fotográfica e informações contextuais.
```
```
Funcionalidades Principais:
```
```
● Captura de Imagem:
○ Tirar foto diretamente (via CameraX).
○ Selecionar imagem da galeria.
○ Visualização prévia da foto escolhida.
● Detalhes da Espécie:
○ Categoria: Seleção obrigatória (Ave, Mamífero, Réptil, Anfíbio, Peixe, Inseto,
Planta, Fungo, Outro).
○ Nome: Campo com autocompletar e sugestões integradas da API iNaturalist.
● Localização e Tempo:
○ Localização geográfica automática (GPS).
○ Opção de ajuste manual da localização em um mapa.
○ Seleção de Data/Hora (padrão: data/hora atual).
● Contexto Adicional:
○ Observações: Campo de texto livre para anotações detalhadas.
○ Habitat: Indicador para categorizar o ambiente (floresta, campo, água,
urbano).
● Persistência de Dados:
○ Botão "Salvar": Executa a validação dos campos.
○ Salva localmente no Room Database.
○ Sincroniza automaticamente com o servidor (quando a conexão estiver
disponível).
```

**Tela 6: Meus Registros**

**Descrição:** Esta tela apresenta uma lista completa dos avistamentos registrados pelo
usuário logado.

**Elementos e Funcionalidades:**

● **Lista de Avistamentos:** Disposição em lista vertical com _cards_ informativos.
● **Conteúdo do** **_Card_** **:**
○ Miniatura da foto do avistamento.
○ Nome da espécie.
○ Categoria (representada por um ícone).
○ Data do registro.
○ Localização resumida.
○ Status de sincronização.
● **Organização:** Ordenação padrão por data, exibindo o registro mais recente
primeiro.
● **Ações na Lista:**
○ **Atualização:** _Pull-to-refresh_ para recarregar a lista.
○ **Exclusão:** Gesto de _swipe_ para a lateral com solicitação de confirmação
para excluir um registro.
● **Filtros:** Opções de filtragem disponíveis por:
○ Categoria.
○ Período.
○ Status de sincronização.
● **Busca:** Campo dedicado para pesquisa por nome da espécie.
● **Estado Vazio:** Uma ilustração e uma Chamada para Ação ( _CTA_ ) são exibidas caso
não haja registros.
● **Navegação:** O clique em um _card_ direciona para a tela de detalhes do registro.


**Tela 7: Detalhes do Avistamento (Registro)**

```
Esta tela exibe todas as informações referentes a um avistamento específico registrado,
oferecendo uma visão completa e recursos interativos.
```
```
Conteúdo Principal:
```
```
● Mídia:
○ Foto em destaque com funcionalidade de tela cheia e zoom.
○ Galeria de fotos, caso haja múltiplos arquivos.
● Detalhes do Registro:
○ Nome da espécie.
○ Nome científico (se aplicável).
○ Categoria do avistamento.
○ Data e hora exatas.
○ Localização: endereço e coordenadas GPS.
○ Observações inseridas pelo usuário.
○ Status de conservação da espécie (informação adicional).
○ Indicador de validação pela comunidade.
```
**Funcionalidades e Ações:**

```
● Localização: Mini mapa exibindo o ponto exato do avistamento.
● Informações Adicionais: Dados complementares sobre a espécie (obtidos via API).
● Gestão do Registro:
○ Botão "Editar" o registro.
○ Botão "Excluir" o registro, com etapa de confirmação.
● Compartilhamento: Botão dedicado com opções para:
○ Redes sociais.
○ WhatsApp.
○ Email.
○ Cópia do link do registro.
```

**Tela 8: Perfil do Usuário**

```
Objetivo: Exibir as informações pessoais do usuário e suas métricas de contribuição na
plataforma.
```
```
Detalhes do Perfil:
```
```
● Foto de perfil (com opção de edição)
● Nome de usuário
● E-mail
● Localização (Cidade/Região)
● Data de adesão ("Membro desde")
```
**Estatísticas de Contribuição:**

```
● Total de registros efetuados
● Diversidade de espécies identificadas
● Categorias de registro mais frequentes
● Sequência de dias consecutivos com registro
● Posição no ranking geral
● Prévia das Insígnias (Badges) conquistadas
```
**Ações e Configurações:**

```
● Botão para Editar Perfil
● Seção de Configurações:
○ Gerenciamento de Notificações
○ Opções de Privacidade
○ Seleção de Unidades de Medida
○ Escolha de Tema (Claro/Escuro)
○ Definição de Idioma
● Botão de Logout
● Botão para Excluir Conta (requer confirmação)
● Link para os Termos de Uso e Política de Privacidade
```
[IMAGEM: Visualização da Tela Perfil do Usuário]


**Tela 9: Gamificação e Ranking**

```
Objetivo: Engajar usuários e incentivar a contribuição através de um sistema de
gamificação.
```
```
Funcionalidades Principais:
```
1. **Ranking de Usuários:**
    ○ **Períodos:** Top 10 Semanal, Top 10 Mensal, Top 10 Geral.
    ○ **Segmentação:** Ranking por Região.
    ○ **Visualização:** Perfil resumido de cada usuário no ranking e Posição atual do
       usuário logado.
2. **Sistema de Badges (Conquistas):**
    ○ **Iniciante:** Primeiro registro.
    ○ **Observador:** 10 registros.
    ○ **Fotógrafo:** 50 registros.
    ○ **Ornitólogo:** 20 aves diferentes registradas.
    ○ **Botânico:** 20 plantas diferentes registradas.
    ○ **Explorador:** Registros em 5 cidades.
    ○ **Mestre:** 100 registros.
    ○ **Lenda:** 500 registros.
3. **Desafios Semanais:**
    ○ Exemplos: "Registre 5 espécies de aves", "Encontre uma espécie nova para
       você", "Faça um registro em área urbana".
4. **Sistema de Pontuação:**
    ○ **Registro comum:** 10 pontos.
    ○ **Espécie nova (para o usuário):** 25 pontos.
    ○ **Espécie rara:** 50 pontos.
    ○ **Desafio completado:** 100 pontos.
5. **Progresso:** Barra de progresso para o próximo nível.


## 5. ENDEREÇOS (HTTPS) DOS SERVIÇOS CONSUMIDOS

## Principais

1. **iNaturalist API (Biodiversidade)**
    ○ **Base URL:**
       [https://api.inaturalist.org/v1/](https://api.inaturalist
       .org/v1/)
    ○ **Documentação:**
       [https://api.inaturalist.org/v1/docs/](https://api.inatur
       alist.org/v1/docs/)
    ○ **Endpoints Utilizados:**
       ■ GET /taxa/autocomplete: Sugestões de espécies.
       ■ GET /taxa/{id}: Detalhes de uma espécie.
       ■ GET /observations: Listar observações.
       ■ POST /observations: Criar observação.
       ■ GET /identifications: Identificações da comunidade.
    ○ **Exemplo de Requisição (Busca de Espécies):**
       @GET("taxa/autocomplete")
    ○ suspend fun searchSpecies(
    ○ @Query("q") query: String,
    ○ @Query("per_page") perPage: Int = 10
    ○ ): Response
2. **GBIF API (Global Biodiversity Information Facility)**
    ○ **Base URL:**
       [https://api.gbif.org/v1/](https://api.gbif.org/v1/)
    ○ **Documentação:**
       [https://www.gbif.org/developer/summary](https://www.gbif
       .org/developer/summary)
    ○ **Endpoints Utilizados:**
       ■ GET /species/search: Buscar espécies.
       ■ GET /species/{key}: Detalhes da espécie.
       ■ GET /occurrence/search: Buscar ocorrências.
       ■ GET /species/{key}/media: Mídia da espécie.
    ○ **Exemplo de Requisição (Busca de Espécies):**
       @GET("species/search")
    ○ suspend fun searchSpecies(
    ○ @Query("q") query: String,
    ○ @Query("limit") limit: Int = 10): Response


**3. Google Maps Platform**

```
● URL Base:
[https://maps.googleapis.com/maps/api/](https://maps.googleapi
s.com/maps/api/)
● Documentação:
[https://developers.google.com/maps/documentation](https://dev
elopers.google.com/maps/documentation)
● Serviços Utilizados:
○ Maps SDK for Android
○ Geocoding API
○ Places API
● Configuração no Projeto (dependências no build.gradle.kts):
implementation("com.google.android.gms:play-services-maps:18.2.0")
● implementation("com.google.android.gms:play-services-location:21.1.0")
```

## 6. ARQUITETURA DO PROJETO

```
O projeto adota uma arquitetura híbrida, combinando o padrão MVVM
(Model-View-ViewModel) para a apresentação com a filosofia da Clean Architecture para
organizar o código em camadas bem definidas.Estrutura de Diretórios (app/)
```
```
A organização do código reflete a separação de responsabilidades:
Diretório Conteúdo Principal
```
```
data/
Camada de Acesso a Dados (Local e
Remota)
```
```
domain/ Camada^ de^ Lógica^ de^ Negócio^ Pura^
```
```
presentation/ Camada^ de^ Interface^ do^ Usuário^ (UI)^
```
```
di/ Módulos^ de^ Injeção^ de^ Dependência^
(Hilt)
```
```
util/ Classes^ de^ Utilitários^ e^ Auxiliares^
```
```
Detalhamento das Camadas Data Layer (Acesso a Dados)
```
```
Responsável por todas as operações de dados, sejam elas persistidas localmente ou
obtidas de fontes remotas.
```
```
● local/database : Configuração do Room (Database, DAOs e Entities) para
persistência local.
● local/preferences : Implementação de SharedPreferences para dados de
sessão ou configurações.
● remote/api : Interfaces Retrofit para comunicação com APIs externas (ex:
BiodiversityApi, GbifApi).
● remote/dto : Data Transfer Objects (DTOs) utilizados para o mapeamento de
respostas JSON.
● repository : Implementações concretas dos contratos de repositório
(SightingRepositoryImpl, UserRepositoryImpl).
```

**Domain Layer (Lógica de Negócio)**

```
Contém a lógica de negócio central e é totalmente independente de frameworks ou
interfaces de usuário.
```
```
● model : Classes de domínio (POJOs/Data Classes) que representam as entidades
de negócio (Sighting, User, Species, Badge).
● repository : Interfaces que definem os contratos dos repositórios (o que eles
devem fazer), implementadas na Camada Data.
● usecase : Casos de uso. Encapsulam regras de negócio específicas, coordenando
dados e modelos para realizar operações (ex: LoginUseCase,
CreateSightingUseCase).
```
**Presentation Layer (Interface do Usuário)**

```
Gerencia a exibição e a interação com o usuário, seguindo o padrão MVVM.
```
```
● screens : Telas do aplicativo. Cada tela geralmente é composta por um Composable
(Screen.kt) e seu respectivo ViewModel (ex: LoginScreen.kt e
LoginViewModel.kt).
● components : Componentes de UI reutilizáveis (ex: BioButton, SightingCard,
CategoryChip).
● navigation : Lógica de navegação do aplicativo (NavGraph, Screen,
BottomNavBar).
● theme : Definições de estilo global (Cores, Tipografia e Tema).
```
**DI (Injeção de Dependência - Hilt)**

```
Módulos Hilt para provisionar as dependências em todo o projeto.
```
```
● AppModule : Dependências de escopo de aplicação.
● DatabaseModule : Configuração e fornecimento do Room Database.
● NetworkModule : Configuração e fornecimento do Retrofit e suas interfaces.
● RepositoryModule : Binding das interfaces de repositório (Domain) com suas
implementações (Data).
```

**Util (Utilitários)**

```
Classes auxiliares genéricas:
```
```
● Constants.kt
● Extensions.kt
● LocationHelper.kt
● ImageHelper.kt
```

## 7. CONCLUSÕES E DIRECIONAMENTO FUTURO

```
Contribuição para os Objetivos ESG do BioMonitor:
```
```
O aplicativo BioMonitor se alinha e contribui significativamente para os pilares
Ambiental, Social e de Governança (ESG), promovendo:
```
```
● Impacto Ambiental:
○ Preservação ambiental por meio do engajamento cívico na observação e
registro de espécies.
○ Geração de dados científicos de alto valor para pesquisas de conservação.
○ Aumento da conscientização pública sobre a relevância da biodiversidade
local.
● Impacto Social:
○ Democratização da ciência cidadã, tornando o conhecimento científico
acessível a todos.
○ Educação da comunidade sobre a fauna e flora locais e a importância da sua
proteção.
○ Criação de uma rede de colaboradores ativos na defesa da biodiversidade.
● Impacto na Governança:
○ Fornecimento de dados transparentes para apoiar e embasar políticas
públicas de conservação.
○ Estabelecimento de parcerias estratégicas com instituições de pesquisa e
órgãos ambientais.
○ Produção de relatórios periódicos que quantificam o impacto e a contribuição
da comunidade.
```
**Alinhamento com os Objetivos de Desenvolvimento Sustentável (ODS) da ONU:**

```
O BioMonitor está diretamente associado aos seguintes ODS:
```
```
● ODS 4 - Educação de Qualidade: Foco na educação ambiental.
● ODS 11 - Cidades e Comunidades Sustentáveis: Monitoramento da
biodiversidade em áreas urbanas.
● ODS 13 - Ação Contra a Mudança Global do Clima: Coleta de dados relevantes
sobre os impactos climáticos.
● ODS 15 - Vida Terrestre: Suporte direto às iniciativas de conservação da
biodiversidade.
● ODS 17 - Parcerias e Meios de Implementação: Conexão entre cidadãos,
pesquisadores e gestores públicos.
```

**Próximos Passos Estratégicos:**

1. Validação e _feedback_ do protótipo com usuários-alvo.
2. Formalização de parcerias com ONGs e universidades.
3. Desenvolvimento do Produto Mínimo Viável (MVP) conforme o cronograma.
4. Realização de testes beta com um grupo de usuários selecionados.
5. Lançamento oficial na Google Play Store.

Expansão de funcionalidades baseada nas sugestões e necessidades dos usuários.


