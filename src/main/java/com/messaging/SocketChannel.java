package com.messaging;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Channel implementation for the separate-process scenario.
 * Wraps a TCP socket and uses Java object serialization to pass Message objects
 * between two JVM processes running on the same machine.
 */
public class SocketChannel implements MessageChannel {

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public SocketChannel(Socket socket) throws IOException {
        this.socket = socket;
        // Important: create output before input to prevent deadlock on both sides
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void send(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Could not send message over socket", e);
        }
    }

    @Override
    public Message receive() {
        try {
            return (Message) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Could not read message from socket", e);
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            // not much we can do here, just log it
            System.err.println("Warning: error while closing connection - " + e.getMessage());
        }
    }
}
