# About this project

The "Nim Game" is a game for 2 people. In this case a pile of 13 matches is given, from which both players have to draw alternately either 1, 2 or 3 matches. The player who gets the last match loses.

The Nim game has several variants. The described variant is about the misère game in one row respectively one pile.

https://en.wikipedia.org/wiki/Nim

# Playing

This game is played via REST calls against an AI. To make playing more comfortable, you could use Insomnia, Postman or similar. A matching Insomnia configuration, which is named `nim-game-insomnia-config.json`, can be found in the root folder of this project. It can be imported in Insomnia.

Please note that you need Java 14 to run the game.

# How to build a jar

You can create a jar using the gradle command: 

```gradle shadowJar``` 

This will create a .jar file in the `build/libs/` folder.

# Troubleshooting

If gradle is not recognized on your computer, make sure gradle is installed and set in your path variable. For more help see https://stackoverflow.com/a/48033901