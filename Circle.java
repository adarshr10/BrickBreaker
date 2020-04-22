/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * circle of a specified color.
 */
public class Circle extends GameObj {
    public static final int SIZE = 20;
    public static final int INIT_POS_X = Paddle.INIT_POS_X;
    public static final int INIT_POS_Y = Paddle.INIT_POS_Y - 3*Paddle.HEIGHT;
    public static int INIT_VEL_X = 4;
    public static int INIT_VEL_Y = 6;
    private int rad;
    private int cen_x;
    private int cen_y;

    private Color color;

    public Circle(int courtWidth, int courtHeight, Color color) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);

        this.color = color;
    }
    
    public Point getEdge(int angle) {
    	double a = Math.toRadians(angle);
    	int x = (int)(cen_x + Math.cos(a) * rad);
    	int y = (int)(cen_y + Math.sin(a) * rad);
    	Point p = new Point(x, y);
    	return p;
    }
    
    @Override
    public boolean intersects(GameObj that) {
    	rad = getHeight()/2;
    	cen_x = this.getPx() + rad;
    	cen_y = this.getPy() + rad;
    	
    	for (int i = 0; i <= 360; i++) {
			int x = (int) getEdge(i).getX();
			int y = (int) getEdge(i).getY();
			if (y >= that.getPy() && y <= that.getPy() + that.getHeight() && 
					x >= that.getPx() && x <= that.getPx() + that.getWidth()) {
				return true;
			}
		}
    	return false;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}