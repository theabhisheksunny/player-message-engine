package com.messaging;

/**
 * Entry point for running both players in the same JVM process.
 *
 * Responsibilities:
 * - Creates two players and the channels connecting them.
 * - Runs each player in its own thread.
 * - Waits for both threads to complete before exiting (graceful shutdown).
 */
public class SameProcessMain {

    public static void main(String[] args) {
        System.out.println("Starting same-process mode (PID: " + ProcessHandle.current().pid() + ")\n");

        BlockingQueueChannel channel1to2 = new BlockingQueueChannel();
        BlockingQueueChannel channel2to1 = new BlockingQueueChannel();

        // Player1 is the initiator — sends first, receives on channel2to1, sends on channel1to2
        Player player1 = new Player("Player1", channel2to1, channel1to2, true, 10);
        // Player2 is the responder — receives on channel1to2, sends on channel2to1
        Player player2 = new Player("Player2", channel1to2, channel2to1, false, 10);

        Thread t1 = new Thread(player1, "player1-thread");
        Thread t2 = new Thread(player2, "player2-thread");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nProgram finished gracefully.");
    }
}
