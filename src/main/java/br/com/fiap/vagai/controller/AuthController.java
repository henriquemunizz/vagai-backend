package br.com.fiap.vagai.controller;

import br.com.fiap.vagai.bo.UsuarioBo;
import br.com.fiap.vagai.config.Autenticacao;
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

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioBo bo;
    private final Autenticacao autenticacao;
    private final ModelMapper mapper;

    public AuthController(UsuarioBo bo, Autenticacao autenticacao, ModelMapper mapper) {
        this.bo = bo;
        this.autenticacao = autenticacao;
        this.mapper = mapper;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDto> cadastrar(@Valid @RequestBody UsuarioCadastroRequestDto dto)
            throws SQLException {
        Usuario usuario = bo.cadastrar(dto);
        return ResponseEntity.created(URI.create("/usuarios/" + usuario.getId()))
                .body(mapper.map(usuario, UsuarioResponseDto.class));
    }

    @PostMapping("/login")
    public UsuarioResponseDto login(@Valid @RequestBody LoginRequestDto dto, HttpServletRequest request)
            throws SQLException {
        Usuario usuario = bo.login(dto);
        autenticacao.iniciar(request, usuario);
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    @GetMapping("/me")
    public UsuarioResponseDto atual(HttpServletRequest request) throws SQLException {
        return mapper.map(autenticacao.atual(request), UsuarioResponseDto.class);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        var sessao = request.getSession(false);
        if (sessao != null) sessao.invalidate();
        return ResponseEntity.noContent().build();
    }
}
