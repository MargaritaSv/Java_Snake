package Snake;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by margarita on 2/27/16.
 */
public class SnakeCanvas extends Canvas implements Runnable, KeyListener {


    private final int BOX_HEIGHT = 15;
    private final int BOX_WIDTH = 15;
    private final int GRID_HEIGHT = 25;
    private final int GRID_WIDTH = 25;

    private LinkedList<Point> snake;
    private Point fruit;

    private Thread runThread;
    private Graphics globalGraphycs;

    private int direction = Direction.NO_DIRECTION;

    private int score = 0;

    public void paint(Graphics g) {

        this.setPreferredSize(new Dimension(640, 480));
        snake = new LinkedList<Point>();
        generatedDefaultSnake();
        placeFruit();
        this.addKeyListener(this);

        globalGraphycs = g.create();
        if (runThread == null) {
            runThread = new Thread(this);
            runThread.start();
        }
    }

    public void generatedDefaultSnake() {

        score = 0;
        snake.clear();
        snake.add(new Point(0, 2));
        snake.add(new Point(0, 1));
        snake.add(new Point(0, 0));

        direction = Direction.NO_DIRECTION;
    }

    public void draw(Graphics g) {
        g.clearRect(0, 0, BOX_WIDTH * GRID_WIDTH + 10, BOX_HEIGHT * GRID_HEIGHT + 20); //for clear the screen if we dont the tail will not resize
        //create new image
        BufferedImage buffer = new BufferedImage(BOX_WIDTH * GRID_WIDTH + 10, BOX_HEIGHT * GRID_HEIGHT + 20, BufferedImage.TYPE_INT_ARGB);
        Graphics bufferGraphics = buffer.getGraphics();

        drawSnake(bufferGraphics);
        drawGrid(bufferGraphics);
        drawFruit(bufferGraphics);
        drawScore(bufferGraphics);

        //flip
        g.drawImage(buffer, 0, 0, BOX_WIDTH * GRID_WIDTH + 10, BOX_HEIGHT * GRID_HEIGHT + 20, this);
    }

    public void move() {

        Point head = snake.peekFirst();
        Point newPoint = head;
        switch (direction) {
            case Direction.NORTH:
                newPoint = new Point(head.x, head.y - 1);
                break;
            case Direction.SOUTH:
                newPoint = new Point(head.x, head.y + 1);
                break;
            case Direction.EAST:
                newPoint = new Point(head.x + 1, head.y);
                break;
            case Direction.WEST:
                newPoint = new Point(head.x - 1, head.y);
                break;
        }
        //remove the tail
        snake.remove(snake.peekLast());

        if (newPoint.equals(fruit)) {
// the snake has hit fruit
            score += 10;
            Point addPoint = (Point) newPoint.clone();

            switch (direction) {
                case Direction.NORTH:
                    newPoint = new Point(head.x, head.y - 1);
                    break;
                case Direction.SOUTH:
                    newPoint = new Point(head.x, head.y + 1);
                    break;
                case Direction.EAST:
                    newPoint = new Point(head.x + 1, head.y);
                    break;
                case Direction.WEST:
                    newPoint = new Point(head.x - 1, head.y);
                    break;
            }
            snake.push(addPoint);
            placeFruit();
        } else if (newPoint.x < 0 || newPoint.x > (GRID_WIDTH - 1)) {
            generatedDefaultSnake();
            return;
        } else if (newPoint.y < 0 || newPoint.y > (GRID_HEIGHT - 1)) {
            generatedDefaultSnake();
            return;
        } else if (snake.contains(newPoint)) {
            //we ran int yourself
            generatedDefaultSnake();
            return;
        }
        //if we reach this point the we are not dead
        snake.push(newPoint);
    }

    public void drawScore(Graphics g) {
        g.drawString("Score: " + score, 0, BOX_HEIGHT * GRID_HEIGHT);
    }

    public void drawGrid(Graphics g) {

        //drawing an outside rectangle
        g.drawRect(0, 0, GRID_WIDTH * BOX_WIDTH, GRID_HEIGHT * BOX_HEIGHT);

        //drawing the vertical lines
        for (int i = BOX_WIDTH; i < GRID_WIDTH * BOX_WIDTH; i += BOX_WIDTH) {
            g.drawLine(i, 0, i, BOX_HEIGHT * GRID_HEIGHT);
        }

        //drawing the horizontal lines
        for (int i = BOX_HEIGHT; i < GRID_HEIGHT * BOX_HEIGHT; i += BOX_HEIGHT) {
            g.drawLine(i, 0, i, BOX_WIDTH * GRID_WIDTH);
        }
    }

    public void drawSnake(Graphics g) {

        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * BOX_WIDTH, p.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT);
        }

        g.setColor(Color.BLACK);
    }

    public void drawFruit(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(fruit.x * BOX_WIDTH, fruit.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT);
        g.setColor(Color.BLACK);
    }

    public void placeFruit() {

        Random rnd = new Random();
        int randomX = rnd.nextInt(GRID_WIDTH);
        int randomY = rnd.nextInt(GRID_HEIGHT);
        Point rndPoint = new Point(randomX, randomY);

        while (snake.contains(rndPoint)) {
            randomX = rnd.nextInt(GRID_WIDTH);
            randomY = rnd.nextInt(GRID_HEIGHT);
            rndPoint = new Point(randomX, randomY);
        }
        fruit = rndPoint;
    }

    @Override
    public void run() {
        while (true) {
            move();
            draw(globalGraphycs);

            try {
                Thread.currentThread();
                Thread.sleep(100);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != Direction.SOUTH) {
                    direction = Direction.NORTH;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != Direction.NORTH) {
                    direction = Direction.SOUTH;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != Direction.WEST) {
                    direction = Direction.EAST;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (direction != Direction.EAST) {
                    direction = Direction.WEST;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}