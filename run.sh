#!/bin/bash
#
# Builds and runs the player-message-engine.
#
# Usage:
#   ./run.sh same-process       (both players in one JVM)
#   ./run.sh separate-process   (each player in its own JVM)
#

set -e

mvn clean compile -q

CP="target/classes"
MODE=${1:-same-process}

case "$MODE" in
    same-process)
        java -cp "$CP" com.messaging.SameProcessMain
        ;;
    separate-process)
        java -cp "$CP" com.messaging.SeparateProcessMain server &
        sleep 1
        java -cp "$CP" com.messaging.SeparateProcessMain client
        wait
        ;;
    *)
        echo "Usage: $0 {same-process|separate-process}"
        exit 1
        ;;
esac
