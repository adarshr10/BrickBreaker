/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;

import java.awt.event.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	private static BufferedReader reader;
	private static BufferedReader readerScore;
	private static BufferedWriter writerScore;
	private static BufferedWriter writer;
	
	public static final String GAME_STATE = "files/previous_save.txt";
	public static final String HIGH_SCORE = "files/high_score.txt";
	
	File file1 =  new File(GAME_STATE);
	File file2 =  new File(HIGH_SCORE);
	
	private static int maxScore;
	
    public void run() {
    	JFrame frame0 = new JFrame("Enhanced Brickbreaker Start Menu");
    	frame0.setLocation(300,300);
    	
    	JPanel menu = new JPanel(new GridLayout(3,1));
    	
    	JButton start = new JButton("Start Game");
    	start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	frame0.setVisible(false);
                runGame();
            }
        });
    	
    	JButton instruct = new JButton("Instructions");
    	instruct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(null, "Welcome to BrickBreaker! \n To move the paddle,"
            			+ "use the left and right arrow keys. To use the buttons, simply use the mouse."
            			+ "\n Note that if this is your first time playing, you will get a warning if you"
            			+ "try to load a saved state. \n In this game, the object is to break bricks"
            			+ "to get the highest score. You gain points by breaking bricks completely"
            			+ " \n Normal bricks are colored with the rainbow colors "
            			+ "and take 2 hits. \n Exploding bricks(draw gray) explode all the bricks "
            			+ "around them and "
            			+ "give you a number of points equal to all the surrounding bricks and itself \n "
            			+ "Note that the above is the case even if all the bricks surrounding are gone,"
            			+ "so you can try to strategize to get a higher score."
            			+ "\n Cloud bricks(white) only take 1 hit to break. \n Pink bricks are speed "
            			+ "changing bricks"
            			+ " that take two hits to break. They randomly select a speed from double "
            			+ "and half the initial ball speed \n"
            			+ "If the ball goes below the paddle, you lose. \n"
            			+ "If you are able to clear all the bricks you win!");
            }
        });
    	
    	JButton load = new JButton("Load Game");
    	load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	try {
					reader = new BufferedReader(new FileReader(GAME_STATE));
					LineNumberReader scores = new LineNumberReader(reader);
					int linenumber = 0;
	    		    
    	            while (scores.readLine() != null){
    	        	linenumber++;
    	            }
    	            scores.close();
					if(linenumber != 61) {
						JOptionPane.showMessageDialog(null, "You do not have any saved games!");
					} else {
						try {
				    		readerScore = new BufferedReader(new FileReader(HIGH_SCORE));
				    		String max = readerScore.readLine();
				    		if(max == null) {
				    			maxScore = 0;
				    			loadOld();
				    		} else {
				    			try {
				    				int temp =  Integer.parseInt(max);
				    				if(temp > 60) {
				    					maxScore = 0;
				    				} else {
				    					maxScore = temp;
				    				}
					    			loadOld();
				    			} catch(NumberFormatException e1) {
				    				maxScore = 0;
				    				loadOld();
				    			}
				    		}
				    		
				    	} catch (FileNotFoundException e1) {
							maxScore = 0;
						} catch(IOException e1){
				    		System.out.println("IOException occurred");
				    	} 
					}
            	} catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "You do not have any saved games!");
				}  catch (IOException e1) {
					System.out.println("IOException occurred");
				} 
            	
            	
            	
            }
        });
    	
    	menu.add(start);
    	menu.add(instruct);
    	menu.add(load);
    	
    	frame0.add(menu);
    	
    	frame0.pack();
    	frame0.setSize(800,500);
        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setVisible(true);
    	
    	
        
    }
    public static void runGame() {
    	final JFrame frame = new JFrame("Enhanced Brickbreaker");
        frame.setLocation(300, 300);
        
        
        // Status panel
        final JPanel status_panel = new JPanel(new GridLayout(1,3));
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        
        final JLabel score = new JLabel("Score: "   + 0);
        status_panel.add(score); 
    	try {
    		readerScore = new BufferedReader(new FileReader(HIGH_SCORE));
    		String max = readerScore.readLine();
    		if(max == null) {
    			maxScore = 0;
    		} else {
    			try {
    				int temp =  Integer.parseInt(max);
    				if(temp > 60) {
    					maxScore = 0;
    				} else {
    					maxScore = temp;
    				}
    			} catch(NumberFormatException e1) {
    				maxScore = 0;
    			}
    		}
    		
    	} catch (FileNotFoundException e1) {
			maxScore = 0;
		} catch(IOException e){
    		System.out.println("IOException occurred");
    	}
        
        final JLabel highScore = new JLabel("High Score: " + maxScore);
        status_panel.add(highScore);

        // Main playing area
        final GameCourt court = new GameCourt(status, score, highScore);
        frame.add(court, BorderLayout.CENTER);
   

        // Reset button
        final JPanel control_panel = new JPanel(new GridLayout(1,3));
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);
        
        final JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        control_panel.add(quit);
        
        final JButton save = new JButton("Save and Quit");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	court.saveGame();
                frame.dispose();
            }
        });
        control_panel.add(save);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // Start game
        court.newGame();
    }
    
    public static void loadOld() {
    	final JFrame frame = new JFrame("Enhanced Brickbreaker");
        frame.setLocation(300, 300);
        
        
        // Status panel
        final JPanel status_panel = new JPanel(new GridLayout(1,3));
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        
        final JLabel score = new JLabel("Score: "   + 0);
        status_panel.add(score);
        
        try {
    		readerScore = new BufferedReader(new FileReader(HIGH_SCORE));
    		String max = readerScore.readLine();
    		if(max == null) {
    			maxScore = 0;
    		} else {
    			try {
    				int temp =  Integer.parseInt(max);
    				if(temp > 60) {
    					maxScore = 0;
    				} else {
    					maxScore = temp;
    				}
    			} catch(NumberFormatException e1) {
    				maxScore = 0;
    			}
    		}
    		
    	} catch (FileNotFoundException e1) {
			maxScore = 0;
		} catch(IOException e){
    		System.out.println("IOException occurred");
    	}
        
        final JLabel highScore = new JLabel("High Score: " + maxScore);
       status_panel.add(highScore);
       

        // Main playing area
        final GameCourt court = new GameCourt(status, score, highScore);
        frame.add(court, BorderLayout.CENTER);
   

        // Reset button
        final JPanel control_panel = new JPanel(new GridLayout(1,3));
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);
        
        final JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        control_panel.add(quit);
        
        final JButton save = new JButton("Save and Quit");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.saveGame();
                frame.dispose();
            }
        });
        control_panel.add(save);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.loadGame();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}