package com.baranagames.sheepishescape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

//Referenca za Tile Sprajtove http://www.netthreads.co.uk/2012/01/31/libgdx-example-of-using-scene2d-actions-and-event-handling/

public class Assets {
	public static Skin skin;
	public static Music ingameMusic, mainMusic , creditMusic , CommentMusic;
	public static Sound selectionSnd , pauseSnd , changeSnd , goalSnd, goalSnd2, lostSnd, winSnd, rtkickerSnd, vkickerSnd;
	public static Sound agentSnd, explodeSnd, grassSnd, manSnd, womanSnd, stickSnd, snakeSnd , buttonSnd;
	public static Sound sheepSnd , cowSnd , donkeySnd , chickSnd , goatSnd,horseSnd,dogSnd, frogSnd, wolfSnd;
	
	public static TextureAtlas textureAtlas , sharedAtlas;
	public static TextureRegion SHEEPS[] = new TextureRegion[9];// ball , ball1 , ball2 , sheep , cage , wolfsheep , line, line2, indicator;
	public static TextureRegion GAURDS[] = new TextureRegion[28];//cow, donkey, horse, goat, dog, agent, forg, grass, sticky, wolf, man, woman, snake, chick,  trick;
	public static TextureRegion BUTTONS[] = new TextureRegion[5];//menu , reset , next , resume , pause; play , match , credits , help , exit , back , forward , snd;
	public static TextureRegion BUTTONS2[] = new TextureRegion[11];
	public static TextureRegion HUDS[]= new TextureRegion[22];//dig0, dig1, dig2 , dig3 , dig4, dig5 , dig6 , dig7 , dig8 , dig9, dig10,board , board1, board2, happy, sideSheep , direction;
	public static TextureRegion TOWERS[]= new TextureRegion[6];//rotator , rtkicker , leg , alvar , mosht,gateArea
    public static TextureRegion	OTHERS[] = 	new TextureRegion[21];	// sun , clouds , levelselection;
    // *********** ALL IMAGES STORED IN ARRAYS ABOVE EXCEPT BORDERS , GATE , CREDITS and energybar   *************
    private static String atlasName = "";
	private static AssetManager manager;//= new AssetManager();
			
	//static int counter = 0;
	public static void load () {
		try
			{							   
			    //System.out.println(Gdx.graphics.getDesktopDisplayMode().width);
				int scrHeight = Gdx.graphics.getHeight(); 
				//if(Gdx.app.getType() == ApplicationType.Desktop)
					//scrHeight = Gdx.graphics.getDesktopDisplayMode().width;
				
				if(scrHeight > 1600)
					atlasName = "images4.atlas";
				else if(scrHeight > 1200) 
					atlasName = "images3.atlas";
				else if(scrHeight > 800)
					atlasName = "images2.atlas";
				else 
					atlasName = "images1.atlas";	
				
				//**********ImgAssets.clear();
				manager = new AssetManager();
											
				manager.load("data/uiskin.json",Skin.class);
				manager.load(atlasName, TextureAtlas.class);
				manager.load("sharedImgs.atlas", TextureAtlas.class);
				manager.load("sounds/mainmenu.mp3", Music.class);				
				manager.load("sounds/ingame.mp3", Music.class);	
				manager.load("sounds/credits.mp3", Music.class);
				manager.load("sounds/comment.mp3", Music.class);
				manager.load("sounds/selection.mp3", Sound.class);
				manager.load("sounds/pause.mp3", Sound.class);
				manager.load("sounds/change.mp3", Sound.class);
				manager.load("sounds/goal.mp3", Sound.class);
				manager.load("sounds/goal2.mp3", Sound.class);
				manager.load("sounds/lost.mp3", Sound.class);
				manager.load("sounds/rtkicker.mp3", Sound.class);
				manager.load("sounds/vkicker.mp3", Sound.class);
				manager.load("sounds/winner.mp3", Sound.class);
				manager.load("sounds/button.mp3", Sound.class);
				
				manager.load("sounds/sheep.mp3", Sound.class);
				manager.load("sounds/cow.mp3", Sound.class);
				manager.load("sounds/donkey.mp3", Sound.class);
				manager.load("sounds/chick.mp3", Sound.class);
				manager.load("sounds/goat.mp3", Sound.class);
				manager.load("sounds/horse.mp3", Sound.class);
				manager.load("sounds/dog.mp3", Sound.class);
				manager.load("sounds/frog.mp3", Sound.class);
				manager.load("sounds/wolf.mp3", Sound.class);
				manager.load("sounds/agent.mp3", Sound.class);
				manager.load("sounds/explode.mp3", Sound.class);
				manager.load("sounds/grass.mp3", Sound.class);
				manager.load("sounds/man.mp3", Sound.class);
				manager.load("sounds/snake.mp3", Sound.class);
				manager.load("sounds/sticky.mp3", Sound.class);
				manager.load("sounds/woman.mp3", Sound.class);				
				
			}catch(Exception ex){				
			}
		
	}
	
	public static void setAssets() {
		//manager.finishLoading();
		skin = manager.get("data/uiskin.json",Skin.class);
		
		// images
		textureAtlas = manager.get(atlasName, TextureAtlas.class);
		sharedAtlas = manager.get("sharedImgs.atlas",TextureAtlas.class);
		
		SHEEPS[0] = new TextureRegion(textureAtlas.findRegion("ball"));
		SHEEPS[1] = new TextureRegion(textureAtlas.findRegion("ball1"));
		SHEEPS[2]= new TextureRegion(textureAtlas.findRegion("ball2"));
		SHEEPS[3] = new TextureRegion(textureAtlas.findRegion("sheep"));
		SHEEPS[4] = new TextureRegion(textureAtlas.findRegion("cage"));
		SHEEPS[5] = new TextureRegion(textureAtlas.findRegion("wolfsheep"));
		SHEEPS[6] = new TextureRegion(textureAtlas.findRegion("line"));
		SHEEPS[7] = new TextureRegion(textureAtlas.findRegion("line2"));
		SHEEPS[8] = new TextureRegion(textureAtlas.findRegion("indicator"));
		
		GAURDS[0] = new TextureRegion(textureAtlas.findRegion("cow"));
		GAURDS[1] = new TextureRegion(textureAtlas.findRegion("cow1"));
		GAURDS[2]= new TextureRegion(textureAtlas.findRegion("donkey"));
		GAURDS[3] = new TextureRegion(textureAtlas.findRegion("donkey1"));
		GAURDS[4] = new TextureRegion(textureAtlas.findRegion("horse"));
		GAURDS[5] = new TextureRegion(textureAtlas.findRegion("horse1"));
		GAURDS[6] = new TextureRegion(textureAtlas.findRegion("goat"));
		GAURDS[7] = new TextureRegion(textureAtlas.findRegion("goat1"));
		GAURDS[8] = new TextureRegion(textureAtlas.findRegion("dog"));
		GAURDS[9] = new TextureRegion(textureAtlas.findRegion("dog1"));
		GAURDS[10] = new TextureRegion(textureAtlas.findRegion("agent"));
		GAURDS[11]= new TextureRegion(textureAtlas.findRegion("agent1"));
		GAURDS[12]= new TextureRegion(textureAtlas.findRegion("frog"));
		GAURDS[13] = new TextureRegion(textureAtlas.findRegion("frog1"));
		GAURDS[14] = new TextureRegion(textureAtlas.findRegion("grass"));
		GAURDS[15] = new TextureRegion(textureAtlas.findRegion("grass1"));
		GAURDS[16] = new TextureRegion(textureAtlas.findRegion("sticky"));
		GAURDS[17] = new TextureRegion(textureAtlas.findRegion("sticky1"));
		GAURDS[18] = new TextureRegion(textureAtlas.findRegion("wolf"));
		GAURDS[19]= new TextureRegion(textureAtlas.findRegion("wolf1"));
		GAURDS[20] = new TextureRegion(textureAtlas.findRegion("man"));
		GAURDS[21] = new TextureRegion(textureAtlas.findRegion("man1"));
		GAURDS[22]= new TextureRegion(textureAtlas.findRegion("woman"));
		GAURDS[23] = new TextureRegion(textureAtlas.findRegion("woman1"));
		GAURDS[24] = new TextureRegion(textureAtlas.findRegion("snake"));
		GAURDS[25] = new TextureRegion(textureAtlas.findRegion("snake1"));
		GAURDS[26] = new TextureRegion(textureAtlas.findRegion("chick"));
		//GAURDS[27] = new TextureRegion(textureAtlas.findRegion("gateArea"));
		GAURDS[27] = new TextureRegion(textureAtlas.findRegion("trick"));
		
		HUDS[0] = new TextureRegion(textureAtlas.findRegion("dig0"));
		HUDS[1] = new TextureRegion(textureAtlas.findRegion("dig1"));
		HUDS[2]= new TextureRegion(textureAtlas.findRegion("dig2"));
		HUDS[3] = new TextureRegion(textureAtlas.findRegion("dig3"));
		HUDS[4] = new TextureRegion(textureAtlas.findRegion("dig4"));
		HUDS[5] = new TextureRegion(textureAtlas.findRegion("dig5"));
		HUDS[6] = new TextureRegion(textureAtlas.findRegion("dig6"));
		HUDS[7] = new TextureRegion(textureAtlas.findRegion("dig7"));
		HUDS[8] = new TextureRegion(textureAtlas.findRegion("dig8"));
		HUDS[9] = new TextureRegion(textureAtlas.findRegion("dig9"));
		HUDS[10] = new TextureRegion(textureAtlas.findRegion("dig10"));
		HUDS[11]= new TextureRegion(textureAtlas.findRegion("scoreBoard"));
		HUDS[12]= new TextureRegion(textureAtlas.findRegion("scoreBoard1"));
		HUDS[13] = new TextureRegion(textureAtlas.findRegion("scoreBoard2"));
		HUDS[14] = new TextureRegion(textureAtlas.findRegion("goal"));
		HUDS[15] = new TextureRegion(textureAtlas.findRegion("sideSheep"));
		HUDS[16] = new TextureRegion(textureAtlas.findRegion("direction"));
		HUDS[17] = new TextureRegion(textureAtlas.findRegion("lost"));
		HUDS[18] = new TextureRegion(textureAtlas.findRegion("extra0"));
		HUDS[19] = new TextureRegion(textureAtlas.findRegion("extra1"));
		HUDS[20] = new TextureRegion(textureAtlas.findRegion("extra2"));
		HUDS[21] = new TextureRegion(textureAtlas.findRegion("extra3"));	
		
				
		BUTTONS[0] = new TextureRegion(textureAtlas.findRegion("menuButton"));
		BUTTONS[1] = new TextureRegion(textureAtlas.findRegion("resetButton"));
		BUTTONS[2]= new TextureRegion(textureAtlas.findRegion("nextButton"));
		BUTTONS[3] = new TextureRegion(textureAtlas.findRegion("resumeButton"));
		BUTTONS[4] = new TextureRegion(textureAtlas.findRegion("pauseButton"));
		
		BUTTONS2[0] = new TextureRegion(sharedAtlas.findRegion("playButton"));
		BUTTONS2[1] = new TextureRegion(sharedAtlas.findRegion("matchButton"));
		BUTTONS2[2] = new TextureRegion(sharedAtlas.findRegion("creditButton"));
		BUTTONS2[3] = new TextureRegion(sharedAtlas.findRegion("exitButton"));
		BUTTONS2[4] = new TextureRegion(sharedAtlas.findRegion("backButton"));
		BUTTONS2[5]= new TextureRegion(sharedAtlas.findRegion("forButton"));
		BUTTONS2[6]= new TextureRegion(sharedAtlas.findRegion("sndOn"));
		BUTTONS2[7]= new TextureRegion(sharedAtlas.findRegion("sndOff"));
		BUTTONS2[8]= new TextureRegion(sharedAtlas.findRegion("rateButton"));
		BUTTONS2[9]= new TextureRegion(sharedAtlas.findRegion("helpButton"));
		BUTTONS2[10]= new TextureRegion(sharedAtlas.findRegion("buyButton"));
		
		TOWERS[0] = new TextureRegion(textureAtlas.findRegion("rotator"));
		TOWERS[1] = new TextureRegion(textureAtlas.findRegion("rtkicker"));
		TOWERS[2]= new TextureRegion(textureAtlas.findRegion("leg"));
		TOWERS[3] = new TextureRegion(textureAtlas.findRegion("alvar"));
		TOWERS[4] = new TextureRegion(textureAtlas.findRegion("mosht"));
		TOWERS[5] = new TextureRegion(textureAtlas.findRegion("gateArea"));
		
		
		OTHERS[0] = new TextureRegion(sharedAtlas.findRegion("sun"));
		OTHERS[1] = new TextureRegion(sharedAtlas.findRegion("cloud1"));
		OTHERS[2]= new TextureRegion(sharedAtlas.findRegion("cloud2"));
		OTHERS[3] = new TextureRegion(sharedAtlas.findRegion("cloud3"));
		OTHERS[4] = new TextureRegion(sharedAtlas.findRegion("cloud4"));
		OTHERS[5] = null;
		OTHERS[6] = null;
		OTHERS[7] = new TextureRegion(sharedAtlas.findRegion("button"));
		OTHERS[8] = null;
		OTHERS[9] = new TextureRegion(sharedAtlas.findRegion("shareButton"));
		OTHERS[10] = new TextureRegion(sharedAtlas.findRegion("stage2a"));
		OTHERS[11] = new TextureRegion(sharedAtlas.findRegion("stage2b"));
		OTHERS[12] = new TextureRegion(sharedAtlas.findRegion("stage2c"));
		OTHERS[13] = new TextureRegion(sharedAtlas.findRegion("stage2d"));
		OTHERS[14] = new TextureRegion(sharedAtlas.findRegion("stage2Select"));		
		OTHERS[15] = new TextureRegion(sharedAtlas.findRegion("season1"));
		OTHERS[16] = new TextureRegion(sharedAtlas.findRegion("season2"));
		OTHERS[17] = new TextureRegion(sharedAtlas.findRegion("season3"));
		OTHERS[18] = new TextureRegion(sharedAtlas.findRegion("season4"));
		OTHERS[19] = new TextureRegion(sharedAtlas.findRegion("season5"));
		OTHERS[20] = new TextureRegion(sharedAtlas.findRegion("direction2"));
				
			
		// sounds
		ingameMusic = manager.get("sounds/ingame.mp3", Music.class);
		mainMusic = manager.get("sounds/mainmenu.mp3", Music.class);
		creditMusic = manager.get("sounds/credits.mp3", Music.class);
		CommentMusic = manager.get("sounds/comment.mp3", Music.class);
		selectionSnd = manager.get("sounds/selection.mp3", Sound.class); 
		pauseSnd = manager.get("sounds/pause.mp3", Sound.class);
		changeSnd = manager.get("sounds/change.mp3", Sound.class);
		goalSnd = manager.get("sounds/goal.mp3", Sound.class);
		goalSnd2 = manager.get("sounds/goal2.mp3", Sound.class);
		lostSnd = manager.get("sounds/lost.mp3", Sound.class);
		winSnd = manager.get("sounds/winner.mp3", Sound.class);
		rtkickerSnd = manager.get("sounds/rtkicker.mp3", Sound.class);
		vkickerSnd = manager.get("sounds/vkicker.mp3", Sound.class);
		buttonSnd = manager.get("sounds/button.mp3", Sound.class);
		
	    sheepSnd = manager.get("sounds/sheep.mp3", Sound.class); 
	    cowSnd = manager.get("sounds/cow.mp3", Sound.class); 
	    donkeySnd = manager.get("sounds/donkey.mp3", Sound.class); 
	    chickSnd =  manager.get("sounds/chick.mp3", Sound.class);
		goatSnd = manager.get("sounds/goat.mp3", Sound.class);
		horseSnd = manager.get("sounds/horse.mp3", Sound.class);
		dogSnd = manager.get("sounds/dog.mp3", Sound.class);
		frogSnd = manager.get("sounds/frog.mp3", Sound.class);
		wolfSnd = manager.get("sounds/wolf.mp3", Sound.class);		
		agentSnd = manager.get("sounds/agent.mp3", Sound.class);
		explodeSnd = manager.get("sounds/explode.mp3", Sound.class);
		grassSnd = manager.get("sounds/grass.mp3", Sound.class);
		manSnd = manager.get("sounds/man.mp3", Sound.class);
		womanSnd = manager.get("sounds/woman.mp3", Sound.class);
		stickSnd = manager.get("sounds/sticky.mp3", Sound.class);
		snakeSnd = manager.get("sounds/snake.mp3", Sound.class);
			//textureAtlas = new TextureAtlas(ImgAssetList[0]);		
	        
		/*/*********for(int i=1; i<ImgAssetList.length; i++)
			ImgAssets.add(textureAtlas.findRegion(ImgAssetList[i]));	*/
			
		//skin = new Skin(Gdx.files.internal("data/uiskin.json"));
	}
	
	public static void dispose() {
		//skin.dispose();
		//***********ImgAssets.clear();
		manager.unload("data/uiskin.json");
		manager.unload(atlasName);		
		manager.unload("sharedImgs.atlas");
		
		manager.unload("sounds/mainmenu.mp3");				
		manager.unload("sounds/ingame.mp3");	
		manager.unload("sounds/credits.mp3");
		manager.unload("sounds/comment.mp3");
		manager.unload("sounds/selection.mp3");
		manager.unload("sounds/pause.mp3");
		manager.unload("sounds/change.mp3");
		manager.unload("sounds/goal.mp3");
		manager.unload("sounds/goal2.mp3");
		manager.unload("sounds/lost.mp3");
		manager.unload("sounds/rtkicker.mp3");
		manager.unload("sounds/vkicker.mp3");
		manager.unload("sounds/winner.mp3");
		manager.unload("sounds/button.mp3");
		
		manager.unload("sounds/sheep.mp3");
		manager.unload("sounds/cow.mp3");
		manager.unload("sounds/donkey.mp3");
		manager.unload("sounds/chick.mp3");
		manager.unload("sounds/goat.mp3");
		manager.unload("sounds/horse.mp3");
		manager.unload("sounds/dog.mp3");
		manager.unload("sounds/frog.mp3");
		manager.unload("sounds/wolf.mp3");
		manager.unload("sounds/agent.mp3");
		manager.unload("sounds/explode.mp3");
		manager.unload("sounds/grass.mp3");
		manager.unload("sounds/man.mp3");
		manager.unload("sounds/snake.mp3");
		manager.unload("sounds/sticky.mp3");
		manager.unload("sounds/woman.mp3");
		
		manager.dispose();
		manager = null;	
	}	
	
	public static boolean isLoaded(){
		return manager.update();
	}
		
	
}
