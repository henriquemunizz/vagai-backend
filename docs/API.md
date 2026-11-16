# Contrato HTTP do Vagaí

Base local `http://localhost:8080`. Corpos e respostas usam JSON UTF-8. As datas representam UTC em ISO 8601 sem sufixo. Acrescente Z ao interpretá-las no navegador. Identificadores dos exemplos são fictícios.

## Autenticação

POST /auth/cadastro ou POST /usuarios recebe nome e senha e devolve o perfil com 201 e Location. POST /auth/login recebe os mesmos campos, cria a sessão e devolve o perfil com 200. GET /auth/me devolve o perfil. POST /auth/logout recebe corpo vazio e devolve 204. Use o cookie JSESSIONID nas operações seguintes.

## Recursos

| Recurso | Coleção | Item | Acesso |
|---|---|---|---|
| Usuario | GET, POST `/usuarios` | GET, PUT, DELETE `/usuarios/{id}` | Cadastro público. Listagem e item restritos à própria conta. |
| Estacionamento | GET, POST `/estacionamentos` | GET, PUT, DELETE `/estacionamentos/{id}` | Leitura pública. Escrita pelo criador de um local colaborativo sem verificação. |
| Reporte | GET, POST `/reportes` | GET, PUT, DELETE `/reportes/{id}` | Leitura pública. Escrita pelo autor. |
| Avaliacao | GET, POST `/avaliacoes` | GET, PUT, DELETE `/avaliacoes/{id}` | Leitura pública. Escrita pelo autor. |
| Regra | GET, POST `/regras` | GET, PUT, DELETE `/regras/{id}` | Leitura pública. Escrita pelo criador do local colaborativo sem verificação. |
| Favorito | GET, POST `/favoritos` | GET, PUT, DELETE `/favoritos/{id}` | Todas as operações são privadas e restritas ao dono. |

POST cria (201, Location relativa). GET e PUT devolvem 200, DELETE devolve 204 sem corpo. PUT recebe o mesmo formato completo do POST. Os campos opcionais omitidos voltam a null. Em reportes, avaliações e regras, idEstacionamento é imutável. Em favoritos, o PUT pode trocar o local. GET de uma coleção devolve uma lista, inclusive quando vazia.

## Corpos por recurso

Envie apenas os campos de entrada. O servidor define proprietário, origem, datas e identificadores. O preço é opcional e deve acompanhar uma unidade tarifária explícita na descrição quando conhecida. Um valor nulo indica preço desconhecido.

### Usuario

Entrada para `POST /usuarios` e `PUT /usuarios/{id}`.

```json
{
  "nome": "motorista_demo",
  "senha": "{{senha}}"
}
```

Resposta de item.

```json
{
  "id": 1,
  "nome": "motorista_demo",
  "cadastro": "2026-11-16T14:30:00"
}
```

### Estacionamento

Entrada para `POST /estacionamentos` e `PUT /estacionamentos/{id}`.

```json
{
  "nome": "Trecho demonstrativo",
  "descricao": "Dados de teste da apresentação",
  "tipo": "ROTATIVO",
  "latitude": -23.5567,
  "longitude": -46.6532,
  "endereco": "Trecho fictício",
  "preco": null,
  "horario": null,
  "ativo": 1
}
```

Resposta de item.

```json
{
  "id": 17,
  "nome": "Trecho demonstrativo",
  "descricao": "Dados de teste da apresentação",
  "tipo": "ROTATIVO",
  "latitude": -23.5567,
  "longitude": -46.6532,
  "endereco": "Trecho fictício",
  "preco": null,
  "horario": null,
  "ativo": 1,
  "idUsuarioCriador": 1,
  "fonte": "COLABORATIVA",
  "idOsm": null,
  "urlOsm": null,
  "verificacao": null
}
```

### Reporte

Entrada para `POST /reportes` e `PUT /reportes/{id}`.

```json
{
  "idEstacionamento": 17,
  "tipo": "SEM_VAGA",
  "observacao": "Passei pelo trecho agora."
}
```

Resposta de item.

```json
{
  "id": 1,
  "idEstacionamento": 17,
  "tipo": "SEM_VAGA",
  "observacao": "Passei pelo trecho agora.",
  "idUsuario": 1,
  "data": "2026-11-16T14:30:00",
  "atual": true
}
```

### Avaliacao

Entrada para `POST /avaliacoes` e `PUT /avaliacoes/{id}`.

```json
{
  "idEstacionamento": 17,
  "nota": 4,
  "comentario": "Sinalização legível."
}
```

Resposta de item.

```json
{
  "id": 1,
  "idEstacionamento": 17,
  "nota": 4,
  "comentario": "Sinalização legível.",
  "idUsuario": 1,
  "data": "2026-11-16T14:30:00"
}
```

### Regra

Entrada para `POST /regras` e `PUT /regras/{id}`.

```json
{
  "idEstacionamento": 17,
  "descricao": "Limite de duas horas (exemplo)",
  "diaSemana": "Segunda a sexta",
  "inicio": "08:00",
  "fim": "18:00",
  "ativo": 1
}
```

Resposta de item.

```json
{
  "id": 1,
  "idEstacionamento": 17,
  "descricao": "Limite de duas horas (exemplo)",
  "diaSemana": "Segunda a sexta",
  "inicio": "08:00",
  "fim": "18:00",
  "ativo": 1
}
```

### Favorito

Entrada para `POST /favoritos` e `PUT /favoritos/{id}`.

```json
{
  "idEstacionamento": 17
}
```

Resposta de item.

```json
{
  "id": 1,
  "idEstacionamento": 17,
  "idUsuario": 1,
  "data": "2026-11-16T14:30:00"
}
```

## Relacionamentos

| Método | Caminho | Corpo ou resposta |
|---|---|---|
| GET | `/estacionamentos/{id}/reportes` | Lista no formato de saída do recurso. |
| GET | `/estacionamentos/{id}/avaliacoes` | Lista no formato de saída do recurso. |
| GET | `/estacionamentos/{id}/regras` | Lista no formato de saída do recurso. |
| POST | `/estacionamentos/{id}/reportes` | Mesmo corpo do POST principal, omitindo idEstacionamento. A URL identifica o local. |
| POST | `/estacionamentos/{id}/avaliacoes` | Mesmo corpo do POST principal, omitindo idEstacionamento. A URL identifica o local. |
| GET | `/usuarios/me/favoritos` | Lista de favoritos da sessão. Use idEstacionamento em GET /estacionamentos/{id} para os detalhes. |

## Busca no mapa

`GET /estacionamentos/proximos?latitude=-23.5567&longitude=-46.6532&raioKm=2&tipo=ROTATIVO`

latitude, longitude e raioKm são obrigatórios. O tipo é opcional. Cada item contém todos os campos da saída de estacionamento e os quatro campos adicionais abaixo. A lista contém apenas locais ativos em ordem crescente de distância em linha reta.

```json
{
  "distanciaKm": 0.134,
  "mediaAvaliacoes": 4,
  "regras": [],
  "ultimoReporteRecente": null
}
```

A média é null quando faltam notas. regras contém somente regras ativas. ultimoReporteRecente contém a saída completa do reporte mais recente dos últimos dez minutos, ou null. O histórico permanece disponível na rota aninhada com atual calculado no momento da consulta. Ao manter a tela aberta, o frontend deve descartar o indicador ao atingir data + 10 minutos.

## Validações e erros

Nome do usuário até 40 caracteres, único e sensível a maiúsculas. Senha de 8 a 255 caracteres. Latitude entre -90 e 90, longitude entre -180 e 180, até sete casas decimais. Raio positivo e finito. Tipos de local GRATUITO, ROTATIVO ou PRIVADO. Tipos de reporte VAGA_ENCONTRADA ou SEM_VAGA. Nota inteira de 1 a 5. Estados inteiros 0 ou 1. Horas em HH:mm.

```json
{
  "status": 409,
  "mensagem": "Local inativo."
}
```

| Status | Situação |
|---|---|
| 400 | Campo obrigatório, tipo, nota, coordenada ou JSON inválido. |
| 401 | Login inválido ou sessão ausente/expirada. |
| 403 | Recurso de outro usuário ou local importado/verificado. |
| 404 | ID, local aninhado ou rota inexistente. |
| 405 | Método incompatível com a rota. |
| 409 | Nome/favorito duplicado, vínculos impedindo exclusão, reporte privado/inativo ou tentativa de mudar vínculo permanente. |
| 500 | Falha interna, com detalhes técnicos apenas no servidor. |

Nos locais importados, urlOsm aponta para o elemento original no OpenStreetMap. O campo também está disponível em cada resultado da busca por proximidade. Nos cadastros manuais ele vale null.
