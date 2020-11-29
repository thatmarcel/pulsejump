# Pulsejump
**Durch Puls des Spielers gesteuertes Computerspiel**

Dieses Readme ist auch verfügbar in [Englisch](https://github.com/thatmarcel/pulsejump/blob/main/README.md)

## Verwendung
Verbinde einen Pulssensor mit einem Arduino und programmiere diesen, sodass er bei jedem Puls eine Ein-Zeichen-Nachricht via Serial (9600 Baud) sendet.

Verbinde den Arduino via USB mit dem Computer, öffne das Spiel und wähle den Port, an den der Arduino angeschlossen ist.

Wenn du einem Hindernis begegnest, versuche deinen Puls zu erhöhen oder zu senken, je nachdem was das Spiel verlangt.

## Buildanweisungen
Stelle sicher, dass du die [IntelliJ IDEA](https://www.jetbrains.com/idea/) mit [JDK 15.0.1](https://www.oracle.com/java/technologies/javase-jdk15-downloads.html) verwendest, da andere Versionen Probleme verursachen können.

Führe den Gradle Task **fatJar** (in IntelliJ unter der `other` Kategorie des Gradle Menüs) aus und eine ausführbare .jar-Datei wird unter `build/libs` generiert.

## Abhängigkeiten
Dieses Projekt ist entwickelt mit [FXGL](https://github.com/AlmasB/FXGL).

Assets aus [bayat.itch.io/platform-game-assets](https://bayat.itch.io/platform-game-assets) und [arks.itch.io/dino-characters](https://arks.itch.io/dino-characters) werden verwendet.

Die Kommunikation mit dem Arduino geschieht mithilfe von [jSerialComm](https://github.com/Fazecast/jSerialComm) und einer modifizierten Version von [The Java Arduino Communication Library](https://github.com/HirdayGupta/Java-Arduino-Communication-Library).