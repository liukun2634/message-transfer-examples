package org.message.transfer.socket;

import org.message.transfer.endpoint.Client;
import org.message.transfer.util.ClientUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient implements Client {
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;


    public SocketClient(String address, int port) throws IOException {
        clientSocket = new Socket(address, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void send(String message) throws IOException {
        out.println(message);
    }

    @Override
    public String receive() throws IOException {
        return in.readLine();
    }


    @Override
    public void close() throws IOException {
        clientSocket.close();
        in.close();
        out.close();
    }

    public static void main(String[] args) {
        try {
            Client client = new SocketClient("127.0.0.1", 7890);

            ClientUtil.greeting(client);

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
