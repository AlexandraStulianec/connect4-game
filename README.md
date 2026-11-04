# connect4-game

A console-based implementation of Connect 4 written in Java, featuring a human player competing against an AI opponent powered by a **minimax** game-tree search.

## Overview

This project is a single-file Java program (`Main.java`) that lets you play Connect 4 against the computer on an **8×8 board**. You enter a column number, the program drops your token, displays the board, and then the AI computes and plays its move. The game ends when a player connects four tokens in a row (horizontally, vertically, or diagonally) or the board fills up.

The AI works by generating a game tree of all possible future moves up to a configurable depth and evaluating positions with the minimax algorithm.

## Features

- 8×8 board with gravity-based token placement
- Human vs. computer gameplay
- Win detection in all four directions (rows, columns, and both diagonals)
- Draw detection when the board is full
- Full game-tree generation down to a configurable depth
- Minimax search with alternating maximizing (AI) and minimizing (human) levels
- Console rendering of the board
- Debug helper that prints the generated search tree with node evaluations

## Requirements

- Java 8 or later (uses only the standard library)

## Build & Run

```bash
javac Main.java
java Main
```

## How to Play

1. The board is printed with column numbers `1–8` above it.
2. Enter a column number when prompted (`Enter a column (1-8):`).
3. Tokens fall to the lowest available row in that column.
4. The board uses the following symbols:

| Value | Meaning |
|-------|---------|
| `0`   | Empty cell |
| `1`   | Your token (human) |
| `2`   | Computer's token (PC) |

5. The game ends with `Human won!!`, `PC won!!`, or `The game ended in a draw!`.

Each run plays a single game.

## How the AI Works

1. **Tree generation** — Starting from the current board, the program builds a tree of all legal moves up to `MAX_DEPTH` (default `4`). Each node stores a board state and the list of children.
2. **Evaluation** — Terminal nodes are scored by a heuristic:
   - `-1000` if the human has four in a row
   - `+1000` if the PC has four in a row
   - otherwise a value derived from the board position
3. **Minimax** — The tree is searched recursively. The AI maximizes the score on its turns and minimizes on the human's turns, propagating values back up the tree.
4. **Move selection** — The root's child with the highest value is chosen and its column is played.

### Configuration

| Constant | Location | Default | Meaning |
|----------|----------|---------|---------|
| `N` | `Main` | `8` | Board size (N×N) and column count |
| `MAX_DEPTH` | `Main` | `4` | Search depth for the minimax tree |
| `EMPTY` / `HUMAN` / `PC` | `Main` | `0` / `1` / `2` | Cell state constants |

## Known Limitations

- **The evaluation function is randomized.** Non-terminal positions are scored with `Math.random() * 10`, so outside of wins and losses that fall within the search horizon, the AI's move choice is effectively arbitrary. The search currently detects and takes immediate wins and avoids immediate losses, but does not play strategically.
- The AI does not block threats that fall beyond its search depth.
- The program exits after one game; there is no rematch loop.
- The board display is plain text and aligned for readability, not for a polished UI.

## Possible Improvements

- Replace the random heuristic with a real evaluation function (e.g., counting two- and three-in-a-row threats, center control, and open lines)
- Add alpha-beta pruning to search deeper within the same time budget
- Add a rematch loop and difficulty levels
- Track and display win statistics
- Add unit tests for `playerWon`, `calculateRow`, and `transformChildNumber2Column`

## Project Structure

```
Main.java
├── Node            // Tree node: board state, children, value
└── Main            // Board logic, game loop, tree building, minimax
```
