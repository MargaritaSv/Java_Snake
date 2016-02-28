package Snake;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

/**
 * Created by margarita on 2/27/16.
 */
public class snakeCanvas extends Canvas implements Runnable, KeyListener {


    private final int BOX_HEIGHT = 10;
    private final int BOX_WEIGHT = 10;
    private final int GRID_HEIGHT = 50;
    private final int GRID_WEIGHT = 50;

    private LinkedList<Point> snake;
    private Point fruit;

    private Thread runThread;
    private Graphics globalGraphycs;

    private int direction = Direction.NO_DIRECTION;

    public void init() {

    }

    public void Paint(Graphics g) {

        this.setPreferredSize(new Dimension(640, 480));
        snake = new LinkedList<Point>();
        snake.add(new Point(3, 1));
        snake.add(new Point(3, 2));
        snake.add(new Point(3, 3));
        fruit = new Point(0, 0);
        this.addKeyListener(this);

        globalGraphycs = g.create();
        if (runThread == null) {
            runThread = new Thread(this);
            runThread.start();
        }
    }

    public void Draw(Graphics g) {
        g.clearRect(0, 0, BOX_WEIGHT * GRID_WEIGHT, BOX_HEIGHT * GRID_HEIGHT); //for clear the screen if we dont the tail will not resize
        DrawGrid(g);
        DrawSnake(g);
        DrawFruit(g);
    }

    public void Move() {

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
//TODO: the snake has hit fruit
        } else if (newPoint.x < 0 || newPoint.x > GRID_WEIGHT) {
//TODO: we went oob
        } else if (newPoint.y < 0 || newPoint.y > GRID_HEIGHT) {
            //TODO: we went oob
        } else if (snake.contains(newPoint)) {
//TODO: we ran int ourself
        }

        //if we reach this point the we are not dead
        snake.push(newPoint);
    }

    public void DrawGrid(Graphics g) {

        //drawing an outside rectangle
        g.drawRect(0, 0, GRID_WEIGHT * BOX_WEIGHT, GRID_HEIGHT * BOX_HEIGHT);

        //drawing the vertical lines
        for (int i = BOX_WEIGHT; i < GRID_WEIGHT * BOX_WEIGHT; i += BOX_WEIGHT) {
            g.drawLine(i, 0, i, BOX_HEIGHT * GRID_HEIGHT);
        }

        //drawing the horizontal lines
        for (int i = BOX_HEIGHT; i < GRID_HEIGHT * BOX_HEIGHT; i += BOX_HEIGHT) {
            g.drawLine(i, 0, i, BOX_WEIGHT * GRID_WEIGHT);
        }
    }

    public void DrawSnake(Graphics g) {

        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * BOX_WEIGHT, p.y * BOX_HEIGHT, BOX_WEIGHT, BOX_HEIGHT);
        }

        g.setColor(Color.BLACK);
    }

    public void DrawFruit(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(fruit.x * BOX_WEIGHT, fruit.y * BOX_HEIGHT, BOX_WEIGHT, BOX_HEIGHT);
        g.setColor(Color.BLACK);
    }

    @Override
    public void run() {
        while (true) {
            Move();
            Draw(globalGraphycs);

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
                direction = Direction.NORTH;
                break;
            case KeyEvent.VK_DOWN:
                direction = Direction.SOUTH;
                break;
            case KeyEvent.VK_RIGHT:
                direction = Direction.EAST;
                break;
            case KeyEvent.VK_LEFT:
                direction = Direction.WEST;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}