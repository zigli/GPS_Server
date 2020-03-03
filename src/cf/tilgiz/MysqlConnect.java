package cf.tilgiz;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;


public class MysqlConnect {

    public static Connection getConnection() throws IOException, SQLException {

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(".\\database.properties"))) {
            props.load(in);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        return DriverManager.getConnection(url, username, password);
    }

    public static Connection connectDb() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                System.out.println("Connection to Store DB succesfull!");
                return conn;
            } catch (SQLException ex) {
                System.out.println("Connection failed...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void disconnectDb(Connection connection) {
        try {
            connection.close();
            System.out.println("Disconnected from Store DB succesfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int writeDb(Connection connection, String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            int rows = statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 5;
    }
//    public static void connectDb1() {
//        String query = "SELECT date,m6 from online where id=1";
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
//            try (Connection conn = getConnection()) {
//                System.out.println("Connection to Store DB succesfull!");
//
//                statement = conn.createStatement();
//                resultSet = statement.executeQuery(query);
//                while (resultSet.next()) {
//
//                    int date = resultSet.getInt(1);
//                    int m6 = resultSet.getInt(2);
//                    System.out.printf("%d - %d \n", date, m6);
//                }
//                int rows = statement.executeUpdate("INSERT Products(ProductName, Price) VALUES ('iPhone X', 76000)," +
//                        "('Galaxy S9', 45000), ('Nokia 9', 36000)");
//                System.out.printf("Added %d rows", rows);

//                int rows = statement.executeUpdate("UPDATE Products SET Price = Price - 5000");
//                System.out.printf("Updated %d rows", rows);

//                String sql = "INSERT INTO Products (ProductName, Price) Values (?, ?)";
//                PreparedStatement preparedStatement = conn.prepareStatement(sql);
//                preparedStatement.setString(1, name);
//                preparedStatement.setInt(2, price);
//
//                int rows = preparedStatement.executeUpdate();
//
//                System.out.printf("%d rows added", rows);
//            }
//        } catch (Exception ex) {
//            System.out.println("Connection failed...");
//
//            System.out.println(ex);
//        }
//    }

}
