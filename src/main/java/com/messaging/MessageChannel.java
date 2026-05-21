package com.messaging;

/**
 * Defines how two players communicate with each other.
 * This abstraction allows swapping the transport layer without changing
 * the Player logic — we can use queues (in-process) or sockets (cross-process).
 */
public interface MessageChannel {

    /** Send a message to the other end. */
    void send(Message message);

    /** Wait and return the next incoming message. */
    Message receive();
}
