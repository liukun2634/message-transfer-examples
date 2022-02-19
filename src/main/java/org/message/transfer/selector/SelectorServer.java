package org.message.transfer.selector;

import org.message.transfer.endpoint.Server;
import org.message.transfer.util.BufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class SelectorServer implements Server {

    private final Selector selector;
    private final ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private final ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    public SelectorServer() throws IOException {
        this.selector = Selector.open();
    }

    public void register(SocketChannel channel) throws IOException {
        //Start from Accept
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void register(int port) throws IOException {
        //Start from Accept
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(port));
        //Selector only support non-blocking channel
        channel.configureBlocking(false);
        //Register on current selector
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void start() throws IOException {
        while (true) {

            selector.select(); //select keys are ready for IO
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();

                it.remove();

                if (key.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);
                    //register client channel
                    clientChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    readBuffer.clear();
                    clientChannel.read(readBuffer);
                    readBuffer.flip();
                    String request = BufferUtil.decode(readBuffer);
                    System.out.println("Server received : " + request);
                    if ("shutdown".equalsIgnoreCase(request)) {
                        String response = "Bye, server is closing";
                        send(clientChannel, response);
                        return;
                    }
                    key.interestOps(SelectionKey.OP_WRITE);
                } else if (key.isWritable()) {
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    String response = "Received Message";
                    send(clientChannel, response);
                    key.cancel();
                }
            }
        }
    }

    private void send(SocketChannel clientChannel, String message) throws IOException {
        writeBuffer.clear();
        writeBuffer.put(message.getBytes());
        writeBuffer.flip();
        clientChannel.write(writeBuffer);
    }

    @Override
    public void close() throws IOException {
        selector.close();
    }

    public static void main(String[] args) throws IOException {
        SelectorServer server = new SelectorServer();
        server.register(7890);
        server.start();
        server.close();
    }
}
