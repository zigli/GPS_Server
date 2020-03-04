package cf.tilgiz;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Worker {
    ServerSocket serverSocket;
    Socket clientSocket;
    BufferedInputStream reader;

    Worker(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void accept() throws IOException {
        clientSocket = serverSocket.accept();
    }

    private void createReaderStream() throws IOException {
        reader = new BufferedInputStream(clientSocket.getInputStream());
    }

    public String read() throws IOException {
        createReaderStream();
        StringBuilder stringBuilder = new StringBuilder();
        int c;
        while((c=reader.read())!=-1){
            stringBuilder.append((char)c);
            if (c == ']') {
                break;
            }
        }
        return stringBuilder.toString();
    }

    public String getClientIpAddress(){
        return clientSocket.getInetAddress().getHostAddress();
    }

    public void close() throws IOException {
        reader.close();
        clientSocket.close();
    }
}
