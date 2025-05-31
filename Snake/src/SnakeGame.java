import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; // for storing the segments of the snake body
import java.util.Random; // for getting random food for the snake
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener { // we are writing the extend to take the
                                                                               // property of the j panel
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    // snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // Food
    Tile food;
    Random random;

    // Game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;

    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) { // constructor
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(200, this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // grid lines
        for( int i=0;i<boardWidth/tileSize;i++){ //changed
        g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
        g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }

        // food
        g.setColor(Color.red);
        // g.fillRect(food.x*tileSize, food.y*tileSize,tileSize , tileSize); //Changed
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // snake Body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        // snake head
        g.setColor(Color.green);
        // g.fillRect(snakeHead.x *tileSize, snakeHead.y*tileSize ,tileSize,tileSize); //Changed
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakepart = snakeBody.get(i);
            g.fillRect(snakepart.x*tileSize, snakepart.y*tileSize, tileSize, tileSize);
            g.fill3DRect(snakepart.x * tileSize, snakepart.y * tileSize, tileSize, tileSize, true);
            g.setColor(Color.blue); // Changed
        }
        // Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over:" + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        } else {
            g.drawString("Score:" + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize); // 600/25-->24
        food.y = random.nextInt(boardHeight / tileSize); // randoem is for random location of the food

    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        // snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // gameOver conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // collide with the snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0
                || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }

    }

    // do not need
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
