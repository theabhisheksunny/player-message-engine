package com.messaging;

import java.io.Serializable;

/**
 * Simple value object that holds a message being passed between players.
 * Carries the sender's name and the actual text content.
 * Implements Serializable so it can be sent over sockets between processes.
 */
public record Message(String senderName, String content) implements Serializable {
}
