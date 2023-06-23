package com.baranagames.sheepishescape.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class HUDs2 extends Group{

	private Sprite scoreBoard ;
	private Image foals , moves, maxFoal , maxMove, happy , leftGoals , rightGoals , sideIndicator;
	private Image sideSheep , scoreBoard2 , sideIndicator2;	
		
	public HUDs2(int allowedMoves , int allowdFoals , int dir)
	{
		scoreBoard = new Sprite(Assets.HUDS[11]);//Assets.textureAtlas.findRegion("scoreBoard"));
		scoreBoard.setOrigin(0, 0);
		scoreBoard.setSize(25, 3);		
				
		foals = new Image(Assets.HUDS[0]);
		foals.setOrigin(1.5f, 1.5f);
		foals.setSize(3, 3);
		addActor(foals);		
		maxFoal = new Image(Assets.HUDS[allowdFoals]);
		maxFoal.setOrigin(1.5f, 1.5f);
		maxFoal.setSize(3, 3);		
		maxFoal.addAction(sequence(parallel(Actions.moveBy(0, -10, 0f),scaleTo(1.25f, 1.25f, 0.5f)),delay(1f),parallel(Actions.moveBy(0, 10, 0.5f),scaleTo(1, 1, 0.5f))));
		addActor(maxFoal);		
		
		moves = new Image(Assets.HUDS[0]);
		moves.setOrigin(1.5f, 1.5f);
		moves.setSize(3, 3);		
		addActor(moves);		
		maxMove = new Image(Assets.HUDS[allowedMoves]);
		maxMove.setOrigin(1.5f, 1.5f);
		maxMove.setSize(3, 3);		
		maxMove.addAction(sequence(parallel(Actions.moveBy(0, -10, 0f),scaleTo(1.25f, 1.25f, 0.5f)),delay(1f),parallel(Actions.moveBy(0, 10, 0.5f),scaleTo(1, 1, 0.5f))));
		addActor(maxMove);
		
		setScoreboardPosition(dir);
		
		happy = new Image(Assets.HUDS[14]);
		happy.setSize(8, 8);
		happy.setOrigin(4, 4);	
		happy.addAction(fadeOut(0));
		//happy.setPosition(MyFirstGame.VIEWPORT_WIDTH/2-happy.getWidth()/2, MyFirstGame.VIEWPORT_HEIGHT/2-happy.getHeight()/2);
		addActor(happy);
		
		scoreBoard2 = new Image(Assets.HUDS[13]);
		scoreBoard2.setOrigin(0, 0);
		scoreBoard2.setSize(20, 3);
		scoreBoard2.setPosition(MyFirstGame.VIEWPORT_WIDTH/2 - 10, 1);
		addActor(scoreBoard2);
		leftGoals = new Image(Assets.HUDS[0]);
		leftGoals.setOrigin(1.5f, 1.5f);
		leftGoals.setSize(3, 3);
		leftGoals.setPosition(scoreBoard2.getX()+4f, 1);
		addActor(leftGoals);
		rightGoals = new Image(Assets.HUDS[0]);
		rightGoals.setOrigin(1.5f, 1.5f);
		rightGoals.setSize(3, 3);
		rightGoals.setPosition(scoreBoard2.getX()+12.75f, 1);
		addActor(rightGoals);
		
		sideIndicator = new Image(Assets.HUDS[16]);
		sideIndicator.setSize(10 , 5);	
		sideIndicator.setOrigin(5 , 2.5f);		
	//	sideIndicator.addAction(hide());
	    addActor(sideIndicator);
	    
	    sideIndicator2 = new Image(Assets.OTHERS[20]);
	    sideIndicator2.setSize(10, 10);
	    sideIndicator2.setOrigin(5 , 5);	
	    sideIndicator2.setPosition(MyFirstGame.VIEWPORT_WIDTH/2 - 5, MyFirstGame.VIEWPORT_HEIGHT/2 - 5);	   
	    sideIndicator2.addAction(fadeOut(0));
		addActor(sideIndicator2);
	    
	    sideSheep = new Image(Assets.HUDS[15]);
	    sideSheep.setSize(3 , 3);	
	    sideSheep.setOrigin(1.5f, 1.5f);	
	    sideSheep.setPosition(scoreBoard2.getX() + scoreBoard2.getWidth()/2, 1);
		addActor(sideSheep);	
		
		/*cursorImg = new Image(new Texture(Gdx.files.internal("cursor.png")));
		cursorImg.setSize(2, 2);	
		addActor(cursorImg);*/
		
		
	}
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);	
		/*mousePoint.set(Gdx.input.getX(), Gdx.input.getY(),0);
		getStage().getCamera().unproject(mousePoint);
		cursorImg.setPosition(mousePoint.x-1f, mousePoint.y-1f);	*/	
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub	
		super.draw(batch, parentAlpha);
		scoreBoard.draw(batch);
		//scoreBoard2.draw(batch);		
	}		
	
	public void showPlayerSide(int dir){	
		sideIndicator.clearActions();
		if(MyFirstGame.SOUNDS_ON)
			Assets.changeSnd.play(MyFirstGame.VOLUME+0.5f);
		if(dir == 1){  // right
			sideIndicator.setColor(Color.MAGENTA);
			sideIndicator.setRotation(0);
			sideIndicator.setPosition(MyFirstGame.VIEWPORT_WIDTH/2 - 12, 50);
			sideIndicator.addAction(sequence(delay(1),Actions.moveBy(-10, 0, 0.75f),Actions.moveBy(10, 0, 0.75f)
											,Actions.moveBy(-10, 0, 0.75f),Actions.moveBy(10, 0, 0.75f),Actions.moveBy(0, 5,0.5f),Actions.run(activeOtherPlayer)));
			sideIndicator2.setColor(Color.MAGENTA);			
			sideIndicator2.addAction(repeat(3, sequence(fadeIn(0.25f),delay(0.75f),fadeOut(0.25f))));
			
			sideSheep.setColor(Color.MAGENTA);
			sideSheep.addAction(moveTo(scoreBoard2.getX()-2 + scoreBoard2.getWidth(), 1, 1));
		}
		else{
			sideIndicator.setColor(Color.WHITE);
			sideIndicator.setRotation(180);
			sideIndicator.setPosition(MyFirstGame.VIEWPORT_WIDTH/2 + 4  , 50);
			sideIndicator.addAction(sequence(delay(1),Actions.moveBy(10, 0, 0.75f),Actions.moveBy(-10, 0, 0.75f)
											,Actions.moveBy(10, 0, 0.75f),Actions.moveBy(-10, 0, 0.75f),Actions.moveBy(0, 5,0.5f),Actions.run(activeOtherPlayer)));
			sideIndicator2.setColor(Color.WHITE);			
			sideIndicator2.addAction(repeat(3, sequence(fadeIn(0.25f),delay(0.75f),fadeOut(0.25f))));
			
			sideSheep.setColor(Color.WHITE);
			sideSheep.addAction(moveTo(scoreBoard2.getX(), 1, 1));
		}		
	}
	
	Runnable activeOtherPlayer = new Runnable() {		
		@Override
		public void run() {
			// TODO Auto-generated method stub			
			MyFirstGame.PLAYER_READY = true;			
		}
	};
	
	public void setScoreboardPosition(int dir){		
		if(dir == 0) // move left 
			scoreBoard.setPosition(10, 56);		
		else // move right 
		    scoreBoard.setPosition(MyFirstGame.VIEWPORT_WIDTH - 40, 56);	
		moves.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[0])));
		foals.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[0])));
		foals.setPosition(scoreBoard.getX()+5.2f, 56);
		maxFoal.setPosition(scoreBoard.getX()+9.7f, 56);
		moves.setPosition(scoreBoard.getX()+20.7f, 56);
		maxMove.setPosition(scoreBoard.getX()+25.3f, 56);		
	}
	
	public void addGoal(int goalNo , int side , int who) {
		if(MyFirstGame.SOUNDS_ON && goalNo < 10)
			if(who == side)
				Assets.goalSnd.play(MyFirstGame.VOLUME+0.5f);
			else 
				Assets.goalSnd2.play(MyFirstGame.VOLUME+0.5f);
		happy.clearActions();
		if(side == 0)  { // left
			leftGoals.clearActions();
			leftGoals.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[goalNo])));
			leftGoals.addAction(sequence(scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1),delay(0.5f),scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1)));
			happy.setColor(Color.WHITE);
			happy.setPosition(MyFirstGame.VIEWPORT_WIDTH/4-happy.getWidth()/2,  MyFirstGame.VIEWPORT_HEIGHT/2-happy.getHeight()/2);
			happy.addAction(sequence(fadeIn(0.25f),Actions.moveBy(0,-8,0.5f),Actions.moveBy(0,8,0.5f),
					Actions.moveBy(0,-8,0.5f),Actions.moveBy(0,8,0.5f),delay(1),fadeOut(0.25f)));
			//happy.addAction(sequence(fadeIn(0.3f),scaleTo(2f, 2f,0.5f),delay(0.5f)
				//	,parallel(Actions.rotateBy(360,2),fadeOut(2f),scaleTo(1f, 1f,2))));
		}
		else {
			rightGoals.clearActions();			
			rightGoals.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[goalNo])));			
			rightGoals.addAction(sequence(scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1),delay(0.5f),scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1)));
			happy.setColor(Color.MAGENTA);
			happy.setPosition(MyFirstGame.VIEWPORT_WIDTH*0.75f - happy.getWidth()/2, MyFirstGame.VIEWPORT_HEIGHT/2-happy.getHeight()/2);
			happy.addAction(sequence(fadeIn(0.25f),Actions.moveBy(0,-8,0.5f),Actions.moveBy(0,8,0.5f),
					Actions.moveBy(0,-8,0.5f),Actions.moveBy(0,8,0.5f),delay(1),fadeOut(0.25f)));
			//happy.addAction(sequence(fadeIn(0.3f),scaleTo(2f, 2f,0.5f),delay(0.5f)
				//	,parallel(Actions.rotateBy(360,2),fadeOut(2f),scaleTo(1f, 1f,2))));
		}
		
		
		/*happy.addAction(sequence(fadeIn(0.3f),scaleTo(2f, 2f,0.5f),delay(0.5f)
				,parallel(Actions.rotateBy(360,2),fadeOut(2f),scaleTo(1f, 1f,2))));*/
				//,scaleTo(1, 1,0.5f),delay(0.5f),scaleTo(2f, 2f,0.5f),delay(0.5f),scaleTo(1, 1,0.5f)
				
	}
	
	public void addMove(int moveNo){
		//System.out.println(MyFirstGame.VIEWPORT_WIDTH/scoreBoard.getWidth());
		moves.clearActions();
		moves.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[moveNo])));
		moves.addAction(sequence(scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1),delay(0.5f),scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1)));
	}
	
	public void addFoal(int foalNo) {
		foals.clearActions();
		foals.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[foalNo])));
		foals.addAction(sequence(scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1),delay(0.5f),scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1)));
	}	
	
	public void showFinalResult(){
		rightGoals.clearActions();
		leftGoals.clearActions();
		rightGoals.addAction(parallel(scaleTo(2, 2,2),Actions.moveTo(MyFirstGame.VIEWPORT_WIDTH/2 + 10, 15,2),Actions.rotateBy(360,2)));
		leftGoals.addAction(parallel(scaleTo(2, 2,2),Actions.moveTo(MyFirstGame.VIEWPORT_WIDTH/2 - 13, 15,2),Actions.rotateBy(360,2)));
	}
	
	public void reset(int allowedMoves , int allowdFoals){
		maxMove.clearActions();
		maxFoal.clearActions();
		foals.clearActions();
		moves.clearActions();
		leftGoals.clearActions();
		rightGoals.clearActions();
		maxMove.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[allowedMoves])));
		maxFoal.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[allowdFoals])));
		moves.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[0])));
		foals.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[0])));
		maxFoal.addAction(sequence(parallel(Actions.moveBy(0, -10, 0f),scaleTo(1.25f, 1.25f, 0.5f)),delay(1f),parallel(Actions.moveBy(0, 10, 0.5f),scaleTo(1, 1, 0.5f))));
		maxMove.addAction(sequence(parallel(Actions.moveBy(0, -10, 0f),scaleTo(1.25f, 1.25f, 0.5f)),delay(1f),parallel(Actions.moveBy(0, 10, 0.5f),scaleTo(1, 1, 0.5f))));
		leftGoals.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[0])));
		leftGoals.addAction(scaleTo(1, 1));
		leftGoals.setPosition(scoreBoard2.getX()+4f, 1);
		rightGoals.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[0])));
		rightGoals.addAction(scaleTo(1, 1));
		rightGoals.setPosition(scoreBoard2.getX()+12.75f, 1);

	}
	
	
	public void destroy(){	
		clearActions();
		clearChildren();
		clear();
		//remove();				
	}
	
}
