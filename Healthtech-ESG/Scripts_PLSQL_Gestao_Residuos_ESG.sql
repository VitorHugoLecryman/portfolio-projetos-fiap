-- ============================================================
-- PROJETO HEALTHTECH - GESTÃO DE RESÍDUOS HOSPITALARES (ESG)
-- Startup One - Sistema de Monitoramento Inteligente
-- ============================================================

-- ============================================================
-- 1. CRIAÇÃO DAS TABELAS
-- ============================================================

-- Tabela HOSPITAL
CREATE TABLE HOSPITAL (
    id_hospital     NUMBER PRIMARY KEY,
    nome            VARCHAR2(100) NOT NULL,
    endereco        VARCHAR2(200),
    cidade          VARCHAR2(50),
    responsavel     VARCHAR2(100)
);

-- Tabela TIPO_RESIDUO (classificação ANVISA)
CREATE TABLE TIPO_RESIDUO (
    id_tipo         NUMBER PRIMARY KEY,
    descricao       VARCHAR2(100) NOT NULL,
    classe_anvisa   VARCHAR2(10) NOT NULL,  -- A1, A2, B, C, D, E
    tempo_max_arm   NUMBER NOT NULL,         -- tempo máximo armazenamento em horas
    periculosidade  VARCHAR2(20) NOT NULL    -- ALTO, MEDIO, BAIXO
);

-- Tabela COLETOR_RESIDUO
CREATE TABLE COLETOR_RESIDUO (
    id_coletor      NUMBER PRIMARY KEY,
    id_hospital     NUMBER NOT NULL,
    id_tipo         NUMBER NOT NULL,
    localizacao     VARCHAR2(100),
    capacidade_kg   NUMBER(10,2) NOT NULL,
    nivel_atual_kg  NUMBER(10,2) DEFAULT 0,
    status          VARCHAR2(20) DEFAULT 'ATIVO',  -- ATIVO, MANUTENCAO, CHEIO
    ultima_coleta   DATE,
    data_instalacao DATE DEFAULT SYSDATE,
    CONSTRAINT fk_coletor_hospital FOREIGN KEY (id_hospital) REFERENCES HOSPITAL(id_hospital),
    CONSTRAINT fk_coletor_tipo FOREIGN KEY (id_tipo) REFERENCES TIPO_RESIDUO(id_tipo),
    CONSTRAINT chk_status CHECK (status IN ('ATIVO', 'MANUTENCAO', 'CHEIO'))
);

-- Tabela DESCARTE_RESIDUO
CREATE TABLE DESCARTE_RESIDUO (
    id_descarte         NUMBER PRIMARY KEY,
    id_coletor          NUMBER NOT NULL,
    peso_kg             NUMBER(10,2) NOT NULL,
    data_descarte       TIMESTAMP DEFAULT SYSTIMESTAMP,
    setor_origem        VARCHAR2(50),
    responsavel_descarte VARCHAR2(100),
    CONSTRAINT fk_descarte_coletor FOREIGN KEY (id_coletor) REFERENCES COLETOR_RESIDUO(id_coletor)
);

-- Tabela ALERTA_COLETA
CREATE TABLE ALERTA_COLETA (
    id_alerta       NUMBER PRIMARY KEY,
    id_coletor      NUMBER NOT NULL,
    tipo_alerta     VARCHAR2(20) NOT NULL,  -- CAPACIDADE, VENCIMENTO, CRITICO
    mensagem        VARCHAR2(500),
    data_alerta     TIMESTAMP DEFAULT SYSTIMESTAMP,
    status          VARCHAR2(20) DEFAULT 'PENDENTE',  -- PENDENTE, ENVIADO, RESOLVIDO
    destinatario    VARCHAR2(100),
    CONSTRAINT fk_alerta_coletor FOREIGN KEY (id_coletor) REFERENCES COLETOR_RESIDUO(id_coletor),
    CONSTRAINT chk_tipo_alerta CHECK (tipo_alerta IN ('CAPACIDADE', 'VENCIMENTO', 'CRITICO')),
    CONSTRAINT chk_status_alerta CHECK (status IN ('PENDENTE', 'ENVIADO', 'RESOLVIDO'))
);

-- Sequences para IDs automáticos
CREATE SEQUENCE seq_hospital START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_tipo_residuo START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_coletor START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_descarte START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_alerta START WITH 1 INCREMENT BY 1;

-- ============================================================
-- 2. INSERÇÃO DE DADOS (5-10 registros por tabela)
-- ============================================================

-- Hospitais
INSERT INTO HOSPITAL VALUES (seq_hospital.NEXTVAL, 'Hospital São Paulo', 'Rua Napoleão de Barros, 715', 'São Paulo', 'Dr. Carlos Silva');
INSERT INTO HOSPITAL VALUES (seq_hospital.NEXTVAL, 'Hospital das Clínicas', 'Av. Dr. Enéas Carvalho, 255', 'São Paulo', 'Dra. Maria Santos');
INSERT INTO HOSPITAL VALUES (seq_hospital.NEXTVAL, 'Hospital Santa Casa', 'Rua Dr. Cesário Mota Jr, 112', 'São Paulo', 'Dr. João Oliveira');
INSERT INTO HOSPITAL VALUES (seq_hospital.NEXTVAL, 'Hospital Albert Einstein', 'Av. Albert Einstein, 627', 'São Paulo', 'Dra. Ana Costa');
INSERT INTO HOSPITAL VALUES (seq_hospital.NEXTVAL, 'Hospital Sírio-Libanês', 'Rua Dona Adma Jafet, 91', 'São Paulo', 'Dr. Pedro Lima');

-- Tipos de Resíduo (classificação ANVISA RDC 222/2018)
INSERT INTO TIPO_RESIDUO VALUES (seq_tipo_residuo.NEXTVAL, 'Infectante - Cultura de Microrganismos', 'A1', 24, 'ALTO');
INSERT INTO TIPO_RESIDUO VALUES (seq_tipo_residuo.NEXTVAL, 'Infectante - Bolsas de Sangue', 'A1', 24, 'ALTO');
INSERT INTO TIPO_RESIDUO VALUES (seq_tipo_residuo.NEXTVAL, 'Químico - Medicamentos Vencidos', 'B', 72, 'MEDIO');
INSERT INTO TIPO_RESIDUO VALUES (seq_tipo_residuo.NEXTVAL, 'Radioativo', 'C', 48, 'ALTO');
INSERT INTO TIPO_RESIDUO VALUES (seq_tipo_residuo.NEXTVAL, 'Comum - Reciclável', 'D', 168, 'BAIXO');
INSERT INTO TIPO_RESIDUO VALUES (seq_tipo_residuo.NEXTVAL, 'Perfurocortante - Agulhas e Lâminas', 'E', 48, 'ALTO');

-- Coletores de Resíduo
INSERT INTO COLETOR_RESIDUO VALUES (seq_coletor.NEXTVAL, 1, 1, 'UTI - Ala Norte', 50.00, 15.00, 'ATIVO', SYSDATE-5, SYSDATE-180);
INSERT INTO COLETOR_RESIDUO VALUES (seq_coletor.NEXTVAL, 1, 6, 'Centro Cirúrgico', 30.00, 25.00, 'ATIVO', SYSDATE-2, SYSDATE-180);
INSERT INTO COLETOR_RESIDUO VALUES (seq_coletor.NEXTVAL, 2, 2, 'Banco de Sangue', 40.00, 10.00, 'ATIVO', SYSDATE-3, SYSDATE-120);
INSERT INTO COLETOR_RESIDUO VALUES (seq_coletor.NEXTVAL, 2, 3, 'Farmácia Central', 25.00, 5.00, 'ATIVO', SYSDATE-10, SYSDATE-120);
INSERT INTO COLETOR_RESIDUO VALUES (seq_coletor.NEXTVAL, 3, 5, 'Refeitório', 100.00, 45.00, 'ATIVO', SYSDATE-1, SYSDATE-90);
INSERT INTO COLETOR_RESIDUO VALUES (seq_coletor.NEXTVAL, 3, 1, 'Laboratório', 35.00, 30.00, 'ATIVO', SYSDATE-1, SYSDATE-90);
INSERT INTO COLETOR_RESIDUO VALUES (seq_coletor.NEXTVAL, 4, 4, 'Radiologia', 20.00, 8.00, 'ATIVO', SYSDATE-7, SYSDATE-60);
INSERT INTO COLETOR_RESIDUO VALUES (seq_coletor.NEXTVAL, 5, 6, 'Emergência', 40.00, 35.00, 'ATIVO', SYSDATE-1, SYSDATE-45);

-- Descartes de Resíduo
INSERT INTO DESCARTE_RESIDUO VALUES (seq_descarte.NEXTVAL, 1, 5.00, SYSTIMESTAMP-2, 'UTI', 'Enf. Paula Souza');
INSERT INTO DESCARTE_RESIDUO VALUES (seq_descarte.NEXTVAL, 1, 3.50, SYSTIMESTAMP-1, 'UTI', 'Enf. Roberto Dias');
INSERT INTO DESCARTE_RESIDUO VALUES (seq_descarte.NEXTVAL, 2, 8.00, SYSTIMESTAMP-1, 'Centro Cirúrgico', 'Enf. Carla Mendes');
INSERT INTO DESCARTE_RESIDUO VALUES (seq_descarte.NEXTVAL, 3, 4.00, SYSTIMESTAMP-3, 'Banco de Sangue', 'Téc. Marcos Alves');
INSERT INTO DESCARTE_RESIDUO VALUES (seq_descarte.NEXTVAL, 5, 20.00, SYSTIMESTAMP-1, 'Refeitório', 'Aux. José Santos');
INSERT INTO DESCARTE_RESIDUO VALUES (seq_descarte.NEXTVAL, 6, 12.00, SYSTIMESTAMP, 'Laboratório', 'Téc. Fernanda Lima');

-- Alertas de Coleta
INSERT INTO ALERTA_COLETA VALUES (seq_alerta.NEXTVAL, 2, 'CAPACIDADE', 'Coletor atingiu 83% da capacidade. Agendar coleta.', SYSTIMESTAMP-1, 'ENVIADO', 'compliance@hospital.com');
INSERT INTO ALERTA_COLETA VALUES (seq_alerta.NEXTVAL, 6, 'CAPACIDADE', 'Coletor atingiu 85% da capacidade. Coleta urgente.', SYSTIMESTAMP, 'PENDENTE', 'compliance@hospital.com');
INSERT INTO ALERTA_COLETA VALUES (seq_alerta.NEXTVAL, 8, 'CAPACIDADE', 'Coletor atingiu 87% da capacidade. Ação imediata.', SYSTIMESTAMP, 'PENDENTE', 'compliance@hospital.com');
INSERT INTO ALERTA_COLETA VALUES (seq_alerta.NEXTVAL, 1, 'VENCIMENTO', 'Resíduo próximo do tempo máximo de armazenamento.', SYSTIMESTAMP-2, 'RESOLVIDO', 'gestao.residuos@hospital.com');
INSERT INTO ALERTA_COLETA VALUES (seq_alerta.NEXTVAL, 7, 'VENCIMENTO', 'Material radioativo requer coleta em 12 horas.', SYSTIMESTAMP, 'PENDENTE', 'seguranca@hospital.com');

-- ============================================================
-- 3. TRIGGERS DE AUTOMAÇÃO ESG
-- ============================================================

-- ============================================================
-- TRIGGER 1: Atualiza nível do coletor após cada descarte
-- ============================================================
CREATE OR REPLACE TRIGGER TRG_ATUALIZA_NIVEL_COLETOR
AFTER INSERT ON DESCARTE_RESIDUO
FOR EACH ROW
DECLARE
    v_status VARCHAR2(20);
BEGIN
    -- Verifica se o coletor está ativo
    SELECT status INTO v_status 
    FROM COLETOR_RESIDUO 
    WHERE id_coletor = :NEW.id_coletor;
    
    IF v_status = 'ATIVO' THEN
        -- Atualiza o nível atual do coletor somando o peso descartado
        UPDATE COLETOR_RESIDUO
        SET nivel_atual_kg = nivel_atual_kg + :NEW.peso_kg
        WHERE id_coletor = :NEW.id_coletor;
    END IF;
END;
/

-- ============================================================
-- TRIGGER 2: Alerta automático quando coletor atinge 80%
-- ============================================================
CREATE OR REPLACE TRIGGER TRG_ALERTA_CAPACIDADE_80
AFTER UPDATE OF nivel_atual_kg ON COLETOR_RESIDUO
FOR EACH ROW
DECLARE
    v_percentual NUMBER;
    v_tipo_residuo VARCHAR2(100);
    v_hospital VARCHAR2(100);
BEGIN
    -- Calcula o percentual de ocupação
    v_percentual := (:NEW.nivel_atual_kg / :NEW.capacidade_kg) * 100;
    
    -- Se atingiu 80% mas ainda não está cheio, gera alerta
    IF v_percentual >= 80 AND v_percentual < 100 AND :NEW.status = 'ATIVO' THEN
        
        -- Busca informações para a mensagem
        SELECT t.descricao, h.nome 
        INTO v_tipo_residuo, v_hospital
        FROM COLETOR_RESIDUO c
        JOIN TIPO_RESIDUO t ON c.id_tipo = t.id_tipo
        JOIN HOSPITAL h ON c.id_hospital = h.id_hospital
        WHERE c.id_coletor = :NEW.id_coletor;
        
        -- Insere alerta de capacidade
        INSERT INTO ALERTA_COLETA (
            id_alerta, id_coletor, tipo_alerta, mensagem, 
            data_alerta, status, destinatario
        ) VALUES (
            seq_alerta.NEXTVAL,
            :NEW.id_coletor,
            'CAPACIDADE',
            'ATENÇÃO: Coletor de ' || v_tipo_residuo || ' no ' || v_hospital || 
            ' atingiu ' || ROUND(v_percentual, 1) || '% da capacidade (' || 
            :NEW.nivel_atual_kg || 'kg de ' || :NEW.capacidade_kg || 'kg). Agendar coleta.',
            SYSTIMESTAMP,
            'PENDENTE',
            'compliance@healthtech.com.br'
        );
    END IF;
END;
/

-- ============================================================
-- TRIGGER 3: Alerta crítico quando resíduo ultrapassa tempo máximo
-- ============================================================
CREATE OR REPLACE TRIGGER TRG_ALERTA_TEMPO_ARMAZENAMENTO
AFTER UPDATE OF ultima_coleta ON COLETOR_RESIDUO
FOR EACH ROW
DECLARE
    v_tempo_max NUMBER;
    v_horas_armazenado NUMBER;
    v_tipo_residuo VARCHAR2(100);
    v_periculosidade VARCHAR2(20);
BEGIN
    -- Busca tempo máximo de armazenamento do tipo de resíduo
    SELECT t.tempo_max_arm, t.descricao, t.periculosidade
    INTO v_tempo_max, v_tipo_residuo, v_periculosidade
    FROM TIPO_RESIDUO t
    WHERE t.id_tipo = :NEW.id_tipo;
    
    -- Calcula horas desde última coleta
    v_horas_armazenado := (SYSDATE - :NEW.ultima_coleta) * 24;
    
    -- Se ultrapassou 70% do tempo máximo, gera alerta preventivo
    IF v_horas_armazenado >= (v_tempo_max * 0.7) THEN
        INSERT INTO ALERTA_COLETA (
            id_alerta, id_coletor, tipo_alerta, mensagem,
            data_alerta, status, destinatario
        ) VALUES (
            seq_alerta.NEXTVAL,
            :NEW.id_coletor,
            'VENCIMENTO',
            'ALERTA CRÍTICO: Resíduo ' || v_tipo_residuo || ' (Periculosidade: ' || 
            v_periculosidade || ') está armazenado há ' || ROUND(v_horas_armazenado, 1) || 
            ' horas. Tempo máximo permitido: ' || v_tempo_max || ' horas. ' ||
            'Restam ' || ROUND(v_tempo_max - v_horas_armazenado, 1) || ' horas para coleta obrigatória.',
            SYSTIMESTAMP,
            'PENDENTE',
            'urgente.compliance@healthtech.com.br'
        );
    END IF;
END;
/

-- ============================================================
-- TRIGGER 4: Bloqueio automático quando atinge 100% + auditoria
-- ============================================================
CREATE OR REPLACE TRIGGER TRG_BLOQUEIO_COLETOR_CHEIO
AFTER UPDATE OF nivel_atual_kg ON COLETOR_RESIDUO
FOR EACH ROW
DECLARE
    v_percentual NUMBER;
    v_hospital VARCHAR2(100);
    v_tipo_residuo VARCHAR2(100);
BEGIN
    -- Calcula percentual de ocupação
    v_percentual := (:NEW.nivel_atual_kg / :NEW.capacidade_kg) * 100;
    
    -- Se atingiu ou ultrapassou 100%
    IF v_percentual >= 100 AND :OLD.status = 'ATIVO' THEN
        
        -- Busca informações para registro
        SELECT h.nome, t.descricao
        INTO v_hospital, v_tipo_residuo
        FROM COLETOR_RESIDUO c
        JOIN HOSPITAL h ON c.id_hospital = h.id_hospital
        JOIN TIPO_RESIDUO t ON c.id_tipo = t.id_tipo
        WHERE c.id_coletor = :NEW.id_coletor;
        
        -- Atualiza status para CHEIO (bloqueado)
        UPDATE COLETOR_RESIDUO
        SET status = 'CHEIO'
        WHERE id_coletor = :NEW.id_coletor;
        
        -- Gera alerta crítico para auditoria
        INSERT INTO ALERTA_COLETA (
            id_alerta, id_coletor, tipo_alerta, mensagem,
            data_alerta, status, destinatario
        ) VALUES (
            seq_alerta.NEXTVAL,
            :NEW.id_coletor,
            'CRITICO',
            'BLOQUEIO AUTOMÁTICO: Coletor #' || :NEW.id_coletor || ' de ' || v_tipo_residuo ||
            ' no ' || v_hospital || ' atingiu capacidade máxima (' || :NEW.nivel_atual_kg || 
            'kg). Coletor BLOQUEADO para novos descartes. COLETA IMEDIATA OBRIGATÓRIA. ' ||
            'Registro de auditoria gerado em ' || TO_CHAR(SYSTIMESTAMP, 'DD/MM/YYYY HH24:MI:SS') ||
            ' conforme RDC 222/2018 ANVISA.',
            SYSTIMESTAMP,
            'PENDENTE',
            'auditoria.ambiental@healthtech.com.br'
        );
    END IF;
END;
/

-- ============================================================
-- FIM DOS SCRIPTS
-- ============================================================