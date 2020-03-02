package cf.tilgiz;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static cf.tilgiz.MysqlConnect.getConnection;

public class Server {

    private static Statement statement;
    private static ResultSet resultSet;

    public static void main(String[] args) throws IOException {
        int port = (args.length != 0 && isNumeric(args[0]))? Integer.parseInt(args[0]): 8000;
        runServer(port);
    }

    private static void runServer(int port) throws IOException {
        Worker server = new Worker(port);
        while (true) {
            server.accept();
            String str = server.read();
            System.out.println(str);

//            connectDb();
            server.close();
        }
    }

    public static boolean isNumeric(final String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static void connectDb(){
        String query = "SELECT date,m6 from online where id=1";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()){
                System.out.println("Connection to Store DB succesfull!");

                statement = conn.createStatement();
                resultSet = statement.executeQuery(query);
                while(resultSet.next()){

                    int date = resultSet.getInt(1);
                    int m6 = resultSet.getInt(2);
                    System.out.printf("%d - %d \n", date, m6);
                }
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
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }

}
