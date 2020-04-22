import java.awt.Color;
import java.util.Random;

public class SpeedChangeBrick extends Bricks {
	
	private Random rand;
	
	public SpeedChangeBrick(int courtWidth, int courtHeight, Color color, int state) {
		super(courtWidth, courtHeight, color, state);
		// TODO Auto-generated constructor stub
		rand = new Random();
	}
	
	@Override
	public void collisionReact() {
		this.setState(this.getStatus()-1);
		int generate = rand.nextInt(2);
		Circle s = GameCourt.getBall();
		if(generate == 1) {
			s.setVx(Math.max(2,rand.nextInt(Circle.INIT_VEL_X)-1));
			s.setVy(Math.max(3,rand.nextInt(Circle.INIT_VEL_Y)-1));
		} else {
			s.setVx(Math.max(5,rand.nextInt(Circle.INIT_VEL_X*2)));
			s.setVy(Math.max(7,rand.nextInt(Circle.INIT_VEL_X*2)));
		}
		
	}

}
