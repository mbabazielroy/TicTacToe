
class TicTacToe {
    private char[][] board; // the 3 x 3 board
    private char currentPlayer; // the current player ('X' or 'O')

    public TicTacToe(){
        board = new char[3][3];
        currentPlayer = 'X';//Player X starts first
        initializeBoard();
    }

    //Initializing the Board with empty values
    public void initializeBoard(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                board[i][j] ='-';
            }
        }
    }

    //Displaying the current board state
    public void displayBoard(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    //Return true if the move is valid (Cell is empty)
    public boolean isValidMove(int row, int col){
        return board[row][col] == '-';
    }

    //Place players's move on the board
    public void makeMove(int row, int col){
        if(isValidMove(row, col)){
            board[row][col] = currentPlayer;
            switchPlayer();
        }else{
            System.out.println("Invalid move. Choose an empty cell.");
        }
    }

    //switch to other player
    private void switchPlayer() {
      currentPlayer = (currentPlayer == 'X')?'O':'X'; // What does this line mean ????//
    }

    //Check if a player has won the game
    public boolean checkWin(){
        // Check rows, columns and diagonals
        for(int i=0; i<3; i++){
            if(checkRow(i) || checkColumn(i)) return true;
        }
        return checkDiagonals();
    }

    private boolean checkDiagonals() {
        return ((board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
                (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer));
    }

    private boolean checkColumn(int col) {
        return (board[0][col] == currentPlayer &&
                board[1][col] == currentPlayer &&
                board[2][col] == currentPlayer);
    }

    private boolean checkRow(int row) {
        return (board[row][0] == currentPlayer &&
        board[row][1] == currentPlayer &&
        board[row][2] == currentPlayer);
    }

    public char getCurrentPlayer(){
        return currentPlayer;
    }

    // Check if the board is full ( for a draw)
    public boolean isBoardFull(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(board[i][j] == '-') return false;
            }
        }
        return true;
    }

}

