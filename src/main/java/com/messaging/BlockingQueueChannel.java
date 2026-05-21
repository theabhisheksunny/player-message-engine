package com.messaging;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Channel implementation for the same-process scenario.
 * Uses a LinkedBlockingQueue internally — thread-safe, no explicit locking needed.
 * One player puts messages in, the other takes them out.
 */
public class BlockingQueueChannel implements MessageChannel {

    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    @Override
    public void send(Message message) {
        queue.offer(message);
    }

    @Override
    public Message receive() {
        try {
            return queue.take(); // blocks until something is available
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for a message", e);
        }
    }
}
