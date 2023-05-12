package com.sotk.main;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.sotk.managers.AssetsManager;
import com.sotk.managers.TileMap;

public class Launcher {
	
	public static JFrame frame;
	
	public static void main(String[] args) {
		Image icon = AssetsManager.loadImage("/Spear.png");
		frame = new JFrame("Spear of the King");
		frame.setIconImage(icon);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setResizable(true);
		
		GamePanel gp = new GamePanel();
		frame.setContentPane(gp);
		frame.addKeyListener(gp);		
		frame.setLayout(null);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	

}
