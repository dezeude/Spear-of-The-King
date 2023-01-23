package com.sotk.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.sotk.managers.KeyManager;
import com.sotk.states.GameState;
import com.sotk.states.State;

public class GamePanel extends JPanel implements Runnable, MouseListener, KeyListener{
	public static int Width, Height;
	private Thread thread;
	private boolean running;
	private State gameState;
	private KeyManager keyManager;
	private Graphics gOffscreen;
	//offscreen graphics
	private static BufferedImage offScreenBuffer;
	//the buffer that is scaled and painted to the screen/Panel.
	
	public GamePanel(int width, int height) {
		super();
		Width = width;
		Height = height;
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.black);
		init();
	}
	
	public void init() {
		addMouseListener(this);
		addKeyListener(this);
		offScreenBuffer = new BufferedImage(32*25, 32*14, BufferedImage.TYPE_INT_ARGB);
		running = true;
		gameState = new GameState(this);
		keyManager = KeyManager.getInstance();
		thread = new Thread(this);
		thread.start();
	}	
	
	@Override
	public void run() {
//		int fps = 60;
//		long start;
//		long elapsed;
//		long wait;
//		long targetTime = 1000 / fps;
		
		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while(running) {
//			start = System.nanoTime();
//			
//				update();
//				repaint();
//				
//			elapsed = System.nanoTime() - start;
//			System.out.println("FPS: " + elapsed / 1000);
//			
//			wait = targetTime - elapsed / 1000000;
//			
//			if(wait < 0) wait = 5;
//			try {
//				Thread.sleep(wait);
//			} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			if(delta >= 1){
				update();
				repaint();
				ticks++;
				delta--;
			}
			
			if(timer >= 1000000000){
				System.out.println("Ticks and Frames: " + ticks);
				ticks = 0;
				timer = 0;
			}
		}
		
	}
	
	public void update() {
//		System.out.println();
		gameState.update();	
	}
	
	public static int getWindowWidth() {
		return offScreenBuffer.getWidth();
	}
	
	public static int getWindowHeight() {
		return offScreenBuffer.getHeight();
	}
	
//	public void drawToOffScreen() {
//		gOffscreen = offScreenBuffer.getGraphics();
//		gOffscreen.setColor(Color.white);
//		gOffscreen.fillRect(0, 0, offScreenBuffer.getWidth(), offScreenBuffer.getHeight());
//		gameState.render(gOffscreen);
//		gOffscreen.dispose();
//	}
//	
//	public void drawToScreen() {
//		gScreen = getGraphics();
//		gScreen.fillRect(0, 0, Width, Height);
//		gScreen.drawImage(offScreenBuffer, 0, 0, Width, Height, null);
//		gScreen.dispose();
//		
//	}
	
	@Override
	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
		
		gOffscreen = offScreenBuffer.getGraphics(); 
		gOffscreen.setColor(Color.white);
//		gOffscreen.fillRect(0, 0, offScreenBuffer.getWidth(), offScreenBuffer.getHeight());
		gameState.render(gOffscreen);
		gOffscreen.dispose();

		g.fillRect(0, 0, Width, Height);
		g.drawImage(offScreenBuffer, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		
		//which is better? getGraphics() or paintComponent?
		//Answer: getGraphics doesn't work lol. Graphics is always null
//		Graphics g2 = getGraphics();
//		g2.setColor(Color.red);
//		g2.fillRect(0, 0, 200, 200);
//		g2.dispose();
	}	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
//		System.out.println("cliek");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		gameState.mousePressed(e.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		gameState.mouseReleased(e.getButton());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
//		if(key == KeyEvent.VK_W) {
//			keyManager.keyPressed(key);
//		}
//		if(key == KeyEvent.VK_S) {
//			keyManager.keyPressed(key);
//		}
//		if(key == KeyEvent.VK_A) {
//			keyManager.keyPressed(key);
//		}
//		if(key == KeyEvent.VK_D) {
//			keyManager.keyPressed(key);
//		}
		if(key == KeyEvent.VK_ESCAPE)
			System.exit(0);
//		if(key == KeyEvent.VK_SPACE) {
			keyManager.keyPressed(key);
//		}
		
		gameState.keyPressed(key);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_W) {
			keyManager.keyReleased(key);
		}
		if(key == KeyEvent.VK_S) {
			keyManager.keyReleased(key);
		}
		if(key == KeyEvent.VK_A) {
			keyManager.keyReleased(key);
		}
		if(key == KeyEvent.VK_D) {
			keyManager.keyReleased(key);
		}
		
		gameState.keyReleased(key);
	}
	
}
