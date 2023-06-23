package com.baranagames.sheepishescape.actors;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;
import com.baranagames.sheepishescape.MyFirstGame.GameState;
import com.baranagames.sheepishescape.screens.LevelSelectionScreen2;
import com.baranagames.sheepishescape.screens.MainMenuScreen;
import com.baranagames.sheepishescape.screens.SecondScreen;


public class PauseMenu2 extends Table{
	//Table table;
	private final MyFirstGame game;
	private boolean showButtons;
	private Image resumeButton , resetButton, menuButton;
	private SecondScreen screen;	
	
	public PauseMenu2(final MyFirstGame game , SecondScreen screen , int level) {
		this.screen = screen;
		showButtons = false;		
		this.game = game;		
		// TODO Auto-generated constructor stub
		//table = new Table();
		//setBounds(FirstScreen.VIEWPORT_WIDTH/2-3, FirstScreen.VIEWPORT_HEIGHT/2-1, 6, 2);
		//setBackground(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("backc.png")))));
		 setBounds(MyFirstGame.VIEWPORT_WIDTH/2 - 13,  MyFirstGame.VIEWPORT_HEIGHT/2 - 10,26, 26);
		 setFillParent(false);				
		 resumeButton =  new Image(Assets.BUTTONS[3]);//new Image(Assets.getImg("resume"));		 
		 resumeButton.setSize(10, 10);
		 resumeButton.setPosition(8, 0);
		 resumeButton.setOrigin(5,5);
		 resumeButton.setName("resume");
		 resumeButton.addListener(myClickListener);
		 addActor(resumeButton);
		 
		 resetButton = new Image(Assets.BUTTONS[1]);		 
		 resetButton.setSize(10, 10);
		 resetButton.setPosition(2f, 11);
		 resetButton.setOrigin(5,5);
		 resetButton.setName("reset");
		 resetButton.addListener(myClickListener);
		 addActor(resetButton);
		 
		 menuButton = new Image(Assets.BUTTONS[0]);		 
		 menuButton.setSize(10, 10);
		 menuButton.setPosition(14, 11);
		 menuButton.setOrigin(5,5);
		 menuButton.setName("menu");
		 menuButton.addListener(myClickListener);
		 addActor(menuButton);		
		
	} 	
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub	
		if(showButtons)//FirstScreen.gameState == GameState.PAUSE  ||FirstScreen.gameState == GameState.RUNNING || FirstScreen.gameState == GameState.GAMEOVER)	 
		   super.draw(batch, parentAlpha);		
		//draw(batch, parentAlpha);
		//resumeButton.draw(batch, parentAlpha);
	}	
	
	public void hideExtraButtons(){		
		showButtons = true;
		resumeButton.setVisible(true);
		resumeButton.addAction(Actions.color(Color.WHITE));	
		if(MyFirstGame.gameState == GameState.GAMEWIN)   
			resumeButton.setVisible(false);		
	}
	
	public void setShowButtons(boolean flag){
		showButtons = flag;
	}
	
	public ClickListener myClickListener = new ClickListener() {
		@Override
		public void clicked (InputEvent event, float x, float y) {
			if(showButtons)
			{
				if(MyFirstGame.gameState == GameState.PAUSE && event.getListenerActor().getName().equals("resume")){
					MyFirstGame.gameState = GameState.RUNNING;
					showButtons = false;
				}
							
				if(event.getListenerActor().getName().equals("reset")) {
					if(MyFirstGame.SOUNDS_ON && !Assets.ingameMusic.isPlaying())
						Assets.ingameMusic.play();	
					screen.reset();
					resetButton.addAction(Actions.color(Color.WHITE));	
				}
				else if(event.getListenerActor().getName().equals("menu")){
					game.setScreen(new MainMenuScreen(game));
					screen.dispose();
				}
			}
		}
		
		@Override
		public void	enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
			if(showButtons){
				if(MyFirstGame.SOUNDS_ON)
					Assets.buttonSnd.play(MyFirstGame.VOLUME+0.4f);
				event.getListenerActor().addAction(Actions.color(Color.YELLOW));
			}
		}
		@Override
		public void	exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
			if(showButtons)
				event.getListenerActor().addAction(Actions.color(Color.WHITE));		
		}
	};
	/*public void setActive(boolean state){
		active = state;
	}*/
	public void destroy(){		
		clearActions();
		clearChildren();
		clear();
		//remove();				
	}
	
	
		
}


