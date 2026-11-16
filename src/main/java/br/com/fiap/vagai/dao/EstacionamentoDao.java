package br.com.fiap.vagai.dao;

import br.com.fiap.vagai.model.Estacionamento;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class EstacionamentoDao extends JdbcDao<Estacionamento> {
    public EstacionamentoDao(DataSource origem) {
        super(origem);
    }

    @Override
    protected Estacionamento ler(ResultSet linha) throws SQLException {
        Estacionamento registro = new Estacionamento();
        registro.setId(linha.getLong("id_estacionamento"));
        registro.setIdUsuarioCriador(linha.getObject("id_usuario_criador") == null ? null : linha.getLong("id_usuario_criador"));
        registro.setNome(linha.getString("nm_estacionamento"));
        registro.setDescricao(linha.getString("ds_estacionamento"));
        registro.setTipo(linha.getString("tp_estacionamento"));
        registro.setLatitude(linha.getBigDecimal("nr_latitude"));
        registro.setLongitude(linha.getBigDecimal("nr_longitude"));
        registro.setEndereco(linha.getString("ds_endereco"));
        registro.setPreco(linha.getBigDecimal("vl_preco"));
        registro.setHorario(linha.getString("ds_horario"));
        registro.setFonte(linha.getString("ds_fonte"));
        registro.setIdOsm(linha.getString("ds_id_osm"));
        registro.setVerificacao(linha.getTimestamp("dt_verificacao") == null ? null : linha.getTimestamp("dt_verificacao").toLocalDateTime());
        registro.setAtivo(linha.getInt("st_estacionamento"));
        return registro;
    }

    public Estacionamento buscar(long id) throws SQLException {
        return primeiro("SELECT * FROM tb_estacionamento WHERE id_estacionamento = ?", id);
    }

    public Estacionamento bloquear(long id) throws SQLException {
        return primeiro("SELECT * FROM tb_estacionamento WHERE id_estacionamento = ? FOR UPDATE", id);
    }

    public List<Estacionamento> ativos(String tipo) throws SQLException {
        if (tipo == null) return consultar("SELECT * FROM tb_estacionamento WHERE st_estacionamento = 1");
        return consultar("SELECT * FROM tb_estacionamento WHERE st_estacionamento = 1 AND tp_estacionamento = ?", tipo);
    }

    public List<Estacionamento> porOsm(String chave) throws SQLException {
        return consultar("SELECT * FROM tb_estacionamento WHERE ds_id_osm = ? FOR UPDATE", chave);
    }
    public List<Estacionamento> listar() throws SQLException {
        return consultar("SELECT * FROM tb_estacionamento ORDER BY id_estacionamento");
    }


    public void inserir(Estacionamento registro) throws SQLException {
        registro.setId(proximoId("SELECT sq_tb_estacionamento.NEXTVAL FROM dual"));
        executar("""
                INSERT INTO tb_estacionamento (id_estacionamento, id_usuario_criador, nm_estacionamento, ds_estacionamento,
                    tp_estacionamento, nr_latitude, nr_longitude, ds_endereco,
                    vl_preco, ds_horario, ds_fonte, ds_id_osm,
                    dt_verificacao, st_estacionamento)
                VALUES (?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?)
                """,
                registro.getId(), registro.getIdUsuarioCriador(), registro.getNome(), registro.getDescricao(), 
                registro.getTipo(), registro.getLatitude(), registro.getLongitude(), registro.getEndereco(), 
                registro.getPreco(), registro.getHorario(), registro.getFonte(), registro.getIdOsm(), 
                registro.getVerificacao(), registro.getAtivo());
    }

    public void atualizar(Estacionamento registro) throws SQLException {
        executar("""
                UPDATE tb_estacionamento
                SET nm_estacionamento = ?, ds_estacionamento = ?, tp_estacionamento = ?, nr_latitude = ?,
                    nr_longitude = ?, ds_endereco = ?, vl_preco = ?, ds_horario = ?,
                    st_estacionamento = ?
                WHERE id_estacionamento = ?
                """,
                registro.getNome(), registro.getDescricao(), registro.getTipo(), registro.getLatitude(), 
                registro.getLongitude(), registro.getEndereco(), registro.getPreco(), registro.getHorario(), 
                registro.getAtivo(), registro.getId());
    }

    public void excluir(long id) throws SQLException {
        if (executar("DELETE FROM tb_estacionamento WHERE id_estacionamento = ?", id) == 0)
            throw new br.com.fiap.vagai.exception.NaoEncontrado();
    }
}
