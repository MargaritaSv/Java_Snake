package Snake;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by margarita on 2/27/16.
 */
public class snakeCanvas extends Canvas {


    private final int BOX_HEIGHT = 5;
    private final int BOX_WEIGHT = 5;
    private final int GRID_HEIGHT = 30;
    private final int GRID_WEIGHT = 30;

    private LinkedList<Point> snake;

    public void Draw(Graphics g) {

    }

    public void DrawGrid(Graphics g) {

        //drwaing an outside rectangle
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

}