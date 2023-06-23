package com.baranagames.sheepishescape.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;
import com.baranagames.sheepishescape.MyFirstGame.GameState;


public class Gaurds extends Group{

	private Array<Body> bodies = new Array<Body>();
	
	private World world;		
	private Vector2 towerPos;
	private Body goalBody , tirakBody1 , tirakBody2;
	private int yDir , halfGateHeight;
//	Animation anim;
	private float distance , lastGateY , animTime;
	private float stateTime2[] = {0,0} , returnTime ;
	private float stateTime[] = {0,0,0} , randomTime[] = {5,9,13};	
	private float changeImageTime[] = {5,10,15,5,10,15,5};
	private char imageChar[] = {'1','1','1','1','1','1','1'};
	private Body stickyBody;	
	private Animation anim;	
		
	public Gaurds(World world, String gateNo)
	{ 
		yDir = 1;
		distance = -1;		
		animTime = 0;
		
		this.world = world;
		for (Body box : bodies)
			if(box != null)  world.destroyBody(box);
		bodies.clear();
		
		anim = new Animation(0.5f,Assets.textureAtlas.findRegion("gate"+gateNo),Assets.textureAtlas.findRegion("gate"+gateNo+"b"));	
	}
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		if(MyFirstGame.gameState == GameState.RUNNING )	 {
		super.act(delta);	
		int i = 0 , j =0 , k = 0 , m = 0;
		
		if(distance > 0 && Math.abs(goalBody.getPosition().y - lastGateY) >= distance ){
			goalBody.setLinearVelocity(0,0);
			tirakBody1.setLinearVelocity(0,0);
			tirakBody2.setLinearVelocity(0,0);
			distance = -1;
		}		
				
		for(Actor towerImage : getChildren()){
			/*temp = (Image)towerImage;
			if(towerImage.getName().equals("gaurd"))
				temp.setDrawable(new SpriteDrawable(new Sprite(anim.getKeyFrame(stateTime[i],true))));*/
			Body tempBody = bodies.get(i);			
			towerPos = tempBody.getPosition();
			if(!tempBody.getUserData().equals("done")){
				towerImage.setPosition(towerPos.x - towerImage.getWidth()/2, towerPos.y- towerImage.getHeight()/2);
				towerImage.setRotation(bodies.get(i).getAngle() * MathUtils.radiansToDegrees);	
			}
			
			if(!tempBody.getUserData().equals("goal") && !tempBody.getUserData().equals("tirak")){
				changeImageTime[m] += delta;				
				if(changeImageTime[m] >= 20){
					int index = 0;
					changeImageTime[m] = MathUtils.random(15);
					//if(towerImage.getName().equals("cow")) index = 0;
					if(towerImage.getName().equals("donkey")) index = 2;
					else if(towerImage.getName().equals("horse")) index = 4;
					else if(towerImage.getName().equals("goat")) index = 6;
					else if(towerImage.getName().equals("dog")) index = 8;
					else if(towerImage.getName().equals("agent")) index = 10;
					else if(towerImage.getName().equals("frog")) index = 12;
					else if(towerImage.getName().equals("grass")) index = 14;
					else if(towerImage.getName().equals("sticky")) index = 16;
					else if(towerImage.getName().equals("wolf")) index = 18;
					else if(towerImage.getName().equals("man")) index = 20;
					else if(towerImage.getName().equals("woman")) index = 22;
					else if(towerImage.getName().equals("snake")) index = 24;
					
					
					Image temp = (Image)towerImage;
					if(imageChar[m] == '1'){
						temp.setDrawable(new SpriteDrawable(new Sprite(Assets.GAURDS[index+1])));// findRegion(towerImage.getName()+"1"))));  //new Texture(Gdx.files.internal(towerImage.getName()+"1.png")))));
						imageChar[m] = '0';
					}
					else{
						temp.setDrawable(new SpriteDrawable(new Sprite(Assets.GAURDS[index])));
						imageChar[m] = '1';
					}
					
				}					
				m++;
			}
		
			if(tempBody.getUserData().equals("deleted")){
				tempBody.setUserData("done");
				tempBody.setActive(false);
				//towerImage.setVisible(false);				
				//towerImage.setPosition(-50,-50);
				towerImage.addAction(sequence(parallel(Actions.rotateBy(360, 2),Actions.moveBy(-5, -5,2),Actions.fadeOut(2)),Actions.moveTo(-50, -50)));
			}
			else if(tempBody.getUserData().equals("goal")){
				Image temp = (Image)towerImage;
				temp.setDrawable(new SpriteDrawable(new Sprite(anim.getKeyFrame(animTime,true))));
			}
			else if(tempBody.getUserData().equals("donkey")){
				stateTime[j] += delta;
				//System.out.println(stateTime+"  "+randomTime);
				if(stateTime[j] >= randomTime[j]) {
					Assets.donkeySnd.play(MyFirstGame.VOLUME);
					tempBody.setLinearVelocity(MathUtils.random(-250,250), MathUtils.random(-250,250));
					//tempBody.applyLinearImpulse(MathUtils.random(150), MathUtils.random(150), tempBody.getPosition().x, tempBody.getPosition().y, true);
					//System.out.println(tempBody.getLinearVelocity());
					stateTime[j] = 0;
					randomTime[j] = MathUtils.random(3, 8);						
				}
				j++;
			}
			else if(tempBody.getUserData().equals("catched")){	
				if(stickyBody != null) {
					tempBody.setUserData("sticked");
					RevoluteJointDef jd = new RevoluteJointDef();		
					jd.collideConnected = false;
					Vector2 anchor =new Vector2(towerPos.x,towerPos.y);
					jd.initialize(stickyBody, tempBody, anchor);
					world.createJoint(jd);
					
					RevoluteJointDef jd1 = new RevoluteJointDef();		
					jd1.collideConnected = false;
					anchor =new Vector2(towerPos.x-0.5f,towerPos.y-0.5f);
					jd1.initialize(stickyBody, tempBody, anchor);
					world.createJoint(jd1);
					
					stickyBody = null;					
				}
			}
			else if(tempBody.getUserData().equals("rstSticky")){	
				 Array<JointEdge> list = tempBody.getJointList();				 
		    	 while (list.size > 0) {
		    		 world.destroyJoint(list.get(0).joint);
		    	 }
		    	// System.out.println(tempBody.getJointList().size);
		    	 tempBody.setUserData("sticky");
			}
			else if(tempBody.getUserData().equals("dog")){			
				stateTime2[k] += delta;
				if(stateTime2[k] >= returnTime){
					stateTime2[k] = 0;				
					tempBody.setLinearVelocity(tempBody.getLinearVelocity().rotate(180));
				}			
				k++;
			}
			/*else if(towerImage.getName().equals("goal")){
				Image temp = (Image)towerImage;
				temp.setDrawable(new SpriteDrawable(new Sprite(anim.getKeyFrame(stateTime,true))));
			}*/
			/*else if(tempBody.getUserData().equals("agentOff")){
				if(agentOffTime == -1)
					agentOffTime = 0;
				agentOffTime += Gdx.graphics.getDeltaTime();
				//System.out.println(sleepTime[i]);
				if(agentOffTime >= 2){
					tempBody.setUserData("agent");
					agentOffTime = -1;
				}
				
			}*/
			i++;
			
		}
		
		//		
	    animTime += delta;	
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub			
		super.draw(batch, parentAlpha);		
	}
	
	public void addObjects(String name , float x, float y , float radious)   //woman foal khar wolf
	{
		 BodyDef bodyDef = new BodyDef();  
	     bodyDef.type = BodyType.DynamicBody;  
	     bodyDef.position.set(x,y);  
	     Body wolfModel = world.createBody(bodyDef);  
	     CircleShape dynamicCircle = new CircleShape();  
	     dynamicCircle.setRadius(radious);  
	     FixtureDef fixtureDef = new FixtureDef();  
	     fixtureDef.shape = dynamicCircle;  
	     fixtureDef.density = 1f;  
	     fixtureDef.friction = 0.8f;  
	     fixtureDef.restitution = 0.2f;  
	     wolfModel.createFixture(fixtureDef);
	     wolfModel.setUserData(name);
	     dynamicCircle.dispose();
	     //System.out.println(sheepModel.getMass());
	     wolfModel.setLinearVelocity(0, 0);
	     wolfModel.setAngularVelocity(0);
	     //wolfModel.setFixedRotation(true);
	     bodies.add(wolfModel);
		//sheepTexture = new Texture(Gdx.files.internal("data/gfx/ball.png"));
		//TextureRegion  wolfTexture = new TextureRegion(new Texture(Gdx.files.internal(name+".png")));//Assets.getImg("badlogic");
		//sheepTexture.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	     int index = 0;
		//if(towerImage.getName().equals("cow")) index = 0;
		if(name.equals("donkey")) index = 2;
		else if(name.equals("horse")) index = 4;
		else if(name.equals("goat")) index = 6;
		else if(name.equals("dog")) index = 8;
		else if(name.equals("agent")) index = 10;
		else if(name.equals("frog")) index = 12;
		else if(name.equals("grass")) index = 14;
		else if(name.equals("sticky")) index = 16;
		else if(name.equals("wolf")) index = 18;
		else if(name.equals("man")) index = 20;
		else if(name.equals("woman")) index = 22;
		else if(name.equals("snake")) index = 24;
		Image sheepImage = new Image(Assets.GAURDS[index]);
		sheepImage.setSize(radious*2,radious*2);
		sheepImage.setOrigin(radious, radious);
		sheepImage.setName(name);
		addActor(sheepImage);		
	}
	
	public void setStickyBody(Body body){
		stickyBody = body;
	}
	
	public void addGaurd(int type , float x, float y , float time , float velocity,float radious) {
		
		returnTime = time;
		BodyDef ballBodyDef = new BodyDef();		
		ballBodyDef.type = BodyType.KinematicBody;
		//ballBodyDef.position.set(0, 0);
		//PolygonShape shape1 = new PolygonShape();
		//shape.setAsBox(width/2, height/2);
		//shape1.setAsBox(0.75f, 0.75f);
		CircleShape shape = new CircleShape();
		shape.setRadius(radious);

		FixtureDef fd = new FixtureDef();
		fd.density =1f;
		fd.friction = 0.8f;
		fd.restitution = 0.7f;
		fd.shape = shape;
		
		Body towerBody = world.createBody(ballBodyDef);		
		towerBody.createFixture(fd);
		
		shape.dispose();
		
		towerBody.setTransform(x, y, 0); 
		if(type == 0) //horizontal		
		  towerBody.setLinearVelocity(-velocity, 0); // assume that body move left at the begining 		
		else if(type == 1) //vertical
			towerBody.setLinearVelocity(0, -velocity);
		else if(type == 2) //both
				towerBody.setLinearVelocity(-velocity, -velocity);	
			  //towerBody.setAngularVelocity(1);
		
		towerBody.setUserData("dog");
		bodies.add(towerBody);
		
		//Texture towerTexture = new TextureRegion(Gdx.files.internal("data/tower.png"));
		//towerTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	//	TextureRegion towerTexture = new TextureRegion(new Texture(Gdx.files.internal("dog.png")));	//Assets.getImg("tower");
		Image towerIm = new Image(Assets.GAURDS[8]);
		towerIm.setSize(radious*2, radious*2);
		towerIm.setOrigin(radious,radious);
		towerIm.setName("dog");
		//towerImage.add(towerIm);
		addActor(towerIm);	
		
	}
	
	public void addGate(float x, float y, float height, float angle){
		
		 halfGateHeight = (int)height/2;		
		//************** add gate *************** 
		 float width = 3.5f;
		 BodyDef bodyDef = new BodyDef();  
	     bodyDef.type = BodyType.KinematicBody;  
	     //bodyDef.position.set(MyFirstGame.VIEWPORT_WIDTH,y);  
	     goalBody = world.createBody(bodyDef);  
	     
	     PolygonShape dynamicCircle = new PolygonShape();  
	     dynamicCircle.setAsBox(width/2, height/2);
	     FixtureDef fixtureDef = new FixtureDef();  
	     fixtureDef.shape = dynamicCircle;  
	     fixtureDef.density = 1f;  
	     fixtureDef.friction = 0.8f;  
	     fixtureDef.restitution = 0.2f;  
	     fixtureDef.isSensor = true;
	     goalBody.createFixture(fixtureDef);
	     goalBody.setUserData("goal");
	     dynamicCircle.dispose();
	     //System.out.println(sheepModel.getMass());
	    
	   	 goalBody.setTransform(x , y , angle);
	     
	     goalBody.setLinearVelocity(0, 0);
	     goalBody.setAngularVelocity(0);
	     //wolfModel.setFixedRotation(true);
	     bodies.add(goalBody);		
		Image sheepImage = new Image();
		sheepImage.setSize(width,height);
		sheepImage.setOrigin(width/2, height/2);
		sheepImage.setName("goal");
		addActor(sheepImage);	
		
		//******************** add tirak ha  *****************
	    addTirak(x , y, 1);
		addTirak(x , y, -1);			
		
	}
	
	private void addTirak(float x, float goalY , int dir){
		float width = 3.15f;
		BodyDef bodyDef = new BodyDef();  
	     bodyDef.type = BodyType.KinematicBody;  
	     //bodyDef.position.set(MyFirstGame.VIEWPORT_WIDTH,y);  
	     Body tirakBody = world.createBody(bodyDef);  
	     
	     PolygonShape dynamicCircle = new PolygonShape();  
	     dynamicCircle.setAsBox(width/2, 0.12f);
	     FixtureDef fixtureDef = new FixtureDef();  
	     fixtureDef.shape = dynamicCircle;  
	     fixtureDef.density = 1f;  
	     fixtureDef.friction = 0.8f;  
	     fixtureDef.restitution = 0.5f;  
	    // fixtureDef.isSensor = true;
	     tirakBody.createFixture(fixtureDef);
	     tirakBody.setUserData("tirak");
	     dynamicCircle.dispose();
	     //System.out.println(sheepModel.getMass());
	    
	     if(x > 30)  // gate is in right side of the filed (in two player)
	    	 x += 0.2f;
	     else x -= 0.2f;
	     tirakBody.setTransform(x , goalY + ((halfGateHeight+0.15f)*dir)  , 0);
	     
	     tirakBody.setLinearVelocity(0, 0);
	     tirakBody.setAngularVelocity(0);
	     //wolfModel.setFixedRotation(true);
	     bodies.add(tirakBody);		
	     if(dir == 1) tirakBody1 = tirakBody;
	     else  tirakBody2 = tirakBody;
		Image sheepImage = new Image(Assets.HUDS[18]);
		sheepImage.setSize(width,1);
		sheepImage.setOrigin(width/2, 0.5f);
		sheepImage.setName("tirak");
		sheepImage.setVisible(false);
		addActor(sheepImage);	
	}
		
	public Vector2 getGatePos(){
		return goalBody.getPosition();
	}
	
	public void moveGate(){
		lastGateY = goalBody.getPosition().y;
		if(yDir == 1)
		    distance = MathUtils.random(MyFirstGame.VIEWPORT_HEIGHT - lastGateY  - halfGateHeight - 5);   //move up
		else distance = MathUtils.random(lastGateY  - halfGateHeight - 3);  // move down
		goalBody.setLinearVelocity(0, yDir * 20);
		tirakBody1.setLinearVelocity(0, yDir * 20);
		tirakBody2.setLinearVelocity(0, yDir * 20);
		yDir *= -1;
	}
	
	public float getGateY(){
		if(distance == -1)
			return goalBody.getPosition().y;
		return -1;
	}
	
	public void reset(float offset , String assetList[], int currentLevel){
		distance = -1;
		yDir = 1;
		stateTime[0] = stateTime[1] =stateTime[2] = 0 ;// =stateTime2 = 0;
		stickyBody = null;
		
		int i= 0 , j;	
		
	    for(j=0;;j++)
	        if(assetList[j].contains("lev"+Integer.toString(currentLevel))) 
	        	break;
	    while(!assetList[j].equals("guards"))
		   j++;
		j++;
		
		for(Actor actor : getChildren()){	
			if(assetList[j].equals("end"))
				break;
		    Body body = bodies.get(i);
		    actor.clearActions();
		    //if(body.getUserData().equals("goal"))
		    //	break;
			if(!body.isActive()) {				
				body.setActive(true);
				actor.setVisible(true);		
				actor.addAction(Actions.fadeIn(0.25f));
				body.setUserData(actor.getName());
			}					
			if(body.getUserData().equals("sticked") || body.getUserData().equals("rstSticky")){
				body.setUserData("sticky");
				 Array<JointEdge> list = body.getJointList();
				// System.out.println(list.size);
		    	 while (list.size > 0) {
		 	        world.destroyJoint(list.get(0).joint);		 	       
		 	     }
			}
			
			if(body.getUserData().equals("dog")){				
			    j += 6;
			}
			else if(body.getUserData().equals("goal")){
				body.setLinearVelocity(0,0);
				if(body.getPosition().y != Float.parseFloat(assetList[j+1]))	{
					body.setTransform(body.getPosition().x,Float.parseFloat(assetList[j+1]), body.getAngle());
					tirakBody1.setTransform(tirakBody1.getPosition().x, Float.parseFloat(assetList[j+1]) + halfGateHeight+0.15f,  0);
					tirakBody2.setTransform(tirakBody2.getPosition().x, Float.parseFloat(assetList[j+1]) - halfGateHeight-0.15f,  0);			
					}				
				j +=4; 				
			}	
			else if(!body.getUserData().equals("tirak"))			
			{
				body.setTransform(offset+Float.parseFloat(assetList[j+1]),Float.parseFloat(assetList[j+2]), 0);
				body.setLinearVelocity(0,0);
				body.setAngularVelocity(0);
				j += 3;	
			}
			i++;
		}
	}
	
	public void destroy(){
		distance = -1;
		yDir = 1;
		stateTime[0] = stateTime[1] =stateTime[2] = stateTime2[0] = stateTime2[1] = 0;		
	
		bodies.clear();
		goalBody = tirakBody1 = tirakBody2 = stickyBody = null;
		clearActions();
		clearChildren();
		clear();
					
	}
	

	
	
}
