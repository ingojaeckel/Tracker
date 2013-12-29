#!/bin/sh
mvn exec:java -Dexec.mainClass="tracker.client.RunClient" -Dexec.args="192.168.1.104 6789 6000000"
