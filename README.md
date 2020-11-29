# Pulsejump
**Platformer game controlled by the player's pulse**

This readme is also available in [German](https://github.com/thatmarcel/pulsejump/blob/main/README_DE.md)

## Usage
Connect a pulse sensor to an Arduino and make it send a one character message over serial (9600 baud) when a heartbeat is detected.

Connect the Arduino to the computer via USB, open the game and choose the port which the Arduino is connected to.

When you encounter an obstacle in the game, try to increase or lower your pulse, depending on what the game tells you.

## Build instructions
First, make sure that you are using the [IntelliJ IDEA](https://www.jetbrains.com/idea/) with [JDK 15.0.1](https://www.oracle.com/java/technologies/javase-jdk15-downloads.html) as other versions may cause issues.

Run the Gradle task **fatJar** (in IntelliJ found in the `other` category of the Gradle menu) and an executable jar will be generated in `build/libs`.

## Dependencies
This project is built with [FXGL](https://github.com/AlmasB/FXGL).

The game uses assets from [bayat.itch.io/platform-game-assets](https://bayat.itch.io/platform-game-assets) and [arks.itch.io/dino-characters](https://arks.itch.io/dino-characters).

Communication with the Arduino that is sending the pulse is done through [jSerialComm](https://github.com/Fazecast/jSerialComm) and a modified version of [The Java Arduino Communication Library](https://github.com/HirdayGupta/Java-Arduino-Communication-Library).