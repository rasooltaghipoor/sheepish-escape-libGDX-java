package com.baranagames.sheepishescape.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Screen;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;

public class LevelSelectionScreen2 implements Screen , InputProcessor{
	
	private MyFirstGame game;
	private Stage stage;
	private Texture background , buyTexture;
	private Image levelButtons[] = new Image[25];
	private Image backButton , stageHint , buyImg;
	private Table levels;
	private InputMultiplexer inputMultiplexer = new InputMultiplexer();
	//boolean backIsClicked;
	
	public LevelSelectionScreen2(MyFirstGame game) {
		this.game = game;		
		buyTexture = null;
		buyImg = null;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.3f, 0.9f,0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.getBatch().begin();
		float tileCountX =  Gdx.graphics.getWidth() / 256f + 1 ; 
		float tileCountY =  Gdx.graphics.getHeight() / 256f +1 ;
		stage.getBatch().draw(background, 0, 0 , 256f * tileCountX, 266f * tileCountY, 0, tileCountX,  tileCountY, 0);	
		stage.getBatch().end();
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
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);		
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(true);	
		
		Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
		Cursor cursor = Gdx.graphics.newCursor(pm, 0, 0);
		if (cursor != null)
		    cursor.setSystemCursor();
	//	Gdx.input.setCursorImage(pm, 0, 0);
		pm.dispose();
		
		background = new Texture(Gdx.files.internal("bk1.png"));
		background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		int scrWitdh = Gdx.graphics.getWidth();
		int scrHeight = Gdx.graphics.getHeight();
		float cellSize=0 , pad = 0;
		if(scrWitdh >= 1024 && scrHeight >= 768)  {
			cellSize = (scrHeight - 200)/5;
			if(cellSize > 256) 
				cellSize = 256;
			pad = 10;
		}
		else {
			pad = 5;
			cellSize = (scrHeight - 80)/5;
		    if((cellSize*6 + 80) > scrWitdh)
		    	cellSize = (scrWitdh - 80)/6;
		}
			
		//System.out.println(cellSize);
	//	int c = 1;
		//for (int l = 0; l < 10; l++) {
			levels = new Table();//.pad(100);
			
			backButton = new Image(Assets.BUTTONS[0]);		 
			backButton.setName("back");	// back		
			backButton.setOrigin(64, 64);
			backButton.addListener(levelClickListener);
			
			stageHint = new Image(Assets.OTHERS[14]);		 
			stageHint.setName("stageHint");	// hint		
			stageHint.setOrigin(128, 64);
			//	backButton.setVisible(false);			
						
			levels.add(backButton).width(cellSize).height(cellSize).pad(pad);
			levels.add();
			levels.add(stageHint).width(cellSize*2).height(cellSize).pad(pad).colspan(2);
			levels.add();
			levels.add();//forwardButton).width(100).height(100);
			
			//levels.defaults().pad(20, 40, 20, 40);
			int k = 0;
			//Texture buttonTexture = new Texture(Gdx.files.internal("stageOn3.png"));
			for (int i = 0; i < 4; i++) {
				levels.row();
				for (int j = 0; j < 6; j++) {
					 levelButtons[k] = new Image(Assets.OTHERS[i+10]);
					 levelButtons[k].setOrigin(cellSize/2, cellSize/2);
					 levelButtons[k].addListener(levelClickListener);
					// levelButtons[k].setDrawable(new SpriteDrawable(new Sprite(Assets.textureAtlas.findRegion("stageOn3"))));		
					 levelButtons[k].setName(Integer.toString(k+1));
					 //levelButtons[k].setName("Level" + Integer.toString(c++));
					 levels.add( levelButtons[k]).width(cellSize).height(cellSize).pad(pad);//padRight(20).padBottom(20);
					 
					 k++;
				
				}
			}
			//stage.addActor(levels);
			levels.setFillParent(true);
		
		stage.addActor(levels);	
		
		if(!MyFirstGame.IS_PREMIUM){
			buyTexture = new Texture(Gdx.files.internal("buy.png"),true);
			buyTexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
			buyImg = new Image(buyTexture);	
			//System.out.println(buyImg.getWidth());
			if(buyImg.getWidth() > scrWitdh * 0.75f)
			   buyImg.setSize(scrWitdh * 0.75f , scrWitdh * 0.375f);
			buyImg.setPosition(scrWitdh/2 -buyImg.getWidth()/2, scrHeight/2 - buyImg.getHeight()/2);
			buyImg.setVisible(false);
			stage.addActor(buyImg);
		}
		
		if(MyFirstGame.SOUNDS_ON) {
			Assets.selectionSnd.loop();
			Assets.selectionSnd.play(MyFirstGame.VOLUME);	
		}
	}	
	

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
		Assets.selectionSnd.stop();
		background.dispose();
		if(buyTexture != null) {
			buyTexture.dispose();
			buyTexture =null;
			buyImg = null;		
		}
		stage.clear();
		stage.dispose();
	}	
	
	public ClickListener levelClickListener = new ClickListener() {
		@Override
		public void clicked (InputEvent event, float x, float y) {
			if(event.getListenerActor().getName().equals("back"))	{		 
				game.setScreen(new MainMenuScreen(game));
				dispose();
			}
			else {
				int level = Integer.parseInt(event.getListenerActor().getName());
				System.out.println(MyFirstGame.buyLevel/2);
				if(!MyFirstGame.IS_PREMIUM && level > MyFirstGame.buyLevel/2){
					buyImg.setVisible(true);
					return;
				}
				game.setScreen(new SecondScreen(game,level));
				dispose();					
			}			
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
	public boolean keyDown(int keycode) {
		switch (keycode) {
		  case Keys.ESCAPE:
		  case Keys.BACK:
			  if(buyImg!= null && buyImg.isVisible())
				  buyImg.setVisible(false);
			  else {
				  game.setScreen(new MainMenuScreen(game));
				  dispose();
			  }
		      //exitWindow.setX(scrWidth/2 - exitWindow.getWidth()/2);
			 /* if(scrState == ScreenState.NORMAL) {
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
			  }*/
					  
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
