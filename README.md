Introduction
============
Tracker is a TCP server/client that implements a simple state protocol to track the state of clients. Clients can open connections, close connections, and change their state. The server keeps track of the state of all connected clients.

The state is completely transparent to the server. It is up to clients to interprete the state. The state stored on the server can be seen as a distributed hash map. UUIDs of clients are mapped to client states.

Message Format
==============

<pre>
-+ Header
 +- Magic Number
 +- Version
 +- Type

-+ Payload
 +- Length
 +- Content
</pre>

Messages
========

Messges sent from client to server
----------------------------------

| Type | Name   | Payload	   | Response |	Description                                                                          |
| ---- | ------ | ---------------- | -------- | ------------------------------------------------------------------------------------ |
| 0    | Open   | "theState1"	   | "someId" |	Client establishes connection and tells server its state.                            |
|      |        |		   |	      | Server responds with clients UUID.                                                   |
| 1    | Close  | "someId"	   | "1"      | Client terminates connection.                                                        |
| 2    | Update | {		   | "1"      |	Describes a state update for one client.                                             |
|      |        |   "id":"someId", |	      |				                                                             |
|      |        |   "version":2,   |	      | Clients send this to the server when their state changes.                            |
|      |        |   "state":".."   |          | Servers can broadcast this to other clients to inform them about the state change.   |
|      |        | }                |          |                                                                                      |

Messages sent from server to client
-----------------------------------

Type | Name | Payload | Response | Description |
---- | ---- |-------- | -------- | ----------- |
3 | Update | { | "1" | Describes a state update for all clients. |
 | |  "version": 1, | |				
 | |   "state": [ | |
 | |    {"id":1,"version":1,"state":".."}, | | Servers can broadcast this to clients to inform them about state changes. |
 | |    {"id":2,"version":1,"state":".."}, | | Servers may want to use message type 2 instead to minimize the packet size. |
 | |    {"id":3,"version":1,"state":".."} | | | 
 | |   ] | | |
 | | } | | |


Example Packets
===============

| Magic | Version | Type            | Length   | Content | Description |
| ----- | ------- | --------------- | -------- | ------- | ----------- |
| 42	| 01	  | 00 (Open)       | 11       | 09 "theState1" | Client initiates connection with "theState1" |
| 42    | 01      | 01 (Close)	    | 08       | 07 "someId1" | Client closes connection. |
| 42    | 01      | 02 (Update One) | 19       | 02 07 "someId1" 09 "theState1" | State update for one client. |
| 42    | 01      | 03 (Update All) | 58       | 03 01 07 "someId1" 09 "theState1" | State update for all clients. |
|  |  |  |  | 02 07 "someId2" 09 "theState2" |  |
|  |  |  |  | 03 07 "someId3" 09 "theState3" |  |

