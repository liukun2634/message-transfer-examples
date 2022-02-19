package org.message.transfer.socket;

import org.message.transfer.endpoint.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketServer implements Server {
    private final ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;


    public SocketServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }


    @Override
    public void start() throws IOException {
        while (true) {
            acceptConnection();

            String content = in.readLine();
            //End when receive 'shutdown' message
            if ("shutdown".equalsIgnoreCase(content)) {
                out.println("Bye client");
                closeConnection();
                return;
            } else {
                System.out.println("Received Client request: " + content);
                out.println("Hello client");
                closeConnection();
            }
        }
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }

    private void acceptConnection() throws IOException {
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    }

    private void closeConnection() throws IOException {
        clientSocket.close();
        out.close();
        in.close();
    }

    public static void main(String[] args) {
        try {
            Server server = new SocketServer(7890);
            server.start();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
