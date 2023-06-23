package com.baranagames.sheepishescape.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class HUDs extends Group{

	private Sprite scoreBoard ;
	private Image foals , goals, maxFoal , maxGoal, happy , sheep ;
		
	public HUDs(int wantedGoals , int allowdFoals)
	{
		scoreBoard = new Sprite(Assets.HUDS[12]);
		scoreBoard.setOrigin(0, 0);
		scoreBoard.setSize(35, 3);
		scoreBoard.setPosition(MyFirstGame.VIEWPORT_WIDTH/2 - 17.5f, 56);
		
		sheep = new Image(Assets.SHEEPS[2]);
		sheep.setSize(5, 5);
		sheep.setOrigin(2.5f, 2.5f);	
		sheep.setPosition(-10,-10);
		//sheep.setPosition(MyFirstGame.VIEWPORT_WIDTH/2-happy.getWidth()/2, MyFirstGame.VIEWPORT_HEIGHT/2-happy.getHeight()/2);
		addActor(sheep);
		
		foals = new Image(Assets.HUDS[0]);
		foals.setOrigin(1.5f, 1.5f);
		foals.setSize(3, 3);
		foals.setPosition(scoreBoard.getX()+5.5f, 56);
		addActor(foals);
		goals = new Image(Assets.HUDS[0]);
		goals.setOrigin(1.5f, 1.5f);
		goals.setSize(3, 3);
		goals.setPosition(scoreBoard.getX()+28, 56);
		addActor(goals);
		
		happy = new Image(Assets.HUDS[14]);
		happy.setSize(8, 8);
		happy.setOrigin(4, 4);	
		happy.addAction(fadeOut(0));
		happy.setPosition(goals.getX() - 2.5f, MyFirstGame.VIEWPORT_HEIGHT - 13);
		happy.addAction(fadeOut(0));
		addActor(happy);
		
		maxFoal = new Image(Assets.HUDS[allowdFoals]);
		maxFoal.setOrigin(1.5f, 1.5f);
		maxFoal.setSize(3, 3);
		maxFoal.setPosition(scoreBoard.getX()+10.5f, 56);
		maxFoal.addAction(sequence(parallel(Actions.moveBy(0, -10, 0f),scaleTo(1.25f, 1.25f, 0.5f)),delay(1f),parallel(Actions.moveBy(0, 10, 0.5f),scaleTo(1, 1, 0.5f))));
		addActor(maxFoal);
		maxGoal = new Image(Assets.HUDS[wantedGoals]);
		maxGoal.setOrigin(1.5f, 1.5f);
		maxGoal.setSize(3, 3);
		maxGoal.setPosition(scoreBoard.getX()+33, 56);
		maxGoal.addAction(sequence(parallel(Actions.moveBy(0, -10, 0f),scaleTo(1.5f, 1.25f, 0.25f)),delay(1f),parallel(Actions.moveBy(0, 10, 0.5f),scaleTo(1, 1, 0.5f))));
		addActor(maxGoal);		
		
				
		/*Image cursorImg = new Image(new Texture(Gdx.files.internal("cursor.png")));
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
		scoreBoard.draw(batch);
		super.draw(batch, parentAlpha);
		
		//foals.draw(batch, parentAlpha);
	   // goals.draw(batch, parentAlpha);
	   // maxFoal.draw(batch);
	   // maxGoal.draw(batch);
	}		
	
	public float getXPosition(int type){
		if(type == 0)  // foals
			return scoreBoard.getX()+8;
		else //goals
			return scoreBoard.getX()+37;
	}
	
	public void addGoal(int goalNo) {
		if(MyFirstGame.SOUNDS_ON)
			Assets.goalSnd.play(MyFirstGame.VOLUME+0.5f);
		goals.clearActions();
		goals.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[goalNo])));
		goals.addAction(sequence(scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1),delay(0.5f),scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1)));

		//goals.addAction(sequence(parallel(scaleTo(2f, 2f,0.3f),Actions.moveBy(0, -2, 0.3f)),delay(0.5f),parallel(scaleTo(1, 1,0.3f),Actions.moveBy(0, 2, 0.3f))));
		//goals.addAction(sequence(scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1),delay(0.5f),scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1)));
		//goals.addAction(sequence(Actions.rotateBy(180, 0.5f),delay(0.5f),Actions.rotateBy(180, 0.5f)));
		happy.clearActions();
		happy.setY(MyFirstGame.VIEWPORT_HEIGHT - 13);
		//happy.addAction(sequence(fadeIn(0.3f),scaleTo(2f, 2f,0.5f),delay(0.5f)
			//	,parallel(Actions.rotateBy(360,2),fadeOut(2f),scaleTo(1f, 1f,2))));
		//happy.addAction(sequence(fadeIn(0.5f),fadeOut(0.5f),fadeIn(0.5f),fadeOut(0.5f),fadeIn(0.5f),fadeOut(0.5f)));		
		happy.addAction(sequence(fadeIn(0.25f),Actions.moveBy(0,-3,0.5f),Actions.moveBy(0,3,0.5f),
				Actions.moveBy(0,-3,0.5f),Actions.moveBy(0,3,0.5f),delay(1),fadeOut(0.25f)));				
	}
	
	public void showLost(){		
		if(MyFirstGame.SOUNDS_ON)
			Assets.lostSnd.play(MyFirstGame.VOLUME+0.5f);
		happy.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[17])));
		happy.setPosition(foals.getX() - 2.5f, MyFirstGame.VIEWPORT_HEIGHT - 13);
		happy.clearActions();
		happy.addAction(fadeIn(0.25f));	
		happy.addAction(forever(sequence(Actions.moveBy(-5,0,1f),Actions.moveBy(5,0,1f),delay(1))));
	}
	
	public void addFoal(int foalNo) {
		foals.clearActions();
		foals.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[foalNo])));
		foals.addAction(sequence(scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1),delay(0.5f),scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1)));
		//foals.addAction(sequence(parallel(scaleTo(2f, 2f,0.3f),Actions.moveBy(0, -2, 0.3f)),delay(0.3f),parallel(scaleTo(1, 1,0.3f),Actions.moveBy(0, 2, 0.3f))));
		//foals.addAction(sequence(scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1),delay(0.5f),scaleTo(1.2f, 1.2f),delay(0.5f),scaleTo(1, 1)));
	}	
	
	
	public void showScapee(float x, float y , Vector2 gatePos){
		sheep.setPosition(x-2.5f, y-2.5f);
		sheep.addAction(Actions.sequence(Actions.moveTo(gatePos.x - 2, gatePos.y - sheep.getHeight()/2, 0.75f),Actions.moveBy(15, 0, 0.75f)));
	}
	//public void addHint
	
	public void reset(int wantedGoals , int allowdFoals){
		maxGoal.clearActions();
		maxFoal.clearActions();
		foals.clearActions();
		goals.clearActions();
		maxGoal.setPosition(scoreBoard.getX()+33, 56);
		maxFoal.setPosition(scoreBoard.getX()+10.5f, 56);
		maxGoal.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[wantedGoals])));
		maxFoal.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[allowdFoals])));
	    goals.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[0])));
		foals.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[0])));
		maxFoal.addAction(sequence(parallel(Actions.moveBy(0, -10, 0f),scaleTo(1.25f, 1.25f, 0.5f)),delay(1f),parallel(Actions.moveBy(0, 10, 0.5f),scaleTo(1, 1, 0.5f))));
		maxGoal.addAction(sequence(parallel(Actions.moveBy(0, -10, 0f),scaleTo(1.25f, 1.25f, 0.5f)),delay(1f),parallel(Actions.moveBy(0, 10, 0.5f),scaleTo(1, 1, 0.5f))));
		happy.clearActions();
		happy.addAction(fadeOut(0f));
		happy.setDrawable(new SpriteDrawable(new Sprite(Assets.HUDS[14])));
		happy.setPosition(goals.getX() - 5, MyFirstGame.VIEWPORT_HEIGHT - 15);
	}
	
	public void destroy(){
		clearActions();
		clearChildren();
		clear();		
		//remove();				
	}
	
	
	
	
}
