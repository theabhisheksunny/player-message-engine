package com.messaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Entry point for running each player in a separate JVM process (different PIDs).
 *
 * Responsibilities:
 * - Accepts a role argument ("server" for Player2, "client" for Player1).
 * - Sets up TCP socket communication between the two processes.
 * - The server (Player2) listens and waits for the client to connect.
 * - The client (Player1/initiator) connects and starts the message exchange.
 * - Both processes terminate gracefully after the exchange completes.
 */
public class SeparateProcessMain {

    private static final int PORT = 9876;
    private static final int MAX_MESSAGES = 10;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java SeparateProcessMain <server|client>");
            System.exit(1);
        }

        String role = args[0].toLowerCase();
        if (role.equals("server")) {
            startServer();
        } else if (role.equals("client")) {
            startClient();
        } else {
            System.err.println("Invalid argument. Use 'server' or 'client'.");
            System.exit(1);
        }
    }

    private static void startServer() {
        System.out.println("Player2 (server) | PID: " + ProcessHandle.current().pid());
        System.out.println("Waiting for connection on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Socket connection = serverSocket.accept();
            System.out.println("Connected.\n");

            SocketChannel channel = new SocketChannel(connection);
            Player player2 = new Player("Player2", channel, channel, false, MAX_MESSAGES);
            player2.run();
            channel.close();

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("\nPlayer2 process finished gracefully.");
    }

    private static void startClient() {
        System.out.println("Player1 (client/initiator) | PID: " + ProcessHandle.current().pid());

        try {
            Socket connection = new Socket("localhost", PORT);
            System.out.println("Connected to Player2.\n");

            SocketChannel channel = new SocketChannel(connection);
            Player player1 = new Player("Player1", channel, channel, true, MAX_MESSAGES);
            player1.run();
            channel.close();

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("\nPlayer1 process finished gracefully.");
    }
}
