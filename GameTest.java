import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.NoSuchElementException;

import javax.swing.JLabel;

import org.junit.Test;

/** 
 *  You can use this file (and others) to test your
 *  implementation.
 */

public class GameTest {

	  @Test
	  public void testLoseCondition() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	court.newGame();
	  	court.getBall().setPy(460);
	  	court.tick();
	  	assertEquals(false, court.playingState());
	  }
	  
	  @Test
	  public void testPlayingCondition() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	court.newGame();
	  	court.tick();
	  	assertEquals(true, court.playingState());
	  }
	  
	  
	  @Test
	  public void testWinCondition() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	court.newGame();
	  	court.getBall().setVy(0);
	  	court.getBall().setVx(0);
	  	for(int row = 0; row < court.getBricks().length; row++) {
	  		for(int col = 0; col < court.getBricks()[0].length; col++) {
	  			court.getBricks()[row][col].setState(0);
	  		}
	  	}
	  	court.tick();
	  	assertEquals(false, court.playingState());
	  	
	  }	
	  
	  @Test
	  public void testGameStartScore() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	court.newGame();
	  	assertEquals(0, court.getScore());
	  }
	  
	  @Test (expected = NullPointerException.class)
	  public void testNullGameState() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	GameCourt.GAME_STATE = null;
	  	court.loadGame();
	  }
	  
	  @Test (expected = NullPointerException.class)
	  public void testNullHighScore() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	GameCourt.HIGH_SCORE = null;
	  	court.loadGame();
	  	
	  }
	  
	  @Test
	  public void testNormalBrickCollision() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	court.newGame();
	  	court.getBricks()[5][8].collisionReact();
	  	assertEquals(1, court.getBricks()[5][8].getStatus());	
	  }	
	  
	  @Test
	  public void testCloudBrickCollision() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	court.newGame();
	  	court.getBricks()[5][7].collisionReact();
	  	assertEquals(0, court.getBricks()[5][7].getStatus());
	  }
	  
	  @Test
	  public void testSpeedChangeBrickCollision() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	court.newGame();
	  	int currSpeed = court.getBall().getVx(); 
	  	court.getBricks()[5][8].collisionReact();
	  	assertEquals(1, court.getBricks()[5][8].getStatus());
	  	assertEquals(true, currSpeed == court.getBall().getVx());
	  }
	  
	  @Test
	  public void testExplodingBrickCollision() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	court.newGame();
	  	court.getBricks()[5][0].collisionReact();
	  	assertEquals(0, court.getBricks()[5][0].getStatus());
	  	
	  }
	  
	  // Please note that this test may fail if the randomly chosen speed happens to be the the same
	  // as the original. I have added print statements to confirm that the speed changes.
	  
	  @Test
	  public void testSpeedChangeBrickCollisionTwice() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	court.newGame();
	  	court.getBricks()[5][8].collisionReact();
	  	System.out.println("Speed 1: " + court.getBall().getVx());
	  	int currSpeed = court.getBall().getVx(); 
	  	court.getBricks()[5][8].collisionReact();
	  	System.out.println("Speed 2: " +court.getBall().getVx());
	  	assertEquals(0, court.getBricks()[5][8].getStatus());
	  	assertEquals(true, currSpeed == court.getBall().getVx());
	  }
    
	  @Test
	  public void testCloudBrickCollisionTwice() {
		  GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
		  	court.newGame();
		  	court.getBricks()[5][7].collisionReact();
		  	court.getBricks()[5][7].collisionReact();
		  	assertEquals(-2, court.getBricks()[5][7].getStatus());
	  }
	  
	  @Test
	  public void testNormalBrickCollisionTwice() {
	  	GameCourt court = new GameCourt(new JLabel(), new JLabel(), new JLabel());
	  	court.newGame();
	  	court.getBricks()[5][8].collisionReact();
	  	court.getBricks()[5][8].collisionReact();
	  	assertEquals(0, court.getBricks()[5][8].getStatus());	
	  }	
    
    

}
