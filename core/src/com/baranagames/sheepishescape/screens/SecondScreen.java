package com.baranagames.sheepishescape.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;
import com.baranagames.sheepishescape.MyFirstGame.GameState;
import com.baranagames.sheepishescape.actors.Gaurds;
import com.baranagames.sheepishescape.actors.HUDs2;
import com.baranagames.sheepishescape.actors.PauseMenu2;
import com.baranagames.sheepishescape.actors.Sheeps;
import com.baranagames.sheepishescape.actors.Towers;

public class SecondScreen implements Screen , InputProcessor{
	

	public static boolean Is_Goal = false;
	static final float BOX_STEP=1/60f;  
    static final int BOX_VELOCITY_ITERATIONS=6;  
    static final int BOX_POSITION_ITERATIONS=2;
    public static final float MAX_TOUCH_DISTANCE = 2.5f;   
    private Sheeps mySheep; 
    private Towers tower;
    private MyFirstGame game;
    private Stage stage;
    private World world;
    private HUDs2 huds;
    private Gaurds gaurds;  
    private  PauseMenu2 menu;
    //private Box2DDebugRenderer debugRenderer;  
	private Vector3 testPoint = new Vector3(10 ,  50 , 0);
	private Body hitBody = null;
	private Body groundBody;// , ceilBody;
	private final Image pauseButton = new Image(Assets.BUTTONS[4]);	
	private InputMultiplexer inputMultiplexer = new InputMultiplexer();
	private Camera camera;
    private Image line;   
    private int allowedMoves, allowedFoals;    
    private int leftGoals , rightGoals , foalCounter , moveCounter;   
    private int currentLevel , currentPlayer;
    private MouseJoint mouseJoint;
    private Vector3 mousePoint = new Vector3();
    private float dragTime , allowedDragTime;
    private float red , green , blue , MARGIN;   
    private Texture hintTexture;
    private  Image hintImg;
    private  boolean normalExisted , lastMove;
    
	public SecondScreen(MyFirstGame game, int level)	{
		
		dragTime = 0;
		leftGoals = rightGoals = moveCounter = 0;
		currentLevel = level;		
		lastMove = normalExisted = false;
		//dragIsActive = true;
	/*	p = new ParticleEffect();
		p.load(Gdx.files.internal("data/test.p"), Gdx.files.internal("data")); 
		p.setPosition(400, 200);*/
		
	//	savedSheepsCnt = deadSheepsCnt = givenSheepsCnt = 0;
		
		this.game = game;
		
		MyFirstGame.gameState = GameState.RUNNING;
	
		//debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0f, 0f), true); 
		createCollisionListener();
		
		//System.out.println(stage.getCamera().viewportWidth);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		MyFirstGame.VIEWPORT_WIDTH = MyFirstGame.VIEWPORT_HEIGHT * w / h;	
		
		MARGIN = 0;
		if(MyFirstGame.VIEWPORT_WIDTH - 80 > 16)   // aspect ratio > 8:5
			MARGIN = (MyFirstGame.VIEWPORT_WIDTH-80 - 16) / 2;
		//MyFirstGame.VIEWPORT_WIDTH = MyFirstGame.VIEWPORT_WIDTH;
		//OFFSET = (MyFirstGame.VIEWPORT_WIDTH - 80);
		
		
		stage = new Stage(new StretchViewport(MyFirstGame.VIEWPORT_WIDTH, MyFirstGame.VIEWPORT_HEIGHT));
	
		camera = stage.getCamera();
        camera.position.set(MyFirstGame.VIEWPORT_WIDTH-MyFirstGame.VIEWPORT_WIDTH/2,MyFirstGame.VIEWPORT_HEIGHT/2, 0);
        //System.out.println(camera.position);
        camera.update();
     
        hintTexture = new Texture(Gdx.files.internal("intro2p.png"),true);
        hintTexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		hintImg = new Image(hintTexture);
		float ratio = h / MyFirstGame.VIEWPORT_HEIGHT;
		hintImg.setSize(hintTexture.getWidth()/ratio, hintTexture.getHeight()/ratio);
		if(hintImg.getWidth() > MyFirstGame.VIEWPORT_WIDTH)
			hintImg.setSize(MyFirstGame.VIEWPORT_WIDTH, MyFirstGame.VIEWPORT_WIDTH/2);
		hintImg.setPosition(MyFirstGame.VIEWPORT_WIDTH/2 - hintImg.getWidth()/2, MyFirstGame.VIEWPORT_HEIGHT/2 - hintImg.getHeight()/2);
		hintImg.setOrigin(hintImg.getWidth()/2, hintImg.getHeight()/2);	
		hintImg.addAction(Actions.sequence(Actions.delay(3.5f),Actions.fadeOut(2)));
      
        currentPlayer = MathUtils.random(1);  // 0 = left  , 1 = right        
	}
	
		
	@Override
	public void show() {
		
		// TODO Auto-generated method stub	
		Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
		Cursor cursor = Gdx.graphics.newCursor(pm, 0, 0);
		if (cursor != null)
		    cursor.setSystemCursor();
		//Gdx.input.setCursorImage(pm, 0, 0);
		pm.dispose();
		//***************************  Read Level Data From File  **************************
		//***************************  Apply Level Data     **************************
		
		
				
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
		//inputMultiplexer.addProcessor(stage2);
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(true);
			
		createBorders("l_vertical_border");
		createBorders("r_vertical_border");		
		createBorders("ceil");
		createBorders("ground");		
		
		FileHandle handle = Gdx.files.internal("data/Levels2.txt");	
        String assetList[] = handle.readString().split(";");
        int i;
        for(i=0;;i++)
        	if(assetList[i].contains("lev"+Integer.toString(currentLevel))) 
        		break;
        i++;  // pass "level" word
        
        tower = new Towers(world,groundBody,assetList[i++],assetList[i++],MARGIN);
        String gateNo = assetList[i-1];  // used for specify color of gate in gaurd constructor
    	
        red = Float.parseFloat(assetList[i++]);
        green = Float.parseFloat(assetList[i++]);
        blue = Float.parseFloat(assetList[i++]);
        
        allowedMoves = Integer.parseInt(assetList[i]);
    	allowedFoals = Integer.parseInt(assetList[i+1]);
    	i+=2;
    	  	
    	float sheepRadious=2.5f;
    	allowedDragTime = 0.35f;
    	if(Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS) {
    		sheepRadious = 3;// set radious  = 3
    		//allowedDragTime = 0.6f;
	    }

		mySheep = new Sheeps(world , groundBody);
		//int j = i+1;  // j is offset of balls locations . they located after ball's properties.
		float xPos , xPos2;
		if(currentPlayer == 0) 		{
			xPos = MARGIN+15;
			xPos2 = MARGIN+20 ;			
		}
		else 	{
			xPos = MyFirstGame.VIEWPORT_WIDTH - MARGIN - 15;
			xPos2 = MyFirstGame.VIEWPORT_WIDTH - MARGIN - 20;
		}
				
		mySheep.addSheepBody("ball", sheepRadious,xPos ,20);	
		mySheep.addSheepBody("ball", sheepRadious,xPos2 ,30);
		mySheep.addSheepBody("ball", sheepRadious,xPos ,40);
		
		//i = j;		

		//***************       Towers        *****************
		i++; // pass "tower" word
		
		//Vector2 gatePos = null;
		while(!assetList[i].equals("guards")){
			if(assetList[i].equals("normal") && !normalExisted)
				normalExisted = true;
			if(!assetList[i+1].equals("40")){
				tower.addTower(assetList[i], Float.parseFloat(assetList[i+1])+MARGIN , Float.parseFloat(assetList[i+2])
						, Float.parseFloat(assetList[i+3]) , Float.parseFloat(assetList[i+4]) , Float.parseFloat(assetList[i+5]));
				tower.addTower(assetList[i], MyFirstGame.VIEWPORT_WIDTH-Float.parseFloat(assetList[i+1])-MARGIN , MyFirstGame.VIEWPORT_HEIGHT-Float.parseFloat(assetList[i+2])-2
						, Float.parseFloat(assetList[i+3]) , Float.parseFloat(assetList[i+4]) , Float.parseFloat(assetList[i+5]));		
			}	
			else 
				tower.addTower(assetList[i],MyFirstGame.VIEWPORT_WIDTH/2 , MyFirstGame.VIEWPORT_HEIGHT/2 - 2
						, Float.parseFloat(assetList[i+3]) , Float.parseFloat(assetList[i+4]) , Float.parseFloat(assetList[i+5]));
							
			i += 6;					
		}
		
		//int gateNo = 0;
		//***************       guards objects        *****************
		i++; // pass "guards" word
		// 
		gaurds = new Gaurds(world,gateNo);
		while(!assetList[i].equals("end")){
			if(assetList[i].equals("dog")) {
				gaurds.addGaurd(Integer.parseInt(assetList[i+1]),Float.parseFloat(assetList[i+2])+MARGIN,Float.parseFloat(assetList[i+3])
						, Float.parseFloat(assetList[i+4]),Float.parseFloat(assetList[i+5]),sheepRadious);	
				gaurds.addGaurd(Integer.parseInt(assetList[i+1]),MyFirstGame.VIEWPORT_WIDTH - Float.parseFloat(assetList[i+2]) - MARGIN,
						MyFirstGame.VIEWPORT_HEIGHT-Float.parseFloat(assetList[i+3])-2, Float.parseFloat(assetList[i+4]),-Float.parseFloat(assetList[i+5]),sheepRadious);				

				i += 6;
			}
			else if(assetList[i].equals("gate")){
				//float x;
				//gatePos = new Vector2(Float.parseFloat(assetList[i+1]) , Float.parseFloat(assetList[i+2]));
				//if(gateNo == 1)  x  = MyFirstGame.VIEWPORT_WIDTH-Float.parseFloat(assetList[i+2])/2;
				//else x = Float.parseFloat(assetList[i+2])/2;
				gaurds.addGate(MyFirstGame.VIEWPORT_WIDTH-1.75f-MARGIN,Float.parseFloat(assetList[i+1])
						, Float.parseFloat(assetList[i+2])	, 0);
				//tower.addGateArea(0,Float.parseFloat(assetList[i+1]));
				gaurds.addGate(MARGIN+1.75f,Float.parseFloat(assetList[i+1]) 
						, Float.parseFloat(assetList[i+2]) , 3.14f);
				//tower.addGateArea(1,Float.parseFloat(assetList[i+1]));
				//gateNo++;
				
				i += 3;
			}			
		}

		line = new Image(Assets.SHEEPS[7]);
        line.setOrigin(0, 0);
        line.setVisible(false);
		
		huds = new HUDs2(allowedMoves,allowedFoals,currentPlayer);
		menu = new PauseMenu2(game,this,currentLevel);	
		
		//stage.addActor(goalLineImg);
		stage.addActor(tower);	
		stage.addActor(gaurds);
		stage.addActor(mySheep);
		stage.addActor(huds);
		stage.addActor(line);
		stage.addActor(hintImg);
		stage.addActor(menu);
		createButtons(); 
		
		if(MyFirstGame.SOUNDS_ON) {
			Assets.ingameMusic.setVolume(MyFirstGame.VOLUME+0.3f);
			Assets.ingameMusic.setLooping(true);
			Assets.ingameMusic.play();  
		}  
		
        MyFirstGame.PLAYER_READY = false;
        huds.showPlayerSide(currentPlayer);
        mySheep.changeColor(currentPlayer);
        tower.setActiveGate(currentPlayer, gaurds.getGateY(),MARGIN);
       
	}
	
	public void reset(){		
		leftGoals = rightGoals = moveCounter = 0;
		lastMove = false;
		line.setVisible(false);		
		dragTime = 0;
		hitBody = currentBody = null;
		foalCounter = moveCounter = 0;
		sheepMoves = false;		
		currentPlayer = MathUtils.random(1);
		
		//FileHandle handle = Gdx.files.internal("data/Levels.txt");
		//String assetList[] = handle.readString().split(";");		
		//gaurds.reset(OFFSET, assetList, currentLevel);
		float xPos , xPos2;
		//System.out.println(currentPlayer);
		if(currentPlayer == 0) 		{
			xPos = 15+MARGIN;
			xPos2 = 20+MARGIN ;			
		}
		else 	{
			xPos = MyFirstGame.VIEWPORT_WIDTH - MARGIN-15;
			xPos2 = MyFirstGame.VIEWPORT_WIDTH - MARGIN-20;
		}
		mySheep.reset2(xPos,xPos2,xPos);			
		huds.reset(allowedMoves, allowedFoals);
		MyFirstGame.PLAYER_READY = false;
		huds.showPlayerSide(currentPlayer);
		mySheep.changeColor(currentPlayer);
					
		huds.setScoreboardPosition(currentPlayer);
		tower.setActiveGate(currentPlayer,gaurds.getGateY(),MARGIN);
		if(normalExisted){
			FileHandle handle = Gdx.files.internal("data/Levels2.txt");	
	        String assetList[] = handle.readString().split(";");
			tower.reset2(MARGIN, assetList , currentLevel);		
		}
		
		MyFirstGame.gameState = GameState.RUNNING;
		menu.setShowButtons(false);
	}
	
	//float last = 0;
	//FPSLogger logger = new FPSLogger();
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub		
			
		Gdx.gl.glClearColor(red, green, blue, 1f );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		
		stage.act(delta);
		
		stage.draw();		
		
		if(MyFirstGame.gameState == GameState.RUNNING) 
		{							
		   //debugRenderer.render(world, stage.getCamera().combined); 
			world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS); 	
			if(mouseJoint != null && sheepMoves)   {			
				dragTime += Gdx.graphics.getDeltaTime();
				if(dragTime > allowedDragTime) {
					world.destroyJoint(mouseJoint);
					mouseJoint = null;	
					line.setVisible(false);
				}
				if(currentBody != null){
					mousePoint.set(Gdx.input.getX(), Gdx.input.getY(),0);
					camera.unproject(mousePoint);
					float distance = currentBody.getPosition().dst(mousePoint.x, mousePoint.y);
					   // float angle =  pos[0].angleRad(pos[1]);		 
					float angle = ((float)Math.atan2(currentBody.getPosition().y-mousePoint.y, mousePoint.x-currentBody.getPosition().x)) * MathUtils.radiansToDegrees;		  
					line.setPosition(currentBody.getPosition().x,currentBody.getPosition().y);	   
					line.setSize(distance, 0.4f);
					line.setRotation(-angle);		
				}
            }	
			//if(hitBody != null)System.out.println(hitBody.getLinearVelocity());
			if(lastMove && currentBody != null && currentBody.getLinearVelocity().isZero(1f)){
				if(currentPlayer == 0){    					
					huds.setScoreboardPosition(1);					
					currentPlayer = 1;				
				}
				else{    					
					huds.setScoreboardPosition(0);					
					currentPlayer = 0;					
				}     
				tower.setActiveGate(currentPlayer,gaurds.getGateY(),MARGIN);
				mySheep.resetIndicator();
				MyFirstGame.PLAYER_READY = false;
				huds.showPlayerSide(currentPlayer);
				mySheep.changeColor(currentPlayer);
				lastMove = false;
				moveCounter = 0;
				foalCounter = 0;
				mySheep.drawLine(false);
				mySheep.setSheepMoving(false);
				currentBody.setUserData("ball");
				currentBody = null;				
			}
			if(currentBody != null && currentBody.getLinearVelocity().isZero(1f)){
				mySheep.setSelected(1);
				if(mySheep.isFoal()) {
					foalCounter++;
					
					if(foalCounter > allowedFoals) {
						if(currentPlayer == 0){    					
	    					huds.setScoreboardPosition(1);	    					
	    					currentPlayer = 1;
	    				}
	    				else{    					
	    					huds.setScoreboardPosition(0);	    					
	    					currentPlayer = 0;
	    				}   		
						tower.setActiveGate(currentPlayer,gaurds.getGateY(),MARGIN);
						mySheep.resetIndicator();
						MyFirstGame.PLAYER_READY = false;
						lastMove = false;
	    				moveCounter = 0;
	    				foalCounter = 0;
	    				huds.showPlayerSide(currentPlayer);
	    				mySheep.changeColor(currentPlayer);
	    				mySheep.drawLine(false);
	    				mySheep.setSheepMoving(false);
	    				currentBody.setUserData("ball");
	    				currentBody = null;    				
						/*MyFirstGame.gameState = GameState.GAMEOVER;
						menu.hideExtraButtons();*/
					}
					else
						huds.addFoal(foalCounter);
				}
			}
		} // end if running
		
	}
	
	private void createBorders(String type) {
		BodyDef bd = new BodyDef();
		/*if(type.equals("ground"))
			bd.position.set(VIEWPORT_WIDTH/2, 0.5f); //ground
		else if(type.equals("ceil"))
			bd.position.set(VIEWPORT_WIDTH/2, VIEWPORT_HEIGHT-0.5f); // ceil*/
		if(type.equals("l_vertical_border"))
			bd.position.set(MARGIN+0.75f, MyFirstGame.VIEWPORT_HEIGHT/2); // vertical_border
		else if(type.equals("r_vertical_border"))
			bd.position.set(MyFirstGame.VIEWPORT_WIDTH-MARGIN-0.75f,MyFirstGame.VIEWPORT_HEIGHT/2);
		
		bd.type = BodyType.StaticBody;
		
		Body body = world.createBody(bd);
		body.setUserData("world_borders");
		PolygonShape shape = new PolygonShape();
		if(type.equals("l_vertical_border") || type.equals("r_vertical_border"))
			shape.setAsBox(1f, MyFirstGame.VIEWPORT_HEIGHT/2);
		else if(type.equals("ground"))	shape.setAsBox(MyFirstGame.VIEWPORT_WIDTH/2, 0.5f);
		else  shape.setAsBox(MyFirstGame.VIEWPORT_WIDTH/2, 0.5f);// ceil
		
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.5f;
		fd.shape = shape;

		body.createFixture(fd);
		
		if(type.equals("ground")) {
			body.setTransform(MyFirstGame.VIEWPORT_WIDTH/2, 1f, 0f);			
		}		
		else if(type.equals("ceil")) {
			body.setTransform(MyFirstGame.VIEWPORT_WIDTH/2, MyFirstGame.VIEWPORT_HEIGHT-4f, 0);			
			groundBody = body;
		}
		shape.dispose();
	}		
	
	private void createCollisionListener() {
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
            	if(MyFirstGame.gameState == GameState.RUNNING) {
            		Body bodyA = contact.getFixtureA().getBody();
            		Body bodyB = contact.getFixtureB().getBody();
               
            		if(bodyA.getUserData().equals("world_borders") || bodyB.getUserData().equals("world_borders")
            		   || bodyA.getUserData().equals("kinematic") || bodyB.getUserData().equals("kinematic"))
            	      //  || bodyA.getUserData().equals("ball") || bodyB.getUserData().equals("ball"))
            			return;               
               //System.out.println(bodyA.getUserData()+"  "+bodyB.getUserData());
            		if(bodyB.getUserData().equals("balljoon")) {
            			bodyA = contact.getFixtureB().getBody();
            			bodyB = contact.getFixtureA().getBody();            		  
            		}
               
            		if(bodyA.getUserData().equals("balljoon")) 
            		{            			
            			if(bodyB.getUserData().equals("goal") && mySheep.isCorrectlyPassed()){
            				mySheep.resetIndicator();
            				if(bodyB.getPosition().x > 25){
            					leftGoals++;
            					huds.addGoal(leftGoals, 0 , currentPlayer);            					
            					bodyA.setUserData("ball");
            					MyFirstGame.PLAYER_READY = false;
            					if(leftGoals == 10){
            						if(MyFirstGame.SOUNDS_ON)
            							Assets.winSnd.play(MyFirstGame.VOLUME+0.5f);
            						MyFirstGame.gameState = GameState.GAMEWIN;
            						menu.hideExtraButtons();
            						huds.showFinalResult();
            					}
            					else {  
            						if(currentPlayer == 0)
            							currentPlayer = 1;
            						else 
            							currentPlayer = 0;
            						huds.setScoreboardPosition(currentPlayer);
            						tower.setActiveGate(currentPlayer,gaurds.getGateY(),MARGIN);         					 						            					
            						moveCounter = 0;
            						foalCounter = 0;
            						lastMove = false;
            						huds.showPlayerSide(currentPlayer);
            						mySheep.changeColor(currentPlayer);
            					}
            				}
            				else if(bodyB.getPosition().x < 25){
            					rightGoals++;
            					huds.addGoal(rightGoals, 1 , currentPlayer);
            					bodyA.setUserData("ball");
            					MyFirstGame.PLAYER_READY = false;
            					if(rightGoals == 10){
            						if(MyFirstGame.SOUNDS_ON)
            							Assets.winSnd.play(MyFirstGame.VOLUME+0.5f);
            						MyFirstGame.gameState = GameState.GAMEWIN;
            						menu.hideExtraButtons();
            						huds.showFinalResult();
            					}
            					else {
            						if(currentPlayer == 1)
            							currentPlayer = 0;
            						else 
            							currentPlayer = 1;
            						huds.setScoreboardPosition(currentPlayer);     						       
            						tower.setActiveGate(currentPlayer,gaurds.getGateY(),MARGIN);          						
            						moveCounter = 0;
            						foalCounter = 0;
            						lastMove = false;
            						huds.showPlayerSide(currentPlayer);
            						mySheep.changeColor(currentPlayer);
            					}
            				}           				
            			
            			}
            			else if(bodyB.getUserData().equals("rtkicker")){
            				//bodyB.setAngularVelocity(0);
            				//if(bodyB.getAngularVelocity() < 10)
            				if(MyFirstGame.SOUNDS_ON)
            					Assets.rtkickerSnd.play(MyFirstGame.VOLUME+0.2f);
            				bodyB.setUserData("rtkickerOn");
            				bodyB.setAngularVelocity(15);
            			//	dir *= -1;
            			}
            			else if(bodyB.getUserData().equals("dog")){
            				Assets.dogSnd.play(MyFirstGame.VOLUME);            				
            			}
               	        
            		}// end if balljoon
            	}
                
            }

            @Override
            public void endContact(Contact contact) {      }
            @Override
            public void preSolve(Contact contact, com.badlogic.gdx.physics.box2d.Manifold oldManifold) {       }
            @Override
            public void postSolve(Contact contact, com.badlogic.gdx.physics.box2d.ContactImpulse impulse) {       }

        });
    }

	private void createButtons()	{   
		 	 
		 pauseButton.setSize(6, 6);
		 pauseButton.setPosition(MyFirstGame.VIEWPORT_WIDTH - 6, MyFirstGame.VIEWPORT_HEIGHT-6);
		 pauseButton.setOrigin(3,3);
		 stage.addActor(pauseButton);
			
		pauseButton.addListener(new ClickListener() {
		
			@Override 
			public void clicked(InputEvent event, float x, float y) {
				if(MyFirstGame.gameState == GameState.RUNNING) {
					if(MyFirstGame.SOUNDS_ON)
						Assets.pauseSnd.play(MyFirstGame.VOLUME+0.5f);
					MyFirstGame.gameState = GameState.PAUSE;					
					menu.hideExtraButtons();
					pauseButton.addAction(Actions.color(Color.WHITE));			
				}
			}
				
			public void	enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				if(MyFirstGame.gameState == GameState.RUNNING){
					if(MyFirstGame.SOUNDS_ON)
						Assets.buttonSnd.play(MyFirstGame.VOLUME+0.4f);
					event.getListenerActor().addAction(Actions.color(Color.YELLOW));	
				}
				
			}
			public void	exit(InputEvent event, float x, float y, int pointer, Actor fromActor){	
				if(MyFirstGame.gameState == GameState.RUNNING)
					event.getListenerActor().addAction(Actions.color(Color.WHITE));			
			}
		});		
		
		// create a body to prevent objects passed through pause button
		BodyDef bodyDef = new BodyDef();  
		bodyDef.type = BodyType.StaticBody;  
		bodyDef.position.set(pauseButton.getX()+pauseButton.getWidth()/2, pauseButton.getY()+pauseButton.getHeight()/2);  
		Body sheepModel = world.createBody(bodyDef);  
		CircleShape dynamicCircle = new CircleShape();  
		dynamicCircle.setRadius(pauseButton.getWidth()/2);  
		FixtureDef fixtureDef = new FixtureDef();  
		fixtureDef.shape = dynamicCircle;  
		fixtureDef.density = 1f;  
		sheepModel.createFixture(fixtureDef);
		sheepModel.setUserData("kinematic");
		dynamicCircle.dispose();
		
	}
	
	
	QueryCallback callback = new QueryCallback() {
	@Override
	public boolean reportFixture (Fixture fixture) {
		// if the hit fixture's body is the ground body
		// we ignore it
		
		if (!(fixture.getBody().getUserData().equals("ball") || fixture.getBody().getUserData().equals("balljoon"))) return true;
		// if the hit point is inside the fixture of the body
		// we report it
		//if (fixture.testPoint(testPoint.x, testPoint.y)) {
			hitBody = fixture.getBody();
			mySheep.drawLine(false);
			return false;
		//} else
		//	return true;
	 }
   };
	
   Body currentBody = null;
	@Override
	public boolean touchDown (int x, int y, int pointer, int newParam) {
		dragTime = 0;	
		if(MyFirstGame.gameState == GameState.RUNNING && MyFirstGame.PLAYER_READY) {
			testPoint.set(x, y, 0);		
			camera.unproject(testPoint);					
			world.QueryAABB(callback, testPoint.x -1f, testPoint.y - 1f, testPoint.x + 1f, testPoint.y + 1f);		
			
			if(hitBody != null){				
				mySheep.setSelected(1);
				if(mySheep.isFoal()) {
					foalCounter++;					
					if(foalCounter > allowedFoals) {
						if(currentPlayer == 0){    					
	    					huds.setScoreboardPosition(1);	    					
	    					currentPlayer = 1;
	    				}
	    				else{    					
	    					huds.setScoreboardPosition(0);	    					
	    					currentPlayer = 0;
	    				}	
						tower.setActiveGate(currentPlayer,gaurds.getGateY(),MARGIN);
						mySheep.resetIndicator();
						MyFirstGame.PLAYER_READY = false;
						lastMove = false;
	    				moveCounter = 0;
	    				foalCounter = 0;
	    				huds.showPlayerSide(currentPlayer);
	    				mySheep.changeColor(currentPlayer);
	    				if(currentBody != null)  currentBody.setUserData("ball");
	    				currentBody = null;
	    				
	    				return false;
						/*MyFirstGame.gameState = GameState.GAMEOVER;
						menu.hideExtraButtons();*/
					}
					else
						huds.addFoal(foalCounter);
				}
				//if(MyFirstGame.PLAYER_READY == true) {  // check if PLAYER_READY isn't set to false in if(foalCounter > allowedFoals)
				if(currentBody != null && !currentBody.equals(hitBody)) {
					currentBody.setUserData("ball");
					currentBody = null;
				}			
				
				hitBody.setUserData("balljoon");		
				
				currentBody = hitBody;
				
				hitBody.setLinearVelocity(0, 0);
				hitBody.setAngularVelocity(0);
				
				MouseJointDef def = new MouseJointDef();
				def.bodyA = groundBody;
				def.bodyB = hitBody;
				def.collideConnected = true;
				def.target.set(testPoint.x, testPoint.y);
				def.maxForce = 20000;
			
				mouseJoint = (MouseJoint)world.createJoint(def);
				hitBody.setAwake(true);				
				
				hitBody = null;
				mySheep.drawLine(true);		

				// draw first line between body and mouse point
				//mousePoint.set(Gdx.input.getX(), Gdx.input.getY(),0);
				//camera.unproject(mousePoint);
				float distance = currentBody.getPosition().dst(testPoint.x, testPoint.y);
				   // float angle =  pos[0].angleRad(pos[1]);		 
				float angle = ((float)Math.atan2(currentBody.getPosition().y-testPoint.y, testPoint.x-currentBody.getPosition().x)) * MathUtils.radiansToDegrees;		  
				line.setPosition(currentBody.getPosition().x,currentBody.getPosition().y);	   
				line.setSize(distance, 0.25f);
				line.setRotation(-angle);	
				line.setVisible(true);
			}
			else if(currentBody != null){
				float distance = currentBody.getPosition().dst(testPoint.x, testPoint.y);
				//System.out.println(distance);
				if(distance <= 6) {
					currentBody.setUserData("balljoon");
					currentBody.setLinearVelocity(0, 0);
					//hitBody.setAngularVelocity(0);
					MouseJointDef def = new MouseJointDef();
					def.bodyA = groundBody;
					def.bodyB = currentBody;
					def.collideConnected = true;
					def.target.set(testPoint.x, testPoint.y);
					def.maxForce = 20000;
				
					mouseJoint = (MouseJoint)world.createJoint(def);
					currentBody.setAwake(true);				
					
					mySheep.drawLine(true);	
					
					// draw first line between body and mouse point					
					   // float angle =  pos[0].angleRad(pos[1]);		 
					float angle = ((float)Math.atan2(currentBody.getPosition().y-testPoint.y, testPoint.x-currentBody.getPosition().x)) * MathUtils.radiansToDegrees;		  
					line.setPosition(currentBody.getPosition().x,currentBody.getPosition().y);	   
					line.setSize(distance, 0.25f);
					line.setRotation(-angle);	
					line.setVisible(true);
				}
			}
		}
		return false;
	}

	Vector2 target = new Vector2();
	boolean sheepMoves = false;
	@Override
	public boolean touchDragged (int x, int y, int pointer) {	
		if (mouseJoint != null) {	
		/*	if(!sheepMoves){
			    mySheep.setSheepMoving();	
			    sheepMoves = true;
			    moveCounter++;
			    huds.addMove(moveCounter);
			    if(moveCounter == allowedMoves)
			    	lastMove = true;
			}	*/	
			if(!sheepMoves && currentBody!= null && !currentBody.getLinearVelocity().isZero(4f)){
				mySheep.setSelected(2);
				mySheep.setSheepMoving(true);	
			    sheepMoves = true;
			    moveCounter++;
			    Assets.sheepSnd.play(MyFirstGame.VOLUME);
			    if(!lastMove)
			    	huds.addMove(moveCounter);
			    if(moveCounter == allowedMoves)
			    	lastMove = true;
			}
			stage.getCamera().unproject(testPoint.set(x, y, 0));
			mouseJoint.setTarget(target.set(testPoint.x, testPoint.y));
			//sheepSound.play();
		}
		
		return false;
	}

	@Override
	public boolean mouseMoved (int x, int y) {			
		return false; 
		}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {		
		sheepMoves = false;		
		if (mouseJoint != null) {			
			/*if(!currentBody.getLinearVelocity().isZero(4f)){
				mySheep.setSheepMoving(true);	
			   // sheepMoves = true;
			    moveCounter++;
			    huds.addMove(moveCounter);
			    if(moveCounter == allowedMoves)
			    	lastMove = true;
			}*/
			world.destroyJoint(mouseJoint);
			mouseJoint = null;		
			line.setVisible(false);
		}
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		  case Keys.ESCAPE:
		  case Keys.BACK:
			 if(MyFirstGame.gameState == GameState.PAUSE){
				// menu.setActive(false);
				 MyFirstGame.gameState = GameState.RUNNING; 	
				 menu.setShowButtons(false);
			 }
			 else {
				 MyFirstGame.gameState = GameState.PAUSE;
				 menu.hideExtraButtons();
				// menu.setActive(true);
			 }
			 break;
		}
		return true;
	}
	@Override
	public boolean keyUp(int keycode) {	return false;	}
	@Override
	public boolean keyTyped(char character) {	return false;	}
	@Override
	public boolean scrolled(int amount) { return false; 	} 
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub		
		camera.viewportHeight = MyFirstGame.VIEWPORT_HEIGHT;
		camera.viewportWidth = MyFirstGame.VIEWPORT_HEIGHT * width/height ;		
		camera.update();
	}	
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub		
	/*	if(MyFirstGame.gameState == GameState.RUNNING) {
			MyFirstGame.gameState = GameState.PAUSE;		
			menu.hideExtraButtons();
		}*/
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub		
		if(MyFirstGame.gameState == GameState.PAUSE) {
			MyFirstGame.gameState = GameState.RUNNING;		
			menu.setShowButtons(false);
		}
		//menu.hideExtraButtons();
		
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub	
		//tower.disposeBackTexture();
		Assets.ingameMusic.stop();
		
		hintTexture.dispose();
		
		mySheep.destroy();
		tower.destroy();
		gaurds.destroy();
		huds.destroy();
		menu.destroy();
		
		destroyBodies();
		
		world.dispose();
		world = null;
	//***	debugRenderer.dispose();
	//***	debugRenderer = null;
		
		stage.clear();
		stage.dispose();		
	}
	
	public void destroyBodies()
	{
		Array<Body> towerModel=new Array<Body>();
		world.getBodies(towerModel);
		for (Iterator<Body> iter = towerModel.iterator(); iter.hasNext();) {
		     Body body = iter.next();
		     if(body!=null) {	
		    	 Array<JointEdge> list = body.getJointList();
		    	 while (list.size > 0) {
		 	        world.destroyJoint(list.get(0).joint);
		 	     }
		    	 world.destroyBody(body);
		    	 iter.remove();
		         body.setUserData(null);
		         body = null;
		     }
		     towerModel.clear();
	     }
	}


}
