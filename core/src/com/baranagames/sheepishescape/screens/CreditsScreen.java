package com.baranagames.sheepishescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.baranagames.sheepishescape.MyFirstGame;
import com.baranagames.sheepishescape.Assets;

public class CreditsScreen implements Screen {
	
	private MyFirstGame game;
	private Stage stage;	
	private Image backButton , websiteButton , facebookButton;
	private Texture backTexture;
	
	public CreditsScreen(MyFirstGame game) {
		this.game = game;		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(1f, 0.7f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
//		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		//Gdx.input.setCursorCatched(false);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
		Cursor cursor = Gdx.graphics.newCursor(pm, 0, 0);
		if (cursor != null)
		    cursor.setSystemCursor();
		//Gdx.input.setCursorImage(pm, 0, 0);
		pm.dispose();
		
		int scrWitdh = Gdx.graphics.getWidth();
		int scrHeight = Gdx.graphics.getHeight();

		
		float imageWidth = 512;
		if(scrWitdh < 2048)
			imageWidth = scrWitdh/4;
		
		Image cloud1  = new Image(Assets.OTHERS[1]);
		cloud1.setOrigin(0, 0);
		cloud1.setSize(imageWidth, imageWidth/2);
		cloud1.setPosition(10, scrHeight/2 +50);
		cloud1.addAction(Actions.forever(Actions.sequence(Actions.moveTo(scrWitdh+50, cloud1.getY(), 110),Actions.moveTo(-cloud1.getWidth()-50, cloud1.getY()))));
		stage.addActor(cloud1);
		
		Image cloud2  = new Image(Assets.OTHERS[2]);
		cloud2.setOrigin(0, 0);
		cloud2.setSize(imageWidth/2, imageWidth/4);
		cloud2.setPosition(scrWitdh/2+100 , scrHeight/2 +250);
		cloud2.addAction(Actions.forever(Actions.sequence(Actions.moveTo(scrWitdh+50, cloud2.getY(), 80),Actions.moveTo(-cloud2.getWidth()-50, cloud2.getY()))));
		stage.addActor(cloud2);
		
		Image cloud3  = new Image(Assets.OTHERS[3]);
		cloud3.setOrigin(0, 0);
		cloud3.setSize(imageWidth, imageWidth/2);
		cloud3.setPosition(scrWitdh/2 , scrHeight/2+ 100);
		cloud3.addAction(Actions.forever(Actions.sequence(Actions.moveTo(scrWitdh+50, cloud3.getY(), 70),Actions.moveTo(-cloud3.getWidth()-50, cloud3.getY()))));
		stage.addActor(cloud3);
		
		Image cloud4  = new Image(Assets.OTHERS[4]);
		cloud4.setOrigin(0, 0);
		cloud4.setSize(imageWidth/2, imageWidth/4);
		cloud4.setPosition(-200 , scrHeight/2 + 150);
		cloud4.addAction(Actions.forever(Actions.sequence(Actions.moveTo(scrWitdh+50, cloud4.getY(), 100),Actions.moveTo(-cloud4.getWidth()-50, cloud4.getY()))));
		stage.addActor(cloud4);	
		
		String backTxt = "mainBack.png";
		if(scrWitdh > 1024)   backTxt = "mainBack1.png";
		backTexture = new Texture(Gdx.files.internal(backTxt),true);
		backTexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);	
		Image backImage = new Image(backTexture);//(Assets.backgroundTexture);
		if(backImage.getWidth() > scrWitdh)
			backImage.setSize(scrWitdh, scrWitdh/4);
		backImage.setPosition(scrWitdh/2 - backImage.getWidth()/2, 0);	
		stage.addActor(backImage);		
		
		Image programmerImg  = new Image(Assets.sharedAtlas.findRegion("cprog"));		
		programmerImg.setSize(imageWidth, imageWidth*programmerImg.getHeight()/programmerImg.getWidth());
		programmerImg.setPosition(scrWitdh/2 - programmerImg.getWidth()/2,scrHeight/2 - programmerImg.getHeight()/2 + 50);
		programmerImg.addAction(Actions.fadeOut(0));
		programmerImg.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1),Actions.delay(3),Actions.fadeOut(1),Actions.delay(22))));
		stage.addActor(programmerImg);
		
		Image graphistImg  = new Image(Assets.sharedAtlas.findRegion("cgraph"));
		graphistImg.setSize(imageWidth, imageWidth*graphistImg.getHeight()/graphistImg.getWidth());
		graphistImg.setPosition(scrWitdh/2 - graphistImg.getWidth()/2, scrHeight/2 - graphistImg.getHeight()/2 + 50);
		graphistImg.addAction(Actions.fadeOut(0));
		graphistImg.addAction(Actions.forever(Actions.sequence(Actions.delay(5),Actions.fadeIn(1),Actions.delay(3),Actions.fadeOut(1),Actions.delay(17))));
		/*graphistImg.addAction(Actions.sequence(Actions.forever(Actions.sequence(Actions.delay(3),
				Actions.moveBy(0, scrHeight+graphistImg.getHeight()+100,10),Actions.moveTo(graphistImg.getX() , -graphistImg.getHeight()),Actions.delay(9)))));*/
		stage.addActor(graphistImg);
		
		Image thanksImg  = new Image(Assets.sharedAtlas.findRegion("cthanks"));    
		thanksImg.setSize(imageWidth, imageWidth*thanksImg.getHeight()/thanksImg.getWidth());
		thanksImg.setPosition(scrWitdh/2 - thanksImg.getWidth()/2, scrHeight/2 - thanksImg.getHeight()/2 + 50);
		thanksImg.addAction(Actions.fadeOut(0));
		thanksImg.addAction(Actions.forever(Actions.sequence(Actions.delay(10),Actions.fadeIn(1),Actions.delay(3),Actions.fadeOut(1),Actions.delay(12))));
		/*thanksImg.addAction(Actions.sequence(Actions.forever(Actions.sequence(Actions.delay(6),
				Actions.moveBy(0, scrHeight+graphistImg.getHeight()+100,10),Actions.moveTo(graphistImg.getX() , -thanksImg.getHeight()),Actions.delay(6)))));*/
		stage.addActor(thanksImg);
		
		Image toolsImg  = new Image(Assets.sharedAtlas.findRegion("ctools"));	
		toolsImg.setSize(imageWidth, imageWidth*toolsImg.getHeight()/toolsImg.getWidth());
		toolsImg.setPosition(scrWitdh/2 - toolsImg.getWidth()/2, scrHeight/2 - toolsImg.getHeight()/2 + 50);
		toolsImg.addAction(Actions.fadeOut(0));
		toolsImg.addAction(Actions.forever(Actions.sequence(Actions.delay(15),Actions.fadeIn(1),Actions.delay(3),Actions.fadeOut(1),Actions.delay(7))));
		/*toolsImg.addAction(Actions.sequence(Actions.forever(Actions.sequence(Actions.delay(9),
				Actions.moveBy(0, scrHeight+graphistImg.getHeight()+100,10),Actions.moveTo(graphistImg.getX() , -toolsImg.getHeight()),Actions.delay(3)))));*/
		stage.addActor(toolsImg);
		
		Image logoImg  = new Image(Assets.sharedAtlas.findRegion("clogo"));		
		logoImg.setSize(imageWidth*0.75f, imageWidth*0.75f*logoImg.getHeight()/logoImg.getWidth());
		logoImg.setPosition(scrWitdh/2 - logoImg.getWidth()/2, scrHeight/2 - logoImg.getHeight()/2 + 50);
		logoImg.addAction(Actions.fadeOut(0));
		logoImg.addAction(Actions.forever(Actions.sequence(Actions.delay(20),Actions.fadeIn(1),Actions.delay(3),Actions.fadeOut(1),Actions.delay(2))));
		/*logoImg.addAction(Actions.sequence(Actions.forever(Actions.sequence(Actions.delay(12),
				Actions.moveBy(0, scrHeight+graphistImg.getHeight()+100,10),Actions.moveTo(graphistImg.getX() , -logoImg.getHeight())))));*/
		stage.addActor(logoImg);		
	
		float width = scrWitdh/3;
		if(width > 512) 
			width = 512;
		websiteButton = new Image (Assets.sharedAtlas.findRegion("website"));	
		websiteButton.setName("website");
		websiteButton.setSize(width, width/8);		
		websiteButton.setPosition(scrWitdh/2 - websiteButton.getWidth() - scrWitdh/12, 20);
		websiteButton.addListener(myInputListener);
		stage.addActor(websiteButton);
			
		facebookButton = new Image (Assets.sharedAtlas.findRegion("facebook"));	
		facebookButton.setName("facebook");
		facebookButton.setSize(width, width/8);
		facebookButton.setPosition(scrWitdh/2 + scrWitdh/12 , 20);
		facebookButton.addListener(myInputListener);
		stage.addActor(facebookButton);
	
		width = 256;
		if(scrWitdh < 2560)
			width = scrWitdh/10;
		
		backButton = new Image (Assets.BUTTONS[0]);
		backButton.setName("back");
		backButton.setSize(width, width);
		//backButton.setSize(128, 64);
		//backButton.setOrigin(64, 32);
		backButton.setPosition(20 , scrHeight-backButton.getHeight()-20);
		backButton.addListener(myInputListener);
		stage.addActor(backButton);
		
		if(MyFirstGame.SOUNDS_ON) {
			Assets.creditMusic.setVolume(MyFirstGame.VOLUME+0.5f);
			Assets.creditMusic.setLooping(true);
			Assets.creditMusic.play();			
		}
	
	}
	
	public InputListener myInputListener = new InputListener() {		
		
		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			// TODO Auto-generated method stub
			if(event.getListenerActor().getName().equals("website")){				
				Gdx.net.openURI("https://sites.google.com/site/baranagames/");
			}
			else if(event.getListenerActor().getName().equals("facebook")){
				Gdx.net.openURI("https://www.facebook.com/SheepishEscape");		
			}
			else if(event.getListenerActor().getName().equals("back")){				
				game.setScreen(new MainMenuScreen(game));
				dispose();
			}			
			
			return true;
		}
		
		@Override
		public void	enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
			if(MyFirstGame.SOUNDS_ON)
				Assets.buttonSnd.play(MyFirstGame.VOLUME+0.4f);
			event.getListenerActor().addAction(Actions.color(Color.YELLOW));
		}
		@Override
		public void	exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
			event.getListenerActor().addAction(Actions.color(Color.WHITE));			
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
		Assets.creditMusic.stop();			
		backTexture.dispose();
		stage.clear();
		stage.dispose();
	}
	
	
}
