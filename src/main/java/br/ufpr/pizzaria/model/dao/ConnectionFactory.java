package br.ufpr.pizzaria.model.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Fábrica de conexões (padrão Factory, como na aula de JDBC).
 * Lê url, usuário e senha do arquivo db.properties, assim é possível trocar
 * a configuração do banco sem recompilar o código.
 */
public class ConnectionFactory {

    private static final String ARQUIVO = "/db.properties";
    private static Properties properties;

    // Construtor privado: garante que a fábrica não será instanciada
    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException, IOException {
        readProperties();
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.pwd"));
    }

    /**
     * Carrega o db.properties uma única vez. O arquivo é lido do classpath
     * (src/main/resources), o que funciona tanto na IDE quanto dentro do .jar.
     */
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
