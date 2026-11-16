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
import br.com.fiap.vagai.service.Senhas;

@RestController
@Transactional(rollbackFor = SQLException.class)
public class UsuarioController {
    private final UsuarioDao usuarios;
    private final Sessao sessao;
    private final ModelMapper mapper;

    public UsuarioController(UsuarioDao usuarios, Sessao sessao, ModelMapper mapper) {
        this.usuarios = usuarios;
        this.sessao = sessao;
        this.mapper = mapper;
    }

    @PostMapping({"/usuarios", "/auth/cadastro"})
    public ResponseEntity<UsuarioSaida> cadastrar(@Valid @RequestBody UsuarioEntrada entrada) throws SQLException {
        String nome = entrada.getNome().strip();
        if (!usuarios.porNome(nome).isEmpty()) throw new Conflito("Nome de usuário já cadastrado.");
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setSenhaHash(Senhas.proteger(entrada.getSenha()));
        usuario.setCadastro(LocalDateTime.now(ZoneOffset.UTC).withNano(0));
        usuarios.inserir(usuario);
        return ResponseEntity.created(URI.create("/usuarios/" + usuario.getId())).body(saida(usuario));
    }

    @PostMapping("/auth/login")
    public UsuarioSaida login(@Valid @RequestBody LoginEntrada entrada, HttpServletRequest request) throws SQLException {
        Usuario usuario = usuarios.porNome(entrada.nome().strip()).stream().findFirst().orElseThrow(NaoAutenticado::new);
        if (!Senhas.confere(entrada.senha(), usuario.getSenhaHash())) throw new NaoAutenticado();
        sessao.iniciar(request, usuario);
        return saida(usuario);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        var atual = request.getSession(false);
        if (atual != null) atual.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/auth/me")
    public UsuarioSaida perfil(HttpServletRequest request) throws SQLException {
        return saida(sessao.atual(request));
    }

    @GetMapping("/usuarios")
    public List<UsuarioSaida> listar(HttpServletRequest request) throws SQLException {
        return List.of(perfil(request));
    }

    @GetMapping("/usuarios/{id}")
    public UsuarioSaida buscar(@PathVariable long id, HttpServletRequest request) throws SQLException {
        sessao.exigirDono(request, id);
        return saida(usuarios.buscar(id));
    }

    @PutMapping("/usuarios/{id}")
    public UsuarioSaida atualizar(@PathVariable long id, @Valid @RequestBody UsuarioEntrada entrada,
                                 HttpServletRequest request) throws SQLException {
        sessao.exigirDono(request, id);
        Usuario usuario = usuarios.buscar(id);
        usuario.setNome(entrada.getNome().strip());
        usuario.setSenhaHash(Senhas.proteger(entrada.getSenha()));
        usuarios.atualizar(usuario);
        sessao.iniciar(request, usuario);
        return saida(usuario);
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id, HttpServletRequest request) throws SQLException {
        sessao.exigirDono(request, id);
        usuarios.excluir(id);
        return logout(request);
    }

    private UsuarioSaida saida(Usuario usuario) {
        return mapper.map(usuario, UsuarioSaida.class);
    }
}
