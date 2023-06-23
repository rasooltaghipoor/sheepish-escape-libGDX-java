package com.baranagames.sheepishescape;


import sun.rmi.runtime.Log;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.screens.FirstScreen;
import com.baranagames.sheepishescape.screens.SecondScreen;
import com.baranagames.sheepishescape.screens.SplashScreen;

public class MyFirstGame extends Game {
	
	public static boolean SOUNDS_ON;
	public static float VOLUME;
	public static float VIEWPORT_WIDTH;
	public static float VIEWPORT_HEIGHT = 60;
	public static boolean PLAYER_READY = true;
	public enum GameState {RUNNING,PAUSE,GAMEOVER,GAMEWIN};
	public static GameState gameState = GameState.RUNNING; 	
    public static int TOTAL_SCORE;
    public static boolean IS_PREMIUM , IS_PREMIUM_TMP;
    public static int buyLevel = 13;
    public static ActionResolver actionResolver;
	
    public MyFirstGame(ActionResolver googleServices)
	{
		super();
		actionResolver = googleServices;
	}
    
	public void create() {
		
		// TODO Auto-generated method stub
		  IS_PREMIUM = false;
		  VOLUME = 0.4f;
		  FileHandle file = Gdx.files.local("Sheepish.txt");
		  if(!file.exists()) {
			  String initFile = "0;0;1;0;1;0;";
			  // useless ; premium ; sndOn ; score ; current level  ; current level score
	          file.writeBytes(initFile.getBytes(), false);
	          SOUNDS_ON = true;	
	          TOTAL_SCORE = 0;	          
		  }		
		  else{			
			  String file_content[] = file.readString().split(";");			  
			  if(file_content[1].equals("1"))
				  IS_PREMIUM = true;
			  TOTAL_SCORE = Integer.parseInt(file_content[3]);
			  if(file_content[2].equals("1"))  { 
				  SOUNDS_ON = true;				 
			  }
			  else {
				  SOUNDS_ON = false;
				  VOLUME = 0;
			  }
		  }		 		 	 
		  Assets.load();	
		 // Assets.setAssets();
		  setScreen(new SplashScreen(this));		
	}
	
	public void render() {
        super.render(); //important!
    }

	@Override
	public void dispose() {
		Assets.dispose();			
		//System.out.println("ssss");67
	}	
	
}

