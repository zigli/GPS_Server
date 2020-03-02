package cf.tilgiz;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import static cf.tilgiz.MysqlConnect.*;

public class Server {

    public static final String ONLINE_STATUS_FILE;
    public static final String ONLINE_TRACK_FILE;
    public static final int SKIP_DB_TIMEOUT;

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
        int port = (args.length != 0 && isNumeric(args[0])) ? Integer.parseInt(args[0]) : 8000;
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
//            String inputStr = "[3G*1208014868*000A*LK,0,0,100]";

            Parser parser = new Parser(inputStr, clientIp);
            parser.parseString();
            //System.out.println(parser.checkDbSkip());
            if (!parser.checkDbSkip()) {
                System.out.println(parser.buildQuery());
                Connection connection = connectDb();
                if (connection != null) disconnectDb(connection);
            }
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

}
