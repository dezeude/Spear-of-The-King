package com.sotk.entities;

public abstract class Entity {
	protected int bx, by;
	public abstract void update();
	public abstract void render(java.awt.Graphics g);
	public abstract void interact();
	public abstract int getX();
	public abstract int getY();
	public abstract void addExtras(String[] extras);
}
