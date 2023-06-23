package com.baranagames.sheepishescape.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.baranagames.sheepishescape.MyFirstGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		//LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
				//new LwjglApplication(new MyFirstGame(), config);
				LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
				//cfg.useGL30 = false;
				cfg.title = "Sheepish Escape";
			    cfg.width =1024;
			    cfg.height =600;
				
				cfg.forceExit = true;
				cfg.vSyncEnabled = false;
				cfg.allowSoftwareMode = true;
				cfg.resizable = false;				
				
				//System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");			
				new LwjglApplication(new MyFirstGame(new ActionResolverDesktop()), cfg);
			    //Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);	
	}
}
