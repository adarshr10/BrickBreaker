import java.awt.*;

import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	private Random rand;
	private static int winCounter;
	
    // the state of the game logic
    private Paddle paddle; // the Black Square, keyboard control
    private static Circle ball; // the Golden ball, bounces
   // private Poison poison; // the Poison Mushroom, doesn't move
    private static Bricks[][] bricks = new Bricks[6][10]; // 6 rows 10 cols
    public static Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, 
    								Color.MAGENTA};
    
    public boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    
    private JLabel score;
    private int currScore;
    private JLabel maxScore;
    private int highScore;
    
    private BufferedWriter writer;
    private BufferedReader reader;
    private BufferedWriter writerScore;
    private BufferedReader readerScore;

    // Game constants
    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 500;
    public static final int PADDLE_VELOCITY = 12;
    public static String GAME_STATE = "files/previous_save.txt";
    public static String HIGH_SCORE = "files/high_score.txt";
    
    private static Map<Bricks, Integer> states;
    

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 25;

    public GameCourt(JLabel status, JLabel score, JLabel maxScore) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.currScore = 0;
        

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    paddle.setVx(-PADDLE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    paddle.setVx(PADDLE_VELOCITY);
                }
            }

            public void keyReleased(KeyEvent e) {
                paddle.setVx(0);
                paddle.setVy(0);
            }
        });

        states = new TreeMap<Bricks, Integer>();
        
        rand = new Random();
        winCounter = 0;
        
        this.status = status;
        this.score = score;
        this.maxScore = maxScore;
    }

    
    public void newGame() {
    	
    	currScore = 0;
    	score.setText("Score: " + currScore);
    	
    	paddle = new Paddle(COURT_WIDTH, COURT_HEIGHT, Color.WHITE);
        for(int row = 0; row < bricks.length; row++) {
    		for(int col = 0; col < bricks[0].length; col++) {
    			if(col == 9 && row == 5 || col == 0 && row == 5 ) {
    				Bricks brick = new ExplodingBricks(COURT_WIDTH, COURT_HEIGHT, Color.DARK_GRAY, 
    						2);
	    			bricks[row][col] = brick;
    			}else if(col - row == 2 || row - col == 4 || col - row == -2 || row - col == -4) {
					Bricks brick = new CloudBricks(COURT_WIDTH, COURT_HEIGHT, Color.WHITE, 2);
	    			bricks[row][col] = brick;
				} else if(col - row == 3 || row - col == 6 || col - row == -3 || row - col == -6){
					Bricks brick = new SpeedChangeBrick(COURT_WIDTH, COURT_HEIGHT, Color.PINK, 2);
	    			bricks[row][col] = brick;
				}else {
    				Bricks brick = new Bricks(COURT_WIDTH, COURT_HEIGHT, colors[row], 2);
        			bricks[row][col] = brick;
    			}
    			
    		}
        }
        ball = new Circle(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW);
        
        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    public void loadGame() {
    	
    	try {
    		readerScore = new BufferedReader(new FileReader(HIGH_SCORE));
    	} catch(IOException e) {
    		System.out.println("IO Exception Occurred");
    	}
    	
    	try {
    		reader = new BufferedReader(new FileReader(GAME_STATE));
    	} catch(IOException e) {
    		System.out.println("IO Exception Occurred");
    	}
    	
    	paddle = new Paddle(COURT_WIDTH, COURT_HEIGHT, Color.WHITE);
        //poison = new Poison(COURT_WIDTH, COURT_HEIGHT);
        for(int row = 0; row < bricks.length; row++) {
    		for(int col = 0; col < bricks[0].length; col++) {
    			String checker;
				try {
					checker = reader.readLine();
					try{
						if(Integer.parseInt(checker) > 2 || Integer.parseInt(checker) < 0) {
							Bricks brick = new Bricks(COURT_WIDTH, COURT_HEIGHT, colors[row], 
		    						2);
		        			bricks[row][col] = brick;
						} else {
							Color currColor = colors[row];
							if(Integer.parseInt(checker) == 1) {
								Bricks brick = new Bricks(COURT_WIDTH, COURT_HEIGHT, new Color(
		        						Math.max(currColor.getRed() - 60, 0), 
		        						Math.max(currColor.getGreen() - 60, 0), 
		        						Math.max(currColor.getBlue() - 60, 0)), 
										Integer.parseInt(checker));
			        			bricks[row][col] = brick;
							} else {
								if(col == 9 && row == 5 || col == 0 && row == 5 ) {
				    				Bricks brick = new ExplodingBricks(COURT_WIDTH, COURT_HEIGHT, 
				    						Color.DARK_GRAY, Integer.parseInt(checker));
					    			bricks[row][col] = brick;
				    			}else if(col - row == 2 || row - col == 4 || col - row == -2 || 
				    					row - col == -4) {
									Bricks brick = new CloudBricks(COURT_WIDTH, COURT_HEIGHT, 
											Color.WHITE, Integer.parseInt(checker));
					    			bricks[row][col] = brick;
								} else if(col - row == 3 || row - col == 6 || col - row == -3 || 
										row - col == -6){
									Bricks brick = new SpeedChangeBrick(COURT_WIDTH, COURT_HEIGHT, 
											Color.PINK, Integer.parseInt(checker));
					    			bricks[row][col] = brick;
								}else {
				    				Bricks brick = new Bricks(COURT_WIDTH, COURT_HEIGHT, 
				    						colors[row], Integer.parseInt(checker));
				        			bricks[row][col] = brick;
				    			}
							}
						}
					} catch(NumberFormatException e){
						Bricks brick = new Bricks(COURT_WIDTH, COURT_HEIGHT, colors[row], 
	    						2);
	        			bricks[row][col] = brick;
					}
								
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("IO Exception Occurred");
				}
    			
    		}
        }
        
        try {
        	currScore = Integer.parseInt(reader.readLine());
        	score.setText("Score: " + currScore);
        }catch(IOException e) {
        	System.out.println("IO Exception occurred");
        }

        
        
        ball = new Circle(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW);
        
        try {
	    	String max;
	    	max = readerScore.readLine();
	    	System.out.println(max);
	    	if(max == null) {
	    		maxScore.setText("High Score: " + 0);
	    	} else {
	    		maxScore.setText("High Score: " + Integer.parseInt(max));
	    	}	
	    }catch(IOException e) {
	    	System.out.println("IO Exception occurred");
	    }


        playing = true;
        status.setText("Running...");
       
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
    	try {
    		readerScore = new BufferedReader(new FileReader(HIGH_SCORE));
    	} catch(IOException e) {
    		System.out.println("IO Exception Occurred");
    	}
    	
    	currScore = 0;
    	score.setText("Score: " + currScore);
    	
    	paddle = new Paddle(COURT_WIDTH, COURT_HEIGHT, Color.WHITE);
        //poison = new Poison(COURT_WIDTH, COURT_HEIGHT);
        for(int row = 0; row < bricks.length; row++) {
    		for(int col = 0; col < bricks[0].length; col++) {
    			if(col == 9 && row == 5 || col == 0 && row == 5 ) {
    				Bricks brick = new ExplodingBricks(COURT_WIDTH, COURT_HEIGHT, 
    						Color.DARK_GRAY, 2);
	    			bricks[row][col] = brick;
    			}else if(col - row == 2 || row - col == 4 || col - row == -2 || row - col == -4) {
					Bricks brick = new CloudBricks(COURT_WIDTH, COURT_HEIGHT, Color.WHITE, 2);
	    			bricks[row][col] = brick;
				} else if(col - row == 3 || row - col == 6 || col - row == -3 || row - col == -6){
					Bricks brick = new SpeedChangeBrick(COURT_WIDTH, COURT_HEIGHT, Color.PINK, 2);
	    			bricks[row][col] = brick;
				}else {
    				Bricks brick = new Bricks(COURT_WIDTH, COURT_HEIGHT, colors[row], 2);
        			bricks[row][col] = brick;
    			}
    		}
        }
        ball = new Circle(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW);


        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    /**
     * Save the state of the game
     */
    
    public void saveGame() {
    	try {
        	writer = new BufferedWriter(new FileWriter(GAME_STATE));
        	
        }
        catch(IOException e){
        	System.out.println("IO Exception occurred");
        }
    	
    	try {
        	writerScore = new BufferedWriter(new FileWriter(HIGH_SCORE));
        	
        }
        catch(IOException e){
        	System.out.println("IO Exception occurred");
        }
    	
    	for(int row = 0; row < bricks.length; row++) {
    		for(int col = 0; col < bricks[0].length; col++) {
    			try {
					writer.write((new Integer(bricks[row][col].getStatus())).toString());
					writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("IO Exception occurred");
				}
    			
    		}
        }
			if (playing == true) {
				try {
					writer.write((new Integer(currScore).toString()));
					if(currScore > highScore) {
						writerScore.write(new Integer(currScore).toString());
						highScore = currScore;
					} else {
						writerScore.write(new Integer(highScore).toString());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("IO Exception occurred");
				}
		    	try {
		    		writer.close();
		    		writerScore.close();
		    	} catch(IOException e) {
		    		System.out.println("IO Exception occurred");
		    	}
			}
    		
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (this.playingState()) {
            // advance the square and ball in their current direction.
        	paddle.move();
            ball.move();

            // make the ball bounce off walls...
            ball.bounce(ball.hitWall());
            // ...and the paddle
            if(ball.intersects(paddle)) {
            	if(ball.getPx() + ball.getVx() > (paddle.getPx() + paddle.getVx() + 60)
            			&& ball.getVx() < 0) {
            		ball.bounce(ball.hitPaddle(paddle));
            		ball.setVx(-ball.getVx());
            	} else if(ball.getPx() + ball.getVx() < (paddle.getPx() + paddle.getVx() + 40) 
            			&& ball.getVx() > 0){

            		ball.bounce(ball.hitPaddle(paddle));
            		ball.setVx(-ball.getVx());
            	} else {
            		ball.bounce(ball.hitPaddle(paddle)); 
            }
            		
           } 
            
            for(int row = 0; row < bricks.length; row++) {
        		for(int col = 0; col < bricks[0].length; col++) {
        			states.put(bricks[row][col],bricks[row][col].getStatus());
        			if(ball.intersects(bricks[row][col]) && bricks[row][col].getStatus() == 2) {
        				ball.bounce(ball.hitObj(bricks[row][col]));
        				Color currColor = bricks[row][col].getColor();
        				bricks[row][col].setColor(new Color(
        						Math.max(currColor.getRed() - 60, 0), Math.max(currColor.getGreen() 
        								- 60, 0), 
        						Math.max(currColor.getBlue() - 60, 0)));
        				bricks[row][col].collisionReact();
        				if(bricks[row][col] instanceof CloudBricks) {
        					currScore++;
        					score.setText("Score: " + currScore);
        				}
        				if(bricks[row][col] instanceof ExplodingBricks) {
        					if(row == 0 && col == 0 || row == 5 && col == 0 || row == 0 && col == 9 
        							|| row == 5 && col == 9) {
        						currScore+=4;
            					score.setText("Score: " + currScore);
        					} else if(row == 0 || row == 5) {
        						currScore+=6;
            					score.setText("Score: " + currScore);
        					} else if(col == 0 || col == 9) {
        						currScore+=6;
            					score.setText("Score: " + currScore);
        					} else {
        						currScore+=9;
            					score.setText("Score: " + currScore);
        					}
        					
        				} 
        			} else if(ball.intersects(bricks[row][col]) && 
        					bricks[row][col].getStatus() == 1){
        				bricks[row][col].collisionReact();
        				ball.bounce(ball.hitObj(bricks[row][col])); 
        				currScore++;
        				score.setText("Score: " + currScore);

        			}
        		}
            }
            	 

            
            if(this.hasLost()) {
            	status.setText("You lose!");
            } 
            
        	if(this.hasWon()) {
        		status.setText("You Win!");
        	} 
            
            

            // update the display
            repaint();
        }
    }
    
    public int getScore() {
    	return currScore;
    }
    
    public static Circle getBall() {
    	return ball;
    }
    
    public static Bricks[][] getBricks(){
    	return bricks;
    }
    
 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paddle.draw(g);
        for(int row = 0; row < 6*Bricks.HEIGHT; row+=(Bricks.HEIGHT)) {
    		for(int col = 0; col < GameCourt.COURT_WIDTH; col+=(Bricks.WIDTH)) {
    			bricks[row/Bricks.HEIGHT][col/Bricks.WIDTH].setPy(row);
    			bricks[row/Bricks.HEIGHT][col/Bricks.WIDTH].setPx(col);
    			if(bricks[row/Bricks.HEIGHT][col/Bricks.WIDTH].getStatus() != 0) {
    				bricks[row/Bricks.HEIGHT][col/Bricks.WIDTH].draw(g);
    			} 
    		}
        }
        ball.draw(g);
        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }


	public boolean playingState() {
		boolean b = playing;
		return b;
	}
	
	public void setPlayingState(boolean b) {
		playing = b;
	}
	
	public boolean hasWon() {
		if(!states.containsValue(1) && !states.containsValue(2)) {
    		this.setPlayingState(false);
    		return true;
    	} else {
    		return false;
    	}
	}
	
	public boolean hasLost() {
		if(ball.getPy() > paddle.getPy() + paddle.getHeight() + 35) {
	    	this.setPlayingState(false); 
	    	return true;
	    }  else {
	    	return false;
	    }
	}
	
	
}
