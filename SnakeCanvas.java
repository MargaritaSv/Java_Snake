package Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
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

    private int direction = Direction.NO_DIRECTION;

    private int score = 0;
    private String highscore = "";

    private boolean isInMenu = true;
    private Image menuImage;
    private boolean isAnTheGame = false;
    private boolean won = true;

    public void unit() {

    }

    public void paint(Graphics g) {

        if (runThread == null) {

            this.setPreferredSize(new Dimension(640, 480));
            this.addKeyListener(this);

            runThread = new Thread(this);
            runThread.start();
        }

        if (isInMenu) {
            //draw the menu
            drawMenu(g);

        } else if (isAnTheGame) {
            //draw the and game screen
            drawEndGame(g);
        } else {
            //draw everything else
            if (snake == null) {
                snake = new LinkedList<Point>();
                generatedDefaultSnake();
                placeFruit();
            }

            if (highscore.equals("")) {
                //init hight score
                highscore = this.getHightScoreValue();
            }
            drawSnake(g);
            drawGrid(g);
            drawFruit(g);
            drawScore(g);
        }
    }

    public void drawEndGame(Graphics g) {
        BufferedImage endImage = new BufferedImage(this.getPreferredSize().width, this.getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);
        Graphics endGraphics = endImage.getGraphics();
        endGraphics.setColor(Color.BLACK);

        if (won) {
            endGraphics.drawString("You win!", this.getPreferredSize().width / 2, this.getPreferredSize().height / 2);
        } else {
            endGraphics.drawString("You lose!", this.getPreferredSize().width / 2, this.getPreferredSize().height / 2);
        }
        endGraphics.drawString("Your score: " + score, this.getPreferredSize().width / 2, (this.getPreferredSize().height / 2) + 20);
        endGraphics.drawString("Press \"space \" to start new game", this.getPreferredSize().width / 2, (this.getPreferredSize().height / 2) + 40);
        g.drawImage(endImage, 0, 0, this);
    }

    public void drawMenu(Graphics g) {

        if (this.menuImage == null) {
            try {
                URL imagePath = SnakeCanvas.class.getResource("snakeMenu.png");
                menuImage = Toolkit.getDefaultToolkit().getImage(imagePath);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //  Image menuImage = null;

        g.drawImage(menuImage, 0, 0, 640, 480, this);
    }

    public void update(Graphics g) {
        //this is a default update method which will contain our dubal buffering

        Graphics offScreenGraphics;
        BufferedImage offscreen = null;
        Dimension d = this.getSize();

        offscreen = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
        offScreenGraphics = offscreen.getGraphics();
        offScreenGraphics.setColor(this.getBackground());
        offScreenGraphics.fillRect(0, 0, d.width, d.height);
        offScreenGraphics.setColor(this.getForeground()); //paint everything else
        paint(offScreenGraphics);

        //flip
        g.drawImage(offscreen, 0, 0, this);
    }

    public void generatedDefaultSnake() {

        score = 0;
        snake.clear();
        snake.add(new Point(0, 2));
        snake.add(new Point(0, 1));
        snake.add(new Point(0, 0));

        direction = Direction.NO_DIRECTION;
    }

    public void move() {

        if (direction != Direction.NO_DIRECTION) {
            return;
        }
        Point head = snake.peekFirst();
        Point newPoint = head;

        newPoint = moveDirection(head, newPoint);

        //remove the tail
        if (this.direction != Direction.NO_DIRECTION) {
            snake.remove(snake.peekLast());
        }

        if (newPoint.equals(fruit)) {
            // the snake has hit fruit
            score += 10;
            Point addPoint = (Point) newPoint.clone();
            newPoint = moveDirection(head, newPoint);
            snake.push(addPoint);
            placeFruit();
        } else if ((newPoint.x < 0 || newPoint.x > (GRID_WIDTH - 1)) || (newPoint.y < 0 || newPoint.y > (GRID_HEIGHT - 1))) {
            checkScore();
            won = false;
            isAnTheGame = true;
            return;

        } else if (snake.contains(newPoint)) {
            //we ran eat yourself
            checkScore();
            won = false;
            isAnTheGame = true;
            return;

        } else if (snake.size() == (GRID_HEIGHT * GRID_WIDTH)) {
            //we won!
            checkScore();
            won = true;
            isAnTheGame = true;
        }
        //if we reach this point the we are not dead
        snake.push(newPoint);
    }

    private Point moveDirection(Point head, Point newPoint) {
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
        return newPoint;
    }

    private void checkScore() {

        if (highscore.equals("")) {
            return;
        }

        //format Magi:100
        if (score > Integer.parseInt(highscore.split(":")[1])) {
            //user has set a new record
            String name = JOptionPane.showInputDialog("What is your name?");
            highscore = name + ":" + score;

            File scoreFile = new File("hightscore.dat");
            if (!scoreFile.exists()) {
                try {
                    scoreFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileWriter wr = null;
            BufferedWriter bfWr = null;

            try {
                wr = new FileWriter(scoreFile);
                bfWr = new BufferedWriter(wr);
                bfWr.write(this.highscore);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    if (bfWr != null) {
                        bfWr.close();
                    }
                } catch (Exception ex) {

                }
            }
        }
    }

    public void drawScore(Graphics g) {

        g.drawString("Score: " + score, 0, BOX_HEIGHT * GRID_HEIGHT + 10);
        g.drawString("Highscore: " + highscore, 0, BOX_HEIGHT * GRID_HEIGHT + 20);
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

    private void placeFruit() {

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
            repaint();
            if (!isInMenu && !isAnTheGame) {
                move();
            }

            try {
                Thread.currentThread();
                Thread.sleep(100);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    protected String getHightScoreValue() {

        //format: Magi:100
        FileReader readFile = null;
        BufferedReader rd = null;

        try {
            readFile = new FileReader("hightscore.dat");
            rd = new BufferedReader(readFile);
            return rd.readLine();

        } catch (Exception ex) {
            return "0";
        } finally {
            try {
                if (readFile != null) {
                    rd.close();

                }
            } catch (IOException ex) {
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
            case KeyEvent.VK_ENTER:
                if (isInMenu) {
                    isInMenu = false;
                    repaint();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                isInMenu = true;
                break;
            case KeyEvent.VK_SPACE:
                if (isAnTheGame) {
                    isAnTheGame = false;
                    won = false;
                    generatedDefaultSnake();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}