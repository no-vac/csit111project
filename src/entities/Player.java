package entities;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.glass.events.KeyEvent;

import csit111project.GamePanel;
import gamestate.State;
import objects.Block;
import objects.EndGate;
import physics.Collision;

public class Player{

	BufferedImage RightI, LeftI, RightR, LeftR, RightJ, LeftJ;
	private boolean right = false, left = false, jumping = false, falling = false;

	private boolean topCollision = false;
	private boolean FacingRight=true;
	private boolean FacingLeft=false;
	//private int state; //0 is idle right, 1 is idle left ,2 is running right, 3 is running left
	
	private double x, y;
	private int width, height;

	private double moveSpeed = 5;

	private double maxJumpSpeed = 5;
	private double currentJumpSpeed = maxJumpSpeed;

	private double maxFallSpeed = 5;
	private double currentFallSpeed = 0.1;
	public boolean win=false;

	
	/*
	 * INITIALIZATION
	 */
	public void init(){
		try{
			RightI = ImageIO.read(getClass().getResourceAsStream("/images/Charassets/actions/idle/right/Charidle01.png"));
			LeftI = ImageIO.read(getClass().getResourceAsStream("/images/Charassets/actions/idle/left/CharidleL01.png"));
			RightR = ImageIO.read(getClass().getResourceAsStream("/images/Charassets/actions/idle/right/Charidle02.png"));
			LeftR = ImageIO.read(getClass().getResourceAsStream("/images/Charassets/actions/idle/left/CharidleL02.png"));
			LeftJ = ImageIO.read(getClass().getResourceAsStream("/images/Charassets/actions/idle/left/CharidleL03.png"));
			RightJ = ImageIO.read(getClass().getResourceAsStream("/images/Charassets/actions/idle/right/Charidle03.png"));
			
		}catch (IOException e){
		}
	}
	
	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public int getFallSpeed() {
		return (int) currentFallSpeed;
	}

	public int getJumpSpeed() {
		return (int) currentJumpSpeed;
	}

	/*
	 * PLAYER METHOD
	 */
	public Player(int width, int height) {
		x = GamePanel.WIDTH / 2;
		y = GamePanel.HEIGHT / 2;
		this.width = width;
		this.height = height;
		init();
		
	}

	/*
	 * PLAYER UPDATE METHOD
	 */
	public void update(Block[] b, EndGate endGate) {

		int iX = (int) x;
		int iY = (int) y;
		
		for (int i = 0; i < b.length; i++) {

			// right
				//BlockCollision
			for (int j = 0; j <= height; j++) {
				if (Collision.playerBlock(new Point(iX + width + (int) State.xOffset+2, iY + (int) State.yOffset + j - 1),
						b[i])) {
					right = false;
					// }else{
					// if(!leftCollision && right)
					// right=true;
				}
				//EndGate Collision
				if(Collision.playerGate(new Point(iX + width + (int) State.xOffset, iY + (int) State.yOffset + j - 1), endGate)){
					win=true;
				}
			}

			// left
				//Block Collision
			for (int j = 0; j < height; j++) {
				if (Collision.playerBlock(new Point(iX + (int) State.xOffset+1, iY + (int) State.yOffset + j),b[i])) {
					left = false;
					// }else{
					// if(!rightCollision && !left)
					// left=true;

				}
				//EndGate Collision
				if (Collision.playerGate(new Point(iX + (int) State.xOffset - 1, iY + (int) State.yOffset + j),endGate)) {
					win=true;

				}
				
			}

			// top
				//Block Collision
			for (int j = 0; j < width; j++) {
				if (Collision.playerBlock(new Point(iX + (int) State.xOffset + j+2, iY + (int) State.yOffset), b[i])) {
					jumping = false;
					currentJumpSpeed = maxJumpSpeed;
					falling = true;
				}
				//EndGateCollision
				if (Collision.playerGate(new Point(iX + (int) State.xOffset + j, iY + (int) State.yOffset), endGate)) {
					win=true;
				}
			}

			// bottom
				//Block Collision
			for (int j = 0; j < width-1; j++) {
				if (Collision.playerBlock(
						new Point(iX + (int) State.xOffset + j+2, iY + height + (int) State.yOffset + 1), b[i])) {
					State.yOffset = b[i].getY() - height-y;
					falling = false;
					topCollision = true;
				} else {
					if (!topCollision && !jumping)
						falling = true;
				}
				
				//EndGate Collision
				if (Collision.playerGate(
						new Point(iX + (int) State.xOffset + j, iY + height + (int) State.yOffset + 1), endGate)) {
					win=true;
				}
			}
		}
		topCollision = false;

		/*
		 * Movement Mechanics (temporary)
		 */
		if (right)
			State.xOffset += moveSpeed;
			
		if (left)
			State.xOffset -= moveSpeed;

		/*
		 * Jumping/Falling Mechanics
		 */
		if (jumping) {
			State.yOffset -= currentJumpSpeed;
			currentJumpSpeed -= .1;
			falling = false;

			if (currentJumpSpeed <= 0) {
				currentJumpSpeed = maxJumpSpeed;
				jumping = false;
				falling = true;
			}
		}

		if (falling) {
			State.yOffset += currentFallSpeed;
			if (currentFallSpeed <= maxFallSpeed) {
				currentFallSpeed += 0.1;
				
				
			}
		}

		if (!falling) {
			currentFallSpeed = .1;
		}
	}

	
	/*
	 * PLAYER RENDER METHOD
	 */
	public void render(Graphics2D g) {
		//character graphics
		//g.setColor(Color.WHITE);
		if (FacingRight){
			g.drawImage(RightI, (int) x, (int) y, null);
		}if(FacingLeft){
			g.drawImage(LeftI, (int) x, (int) y, null);
		}if(right){
			g.drawImage(RightR,(int) x,(int) y, null);
		}if(left){
			g.drawImage(LeftR,(int) x,(int) y, null);
		}if(jumping==true||falling==true){
			if (FacingRight){
				g.drawImage(RightJ,(int) x,(int) y, null);
			}if(FacingLeft){
				g.drawImage(LeftJ,(int) x,(int) y, null);
			}
		}
		
		g.drawRect((int) x, (int) y, width, height);
		
	}

	
	/*
	 * KEY EVENTS
	 */
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT){ // go right pressed
			right = true;
			//state = 2;
			FacingLeft = false;
			FacingRight = true;
			//ImageIcon RunR = new ImageIcon("Charidle02.png");
			//character = RunR.getImage();
		}
			
		if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT){ // go left pressed
			left = true;
			//state = 3;
			FacingRight = false;
			FacingLeft = true;
		}
			
		if ((k == KeyEvent.VK_W || k == KeyEvent.VK_UP) && !jumping && !falling){ //jump pressed
			jumping = true;
		}
	}

	public void keyReleased(int k) {
		if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT){// go right released
			right = false;
			//state = 0;
			FacingRight=true;
			FacingLeft = false;
	}
		if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT){ // go left released
			left = false;
			//state = 1;
			FacingRight=false;
			FacingLeft = true;
	}
	}
}
