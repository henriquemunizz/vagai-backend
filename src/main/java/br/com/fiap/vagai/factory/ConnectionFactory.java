package br.com.fiap.vagai.factory;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.stereotype.Component;

@Component
public class ConnectionFactory {
    private final DataSource dataSource;

    public ConnectionFactory(DataSource dataSource) {
        this.dataSource = new TransactionAwareDataSourceProxy(dataSource);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
