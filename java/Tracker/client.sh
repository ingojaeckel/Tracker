#!/bin/sh
mvn exec:java -Dexec.mainClass="tracker.client.RunClient" -Dexec.args="127.0.0.1 6789 60000"
