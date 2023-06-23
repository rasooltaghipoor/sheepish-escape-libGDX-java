package com.baranagames.sheepishescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.baranagames.sheepishescape.MyFirstGame;
import com.baranagames.sheepishescape.Assets;


public class MainMenuScreen implements Screen , InputProcessor{
	
	private MyFirstGame game;
	private Stage stage;
	private Image startGameButton;
	private Image matchButton , soundButton , rateButton , buyButton;
	private Image scoreButton ,creditButton, helpButton ;
	private Texture backTexture , titleTexture;
	private BitmapFont font , font2;
	private Window exitWindow , buyWindow; 
	//private Batch batch;
	private enum ScreenState {NORMAL,EXIT_WINDOW_ACTIVE,ACHIEVE_WINDOW_ACTIVE};
	private ScreenState scrState; 	
	private InputMultiplexer inputMultiplexer = new InputMultiplexer();
	private boolean checkPremium;
	private TextButton buyButton2;
	private Label bw_title;
	
	public MainMenuScreen(MyFirstGame game) {
		this.game = game;		
		scrState = ScreenState.NORMAL;
		checkPremium = false;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.4f, 0.5f, 0.9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
		
		if(checkPremium && MyFirstGame.IS_PREMIUM_TMP){		
			//System.out.println("*** *************** Game is activated! *******************");
			MyFirstGame.IS_PREMIUM = true;
			buyButton.setVisible(false);
			//startGameButton.addAction(Actions.scaleBy(3, 3));
			checkPremium = false;
			buyButton2.setVisible(false);
			bw_title.setText("\n***  FULL VERSION WAS ACTIVATED!  ***\n\nTHANK YOU FOR BUYING FULL VERSION. "
					+ "NOW ALL STAGES AND FILED IN ONE-PLAYER AND TWO-PLAYER MODE ARE ACTIVATED!");
			//buyWindow.setVisible(false);
			FileHandle handle = Gdx.files.local("Sheepish.txt");
			char[] myNameChars = handle.readString().toCharArray();
			myNameChars[2] = '1';
			handle.writeString(String.valueOf(myNameChars), false);			
		}
		/*batch.begin();
		font.draw(batch, "Total Score:"+MyFirstGame.TOTAL_SCORE, Gdx.graphics.getWidth()/2 , Gdx.graphics.getHeight());
		batch.end();*/
//		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void show() {
		
		// TODO Auto-generated method stub
	//		
		stage = new Stage();
		//stage = new Stage(new FitViewport(1024,768));
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);		
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(true);	
								
		Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
				Cursor cursor = Gdx.graphics.newCursor(pm, 0, 0);
		if (cursor != null)
		    cursor.setSystemCursor();
		//Gdx.input.setCursorImage(pm, 0, 0);
		pm.dispose();
		
		//batch = stage.getBatch();
		
		int scrWitdh = Gdx.graphics.getWidth();
		int scrHeight = Gdx.graphics.getHeight();
		float cellWidth , cellPad;
		
		if(scrWitdh > 2000){
			cellWidth = 400;
			cellPad = 96;
		}
		else if(scrWitdh > 1366){
			cellWidth = 320;
			cellPad = 64;
		}
		else if(scrWitdh >= 1024 && scrHeight >= 768) {
			cellWidth = 256;
		    cellPad = 32;		   
		}
		else {
			cellWidth = (scrWitdh - 256)/3;
			//System.out.println(cellWidth);
			//cellWidth = 128;
			cellPad = 16;
		   // if((cellWidth*3 + 100) > scrHeight)
		    //	cellWidth = (scrHeight - 100)/3;
		}
	
		float cloudWidth = 512;
		if(scrWitdh < 2048)
			cloudWidth = scrWitdh/4;
		
		Image sun  = new Image(Assets.OTHERS[0]);
		sun.setOrigin(0, 0);
		sun.setSize(cloudWidth/4, cloudWidth/4);
		sun.setPosition(scrWitdh/2-200, scrHeight/2+150);
		//sun.addAction(Actions.forever(Actions.sequence(Actions.moveTo(scrWitdh+300, cloud1.getY(), 60),Actions.moveTo(-300, cloud1.getY()))));
		stage.addActor(sun);
		
		Image cloud1  = new Image(Assets.OTHERS[1]);
		cloud1.setOrigin(0, 0);
		cloud1.setSize(cloudWidth, cloudWidth/2);
		cloud1.setPosition(10, scrHeight/2 +50);
		cloud1.addAction(Actions.forever(Actions.sequence(Actions.moveTo(scrWitdh+50, cloud1.getY(), 110),Actions.moveTo(-cloud1.getWidth()-50, cloud1.getY()))));
		stage.addActor(cloud1);
		
		Image cloud2  = new Image(Assets.OTHERS[2]);
		cloud2.setOrigin(0, 0);
		cloud2.setSize(cloudWidth/2, cloudWidth/4);
		cloud2.setPosition(scrWitdh/2+100 , scrHeight/2 +250);
		cloud2.addAction(Actions.forever(Actions.sequence(Actions.moveTo(scrWitdh+50, cloud2.getY(), 80),Actions.moveTo(-cloud2.getWidth()-50, cloud2.getY()))));
		stage.addActor(cloud2);
		
		Image cloud3  = new Image(Assets.OTHERS[3]);
		cloud3.setOrigin(0, 0);
		cloud3.setSize(cloudWidth, cloudWidth/2);
		cloud3.setPosition(scrWitdh/2 , scrHeight/2+ 100);
		cloud3.addAction(Actions.forever(Actions.sequence(Actions.moveTo(scrWitdh+50, cloud3.getY(), 70),Actions.moveTo(-cloud3.getWidth()-50, cloud3.getY()))));
		stage.addActor(cloud3);
		
		Image cloud4  = new Image(Assets.OTHERS[4]);
		cloud4.setOrigin(0, 0);
		cloud4.setSize(cloudWidth/2, cloudWidth/4);
		cloud4.setPosition(-200 , scrHeight/2 + 150);
		cloud4.addAction(Actions.forever(Actions.sequence(Actions.moveTo(scrWitdh+50, cloud4.getY(), 100),Actions.moveTo(-cloud4.getWidth()-50, cloud4.getY()))));
		stage.addActor(cloud4);	
		
		titleTexture = new Texture(Gdx.files.internal("title.png"),true);
		titleTexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);	
		Image titleImg  = new Image(titleTexture); 
		titleImg.setOrigin(0, 0);
		//	cloud4.setSize(256, 128);
		if(scrWitdh <= 2048)
			titleImg.setSize(scrWitdh/2, scrWitdh/4);
		titleImg.setPosition(scrWitdh/2 - titleImg.getWidth()/2 , scrHeight - titleImg.getHeight()-32);		
		stage.addActor(titleImg);
		
		//**************** add animal pics     ******************
		float moveTime = 1.5f , offest = 50;
		if(scrWitdh > 2048)
			offest = (scrWitdh - 2048)/2 + 50;
		//System.out.println(offest);
		if(scrHeight > 1000) moveTime = 2f;
		for(int i=0; i<15;i++)	{
			Image img= null;
			if(i < 14 && i != 7)    // i=7 *2 = 14 is grass that should not be showed
			   img = new Image(Assets.GAURDS[i*2]);
			else if(i == 14)
			   img = new Image(Assets.SHEEPS[0]);
			//System.out.println(img.getWidth());
			if(i != 7){						
				img.setOrigin(img.getWidth()/2, img.getHeight()/2);
				img.setPosition(MathUtils.random(img.getWidth()+offest, scrWitdh - img.getWidth()-offest) , -100);
				img.addAction(Actions.forever(Actions.sequence(Actions.delay(MathUtils.random(5, 45)),
						Actions.moveBy(0, MathUtils.random(scrHeight-250, scrHeight) , moveTime),Actions.rotateBy(360,0.5f),
						Actions.moveTo(img.getX(),-100,1f),Actions.delay(MathUtils.random(5)))));
				stage.addActor(img);
			}
		}
	   //******************* add  menu buttons ********************
		startGameButton = new Image (Assets.BUTTONS2[0]);
		startGameButton.setName("start");
		startGameButton.setSize(cellWidth, cellWidth/2);
		startGameButton.setOrigin(cellWidth/2, cellWidth/4);
		startGameButton.setColor(Color.WHITE);
		startGameButton.addListener(myInputListener);
		matchButton = new Image(Assets.BUTTONS2[1]);
		matchButton.setName("options");
		matchButton.setSize(cellWidth, cellWidth/2);
		matchButton.setOrigin(cellWidth/2, cellWidth/4);
		matchButton.setColor(Color.WHITE);
		matchButton.addListener(myInputListener);
		/*helpButton = new Image(Assets.BUTTONS[8]);
		helpButton.setName("help");
		helpButton.setOrigin(cellWidth/2, cellWidth/4);
		helpButton.addListener(myInputListener);*/
		creditButton = new Image(Assets.BUTTONS2[2]);
		creditButton.setName("credits");
		creditButton.setSize(cellWidth, cellWidth/2);
		creditButton.setOrigin(cellWidth/2, cellWidth/4);
		creditButton.setColor(Color.WHITE);
		creditButton.addListener(myInputListener);
		scoreButton = new Image(Assets.BUTTONS2[3]);
		scoreButton.setName("scores");
		scoreButton.setSize(cellWidth, cellWidth/2);
		scoreButton.setOrigin(cellWidth/2, cellWidth/4);
		scoreButton.setColor(Color.WHITE);
		scoreButton.addListener(myInputListener);
		helpButton = new Image(Assets.BUTTONS2[9]);
		helpButton.setName("help");
		helpButton.setSize(cellWidth, cellWidth/2);
		helpButton.setOrigin(cellWidth/2, cellWidth/4);
		helpButton.setColor(Color.WHITE);
		helpButton.addListener(myInputListener);
		
		startGameButton.setPosition(scrWitdh/2 + cellPad, cellWidth/2 + cellPad + cellPad/5);
		scoreButton.setPosition(scrWitdh/2 - cellWidth - cellPad, cellWidth/2 + cellPad + cellPad/5);
		creditButton.setPosition(scoreButton.getX()-cellWidth/2, scoreButton.getY() - cellWidth/2 - cellPad/5);
		matchButton.setPosition(startGameButton.getX()+cellWidth/2, startGameButton.getY() - cellWidth/2 - cellPad/5);
		helpButton.setPosition(scrWitdh/2 - helpButton.getWidth()/2, scoreButton.getY() - cellWidth/2 - cellPad/5);
			
		if(MyFirstGame.SOUNDS_ON)
			soundButton = new Image(Assets.BUTTONS2[6]);
		else   
			soundButton = new Image(Assets.BUTTONS2[7]);
		soundButton.setName("sound");
		soundButton.setSize(cellWidth/2, cellWidth/2);
		soundButton.setOrigin(cellWidth/4, cellWidth/4);
		soundButton.addListener(myInputListener);
		soundButton.setPosition(5, scrHeight - cellWidth/2 - 5);
		stage.addActor(soundButton);
		
		rateButton = new Image(Assets.BUTTONS2[8]);
		rateButton.setName("rate");
		rateButton.setSize(cellWidth/2, cellWidth/2);
		rateButton.setOrigin(cellWidth/4, cellWidth/4);
		rateButton.addListener(myInputListener);
		rateButton.setPosition(scrWitdh - cellWidth/2 - 5, scrHeight - cellWidth/2 - 5);
		stage.addActor(rateButton);
		
		buyButton = new Image(Assets.BUTTONS2[10]);
		buyButton.setName("buy");
		buyButton.setSize(cellWidth/2, cellWidth/2);
		buyButton.setOrigin(cellWidth/4, cellWidth/4);
		buyButton.addListener(myInputListener);
		buyButton.setPosition(rateButton.getX(), rateButton.getY() - cellWidth/2 - 5);
		stage.addActor(buyButton);
		
		String backTxt = "mainBack.png";
		if(scrWitdh > 1024)   backTxt = "mainBack1.png";
		backTexture = new Texture(Gdx.files.internal(backTxt),true);
		backTexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);	
		Image backImage = new Image(backTexture);//(Assets.backgroundTexture);
		if(backImage.getWidth() > scrWitdh)
			backImage.setSize(scrWitdh, scrWitdh/4);
		backImage.setPosition(scrWitdh/2 - backImage.getWidth()/2, 0);
		
		stage.addActor(backImage);		
		stage.addActor(startGameButton);
		stage.addActor(matchButton);
		stage.addActor(creditButton);
		stage.addActor(scoreButton);
		stage.addActor(helpButton);
		
		//******************************************  windows
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size =scrWitdh/30;
		parameter.color = Color.BLACK;
		font = generator.generateFont(parameter); // font size 12 pixels
		parameter.size =Gdx.graphics.getWidth()/25;		
		font2 = generator.generateFont(parameter); 
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		
		LabelStyle title_style = new LabelStyle(font, Color.BLACK);
		LabelStyle title_style2 = new LabelStyle(font2, Color.BLACK);
		
		TextButtonStyle but_style2 = new TextButtonStyle();
		but_style2.font = font2;
		but_style2.fontColor = Color.DARK_GRAY;
	    but_style2.up = new SpriteDrawable(new Sprite(Assets.HUDS[21]));
	    
	    Label scoreboard = new Label("Score:"+MyFirstGame.TOTAL_SCORE, title_style2);	    
	    scoreboard.setPosition(5 , scoreButton.getY()+scoreButton.getHeight()/4);
	    stage.addActor(scoreboard);
		
		//********************    Declare Exit Menu    *****************************************************
		WindowStyle style = new WindowStyle();
		style.titleFont = font2;					
		exitWindow = new Window("", style);
		exitWindow.setBackground(new SpriteDrawable(new Sprite(Assets.HUDS[20])));
		exitWindow.setSize(scrWitdh * 0.9f, scrHeight * 0.9f);
		exitWindow.setPosition(scrWitdh/2 - exitWindow.getWidth()/2, scrHeight/2 - exitWindow.getHeight()/2);
		exitWindow.setVisible(false);
		//exitWindow.setPosition(-200, scrHeight/2 - exitWindow.getHeight()/2);
				
		float cell_width = exitWindow.getWidth()*0.45f;
		float cell_height = cell_width / 4;
		float pad = (exitWindow.getHeight()/exitWindow.getWidth()) *  10;
		//System.out.println(pad);
					
		Label p_title = new Label("Design and Programming\n", title_style);		
		exitWindow.add(p_title).pad(pad).colspan(2);
		exitWindow.row();
		TextButton resumeButton = new TextButton("Resume",but_style2);
		resumeButton.setName("qw_resume");		
		resumeButton.addListener(myInputListener);
		exitWindow.add(resumeButton).width(cell_width).height(cell_height).pad(pad);
		TextButton webSiteButton = new TextButton("Visit Website",but_style2);
		webSiteButton.setName("qw_website");
		webSiteButton.addListener(myInputListener);
		exitWindow.add(webSiteButton).width(cell_width).height(cell_height).pad(pad);
		exitWindow.row();
		TextButton playStoreButton = new TextButton("Google Play",but_style2);
		playStoreButton.setName("qw_gstore");
		playStoreButton.addListener(myInputListener);
		exitWindow.add(playStoreButton).width(cell_width).height(cell_height).pad(pad);
		TextButton itunesStoreButton = new TextButton("itunes Store",but_style2);
		itunesStoreButton.setName("qw_itunes");
		itunesStoreButton.addListener(myInputListener);
		exitWindow.add(itunesStoreButton).width(cell_width).height(cell_height).pad(pad);
		exitWindow.row();		
		Label e_title = new Label("\nPress Back key again to Exit", title_style);		
		exitWindow.add(e_title).pad(pad).colspan(2);
		//Button button = new Button
		//exitWindow.setVisible(false);
		
		stage.addActor(exitWindow);
				
		//***********************************  Declare Achievments and Buy Menu  ********************************
		buyWindow = new Window("", style);
		buyWindow.setBackground(new SpriteDrawable(new Sprite(Assets.HUDS[20])));
		buyWindow.setSize(scrWitdh * 0.9f, scrHeight * 0.9f);
		buyWindow.setPosition(scrWitdh/2 - buyWindow.getWidth()/2, scrHeight/2 - buyWindow.getHeight()/2);
		buyWindow.setVisible(false);
		//exitWindow.setPosition(-200, scrHeight/2 - exitWindow.getHeight()/2);
		
		cell_width = buyWindow.getWidth()*0.6f;
		cell_height = cell_width / 3;
		pad = (buyWindow.getHeight()/buyWindow.getWidth()) *  5;
							
		String comment = "Currently, you have access to 12 stages in one-player mode and 4 fields in two-player mode. "
				+ "In order to activate all stages please buy full version for a little price.\n";
		bw_title = new Label(comment, title_style);	
		bw_title.setWrap(true);
		bw_title.setAlignment(Align.center);
		buyWindow.add(bw_title).width(cell_width*1.5f).pad(pad * 2);
		buyWindow.row();
		buyButton2 = new TextButton("Buy Full Version\n(Activate All Stages)",but_style2);
		buyButton2.setName("bw_buy");
		buyButton2.addListener(myInputListener);
		buyWindow.add(buyButton2).width(cell_width).height(cell_height).pad(pad);	
				
		stage.addActor(buyWindow);		
		
		if(MyFirstGame.IS_PREMIUM)
			buyButton.setVisible(false);
		else	
			checkPremium = true;
		
		if(MyFirstGame.SOUNDS_ON) {
			Assets.mainMusic.setVolume(MyFirstGame.VOLUME+0.5f);
			Assets.mainMusic.setLooping(true);
			Assets.mainMusic.play();			
		}
		
	}
	
	public InputListener myInputListener = new InputListener() {		
		
		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
				// TODO Auto-generated method stub
			String evtName = event.getListenerActor().getName();
			if(evtName.equals("start")){				
				game.setScreen(new LevelSelectionScreen(game));
				dispose();
			}
			else if(evtName.equals("options")){
				game.setScreen(new LevelSelectionScreen2(game));
				dispose();			
			}
			else if(evtName.equals("help")){				
				dispose();
				game.setScreen(new HelpScreen(game));
			}
			else if(evtName.equals("credits")){
				game.setScreen(new CreditsScreen(game));
				dispose();	
			}
			else if(evtName.equals("scores")){				
				if (MyFirstGame.actionResolver.getSignedInGPGS()) {
					MyFirstGame.actionResolver.submitScoreGPGS(MyFirstGame.TOTAL_SCORE);					
					MyFirstGame.actionResolver.getLeaderboardGPGS();				
				}
				else  MyFirstGame.actionResolver.loginGPGS();
			}			
			else if(evtName.equals("sound")){
				FileHandle handle = Gdx.files.local("Sheepish.txt");
				char[] myNameChars = handle.readString().toCharArray();
				if(MyFirstGame.SOUNDS_ON){
					MyFirstGame.SOUNDS_ON = false;
					Assets.mainMusic.stop();
					MyFirstGame.VOLUME = 0;
										
					myNameChars[4] = '0';
					//myName = String.valueOf(myNameChars);
					soundButton.setDrawable(new SpriteDrawable(new Sprite(Assets.BUTTONS2[7])));
				}
				else {
					MyFirstGame.SOUNDS_ON = true;
					Assets.mainMusic.play();
					MyFirstGame.VOLUME = 0.4f;
					myNameChars[4] = '1';
					soundButton.setDrawable(new SpriteDrawable(new Sprite(Assets.BUTTONS2[6])));
				}
				handle.writeString(String.valueOf(myNameChars), false);
				//System.out.println(String.valueOf(myNameChars));
			}
			else if(evtName.equals("rate")){
				if(Gdx.app.getType() == ApplicationType.Android) {	
					try{
						Gdx.net.openURI("market://details?id=com.baranagames.sheepishescape.android");
					}catch(Exception ex){
						Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.baranagames.sheepishescape.android");
					}
					
				}		
				else if(Gdx.app.getType() == ApplicationType.iOS) {					
					try{
						Gdx.net.openURI("itms-apps://itunes.apple.com/app/id353372460");
					}catch(Exception ex){
						Gdx.net.openURI("https://itunes.apple.com/app/id353372460");
					}
				}
				
				/*String url = "bazaar://details?id=com.baranagames.sheepishescape.android";   
                Intent next = new Intent(Intent.ACTION_EDIT);
                try {
                    next.setData(Uri.parse(url));
                    startActivity(next);
                } catch (Exception e) {
                    next.setData(Uri.parse("http://cafebazaar.ir/app/com.baranagames.sheepishescape.android")); 
                    startActivity(next);
                }
				}*/
			}			
			else if(evtName.equals("buy")){
				buyWindow.setVisible(true);
				scrState = ScreenState.ACHIEVE_WINDOW_ACTIVE;
			}
			else if(evtName.equals("bw_buy")){
				MyFirstGame.actionResolver.purchasePremium();
			}
			else if(evtName.equals("qw_resume")){
				exitWindow.setVisible(false);
				scrState = ScreenState.NORMAL;
			}
			else if(evtName.equals("qw_website"))
				Gdx.net.openURI("https://sites.google.com/site/baranagames");
			else if(evtName.equals("qw_gstore"))
				Gdx.net.openURI("https://www.play.google.com");
			else if(evtName.equals("qw_itunes"))
				Gdx.net.openURI("https://www.itunes.apple.com");
			
			event.getListenerActor().addAction(Actions.color(Color.WHITE)); 
			
			return true;
		}
		
		@Override
		public void	enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
			//event.getListenerActor().addAction(Actions.sequence(Actions.rotateBy(15),Actions.moveBy(0, 5)));	
			if(MyFirstGame.SOUNDS_ON)
				Assets.buttonSnd.play(MyFirstGame.VOLUME+0.4f);
			if(event.getListenerActor().getName().equals("rate") || event.getListenerActor().getName().equals("sound") || event.getListenerActor().getName().equals("buy"))
				event.getListenerActor().addAction(Actions.sequence(Actions.color(Color.CYAN)));
			else
				event.getListenerActor().addAction(Actions.sequence(Actions.color(Color.CYAN),Actions.moveBy(0, 5)));			
		}
		@Override
		public void	exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
			
			if(event.getListenerActor().getName().equals("rate") || event.getListenerActor().getName().equals("sound") || event.getListenerActor().getName().equals("buy"))
				event.getListenerActor().addAction(Actions.sequence(Actions.color(Color.WHITE)));
			else
				event.getListenerActor().addAction(Actions.sequence(Actions.color(Color.WHITE),Actions.moveBy(0, -5)));
		}
	};

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		Assets.mainMusic.stop();
		backTexture.dispose();
		titleTexture.dispose();
		font.dispose();
		font2.dispose();
		stage.clear();
		stage.dispose();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		  case Keys.ESCAPE:
		  case Keys.BACK:
		      //exitWindow.setX(scrWidth/2 - exitWindow.getWidth()/2);
			  if(scrState == ScreenState.NORMAL) {
				  exitWindow.setVisible(true);
				  scrState = ScreenState.EXIT_WINDOW_ACTIVE;
			  }
			  else if(scrState == ScreenState.EXIT_WINDOW_ACTIVE) {
				  dispose();
				  Gdx.app.exit();
			  }
			  else if(scrState == ScreenState.ACHIEVE_WINDOW_ACTIVE) {
				  buyWindow.setVisible(false);
				  scrState = ScreenState.NORMAL;
			  }
					  
			  break;
		}
		return true;
	}
	@Override
	public boolean touchDown (int x, int y, int pointer, int newParam) {	return false;	}
	@Override
	public boolean touchDragged (int x, int y, int pointer) {	return false;	}
	@Override
	public boolean mouseMoved (int x, int y) {		return false;	}
	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {	return false;  }		
	@Override
	public boolean keyUp(int keycode) {	return false;	}
	@Override
	public boolean keyTyped(char character) {	return false;	}
	@Override
	public boolean scrolled(int amount) { return false; 	} 
	
}
