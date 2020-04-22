import java.awt.Color;

public class CloudBricks extends Bricks {

	public CloudBricks(int courtWidth, int courtHeight, Color color, int state) {
		super(courtWidth, courtHeight, color, state);
	}
	
	@Override
	public void collisionReact() {
		this.setState(this.getStatus() - 2);
	}

}
