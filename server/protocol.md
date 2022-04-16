# Client - Server Communication Protocol
This document describes the format of messages that will be sent back and forth between starship arena clients and servers. It is organized according to the lifecycle of an arena.

## Connecting
Clients connect to an arena by connecting to the starship arena server's **gateway TCP socket**. Upon connecting, clients must immediately send the following information:
```
arenaId:            UUID (16 bytes),
reconnecting:       boolean (1 byte),
if reconnecting:
    clientId:       UUID (16 bytes)
```
The `arenaId` is the unique id of the arena that the client is attempting to join. `reconnecting` indicates to the server if the client is trying to reconnect to an arena that they already joined, and if so, the server expects the client to send a `clientId` which is their unique id which they were assigned when first joining.

If the connection was successful, the server will respond:
```
success:            boolean (1 byte),
if success:
    clientId:       UUID (16 bytes)
```
If the client receives `success == true`, then they have been successfully connected and should expect to read their `clientId` (this is provided even if the client is reconnecting, for simplicity). If the client receives `success == false`, then they can close the socket. The server will close the socket immediately after a failed connection.

### General Message Information
Unless otherwise specified, all client/server messages are always prefixed with a standard header:
```
messageType:        byte,
messageSize:        int (4 bytes)
```
The `messageType` indicates what type of message was sent, and what structure it will have. The `messageSize` indicates the size, in bytes, of the message, excluding the header data itself.

### Chat Messages
At any point while a client is connected to an arena, it may send and receive chat messages. To send a chat message, a client sends the following:
```
messageType:        1,
messageSize:        msg.length(),
msg:                String
```
Chat messages will be relayed by the arena to all clients using the following format:
```
messageType:        2,
messageSize:        16 + 8 + msg.length(),
clientId:           UUID (16 bytes),
timestamp:          long (8 bytes),
msg:                String
```

### Error Messages
Generally, error messages take the following form:
```
messageType:        0,
messageSize:        msg.length(),
msg:                String
```
They can be sent by the server as a response to any client message that prompted that error.

## Staging
During the staging part of the arena lifecycle, clients can of course chat, but they can also send commands to configure their experience prior to the start of the game. This includes the following:
- Setting their username
- Choosing to be a spectator or a player (all clients are spectators by default)
- Choosing a team to be on (if choice is allowed, and if teams exist)
- Choosing a spacecraft model (if choice is allowed)
- Fetch arena configuration settings

### Client Config Message
This message is sent by the client to indicate that they'd like to change their current configuration.
```
messageType:        10,
messageSize:        see below,
role:               byte (0 = spectator, 1 - 128 = player or team number)
```
