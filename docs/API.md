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

Envie apenas os campos de entrada. O servidor define proprietário, origem, datas e identificadores. As tarifas são em reais. No cadastro manual de PRIVADO, informe primeiraHora e também demaisHoras. GRATUITO aceita somente zero ou null nas tarifas.

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
  "nome": "Estacionamento de teste",
  "descricao": "Exemplo fictício",
  "tipo": "PRIVADO",
  "rua": "Rua das Flores",
  "bairro": "Centro",
  "numero": "120A",
  "primeiraHora": 18,
  "demaisHoras": 8,
  "ativo": 1
}
```

Resposta de item.

```json
{
  "id": 17,
  "idUsuarioCriador": 1,
  "fonte": "USUARIO",
  "idOsm": null,
  "verificacao": null,
  "nome": "Estacionamento de teste",
  "descricao": "Exemplo fictício",
  "tipo": "PRIVADO",
  "rua": "Rua das Flores",
  "bairro": "Centro",
  "numero": "120A",
  "primeiraHora": 18,
  "demaisHoras": 8,
  "ativo": 1
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
  "descricao": "Permitido durante o atendimento",
  "diaSemana": 1,
  "inicio": 480,
  "fim": 1080,
  "ativo": 1,
  "tipo": "PERMITIDO"
}
```

Resposta de item.

```json
{
  "id": 1,
  "idEstacionamento": 17,
  "descricao": "Permitido durante o atendimento",
  "diaSemana": 1,
  "inicio": 480,
  "fim": 1080,
  "ativo": 1,
  "tipo": "PERMITIDO"
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

## Lista por rua

GET /estacionamentos?rua=Rua%20das%20Flores

GET /estacionamentos?rua=Flores&bairro=Centro

A resposta é uma lista de objetos no formato de EstacionamentoResponseDto. Cada item tem nome, rua, bairro, numero, tipo, as duas tarifas, autoria, origem e estado. Os parâmetros rua e bairro são opcionais. A pesquisa encontra trechos do nome, ignora maiúsculas e minúsculas e considera os acentos. Sem filtros, lista todos os locais ativos. A ordem é rua, bairro, número como texto e ID. A pesquisa inclui os três tipos. Resultado sem correspondência devolve 200 com [].

numero aceita valores como "120", "120A" ou "s/n". Para um trecho gratuito ou rotativo, informe o número de referência da rua. O cadastro identifica um local ou trecho. Campos de endereço são obrigatórios no POST e no PUT.

primeiraHora é o valor total da primeira hora. demaisHoras é o valor de cada hora adicional. null indica tarifa desconhecida na origem do registro. Uma importação OSM pode ter tarifas desconhecidas. A API expõe as tarifas cadastradas. Regras de arredondamento ou cobrança de frações precisam constar na descrição.

Consulte avaliações e reportes nas rotas aninhadas do local. Reportes com mais de dez minutos têm atual=false. Avaliação é única por usuário e local. Use PUT para mudar sua nota.

## Regras por dia e horário

GET /estacionamentos/17/regras?diaSemana=2&minuto=1260

Dia vai de 1 (segunda) a 7 (domingo). inicio, fim e minuto representam minutos desde 00h00, entre 0 e 1439. Por exemplo, 20h corresponde a 1200 e 6h a 360. diaSemana e minuto devem ser enviados juntos. Sem esses parâmetros, a rota devolve o histórico completo de regras do local.

Um intervalo de segunda com inicio=1200 e fim=360 se aplica de segunda às 20h até terça às 6h. O início é incluído e o fim excluído. Valores iguais representam 24 horas desde o início informado. Cadastre uma linha por dia em que o intervalo começa. Múltiplos intervalos no mesmo dia são permitidos.

A consulta com dia e minuto retorna somente regras ativas aplicáveis naquele instante, incluindo as que começaram no dia anterior. Cada regra traz PERMITIDO ou PROIBIDO. Se houver regras conflitantes, PROIBIDO deve prevalecer na apresentação. Uma lista vazia significa informação ausente. Horários são locais ao estacionamento.

## Validações e erros

Nome do usuário até 40 caracteres, único e sensível a maiúsculas. Senha de 8 a 255 caracteres. Tipos de local GRATUITO, ROTATIVO ou PRIVADO. Tipos de reporte VAGA_ENCONTRADA ou SEM_VAGA. Nota inteira de 1 a 5. Estados inteiros 0 ou 1. Horários em minutos de 0 a 1439 e dia da semana de 1 a 7.

```json
{
  "status": 409,
  "mensagem": "Local inativo."
}
```

| Status | Situação |
|---|---|
| 400 | Campo obrigatório, tipo, nota, horário ou JSON inválido. |
| 401 | Login inválido ou sessão ausente/expirada. |
| 403 | Recurso de outro usuário ou local importado/verificado. |
| 404 | ID, local aninhado ou rota inexistente. |
| 405 | Método incompatível com a rota. |
| 409 | Nome, favorito ou avaliação duplicada, vínculos impedindo exclusão, reporte privado/inativo ou tentativa de mudar vínculo permanente. |
| 500 | Falha interna, com detalhes técnicos apenas no servidor. |

