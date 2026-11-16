package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Favorito;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class FavoritoDao extends JdbcDao<Favorito> {
    public FavoritoDao(DataSource origem) {
        super(origem);
    }

    @Override
    protected Favorito ler(ResultSet linha) throws SQLException {
        Favorito registro = new Favorito();
        registro.setId(linha.getLong("id_usuario_favorito"));
        registro.setIdUsuario(linha.getLong("id_usuario"));
        registro.setIdEstacionamento(linha.getLong("id_estacionamento"));
        registro.setData(linha.getTimestamp("dt_favorito").toLocalDateTime());
        return registro;
    }

    public Favorito buscar(long id) throws SQLException {
        return primeiro("SELECT * FROM tb_usuario_favorito WHERE id_usuario_favorito = ?", id);
    }

    public List<Favorito> doUsuario(long usuario) throws SQLException {
        return consultar("SELECT * FROM tb_usuario_favorito WHERE id_usuario = ? ORDER BY id_usuario_favorito", usuario);
    }


    public void inserir(Favorito registro) throws SQLException {
        registro.setId(proximoId("SELECT sq_tb_usuario_favorito.NEXTVAL FROM dual"));
        executar("""
                INSERT INTO tb_usuario_favorito (id_usuario_favorito, id_usuario, id_estacionamento, dt_favorito)
                VALUES (?, ?, ?, ?)
                """,
                registro.getId(), registro.getIdUsuario(), registro.getIdEstacionamento(), registro.getData());
    }

    public void atualizar(Favorito registro) throws SQLException {
        executar("UPDATE tb_usuario_favorito SET id_estacionamento = ? WHERE id_usuario_favorito = ?",
                registro.getIdEstacionamento(), registro.getId());
    }

    public void excluir(long id) throws SQLException {
        if (executar("DELETE FROM tb_usuario_favorito WHERE id_usuario_favorito = ?", id) == 0)
            throw new br.com.fiap.vagai.exception.NaoEncontrado();
    }
}
