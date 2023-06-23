package com.baranagames.sheepishescape.actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;
import com.baranagames.sheepishescape.MyFirstGame.GameState;


public class Towers extends Group{

	private Array<Body> towerModel = new Array<Body>();	
	private World world;	
	private int index;
	private Vector2 towerPos;	
	private Body tempBody , twBody , groundBody;	
	private Texture background = null;		
	private Array<Sprite> bgSprites = new Array<Sprite>();
	private Sprite gateArea;//, midArea;       
	private float distance , lastTowerY,lastXVelocity=0;
	private float origXYPosition[] = {0,0,0,0,0};
	private float tileCountX = MyFirstGame.VIEWPORT_WIDTH /12.8f + 1 ; 
	private float tileCountY = MyFirstGame.VIEWPORT_HEIGHT/12.8f +1 ;	
	private Sprite extra1 , extra2 , extra3 , extra4;
     
	public Towers(World world , Body ground , String back , String borderNo , float margin)
	{
		//activeGate = 0;
		groundBody = ground;
		distance = -1;
		twBody = null;		
		//removeWall = false;
		//stateTime = 0;
		index = 0;
		//Trap_Angle_Status = 0;
		this.world = world;
		for (Body box : towerModel)
			if(box != null)  world.destroyBody(box);
		towerModel.clear();
		
		/*anim = new Animation(0.2f, new TextureRegion(new Texture(Gdx.files.internal("g1.png")))
		,new TextureRegion(new Texture(Gdx.files.internal("g2.png"))) 
		, new TextureRegion(new Texture(Gdx.files.internal("g3.png"))));*/
		if(!back.equals("none")) {
			background = new Texture(Gdx.files.internal(back+".png"));
			background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);	
		}
    	
		//*******************************  adding Borders  ********************
		
		float back_width = 20f;
		int k;
		
		Sprite s = new Sprite(Assets.textureAtlas.findRegion("border-up"));//new TextureRegion(new Texture(Gdx.files.internal("border-up.png"))));
		s.setOrigin(0, 0);	
			
		 // up border        
	     s.setSize(back_width, 5f);
	     int repeats = (int)((MyFirstGame.VIEWPORT_WIDTH-(margin*2))/back_width) + 1; 
	    // System.out.println(repeats);
	     for (k = 0; k < repeats; k++)
	     {
	         Sprite s1 = new Sprite(s);
	         s1.setX(k*back_width+margin);
	         s1.setY(55);
	       // s1.setSize(back_width,1.5f);
	         bgSprites.add(s1);
	     }
	       
	       
        // left border
	    s.setRegion(Assets.textureAtlas.findRegion("border"+borderNo));
		s.setSize(back_width, 4.2f);
        repeats = (int)(MyFirstGame.VIEWPORT_HEIGHT/back_width) + 1;
        for (k = 0; k < repeats; k++)
        {
          Sprite s1 = new Sprite(s);
          s1.setX(margin);
          s1.setY(MyFirstGame.VIEWPORT_HEIGHT - k*back_width);
          s1.setRotation(-90);
          // s1.setSize(back_width,1.5f);
          bgSprites.add(s1);
        }
        // right border        
        for (k = 0; k < repeats; k++)
        {
          Sprite s1 = new Sprite(s);
          s1.setX(MyFirstGame.VIEWPORT_WIDTH-margin);
          s1.setY(k*back_width);
          s1.setRotation(90);
          // s1.setSize(back_width,1.5f);
          bgSprites.add(s1);
        }
        
          // down border
       s.setRegion(Assets.textureAtlas.findRegion("border-dn"));
       s.setSize(back_width, 1.5f);
       repeats = (int)((MyFirstGame.VIEWPORT_WIDTH-(margin*2))/back_width) + 1; 
       for (k = 0; k < repeats; k++)
       {
         Sprite s1 = new Sprite(s);
         s1.setX(k*back_width+margin);
         s1.setY(0);
       // s1.setSize(back_width,1.5f);
         bgSprites.add(s1);
       }
       
        extra1 = new Sprite(Assets.HUDS[Integer.parseInt(borderNo)+18]);		
        extra1.setSize(10, 5);
		extra1.setPosition(MyFirstGame.VIEWPORT_WIDTH-margin, 55);
		extra2 = new Sprite(extra1);		
       	extra2.setPosition(MyFirstGame.VIEWPORT_WIDTH-margin, 0);
		extra3 = new Sprite(extra1);        
		extra3.setPosition(margin-extra3.getWidth(), 55);
		extra4 = new Sprite(extra1);        
		extra4.setPosition(margin-extra4.getWidth(), 0);
	      
       	gateArea = new Sprite(Assets.TOWERS[5]);//Assets.textureAtlas.findRegion("gateArea"));////
		gateArea.setSize(12, 25);     	
		gateArea.setOrigin(0,0);		
	}
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		if(MyFirstGame.gameState == GameState.RUNNING)	 {
		index = 0;
		super.act(delta);	
		//for(int i=0; i<towerModel.size; i++){
		int i = 0;
	
		if(distance > 0 && Math.abs(twBody.getPosition().y - lastTowerY) >= distance ){
			twBody.setLinearVelocity(lastXVelocity,0);
			//gateEntryBody.setLinearVelocity(0,0);
			distance = -1;
			twBody = null;
		//	System.out.println("null");
		}
		//System.out.println(towerModel.size+" "+getChildren().size);
		for(Actor towerImage : getChildren()){	
			tempBody = towerModel.get(i);
			towerPos = tempBody.getPosition();
			//System.out.println(tempBody.getUserData());
			if(tempBody.getUserData().equals("deleted")){
				if(twBody != null && twBody.equals(tempBody)){
					twBody = null;
					distance = -1;
				}
				tempBody.setUserData("done");
				tempBody.setActive(false);
				//Image temp = (Image)towerImage;
				towerImage.addAction(Actions.sequence(Actions.parallel(Actions.rotateBy(360, 2),Actions.moveBy(-5, -5,2),Actions.fadeOut(2)),Actions.moveTo(-50, -50)));
				//towerImage.setVisible(false);
				//towerImage.setPosition(-50,-50);
			}
			else if(towerImage.getName().equals("vkicker") && tempBody.isActive()){
				//System.out.println(tempBody.getPosition().y- origXYPosition[index]);
				//System.out.println("");
				if(tempBody.getPosition().y - origXYPosition[index] >= 4)
					tempBody.setLinearVelocity(0,-10);						
				else if(tempBody.getPosition().y - origXYPosition[index] < -4)   { 
					if(MyFirstGame.SOUNDS_ON)
						Assets.vkickerSnd.play(MyFirstGame.VOLUME);
					tempBody.setLinearVelocity(0, 35);
				}
				index++;
			} 
			else if(towerImage.getName().equals("hkicker") && tempBody.isActive()){
				//System.out.println(tempBody.getPosition().y- origXYPosition[index]);
				if(tempBody.getPosition().x - origXYPosition[index] >= 4){
					tempBody.setLinearVelocity(-35,0);		
					if(MyFirstGame.SOUNDS_ON)
						Assets.vkickerSnd.play(MyFirstGame.VOLUME);
				}
				else if(tempBody.getPosition().x - origXYPosition[index] < -4)    
					tempBody.setLinearVelocity(10, 0);
				index++;
			}
			else if(tempBody.getUserData().equals("rtkickerOn") && tempBody.getAngle() >= 3.14f) {					
				tempBody.setAngularVelocity(-5);
				tempBody.setUserData("rtkickerBack");				
			}
			else if(tempBody.getUserData().equals("rtkickerBack") && tempBody.getAngle() <= 0f){
				tempBody.setAngularVelocity(0);
				tempBody.setUserData("rtkicker");				
			}					
				
			if(!tempBody.getUserData().equals("done")) {
				towerImage.setPosition(towerPos.x - towerImage.getWidth()/2, towerPos.y - towerImage.getHeight()/2);
				towerImage.setRotation(towerModel.get(i).getAngle() * MathUtils.radiansToDegrees);				
			}
			
			i++;
		}
		//		
		//stateTime += delta;		
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub	
		//BG3.draw(batch);
		//batch.begin();
		if(background != null)			
			batch.draw(background, 0, 0 ,12.8f * tileCountX, 12.8f * tileCountY, 0, tileCountX,  tileCountY, 0);	
		//stage.getBatch().end();
		//lb.draw(batch);
		//rb.draw(batch);
		
		
				
		for(int i=0; i<bgSprites.size; i++){
			bgSprites.get(i).draw(batch);	
			//System.out.println(i+"  "+bgSprites.get(i).getX());
		}
		
		extra1.draw(batch);
		extra2.draw(batch);
		extra3.draw(batch);
		extra4.draw(batch);
		
		//midArea.setRegion(anim.getKeyFrame(stateTime,true));
		//midArea.draw(batch);
		gateArea.draw(batch);	
		//activeSide.draw(batch, parentAlpha);
		//ub.draw(batch);
		//bb.draw(batch);			
		super.draw(batch, parentAlpha);			
		
	}	
	
	public void setActiveGate(int i, float y , float margin){
		if(i == 0){   // right gate
			gateArea.setRotation(0);
			gateArea.setPosition(MyFirstGame.VIEWPORT_WIDTH - 13.5f - margin , y - 12.5f);			
		}
		else {    //left gate
			gateArea.setPosition(13.5f+margin , y + 12.5f);
			gateArea.setRotation(180);			
		}
	}
	
	public void moveGateArea(float y){
		gateArea.setY(y - 12.5f);
	}
	
	public void addTower(String type , float x, float y , float width , float height , float angle) {
		//towerType.add(type);
		//System.out.println(type+":"+x+":"+y+":"+width+":"+height);
		BodyDef ballBodyDef = new BodyDef();
		if(type.equals("normal"))
			ballBodyDef.type = BodyType.DynamicBody;
		else
			ballBodyDef.type = BodyType.KinematicBody;
		//ballBodyDef.position.set(0, 0);
		PolygonShape shape = new PolygonShape();
		//shape.setAsBox(width/2, height/2);
		shape.setAsBox(width/2, height/2);

		FixtureDef fd = new FixtureDef();
		fd.density =1f;
		//if(type.equals("normal")) fd.density = 2f;
		fd.friction = 1f;
		fd.restitution = 0.0f;
		fd.shape = shape;
		
		Body towerBody = world.createBody(ballBodyDef);		
		towerBody.createFixture(fd);		

		shape.dispose();
		towerBody.setUserData("kinematic");		
		towerBody.setTransform(x, y, angle); 
		
		if(type.equals("rotator")) {
		  //towerBody.setLinearVelocity(1, 0);
		  towerBody.setAngularVelocity(2f);
		}
		else if(type.equals("vkicker")){
			towerBody.setLinearVelocity(0, -10);
			origXYPosition[index++] = y;
		}
		else if(type.equals("hkicker")) {
			towerBody.setLinearVelocity(10, 0);
			origXYPosition[index++] = x;
		}
		else if(type.equals("rtkicker")) { 
			towerBody.setUserData("rtkicker");	
			//lastAngle = towerBody.getAngle();
			//System.err.println(lastAngle);
		}
		
		towerModel.add(towerBody);		
		
		//TextureRegion towerTexture;
		Image towerIm = null;
		//Texture towerTexture = new TextureRegion(Gdx.files.internal("data/tower.png"));
		//towerTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		if(type.equals("vkicker") || type.equals("hkicker"))
			towerIm = new Image(Assets.TOWERS[4]);
		else if(type.equals("rotator"))
			towerIm = new Image(Assets.TOWERS[0]);
		else if(type.equals("normal"))
			towerIm = new Image(Assets.TOWERS[3]);
		else if(type.equals("rtkicker"))
			towerIm = new Image(Assets.TOWERS[1]);
		else
			towerIm = new Image(Assets.TOWERS[2]);//new Texture(Gdx.files.internal("leg.png")));//Assets.textureAtlas.findRegion("leg"));//Assets.getImg("tower");
		//Image towerIm = new Image(towerTexture);
		towerIm.setSize(width, height);
		towerIm.setOrigin(width/2, height/2);
		towerIm.setName(type);
		//towerImage.add(towerIm);
		addActor(towerIm);	
		
	}
	
	public void AddChicksTower(int count,float x, float y){
		float radious = 1.5f;
		CircleShape shape = new CircleShape();
		shape.setRadius(radious);//,new Vector2(0,height/2), 0);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = 1.0f;
		fd.friction = 0.8f;
		fd.restitution = 1;
		
		RevoluteJointDef jd = new RevoluteJointDef();		
		jd.collideConnected = false;		
				
		Body prevBody = groundBody;
		Body body=null;
		int i;
		for (i = 0; i < count; i++) {
			//towerType.add("chain");
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.position.set(x + 2*i*radious , y);
			body = world.createBody(bd);
			body.createFixture(fd);
			body.setUserData("chick");		
			Vector2 anchor = new Vector2(body.getPosition());
			jd.initialize(prevBody, body, anchor);
			world.createJoint(jd);
			prevBody = body;
		
			towerModel.add(body);		
			//	Texture towerTexture = new Texture(Gdx.files.internal("data/tower.png"));
			//	towerTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			//TextureRegion towerTexture = new TextureRegion(new Texture(Gdx.files.internal("chick.png")));//Assets.getImg("tower");
			Image towerIm = new Image(Assets.GAURDS[26]);
			towerIm.setSize(radious*2, radious*2);
			towerIm.setName("chick");
			towerIm.setOrigin(radious, radious);
			//towerImage.add(towerIm);
			addActor(towerIm);
			
		}
		Vector2 anchor = new Vector2(body.getPosition());
		jd.initialize(groundBody, body, anchor);
		world.createJoint(jd);
		
		shape.dispose();
	}
	
	public void moveRandomTower(){	
		int towerCount = 0;
		for(int i=0; i<towerModel.size;i++)
			if(towerModel.get(i).getUserData().equals("kinematic") || towerModel.get(i).getUserData().equals("rtkicker"))					
				towerCount++;
		
		if(twBody == null && towerCount > 0)
		{				
			int rand= 0;
			while(true){
				rand = MathUtils.random(towerModel.size-1);
				//System.out.println(towerModel.get(rand).getUserData());
				if(towerModel.get(rand).getUserData().equals("kinematic") || towerModel.get(rand).getUserData().equals("rtkicker"))						
					break;				
			}
			twBody = towerModel.get(rand);
			lastTowerY = twBody.getPosition().y;
			lastXVelocity = twBody.getLinearVelocity().x;
			if(twBody.getPosition().y <= 28){   // twBody.y is smaller than screen height/2
				distance = MathUtils.random(5,MyFirstGame.VIEWPORT_HEIGHT - lastTowerY - 10);   //move up
				twBody.setLinearVelocity(0, 20);
			}
			else {
				distance = MathUtils.random(5,lastTowerY  - 8);  // move down
				twBody.setLinearVelocity(0, -20);	
			}
			
		}// end if
		
	}
	
	public void reset(float offset , String assetList[], int currentLevel){
		distance = -1;
		twBody = null;	
		int i= 0 , j;	
		
	    for(j=0;;j++)
	        if(assetList[j].contains("lev"+Integer.toString(currentLevel))) 
	        	break;
	    while(!assetList[j].equals("towers"))
			   j++;
		j++;
		
		for(Actor actor : getChildren()){				
			Body body = towerModel.get(i);
			actor.clearActions();
			if(!body.isActive()) {				
				body.setActive(true);
				actor.setVisible(true);
				actor.addAction(Actions.fadeIn(0.25f));
				if(actor.getName().equals("rtkicker"))
					body.setUserData("rtkicker");
				else body.setUserData("kinematic");
			}					
			// if !chick && !vkicker && is moved than transform to original place			
			if(actor.getName().equals("chick")){
			    j += 4;
			}
			/*else if(actor.getName().equals("vkicker"))
				j += 6;*/
			else {
				//System.out.println(assetList[j]+" "+assetList[j+1]+" "+assetList[j+2]);
				if(body.getPosition().y != Float.parseFloat(assetList[j+2]) || body.getPosition().x != (offset+Float.parseFloat(assetList[j+1])))				
					body.setTransform(offset+Float.parseFloat(assetList[j+1]),Float.parseFloat(assetList[j+2]), body.getAngle());
				if(actor.getName().equals("hkicker"))
					body.setLinearVelocity(10, 0);
				else if(actor.getName().equals("vkicker"))
					body.setLinearVelocity(0, -10);
				else body.setLinearVelocity(0,0);
				j += 6;	
			}
			i++;
		}
	}
	
	public void reset2(float margin , String assetList[], int currentLevel){
		distance = -1;
		twBody = null;	
		boolean otherSide = false;  // tower places in left side or right(other) side  
		int i= 0 , j;	
		
	    for(j=0;;j++)
	        if(assetList[j].contains("lev"+Integer.toString(currentLevel))) 
	        	break;
	    while(!assetList[j].equals("towers"))
			   j++;
		j++;
		
		for(Actor actor : getChildren()){			
			Body body = towerModel.get(i);			
			if(actor.getName().equals("normal")){				
				if(!otherSide){
					body.setTransform(Float.parseFloat(assetList[j+1])+margin , Float.parseFloat(assetList[j+2]),0);
					otherSide = true;
				}
				else{
					body.setTransform(MyFirstGame.VIEWPORT_WIDTH-Float.parseFloat(assetList[j+1])-margin , MyFirstGame.VIEWPORT_HEIGHT-Float.parseFloat(assetList[j+2])-2,0);
					otherSide = false;					
				}
				body.setLinearVelocity(0,0);					
			}
			if(i%2 == 1)
				j += 6;
			i++;
		}
	}
	
	public void destroy(){
		distance = -1;		
		index = 0;
		tempBody = twBody = null;
		if(background != null)
			background.dispose();
		//for (Body box : towerModel)
			//if(box != null)  world.destroyBody(box);
		towerModel.clear();
		clearActions();
		clearChildren();
		clear();
		/*for(Actor image : getChildren()){
			image.clear();
			image.remove();
		}			*/
		
		//remove();				
	}	
	
}
