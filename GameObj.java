import java.awt.*;
import java.util.Collections;

/** 
 * An object in the game. 
 *
 * Game objects exist in the game court. They have a position, velocity, size and bounds. Their
 * velocity controls how they move; their position should always be within their bounds.
 */
public abstract class GameObj {
    /*
     * Current position of the object (in terms of graphics coordinates)
     *  
     * Coordinates are given by the upper-left hand corner of the object. This position should
     * always be within bounds.
     *  0 <= px <= maxX 
     *  0 <= py <= maxY 
     */
    private int px; 
    private int py;

    /* Size of object, in pixels. */
    private int width;
    private int height;

    /* Velocity: number of pixels to move every time move() is called. */
    private int vx;
    private int vy;

    /* 
     * Upper bounds of the area in which the object can be positioned. Maximum permissible x, y
     * positions for the upper-left hand corner of the object.
     */
    private int maxX;
    private int maxY;

    /**
     * Constructor
     */
    public GameObj(int vx, int vy, int px, int py, int width, int height, int courtWidth,
        int courtHeight) {
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.width  = width;
        this.height = height;

        // take the width and height into account when setting the bounds for the upper left corner
        // of the object.
        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;
    }

    /*** GETTERS **********************************************************************************/
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }
    
    public int getVx() {
        return this.vx;
    }
    
    public int getVy() {
        return this.vy;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }

    /*** SETTERS **********************************************************************************/
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    public void setPy(int py) {
        this.py = py;
        clip();
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    /*** UPDATES AND OTHER METHODS ****************************************************************/

    /**
     * Prevents the object from going outside of the bounds of the area designated for the object.
     * (i.e. Object cannot go outside of the active area the user defines for it).
     */ 
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }

    /**
     * Moves the object by its velocity.  Ensures that the object does not go outside its bounds by
     * clipping.
     */
    public void move() {
        this.px += this.vx;
        this.py += this.vy;

        clip();
    }

    /**
     * Determine whether this game object is currently intersecting another object.
     * 
     * Intersection is determined by comparing bounding boxes. If the bounding boxes overlap, then
     * an intersection is considered to occur.
     * 
     * @param that The other object
     * @return Whether this object intersects the other object.
     */
    public boolean intersects(GameObj that) {
        return (this.px + this.width >= that.px
            && this.py + this.height >= that.py
            && that.px + that.width >= this.px 
            && that.py + that.height >= this.py);
    }


    /**
     * Determine whether this game object will intersect another in the next time step, assuming
     * that both objects continue with their current velocity.
     * 
     * Intersection is determined by comparing bounding boxes. If the  bounding boxes (for the next
     * time step) overlap, then an intersection is considered to occur.
     * 
     * @param that The other object
     * @return Whether an intersection will occur.
     */
    public boolean willIntersect(GameObj that) {
        int thisNextX = this.getPx() + this.getVx();
        int thisNextY = this.getPy() + this.getVy();
        int thatNextX = that.getPx() + that.getVx();
        int thatNextY = that.getPy() + that.getVy();
    
        return (thisNextX + this.getWidth() >= thatNextX
            && thisNextY + this.getHeight() >= thatNextY
            && thatNextX + that.getWidth() >= thisNextX 
            && thatNextY + that.getHeight() >= thisNextY);
    }


    /**
     * Update the velocity of the object in response to hitting an obstacle in the given direction.
     * If the direction is null, this method has no effect on the object.
     *
     * @param d The direction in which this object hit an obstacle
     */
    public void bounce(Direction d) {
        if (d == null) {
        	//System.out.println("NULL");
        	return; 
        }
        switch (d) {
        case UP:
  
            this.vy = Math.abs(this.vy);
            break;  
        case DOWN:

            this.vy = -Math.abs(this.vy);
            break;
        case LEFT:
  
            this.vx = Math.abs(this.vx);
            break;
        case RIGHT:

            this.vx = -Math.abs(this.vx);
            break;
        }
    }
    
//    public abstract void setState(boolean nState);
//    
//    public abstract boolean getState();

    /**
     * Determine whether the game object will hit a wall in the next time step. If so, return the
     * direction of the wall in relation to this game object.
     *  
     * @return Direction of impending wall, null if all clear.
     */
    public Direction hitWall() {
        if (this.px + this.vx < 0) {
            return Direction.LEFT;
        } else if (this.px + this.vx > this.maxX) {
           return Direction.RIGHT;
        }

        if (this.py + this.vy < 0) {
            return Direction.UP;
        } else if (this.py + this.vy > this.maxY) {
            return Direction.DOWN;
        } else {
            return null;
        }
    }
    
    /**
     * Determine whether the game object will hit a wall in the next time step. If so, return the
     * direction of the wall in relation to this game object.
     *  
     * @return Opposite direction of impending paddle, null if all clear.
     */
    public Direction hitPaddle(Paddle p) {
        if (this.px + this.vx < p.getPx()) {
            return Direction.RIGHT;
        } else if (this.px + this.vx > p.getPx() + p.getWidth()) {
           return Direction.LEFT;
        }

        if (this.py + this.vy < p.getPy()) {
            return Direction.DOWN;
        } else if (this.py + this.vy > p.getPy() + p.getHeight()) {
            return Direction.UP;
        } else {
            return null;
        }
    }
    
    /**
     * Determine whether the game object will hit a wall in the next time step. If so, return the
     * direction of the wall in relation to this game object.
     *  
     * @return Opposite direction of impending brick, null if all clear.
     */
    public Direction hitBrick(Bricks b) {
        if (this.px + this.vx < b.getPx()) {
            return Direction.RIGHT;
        } else if (this.px + this.vx > b.getPx() + b.getWidth()) {
           return Direction.LEFT;
        }

        if (this.py + this.vy < b.getPy()) {
            return Direction.DOWN;
        } else if (this.py + this.vy > b.getPy() + b.getHeight()) {
            return Direction.UP;
        } else {
            return null;
        }
    }

    /**
     * Determine whether the game object will hit another object in the next time step. If so,
     * return the direction of the other object in relation to this game object.
     * 
     * @param that The other object
     * @return Direction of impending object, null if all clear.
     */
    public Direction hitObj(GameObj that) {
    	if(this.willIntersect(that)) {
		    double dx = that.getPx() + that.getWidth() / 2 - (this.getPx() + this.getWidth() / 2);
		    double dy = that.getPy() + that.getHeight() / 2 - (this.getPy() + this.getHeight() / 2);
		
		    double theta = Math.acos(dx / (Math.sqrt(dx * dx + dy *dy)));
		    double diagTheta = Math.atan2(this.getHeight() / 2, this.getWidth() / 2);
		
		    if (theta <= diagTheta) {
		        return Direction.RIGHT;
		    } else if (theta > diagTheta && theta <= Math.PI - diagTheta) {
		        // Coordinate system for GUIs is switched
		        if (dy > 0) {
		            return Direction.DOWN;
		        } else {
		            return Direction.UP;
		        }
		    } else {
		        return Direction.LEFT;
		    }
    	} else {
    		return null;
    	}
    }
    public abstract void draw(Graphics g);




}
