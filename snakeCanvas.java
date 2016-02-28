package Snake;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by margarita on 2/27/16.
 */
public class snakeCanvas extends Canvas implements Runnable {


    private final int BOX_HEIGHT = 5;
    private final int BOX_WEIGHT = 5;
    private final int GRID_HEIGHT = 30;
    private final int GRID_WEIGHT = 30;

    private LinkedList<Point> snake;
    private Point fruit;

    private Thread runThread;
    private Graphics globalGraphycs;
    private int direction = Direction.NO_DIRECTION;

    public void Paint(Graphics g) {

        snake = new LinkedList<Point>();
        fruit = new Point();

        globalGraphycs = g.create();
        if (runThread == null) {
            runThread = new Thread(this);
            runThread.start();
        }
    }

    public void Draw(Graphics g) {
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
}