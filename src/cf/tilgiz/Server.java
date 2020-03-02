package cf.tilgiz;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import static cf.tilgiz.MysqlConnect.getConnection;

public class Server {

    public static final String ONLINE_STATUS_FILE;
    public static final String ONLINE_TRACK_FILE;
    public static final int SKIP_DB_TIMEOUT;

    private static Statement statement;
    private static ResultSet resultSet;

    static {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(".\\src\\cf\\tilgiz\\Server.properties"))) {
            props.load(in);
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Can not read configuration file (Server.properties)");
            System.exit(0);
        }
        ONLINE_STATUS_FILE = props.getProperty("ONLINE_STATUS_FILE");
        ONLINE_TRACK_FILE = props.getProperty("ONLINE_TRACK_FILE");
        SKIP_DB_TIMEOUT = Integer.parseInt(props.getProperty("SKIP_DB_TIMEOUT"));
    }

    public static void main(String[] args) throws IOException {
        int port = (args.length != 0 && isNumeric(args[0]))? Integer.parseInt(args[0]): 8000;
        runServer(port);
    }

    private static void runServer(int port) throws IOException {
        Worker server = new Worker(port);
        while (true) {
            server.accept();
            String clientIp = server.getClientIpAddress();
            String str = server.read();
            System.out.println(clientIp + " : " + str);

            String inputStr = "[3G*1208014868*00E3*UD2,290318,170736,V,54.891907,N,52.3444133,E,0.00,133.6,0.0,7,100,100,0,0,00000008,6,255,250,2,1634,46522,149,1634,47882,138,1634,46532,137,1634,49333,137,1634,47941,137,1634,47884,136,1,Keenetic-2154,28:28:5d:b9:7d:f0,-79,37.3]";
//        String inputStr = "[3G*1208014868*000A*LK,0,0,100]";

            Parser parser = new Parser(inputStr,clientIp);
            System.out.println(ONLINE_STATUS_FILE);
            parser.parseString();
            System.out.println(parser.checkDbSkip());
            System.out.println(parser.buildQuery());


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
