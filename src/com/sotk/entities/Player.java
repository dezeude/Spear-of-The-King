package com.sotk.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.sotk.levels.Level;
import com.sotk.main.GamePanel;
import com.sotk.main.Launcher;
import com.sotk.managers.Animation;
import com.sotk.managers.Camera;
import com.sotk.managers.KeyManager;

public class Player extends Creature {
	private int renderWidth = 150, renderHeight = 150;
	private int xOff = 64,yOff = 57; //bounds x and y offsets, with the width and height 
	private float maxSpeed = 5.0f;
	private int timeSinceJumped = 0;
	private boolean isJumping = false;
	

	KeyManager keyManager;
	
	//animations
//	AnimationManager animationManager;
	Animation idle;
	Animation run;
//	Animation attack1;
	Animation attack2;
	Animation jump;
	Animation fall;
	Animation curAnim;
	Animation takeHit;
	Animation death;
	BufferedImage curFrame;
	
	boolean inAnimation = false; //for important animations that cannot be cancelled until finished
//	boolean attackQ = false;
	
	long lastClicked;
	
	Level level;
	
	public Player(int x, int y, Level level) {
		//make the width and height of the gamePanel static
		this.bx = x;
		this.by = y;
		bw = 20;
		bh = 40;
		health = 3;
		keyManager = KeyManager.getInstance();
		Camera.setDivisor(10);
		this.level = level;
//		animationManager = new AnimationManager();
		
		top = false;
		bottom = false;
		left = false;
		right = false;
		loadAnimations();
	}
	
	public void loadAnimations() {
		//load the idle animation
		idle = new Animation("/animations/player/Idle.png", 8, 0.1f);		
		
		//load the running animation
		run = new Animation("/animations/player/Run.png", 8, 0.2f);
		
		//load the attack1 animation
//		attack1 = new Animation("/animations/player/Attack1.png", 5, 0.3f);
		
		
		//load the attack2 animation
		attack2 = new Animation("/animations/player/Attack2.png", 5, 0.4f);
		attack2.setAttackFrame(3, 85, 37, 40, 60);
		
		jump = new Animation("/animations/player/Jump.png", 2, 0.2f);
		
		fall = new Animation("/animations/player/Fall.png", 2, 0.2f);
		
		takeHit = new Animation("/animations/player/Take hit.png", 3, 0.1f);
		death = new Animation("/animations/player/Death.png", 8, 0.1f);
	}
	
	
	public void processInput() { 
//		velx = 0;
		if(keyManager.getKey(KeyEvent.VK_A)) {
			if(!inAnimation || attacking) {
				if(!attacking)
					facingRight = false;
				velx += -5;
				if(velx < maxSpeed)
					velx = -maxSpeed;
			}
			
		}
			
		if(keyManager.getKey(KeyEvent.VK_D)) {
			if(!inAnimation || attacking) {
				if(!attacking)
					facingRight = true;
				velx += 5;
				if(velx > maxSpeed)
					velx = maxSpeed;
			}
			
		}
			
		if(keyManager.getKey(KeyEvent.VK_W)) { //if w is being pressed
//			System.out.println(System.currentTimeMillis());
			if(!invincible) {
				if(bottom) { //if there is a tile under the player
					timeSinceJumped = 0;
					vely = -9;
					bottom = false;
					isJumping = true;
				}
				else { //if the player is in the air
					timeSinceJumped++;
					if(timeSinceJumped <= 20 && isJumping) {
						vely -= 0.3;
					}
					else {
						timeSinceJumped = 0;
						isJumping = false;
					}
					
				}
			}
				
		}
		if(!keyManager.getKey(KeyEvent.VK_W) && !bottom)
// if the w isn't held and the player is in the air.
			isJumping = false;
// the player isn't jumping anymore			
		
		if(bottom) {//if the player is standing on a tile
			//change the y velocity so the player falls slower
			vely = 1;
		}
		else {//if player is falling
			
			vely += gravity;
			//apply gravity
		}
		if(velx > 0) { //player moving right
			if(velx <= 1)
				velx = 0;
			else
			velx--;
		} 
		if(velx < 0) { //player moving left
			if(velx >= -1)
				velx = 0;
			else
			velx++;
		}
		
		top = false;
		bottom = false;
		left = false;
		right = false;
		
		move((int)velx,(int)vely); //move the player and check for collisions
		
	}	
	
	@Override
	public void update() {
		if(alive) {
			
		processInput();
		Camera.smoothTo(bx+bw/2,by-bh/2);//center player in the middle of the screen.
		
		if(inAnimation) {
			if(invincible) {
				curAnim = takeHit;
				if(takeHit.getIndex() == takeHit.length() - 1) {
					resetAnims();
					takeHit.reset();
				}
				
			}
			else if(attacking) {
				//change the player's Frame depending on the state of the player
				curAnim = attack2;
				if(attack2.getIndex() == attack2.length() - 1) {
					resetAnims();
					attack2.reset();
				}	
				
			}
			else
				resetAnims();
			
		}
		else {
			if(bottom) { //if the player is on a tile/on the ground
				if(velx == 0) {
					curAnim = idle;
				}
				else {
					curAnim = run;
				}
			}
			else {
				if(vely < 0) { //player is ascending
					curAnim = jump;
				}
				else {
					curAnim = fall;
				}
				
			}
			
		}
		
		}
		else {
			curAnim = death;
			if(death.getIndex() == death.length() - 1) 
				death.lock();
		}
	
		
		if(facingRight) {
			curFrame = curAnim.getCurFrame();
			
			if(curAnim.isAttackFrame()) { //if the attack frame exists
				Rectangle curAttackFrame = curAnim.getAttackFrame();
				Rectangle newB = new Rectangle(bx - xOff + curAttackFrame.x,
											   by - yOff + curAttackFrame.y,
											   curAttackFrame.width,
											   curAttackFrame.height);
//				System.out.println(curAttackFrame);
				level.damageEnemies(newB, 1);
			}
		}
		else {
			curFrame = curAnim.getMirrorFrame();
			
			if(curAnim.isAttackFrame()) { //if the attack frame exists
				Rectangle curAttackFrame = curAnim.getAttackFrame();
				Rectangle newB = new Rectangle(bx - xOff + curAttackFrame.x - bw - curAttackFrame.width,
											   by - yOff + curAttackFrame.y,
											   curAttackFrame.width,
											   curAttackFrame.height);
//				System.out.println(curAttackFrame);
				level.damageEnemies(newB, 1);
			}
		}
		
		curAnim.play();
		

	}
	
	@Override
	public void render(Graphics g) {
		
		//draw the player
		g.drawImage(curFrame, bx - xOff - Camera.getXOffset(), by - yOff - Camera.getYOffset(), renderWidth, renderHeight, null);
	}
	
	public void resetAnims() {
		attacking = false;
		inAnimation = false;
		invincible = false;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(bx,by,bw,bh);
	}
	
	public double getDist(Rectangle other) {
		return Point2D.distance(bx + bw/2,
								by + bh/2,
								other.x + other.width/2,
								other.y + other.height/2);
	}
	
	public void attack() {
		if(!attacking && !inAnimation) {//if the player is not attacking
			attacking = true;
			inAnimation = true;
		}
			
		else { //if the player is attacking
			//let the player attack depending on how long ago the player clicked
			lastClicked = System.nanoTime();
		}
		
		
			
	}
	
	public float getVelX() {
		return velx;
	}
	
	public float getVelY() {
		return vely;
	}


	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return bx;
	}


	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return by;
	}


	@Override
	public void die() {
		resetAnims();
		inAnimation = true;
		alive = false;
	}

	@Override
	public void takeHit() {
		attacking = false;
		invincible = true;
		inAnimation = true;
		attack2.reset();
	}
	
	public boolean isAlive() {
		return alive;
	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub
		//Do Nothing. Can't Interact with player.
	}


}
