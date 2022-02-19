package org.message.transfer.socketchannel;

import org.message.transfer.endpoint.Client;
import org.message.transfer.endpoint.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SocketChannelServer implements Server {
    private final ServerSocketChannel serverChannel;
    private final static int MAX_BUFFER_SIZE = 1024;
    private final ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);

    public SocketChannelServer(int port) throws IOException {
        this(port, false);
    }

    public SocketChannelServer(int port, boolean block) throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(block);
    }


    @Override
    public void start() throws IOException {
        while (true) {
            SocketChannel clientChannel = serverChannel.accept();
            if (clientChannel != null) {
                buffer.clear();
                clientChannel.read(buffer);
                buffer.flip();

                if (buffer.hasRemaining()) {
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    String message = new String(bytes);
                    System.out.println(message);
                    if ("shutdown".equalsIgnoreCase(message)) {
                        send(clientChannel, "Bye client");
                        return;
                    } else {
                        send(clientChannel, "Hello client");
                    }
                }
            }
        }
    }

    private void send(SocketChannel clientChannel, String message) throws IOException {
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        clientChannel.write(buffer);
    }


    @Override
    public void close() throws IOException {
        serverChannel.close();
    }

    public static void main(String[] args) throws IOException {
        Server server = new SocketChannelServer(7890);
        server.start();
        server.close();
    }
}
