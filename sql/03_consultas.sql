-- 01. Locais ativos rotativos ordenados pelo nome.
SELECT
  nm_estacionamento,
  ds_endereco
FROM
  tb_estacionamento
WHERE
  st_estacionamento = 1
  AND tp_estacionamento = 'ROTATIVO'
ORDER BY
  nm_estacionamento;

-- 02. Simulação de duas unidades da tarifa informada.
SELECT
  nm_estacionamento,
  vl_preco,
  vl_preco * 2 AS duas_unidades
FROM
  tb_estacionamento
WHERE
  vl_preco IS NOT NULL
ORDER BY
  duas_unidades;

-- 03. Soma das tarifas cadastradas e quantidade de preços conhecidos por tipo.
-- COUNT(vl_preco) conta só as linhas com preço preenchido (ignora NULL).
SELECT
  tp_estacionamento,
  COUNT(vl_preco) AS precos_conhecidos,
  SUM(vl_preco) AS soma_tarifas
FROM
  tb_estacionamento
GROUP BY
  tp_estacionamento
ORDER BY
  tp_estacionamento;

-- 04. Faixa de preços conhecidos por categoria.
SELECT
  tp_estacionamento,
  MIN(vl_preco) AS menor,
  MAX(vl_preco) AS maior
FROM
  tb_estacionamento
WHERE
  vl_preco IS NOT NULL
GROUP BY
  tp_estacionamento
ORDER BY
  tp_estacionamento;

-- 05. Locais com média de avaliação a partir de quatro.
SELECT
  e.id_estacionamento,
  e.nm_estacionamento,
  ROUND(AVG(a.nr_nota), 2) AS media
FROM
  tb_estacionamento e
  INNER JOIN tb_avaliacao a ON a.id_estacionamento = e.id_estacionamento
GROUP BY
  e.id_estacionamento,
  e.nm_estacionamento
HAVING
  AVG(a.nr_nota) >= 4
ORDER BY
  media DESC;

-- 06. Histórico de reportes com autor e local.
SELECT
  u.nm_usuario,
  e.nm_estacionamento,
  r.tp_reporte,
  r.dt_reporte
FROM
  tb_reporte r
  INNER JOIN tb_usuario u ON u.id_usuario = r.id_usuario
  INNER JOIN tb_estacionamento e ON e.id_estacionamento = r.id_estacionamento
ORDER BY
  r.dt_reporte DESC;

-- 07. Todos os usuários, inclusive os que não têm favoritos.
SELECT
  u.id_usuario,
  u.nm_usuario,
  COUNT(f.id_usuario_favorito) AS favoritos
FROM
  tb_usuario u
  LEFT JOIN tb_usuario_favorito f ON f.id_usuario = u.id_usuario
GROUP BY
  u.id_usuario,
  u.nm_usuario
ORDER BY
  u.id_usuario;

-- 08. Todos os locais, inclusive os que não têm regras ativas.
-- O filtro st_ativo = 1 fica no ON (e não no WHERE) para não eliminar
-- os locais sem regra: no WHERE, a linha com regra NULL seria descartada.
SELECT
  e.nm_estacionamento,
  r.tp_regra,
  r.nr_dia_semana,
  r.hr_inicio,
  r.hr_fim,
  r.ds_regra
FROM
  tb_regra_estacionamento r
  RIGHT JOIN tb_estacionamento e ON e.id_estacionamento = r.id_estacionamento
  AND r.st_ativo = 1
ORDER BY
  e.nm_estacionamento,
  r.nr_dia_semana,
  r.hr_inicio;

-- 09. Locais com tarifa acima da média conhecida.
-- AVG ignora NULL, então a média considera só os preços conhecidos.
SELECT
  nm_estacionamento,
  vl_preco
FROM
  tb_estacionamento
WHERE
  vl_preco > (
    SELECT
      AVG(vl_preco)
    FROM
      tb_estacionamento
  )
ORDER BY
  vl_preco DESC;

-- 10. Motoristas e os locais que salvaram pela relação N:N.
SELECT
  u.nm_usuario,
  e.nm_estacionamento,
  f.dt_favorito
FROM
  tb_usuario_favorito f
  INNER JOIN tb_usuario u ON u.id_usuario = f.id_usuario
  INNER JOIN tb_estacionamento e ON e.id_estacionamento = f.id_estacionamento
ORDER BY
  u.nm_usuario,
  e.nm_estacionamento;