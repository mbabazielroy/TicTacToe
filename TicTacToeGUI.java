import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TicTacToeGUI implements ActionListener {
    private JButton[][] buttons;
    private JFrame frame;
    private TicTacToe game;

    public TicTacToeGUI(){
        game = new TicTacToe();
        frame = new JFrame("Tic-Tac-Toe");

        // Set up the grid layout
        frame.setLayout(new GridLayout(3, 3));
        buttons = new JButton[3][3];

        // Initialize the buttons and add them to the frame
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);  // Add action listener to buttons
                frame.add(buttons[i][j]);
            }
        }

        // Set up the frame
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == clickedButton && clickedButton.getText().equals("")) {
                    clickedButton.setText(String.valueOf(game.getCurrentPlayer()));  // Set X or O
                    game.makeMove(i, j);

                    // Check for a win or draw after each move
                    if (game.checkWin()) {
                        JOptionPane.showMessageDialog(frame, game.getCurrentPlayer() + " wins!");
                        resetGame();
                    } else if (game.isBoardFull()) {
                        JOptionPane.showMessageDialog(frame, "It's a draw!");
                        resetGame();
                    }
                    break;
                }
            }
        }
    }

    // Reset the game board for a new game
    private void resetGame() {
        game.initializeBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}

