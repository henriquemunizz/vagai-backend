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
public class RegraController {
    private final RegraDao dao;
    private final EstacionamentoDao locais;
    private final Sessao sessao;
    private final ModelMapper mapper;

    public RegraController(RegraDao dao, EstacionamentoDao locais, Sessao sessao, ModelMapper mapper) {
        this.dao = dao;
        this.locais = locais;
        this.sessao = sessao;
        this.mapper = mapper;
    }

    @GetMapping("/regras")
    public List<RegraSaida> listar() throws SQLException {
        return dao.listar().stream().map(this::saida).toList();
    }

    @GetMapping("/regras/{id}")
    public RegraSaida buscar(@PathVariable long id) throws SQLException {
        Regra registro = dao.buscar(id);
        return saida(registro);
    }

    @PostMapping("/regras")
    public ResponseEntity<RegraSaida> criar(@Valid @RequestBody RegraEntrada entrada,
                                          HttpServletRequest request) throws SQLException {
        sessao.atual(request);
        Regra registro = mapper.map(entrada, Regra.class);
        exigirLocalEditavel(request, locais.bloquear(entrada.getIdEstacionamento()));
        dao.inserir(registro);
        return ResponseEntity.created(URI.create("/regras/" + registro.getId())).body(saida(registro));
    }

    @PutMapping("/regras/{id}")
    public RegraSaida atualizar(@PathVariable long id, @Valid @RequestBody RegraEntrada entrada,
                              HttpServletRequest request) throws SQLException {
        sessao.atual(request);
        Regra registro = dao.buscar(id);
        exigirLocalEditavel(request, locais.bloquear(registro.getIdEstacionamento()));
        if (!registro.getIdEstacionamento().equals(entrada.getIdEstacionamento()))
            throw new Conflito("O vínculo com o estacionamento é permanente.");
        mapper.map(entrada, registro);
        dao.atualizar(registro);
        return saida(dao.buscar(id));
    }

    @DeleteMapping("/regras/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id, HttpServletRequest request) throws SQLException {
        sessao.atual(request);
        Regra registro = dao.buscar(id);
        exigirLocalEditavel(request, locais.bloquear(registro.getIdEstacionamento()));
        dao.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estacionamentos/{id}/regras")
    public List<RegraSaida> doLocal(@PathVariable long id) throws SQLException {
        locais.buscar(id);
        return dao.doLocal(id).stream().map(this::saida).toList();
    }

    private void exigirLocalEditavel(HttpServletRequest request, Estacionamento local) throws SQLException {
        sessao.exigirDono(request, local.getIdUsuarioCriador());
        if (local.getIdOsm() != null || local.getVerificacao() != null) throw new SemPermissao();
    }

    private RegraSaida saida(Regra registro) {
        return mapper.map(registro, RegraSaida.class);
    }
}
