package cf.tilgiz;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static cf.tilgiz.MysqlConnect.*;

public class Server {

    public static final String ONLINE_STATUS_FILE;
    public static final String ONLINE_TRACK_FILE;
    public static final int SKIP_DB_TIMEOUT;
    public static int LISTEN_PORT = 8000;
    private static Logger LOGGER;

    static {
        try(FileInputStream ins = new FileInputStream("log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Server.class.getName());
        }catch (Exception e){
            LOGGER.log(Level.WARNING,"Exception (log.config)", e);
        }
    }

    static {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("Server.properties"))) {
            props.load(in);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Can not read configuration file (Server.properties)", e);
            System.exit(0);
        }
        ONLINE_STATUS_FILE = props.getProperty("ONLINE_STATUS_FILE");
        ONLINE_TRACK_FILE = props.getProperty("ONLINE_TRACK_FILE");
        SKIP_DB_TIMEOUT = Integer.parseInt(props.getProperty("SKIP_DB_TIMEOUT"));
        try {
            LISTEN_PORT = Integer.parseInt(props.getProperty("LISTEN_PORT"));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING,"Invalid port number, ", e);
            System.exit(0);
        }
    }

    public static void main(String[] args){
        try {
            runServer(LISTEN_PORT);
        } catch (IllegalArgumentException | IOException e) {
            LOGGER.log(Level.WARNING,"runServer exception:", e);
        }

    }

    private static void runServer(int port) throws IOException {
        Worker server = new Worker(port);
        LOGGER.log(Level.INFO,"Server started. Waiting connection on port: " + port);
        while (true) {
            server.accept();
            String clientIp = server.getClientIpAddress();
            LOGGER.log(Level.INFO,"Client connected with ip: " + clientIp);
            String str = server.read();
            LOGGER.log(Level.INFO,"Input string: " + str);
            Parser parser = new Parser(str, clientIp);
            try {
                parser.parseString();
                if (!parser.checkDbSkip()) {
                    try {
                        String query = parser.buildQuery();
                        LOGGER.log(Level.INFO,"Query string: " + query);
                        int rows = writeDb(query);
                        if(rows == -1){
                            LOGGER.log(Level.WARNING,"Can not write DB!");
                        } else {
                            LOGGER.log(Level.INFO,rows + " rows written DB!");
                        }
                    }catch (IOException e){
                        LOGGER.log(Level.WARNING,"IO exception catched:", e);
                    }catch (SQLException e){
                        LOGGER.log(Level.WARNING,"SQL exception catched:", e);
                    }catch (Exception e){
                        LOGGER.log(Level.WARNING,"Exception catched:", e);
                    }
                }

            }catch (IOException e){
                LOGGER.log(Level.WARNING,"IO exception catched:", e);
            }catch (Exception e) {
                LOGGER.log(Level.WARNING,"Incorrect input data", e);
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
