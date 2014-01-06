[![Build Status](https://travis-ci.org/jaeckel/Tracker.png?branch=master)](https://travis-ci.org/jaeckel/Tracker)

Introduction
============
Tracker is a TCP server/client that implements a simple state protocol to track the state of clients. Clients can open connections, close connections, and change their state. The server keeps track of the state of all connected clients.

Message Format
==============

<pre>
-+ Header
 +- Magic Number
 +- Type

-+ Payload (Optional)
</pre>

Messages
========

Clients may sent any of the following messages to the server.

| Type | Name | Payload (example) | Response | Description |
| ---- | ---- | ----------------- | -------- | ----------- |
| 0 | Open | "theState1" | 1 |	Client establishes connection and tells server its state. |
| 1 | Close | None | 1 | Client terminates connection. Clients are expected to send this before disconnecting from the server. |
| 2 | Update | "updatedState" | None | Describes a state update for one client. |
| 3 | Get | None | "{client1=state1, client2=state2}" | Requests the most recent state. Server replies with a flattened map that represents the entire state. Client identifiers are mapped to state descriptions. |
| | | | | |

Byte representation of state
----------------------------

The state is completely transparent to the server and it is up to clients to interpret it. The state stored on the server can be seen as a distributed hash map. Client identifiers are are mapped to strings describing the states.

The state is transmitted in two parts: (1) 8 bytes for the length of the state, (2) followed by the bytes representing the actual string. The state "abc" will be transmitted as 03 97 98 99 (decimal) or 03 61 62 63 (hex).

Example Packets
===============

Bytes in sample packets are shown in decimal.

| Magic | Type | Payload | Response | Description |
| ----- | ---- | ------ | --------- | ----------- |
| 42    | 0    | 03 97 98 99 | 1 | Initiates connection with state "abc". |
| 42    | 1    | None | 1 | Closes the connection. |
| 42    | 2    | 04 03 97 98 99 | None | Updates the state of the connected client to "abc" |
| 42    | 3    | None | 15 123 101 61 102  44  32  99  61   100  44  32  97  61  98 125 | Requests the overall state of all connected clients. In this case, the server responds with "{a=b, c=d, e=f}". |

Resources
=========
* Letting a JavaScript client talk to Node.js http://stackoverflow.com/questions/7340475/client-side-tcp-with-node-js-socket-io
 * Maybe this can be used to let a JavaScript client talk with the TCP server with Node.js as a proxy?
* Node.js for Raspberry Pi https://gist.github.com/adammw/3245130
