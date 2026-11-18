package br.com.fiap.vagai.controller;

import br.com.fiap.vagai.bo.UsuarioBo;
import br.com.fiap.vagai.config.Autenticacao;
import br.com.fiap.vagai.dao.UsuarioDao;
import br.com.fiap.vagai.dto.*;
import br.com.fiap.vagai.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Transactional(rollbackFor = SQLException.class)
public class UsuarioController {
    private final UsuarioDao dao;
    private final UsuarioBo bo;
    private final Autenticacao autenticacao;
    private final ModelMapper mapper;

    public UsuarioController(UsuarioDao dao, UsuarioBo bo, Autenticacao autenticacao, ModelMapper mapper) {
        this.dao = dao;
        this.bo = bo;
        this.autenticacao = autenticacao;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> cadastrar(@Valid @RequestBody UsuarioCadastroRequestDto dto)
            throws SQLException {
        Usuario usuario = bo.cadastrar(dto);
        return ResponseEntity.created(URI.create("/usuarios/" + usuario.getId()))
                .body(mapper.map(usuario, UsuarioResponseDto.class));
    }

    @GetMapping
    public List<UsuarioResponseDto> listar(HttpServletRequest request) throws SQLException {
        return List.of(mapper.map(autenticacao.atual(request), UsuarioResponseDto.class));
    }

    @GetMapping("/{id}")
    public UsuarioResponseDto buscar(@PathVariable long id, HttpServletRequest request) throws SQLException {
        autenticacao.exigirDono(request, id);
        return mapper.map(dao.buscar(id), UsuarioResponseDto.class);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDto atualizar(@PathVariable long id, @Valid @RequestBody UsuarioAtualizacaoRequestDto dto,
                                        HttpServletRequest request) throws SQLException {
        autenticacao.exigirDono(request, id);
        Usuario usuario = bo.atualizar(id, dto);
        autenticacao.iniciar(request, usuario);
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable long id, HttpServletRequest request) throws SQLException {
        autenticacao.exigirDono(request, id);
        dao.remover(id);
        request.getSession(false).invalidate();
        return ResponseEntity.noContent().build();
    }
}
