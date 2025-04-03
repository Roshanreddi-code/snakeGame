package snakeGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
    private Image apple, dot, head;
    private final int TILE_SIZE = 10;
    private final int TOTAL_TILES = 900;
    private final int RANDOM_POSITION = 19;
    private int dots;
    
    private int appleX, appleY;
    private final int x[] = new int[TOTAL_TILES];
    private final int y[] = new int[TOTAL_TILES];
    
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    
    private Timer timer;
    private int score = 0;

    public Board() {
        addKeyListener(new KeyHandler());
        setBackground(Color.BLACK);
        setFocusable(true);
        loadImages();
        initGame();
    }

    private void loadImages() {
        apple = new ImageIcon(getClass().getResource("/snakegame/icons/apple.png")).getImage();
        dot = new ImageIcon(getClass().getResource("/snakegame/icons/dot.png")).getImage();
        head = new ImageIcon(getClass().getResource("/snakegame/icons/head.png")).getImage();
    }

    private void initGame() {
        dots = 3;  // Initial snake size
        for (int i = 0; i < dots; i++) {
            x[i] = 100 - i * TILE_SIZE;
            y[i] = 100;
        }
        locateApple();
        timer = new Timer(140, this);
        timer.start();
    }

    private void locateApple() {
        int random = (int) (Math.random() * RANDOM_POSITION);
        appleX = random * TILE_SIZE;
        random = (int) (Math.random() * RANDOM_POSITION);
        appleY = random * TILE_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            drawScore(g);
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + score, 10, 20);
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over!";
        Font font = new Font("Arial", Font.BOLD, 20);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(msg, (getWidth() - metrics.stringWidth(msg)) / 2, getHeight() / 2);
        g.drawString("Score: " + score, (getWidth() - metrics.stringWidth("Score: " + score)) / 2, getHeight() / 2 + 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            score += 10; 
            locateApple();
        }
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (y[0] >= 400 || x[0] >= 400 || y[0] < 0 || x[0] < 0) {
            inGame = false;
        }
        if (!inGame) {
            timer.stop();
        }
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (leftDirection) {
            x[0] -= TILE_SIZE;
        }
        if (rightDirection) {
            x[0] += TILE_SIZE;
        }
        if (upDirection) {
            y[0] -= TILE_SIZE;
        }
        if (downDirection) {
            y[0] += TILE_SIZE;
        }
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_UP && !downDirection) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if (key == KeyEvent.VK_DOWN && !upDirection) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}