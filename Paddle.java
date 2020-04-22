import java.awt.Color;
import java.awt.Graphics;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * square of a specified color.
 */
public class Paddle extends GameObj {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;
    public static final int INIT_POS_X = 350;
    public static final int INIT_POS_Y = 400;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private Color color;

    /**
    * Note that, because we don't need to do anything special when constructing a Square, we simply
    * use the superclass constructor called with the correct parameters.
    */
    public Paddle(int courtWidth, int courtHeight, Color color) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, WIDTH, HEIGHT, courtWidth, 
        		courtHeight);

        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}