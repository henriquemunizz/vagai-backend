-- =====================================================================
-- Vagaí - Estrutura do banco (Oracle)
-- Execute em um esquema Oracle vazio destinado ao Vagaí.
--
-- Padrão de nomes das constraints:
--   pk_ = chave primária | fk_ = chave estrangeira | uq_ = única
--   ck_ = check          | idx_ = índice
-- Nomear as constraints faz o erro do Oracle dizer QUAL regra quebrou
-- (ex.: ORA-00001: unique constraint (UQ_AVALIACAO) violated)
-- em vez de um nome gerado tipo SYS_C0012345.
-- =====================================================================
-- Para recriar tudo do zero, descomente este bloco (filhas antes das mães):
-- DROP TABLE tb_usuario_favorito     CASCADE CONSTRAINTS PURGE;
-- DROP TABLE tb_regra_estacionamento CASCADE CONSTRAINTS PURGE;
-- DROP TABLE tb_avaliacao            CASCADE CONSTRAINTS PURGE;
-- DROP TABLE tb_reporte              CASCADE CONSTRAINTS PURGE;
-- DROP TABLE tb_estacionamento       CASCADE CONSTRAINTS PURGE;
-- DROP TABLE tb_usuario              CASCADE CONSTRAINTS PURGE;
-- DROP SEQUENCE sq_tb_usuario_favorito;
-- DROP SEQUENCE sq_tb_regra_estacionamento;
-- DROP SEQUENCE sq_tb_avaliacao;
-- DROP SEQUENCE sq_tb_reporte;
-- DROP SEQUENCE sq_tb_estacionamento;
-- DROP SEQUENCE sq_tb_usuario;
-- ---------------------------------------------------------------------
-- 1. TB_USUARIO
-- ---------------------------------------------------------------------
CREATE TABLE
    tb_usuario (
        id_usuario NUMBER (10) CONSTRAINT pk_usuario PRIMARY KEY,
        nm_usuario VARCHAR2 (40 CHAR) NOT NULL CONSTRAINT uq_usuario_nome UNIQUE,
        ds_senha VARCHAR2 (255 CHAR) NOT NULL,
        dt_cadastro DATE DEFAULT SYSDATE NOT NULL
    );

COMMENT ON TABLE tb_usuario IS 'Contas de motoristas';

COMMENT ON COLUMN tb_usuario.ds_senha IS 'Hash da senha no formato salt:hash (Base64). Nunca armazenar senha em texto puro';

CREATE SEQUENCE sq_tb_usuario START
WITH
    1 INCREMENT BY 1 NOCYCLE;

-- ---------------------------------------------------------------------
-- 2. TB_ESTACIONAMENTO
-- ---------------------------------------------------------------------
CREATE TABLE
    tb_estacionamento (
        id_estacionamento NUMBER (10) CONSTRAINT pk_estacionamento PRIMARY KEY,
        id_usuario_criador NUMBER (10) CONSTRAINT fk_estac_criador REFERENCES tb_usuario (id_usuario) ON DELETE SET NULL,
        nm_estacionamento VARCHAR2 (100 CHAR) NOT NULL,
        ds_estacionamento VARCHAR2 (255 CHAR),
        tp_estacionamento VARCHAR2 (20 CHAR) NOT NULL CONSTRAINT ck_estac_tipo CHECK (
            tp_estacionamento IN ('GRATUITO', 'ROTATIVO', 'PRIVADO')
        ),
        nr_latitude NUMBER (10, 7) NOT NULL CONSTRAINT ck_estac_latitude CHECK (nr_latitude BETWEEN -90 AND 90),
        nr_longitude NUMBER (10, 7) NOT NULL CONSTRAINT ck_estac_longitude CHECK (nr_longitude BETWEEN -180 AND 180),
        ds_endereco VARCHAR2 (255 CHAR),
        vl_preco NUMBER (10, 2) CONSTRAINT ck_estac_preco CHECK (vl_preco >= 0),
        ds_fonte VARCHAR2 (50 CHAR) CONSTRAINT ck_estac_fonte CHECK (ds_fonte IN ('OSM', 'USUARIO', 'TESTE')),
        ds_id_osm VARCHAR2 (50 CHAR) CONSTRAINT uq_estac_osm UNIQUE,
        dt_verificacao DATE,
        st_estacionamento NUMBER (1) DEFAULT 1 NOT NULL CONSTRAINT ck_estac_status CHECK (st_estacionamento IN (0, 1)),
        -- Local gratuito não pode ter preço maior que zero.
        CONSTRAINT ck_estac_gratuito CHECK (
            tp_estacionamento <> 'GRATUITO'
            OR NVL (vl_preco, 0) = 0
        ),
        -- Local importado do OpenStreetMap precisa guardar o id de origem.
        CONSTRAINT ck_estac_osm CHECK (
            ds_fonte <> 'OSM'
            OR ds_id_osm IS NOT NULL
        )
    );

COMMENT ON TABLE tb_estacionamento IS 'Locais colaborativos ou importados para o mapa';

COMMENT ON COLUMN tb_estacionamento.st_estacionamento IS '1 = ativo (aparece no mapa); 0 = inativo (exclusão lógica)';

COMMENT ON COLUMN tb_estacionamento.vl_preco IS 'Tarifa informada; NULL = preço desconhecido; 0 = gratuito';

COMMENT ON COLUMN tb_estacionamento.ds_fonte IS 'Origem do cadastro: OSM, USUARIO ou TESTE';

CREATE SEQUENCE sq_tb_estacionamento START
WITH
    1 INCREMENT BY 1 NOCYCLE;

-- ---------------------------------------------------------------------
-- 3. TB_REPORTE
-- ---------------------------------------------------------------------
CREATE TABLE
    tb_reporte (
        id_reporte NUMBER (10) CONSTRAINT pk_reporte PRIMARY KEY,
        id_usuario NUMBER (10) NOT NULL CONSTRAINT fk_reporte_usuario REFERENCES tb_usuario (id_usuario) ON DELETE CASCADE,
        id_estacionamento NUMBER (10) NOT NULL CONSTRAINT fk_reporte_estac REFERENCES tb_estacionamento (id_estacionamento) ON DELETE CASCADE,
        tp_reporte VARCHAR2 (20 CHAR) NOT NULL CONSTRAINT ck_reporte_tipo CHECK (tp_reporte IN ('VAGA_ENCONTRADA', 'SEM_VAGA')),
        ds_observacao VARCHAR2 (255 CHAR),
        dt_reporte DATE DEFAULT SYSDATE NOT NULL
    );

COMMENT ON TABLE tb_reporte IS 'Histórico de relatos de disponibilidade';

CREATE SEQUENCE sq_tb_reporte START
WITH
    1 INCREMENT BY 1 NOCYCLE;

-- Consulta mais comum do app: "últimos reportes deste local".
CREATE INDEX idx_reporte_estac_data ON tb_reporte (id_estacionamento, dt_reporte);

-- ---------------------------------------------------------------------
-- 4. TB_AVALIACAO
-- ---------------------------------------------------------------------
CREATE TABLE
    tb_avaliacao (
        id_avaliacao NUMBER (10) CONSTRAINT pk_avaliacao PRIMARY KEY,
        id_usuario NUMBER (10) NOT NULL CONSTRAINT fk_avaliacao_usuario REFERENCES tb_usuario (id_usuario) ON DELETE CASCADE,
        id_estacionamento NUMBER (10) NOT NULL CONSTRAINT fk_avaliacao_estac REFERENCES tb_estacionamento (id_estacionamento) ON DELETE CASCADE,
        nr_nota NUMBER (1) NOT NULL CONSTRAINT ck_avaliacao_nota CHECK (nr_nota BETWEEN 1 AND 5),
        ds_comentario VARCHAR2 (500 CHAR),
        dt_avaliacao DATE DEFAULT SYSDATE NOT NULL,
        -- Uma avaliação por usuário por local (para mudar a nota, faz UPDATE).
        CONSTRAINT uq_avaliacao UNIQUE (id_usuario, id_estacionamento)
    );

COMMENT ON TABLE tb_avaliacao IS 'Notas e comentários sobre locais (uma por usuário por local)';

CREATE SEQUENCE sq_tb_avaliacao START
WITH
    1 INCREMENT BY 1 NOCYCLE;

-- Acelera o cálculo da média de notas de um local.
CREATE INDEX idx_avaliacao_estac ON tb_avaliacao (id_estacionamento);

-- ---------------------------------------------------------------------
-- 5. TB_REGRA_ESTACIONAMENTO
-- Uma linha = uma regra em UM dia da semana.
-- "Segunda a sexta, 20h às 6h" vira 5 linhas (dias 1 a 5).
-- ---------------------------------------------------------------------
CREATE TABLE
    tb_regra_estacionamento (
        id_regra NUMBER (10) CONSTRAINT pk_regra PRIMARY KEY,
        id_estacionamento NUMBER (10) NOT NULL CONSTRAINT fk_regra_estac REFERENCES tb_estacionamento (id_estacionamento) ON DELETE CASCADE,
        tp_regra VARCHAR2 (10 CHAR) NOT NULL CONSTRAINT ck_regra_tipo CHECK (tp_regra IN ('PERMITIDO', 'PROIBIDO')),
        ds_regra VARCHAR2 (255 CHAR) NOT NULL,
        nr_dia_semana NUMBER (1) NOT NULL CONSTRAINT ck_regra_dia CHECK (nr_dia_semana BETWEEN 1 AND 7),
        hr_inicio VARCHAR2 (5 CHAR) NOT NULL CONSTRAINT ck_regra_inicio CHECK (
            REGEXP_LIKE (hr_inicio, '^([01][0-9]|2[0-3]):[0-5][0-9]$')
        ),
        hr_fim VARCHAR2 (5 CHAR) NOT NULL CONSTRAINT ck_regra_fim CHECK (
            REGEXP_LIKE (hr_fim, '^([01][0-9]|2[0-3]):[0-5][0-9]$')
        ),
        st_ativo NUMBER (1) DEFAULT 1 NOT NULL CONSTRAINT ck_regra_status CHECK (st_ativo IN (0, 1)),
        -- Início igual ao fim não define intervalo nenhum.
        CONSTRAINT ck_regra_intervalo CHECK (hr_inicio <> hr_fim),
        -- Impede cadastrar a mesma regra duas vezes.
        CONSTRAINT uq_regra UNIQUE (
            id_estacionamento,
            tp_regra,
            nr_dia_semana,
            hr_inicio
        )
    );

COMMENT ON TABLE tb_regra_estacionamento IS 'Restrições e horários por local, uma linha por dia da semana';

COMMENT ON COLUMN tb_regra_estacionamento.nr_dia_semana IS '1 = segunda, 2 = terça, ..., 6 = sábado, 7 = domingo (ISO 8601)';

COMMENT ON COLUMN tb_regra_estacionamento.hr_inicio IS 'Formato HH:MM (00:00 a 23:59)';

COMMENT ON COLUMN tb_regra_estacionamento.hr_fim IS 'Formato HH:MM. Se hr_fim < hr_inicio, a regra atravessa a meia-noite e termina no dia seguinte. Dia inteiro = 00:00 a 23:59';

CREATE SEQUENCE sq_tb_regra_estacionamento START
WITH
    1 INCREMENT BY 1 NOCYCLE;

-- ---------------------------------------------------------------------
-- 6. TB_USUARIO_FAVORITO (associativa N:N)
-- ---------------------------------------------------------------------
CREATE TABLE
    tb_usuario_favorito (
        id_usuario_favorito NUMBER (10) CONSTRAINT pk_usuario_favorito PRIMARY KEY,
        id_usuario NUMBER (10) NOT NULL CONSTRAINT fk_favorito_usuario REFERENCES tb_usuario (id_usuario) ON DELETE CASCADE,
        id_estacionamento NUMBER (10) NOT NULL CONSTRAINT fk_favorito_estac REFERENCES tb_estacionamento (id_estacionamento) ON DELETE CASCADE,
        dt_favorito DATE DEFAULT SYSDATE NOT NULL,
        -- O mesmo usuário não favorita o mesmo local duas vezes.
        CONSTRAINT uq_favorito UNIQUE (id_usuario, id_estacionamento)
    );

COMMENT ON TABLE tb_usuario_favorito IS 'Associação entre motorista e local favorito';

CREATE SEQUENCE sq_tb_usuario_favorito START
WITH
    1 INCREMENT BY 1 NOCYCLE;