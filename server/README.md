# Starship Arena Server

The server is a program that runs the physics of the arena, and that clients can connect to. Each server specifically has the capability to host one or more arenas simultaneously. Clients may query the server for metadata, like which arenas are available, and their current statuses.

## Arena Lifecycle

Within the context of each arena, clients may connect during the so-called "staging" phase before the battle commences. Once all clients are assembled and the arena's settings are established, the arena transitions to the "battle" phase, where players (or AI) will control their spacecraft to achieve the configured objective. Once battle is deemed to be over, the arena transitions to the "analysis" stage, where the server provides detailed analytics and statistics about the battle to players. Spectators may join at any point, as long as the arena is open.

### Staging
This is the first stage that all arenas start in. It offers the following functionality:
- Players can connect
- Spectators can connect
- Configure arena settings for battle

### Battle
The main stage for the arena, in which the all "players" (AI or human players) control their respective spacecraft to achieve an objective. It offers the following functionality:
- Battle physics simulation and real-time UDP data updates sent to all connected clients
- Spectators can connect
- Registered players can re-connect if their connection dies for some reason

### Analysis
After the battle has ended (due to time constraints, objective achieved, etc.), the arena enters the final analysis stage for a set period of time before closing. During the analysis stage, the arena will provide statistics and analysis for each player. It offers the following functionality:
- Player statistics and analysis sent to all players
- All clients can disconnect
