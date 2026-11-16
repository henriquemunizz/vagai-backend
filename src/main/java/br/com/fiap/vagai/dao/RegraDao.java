package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Regra;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class RegraDao extends JdbcDao<Regra> {
    public RegraDao(DataSource origem) {
        super(origem);
    }

    @Override
    protected Regra ler(ResultSet linha) throws SQLException {
        Regra registro = new Regra();
        registro.setId(linha.getLong("id_regra"));
        registro.setIdEstacionamento(linha.getLong("id_estacionamento"));
        registro.setDescricao(linha.getString("ds_regra"));
        registro.setDiaSemana(linha.getString("ds_dia_semana"));
        registro.setInicio(linha.getString("hr_inicio"));
        registro.setFim(linha.getString("hr_fim"));
        registro.setAtivo(linha.getInt("st_ativo"));
        return registro;
    }

    public Regra buscar(long id) throws SQLException {
        return primeiro("SELECT * FROM tb_regra_estacionamento WHERE id_regra = ?", id);
    }

    public List<Regra> listar() throws SQLException {
        return consultar("SELECT * FROM tb_regra_estacionamento ORDER BY id_regra");
    }

    public List<Regra> doLocal(long id) throws SQLException {
        return consultar("SELECT * FROM tb_regra_estacionamento WHERE id_estacionamento = ? ORDER BY id_regra DESC", id);
    }

    public void inserir(Regra registro) throws SQLException {
        registro.setId(proximoId("SELECT sq_tb_regra_estacionamento.NEXTVAL FROM dual"));
        executar("""
                INSERT INTO tb_regra_estacionamento (id_regra, id_estacionamento, ds_regra, ds_dia_semana,
                    hr_inicio, hr_fim, st_ativo)
                VALUES (?, ?, ?, ?,
                    ?, ?, ?)
                """,
                registro.getId(), registro.getIdEstacionamento(), registro.getDescricao(), registro.getDiaSemana(), 
                registro.getInicio(), registro.getFim(), registro.getAtivo());
    }

    public void atualizar(Regra registro) throws SQLException {
        executar("""
                UPDATE tb_regra_estacionamento
                SET ds_regra = ?, ds_dia_semana = ?, hr_inicio = ?, hr_fim = ?,
                    st_ativo = ?
                WHERE id_regra = ?
                """,
                registro.getDescricao(), registro.getDiaSemana(), registro.getInicio(), registro.getFim(), 
                registro.getAtivo(), registro.getId());
    }

    public void excluir(long id) throws SQLException {
        if (executar("DELETE FROM tb_regra_estacionamento WHERE id_regra = ?", id) == 0)
            throw new br.com.fiap.vagai.exception.NaoEncontrado();
    }
}
