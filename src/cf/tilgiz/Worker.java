package cf.tilgiz;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker {
    ServerSocket serverSocket;
    Socket clientSocket;
    DataInputStream reader;

    Worker(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void accept() throws IOException {
        clientSocket = serverSocket.accept();
    }

    private void createReaderStream() throws IOException {
//        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        reader = new DataInputStream(clientSocket.getInputStream());
    }

    public String read() throws IOException {
        createReaderStream();
        return reader.readUTF();
    }

    public String getClientIpAddress(){
        return clientSocket.getInetAddress().getHostAddress();
    }

    public void close() throws IOException {
        reader.close();
        clientSocket.close();
    }
}
