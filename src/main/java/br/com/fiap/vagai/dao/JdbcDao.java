package br.com.fiap.vagai.dao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import br.com.fiap.vagai.exception.NaoEncontrado;

abstract class JdbcDao<T> {
    private final DataSource conexoes;

    JdbcDao(DataSource origem) {
        conexoes = new TransactionAwareDataSourceProxy(origem);
    }

    protected abstract T ler(ResultSet linha) throws SQLException;

    protected List<T> consultar(String sql, Object... valores) throws SQLException {
        try (Connection con = conexoes.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            preencher(stmt, valores);
            try (ResultSet linhas = stmt.executeQuery()) {
                List<T> resultado = new ArrayList<>();
                while (linhas.next()) resultado.add(ler(linhas));
                return resultado;
            }
        }
    }

    protected T primeiro(String sql, Object... valores) throws SQLException {
        return consultar(sql, valores).stream().findFirst().orElseThrow(NaoEncontrado::new);
    }

    protected int executar(String sql, Object... valores) throws SQLException {
        try (Connection con = conexoes.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            preencher(stmt, valores);
            return stmt.executeUpdate();
        }
    }

    protected long proximoId(String sql) throws SQLException {
        try (Connection con = conexoes.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet linha = stmt.executeQuery()) {
            linha.next();
            return linha.getLong(1);
        }
    }

    private void preencher(PreparedStatement stmt, Object[] valores) throws SQLException {
        for (int i = 0; i < valores.length; i++) {
            Object valor = valores[i];
            if (valor instanceof java.time.LocalDateTime data) valor = Timestamp.valueOf(data);
            if (valor == null) stmt.setNull(i + 1, Types.NULL);
            else stmt.setObject(i + 1, valor);
        }
    }
}
