# Vagaí · Backend Java

**Global Solution FIAP 2026/2 · CitizenTech.** API para motoristas pesquisarem locais de estacionamento perto do destino, consultarem restrições e compartilharem relatos, avaliações e favoritos. Cada marcador representa um local. Um relato recente indica o que outro motorista observou naquele instante.

## Executar no IntelliJ

1. Instale o JDK 21 e abra o pom.xml como projeto Maven. Selecione JDK 21 como Project SDK. O Maven Wrapper acompanha o projeto.
2. Para testar imediatamente, rode ApiIntegracaoTest pela IDE ou execute `./mvnw verify` na raiz. No Windows, use `.\mvnw.cmd verify`. A suíte sobe Tomcat em porta aleatória e H2 descartável em modo Oracle. A primeira execução baixa dependências.
3. Para usar o Oracle da FIAP, abra uma conexão com o esquema reservado ao Vagaí no SQL Developer. Confirme o serviço, a conta, a conectividade e os privilégios CREATE TABLE e CREATE SEQUENCE. O GAIA também usa TB_USUARIO. Use um esquema próprio e vazio para evitar colisão.
4. Execute como script, na ordem, [estrutura](sql/01_estrutura.sql), [dados de teste opcionais](sql/02_dados_teste.sql) e [consultas](sql/03_consultas.sql). A carga é para uma execução em base vazia. São doze inserts por tabela, um UPDATE e um DELETE com WHERE. Restam 71 linhas. Os dados são fictícios e os hashes das contas de exemplo correspondem a valores aleatórios descartados. Cadastre uma conta para fazer login.
5. Em **Run → Edit Configurations**, selecione VagaiApplication e configure as variáveis abaixo. Inicie a classe ou use `.\mvnw.cmd spring-boot:run`. Verifique `GET http://localhost:8080/estacionamentos` no Postman. Uma base vazia devolve uma lista vazia.

| Variável | Valor ou finalidade |
|---|---|
| DB_URL | URL fornecida pela FIAP, em formato JDBC Thin, por exemplo `jdbc:oracle:thin:@//HOST:1521/SERVICO`. Para SID, use o formato orientado pela instituição. |
| DB_USER, DB_PASSWORD | Credenciais individuais do esquema, configuradas no ambiente de execução. |
| PORT | Porta HTTP, padrão 8080. No Render, utilize a porta do serviço. |
| FRONTEND_ORIGIN | Origem exata do React, sem barra final. Padrão local `http://localhost:5173`. |
| COOKIE_SECURE, COOKIE_SAME_SITE | Local HTTP: false e lax. Produção HTTPS entre sites diferentes: true e none. |
| OSM_BBOX | Sul,oeste,norte,leste em graus decimais. Padrão: -23.57,-46.66,-23.54,-46.63. |
| OSM_TIMEOUT, OSM_URL | Timeout de consulta em segundos (padrão 30) e URL do intérprete Overpass. |

## Código e banco

Java 21, Spring Boot 4.1.1, Maven, JDBC, Hikari DataSource, ojdbc17, Bean Validation e ModelMapper. A aplicação mantém a sessão no servidor por até 30 minutos de inatividade. Apenas o cookie HttpOnly identifica a sessão no cliente. O login troca a sessão e a troca de senha invalida o acesso das demais sessões na próxima consulta autenticada.

| Pacote | Responsabilidade |
|---|---|
| model | Usuario, Estacionamento, Reporte, Avaliacao, Regra e Favorito têm atributos privados, construtor vazio e getters/setters. Os vínculos usam referências a Usuario e Estacionamento. Ao ler uma FK, o DAO monta a referência pelo ID. Reporte calcula sua validade informativa. |
| dto | DTOs com propriedades acessadas pelo ModelMapper e validação na entrada. O JSON dos vínculos usa IDs. Nenhuma saída contém senha ou hash. |
| controller | Rotas, validação, autorização por sessão, conversão com ModelMapper e status HTTP. |
| dao | Um DAO por entidade. JdbcDao abre e fecha recursos JDBC e associa os parâmetros aos PreparedStatements. |
| service | BuscaGeografica calcula Haversine. Senhas deriva hashes PBKDF2-HMAC-SHA256 com salt individual e 600 mil iterações. ImportadorOsm consulta a fonte e SincronizacaoOsm persiste cada elemento. |
| config | Sessao identifica o motorista. Configuracao registra CORS e ModelMapper. |
| exception, handler | Exceções de domínio e respostas de erro JSON. |

As seis tabelas têm PK e sequence. Oito FKs materializam os vínculos: criador do local, usuário/local do reporte, usuário/local da avaliação, local da regra e usuário/local do favorito. A chave única do par no favorito resolve o N:N. A FK impede apagar um local com vínculos, inclusive regras. Remova primeiro os registros que pertencem à sua conta. Um local com contribuição de outro usuário continuará protegido.

## Endpoints e frontend

[Contrato completo, corpos JSON, saídas e erros](docs/API.md) · [Coleção de demonstração do Postman](docs/Vagai.postman_collection.json)

| Caminhos | Operações |
|---|---|
| /auth/cadastro, /auth/login, /auth/logout | POST |
| /auth/me, /usuarios/me/favoritos | GET |
| /usuarios, /estacionamentos, /reportes, /avaliacoes, /regras, /favoritos | POST e GET na coleção. GET, PUT e DELETE em /{id}. |
| /estacionamentos/{id}/reportes e /estacionamentos/{id}/avaliacoes | GET e POST |
| /estacionamentos/{id}/regras, /estacionamentos/proximos | GET |

No Postman, importe a coleção, defina nome e senha nas variáveis locais e mantenha o cookie jar habilitado. Use um nome novo a cada execução completa. O Runner captura os IDs retornados. A sequência cria vínculos, demonstra 400 e 409, remove os registros da demonstração e verifica 404. O roteiro mantém a conta para novos logins. GET /usuarios lista somente a própria conta.

No React, envie credenciais inclusive no login. A mesma origem configurada em FRONTEND_ORIGIN precisa ser usada pelo navegador. Em desenvolvimento, mantenha localhost em ambas as aplicações. Com bloqueio de cookies de terceiros, hospede frontend e API sob o mesmo site.

```typescript
await fetch(api + "/auth/login", {
  method: "POST",
  credentials: "include",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({ nome, senha })
})
const perfil = await fetch(api + "/auth/me", { credentials: "include" })
```

## OpenStreetMap e busca

A busca normal lê locais ativos do Oracle, filtra por raio em quilômetros e tipo e ordena pela distância em linha reta. Para cada resultado, reúne regras ativas, média das notas e último reporte dos dez minutos anteriores. O protótipo calcula as distâncias em memória e consulta os vínculos dos locais selecionados. Isso atende a uma base acadêmica pequena.

Para importar, configure as variáveis do Oracle e execute na raiz:

```shell
./mvnw package
java -jar target/vagai-1.0.0.jar --spring.profiles.active=importar
```

Se já tiver a resposta da Overpass em JSON, a mesma rotina pode ler o arquivo local. No [Overpass Turbo](https://overpass-turbo.eu/), execute a consulta abaixo e use **Export → Data → raw data directly from Overpass API**. Salve o resultado como `estacionamentos.json` (formato com `elements`). Na raiz do projeto, execute:

```text
[out:json][timeout:30];
nwr["amenity"="parking"](-23.57,-46.66,-23.54,-46.63);
out center;
```

```shell
java -jar target/vagai-1.0.0.jar --spring.profiles.active=importar --osm-arquivo=estacionamentos.json
```

Inicie a API no perfil normal e consulte `GET /estacionamentos` ou a busca por raio. Os locais importados trazem `fonte: "OpenStreetMap"`, o identificador original e `urlOsm` para abrir o elemento no mapa.

O perfil importar inicia em modo de linha de comando e termina após uma requisição Overpass para uma caixa de até 0,25 grau por lado. A consulta usa nwr[amenity=parking] e out center. Para estacionamento comercial, a política inicial exige operador privado e acesso público explícitos, sem tags condicionais. Uma tarifa desconhecida fica nula. Os demais casos exigem informação complementar. GRATUITO e ROTATIVO aceitam cadastro colaborativo identificado, com verificação nula. A validação oficial de trechos e tarifas fica para fontes verificadas.

A chave inclui o tipo do elemento, por exemplo way/42. A sincronização atualiza pelo mesmo ID interno e preserva contribuições. Quando um elemento retornado perde as evidências necessárias, o importador inativa o local importado correspondente. Elementos ausentes da resposta não são excluídos. access, fee, parking, capacity e charge disponíveis ficam na descrição. O preço numérico permanece desconhecido na importação. Uma falha interrompe a rotina, e os elementos já confirmados podem ser sincronizados novamente. Em 429 ou 5xx, aguarde antes de repetir. No mapa, atribua os dados a © OpenStreetMap contributors. [Overpass QL](https://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_QL) · [Atribuição do OpenStreetMap](https://www.openstreetmap.org/copyright)

## Docker e Render

Gere a imagem com `docker build -t vagai .`. Com as três variáveis DB_* já exportadas no terminal, execute `docker run --rm -p 8080:8080 -e DB_URL -e DB_USER -e DB_PASSWORD vagai`. O Dockerfile compila com Maven/JDK 21 e executa o JAR em JRE 21.

No Render, crie um Web Service para o repositório, escolha Docker e indique a pasta que contém o Dockerfile como Root Directory se necessário. Configure DB_URL, DB_USER, DB_PASSWORD, FRONTEND_ORIGIN, COOKIE_SECURE=true e COOKIE_SAME_SITE=none para um frontend HTTPS em outro site. Use uma instância com a sessão em memória. Reinícios encerram as sessões. Confirme que o Oracle aceita conexões de saída do serviço. Depois do deploy, valide GET /estacionamentos, login, JSESSIONID e GET /auth/me pelo navegador do frontend. [Docker no Render](https://render.com/docs/docker)

## Verificação dos critérios FIAP

| Critério | Evidência obtida em 16/11/2026 | Validação externa |
|---|---|---|
| Modelagem, 1:N e N:N | Seis tabelas, seis sequences, oito FKs, par único dos favoritos e DER. A suíte executou DDL, carga, UPDATE, DELETE e dez consultas no H2 em modo Oracle. | Repetir no esquema Oracle da FIAP e conferir os planos e resultados necessários à disciplina. |
| DAO JDBC e CRUD | Tomcat recebeu requisições HTTP reais para as seis entidades. Os DAOs usaram DataSource e PreparedStatement. | Repetir o roteiro com ojdbc17 e as credenciais do grupo. |
| DTOs, validação, rotas e HTTP | Entradas e saídas separadas, ModelMapper, rotas aninhadas e Location nas criações. | Demonstrar os contratos no Postman. |
| Exceções e regras | A suíte verificou 400, 401, 403, 404, 405, 409 e 500. Testou duas contas, exclusão com dependências, validade dos relatos, raio e ordenação. | Demonstrar 400 e 404 no vídeo. |
| CORS, sessão e frontend | Preflight de origem permitida e rejeitada, cookies, login, perfil e logout exercitados. | Validar no navegador com os domínios finais e integrar o React. |
| Docker e Render | Maven gerou o JAR executável. Dockerfile multi-stage e configuração por ambiente entregues. | Docker indisponível nesta máquina. Construir a imagem, publicar e verificar acesso ao Oracle pelo serviço. |
| Documentação | PDF com diagrama de classes e DER, contrato JSON e coleção Postman. | Preencher identificação e links da equipe. |

**Resultado local:** Maven `clean verify` passou com dois testes de integração, zero falhas e zero erros. Um teste verifica CORS. O outro percorre toda a jornada dos motoristas, os scripts e a sincronização. A falha de banco provocada no fim desse teste verifica que o HTTP 500 oculta os detalhes internos.

**Overpass:** o serviço público retornou HTTP 502 no teste da revisão. O arquivo de uma resposta real anterior, com 19 elementos, também foi usado na validação. Nenhum deles identificava um operador privado com acesso público. No teste de integração controlado, a rotina importou node/42 e way/42, ignorou o elemento sem classificação, atualizou pelo arquivo JSON e preservou o favorito. A validação com Oracle FIAP continua pendente.

## Entrega e apresentação

**Integrantes, RMs e turma:** preencher com os dados da equipe. **Publicação e vídeo:** links pendentes.

[Documentação técnica](docs/Documentacao-tecnica.pdf) · [Contrato de integração](docs/API.md)

**Roteiro de até cinco minutos.** 0:00–0:35 problema e público. 0:35–1:25 login e busca no frontend integrado. 1:25–2:20 reporte, nota e favorito no mapa. 2:20–3:20 CRUD no Postman e erros 400/404. 3:20–4:15 seguir um reporte do controller ao DTO, DAO e Oracle. 4:15–5:00 mostrar DER, autoria, expiração e URL publicada. A equipe precisa integrar o frontend, publicar e gravar o vídeo.

**Uso de IA.** O Codex foi usado na implementação inicial, nos scripts, nos testes locais e na documentação. A equipe deve revisar as decisões e reproduzir a demonstração antes da entrega acadêmica.
