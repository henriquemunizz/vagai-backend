package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Avaliacao;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class AvaliacaoDao extends JdbcDao<Avaliacao> {
    public AvaliacaoDao(DataSource origem) {
        super(origem);
    }

    @Override
    protected Avaliacao ler(ResultSet linha) throws SQLException {
        Avaliacao registro = new Avaliacao();
        registro.setId(linha.getLong("id_avaliacao"));
        registro.setIdUsuario(linha.getLong("id_usuario"));
        registro.setIdEstacionamento(linha.getLong("id_estacionamento"));
        registro.setNota(linha.getInt("nr_nota"));
        registro.setComentario(linha.getString("ds_comentario"));
        registro.setData(linha.getTimestamp("dt_avaliacao").toLocalDateTime());
        return registro;
    }

    public Avaliacao buscar(long id) throws SQLException {
        return primeiro("SELECT * FROM tb_avaliacao WHERE id_avaliacao = ?", id);
    }

    public List<Avaliacao> listar() throws SQLException {
        return consultar("SELECT * FROM tb_avaliacao ORDER BY id_avaliacao");
    }

    public List<Avaliacao> doLocal(long id) throws SQLException {
        return consultar("SELECT * FROM tb_avaliacao WHERE id_estacionamento = ? ORDER BY id_avaliacao DESC", id);
    }

    public void inserir(Avaliacao registro) throws SQLException {
        registro.setId(proximoId("SELECT sq_tb_avaliacao.NEXTVAL FROM dual"));
        executar("""
                INSERT INTO tb_avaliacao (id_avaliacao, id_usuario, id_estacionamento, nr_nota,
                    ds_comentario, dt_avaliacao)
                VALUES (?, ?, ?, ?,
                    ?, ?)
                """,
                registro.getId(), registro.getIdUsuario(), registro.getIdEstacionamento(), registro.getNota(), 
                registro.getComentario(), registro.getData());
    }

    public void atualizar(Avaliacao registro) throws SQLException {
        executar("UPDATE tb_avaliacao SET nr_nota = ?, ds_comentario = ? WHERE id_avaliacao = ?",
                registro.getNota(), registro.getComentario(), registro.getId());
    }

    public void excluir(long id) throws SQLException {
        if (executar("DELETE FROM tb_avaliacao WHERE id_avaliacao = ?", id) == 0)
            throw new br.com.fiap.vagai.exception.NaoEncontrado();
    }
}
