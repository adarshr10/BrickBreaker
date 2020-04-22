import java.awt.Color;

public class ExplodingBricks extends Bricks {

	public ExplodingBricks(int courtWidth, int courtHeight, Color color, int state) {
		super(courtWidth, courtHeight, color, state);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void collisionReact() {
		this.setState(this.getStatus()-2);
		for(int i = this.getPy()-Bricks.HEIGHT; i <= this.getPy()+Bricks.HEIGHT; i+=Bricks.HEIGHT) {
			for(int j = this.getPx()-Bricks.WIDTH; j <= this.getPx()+Bricks.WIDTH; j+=Bricks.WIDTH) {
				if(i >= 0 && i < Bricks.HEIGHT*6 && j >= 0 && j < GameCourt.COURT_WIDTH) {
					GameCourt.getBricks()[i / Bricks.HEIGHT][j / Bricks.WIDTH].setState(Math.max(0, 
							GameCourt.getBricks()[i / Bricks.HEIGHT][j / Bricks.WIDTH].getStatus() 
							- 2));
					
				} 
			}
		}
		
		
	}

}
