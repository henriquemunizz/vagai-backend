-- DADOS DE TESTE FICTÍCIOS. Execute uma vez em esquema de demonstração vazio.
-- Os hashes pertencem a valores aleatórios descartados. Cadastre sua conta pela API.
-- Contas de motoristas
INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_1',
        'wn1jVxrVzXXHeJbeLpZTmg==:8pa2tOWU3nBtmEt9uTeGlz+4bsraHuYqgOBp7ykbR3s=',
        TIMESTAMP '2026-01-01 15:00:00'
    );

INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_2',
        '/jx/vv5KmR6NWUYxwmyprQ==:MyiYUyRxT7d4eizgMNt9PXqOjgKsSUlJ+RzZFmyjJZA=',
        TIMESTAMP '2026-01-02 15:00:00'
    );

INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_3',
        'A1C9H4s0a5Fr08UKyEpLuA==:kclf0v0oQWjern5wjGsP1cFmLtz6iWx63pD7Zu0cLb0=',
        TIMESTAMP '2026-01-03 15:00:00'
    );

INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_4',
        'Q/LyYb8GiV/SXRF4QJ82Bg==:zSmuOt7gmJmaiw9slOPlHzwpxWn8pendKRhb0UcEbcs=',
        TIMESTAMP '2026-01-04 15:00:00'
    );

INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_5',
        'g094m2XTaW6pxL3bP2JWsw==:sInc8qLqRyGzsK6iVoAf5kSV8lgZsu7mVro0yLF+kpU=',
        TIMESTAMP '2026-01-05 15:00:00'
    );

INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_6',
        'zzsGEcDR0mNDcUJp3PqhSQ==:K1ddMG/WCIWb4ciSBURUJ2c0eeFONCTgUBDOaB5WSlk=',
        TIMESTAMP '2026-01-06 15:00:00'
    );

INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_7',
        'mDbfSwlR39WXY3GNaDL2+A==:6jx6oyVBVadRFQd/fVqFmplxhxhNdst5hy3LdvbdC7g=',
        TIMESTAMP '2026-01-07 15:00:00'
    );

INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_8',
        'jE2vJpQUpPKdACEvnIbW4A==:jSl4Dlo35FAHb6L7ricSAhkn0xbUjsRrTUt0sCPt9OY=',
        TIMESTAMP '2026-01-08 15:00:00'
    );

INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_9',
        'NdPT/AuTl+haJj8uMVaCGQ==:UwKtld2OZrZw/80oFJHF/N4tm1jDFNaL0BYFELoY22c=',
        TIMESTAMP '2026-01-09 15:00:00'
    );

INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_10',
        'xgQW4V7wY8R4tzjCd+Qogw==:doM2wFlHRVw/v3zmtuxnDpKvr/e3vlp/xSb/OZ/JBns=',
        TIMESTAMP '2026-01-10 15:00:00'
    );

INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_11',
        'uE8Oq/BT2nA2NG6qUsItYA==:bdvImdTq56hMLHykOR7dfjWtoH5A2E/26ZtBjmK8r9g=',
        TIMESTAMP '2026-01-11 15:00:00'
    );

INSERT INTO
    tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro)
VALUES
    (
        sq_tb_usuario.NEXTVAL,
        'teste_vagai_12',
        'ADzz9VVS9RI1B5feklj+RQ==:D0wuPO6dvyf6H7OM8CkMVi8wdYwnMGs8o7qYwjNiuwc=',
        TIMESTAMP '2026-01-12 15:00:00'
    );

-- Locais colaborativos ou importados para o mapa
INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_1'
        ),
        'TESTE - Local 1',
        'Exemplo fictício para aula',
        'GRATUITO',
        -23.55050,
        -46.64100,
        'Endereço fictício 1',
        0,
        'TESTE',
        NULL,
        NULL,
        1
    );

INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_2'
        ),
        'TESTE - Local 2',
        'Exemplo fictício para aula',
        'ROTATIVO',
        -23.55100,
        -46.64200,
        'Endereço fictício 2',
        4,
        'TESTE',
        NULL,
        NULL,
        1
    );

INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_3'
        ),
        'TESTE - Local 3',
        'Exemplo fictício para aula',
        'PRIVADO',
        -23.55150,
        -46.64300,
        'Endereço fictício 3',
        NULL,
        'TESTE',
        NULL,
        NULL,
        1
    );

INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_4'
        ),
        'TESTE - Local 4',
        'Exemplo fictício para aula',
        'GRATUITO',
        -23.55200,
        -46.64400,
        'Endereço fictício 4',
        0,
        'TESTE',
        NULL,
        NULL,
        1
    );

INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_5'
        ),
        'TESTE - Local 5',
        'Exemplo fictício para aula',
        'ROTATIVO',
        -23.55250,
        -46.64500,
        'Endereço fictício 5',
        4,
        'TESTE',
        NULL,
        NULL,
        1
    );

INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_6'
        ),
        'TESTE - Local 6',
        'Exemplo fictício para aula',
        'PRIVADO',
        -23.55300,
        -46.64600,
        'Endereço fictício 6',
        NULL,
        'TESTE',
        NULL,
        NULL,
        1
    );

INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_7'
        ),
        'TESTE - Local 7',
        'Exemplo fictício para aula',
        'GRATUITO',
        -23.55350,
        -46.64700,
        'Endereço fictício 7',
        0,
        'TESTE',
        NULL,
        NULL,
        1
    );

INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_8'
        ),
        'TESTE - Local 8',
        'Exemplo fictício para aula',
        'ROTATIVO',
        -23.55400,
        -46.64800,
        'Endereço fictício 8',
        4,
        'TESTE',
        NULL,
        NULL,
        1
    );

INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_9'
        ),
        'TESTE - Local 9',
        'Exemplo fictício para aula',
        'PRIVADO',
        -23.55450,
        -46.64900,
        'Endereço fictício 9',
        NULL,
        'TESTE',
        NULL,
        NULL,
        1
    );

INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_10'
        ),
        'TESTE - Local 10',
        'Exemplo fictício para aula',
        'GRATUITO',
        -23.55500,
        -46.65000,
        'Endereço fictício 10',
        0,
        'TESTE',
        NULL,
        NULL,
        1
    );

INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_11'
        ),
        'TESTE - Local 11',
        'Exemplo fictício para aula',
        'ROTATIVO',
        -23.55550,
        -46.65100,
        'Endereço fictício 11',
        4,
        'TESTE',
        NULL,
        NULL,
        1
    );

INSERT INTO
    tb_estacionamento (
        id_estacionamento,
        id_usuario_criador,
        nm_estacionamento,
        ds_estacionamento,
        tp_estacionamento,
        nr_latitude,
        nr_longitude,
        ds_endereco,
        vl_preco,
        ds_fonte,
        ds_id_osm,
        dt_verificacao,
        st_estacionamento
    )
VALUES
    (
        sq_tb_estacionamento.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_12'
        ),
        'TESTE - Local 12',
        'Exemplo fictício para aula',
        'PRIVADO',
        -23.55600,
        -46.65200,
        'Endereço fictício 12',
        NULL,
        'TESTE',
        NULL,
        NULL,
        1
    );

-- Histórico de relatos de disponibilidade
INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_1'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 1'
                AND ds_fonte = 'TESTE'
        ),
        'VAGA_ENCONTRADA',
        'Relato fictício',
        TIMESTAMP '2026-01-01 15:00:00'
    );

INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_2'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 2'
                AND ds_fonte = 'TESTE'
        ),
        'SEM_VAGA',
        'Relato fictício',
        TIMESTAMP '2026-01-02 15:00:00'
    );

INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_3'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 2'
                AND ds_fonte = 'TESTE'
        ),
        'VAGA_ENCONTRADA',
        'Relato fictício',
        TIMESTAMP '2026-01-03 15:00:00'
    );

INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_4'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 4'
                AND ds_fonte = 'TESTE'
        ),
        'SEM_VAGA',
        'Relato fictício',
        TIMESTAMP '2026-01-04 15:00:00'
    );

INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_5'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 5'
                AND ds_fonte = 'TESTE'
        ),
        'VAGA_ENCONTRADA',
        'Relato fictício',
        TIMESTAMP '2026-01-05 15:00:00'
    );

INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_6'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 5'
                AND ds_fonte = 'TESTE'
        ),
        'SEM_VAGA',
        'Relato fictício',
        TIMESTAMP '2026-01-06 15:00:00'
    );

INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_7'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 7'
                AND ds_fonte = 'TESTE'
        ),
        'VAGA_ENCONTRADA',
        'Relato fictício',
        TIMESTAMP '2026-01-07 15:00:00'
    );

INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_8'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 8'
                AND ds_fonte = 'TESTE'
        ),
        'SEM_VAGA',
        'Relato fictício',
        TIMESTAMP '2026-01-08 15:00:00'
    );

INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_9'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 8'
                AND ds_fonte = 'TESTE'
        ),
        'VAGA_ENCONTRADA',
        'Relato fictício',
        TIMESTAMP '2026-01-09 15:00:00'
    );

INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_10'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 10'
                AND ds_fonte = 'TESTE'
        ),
        'SEM_VAGA',
        'Relato fictício',
        TIMESTAMP '2026-01-10 15:00:00'
    );

INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_11'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 11'
                AND ds_fonte = 'TESTE'
        ),
        'VAGA_ENCONTRADA',
        'Relato fictício',
        TIMESTAMP '2026-01-11 15:00:00'
    );

INSERT INTO
    tb_reporte (
        id_reporte,
        id_usuario,
        id_estacionamento,
        tp_reporte,
        ds_observacao,
        dt_reporte
    )
VALUES
    (
        sq_tb_reporte.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_12'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 11'
                AND ds_fonte = 'TESTE'
        ),
        'SEM_VAGA',
        'Relato fictício',
        TIMESTAMP '2026-01-12 15:00:00'
    );

-- Notas e comentários sobre locais
INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_1'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 1'
                AND ds_fonte = 'TESTE'
        ),
        2,
        'Comentário fictício',
        TIMESTAMP '2026-01-01 15:00:00'
    );

INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_2'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 2'
                AND ds_fonte = 'TESTE'
        ),
        3,
        'Comentário fictício',
        TIMESTAMP '2026-01-02 15:00:00'
    );

INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_3'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 3'
                AND ds_fonte = 'TESTE'
        ),
        4,
        'Comentário fictício',
        TIMESTAMP '2026-01-03 15:00:00'
    );

INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_4'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 4'
                AND ds_fonte = 'TESTE'
        ),
        5,
        'Comentário fictício',
        TIMESTAMP '2026-01-04 15:00:00'
    );

INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_5'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 5'
                AND ds_fonte = 'TESTE'
        ),
        1,
        'Comentário fictício',
        TIMESTAMP '2026-01-05 15:00:00'
    );

INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_6'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 6'
                AND ds_fonte = 'TESTE'
        ),
        2,
        'Comentário fictício',
        TIMESTAMP '2026-01-06 15:00:00'
    );

INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_7'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 7'
                AND ds_fonte = 'TESTE'
        ),
        3,
        'Comentário fictício',
        TIMESTAMP '2026-01-07 15:00:00'
    );

INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_8'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 8'
                AND ds_fonte = 'TESTE'
        ),
        4,
        'Comentário fictício',
        TIMESTAMP '2026-01-08 15:00:00'
    );

INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_9'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 9'
                AND ds_fonte = 'TESTE'
        ),
        5,
        'Comentário fictício',
        TIMESTAMP '2026-01-09 15:00:00'
    );

INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_10'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 10'
                AND ds_fonte = 'TESTE'
        ),
        1,
        'Comentário fictício',
        TIMESTAMP '2026-01-10 15:00:00'
    );

INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_11'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 11'
                AND ds_fonte = 'TESTE'
        ),
        2,
        'Comentário fictício',
        TIMESTAMP '2026-01-11 15:00:00'
    );

INSERT INTO
    tb_avaliacao (
        id_avaliacao,
        id_usuario,
        id_estacionamento,
        nr_nota,
        ds_comentario,
        dt_avaliacao
    )
VALUES
    (
        sq_tb_avaliacao.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_12'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 12'
                AND ds_fonte = 'TESTE'
        ),
        3,
        'Comentário fictício',
        TIMESTAMP '2026-01-12 15:00:00'
    );

-- Restrições e horários por local
-- Cada regra de "segunda a sexta" vira 5 linhas (nr_dia_semana 1 a 5).
-- A subconsulta "dias" gera os números 1, 2, 3, 4, 5:
--   SELECT LEVEL FROM dual CONNECT BY LEVEL <= 5
-- e o CROSS JOIN combina cada local de teste com cada um desses dias.
INSERT INTO
    tb_regra_estacionamento (
        id_regra,
        id_estacionamento,
        tp_regra,
        ds_regra,
        nr_dia_semana,
        hr_inicio,
        hr_fim,
        st_ativo
    )
SELECT
    sq_tb_regra_estacionamento.NEXTVAL,
    e.id_estacionamento,
    'PERMITIDO',
    'Conferir sinalização de teste',
    dias.nr_dia,
    '08:00',
    '18:00',
    1
FROM
    tb_estacionamento e
    CROSS JOIN (
        SELECT
            LEVEL AS nr_dia
        FROM
            dual CONNECT BY LEVEL <= 5
    ) dias
WHERE
    e.ds_fonte = 'TESTE';

-- Exemplo de regra que atravessa a meia-noite: permitido de segunda a sexta, das 20h às 6h.
-- hr_fim ('06:00') menor que hr_inicio ('20:00') = termina no dia seguinte.
INSERT INTO
    tb_regra_estacionamento (
        id_regra,
        id_estacionamento,
        tp_regra,
        ds_regra,
        nr_dia_semana,
        hr_inicio,
        hr_fim,
        st_ativo
    )
SELECT
    sq_tb_regra_estacionamento.NEXTVAL,
    e.id_estacionamento,
    'PERMITIDO',
    'Permitido no período noturno',
    dias.nr_dia,
    '20:00',
    '06:00',
    1
FROM
    tb_estacionamento e
    CROSS JOIN (
        SELECT
            LEVEL AS nr_dia
        FROM
            dual CONNECT BY LEVEL <= 5
    ) dias
WHERE
    e.nm_estacionamento = 'TESTE - Local 1'
    AND e.ds_fonte = 'TESTE';

-- Associação entre motorista e local favorito
INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_1'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 1'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-01 15:00:00'
    );

INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_2'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 2'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-02 15:00:00'
    );

INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_3'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 3'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-03 15:00:00'
    );

INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_4'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 4'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-04 15:00:00'
    );

INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_5'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 5'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-05 15:00:00'
    );

INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_6'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 6'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-06 15:00:00'
    );

INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_7'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 7'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-07 15:00:00'
    );

INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_8'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 8'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-08 15:00:00'
    );

INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_9'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 9'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-09 15:00:00'
    );

INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_10'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 10'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-10 15:00:00'
    );

INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_11'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 11'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-11 15:00:00'
    );

INSERT INTO
    tb_usuario_favorito (
        id_usuario_favorito,
        id_usuario,
        id_estacionamento,
        dt_favorito
    )
VALUES
    (
        sq_tb_usuario_favorito.NEXTVAL,
        (
            SELECT
                id_usuario
            FROM
                tb_usuario
            WHERE
                nm_usuario = 'teste_vagai_12'
        ),
        (
            SELECT
                id_estacionamento
            FROM
                tb_estacionamento
            WHERE
                nm_estacionamento = 'TESTE - Local 12'
                AND ds_fonte = 'TESTE'
        ),
        TIMESTAMP '2026-01-12 15:00:00'
    );

-- Demonstração de UPDATE e DELETE
UPDATE tb_regra_estacionamento
SET
    ds_regra = 'Regra fictícia revisada'
WHERE
    id_estacionamento = (
        SELECT
            id_estacionamento
        FROM
            tb_estacionamento
        WHERE
            nm_estacionamento = 'TESTE - Local 1'
            AND ds_fonte = 'TESTE'
    )
    AND hr_inicio = '08:00';

DELETE FROM tb_usuario_favorito
WHERE
    id_usuario = (
        SELECT
            id_usuario
        FROM
            tb_usuario
        WHERE
            nm_usuario = 'teste_vagai_12'
    )
    AND id_estacionamento = (
        SELECT
            id_estacionamento
        FROM
            tb_estacionamento
        WHERE
            nm_estacionamento = 'TESTE - Local 12'
            AND ds_fonte = 'TESTE'
    );

COMMIT;