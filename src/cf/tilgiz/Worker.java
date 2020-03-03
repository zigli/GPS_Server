package cf.tilgiz;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
//        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        reader = new BufferedInputStream(clientSocket.getInputStream());
    }

    public String read() throws IOException {
        createReaderStream();
        StringBuffer stringBuffer = new StringBuffer();
        int c;
        while((c=reader.read())!=-1){
            stringBuffer.append((char)c);
            if (c == ']') {
                break;
            }
        }
        return stringBuffer.toString();
    }

    public String getClientIpAddress(){
        return clientSocket.getInetAddress().getHostAddress();
    }

    public void close() throws IOException {
        reader.close();
        clientSocket.close();
    }
}
