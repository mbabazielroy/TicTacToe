# TicTacToe

Desktop Swing Tic-Tac-Toe with AI levels, replay, export/import, accessibility, and keyboard controls.

## Run
```
javac TicTacToe.java TicTacToeGUI.java TicTacToeTest.java
java TicTacToeGUI
```

## Features
- AI levels: Random, Heuristic, Perfect (minimax); AI vs AI demo mode; best-move hint toggle.
- Replay: auto or step with slider; move numbering shown only during replay.
- Persistence: settings, stats, and last game stored in `~/.tictactoe.properties`; export/import game snapshots.
- UX: high-contrast and compact modes, readable font toggle, hover/pressed cues, toasts, move timestamps, status bar, move history, stats with longest streak.
- Controls: keyboard arrows + Enter/Space, Ctrl+Z/Y for undo/redo, `?` for shortcut help; mnemonics on buttons.
- Accessibility: focus outlines, larger targets in compact mode, readable font option.

## Tests
```
java TicTacToeTest
```

## Packaging
To build a runnable jar:
```
javac TicTacToe.java TicTacToeGUI.java
jar cfe TicTacToe.jar TicTacToeGUI *.class
java -jar TicTacToe.jar
```
