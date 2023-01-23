package com.sotk.main;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.sotk.managers.AssetsManager;
import com.sotk.managers.TileMap;

import java.awt.*;

public class Launcher {
	
	private static JFrame frame;
	
	public static void main(String[] args) {
//		System.out.println(TileMap.class.getResource("/").getPath());
//		String classpath = System.getProperty("java.class.path");
//        String[] classPathValues = classpath.split(File.pathSeparator);
//        for (String classPath: classPathValues) {
//            System.out.println(classPath);
//        }
		
		
		int width = 32 * 23;//32 is the TileLength
		int height = 32 * 13;
		GamePanel gp = new GamePanel(width,height);
		frame = new JFrame("Spear of the King");
		frame.requestFocusInWindow();
		 //add JFrame component to frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setResizable(false);
//		frame.pack();
//		frame.setUndecorated(true);
		
		frame.setVisible(true);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(gp);
		frame.addKeyListener(gp);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	public static void changeSize(int width, int height) {
		frame.setSize(width,height);
		frame.setLocationRelativeTo(null);
	}
	
	public static void fullScreen() {
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	public static void actualFullScreen() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(frame);
	}
	

}
