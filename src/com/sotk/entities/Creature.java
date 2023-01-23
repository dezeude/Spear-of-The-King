package com.sotk.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.sotk.managers.TileMap;

public abstract class Creature extends Entity {
	protected boolean top = false, bottom = false, left = false, right = false;
	protected int bw, bh, health;
	private int TILELENGTH = TileMap.TILELENGTH;
	protected float velx,vely;
	protected float gravity = 0.45f;
	protected boolean facingRight = true, inAnimation = false, invincible = false, alive = true, attacking = false;
	protected String[] extras;
	
	@Override
	public abstract void update();

	@Override
	public abstract void render(Graphics g);

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
	
	public Rectangle getBounds() {
		return new Rectangle(bx,by,bw,bh);
	}
	
	public void move(int vx, int vy) {
		
		//get all the possible tiles in a list
		ArrayList<Rectangle> hitList = new ArrayList();
		for(int i = by / TILELENGTH - 2; i < (by + bh) / TILELENGTH + 2; i++) {
			for(int j = bx / TILELENGTH - 2; j < (bx + bw) / TILELENGTH + 2; j++) {
				if(i >= 0 && i < TileMap.map.length && j >= 0 && j < TileMap.map[i].length && TileMap.map[i][j] - 1 > -1)
					hitList.add(new Rectangle(j*TILELENGTH,i*TILELENGTH,TILELENGTH,TILELENGTH));
			}
		}

		//calculate collisions on the x-axis first
		bx += vx;
		Rectangle newBounds = new Rectangle(bx, by, bw, bh);
		for(Rectangle r: hitList) {
			if(newBounds.intersects(r)) {
//				System.out.println("collision found on x-axis");
				if(vx > 0) { //if the tile is on the right of the player
					bx = r.x-newBounds.width;
					right = true;
					velx = 0;
				}
				if(vx < 0) {//if the tile is on the left of the player
					bx = r.x+r.width;
					left = true;
					velx = 0;
				}
			}			
		}
							
						
		//calculate collisions on the y-axis second
		by += vy;
		newBounds = new Rectangle(bx, by, bw, bh);
		for(Rectangle r: hitList) {
			if(newBounds.intersects(r)) {
//				System.out.println("collision found on y-axis");
				if(vy > 0) { //if the tile is under the player
					by = r.y-newBounds.height;
					bottom = true;
				}
				if(vy < 0) {//if the tile is above the player
					by = r.y+r.height;
					top = true;
					vely = 0;
				}
			}
		}
		
		
	}
	
	public int getHealth() {
		return health;
	}
	
	public void damage(int dmg) {
		if(!invincible) {
			invincible = true;
			inAnimation = true;
			//play the damage animation
			health -= dmg;
			takeHit();
			if(health <= 0) {
				die();
			}
		}
		
			
	}
	
	public void die() {
		inAnimation = true;
		alive = false;
	}
	
	public abstract void takeHit();
	
//	public abstract void attack(Rectangle hitbox, int damage);
	public abstract void attack();
	//add a hitbox to the level with the damage
	
	public void addExtras(String[] extras) {
		this.extras = extras;
	}
}


