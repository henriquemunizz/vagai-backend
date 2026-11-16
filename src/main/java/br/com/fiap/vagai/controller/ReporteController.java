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
public class ReporteController {
    private final ReporteDao dao;
    private final EstacionamentoDao locais;
    private final Sessao sessao;
    private final ModelMapper mapper;

    public ReporteController(ReporteDao dao, EstacionamentoDao locais, Sessao sessao, ModelMapper mapper) {
        this.dao = dao;
        this.locais = locais;
        this.sessao = sessao;
        this.mapper = mapper;
    }

    @GetMapping("/reportes")
    public List<ReporteSaida> listar() throws SQLException {
        return dao.listar().stream().map(this::saida).toList();
    }

    @GetMapping("/reportes/{id}")
    public ReporteSaida buscar(@PathVariable long id) throws SQLException {
        Reporte registro = dao.buscar(id);
        return saida(registro);
    }

    @PostMapping("/reportes")
    public ResponseEntity<ReporteSaida> criar(@Valid @RequestBody ReporteEntrada entrada,
                                          HttpServletRequest request) throws SQLException {
        Usuario usuario = sessao.atual(request);
        Reporte registro = mapper.map(entrada, Reporte.class);
        registro.setUsuario(usuario);
        registro.setData(LocalDateTime.now(ZoneOffset.UTC).withNano(0));
        Estacionamento local = locais.bloquear(entrada.getIdEstacionamento());
        validarLocal(local);
        registro.setEstacionamento(local);
        dao.inserir(registro);
        return ResponseEntity.created(URI.create("/reportes/" + registro.getId())).body(saida(registro));
    }

    @PutMapping("/reportes/{id}")
    public ReporteSaida atualizar(@PathVariable long id, @Valid @RequestBody ReporteEntrada entrada,
                              HttpServletRequest request) throws SQLException {
        sessao.atual(request);
        Reporte registro = dao.buscar(id);
        sessao.exigirDono(request, registro.getIdUsuario());
        if (!registro.getIdEstacionamento().equals(entrada.getIdEstacionamento()))
            throw new Conflito("O vínculo com o estacionamento é permanente.");
        validarLocal(locais.bloquear(registro.getIdEstacionamento()));
        mapper.map(entrada, registro);
        dao.atualizar(registro);
        return saida(dao.buscar(id));
    }

    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id, HttpServletRequest request) throws SQLException {
        sessao.atual(request);
        Reporte registro = dao.buscar(id);
        sessao.exigirDono(request, registro.getIdUsuario());
        dao.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estacionamentos/{id}/reportes")
    public List<ReporteSaida> doLocal(@PathVariable long id) throws SQLException {
        locais.buscar(id);
        return dao.doLocal(id).stream().map(this::saida).toList();
    }

    @PostMapping("/estacionamentos/{id}/reportes")
    public ResponseEntity<ReporteSaida> criarNoLocal(@PathVariable long id,
            @Valid @RequestBody ReporteLocalEntrada dados, HttpServletRequest request) throws SQLException {
        ReporteEntrada entrada = mapper.map(dados, ReporteEntrada.class);
        entrada.setIdEstacionamento(id);
        return criar(entrada, request);
    }

    private void validarLocal(Estacionamento local) {
        if (local.getAtivo() == 0) throw new Conflito("Local inativo.");
        if ("PRIVADO".equals(local.getTipo())) throw new Conflito("Local privado não recebe reportes de vaga.");
    }

    private ReporteSaida saida(Reporte registro) {
        ReporteSaida resposta = mapper.map(registro, ReporteSaida.class);
        resposta.setAtual(registro.atualEm(LocalDateTime.now(ZoneOffset.UTC)));
        return resposta;
    }
}
