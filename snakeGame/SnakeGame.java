package snakeGame;

import javax.swing.JFrame;

public class SnakeGame extends JFrame {
    public SnakeGame() {
        super("Snake Game");
        add(new Board());  // Add game board
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        new SnakeGame(); 
    }
}