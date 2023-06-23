package com.baranagames.sheepishescape.screens;

import java.util.Iterator;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;
import com.baranagames.sheepishescape.MyFirstGame.GameState;
import com.baranagames.sheepishescape.actors.Gaurds;
import com.baranagames.sheepishescape.actors.HUDs;
import com.baranagames.sheepishescape.actors.PauseMenu;
import com.baranagames.sheepishescape.actors.Sheeps;
import com.baranagames.sheepishescape.actors.Towers;

public class FirstScreen implements Screen , InputProcessor{
	
	private static final float BOX_STEP=1/60f;  
	private static final int BOX_VELOCITY_ITERATIONS=6;  
	private static final int BOX_POSITION_ITERATIONS=2;
    private Sheeps mySheep;
    private Towers tower;
    private MyFirstGame game;
    private Stage stage, stage2;
    private World world;
    //private Box2DDebugRenderer debugRenderer;
  	private Vector3 testPoint = new Vector3(10 ,  50 , 0);
	private Vector3 mousePoint = new Vector3();
	private Body hitBody = null;
	private Body groundBody;
	private Body currentBody = null , crazyBody = null;
	private final Image pauseButton = new Image(Assets.BUTTONS[4]);
	private Image line;
	private Image trickImg = new Image(Assets.GAURDS[27]);	
	private InputMultiplexer inputMultiplexer = new InputMultiplexer();
	private Camera camera;
	private PauseMenu menu;   
    private HUDs huds;
    private Gaurds gaurds;
    private int wantedGoals, allowedFoals , crazyCounter;   
    private int goalCounter , foalCounter , moveCounter , collsionCounter;
    private int currentLevel , agentTTL;
    private float OFFSET, MARGIN ,dragTime , crazyTime , allowedDragTime , sheepRadious;
    private float agentOffTime , horseOffTime , cowOffTime , ratio;
    private float green , red , blue;
    private Vector2 target = new Vector2();
    private boolean sheepMoves , sheepGoCrazy  , gateMoving;
	private MouseJoint mouseJoint;	
	private Image hintImg , buyImg;
	private Texture hintTexture , buyTexture;	
	private int origScore;
	private BitmapFont font;
	private String totalScore= "";
		
	public FirstScreen(MyFirstGame game, int level)	{
		agentTTL = 0;
		hintImg = buyImg = null;
		hintTexture = buyTexture = null;
		dragTime = crazyTime = agentOffTime = horseOffTime = cowOffTime = 0;
		foalCounter = goalCounter = crazyCounter = 0;
		moveCounter = collsionCounter = 0;
		currentLevel = level;		
		origScore = 500;
		this.game = game;
		sheepMoves =  sheepGoCrazy = gateMoving = false;
		hitBody = currentBody = crazyBody = null;
				
		MyFirstGame.gameState = GameState.RUNNING;	
		//debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0f, 0f), true); 
		createCollisionListener();
		
		//System.out.println(stage.getCamera().viewportWidth);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		MyFirstGame.VIEWPORT_WIDTH = MyFirstGame.VIEWPORT_HEIGHT * w / h;	
		OFFSET = (MyFirstGame.VIEWPORT_WIDTH - 80);	
		MARGIN = 0;
		if(OFFSET > 16)  // screen aspect ratio more than 8:5
		{
			MARGIN = (OFFSET - 16)/2;
			OFFSET = MARGIN + 16;	
			
		}
		//System.out.println(OFFSET);
		
		stage = new Stage(new StretchViewport(MyFirstGame.VIEWPORT_WIDTH, MyFirstGame.VIEWPORT_HEIGHT));
		
		camera = stage.getCamera();
        camera.position.set(MyFirstGame.VIEWPORT_WIDTH-MyFirstGame.VIEWPORT_WIDTH/2,MyFirstGame.VIEWPORT_HEIGHT/2, 0);
       
        camera.update();     
        
        stage2 = new Stage();
        stage2.getCamera().position.set(w/2, h/2, 0);
        stage2.getCamera().update();        
    
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
		//Gdx.input.setCursorCatched(true);
		
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
		//inputMultiplexer.addProcessor(stage2);
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(true);
			
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size =Gdx.graphics.getWidth()/30;
		parameter.color = Color.DARK_GRAY;
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		
		createBorders("l_vertical_border");
		createBorders("r_vertical_border");		
		createBorders("ceil");
		createBorders("ground");	
		
		sheepRadious=2.5f;
    	allowedDragTime = 0.35f;
    	if(Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS){
    		sheepRadious = 3;// set radious  = 3
    		//allowedDragTime = 0.6f;
    	}    	
    	  
		mySheep = new Sheeps(world,groundBody);
			
		mySheep.addSheepBody("ball", sheepRadious, 15+OFFSET ,20);	
		mySheep.addSheepBody("ball", sheepRadious, 20+OFFSET ,30);	
		mySheep.addSheepBody("ball", sheepRadious, 15+OFFSET ,40);	
		//i = j;			

		addOtherObjects();
		
		huds = new HUDs(wantedGoals,allowedFoals);
		menu = new PauseMenu(game,this,currentLevel);	
		
		trickImg.setSize(5, 5);
		trickImg.setOrigin(1, 1);
		trickImg.addAction(Actions.hide());
		
		line = new Image(Assets.SHEEPS[7]);
        line.setOrigin(0, 0);
        line.setVisible(false);		
		
		//stage.addActor(goalLineImg);	
		stage.addActor(tower);		   
		stage.addActor(gaurds);	
		stage.addActor(mySheep);
		stage.addActor(huds);
		stage.addActor(trickImg);
		stage.addActor(line);
		if(hintImg != null)
			stage.addActor(hintImg);
		stage.addActor(menu);			
		//else System.out.println("nulll");
		createButtons(); 
		
		if(buyImg != null)
			stage.addActor(buyImg);
		
		MyFirstGame.PLAYER_READY = true;
		
		if(MyFirstGame.SOUNDS_ON) {
			Assets.ingameMusic.setVolume(MyFirstGame.VOLUME+0.4f);
			Assets.ingameMusic.setLooping(true);
			Assets.ingameMusic.play();					
		}
     
	}
	
	public void addOtherObjects(){
		
		String gateNo = String.valueOf((currentLevel-1)/24);
		if(currentLevel >= 97)  gateNo = "0";
		gaurds = new Gaurds(world,gateNo);
		
		FileHandle handle = Gdx.files.internal("data/Levs"+ String.valueOf((currentLevel-1)/24)+".txt");
		String assetList[] = handle.readString().split(";");
//		System.out.println(handle.readString());
		
        int i;
        for(i=0;;i++)
        	if(assetList[i].contains("lev"+Integer.toString(currentLevel))) 
        		break;
                
        i++;  // pass "levX" word
        
        tower = new Towers(world,groundBody,assetList[i++],gateNo,MARGIN);
       // i++; // pass back word !!!!!!
        red = Float.parseFloat(assetList[i++]);
        green = Float.parseFloat(assetList[i++]);
        blue = Float.parseFloat(assetList[i++]);
    	 
    	wantedGoals = Integer.parseInt(assetList[i]);
    	allowedFoals = Integer.parseInt(assetList[i+1]);
    	i+=2;
    	
    	float scrW = Gdx.graphics.getWidth() , scrH = Gdx.graphics.getHeight();
    	ratio = scrH / MyFirstGame.VIEWPORT_HEIGHT;
    	if(!assetList[i].equals("towers")){  // then it is a hint!!
    		
    		if(scrW > 1024 && scrH > 512)
    			hintTexture = new Texture(Gdx.files.internal(assetList[i]+".png"));
    		else
    			hintTexture = new Texture(Gdx.files.internal(assetList[i]+"b.png"));
    		hintImg = new Image(hintTexture);    		
    		hintImg.setSize(hintTexture.getWidth()/ratio, hintTexture.getHeight()/ratio);
    	//	hintImg.setPosition(MyFirstGame.VIEWPORT_WIDTH/2 - hintImg.getWidth()/2, MyFirstGame.VIEWPORT_HEIGHT/2 - hintImg.getHeight()/2);
    		hintImg.setPosition(2, 2);
    		hintImg.setOrigin(0,0);
    		//hintImg.addAction(Actions.sequence(Actions.scaleTo(1.2f, 1.2f,0.5f),Actions.scaleTo(1f, 1f,0.5f),Actions.delay(5)
    			//	,Actions.scaleTo(0.5f, 0.5f,0.5f),Actions.moveTo(2, 2,1),Actions.delay(10),Actions.removeActor()));
    		hintImg.addAction(Actions.sequence(Actions.moveTo(MyFirstGame.VIEWPORT_WIDTH/2 - hintImg.getWidth()/2, MyFirstGame.VIEWPORT_HEIGHT/2 - hintImg.getHeight()/2, 0.5f)
    				,Actions.delay(4),Actions.moveTo(2, 2,1),Actions.fadeOut(2)));
    		
    		
    		i++;
    	}
		//***************       Towers        *****************
		i++; // pass "towers" word
		//tower = new Towers(world,groundBody);
		if(!MyFirstGame.IS_PREMIUM && currentLevel == MyFirstGame.buyLevel-1){
			buyTexture = new Texture(Gdx.files.internal("buy.png"),true);
			buyTexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
			buyImg = new Image(buyTexture);			
			buyImg.setSize(buyTexture.getWidth()/ratio, buyTexture.getHeight()/ratio);
			buyImg.setPosition(MyFirstGame.VIEWPORT_WIDTH/2 -buyImg.getWidth()/2, MyFirstGame.VIEWPORT_HEIGHT/2 - buyImg.getHeight()/2);
			buyImg.setVisible(false);
		}
		//Vector2 gatePos = null;
		while(!assetList[i].equals("guards")){			
			if(assetList[i].equals("chicks")){
				tower.AddChicksTower(Integer.parseInt(assetList[i+1]) , Float.parseFloat(assetList[i+2])+OFFSET, Float.parseFloat(assetList[i+3]));
				i += 4;
			}
			else{
				tower.addTower(assetList[i], Float.parseFloat(assetList[i+1])+OFFSET , Float.parseFloat(assetList[i+2])
						, Float.parseFloat(assetList[i+3]) , Float.parseFloat(assetList[i+4]) , Float.parseFloat(assetList[i+5]));
				i += 6;				
			}
		}		
		//***************       guards objects        *****************
		i++; // pass "guards" word
		//gaurds = new Gaurds(world , false);
		while(!assetList[i].equals("end")){
			if(assetList[i].equals("dog")) {
				gaurds.addGaurd(Integer.parseInt(assetList[i+1]),Float.parseFloat(assetList[i+2])+OFFSET,Float.parseFloat(assetList[i+3])
						, Float.parseFloat(assetList[i+4]),Float.parseFloat(assetList[i+5]),sheepRadious);				
				i += 6;
			}
			else if(assetList[i].equals("gate")){				
				//gatePos = new Vector2(Float.parseFloat(assetList[i+1]) , Float.parseFloat(assetList[i+2]));
				gaurds.addGate(MyFirstGame.VIEWPORT_WIDTH-MARGIN-1.75f,Float.parseFloat(assetList[i+1]) , Float.parseFloat(assetList[i+2])
						, Float.parseFloat(assetList[i+3]));
				tower.setActiveGate(0,Float.parseFloat(assetList[i+1]),MARGIN);
				i += 4;
			}
			else  {
				gaurds.addObjects(assetList[i],Float.parseFloat(assetList[i+1])+OFFSET,Float.parseFloat(assetList[i+2])
				, sheepRadious);//-0.5f);
				i += 3;
			}
		}
		//gaurds.addGate(30, 16, 0);
	}
	
	public void reset(){		
		line.setVisible(false);
		agentTTL = 0;
		origScore = 500;
		agentOffTime = horseOffTime = cowOffTime = 0;
		dragTime = crazyTime = 0;
		crazyCounter = 0;
		totalScore = "";
		if(hintTexture != null){
			hintImg.clearActions();
			hintImg.setVisible(false);
			hintTexture.dispose();
			hintTexture =null;
			hintImg = null;			
		}
		hitBody = currentBody = crazyBody = null;
		foalCounter = goalCounter = moveCounter = collsionCounter = 0;
		sheepMoves = sheepGoCrazy = gateMoving = false;		
		FileHandle handle = Gdx.files.internal("data/Levs"+ String.valueOf((currentLevel-1)/24)+".txt");
		String assetList[] = handle.readString().split(";");		
		gaurds.reset(OFFSET, assetList, currentLevel);
		mySheep.reset(OFFSET);			
		huds.reset(wantedGoals, allowedFoals);
		tower.reset(OFFSET, assetList , currentLevel);
		tower.moveGateArea(gaurds.getGateY());
		MyFirstGame.gameState = GameState.RUNNING;
		menu.setShowButtons(false);
	}
	
	// last = 0;
	//FPSLogger logger = new FPSLogger();
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		/*if (TimeUtils.nanoTime() - last > 2000000000) {
			logger.log();
			Gdx.app.log("SuperJumper",
				"version: " + Gdx.app.getVersion() + ", memory: " + Gdx.app.getJavaHeap()/(1024*1024) + ", " + Gdx.app.getNativeHeap()/(1024*1024)
					+ ", native orientation:" + Gdx.input.getNativeOrientation() + ", orientation: " + Gdx.input.getRotation()
					+ ", accel: " + (int)Gdx.input.getAccelerometerX() + ", " + (int)Gdx.input.getAccelerometerY() + ", "
					+ (int)Gdx.input.getAccelerometerZ() + ", apr: " + (int)Gdx.input.getAzimuth() + ", " + (int)Gdx.input.getPitch()
					+ ", " + (int)Gdx.input.getRoll());
			last = TimeUtils.nanoTime();
		}*/
		
		Gdx.gl.glClearColor(red,green, blue, 1f );
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
			if(sheepGoCrazy && crazyBody != null){
				crazyTime += Gdx.graphics.getDeltaTime();				
				if(crazyTime > 0.6f) {
					crazyCounter++;
					crazyBody.setLinearVelocity(MathUtils.random(-150, 150),MathUtils.random(-150, 150));
					crazyTime = 0;
					//System.out.println(currentBody.getLinearVelocity());
				}
				if(crazyCounter >= 5) {
					crazyCounter = 0;
					crazyTime = 0;
					sheepGoCrazy = false;
					crazyBody = null;
				}
			}
			if(currentBody != null && currentBody.getLinearVelocity().isZero(1)){
				mySheep.setSelected(1);
				if(mySheep.isFoal()) {
					foalCounter++;		
					origScore -= 20;
					if(foalCounter > allowedFoals) {
						MyFirstGame.gameState = GameState.GAMEOVER;
						menu.hideExtraButtons();
						huds.showLost();
						//line.setVisible(false);
					}
					else 
						huds.addFoal(foalCounter);
				}
			}
			
			if(gateMoving && gaurds.getGateY() != -1){
				gateMoving = false;
				tower.moveGateArea(gaurds.getGateY());
			}
			
			if(agentOffTime > 0){				
				agentOffTime -= Gdx.graphics.getDeltaTime();
				if(agentOffTime <= 0){
					agentOffTime = 0;					
				}
			}
			if(horseOffTime > 0){				
				horseOffTime -= Gdx.graphics.getDeltaTime();
				if(horseOffTime <= 0){
					horseOffTime = 0;					
				}
			}
			if(cowOffTime > 0){				
				cowOffTime -= Gdx.graphics.getDeltaTime();
				if(cowOffTime <= 0){
					cowOffTime = 0;					
				}
			}			
			
		} // end if Running
		
		//stage2.getBatch().getProjectionMatrix().setToOrtho2D(0, 0,  Gdx.graphics.getWidth() ,  Gdx.graphics.getHeight());
		stage2.getBatch().begin();
		font.draw(stage2.getBatch(), "Points: "+origScore+totalScore, (2f+MARGIN)*ratio , 59 *ratio);
		stage2.getBatch().end();
		
	}
	
	public void setBuyVisible(){
		if(buyImg != null)
			buyImg.setVisible(true);
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
			bd.position.set(MyFirstGame.VIEWPORT_WIDTH-0.75f-MARGIN,MyFirstGame.VIEWPORT_HEIGHT/2);
		
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
				if(MyFirstGame.gameState == GameState.RUNNING) {
					if(MyFirstGame.SOUNDS_ON)
						Assets.buttonSnd.play(MyFirstGame.VOLUME+0.4f);
					pauseButton.addAction(Actions.color(Color.YELLOW));	
				}
				//Assets.pauseSnd.play(MyFirstGame.VOLUME);
			}
			public void	exit(InputEvent event, float x, float y, int pointer, Actor fromActor){	
				if(MyFirstGame.gameState == GameState.RUNNING)
				    pauseButton.addAction(Actions.color(Color.WHITE));			
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
	     sheepModel.setUserData("pauseBody");
	     dynamicCircle.dispose();
		
		
	}	
	
	private void createCollisionListener() {
        world.setContactListener(new ContactListener() {
         //   Body bodyA , bodyB;
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
            			if(bodyB.getUserData().equals("woman")) { 	
            				Assets.womanSnd.play(MyFirstGame.VOLUME);
            				if(crazyBody != null && bodyA == crazyBody) 
            					crazyBody = null;
            				bodyA.setUserData("sleepy");      
            				if(currentBody != null)  currentBody = null;
            				collsionCounter++;
            				origScore -= 30;
            			}
            			else if(bodyB.getUserData().equals("wolf"))  {
            				if(crazyBody != null && bodyA == crazyBody) 
            					crazyBody = null; 
            				Assets.wolfSnd.play(MyFirstGame.VOLUME);
            				bodyA.setUserData("eaten");
            				bodyB.setUserData("deleted");
            				if(currentBody != null)  currentBody = null;
            				collsionCounter++;
            				origScore -= 30;
            			}            				    	
            			else if(bodyB.getUserData().equals("sticky"))  	{
            				Assets.stickSnd.play(MyFirstGame.VOLUME);
            				gaurds.setStickyBody(bodyA);
            				bodyB.setUserData("catched");  
            				collsionCounter++;
            				origScore -= 30;
            			}            			
            			else if(bodyB.getUserData().equals("goal") && mySheep.isCorrectlyPassed()){      
            				if(crazyBody != null && bodyA == crazyBody) 
            					crazyBody = null;   
            				bodyA.setUserData("escaped");
            				huds.showScapee(bodyA.getPosition().x, bodyA.getPosition().y , gaurds.getGatePos());
            				
            				addScore();            				
                	 //  currentBody.setUserData("ball");
            			}
            			else if(bodyB.getUserData().equals("grass")){            				
            				//bodyB.setUserData("deleted");
            				if(crazyBody == null || crazyBody != bodyA){
            					Assets.grassSnd.play(MyFirstGame.VOLUME);            					
	            				crazyBody = null;
	            				sheepGoCrazy = true;
	            				crazyCounter = 0;
	            				crazyTime = 0;
	            				bodyA.setLinearVelocity(MathUtils.random(-150, 150),MathUtils.random(-150, 150));  
	            				crazyBody = bodyA;
            				}
            				//	bodyA.setLinearVelocity(bodyA.getLinearVelocity().scl(5));
            			}
            			else if(bodyB.getUserData().equals("man")){
            				Assets.manSnd.play(MyFirstGame.VOLUME);
            				if(crazyBody != null && bodyA == crazyBody) 
            					crazyBody = null;        				
            				bodyA.setUserData("caged");
            				bodyA.setAngularVelocity(0);
            				bodyB.setUserData("deleted");
            				if(currentBody != null)  currentBody = null;
            				collsionCounter++;
            				origScore -= 30;
            			}
            			/*else if(bodyB.getUserData().equals("hanging")){
            				bodyB.setAngularVelocity(0);
            				//if(bodyB.getAngularVelocity() < 10)
            				bodyB.setAngularVelocity(10);
            			}*/
            			else if(bodyB.getUserData().equals("rtkicker")){
            				//bodyB.setAngularVelocity(0);
            				//if(bodyB.getAngularVelocity() < 10)
            				if(MyFirstGame.SOUNDS_ON)
            					Assets.rtkickerSnd.play(MyFirstGame.VOLUME+0.2f);
            				bodyB.setUserData("rtkickerOn");
            				bodyB.setAngularVelocity(15);
            			//	dir *= -1;
            			}
            			else if(bodyB.getUserData().equals("cow")){   
            				if(cowOffTime == 0) {  // cow is ready
            					cowOffTime = 3;
	            				bodyB.setLinearVelocity(bodyA.getLinearVelocity().x * 2 , bodyA.getLinearVelocity().y * 2);
	            				gaurds.moveGate();
	            				gateMoving = true;
	            				Assets.cowSnd.play(MyFirstGame.VOLUME);
            				}
            			}
            			else if(bodyB.getUserData().equals("goat")){
            				bodyB.setLinearVelocity(bodyA.getLinearVelocity().rotate(180).scl(2));
            				bodyA.setLinearVelocity(bodyA.getLinearVelocity().rotate(180));
            				collsionCounter++;
            				origScore -= 30;
            				Assets.goatSnd.play(MyFirstGame.VOLUME);
            			}
            			else if(bodyB.getUserData().equals("snake")){
            				Assets.snakeSnd.play(MyFirstGame.VOLUME);
            				mySheep.setBitedBody(bodyA);
            				bodyB.setUserData("deleted");
            			}
            			else if(bodyB.getUserData().equals("horse")){
            				if(horseOffTime == 0) {   //horse is ready
            					horseOffTime = 3;
	            				Assets.horseSnd.play(MyFirstGame.VOLUME);
	            				bodyB.setLinearVelocity(bodyA.getLinearVelocity().x * 2 , bodyA.getLinearVelocity().y * 2);
	            				tower.moveRandomTower();
            				}
            			}
            			else if(bodyB.getUserData().equals("dog")){
            				Assets.dogSnd.play(MyFirstGame.VOLUME);            				
            			}
            			else if(bodyB.getUserData().equals("frog")){
            				Assets.frogSnd.play(MyFirstGame.VOLUME);            				
            			}
            		    else if(bodyB.getUserData().equals("agent")){          		    	//bodyB.setUserData("agentOff");
            		    	if(agentOffTime == 0) {    // agent is ready
            		    		Assets.agentSnd.play(MyFirstGame.VOLUME);
            		    		agentOffTime = 3;    // 3 seconds agnet inactive
	            		    	bodyB.setLinearVelocity(bodyA.getLinearVelocity().x * 2 , bodyA.getLinearVelocity().y * 2);
	            				agentTTL++;            				
	            				//bodyA.setUserData("ball");
	            				if(agentTTL >= 4) bodyB.setUserData("deleted");
	            				int rnd = MathUtils.random(5);            				
								
	            				switch (rnd) {
								case 0:		// foal--					
									trickImg.setPosition(huds.getXPosition(0),54);
									if(foalCounter > 0) foalCounter--;
									huds.addFoal(foalCounter);
									break;
								case 1:     // goal++
									trickImg.setPosition(huds.getXPosition(1),54);
									if(crazyBody != null && bodyA == crazyBody) 
		            					crazyBody = null; 
									bodyA.setUserData("escaped");
									huds.showScapee(bodyA.getPosition().x, bodyA.getPosition().y,gaurds.getGatePos());
									addScore();
									break;
								case 2:    // move gate
									trickImg.setPosition(gaurds.getGatePos().x,gaurds.getGatePos().y);
									gaurds.moveGate();
									Assets.cowSnd.play(MyFirstGame.VOLUME);
									gateMoving = true;
									break;
								case 3:   //crazy grass
									Assets.grassSnd.play(MyFirstGame.VOLUME);
									trickImg.setPosition(bodyA.getPosition().x,bodyA.getPosition().y);
									sheepGoCrazy = true;
									crazyCounter = 0;
									crazyTime = 0;
		            				bodyA.setLinearVelocity(MathUtils.random(-150, 150),MathUtils.random(-150, 150)); 
		            				crazyBody = bodyA;
									break;
								case 4:   // caged
									Assets.manSnd.play(MyFirstGame.VOLUME);
									if(crazyBody != null && bodyA == crazyBody) 
		            					crazyBody = null;   
									trickImg.setPosition(bodyA.getPosition().x,bodyA.getPosition().y);
									bodyA.setUserData("caged");
		            				if(currentBody != null)  currentBody = null;
									break;
								case 5:
									Assets.horseSnd.play(MyFirstGame.VOLUME);
									trickImg.setPosition(bodyB.getPosition().x,bodyB.getPosition().y);
		            				tower.moveRandomTower();
									break;
								default:
									break;
								}
	            				trickImg.addAction(Actions.sequence(Actions.show(),Actions.rotateBy(360, 2),Actions.hide()));
            		    	}
            			}
               	        
            		}
            	}
                
            }
            
            private void addScore(){
            	goalCounter++;
				huds.addGoal(goalCounter);				
				MyFirstGame.PLAYER_READY = false;
				mySheep.checkBited(currentBody);
				if(hintImg != null && goalCounter == 1){
					hintImg.clearActions();
	    			hintImg.addAction(Actions.sequence(Actions.fadeIn(0.5f),Actions.delay(2f),Actions.fadeOut(1)));
				}
				// check if current ball sticked to a sticky body ?
				 Array<JointEdge> list = currentBody.getJointList();				 
		    	 if (list.size > 0) {
		    		 if(list.get(0).joint.getBodyB().getUserData().equals("sticked"))
		    			 list.get(0).joint.getBodyB().setUserData("rstSticky");
		    		 else
		    			 list.get(0).joint.getBodyA().setUserData("rstSticky");
		    		// System.out.println(list.get(0).joint.getBodyA().getUserData()+"  B: "+list.get(0).joint.getBodyB().getUserData());
		    	 }
		    	 currentBody = null;
		    	// if(bodyB != null )System.out.println(bodyB.getJointList().size+"  "+currentBody.getJointList().size);
				//mySheep.resetIndicator();
				Timer.schedule(new Task(){
				    @Override
				    public void run() {
				        // Do your work
				    	if(goalCounter < wantedGoals)
				    		mySheep.reorderSheeps();
				    	MyFirstGame.PLAYER_READY = true;
				    	if(goalCounter == wantedGoals)  {
				    		if(MyFirstGame.SOUNDS_ON)
				    			Assets.winSnd.play(MyFirstGame.VOLUME+0.5f);
				    		MyFirstGame.gameState = GameState.GAMEWIN;
				    		// calculate user score : 1 or 2 or 3
        					//int badFactors = foalCounter + moveCounter + collsionCounter;
        					/*int score;
        					if(badFactors <= (3*wantedGoals + 4))   score = 300;
        					else if(badFactors <= (4*wantedGoals + 8))  score = 200;
        					else score = 100;*/
        					//System.out.println("badfactor:"+badFactors+"      score ( 1 2 3);"+score);
        					
        					menu.hideExtraButtons();
        					
        					FileHandle handle = Gdx.files.local("Sheepish.txt");
        					String file_content[] = handle.readString().split(";");
        					String modifiedContent = "";       		
        					     					
        					if(currentLevel== Integer.parseInt(file_content[4])) { // current level is last passed level
        						if(currentLevel < 120)
        							file_content[4] = Integer.toString(currentLevel+1);				
        						file_content[currentLevel+4] = Integer.toString(origScore);      // set user score 
        						MyFirstGame.TOTAL_SCORE += origScore;
        						file_content[3] = Integer.toString(MyFirstGame.TOTAL_SCORE);  
        						for(int i=0; i<=currentLevel+4 ;i++)
        							modifiedContent += file_content[i]+";";
        						if(currentLevel < 120)
        							modifiedContent += "0;";            						
        					}
        					else // current level is already played and has a score in file
        					{
        						if(Integer.parseInt(file_content[currentLevel+4]) < origScore) {
        							MyFirstGame.TOTAL_SCORE += (origScore - Integer.parseInt(file_content[currentLevel+4]));        							
        							file_content[3] = Integer.toString(MyFirstGame.TOTAL_SCORE);  
        							file_content[currentLevel+4] = Integer.toString(origScore);
        						}
        						for(int i=0; i<=Integer.parseInt(file_content[4]) + 4 ;i++)
        							modifiedContent += file_content[i]+";";
        						//System.out.println(modifiedContent);            						
        					}                    					
        					handle.writeString(modifiedContent, false);
        					totalScore = "\nTotal Score: "+MyFirstGame.TOTAL_SCORE;
        				}
				    	
				    }
				}, 2);
            }
                       
            @Override
            public void endContact(Contact contact) {   
            	Body bodyA = contact.getFixtureA().getBody();
        		Body bodyB = contact.getFixtureB().getBody();
        		if(bodyA.getUserData().equals("balljoon") && bodyB.getUserData().equals("chick")) {
        			bodyA.setLinearVelocity(bodyA.getLinearVelocity().x*3,-bodyA.getLinearVelocity().y*3);
        			Assets.chickSnd.play(MyFirstGame.VOLUME);
        		}
            	else if(bodyB.getUserData().equals("balljoon") && bodyA.getUserData().equals("chick")) {
            		bodyB.setLinearVelocity(bodyB.getLinearVelocity().x*3,-bodyB.getLinearVelocity().y*3);
            		Assets.chickSnd.play(MyFirstGame.VOLUME);
            	}
            	else if(bodyA.getUserData().equals("frog") && (bodyB.getUserData().equals("kinematic") || bodyB.getUserData().equals("rtkicker"))){
            		bodyB.setUserData("deleted");
            		bodyA.setUserData("deleted");
            		Assets.explodeSnd.play(MyFirstGame.VOLUME);
            	}        			
            	else if(bodyB.getUserData().equals("frog") && (bodyA.getUserData().equals("kinematic") || bodyA.getUserData().equals("rtkicker"))){
            		bodyB.setUserData("deleted");
            		bodyA.setUserData("deleted");
            		Assets.explodeSnd.play(MyFirstGame.VOLUME);
            	}            		
            }
            @Override
            public void preSolve(Contact contact, com.badlogic.gdx.physics.box2d.Manifold oldManifold) {       }
            @Override
            public void postSolve(Contact contact, com.badlogic.gdx.physics.box2d.ContactImpulse impulse) {       }

        });
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
	//	} else
		//	return true;
	 }
   };
   
 	@Override
	public boolean touchDown (int x, int y, int pointer, int newParam) {
		dragTime = 0;	
			if(MyFirstGame.gameState == GameState.RUNNING && MyFirstGame.PLAYER_READY) {
			
		/*	if(hitBody != null) {				
				//hitBody.setUserData("ball");
				currentBody = hitBody;
				hitBody = null;					    
			}*/
			if(currentBody != null && currentBody.getUserData().equals("inactive"))
				currentBody = null;
			
			testPoint.set(x, y, 0);		
			camera.unproject(testPoint);					
			world.QueryAABB(callback, testPoint.x -1.5f, testPoint.y -1.5f, testPoint.x + 1.5f, testPoint.y + 1.5f);		
					
			if(hitBody != null){	
				mySheep.setSelected(1);
				if(mySheep.isFoal()) {
					foalCounter++;					
					if(foalCounter > allowedFoals) {
						MyFirstGame.gameState = GameState.GAMEOVER;
						menu.hideExtraButtons();
						huds.showLost();
					}
					else
						huds.addFoal(foalCounter);
				}
					
				if(currentBody != null && !currentBody.equals(hitBody)) {
					//System.out.println(currentBody.getUserData());
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

	@Override
	public boolean touchDragged (int x, int y, int pointer) {	
		if (mouseJoint != null) {			
			if(!sheepMoves && currentBody!= null && !currentBody.getLinearVelocity().isZero(4f)){
				//*** currentBody != null ==> for events that user selects sheep and suddenly collide 
				//*** with something like woman that set currentBody = null 
				mySheep.setSelected(2);
				moveCounter++;
				origScore -= 10;
				Assets.sheepSnd.play(MyFirstGame.VOLUME);
			    mySheep.setSheepMoving(true);
			    sheepMoves = true;
			}
			stage.getCamera().unproject(testPoint.set(x, y, 0));
			mouseJoint.setTarget(target.set(testPoint.x, testPoint.y));			
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
			 else if( MyFirstGame.gameState == GameState.RUNNING) {
				 MyFirstGame.gameState = GameState.PAUSE;
				 menu.hideExtraButtons();
				// menu.setActive(true);
			 }		
			 else if(MyFirstGame.gameState == GameState.GAMEWIN) {
				 if(buyImg != null && buyImg.isVisible())
					 buyImg.setVisible(false);
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
		/*if(MyFirstGame.gameState == GameState.RUNNING) {
			MyFirstGame.gameState = GameState.PAUSE;		
			menu.hideExtraButtons(-1);
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
		//Gdx.input.setInputProcessor(null);
		//Assets.ingameMusic.stop();
		
		if(hintTexture != null){
			hintTexture.dispose();
			hintTexture =null;
			hintImg = null;			
		}
		if(buyTexture != null){
			buyTexture.dispose();
			buyTexture =null;
			buyImg = null;			
		}
		
		mySheep.destroy();
		tower.destroy();
		gaurds.destroy();
		huds.destroy();
		menu.destroy();
		
		destroyAllBodies();
		
		font.dispose();
		world.dispose();
		world = null;
		//*****debugRenderer.dispose();
		//*********debugRenderer = null;		
		stage.clear();
		stage.dispose();
		stage2.clear();
		stage2.dispose();
	}
	
	public void destroyAllBodies()	{
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
	     }
		towerModel.clear();
	}
	

}
