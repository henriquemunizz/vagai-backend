package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Usuario;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class UsuarioDao extends JdbcDao<Usuario> {
    public UsuarioDao(DataSource origem) {
        super(origem);
    }

    @Override
    protected Usuario ler(ResultSet linha) throws SQLException {
        Usuario registro = new Usuario();
        registro.setId(linha.getLong("id_usuario"));
        registro.setNome(linha.getString("nm_usuario"));
        registro.setSenhaHash(linha.getString("ds_senha"));
        registro.setCadastro(linha.getTimestamp("dt_cadastro").toLocalDateTime());
        return registro;
    }

    public Usuario buscar(long id) throws SQLException {
        return primeiro("SELECT * FROM tb_usuario WHERE id_usuario = ?", id);
    }

    public List<Usuario> porNome(String nome) throws SQLException {
        return consultar("SELECT * FROM tb_usuario WHERE nm_usuario = ?", nome);
    }


    public void inserir(Usuario registro) throws SQLException {
        registro.setId(proximoId("SELECT sq_tb_usuario.NEXTVAL FROM dual"));
        executar("INSERT INTO tb_usuario (id_usuario, nm_usuario, ds_senha, dt_cadastro) VALUES (?, ?, ?, ?)",
                registro.getId(), registro.getNome(), registro.getSenhaHash(), registro.getCadastro());
    }

    public void atualizar(Usuario registro) throws SQLException {
        executar("UPDATE tb_usuario SET nm_usuario = ?, ds_senha = ? WHERE id_usuario = ?",
                registro.getNome(), registro.getSenhaHash(), registro.getId());
    }

    public void excluir(long id) throws SQLException {
        if (executar("DELETE FROM tb_usuario WHERE id_usuario = ?", id) == 0)
            throw new br.com.fiap.vagai.exception.NaoEncontrado();
    }
}
