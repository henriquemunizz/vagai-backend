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
public class FavoritoController {
    private final FavoritoDao dao;
    private final EstacionamentoDao locais;
    private final Sessao sessao;
    private final ModelMapper mapper;

    public FavoritoController(FavoritoDao dao, EstacionamentoDao locais, Sessao sessao, ModelMapper mapper) {
        this.dao = dao;
        this.locais = locais;
        this.sessao = sessao;
        this.mapper = mapper;
    }

    @GetMapping({"/favoritos", "/usuarios/me/favoritos"})
    public List<FavoritoSaida> listar(HttpServletRequest request) throws SQLException {
        return dao.doUsuario(sessao.atual(request).getId()).stream().map(this::saida).toList();
    }

    @GetMapping("/favoritos/{id}")
    public FavoritoSaida buscar(@PathVariable long id, HttpServletRequest request) throws SQLException {
        long usuario = sessao.atual(request).getId();
        Favorito registro = dao.buscar(id);
        if (registro.getIdUsuario() != usuario) throw new SemPermissao();
        return saida(registro);
    }

    @PostMapping("/favoritos")
    public ResponseEntity<FavoritoSaida> criar(@Valid @RequestBody FavoritoEntrada entrada,
                                          HttpServletRequest request) throws SQLException {
        Usuario usuario = sessao.atual(request);
        Favorito registro = mapper.map(entrada, Favorito.class);
        registro.setUsuario(usuario);
        registro.setData(LocalDateTime.now(ZoneOffset.UTC).withNano(0));
        registro.setEstacionamento(locais.buscar(entrada.getIdEstacionamento()));
        dao.inserir(registro);
        return ResponseEntity.created(URI.create("/favoritos/" + registro.getId())).body(saida(registro));
    }

    @PutMapping("/favoritos/{id}")
    public FavoritoSaida atualizar(@PathVariable long id, @Valid @RequestBody FavoritoEntrada entrada,
                              HttpServletRequest request) throws SQLException {
        sessao.atual(request);
        Favorito registro = dao.buscar(id);
        sessao.exigirDono(request, registro.getIdUsuario());
        locais.buscar(entrada.getIdEstacionamento());
        mapper.map(entrada, registro);
        dao.atualizar(registro);
        return saida(dao.buscar(id));
    }

    @DeleteMapping("/favoritos/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id, HttpServletRequest request) throws SQLException {
        sessao.atual(request);
        Favorito registro = dao.buscar(id);
        sessao.exigirDono(request, registro.getIdUsuario());
        dao.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private FavoritoSaida saida(Favorito registro) {
        return mapper.map(registro, FavoritoSaida.class);
    }
}
