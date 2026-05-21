# Player Message Engine

Two players exchange messages following a simple protocol: each reply contains the received text concatenated with the sender's message counter. The initiator sends the first message, then both players automatically exchange messages until 10 round-trips complete. Program stops gracefully.

## Prerequisites

- Java 17+
- Maven 3.6+

## Build

```
cd player-message-engine
mvn clean compile
```

## Run — Same Process Mode

Both players run as threads in a single JVM.

```
./run.sh same-process
```

Or directly:
```
java -cp target/classes com.messaging.SameProcessMain
```

## Run — Separate Process Mode

Each player runs in its own JVM (different PIDs).

### Using the script:
```
./run.sh separate-process
```

### Manually with two terminals:

Terminal 1 (start first):
```
java -cp target/classes com.messaging.SeparateProcessMain server
```

Terminal 2:
```
java -cp target/classes com.messaging.SeparateProcessMain client
```

If the server might not be ready yet, use the `--retry` flag (retries up to 3 times with 2s delay):
```
java -cp target/classes com.messaging.SeparateProcessMain client --retry
```

Both terminals show their player's activity. After 10 exchanges both processes exit.

## Design

```
Message.java              - value object for message data
MessageChannel.java       - interface for send/receive
BlockingQueueChannel.java - in-memory channel (same process)
SocketChannel.java        - TCP channel (separate processes)
Player.java               - player logic, transport-agnostic
SameProcessMain.java      - wires up the same-process scenario
SeparateProcessMain.java  - wires up the separate-process scenario
```

## Notes

- Port 9876 is used for the socket-based mode
- Shell script needs execute permission: `chmod +x run.sh`
