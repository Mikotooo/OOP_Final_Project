## Getting Started

This is the final Project for our Object Oriented Programming. 

And here we will be showcasing a "Tetris Game" Inspired by the Tetris Movie. 


A Classic Retro Gameboy UI Will be Implemented

## Folder Structure

The workspace contains 3 File Structure that "Screams OOP", on where the src contains:

- `ui`: package manages all user interface and rendering responsibilities using Java Swing. It visualizes the game state and forwards user input to the core logic without directly modifying game rules.
- `Core`: package contains the main game logic and data models of the Tetris application. It is responsible for enforcing the game rules, managing the board state, handling tetromino behavior, tracking scoring and levels, and coordinating overall game flow.
- `states`: package implements the State design pattern, allowing the game to change behavior dynamically depending on its current state (running, paused, or game over).

## File Structure
src/
  - tetris/
    - App.java
    - core/
      - Board.java
      - Game.java
      - RandomBag.java
      - ScoreManager.java
      - ShapeType.java
      - Tetromino.java
    states/
      - GameOverState.java
      - GameState.java
      - PausedState.java
      - RunningState.java
    ui/
      - GamePanel.java
      - GameWindow.java      
     



## Key Classes 

Key Classes (what each one does)

Game
    - main controller: current piece, next piece, tick speed, state
    - methods: tick(), moveLeft(), moveRight(), softDrop(), hardDrop(), rotate()

Board
    - int[][] grid (or Cell[][])
    - methods: canPlace(piece), lock(piece), clearLines()

Tetromino
    - holds current shape blocks + rotation
    - method: getBlocks() returns 4 points
    - method: rotateCW()

RandomBag
    - implements “7-bag” randomizer (classic modern standard, but still acceptable)
    - if you want super old-school, use pure random instead—either is okay, just explain.

ScoreManager
    - score, level, lines cleared
    - method: addClearedLines(n)

GamePanel (Swing UI)
    - draws board + active piece using paintComponent(Graphics g)
    - calls game.tick() on timer

Controls (simple, classic)
← / → move
↓ soft drop
Space hard drop
↑ rotate
P pause



## Collaborators 
  - Jan Stanlee Achumbre 
  - Hiroshi Curimatmat 
  - Athena Jamie Uy 
  - Aisha Malinao
