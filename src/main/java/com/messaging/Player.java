package com.messaging;

/**
 * Represents a player that exchanges messages with another player.
 *
 * Responsibilities:
 * - If this player is the initiator, it sends the first message to kick off the exchange.
 * - When a message is received, this player replies with a new message containing
 *   the received text concatenated with its own sent-message counter.
 * - The initiator stops after it has sent 10 messages and received 10 messages back.
 * - The responder stops after it has received 10 messages and replied to all of them.
 *
 * This class is transport-agnostic — it works with any MessageChannel implementation
 * (in-process queues or cross-process sockets).
 */
public class Player implements Runnable {

    private final String name;
    private final MessageChannel inChannel;
    private final MessageChannel outChannel;
    private final boolean isInitiator;
    private final int maxMessages;

    private int sent = 0;
    private int received = 0;

    public Player(String name, MessageChannel inChannel, MessageChannel outChannel,
                  boolean isInitiator, int maxMessages) {
        this.name = name;
        this.inChannel = inChannel;
        this.outChannel = outChannel;
        this.isInitiator = isInitiator;
        this.maxMessages = maxMessages;
    }

    @Override
    public void run() {
        // Initiator kicks off the conversation with the first message
        if (isInitiator) {
            doSend("Hello");
        }

        // Main loop: receive a message, then reply (if we haven't hit the limit)
        while (received < maxMessages) {
            Message msg = inChannel.receive();
            received++;
            System.out.println(name + " received message " + received + ": \"" + msg.content() + "\"");

            if (sent < maxMessages) {
                // Reply = received content + this player's send counter
                String reply = msg.content() + " " + (sent + 1);
                doSend(reply);
            }
        }

        System.out.println(name + " finished. Sent: " + sent + ", Received: " + received);
    }

    private void doSend(String text) {
        sent++;
        outChannel.send(new Message(name, text));
        System.out.println(name + " sent message " + sent + ": \"" + text + "\"");
    }
}
