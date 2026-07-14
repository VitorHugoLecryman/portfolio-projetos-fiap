# 🏥 HealthTech-ESG — Gestão Inteligente de Resíduos Hospitalares

> Sistema de monitoramento e controle automatizado de descartes de resíduos em ambiente hospitalar, alinhado aos pilares E (Environmental/Ambiental) e G (Governance/Governança) do ESG.

---

## 📝 O Problema

A gestão inadequada de resíduos de serviços de saúde (RSS) apresenta riscos graves à saúde pública e ao meio ambiente. Hospitais enfrentam desafios críticos como:
* **Falta de Rastreabilidade:** Dificuldade em documentar e auditar precisamente quem descartou, quanto e onde.
* **Descumprimento de Prazos (ANVISA):** Resíduos perigosos ou altamente infectantes possuem limites rígidos de tempo de armazenamento temporário. O controle manual desse tempo é suscetível a erros humanos graves.
* **Exposição e Transbordo:** Falta de controle em tempo real da capacidade dos coletores, gerando riscos de contaminação por transbordo.

---

## 💡 A Solução: Automação e Compliance ESG

A solução consiste em uma arquitetura de banco de dados relacional robusta integrada com **gatilhos e automações via PL/SQL (Oracle)**, removendo o erro humano das operações mais sensíveis:

1. **Rastreabilidade Total:** Todo descarte é registrado com timestamp preciso, peso, coletor utilizado e responsável pelo descarte.
2. **Prevenção de Transbordo (Pilar E):** Quando um coletor atinge 100% de sua capacidade máxima, o sistema altera seu status automaticamente para `CHEIO`, bloqueando novos descartes físicos até a sua coleta física.
3. **Prazos Automatizados:** Alertas preventivos são disparados antes do tempo máximo de armazenamento (conforme classe ANVISA) expirar.
4. **Governança e Auditoria (Pilar G):** Geração de histórico e registros automáticos de auditoria imutáveis sempre que há um evento crítico (bloqueios, alertas, descartes inadequados).

---

## 🏗️ Modelagem de Dados e MER

O banco de dados foi projetado para conectar de forma ágil as operações diárias dos coletores com a gestão central de conformidade.

*Dica: Você pode encontrar o diagrama do Modelo Entidade-Relacionamento detalhado no arquivo `MER_Gestao_Residuos_Hospitalares_ESG.pdf` na raiz desta pasta.*

### Principais Entidades:
* **HOSPITAL:** Cadastro das unidades hospitalares e responsáveis técnicos.
* **TIPO_RESIDUO:** Classificação oficial da ANVISA (A1, A2, B, C, D, E), periculosidade (Alta, Média, Baixa) e limite de armazenamento (em horas).
* **COLETOR_RESIDUO:** Sensores físicos que monitoram localização, nível atual de carga (kg), capacidade total e status.
* **DESCARTE_RESIDUO:** Histórico de operações de descarte realizadas pela equipe médica/operacional.
* **ALERTA_COLETA:** Central de controle que recebe os logs automatizados de auditoria e alertas críticos gerados pelo sistema.

---

## 🛠️ Automações Implementadas (Scripts PL/SQL)

A inteligência da aplicação reside no código contido em `Scripts_PLSQL_Gestao_Residuos_ESG.sql`. Foram desenvolvidas estruturas avançadas em banco de dados:

* **Triggers de Auditoria:** Responsáveis por atualizar instantaneamente o volume do coletor após um descarte e verificar se o limite de capacidade foi atingido.
* **Bloqueio Automático:** Triggers que impedem qualquer operação de gravação ou alteração de status inadequada, garantindo conformidade com regras sanitárias.
* **Procedures de Alerta:** Sub-rotinas escaláveis para varredura e notificação ativa de coletores críticos próximos de vencer o limite temporal.

---

## 💻 Tecnologias Utilizadas

* **Oracle SQL & PL/SQL:** Criação de tabelas, relacionamentos, Sequences, Triggers, Functions e Procedures.
* **dbdiagram.io / Oracle Data Modeler:** Modelação conceitual e lógica do MER.