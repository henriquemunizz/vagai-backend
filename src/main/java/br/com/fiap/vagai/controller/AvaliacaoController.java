package br.com.fiap.vagai.controller;

import br.com.fiap.vagai.config.Sessao;
import br.com.fiap.vagai.dao.*;
import br.com.fiap.vagai.dto.*;
import br.com.fiap.vagai.exception.*;
import br.com.fiap.vagai.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.sql.SQLException;
import java.time.*;
import java.util.List;

@RestController
@Transactional(rollbackFor = SQLException.class)
public class AvaliacaoController {
    private final AvaliacaoDao dao;
    private final EstacionamentoDao locais;
    private final Sessao sessao;
    private final ModelMapper mapper;

    public AvaliacaoController(AvaliacaoDao dao, EstacionamentoDao locais, Sessao sessao, ModelMapper mapper) {
        this.dao = dao;
        this.locais = locais;
        this.sessao = sessao;
        this.mapper = mapper;
    }

    @GetMapping("/avaliacoes")
    public List<AvaliacaoSaida> listar() throws SQLException {
        return dao.listar().stream().map(this::saida).toList();
    }

    @GetMapping("/avaliacoes/{id}")
    public AvaliacaoSaida buscar(@PathVariable long id) throws SQLException {
        Avaliacao registro = dao.buscar(id);
        return saida(registro);
    }

    @PostMapping("/avaliacoes")
    public ResponseEntity<AvaliacaoSaida> criar(@Valid @RequestBody AvaliacaoEntrada entrada,
                                          HttpServletRequest request) throws SQLException {
        Usuario usuario = sessao.atual(request);
        Avaliacao registro = mapper.map(entrada, Avaliacao.class);
        registro.setUsuario(usuario);
        registro.setData(LocalDateTime.now(ZoneOffset.UTC).withNano(0));
        registro.setEstacionamento(locais.buscar(entrada.getIdEstacionamento()));
        dao.inserir(registro);
        return ResponseEntity.created(URI.create("/avaliacoes/" + registro.getId())).body(saida(registro));
    }

    @PutMapping("/avaliacoes/{id}")
    public AvaliacaoSaida atualizar(@PathVariable long id, @Valid @RequestBody AvaliacaoEntrada entrada,
                              HttpServletRequest request) throws SQLException {
        sessao.atual(request);
        Avaliacao registro = dao.buscar(id);
        sessao.exigirDono(request, registro.getIdUsuario());
        if (!registro.getIdEstacionamento().equals(entrada.getIdEstacionamento()))
            throw new Conflito("O vínculo com o estacionamento é permanente.");
        mapper.map(entrada, registro);
        dao.atualizar(registro);
        return saida(dao.buscar(id));
    }

    @DeleteMapping("/avaliacoes/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id, HttpServletRequest request) throws SQLException {
        sessao.atual(request);
        Avaliacao registro = dao.buscar(id);
        sessao.exigirDono(request, registro.getIdUsuario());
        dao.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estacionamentos/{id}/avaliacoes")
    public List<AvaliacaoSaida> doLocal(@PathVariable long id) throws SQLException {
        locais.buscar(id);
        return dao.doLocal(id).stream().map(this::saida).toList();
    }

    @PostMapping("/estacionamentos/{id}/avaliacoes")
    public ResponseEntity<AvaliacaoSaida> criarNoLocal(@PathVariable long id,
            @Valid @RequestBody AvaliacaoLocalEntrada dados, HttpServletRequest request) throws SQLException {
        AvaliacaoEntrada entrada = mapper.map(dados, AvaliacaoEntrada.class);
        entrada.setIdEstacionamento(id);
        return criar(entrada, request);
    }

    private AvaliacaoSaida saida(Avaliacao registro) {
        return mapper.map(registro, AvaliacaoSaida.class);
    }
}
