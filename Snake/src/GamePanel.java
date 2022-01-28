import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JPanel;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
	static final int SCREEN_WIDTH = 1200;
	static final int SCREEN_HEIGHT = 800;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 70;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	private String highScore = "";
	private int score = 0;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		if(highScore.equals("")) {
			highScore = this.getHighScore();
		}
	}
	public void draw(Graphics g) {

		if (running) {
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Luminari", Font.BOLD, 40) );
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + score, ((SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2)-5, (g.getFont().getSize()/2)+10); //sets "GAME OVER" in center of screen
			g.drawString("HighScore: " + highScore, ((SCREEN_WIDTH - metrics.stringWidth("Score: " + highScore))/2)-5, (g.getFont().getSize()/2)+50);
		}else {
			gameOver(g);//g is the graphics
		}
			
		
	}
	public void newApple(){
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move(){
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++; //increase body parts by 1
			//applesEaten++; //basically increases the score for us
			score++;
			newApple();//generates new apple
		}
	}
	public void checkCollisions() {
		//checks if head collides with body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
			checkScore();
		}
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
			checkScore();
		}
		//check if head touches top border
		if(y[0] < 0) {
			running = false;
			checkScore();
		}
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
			checkScore();
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		//Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Luminari", Font.BOLD, 75) );
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", ((SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2)+30, SCREEN_HEIGHT/2); //sets "GAME OVER" in center of screen
		g.setColor(Color.red);
		g.setFont(new Font("Luminari", Font.BOLD, 40) );
		g.drawString("Score: " + applesEaten, ((SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2)-5, (g.getFont().getSize()/2)+10); //sets "GAME OVER" in center of screen
	
		

        } 
	public  String getHighScore(){
		FileReader readFile = null;
		BufferedReader reader = null;
		try {
			readFile = new FileReader("highscore.dat");//.dat is a data file that can't be edited
			reader = new BufferedReader(readFile);
			return reader.readLine();
		} catch (Exception e) {
			return "Nobody:0";
		} 
		finally {
			try {
				if(reader != null)
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void checkScore() {
		
		if(score > Integer.parseInt(highScore.split(":")[1])) {
			String name = JOptionPane.showInputDialog("You set a new HighScore. What is your name?: ");
			highScore = name + ":" + score;
			
			File scoreFile = new File("highscore.dat");
			if(!scoreFile.exists()) {
				try {
					scoreFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			FileWriter writeFile = null;
			BufferedWriter writer = null;
			try {
				writeFile = new FileWriter(scoreFile);
				writer = new BufferedWriter(writeFile);
				writer.write(this.highScore);
			}catch(Exception e) {
				
			}
			finally {
				if(writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	//inner class
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}