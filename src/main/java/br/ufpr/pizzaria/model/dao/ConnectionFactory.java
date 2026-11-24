package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final String ARQUIVO = "/db.properties";
    private static Properties properties;

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException, IOException {
        readProperties();
        try {
            return DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.pwd"));
        } catch (SQLException e) {
            throw new SQLException("Não foi possível conectar ao banco de dados \"pizzaria\".\n"
                    + "Verifique se o MySQL está ligado, se o script banco/pizzaria.sql foi executado\n"
                    + "e se usuário e senha em db.properties estão corretos.\n\nDetalhe: " + e.getMessage(), e);
        }
    }

    private static void readProperties() throws IOException {
        if (properties == null) {
            try (InputStream arquivo = ConnectionFactory.class.getResourceAsStream(ARQUIVO)) {
                if (arquivo == null) {
                    throw new IOException("Arquivo de configuração " + ARQUIVO + " não encontrado.");
                }
                Properties props = new Properties();
                props.load(arquivo);
                properties = props;
            }
        }
    }
}
