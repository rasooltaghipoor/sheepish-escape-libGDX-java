package com.baranagames.sheepishescape.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;
import com.baranagames.sheepishescape.MyFirstGame.GameState;


public class Sheeps extends Group{
 	
	private Array<Body> bodies = new Array<Body>();
	private World world;
    private int deadCounter , sheepStatus;
	private float deadTime;
	private Body ground , bitedBody;
	private Sprite activeBodyIndicator , line , energyBar;	
	private boolean canDrawing , correctlyPassed , sheepIsMoving;	
	private float sleepTime[]={-1,-1,-1};	
	
	public Sheeps(World world, Body ground)
	{
		this.world = world;
		sheepStatus = -1;
		deadTime = 0;
		deadCounter = 1;
		bitedBody = null;
		this.ground = ground;
		sheepIsMoving = false;
		correctlyPassed = false;
		canDrawing = false;
		
		for (Body box : bodies)
			if(box != null)  world.destroyBody(box);
		bodies.clear();			
		
		activeBodyIndicator = new Sprite(Assets.SHEEPS[8]);
		activeBodyIndicator.setSize(12f, 12f);
		activeBodyIndicator.setPosition(-20, 0);
		line = new Sprite(Assets.SHEEPS[6]);
		line.setOrigin(0, 0);	
		energyBar = new Sprite(Assets.textureAtlas.findRegion("engBar0"));//new Texture(Gdx.files.internal("engBar0.png")));
		energyBar.setSize(5f, 1f);
				
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		if(MyFirstGame.gameState ==GameState.RUNNING)	
		{			
			super.act(delta);
			
			int i = 0;
			Vector2 sheepPos;
			Body box;
			for(Actor image : getChildren())
			{			
				box = bodies.get(i);
				sheepPos = box.getPosition();
				image.setPosition(sheepPos.x - image.getWidth()/2, sheepPos.y - image.getHeight()/2);
				image.setRotation(box.getAngle() * MathUtils.radiansToDegrees);	
				if(box.getLinearVelocity().x >= 0.3)  box.setLinearVelocity(box.getLinearVelocity().sub(0.12f, 0));
				else if(box.getLinearVelocity().x <= -0.3)  box.setLinearVelocity(box.getLinearVelocity().sub(-0.12f, 0));
				if(box.getLinearVelocity().y >= 0.3)  box.setLinearVelocity(box.getLinearVelocity().sub(0, 0.12f));
				else if(box.getLinearVelocity().y <= -0.3)  box.setLinearVelocity(box.getLinearVelocity().sub(0, -0.12f));
						
		        if(bitedBody != null && box.equals(bitedBody)){
		        	deadTime += delta;
		        	energyBar.setPosition(box.getPosition().x - energyBar.getWidth()/2, box.getPosition().y + 3);
					/*if(deadCounter == 0){							
						//Image temp = (Image)image;
						//temp.setDrawable(new SpriteDrawable(new Sprite(Assets.textureAtlas.findRegion("ball"+Integer.toString(deadCounter)))));
						deadCounter++;
						//image.setName("changed");
					}*/
					if(deadTime >= 6.75f){
						deadTime = 0;							
						if(deadCounter > 3){
							activeBodyIndicator.setPosition(-20, 0);
							Image temp = (Image)image;
							box.setUserData("inactive");
							//box.setFixedRotation(true);
							box.setTransform(box.getPosition(), 0);
							temp.setDrawable(new SpriteDrawable(new Sprite(Assets.SHEEPS[5])));	
							image.setName("changed");
							canDrawing = false;
							sheepIsMoving = false;
							bitedBody = null;
						}
						else{								
							//temp.setDrawable(new SpriteDrawable(new Sprite(Assets.textureAtlas.findRegion("ball"+Integer.toString(deadCounter)))));
							energyBar.setRegion(Assets.textureAtlas.findRegion("engBar"+deadCounter));// Texture(new Texture(Gdx.files.internal("engBar"+deadCounter+".png")));
							deadCounter++;
						}
						
					}
				}
						
				if(box.getUserData().equals("balljoon")){					
					if(sheepStatus == 0){	 // normal	
						Image temp = (Image)image;
						temp.setDrawable(new SpriteDrawable(new Sprite(Assets.SHEEPS[0])));
						sheepStatus = -1;
					}
					else if(sheepStatus == 1){  // selected
						image.setName("changed");
						Image temp = (Image)image;
						temp.setDrawable(new SpriteDrawable(new Sprite(Assets.SHEEPS[1])));
						sheepStatus = -1;
					}
					else if(sheepStatus == 2){  //moving
						Image temp = (Image)image;
						temp.setDrawable(new SpriteDrawable(new Sprite(Assets.SHEEPS[2])));
						sheepStatus = -1;
					}					
					activeBodyIndicator.setPosition(box.getPosition().x - 6,box.getPosition().y - 6);					
				}
				else if(box.getUserData().equals("ball") && image.getName().equals("changed")){
					Image temp = (Image)image;
					temp.setDrawable(new SpriteDrawable(new Sprite(Assets.SHEEPS[0])));
					image.setName("sheep");
					image.setVisible(true);
				}
				else if(box.getUserData().equals("escaped")){
					image.setVisible(false);
					image.setName("changed");
					box.setUserData("escapee");
					
				//	System.out.println("inja ke kar mikone");
				}
				else if(box.getUserData().equals("sleepy")){					
					if(sleepTime[i] == -1) {
						activeBodyIndicator.setPosition(-20, 0);
						Image temp = (Image)image;
						temp.setDrawable(new SpriteDrawable(new Sprite(Assets.SHEEPS[3])));
						sleepTime[i] = 0;
						canDrawing = false;
						sheepIsMoving = false;
						image.setName("changed");
					}
					sleepTime[i] += Gdx.graphics.getDeltaTime();
							//System.out.println(sleepTime[i]);
					if(sleepTime[i] >= 20){
						box.setUserData("ball");
						image.setName("sheep");
						sleepTime[i] = -1;
						Image temp = (Image)image;
						temp.setDrawable(new SpriteDrawable(new Sprite(Assets.SHEEPS[0])));
					}
				}
				else if(box.getUserData().equals("eaten")){
						//	box.setUserData("");
					activeBodyIndicator.setPosition(-20, 0);
					box.setUserData("inactive");
					image.setName("changed");
					//box.setFixedRotation(true);
					box.setAngularVelocity(0);
					box.setTransform(box.getPosition(), 0);
					Image temp = (Image)image;
					temp.setDrawable(new SpriteDrawable(new Sprite(Assets.SHEEPS[5])));						
					canDrawing = false;
					sheepIsMoving = false;
				}
				else if(box.getUserData().equals("caged")){
					//	box.setUserData("");
					activeBodyIndicator.setPosition(-20, 0);
					box.setUserData("inactive");
					image.setName("changed");
					box.setFixedRotation(true);
					box.setAngularVelocity(0);
					box.setTransform(box.getPosition(), 0);
					Image temp = (Image)image;
					temp.setDrawable(new SpriteDrawable(new Sprite(Assets.SHEEPS[4])));	
					RevoluteJointDef jd = new RevoluteJointDef();		
					jd.collideConnected = false;
					Vector2 anchor =new Vector2(sheepPos.x,sheepPos.y);
					jd.initialize(ground, box, anchor);
					world.createJoint(jd);
					canDrawing = false;
					sheepIsMoving = false;
				}
				else if(box.getUserData().equals("crazy")){
					
				}
			
			    i++;
				
			}		
			
		    if(canDrawing) {
		    	
			    Vector2 pos[] = new Vector2[2];
			    Vector2 posssss=null;
			    int j=0;
			    for(i=0; i<3; i++){
			    			    	
			    	if(bodies.get(i).getUserData().equals("ball") || bodies.get(i).getUserData().equals("sleepy") 
			    			|| bodies.get(i).getUserData().equals("inactive")) {
			    		//System.out.println(bodies.get(i).getUserData());
			    		pos[j] = bodies.get(i).getPosition();
			            j++;
			    	}
			    	else posssss = bodies.get(i).getPosition();
			    }		    
			    float distance = pos[0].dst(pos[1]);
			   // float angle =  pos[0].angleRad(pos[1]);		 
			    float angle = ((float)Math.atan2(pos[0].y-pos[1].y, pos[1].x-pos[0].x)) * MathUtils.radiansToDegrees;		  
			    line.setPosition(pos[0].x,pos[0].y);	   
			    line.setSize(distance, 0.4f);
			    line.setRotation(-angle);	
			    //System.out.println(distance);
			    if(sheepIsMoving && Math.abs(distance - (pos[0].dst(posssss) + posssss.dst(pos[1]))) < 0.2) {
			    	canDrawing = false;
			    	correctlyPassed = true;
			    	sheepIsMoving = false;			    	
			    	//System.out.println("dddddd");
			    }
		    }
	    
	    }
	   
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub	
		//if(state.equals("alive")) {
		activeBodyIndicator.draw(batch);
		if(canDrawing){		   		
		    line.draw(batch);
		}
		if(bitedBody != null)
			energyBar.draw(batch);
		super.draw(batch, parentAlpha);
		
	}	
		
	public void setSelected(int sheepSt ){
		sheepStatus = sheepSt;
	}
	
	public void setBitedBody(Body body){
		bitedBody = body;
		deadCounter = 1;
		deadTime = 0;
	}
	
	public void drawLine(boolean state){
		canDrawing = state;	
		correctlyPassed = false;
	}	
	
	public void setSheepMoving(boolean state){
		sheepIsMoving = state;
	}
	
	public void resetIndicator(){
		activeBodyIndicator.setPosition(-20, 0);		
	}
	
	public void checkBited(Body escBody){
		activeBodyIndicator.setPosition(-20, 0);
		if(bitedBody != null && bitedBody == escBody){
			//System.out.println("ssss");
			bitedBody = null;
			deadCounter = 1;
			deadTime = 0;
		}
	}
	
	public void reorderSheeps() {
		//goals.setDrawable(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("dig"+goalNo+".png")))));
		canDrawing = false;
		correctlyPassed = false;
		sheepIsMoving = false;
		for(int i=0; i<bodies.size; i++){			
			bodies.get(i).setLinearVelocity(-MathUtils.random(50, 100), -MathUtils.random(50, 100));
			//System.out.println(bodies.get(i).getUserData());
			if(bodies.get(i).getUserData().equals("escapee")){
				bodies.get(i).setUserData("ball");					
			}			
		}		
	}
	
	public boolean isCorrectlyPassed(){
		return correctlyPassed;
	}
	
	public boolean isFoal() {
		//System.out.println(canDrawing+"  "+sheepIsMoving+"  "+correctlyPassed);
		 if(sheepIsMoving && !correctlyPassed) {
		    sheepIsMoving = false;	    	
		    return true;		    	
		 }
		 return false;
	}
	
	public void changeColor(int playerNo){
		Color color;
		if(playerNo == 0) color = Color.WHITE;
		else color = Color.MAGENTA;
			
		for(Actor image : getChildren())
			image.setColor(color);			
	}
	
	public void addSheepBody(String name, float radiaus , float x , float y)
	{
		  BodyDef bodyDef = new BodyDef();  
	     bodyDef.type = BodyType.DynamicBody;  
	     bodyDef.position.set(x, y);  
	     Body sheepModel = world.createBody(bodyDef);  
	     CircleShape dynamicCircle = new CircleShape();  
	     dynamicCircle.setRadius(radiaus);  
	     FixtureDef fixtureDef = new FixtureDef();  
	     fixtureDef.shape = dynamicCircle;  
	     fixtureDef.density = 1f;  
	     fixtureDef.friction = 0.5f;  
	     fixtureDef.restitution = 0.5f;  
	     sheepModel.createFixture(fixtureDef);
	     sheepModel.setUserData(name);
	    // sheepModel.setFixedRotation(true);
	     dynamicCircle.dispose();
	     //System.out.println(sheepModel.getMass());
	     sheepModel.setLinearVelocity(0, 0);
		 sheepModel.setAngularVelocity(0);		 
		 bodies.add(sheepModel);
		Image sheepImage = new Image(Assets.SHEEPS[0]);
		sheepModel.setBullet(true);
		//sheepTexture.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//sheepImage = new Image(sheepTexture);
		sheepImage.setSize(radiaus*2,radiaus*2);
		sheepImage.setOrigin(radiaus, radiaus);
		sheepImage.setName("sheep");
		//sheepImage.addAction(sequence(parallel(Actions.moveBy(0, -20),scaleTo(2, 2)),delay(0.5f),parallel(Actions.moveBy(0, 20, 0.5f),scaleTo(1, 1, 0.5f))));
		sheepImage.addAction(sequence(scaleTo(1.25f, 1.25f),delay(0.5f),scaleTo(1, 1, 0.5f)));
		addActor(sheepImage);		
		
	}
	
	public void destroy(){		
		ground = bitedBody = null;
		bodies.clear();
		clearActions();
		clearChildren();
		clear();					
	}
	
	public void reset(float offset){
	
		deadTime = 0;
		deadCounter = 1;
		bitedBody = null;
		sheepIsMoving = false;
		correctlyPassed = false;
		canDrawing = false;
		energyBar.setRegion(Assets.textureAtlas.findRegion("engBar0")); // setTexture(new Texture(Gdx.files.internal("engBar0.png")));
		activeBodyIndicator.setPosition(-20, 0);
		int i= 0;
		for(Actor actor : getChildren()){
			sleepTime[i] = -1;
			if(actor.getName().equals("changed"))	{
				Image temp = (Image)actor;
				temp.setDrawable(new SpriteDrawable(new Sprite(Assets.SHEEPS[0])));
				actor.setName("sheep");
				actor.setVisible(true);
			}
			Body body = bodies.get(i);
			body.setUserData("ball");
			body.setFixedRotation(false);
			body.setLinearVelocity(0,0);
			body.setAngularVelocity(0);
			if(i == 0)	body.setTransform(15+offset, 20,0);
			else if(i == 1) body.setTransform(20+offset, 30,0);
			else body.setTransform(15+offset, 40,0);
			
			 Array<JointEdge> list = bodies.get(i).getJointList();
	    	 while (list.size > 0) {
	 	        world.destroyJoint(list.get(0).joint);
	 	     }
	    	 i++;
	    	 actor.addAction(sequence(scaleTo(1.25f, 1.25f),delay(0.5f),scaleTo(1, 1, 0.5f)));
	    	// actor.addAction(sequence(parallel(Actions.moveBy(0, -20),scaleTo(2, 2)),delay(0.5f),parallel(Actions.moveBy(0, 20, 0.5f),scaleTo(1, 1, 0.5f))));
		}
				
	}
	public void reset2(float x1, float x2 , float x3){
		
		sheepIsMoving = false;
		correctlyPassed = false;
		canDrawing = false;
		activeBodyIndicator.setPosition(-20, 0);
		int i= 0;
		for(Actor actor : getChildren()){			
			Body body = bodies.get(i);
			body.setUserData("ball");
			body.setFixedRotation(false);
			body.setLinearVelocity(0,0);
			body.setAngularVelocity(0);
			if(i == 0)	body.setTransform(x1, 20,0);
			else if(i == 1) body.setTransform(x2, 30,0);
			else body.setTransform(x3, 40,0);
			
			 i++;
			 actor.addAction(sequence(scaleTo(1.25f, 1.25f),delay(0.5f),scaleTo(1, 1, 0.5f)));
	    	 //actor.addAction(sequence(parallel(Actions.moveBy(0, -20),scaleTo(2, 2)),delay(0.5f),parallel(Actions.moveBy(0, 20, 0.5f),scaleTo(1, 1, 0.5f))));
		}
				
	}

}
