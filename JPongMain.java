package com.jpong.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;

public class Pong extends JFrame {
	private static final long serialVersionUID = 1L;
	
private final static int WIDTH = 700, HEIGHT = 450;
	public final int XBORDER = 7, YBORDER = 29;
  private PongPanel panel; 
  
  public Pong() {
    setSize(WIDTH, HEIGHT);
    setTitle("JPong");
    setBackground(Color.BLACK);
    setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    panel = new PongPanel(this);
    add(panel);
  }
  
  public PongPanel getPanel() {
    return panel;
  }
  
  public static void main(String[] args) {
    new Pong();
  }

	
	
public class PongPanel extends JPanel implements ActionListener, KeyListener {
		private static final long serialVersionUID = 1L;
	private Pong game;
	private Ball ball;
	private Paddle player1, player2;
	private int width, height;
  
  private void updateDimensions() {
    width = game.getWidth();
    height = game.getHeight();  
  }
  
  public PongPanel (Pong game) {
    setBackground(Color.BLACK);
    this.game = game;
    updateDimensions();
    
    ball = new Ball(game, 30, 2);
    
    int paddleWidth = 10; 
    int paddleHeight = 60;
    int maxSpeed = 1;
    
    player1 = new Paddle(1, game, KeyEvent.VK_UP, KeyEvent.VK_DOWN,
      (int) (width * 0.9f), paddleWidth, paddleHeight, maxSpeed);
    player2 = new Paddle(2, game, KeyEvent.VK_W, KeyEvent.VK_S,
      (int) (width * 0.1f), paddleWidth, paddleHeight, maxSpeed);
    
    Timer timer = new Timer(5, this);
    timer.start();
    addKeyListener(this);
    setFocusable(true);
  }
  
  public void update() {
    updateDimensions();
    ball.update();
    player1.update();
    player2.update();
  	checkWinner(10);  
  }
  
	public checkWinner(int goal) {
		if (player1.score == 10) {
    	JOptionPane.showMessageDialog(
    			null, "Player 1 wins", "Pong", JOptionPane.PLAIN_MESSAGE);
			resetScore();
		}
    else if (player2.score == 10) {
    	JOptionPane.showMessageDialog(
    			null, "Player 2 wins", "Pong", JOptionPane.PLAIN_MESSAGE);
			resetScore();
		}
	}
	
	public resetScore() {
		player1.score = 0;
		player2.score = 0;
	}	
	
  public Paddle getPlayer(int playerNum) {
    if (playerNum == 1) return player1;
    else return player2;
  }
  
  public void increaseScore(int playerNum) {
    if (playerNum == 1) player1.increaseScore();
    else player2.increaseScore();
  }
  
  public void actionPerformed(ActionEvent e) {
    update();
    repaint();
  }
  
  public void keyPressed(KeyEvent e) {
    player1.pressed(e.getKeyCode());
    player2.pressed(e.getKeyCode());
  }
  public void keyReleased(KeyEvent e) {
    player1.released(e.getKeyCode());
    player2.released(e.getKeyCode());
  }
  public void keyTyped(KeyEvent e) {
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)(g);
    //g2d.setColor(Color.WHITE);
    g.drawString(player2.score + "  :  " + player1.score, 
      width / 2, (int) (height * 0.1f));
    ball.paint(g2d);
    player1.paint(g2d);
    player2.paint(g2d);
  }
  
}


public class Ball {
  private int size;
  private Pong game;
  private int x, y, xSpeed, ySpeed;
  
  public Ball(Pong game, int size, int speed) {
    this.game = game;
    this. size = size;
    xSpeed = speed;
    ySpeed = speed;
    resetBall();
  }
  
  public void resetBall() {
    x = game.getWidth() / 2;
    y = (int) (Math.random() * game.getHeight());
  }
  
  public void update() {
    
    x += xSpeed;
    y += ySpeed;
  
    if (x < 0) {
      game.getPanel().increaseScore(1);
      resetBall();
      xSpeed *= -1;
    }
    else if (x > game.getWidth() - size - game.XBORDER) {
      game.getPanel().increaseScore(2);
      resetBall();
      xSpeed *= -1;
    }
    else if (y < 0 || y > game.getHeight() - size) {
      ySpeed *= -1;
    }
    
		private boolean topOrBottom() {
			return 
		}
		
    checkCollision();
    
  }
  
  public void checkCollision() {
    if (
      game.getPanel().getPlayer(1).getBounds().intersects(getBounds()) 
    ||
      game.getPanel().getPlayer(2).getBounds().intersects(getBounds())
    ) xSpeed *= -1;
  }
  
  public Rectangle getBounds() {
    return new Rectangle(x, y, size, size);
  }
  
  public void paint(Graphics2D g) {
    g.fillRect(x, y, size, size);
  }
  
}


public class Paddle {
  private int xSize, ySize;
  private Pong game;
  private int up, down;
  private int x, y, ySpeed;
  private int maxSpeed;
  
  public int score = 0;
  
  public Paddle (int player, Pong game, int up, int down, int x, 
      int xSize, int ySize, int maxSpeed) {
    this.game = game;
    this.x = x;
    y = game.getHeight() / 2;
    this.up = up;
    this.down = down;
    this.xSize = xSize;
    this.ySize = ySize;
    this.maxSpeed = maxSpeed;
    ySpeed = 0;
  }
  
  public void resetPaddle() {
    y = game.getHeight() / 2;
  }
  
  public void update() {
    if (isInBounds()) y += ySpeed;
    else if (y >= 0) y += ySpeed;
    else if (y <= game.getHeight()) y -= ySpeed;
  }
	
	private boolean isInBounds() {
		return y > 0 && y < game.getHeight() - ySize - game.YBORDER;
	}
	
  public void pressed(int keyCode) {
	  if (keyCode == up)
		  ySpeed = -maxSpeed;
	  else if (keyCode == down)
		  ySpeed = maxSpeed;
  }
  
  public void released(int keyCode) {
	  if (keyCode == up || keyCode == down)
		  ySpeed = 0;
  }
  
  public Rectangle getBounds() {
	  return new Rectangle(x, y, xSize, ySize);
  }
  
  public void paint(Graphics2D g) {
	  g.fillRect(x, y, xSize, ySize);
  }
  
  public void increaseScore() { score++; }
  
}


}//end of pong class
