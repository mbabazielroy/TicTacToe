
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Core game logic for Tic-Tac-Toe. Board cells use '-', 'X', or 'O'.
 */
class TicTacToe {
    enum MoveResult { INVALID, SUCCESS, WIN, DRAW }
    enum AiLevel { RANDOM, HEURISTIC, PERFECT }

    static class Move {
        final int row;
        final int col;
        final char player;
        final long timestamp;

        Move(int row, int col, char player) {
            this(row, col, player, System.currentTimeMillis());
        }

        Move(int row, int col, char player, long timestamp) {
            this.row = row;
            this.col = col;
            this.player = player;
            this.timestamp = timestamp;
        }
    }

    private final char[][] board; // the 3 x 3 board
    private char currentPlayer; // the current player ('X' or 'O')
    private boolean gameOver; // stops moves after a win/draw
    private final Deque<Move> moveHistory = new ArrayDeque<>();
    private final Deque<Move> redoStack = new ArrayDeque<>();
    private final List<int[]> winningCells = new ArrayList<>();
    private final Random random = new Random();

    public TicTacToe() {
        board = new char[3][3];
        initializeBoard();
    }

    // Initializing the board with empty values and resetting state
    public void initializeBoard() {
        initializeBoard('X');
    }

    public void initializeBoard(char startingPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
        currentPlayer = startingPlayer;
        gameOver = false;
        moveHistory.clear();
        redoStack.clear();
        winningCells.clear();
    }

    // Displaying the current board state
    public void displayBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Return true if the move is valid (cell is empty and inside bounds)
    public boolean isValidMove(int row, int col) {
        return !gameOver && inBounds(row, col) && board[row][col] == '-';
    }

    // Place player's move on the board and report outcome
    public MoveResult makeMove(int row, int col) {
        if (!isValidMove(row, col)) {
            return MoveResult.INVALID;
        }

        board[row][col] = currentPlayer;
        Move move = new Move(row, col, currentPlayer);
        moveHistory.addLast(move);
        redoStack.clear();

        if (hasPlayerWon(currentPlayer)) {
            gameOver = true;
            return MoveResult.WIN;
        }

        if (isBoardFull()) {
            gameOver = true;
            return MoveResult.DRAW;
        }

        switchPlayer();
        return MoveResult.SUCCESS;
    }

    // Undo last move, if any
    public boolean undo() {
        if (moveHistory.isEmpty()) return false;
        Move last = moveHistory.pollLast();
        board[last.row][last.col] = '-';
        currentPlayer = last.player;
        gameOver = false;
        winningCells.clear();
        redoStack.push(last);
        return true;
    }

    // Redo most recently undone move, if any
    public MoveResult redo() {
        if (redoStack.isEmpty()) return MoveResult.INVALID;
        Move redoMove = redoStack.pop();
        if (!inBounds(redoMove.row, redoMove.col) || board[redoMove.row][redoMove.col] != '-') {
            return MoveResult.INVALID;
        }

        board[redoMove.row][redoMove.col] = redoMove.player;
        moveHistory.addLast(redoMove);

        if (hasPlayerWon(redoMove.player)) {
            gameOver = true;
            currentPlayer = redoMove.player;
            return MoveResult.WIN;
        }

        if (isBoardFull()) {
            gameOver = true;
            currentPlayer = redoMove.player;
            return MoveResult.DRAW;
        }

        currentPlayer = (redoMove.player == 'X') ? 'O' : 'X';
        return MoveResult.SUCCESS;
    }

    // switch to other player
    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public char getStartingPlayer() {
        if (moveHistory.isEmpty()) {
            return currentPlayer;
        }
        return moveHistory.peekFirst().player;
    }

    // Check if a player has won the game
    private boolean hasPlayerWon(char player) {
        winningCells.clear();

        for (int i = 0; i < 3; i++) {
            if (checkRow(i, player)) {
                winningCells.add(new int[]{i, 0});
                winningCells.add(new int[]{i, 1});
                winningCells.add(new int[]{i, 2});
                return true;
            }
            if (checkColumn(i, player)) {
                winningCells.add(new int[]{0, i});
                winningCells.add(new int[]{1, i});
                winningCells.add(new int[]{2, i});
                return true;
            }
        }
        if (checkMainDiagonal(player)) {
            winningCells.add(new int[]{0, 0});
            winningCells.add(new int[]{1, 1});
            winningCells.add(new int[]{2, 2});
            return true;
        }
        if (checkAntiDiagonal(player)) {
            winningCells.add(new int[]{0, 2});
            winningCells.add(new int[]{1, 1});
            winningCells.add(new int[]{2, 0});
            return true;
        }
        return false;
    }

    private boolean checkMainDiagonal(char player) {
        return board[0][0] == player && board[1][1] == player && board[2][2] == player;
    }

    private boolean checkAntiDiagonal(char player) {
        return board[0][2] == player && board[1][1] == player && board[2][0] == player;
    }

    private boolean checkColumn(int col, char player) {
        return board[0][col] == player && board[1][col] == player && board[2][col] == player;
    }

    // Find the first empty cell (row, col) or null if board full
    public int[] findFirstAvailable() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private boolean checkRow(int row, char player) {
        return board[row][0] == player && board[row][1] == player && board[row][2] == player;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    // Check if the board is full (for a draw)
    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') return false;
            }
        }
        return true;
    }

    // Defensive copy of the board for read-only access
    public char[][] getBoardSnapshot() {
        char[][] copy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 3);
        }
        return copy;
    }

    public List<int[]> getWinningCells() {
        List<int[]> copy = new ArrayList<>();
        for (int[] cell : winningCells) {
            copy.add(new int[]{cell[0], cell[1]});
        }
        return copy;
    }

    public List<Move> getMoveHistorySnapshot() {
        return new ArrayList<>(moveHistory);
    }

    // AI with difficulty levels
    public int[] findBestMove(char aiPlayer, AiLevel level) {
        switch (level) {
            case RANDOM:
                return findRandomMove();
            case PERFECT:
                return findPerfectMove(aiPlayer);
            case HEURISTIC:
            default:
                return findHeuristicMove(aiPlayer);
        }
    }

    // Simple AI: win if possible, otherwise block, take center, corner, then side
    private int[] findHeuristicMove(char aiPlayer) {
        if (gameOver) return null;
        char opponent = (aiPlayer == 'X') ? 'O' : 'X';

        // Try winning move
        int[] winningMove = findImmediateWin(aiPlayer);
        if (winningMove != null) return winningMove;

        // Try blocking move
        int[] blockMove = findImmediateWin(opponent);
        if (blockMove != null) return blockMove;

        // Center
        if (board[1][1] == '-') return new int[]{1, 1};

        // Corners
        int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        for (int[] move : corners) {
            if (board[move[0]][move[1]] == '-') return move;
        }

        // Sides
        int[][] sides = {{0, 1}, {1, 0}, {1, 2}, {2, 1}};
        for (int[] move : sides) {
            if (board[move[0]][move[1]] == '-') return move;
        }
        return null;
    }

    private int[] findImmediateWin(char player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    board[i][j] = player;
                    boolean win = hasPlayerWon(player);
                    board[i][j] = '-';
                    winningCells.clear();
                    if (win) return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private int[] findRandomMove() {
        List<int[]> available = getAvailableMoves();
        if (available.isEmpty()) return null;
        return available.get(random.nextInt(available.size()));
    }

    private int[] findPerfectMove(char aiPlayer) {
        if (gameOver) return null;
        char opponent = (aiPlayer == 'X') ? 'O' : 'X';
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;
        char[][] snapshot = getBoardSnapshot();

        for (int[] move : getAvailableMoves(snapshot)) {
            snapshot[move[0]][move[1]] = aiPlayer;
            int score = minimax(snapshot, 0, false, aiPlayer, opponent);
            snapshot[move[0]][move[1]] = '-';
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int minimax(char[][] state, int depth, boolean maximizing, char aiPlayer, char opponent) {
        char winner = evaluateWinner(state);
        if (winner == aiPlayer) return 10 - depth;
        if (winner == opponent) return depth - 10;
        if (isFull(state)) return 0;

        if (maximizing) {
            int best = Integer.MIN_VALUE;
            for (int[] move : getAvailableMoves(state)) {
                state[move[0]][move[1]] = aiPlayer;
                best = Math.max(best, minimax(state, depth + 1, false, aiPlayer, opponent));
                state[move[0]][move[1]] = '-';
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int[] move : getAvailableMoves(state)) {
                state[move[0]][move[1]] = opponent;
                best = Math.min(best, minimax(state, depth + 1, true, aiPlayer, opponent));
                state[move[0]][move[1]] = '-';
            }
            return best;
        }
    }

    private List<int[]> getAvailableMoves() {
        return getAvailableMoves(board);
    }

    private List<int[]> getAvailableMoves(char[][] state) {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == '-') {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }

    private char evaluateWinner(char[][] state) {
        for (int i = 0; i < 3; i++) {
            if (state[i][0] != '-' && state[i][0] == state[i][1] && state[i][1] == state[i][2]) {
                return state[i][0];
            }
            if (state[0][i] != '-' && state[0][i] == state[1][i] && state[1][i] == state[2][i]) {
                return state[0][i];
            }
        }
        if (state[0][0] != '-' && state[0][0] == state[1][1] && state[1][1] == state[2][2]) return state[0][0];
        if (state[0][2] != '-' && state[0][2] == state[1][1] && state[1][1] == state[2][0]) return state[0][2];
        return '-';
    }

    private boolean isFull(char[][] state) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == '-') return false;
            }
        }
        return true;
    }

    // Apply a full move list to rebuild state (used for persistence/replay)
    public void applyMoves(List<Move> moves, char startPlayer) {
        initializeBoard(startPlayer);
        for (Move move : moves) {
            if (gameOver) break;
            if (isValidMove(move.row, move.col) && move.player == currentPlayer) {
                board[move.row][move.col] = currentPlayer;
                moveHistory.addLast(new Move(move.row, move.col, move.player, move.timestamp));
                redoStack.clear();
                if (hasPlayerWon(currentPlayer)) {
                    gameOver = true;
                    break;
                }
                if (isBoardFull()) {
                    gameOver = true;
                    break;
                }
                switchPlayer();
            }
        }
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3;
    }
}

