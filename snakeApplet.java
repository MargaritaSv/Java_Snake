package Snake;

import java.applet.Applet;
import java.awt.*;

/**
 * Created by margarita on 2/28/16.
 */
public class snakeApplet extends Applet {

    private snakeCanvas c;

    public void init() {
        c = new snakeCanvas();
        c.setPreferredSize(new Dimension(640,480));
        c.setVisible(true);
        c.setFocusable(true);
        this.add(c);
        this.setVisible(true);
        this.setSize(new Dimension(640, 480));
    }

    public void Paint(Graphics g) {
        this.setSize(new Dimension(640, 480));
    }
}
