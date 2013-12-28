#!/bin/sh
mvn exec:java -Dexec.mainClass="tracker.server.RunServer" -Dexec.args="6789"
