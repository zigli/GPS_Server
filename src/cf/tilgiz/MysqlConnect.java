package cf.tilgiz;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;


public class MysqlConnect {

    private static Statement statement;

    public static Connection getConnection() throws IOException, SQLException {

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
            props.load(in);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        return DriverManager.getConnection(url, username, password);
    }

    public static int writeDb(String query) throws Exception {
        int rows;
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection connection = getConnection()) {
                statement = connection.createStatement();
                rows = statement.executeUpdate(query);
            }
        return rows;
    }
}
