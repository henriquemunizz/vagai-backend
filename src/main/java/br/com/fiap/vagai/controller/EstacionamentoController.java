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
public class EstacionamentoController {
    private final EstacionamentoDao dao;
    private final Sessao sessao;
    private final ModelMapper mapper;

    public EstacionamentoController(EstacionamentoDao dao, Sessao sessao, ModelMapper mapper) {
        this.dao = dao;
        this.sessao = sessao;
        this.mapper = mapper;
    }

    @GetMapping("/estacionamentos")
    public List<EstacionamentoSaida> listar() throws SQLException {
        return dao.listar().stream().map(this::saida).toList();
    }

    @GetMapping("/estacionamentos/{id}")
    public EstacionamentoSaida buscar(@PathVariable long id) throws SQLException {
        Estacionamento registro = dao.buscar(id);
        return saida(registro);
    }

    @PostMapping("/estacionamentos")
    public ResponseEntity<EstacionamentoSaida> criar(@Valid @RequestBody EstacionamentoEntrada entrada,
                                          HttpServletRequest request) throws SQLException {
        Usuario usuario = sessao.atual(request);
        Estacionamento registro = mapper.map(entrada, Estacionamento.class);
        registro.setCriador(usuario);
        registro.setFonte("COLABORATIVA");
        dao.inserir(registro);
        return ResponseEntity.created(URI.create("/estacionamentos/" + registro.getId())).body(saida(registro));
    }

    @PutMapping("/estacionamentos/{id}")
    public EstacionamentoSaida atualizar(@PathVariable long id, @Valid @RequestBody EstacionamentoEntrada entrada,
                              HttpServletRequest request) throws SQLException {
        sessao.atual(request);
        Estacionamento registro = dao.bloquear(id);
        exigirLocalEditavel(request, registro);
        mapper.map(entrada, registro);
        dao.atualizar(registro);
        return saida(dao.buscar(id));
    }

    @DeleteMapping("/estacionamentos/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id, HttpServletRequest request) throws SQLException {
        sessao.atual(request);
        Estacionamento registro = dao.bloquear(id);
        exigirLocalEditavel(request, registro);
        dao.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private void exigirLocalEditavel(HttpServletRequest request, Estacionamento local) throws SQLException {
        sessao.exigirDono(request, local.getIdUsuarioCriador());
        if (local.getIdOsm() != null || local.getVerificacao() != null) throw new SemPermissao();
    }

    private EstacionamentoSaida saida(Estacionamento registro) {
        return mapper.map(registro, EstacionamentoSaida.class);
    }
}
