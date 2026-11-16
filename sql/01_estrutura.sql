-- Execute em um esquema Oracle vazio destinado ao Vagaí.

CREATE TABLE tb_usuario (
    id_usuario NUMBER(10) PRIMARY KEY,
    nm_usuario VARCHAR2(40 CHAR) NOT NULL UNIQUE,
    ds_senha VARCHAR2(255 CHAR) NOT NULL,
    dt_cadastro DATE NOT NULL
);
COMMENT ON TABLE tb_usuario IS 'Contas de motoristas';
CREATE SEQUENCE sq_tb_usuario START WITH 1 INCREMENT BY 1 NOCYCLE;

CREATE TABLE tb_estacionamento (
    id_estacionamento NUMBER(10) PRIMARY KEY,
    id_usuario_criador NUMBER(10) REFERENCES tb_usuario(id_usuario),
    nm_estacionamento VARCHAR2(100 CHAR) NOT NULL,
    ds_estacionamento VARCHAR2(255 CHAR),
    tp_estacionamento VARCHAR2(20 CHAR) NOT NULL CHECK (tp_estacionamento IN ('GRATUITO','ROTATIVO','PRIVADO')),
    nr_latitude NUMBER(10,7) NOT NULL CHECK (nr_latitude BETWEEN -90 AND 90),
    nr_longitude NUMBER(10,7) NOT NULL CHECK (nr_longitude BETWEEN -180 AND 180),
    ds_endereco VARCHAR2(255 CHAR),
    vl_preco NUMBER(10,2) CHECK (vl_preco >= 0),
    ds_horario VARCHAR2(255 CHAR),
    ds_fonte VARCHAR2(50 CHAR),
    ds_id_osm VARCHAR2(50 CHAR) UNIQUE,
    dt_verificacao DATE,
    st_estacionamento NUMBER(1) NOT NULL CHECK (st_estacionamento IN (0,1))
);
COMMENT ON TABLE tb_estacionamento IS 'Locais colaborativos ou importados para o mapa';
CREATE SEQUENCE sq_tb_estacionamento START WITH 1 INCREMENT BY 1 NOCYCLE;

CREATE TABLE tb_reporte (
    id_reporte NUMBER(10) PRIMARY KEY,
    id_usuario NUMBER(10) NOT NULL REFERENCES tb_usuario(id_usuario),
    id_estacionamento NUMBER(10) NOT NULL REFERENCES tb_estacionamento(id_estacionamento),
    tp_reporte VARCHAR2(20 CHAR) NOT NULL CHECK (tp_reporte IN ('VAGA_ENCONTRADA','SEM_VAGA')),
    ds_observacao VARCHAR2(255 CHAR),
    dt_reporte DATE NOT NULL
);
COMMENT ON TABLE tb_reporte IS 'Histórico de relatos de disponibilidade';
CREATE SEQUENCE sq_tb_reporte START WITH 1 INCREMENT BY 1 NOCYCLE;

CREATE TABLE tb_avaliacao (
    id_avaliacao NUMBER(10) PRIMARY KEY,
    id_usuario NUMBER(10) NOT NULL REFERENCES tb_usuario(id_usuario),
    id_estacionamento NUMBER(10) NOT NULL REFERENCES tb_estacionamento(id_estacionamento),
    nr_nota NUMBER(1) NOT NULL CHECK (nr_nota BETWEEN 1 AND 5),
    ds_comentario VARCHAR2(500 CHAR),
    dt_avaliacao DATE NOT NULL
);
COMMENT ON TABLE tb_avaliacao IS 'Notas e comentários sobre locais';
CREATE SEQUENCE sq_tb_avaliacao START WITH 1 INCREMENT BY 1 NOCYCLE;

CREATE TABLE tb_regra_estacionamento (
    id_regra NUMBER(10) PRIMARY KEY,
    id_estacionamento NUMBER(10) NOT NULL REFERENCES tb_estacionamento(id_estacionamento),
    ds_regra VARCHAR2(255 CHAR) NOT NULL,
    ds_dia_semana VARCHAR2(50 CHAR),
    hr_inicio VARCHAR2(5 CHAR),
    hr_fim VARCHAR2(5 CHAR),
    st_ativo NUMBER(1) NOT NULL CHECK (st_ativo IN (0,1))
);
COMMENT ON TABLE tb_regra_estacionamento IS 'Restrições e horários por local';
CREATE SEQUENCE sq_tb_regra_estacionamento START WITH 1 INCREMENT BY 1 NOCYCLE;

CREATE TABLE tb_usuario_favorito (
    id_usuario_favorito NUMBER(10) PRIMARY KEY,
    id_usuario NUMBER(10) NOT NULL REFERENCES tb_usuario(id_usuario),
    id_estacionamento NUMBER(10) NOT NULL REFERENCES tb_estacionamento(id_estacionamento),
    dt_favorito DATE NOT NULL,
    CONSTRAINT uq_favorito UNIQUE (id_usuario, id_estacionamento)
);
COMMENT ON TABLE tb_usuario_favorito IS 'Associação entre motorista e local favorito';
CREATE SEQUENCE sq_tb_usuario_favorito START WITH 1 INCREMENT BY 1 NOCYCLE;
