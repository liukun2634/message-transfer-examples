package org.message.transfer.util;

import org.message.transfer.endpoint.Client;

import java.io.IOException;

public class ClientUtil {

    public static void greeting(Client client) throws IOException {
       sendAndReceive(client, "Hello");
    }


    public static void shutdown(Client client) throws IOException {
       sendAndReceive(client, "shutdown");
    }

    private static void sendAndReceive(Client client, String message) throws IOException {
        client.send(message);
        String response = client.receive();
        System.out.println("Received Server response: " + response);
    }

}
