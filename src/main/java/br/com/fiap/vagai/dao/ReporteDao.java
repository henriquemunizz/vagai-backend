package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Reporte;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class ReporteDao extends JdbcDao<Reporte> {
    public ReporteDao(DataSource origem) {
        super(origem);
    }

    @Override
    protected Reporte ler(ResultSet linha) throws SQLException {
        Reporte registro = new Reporte();
        registro.setId(linha.getLong("id_reporte"));
        registro.setIdUsuario(linha.getLong("id_usuario"));
        registro.setIdEstacionamento(linha.getLong("id_estacionamento"));
        registro.setTipo(linha.getString("tp_reporte"));
        registro.setObservacao(linha.getString("ds_observacao"));
        registro.setData(linha.getTimestamp("dt_reporte").toLocalDateTime());
        return registro;
    }

    public Reporte buscar(long id) throws SQLException {
        return primeiro("SELECT * FROM tb_reporte WHERE id_reporte = ?", id);
    }

    public List<Reporte> listar() throws SQLException {
        return consultar("SELECT * FROM tb_reporte ORDER BY id_reporte");
    }

    public List<Reporte> doLocal(long id) throws SQLException {
        return consultar("SELECT * FROM tb_reporte WHERE id_estacionamento = ? ORDER BY id_reporte DESC", id);
    }

    public void inserir(Reporte registro) throws SQLException {
        registro.setId(proximoId("SELECT sq_tb_reporte.NEXTVAL FROM dual"));
        executar("""
                INSERT INTO tb_reporte (id_reporte, id_usuario, id_estacionamento, tp_reporte,
                    ds_observacao, dt_reporte)
                VALUES (?, ?, ?, ?,
                    ?, ?)
                """,
                registro.getId(), registro.getIdUsuario(), registro.getIdEstacionamento(), registro.getTipo(), 
                registro.getObservacao(), registro.getData());
    }

    public void atualizar(Reporte registro) throws SQLException {
        executar("UPDATE tb_reporte SET tp_reporte = ?, ds_observacao = ? WHERE id_reporte = ?",
                registro.getTipo(), registro.getObservacao(), registro.getId());
    }

    public void excluir(long id) throws SQLException {
        if (executar("DELETE FROM tb_reporte WHERE id_reporte = ?", id) == 0)
            throw new br.com.fiap.vagai.exception.NaoEncontrado();
    }
}
