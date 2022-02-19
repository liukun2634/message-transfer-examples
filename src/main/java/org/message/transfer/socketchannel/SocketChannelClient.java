package org.message.transfer.socketchannel;

import org.message.transfer.endpoint.Client;
import org.message.transfer.util.BufferUtil;
import org.message.transfer.util.ClientUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelClient implements Client {

    private final SocketChannel client;
    private final static int MAX_BUFFER_SIZE = 1024;
    private final ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);

    public SocketChannelClient(String address, int port) throws IOException {
        this.client = SocketChannel.open();
        this.client.connect(new InetSocketAddress(address, port));
    }


    @Override
    public void send(String message) throws IOException {
        //1. clear before put data
        buffer.clear();
        //2. put data
        buffer.put(message.getBytes());
        //3. flip before read - should set position to 0, then write
        buffer.flip();
        //4. read buffer and write to channel
        client.write(buffer);
    }

    @Override
    public String receive() throws IOException {
        while (true) {
            //1. clear before put data
            buffer.clear();
            //2. read and save into buffer
            client.read(buffer);
            //3. flip before read
            buffer.flip();

            if (buffer.hasRemaining()) {
              return BufferUtil.decode(buffer);

            }
        }
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    public static void main(String[] args) {
        try {
            Client client = new SocketChannelClient("127.0.0.1", 7890);

            ClientUtil.greeting(client);
//            ClientUtil.shutdown(client);

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
