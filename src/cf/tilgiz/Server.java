package cf.tilgiz;

import java.io.*;

public class Server {

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
