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
public class Bricks extends GameObj implements Comparable {
	public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int INIT_POS_X = 0;
    public static final int INIT_POS_Y = 0;
    public static final int INIT_VEL_X = 2;
    public static final int INIT_VEL_Y = 3;
    public int bState;

    private Color color;

    public Bricks(int courtWidth, int courtHeight, Color color, int state) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, WIDTH, HEIGHT, 
        		courtWidth, courtHeight);
        this.bState = state;

        this.color = color;
    }
    
    public void setState(int nState) {
    	bState = nState;
    }
    
    public void setColor(Color nColor) {
    	this.color = nColor;
    }
    
    public Color getColor() {
    	Color myColor = this.color;
    	return myColor;
    }
    
    public int getStatus() {
    	int cState = this.bState;
    	return cState;
    }
    
    
    
    public void collisionReact() {
    	this.setState(this.getStatus()-1);
    }

    @Override
    public void draw(Graphics g) {
		g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        		
    }
    
    @Override
	public int compareTo(Object o) {
    	if(o instanceof Bricks) {
    		if(this.getStatus()  > ((Bricks) o).getStatus()) {
        		return 1;
        	} else if(this.getStatus()  < ((Bricks) o).getStatus()) {
        		return -1;
        	} else {
        		return 0;
        	}
    	} else {
    		return -1;
    	}
		
	}
}