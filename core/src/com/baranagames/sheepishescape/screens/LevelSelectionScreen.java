package com.baranagames.sheepishescape.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.Screen;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;

public class LevelSelectionScreen implements Screen , InputProcessor {
	
	private MyFirstGame game;
	private Stage stage;
	private Texture background , buyTexture;
	private TextButton levelButtons[] = new TextButton[24];
	private int scores[] = new int[121];  // index 0 is not used
	private int lastLevel , currentPage;
	private Image backButton , forwardButton , menuButton , pageNoImg , buyImg;
	private Table levels;
	private boolean backIsClicked;
	private BitmapFont font;	
	private InputMultiplexer inputMultiplexer = new InputMultiplexer();
	
	public LevelSelectionScreen(MyFirstGame game) {
		this.game = game;		
		backIsClicked = false;
		buyTexture = null;
		buyImg = null;
		FileHandle handle = Gdx.files.local("Sheepish.txt");
        String file_content = handle.readString();
        String info[] = file_content.split(";");
        lastLevel = Integer.parseInt(info[4]);   
        int i , j = 5;
        for(i = 1; i<= lastLevel; i++,j++)
        	scores[i] = Integer.parseInt(info[j]);
		for( ; i<=120 ;i++)
			scores[i] = -1;
		
		currentPage = (lastLevel-1)/24 + 1;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.3f, 0.9f, 0f, 1);
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
		
		stage = new Stage();
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
			
		background = new Texture(Gdx.files.internal("bk1.png"));
		background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		
		int scrWitdh = Gdx.graphics.getWidth();
		int scrHeight = Gdx.graphics.getHeight();
		float cellSize=0 , pad = 0;
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size =scrWitdh/35;
		parameter.color = Color.WHITE;
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		
		TextButtonStyle but_style = new TextButtonStyle();
		but_style.font = font;
		but_style.fontColor = Color.DARK_GRAY;
	    but_style.up = new SpriteDrawable(new Sprite(Assets.OTHERS[7]));
		
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
	
			levels = new Table();//.pad(100);
			//levels.setBounds(0, 0, 200, 300);
			//levels.setBackground(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("backc.png")))));
			levels.setFillParent(true);
			
			
			backButton = new Image(Assets.BUTTONS2[4]);		 
			backButton.setName("-1");	// back		
			backButton.setOrigin(cellSize/2, cellSize/2);
			backButton.addListener(levelClickListener);
			backButton.setVisible(false);
			forwardButton = new Image(Assets.BUTTONS2[5]);		 
			forwardButton.setName("-2");  // forward
			forwardButton.setOrigin(cellSize/2, cellSize/2);
			forwardButton.addListener(levelClickListener);
			menuButton = new Image(Assets.BUTTONS[0]);		 
			menuButton.setName("-3");  // main menu
			menuButton.setOrigin(cellSize/2, cellSize/2);
			menuButton.addListener(levelClickListener);
			pageNoImg = new Image(Assets.OTHERS[currentPage+14]);			
			pageNoImg.setOrigin(cellSize/2, cellSize/2);
			
			
			//levels.add();
			levels.add(pageNoImg).width(cellSize).height(cellSize).pad(pad);		
			
			levels.add(backButton).width(cellSize).height(cellSize).colspan(2).pad(pad);
						
			levels.add(menuButton).width(cellSize).height(cellSize);		
			//levels.add();
			levels.add(forwardButton).width(cellSize).height(cellSize).colspan(2).pad(pad);
			
			
			//levels.defaults().pad(20, 40, 20, 40);
			int k = 0;
			for (int i = 0; i < 4; i++) {
				levels.row();
				for (int j = 0; j < 6; j++) {
					 levelButtons[k] = new TextButton("",but_style);	
					 levelButtons[k].setOrigin(cellSize/2, cellSize/2);
					 levelButtons[k].addListener(levelClickListener);
					 //levelButtons[k].setName("Level" + Integer.toString(c++));
					 levels.add( levelButtons[k]).width(cellSize).height(cellSize).pad(pad);//padRight(20).padBottom(20);
					 k++;
					 //addActor(resumeButton);
					//levels.add(getLevelButton(c++)).width(100).height(100).pad(5);
					//levels.add(new Image(Assets.getImg("ball"))).width(150).height(50);
				}
			}
			//stage.addActor(levels);
			
			refreshLevelTable();
			//levels.addAction(Actions.sequence(Actions.fadeOut(1),Actions.fadeIn(2)));
			//ScrollPane s = new ScrollPane(levels);
			//scroll.addPage(levels);
		//}
		//container.add(levels).expand().fill();
		
		//stage.addActor(backImage);
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
	
	private void refreshLevelTable(){
		int x;
		if(backIsClicked) 		x = -Gdx.graphics.getWidth();		
		else        x = Gdx.graphics.getWidth();			
		levels.addAction(Actions.sequence(Actions.moveBy(x, 0),Actions.moveBy(-x, 0,0.75f)));
		if(currentPage == 1)    backButton.setVisible(false);
		else    backButton.setVisible(true);
		if(currentPage <= 4)    forwardButton.setVisible(true);
		else    forwardButton.setVisible(false);
		
		int buttonIndex = 0;
		int k = 0;
		for(int i=(currentPage-1)*24+1; i <= currentPage*24 ;i++,k++){
			/*if(scores[i] == -1)   buttonIndex = 5;				
			else if(scores[i] == 0)  buttonIndex =6;
			else if(scores[i] == 1)   buttonIndex = 7;
			else if(scores[i] == 2)   buttonIndex =8;
			else if(scores[i] == 3)   buttonIndex = 9;*/
			//levelButtons[k].setDrawable(new SpriteDrawable(new Sprite(Assets.OTHERS[buttonIndex])));	
			if(i<=lastLevel)
				levelButtons[k].setText(String.valueOf(scores[i]));
			else
				levelButtons[k].setText("??");
			levelButtons[k].setName(Integer.toString(i));
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
		font.dispose();
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
			if(event.getListenerActor().getName().equals("-3"))	{  // -3 => menu			
				game.setScreen(new MainMenuScreen(game));
				dispose();					
			}
			else if(event.getListenerActor().getName().equals("-2"))	{  // -2 => forward			
				if(currentPage < 5) {
					backIsClicked = false;
					currentPage++;			
					refreshLevelTable();
					pageNoImg.setDrawable(new SpriteDrawable(new Sprite(Assets.OTHERS[currentPage+14])));
				}	
			}
			else if(event.getListenerActor().getName().equals("-1"))	{		 // -1 => back	
				if(currentPage > 1) {
					backIsClicked = true;
					currentPage--;
					refreshLevelTable();
					pageNoImg.setDrawable(new SpriteDrawable(new Sprite(Assets.OTHERS[currentPage+14])));
				}	
			}
			else {
				//System.out.println(event.getListenerActor().getName());
					int level = Integer.parseInt(event.getListenerActor().getName());
				//game.setScreen(new FirstScreen(game,50));
					if(!MyFirstGame.IS_PREMIUM && level == MyFirstGame.buyLevel){
						buyImg.setVisible(true);
						return;
					}
					if(level <= lastLevel) {
					    //if(level == 1 || level == 25 || level == 49 || level == 73 || level == 97)
						if(level % 24 == 1)
					    	game.setScreen(new CommentScreen(game,level));
					    else
					    	game.setScreen(new FirstScreen(game,level));
						dispose();
					}
				}
			
			//game.setScreen(new FirstScreen(game));
		}
		
		@Override
		public void	enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
			if(MyFirstGame.SOUNDS_ON)
				Assets.buttonSnd.play(MyFirstGame.VOLUME+0.4f);
			if(Integer.parseInt(event.getListenerActor().getName()) <= lastLevel)
				event.getListenerActor().addAction(Actions.color(Color.ORANGE));		
		}
		@Override
		public void	exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
			if(Integer.parseInt(event.getListenerActor().getName()) <= lastLevel)
				event.getListenerActor().addAction(Actions.color(Color.WHITE));			
		}
	};
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		  case Keys.ESCAPE:
		  case Keys.BACK:
			  if(buyImg != null && buyImg.isVisible())
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
