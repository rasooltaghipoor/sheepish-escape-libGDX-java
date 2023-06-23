package com.baranagames.sheepishescape.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;
import com.baranagames.sheepishescape.MyFirstGame.GameState;
import com.baranagames.sheepishescape.screens.CommentScreen;
import com.baranagames.sheepishescape.screens.FirstScreen;
import com.baranagames.sheepishescape.screens.MainMenuScreen;

public class PauseMenu extends Table{
	//Table table;
	private MyFirstGame game;
	private boolean showButtons;
	private Image resumeButton , resetButton, menuButton , nextButton , winnerImg;
	private int currentLevel;
	private FirstScreen screen;
	//private static PauseMenu instance = null;
	
	public PauseMenu(MyFirstGame game , FirstScreen screen , int level) {
		this.screen = screen;
		showButtons = false;
		currentLevel = level;
		this.game = game;		
		// TODO Auto-generated constructor stubMyFirstGame
		//table = new Table();
		
		//setBounds(FirstScreen.VIEWPORT_WIDTH/2-3, FirstScreen.VIEWPORT_HEIGHT/2-1, 6, 2);
		 setBounds(MyFirstGame.VIEWPORT_WIDTH/2 - 13,  MyFirstGame.VIEWPORT_HEIGHT/2-15 ,26, 35);
		 setFillParent(false);		
		// setBackground(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("backc.png")))));

		 resumeButton = new Image(Assets.BUTTONS[3]);// new Image(Assets.getImg("resume"));		 
		 resumeButton.setSize(10, 10);
		 resumeButton.setPosition(8, 0);
		 resumeButton.setOrigin(5,5);
		 resumeButton.setName("resume");
		 resumeButton.addListener(myClickListener);
		 addActor(resumeButton);
		// add(resumeButton).width(4).height(4).pad(0.25f);
		 
		 resetButton = new Image(Assets.BUTTONS[1]);		 
		 resetButton.setSize(10, 10);
		 resetButton.setPosition(2f, 11);
		 resetButton.setOrigin(5,5);
		 resetButton.setName("reset");
		 resetButton.addListener(myClickListener);
		 addActor(resetButton);
		 //add(resetButton).width(4).height(4).pad(0.25f);
		 
		 menuButton = new Image(Assets.BUTTONS[0]);		 
		 menuButton.setSize(10, 10);
		 menuButton.setPosition(14, 11);
		 menuButton.setOrigin(5,5);
		 menuButton.setName("menu");
		 menuButton.addListener(myClickListener);
		 addActor(menuButton);	
		//add(menuButton).width(4).height(4).pad(0.25f);;
		 
		 nextButton = new Image(Assets.BUTTONS[2]);		 
		 nextButton.setSize(10, 10);
		 nextButton.setPosition(8, 22);
		 nextButton.setOrigin(5,5);
		 nextButton.setName("next");
		 nextButton.addListener(myClickListener);
		 addActor(nextButton);
		 //add(nextButton).width(4).height(4).pad(0.25f);		 
		 winnerImg = new Image(Assets.OTHERS[9]);		 
		 winnerImg.setSize(10, 10);
		 winnerImg.setPosition(8, 0);		
		 winnerImg.setOrigin(5,5);
		 winnerImg.setName("share");
		 winnerImg.addListener(myClickListener);
		 winnerImg.setVisible(false);
		 addActor(winnerImg);
		
	} 	
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);			
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub	
		if(showButtons)
		   super.draw(batch, parentAlpha);				
	}	
	
	public void setWinnerImg(int winnerScore){
		
	}
	
	public void hideExtraButtons(){		
		showButtons = true;
		resumeButton.setVisible(true);
		winnerImg.setVisible(false);
		nextButton.setVisible(true);
		if(MyFirstGame.gameState == GameState.PAUSE) {
			nextButton.setVisible(false);
			resumeButton.addAction(Actions.color(Color.WHITE));//scaleTo(1f, 1f));
		}
		else if(MyFirstGame.gameState == GameState.GAMEWIN)  { 
			Assets.ingameMusic.stop();
			resumeButton.setVisible(false);
		//	winnerImg = new Image(Assets.textureAtlas.findRegion("stageOn"+Integer.toString(winnerScore)));	
			//winnerImg.setDrawable(new SpriteDrawable(new Sprite(Assets.OTHERS[winnerScore+6])));	
			winnerImg.setVisible(true);
		}
		else if(MyFirstGame.gameState == GameState.GAMEOVER)  {
			Assets.ingameMusic.stop();
			nextButton.setVisible(false);
			resumeButton.setVisible(false);
		}
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
					//resumeButton.addAction(Actions.color(Color.WHITE));
				}
				String evtName = event.getListenerActor().getName();
				//*********************if(MyFirstGame.gameState == GameState.GAMEWIN && event.getListenerActor().getName().equals("next")){
				if(evtName.equals("next")){					
					//if(currentLevel == 24 || currentLevel == 48 || currentLevel == 72 || currentLevel == 96 || currentLevel == 120)
					if(!MyFirstGame.IS_PREMIUM && currentLevel == MyFirstGame.buyLevel-1)
					{
						screen.setBuyVisible();
						return;
					}
					if(currentLevel % 24 == 0)
						game.setScreen(new CommentScreen(game,currentLevel+1));
					else 
						game.setScreen(new FirstScreen(game,currentLevel+1));
					screen.dispose();					
				}
				
				if(evtName.equals("reset")) {					
					if(MyFirstGame.SOUNDS_ON && !Assets.ingameMusic.isPlaying())
						Assets.ingameMusic.play();					
					screen.reset();
					resetButton.addAction(Actions.color(Color.WHITE));
				}
				else if(evtName.equals("menu")) {
					if(Assets.ingameMusic.isPlaying())
						Assets.ingameMusic.stop();
					game.setScreen(new MainMenuScreen(game));
					screen.dispose();
				}
				else if(evtName.equals("share")) {
					if (MyFirstGame.actionResolver.getSignedInGPGS()) {
						MyFirstGame.actionResolver.submitScoreGPGS(MyFirstGame.TOTAL_SCORE);					
						MyFirstGame.actionResolver.getLeaderboardGPGS();				
					}
					else  MyFirstGame.actionResolver.loginGPGS();
				}
			}
		}
		
		@Override
		public void	enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
			if(showButtons) {
				if(MyFirstGame.SOUNDS_ON)
					Assets.buttonSnd.play(MyFirstGame.VOLUME+0.4f);
				event.getListenerActor().addAction(Actions.color(Color.YELLOW));
			}
			    //event.getListenerActor().addAction(scaleTo(1.2f, 1.2f));				
		}
		@Override
		public void	exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
			if(showButtons)
				event.getListenerActor().addAction(Actions.color(Color.WHITE));
			   // event.getListenerActor().addAction(scaleTo(1f, 1f));			
		}
	};
	
	public void destroy(){		
		clearActions();
		clearChildren();
		clear();
		//remove();				
	}		
		
}


