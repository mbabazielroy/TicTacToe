public class TicTacToeTest {
    public static void main(String[] args) {
        testWinDetection();
        testDrawDetection();
        testInvalidMove();
        testUndoRedo();
        testComputerBlocks();
        testApplyMoves();
        System.out.println("All TicTacToe tests passed.");
    }

    private static void testWinDetection() {
        TicTacToe game = new TicTacToe();
        game.makeMove(0, 0); // X
        game.makeMove(1, 0); // O
        game.makeMove(0, 1); // X
        game.makeMove(1, 1); // O
        TicTacToe.MoveResult result = game.makeMove(0, 2); // X wins
        assertTrue(result == TicTacToe.MoveResult.WIN, "Expected win result");
    }

    private static void testDrawDetection() {
        TicTacToe game = new TicTacToe();
        // X O X
        // X X O
        // O X O
        game.makeMove(0, 0); // X
        game.makeMove(0, 1); // O
        game.makeMove(0, 2); // X
        game.makeMove(1, 2); // O
        game.makeMove(1, 0); // X
        game.makeMove(2, 0); // O
        game.makeMove(1, 1); // X
        game.makeMove(2, 2); // O
        TicTacToe.MoveResult result = game.makeMove(2, 1); // X for draw
        assertTrue(result == TicTacToe.MoveResult.DRAW, "Expected draw result");
    }

    private static void testInvalidMove() {
        TicTacToe game = new TicTacToe();
        game.makeMove(0, 0); // X
        TicTacToe.MoveResult result = game.makeMove(0, 0); // attempt duplicate
        assertTrue(result == TicTacToe.MoveResult.INVALID, "Expected invalid move on occupied cell");
    }

    private static void testUndoRedo() {
        TicTacToe game = new TicTacToe();
        game.makeMove(0, 0); // X
        game.makeMove(1, 1); // O
        boolean undone = game.undo();
        assertTrue(undone, "Undo should succeed");
        TicTacToe.MoveResult redoResult = game.redo();
        assertTrue(redoResult == TicTacToe.MoveResult.SUCCESS, "Redo should succeed");
    }

    private static void testComputerBlocks() {
        TicTacToe game = new TicTacToe();
        game.makeMove(0, 0); // X
        game.makeMove(1, 1); // O
        game.makeMove(0, 1); // X threatens row 0
        int[] aiMove = game.findBestMove('O', TicTacToe.AiLevel.HEURISTIC);
        assertTrue(aiMove != null && aiMove[0] == 0 && aiMove[1] == 2, "Computer should block winning move");
    }

    private static void testApplyMoves() {
        TicTacToe game = new TicTacToe();
        java.util.List<TicTacToe.Move> moves = new java.util.ArrayList<>();
        moves.add(new TicTacToe.Move(0, 0, 'X'));
        moves.add(new TicTacToe.Move(1, 1, 'O'));
        moves.add(new TicTacToe.Move(0, 1, 'X'));
        game.applyMoves(moves, 'X');
        char[][] board = game.getBoardSnapshot();
        assertTrue(board[0][0] == 'X' && board[1][1] == 'O' && board[0][1] == 'X', "applyMoves should rebuild state");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
