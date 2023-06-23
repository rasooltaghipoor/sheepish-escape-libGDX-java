package com.baranagames.sheepishescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.baranagames.sheepishescape.MyFirstGame;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.baranagames.sheepishescape.Assets;


public class HelpScreen implements Screen {
	
	private MyFirstGame game;
	private Stage stage;
	private Image forButton , commentImg , backButton, menuButton;
	private Texture picture[] = {null,null,null};
	private int pageNo;
		
	public HelpScreen(MyFirstGame game) {
		this.game = game;		
		pageNo = 0;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.9f, 0.9f, 0.2f, 1);
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
		float scrW = Gdx.graphics.getWidth() ; 
		/*if(scrW >= 2048)     imgEnd = "b.png";
		else     imgEnd = ".png";*/
		
		
	    picture[0] = new Texture(Gdx.files.internal("help0.png"),true);	   	   
		picture[0].setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);	
		
		float imgW = picture[0].getWidth() , imgH = picture[0].getHeight();
		if(picture[0].getWidth() > scrW){
			//imgH = (imgH * scrW) / imgW;
			imgW = scrW - 50;	
			imgH = imgW/2;
		}
		//System.out.println(imgW + "  "+imgH);
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
		Cursor cursor = Gdx.graphics.newCursor(pm, 0, 0);
		if (cursor != null)
		    cursor.setSystemCursor();
		//Gdx.input.setCursorImage(pm, 0, 0);
		//Gdx.graphics.setCursor(Gdx.graphics.newCursor(pixmap, 16, 16));
		pm.dispose();
		
		int scrWitdh = Gdx.graphics.getWidth();
		int scrHeight = Gdx.graphics.getHeight();
			
		commentImg  = new Image(picture[0]);	//Assets.textureAtlas.findRegion("ball"));		
		commentImg.setSize(imgW, imgH);
		//graphistImg.setOrigin(128, 64);
		commentImg.setPosition(scrWitdh/2 - commentImg.getWidth()/2, 0);
		stage.addActor(commentImg);
		
		float width = 256;
		if(scrWitdh < 2560)
			width = scrWitdh/10;
		
		forButton = new Image (Assets.BUTTONS2[5]);
		forButton.setName("forw");
		forButton.setSize(width, width);
		forButton.setOrigin(forButton.getWidth()/2, forButton.getHeight()/2);
		forButton.setPosition(scrWitdh/2 + forButton.getWidth()  , scrHeight - forButton.getHeight() - 10);
		forButton.addListener(myInputListener);
		stage.addActor(forButton);
		backButton = new Image (Assets.BUTTONS2[4]);
		backButton.setName("back");
		backButton.setSize(width, width);
		backButton.setOrigin(backButton.getWidth()/2, backButton.getHeight()/2);
		backButton.setPosition(scrWitdh/2 - 2*forButton.getWidth() , scrHeight - forButton.getHeight() - 10);
		backButton.addListener(myInputListener);
		backButton.setVisible(false);
		stage.addActor(backButton);
		menuButton = new Image (Assets.BUTTONS[0]);
		menuButton.setName("menu");
		menuButton.setSize(width, width);
		menuButton.setOrigin(menuButton.getWidth()/2, menuButton.getHeight()/2);
		menuButton.setPosition(scrWitdh/2 - menuButton.getWidth()/2 , scrHeight - forButton.getHeight() - 10);
		menuButton.addListener(myInputListener);
		stage.addActor(menuButton);
		//System.out.println(forButton.getWidth());
		
		if(MyFirstGame.SOUNDS_ON) {
			/*Assets.commentSnd.loop();
			Assets.commentSnd.play(MyFirstGame.VOLUME);	*/
			Assets.CommentMusic.setVolume(MyFirstGame.VOLUME+0.4f);
			Assets.CommentMusic.setLooping(true);
			Assets.CommentMusic.play();			
		}
	
	}
	
	public InputListener myInputListener = new InputListener() {		
		
		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			// TODO Auto-generated method stub			
			if(event.getListenerActor().getName().equals("menu"))	{
				game.setScreen(new MainMenuScreen(game));
				dispose();
			}			
			else if(event.getListenerActor().getName().equals("forw"))	{
				pageNo++;
				if(pageNo == 2) 
					forButton.setVisible(false);				
				if(picture[pageNo] == null){
					picture[pageNo] = new Texture(Gdx.files.internal("help"+String.valueOf(pageNo)+".png"),true);
					picture[pageNo].setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);	
				}
				backButton.setVisible(true);				
				commentImg.setDrawable(new SpriteDrawable(new Sprite(picture[pageNo])));
			}
			else if(event.getListenerActor().getName().equals("back"))	{
				pageNo--;
				if(pageNo == 0)
					backButton.setVisible(false);
				forButton.setVisible(true);				
				commentImg.setDrawable(new SpriteDrawable(new Sprite(picture[pageNo])));
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
		Assets.CommentMusic.stop();
		for(int i=0; i<3;i++){
			if(picture[i] != null) {
				picture[i].dispose();
				picture[i] = null;
			}
		}
		
		stage.clear();
		stage.dispose();
	}
	
	
}
