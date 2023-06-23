package com.baranagames.sheepishescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.baranagames.sheepishescape.MyFirstGame;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.baranagames.sheepishescape.Assets;


public class CommentScreen implements Screen {
	
	private MyFirstGame game;
	private Stage stage;
	private Image forButton , commentImg;
	private Texture pictures[] = {null, null, null};
	private int levelNo , picIndex;
	private String imgName = "";
	
	public CommentScreen(MyFirstGame game, int levelNo) {
		this.game = game;		
		this.levelNo = levelNo;
		picIndex = 0;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.9f, 0.9f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
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
		//Gdx.input.setCursorCatched(false);
		int scrWitdh = Gdx.graphics.getWidth();
		int scrHeight = Gdx.graphics.getHeight();
		/*if(scrW >= 2048)     imgEnd = "b.png";
		else     imgEnd = ".png";*/
		
		switch (levelNo) {
		case 1:		pictures[0] = new Texture(Gdx.files.internal("intro.png"),true);	  imgName="intro";	break;
		case 25:	pictures[0] = new Texture(Gdx.files.internal("part2.png"),true);	  imgName="part2";	break;
		case 49:	pictures[0] = new Texture(Gdx.files.internal("part3.png"),true);	  imgName="part3";	break;
		case 73:	pictures[0] = new Texture(Gdx.files.internal("part4.png"),true);	  imgName="part4";	break;
		case 97:	pictures[0] = new Texture(Gdx.files.internal("part5.png"),true);	  imgName="part5";	break;
		default:  pictures[0] = new Texture(Gdx.files.internal("part6.png"),true);	  imgName="part6"; // game have been finished
			break;
		}
		
		pictures[0].setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);		
		float imgW = pictures[0].getWidth() , imgH = pictures[0].getHeight();
		if(pictures[0].getWidth() > scrWitdh){
			//imgH = (imgH * scrW) / imgW;
			imgW = scrWitdh - 50;	
			imgH = imgW/2;
		}
		//System.out.println(imgW + "  "+imgH);
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
		Cursor cursor = Gdx.graphics.newCursor(pm, 0, 0);
		if (cursor != null)
		    cursor.setSystemCursor();
		//Gdx.input.setCursorImage(pm, 0, 0);
		//Gdx.graphics.setCursor(Gdx.graphics.newCursor(pixmap, 16, 16));
		pm.dispose();	
			
		commentImg  = new Image(pictures[0]);	//Assets.textureAtlas.findRegion("ball"));		
		commentImg.setSize(imgW, imgH);
		//graphistImg.setOrigin(128, 64);
		commentImg.setPosition(scrWitdh/2 - commentImg.getWidth()/2, scrHeight/2 - commentImg.getHeight()/2);
		stage.addActor(commentImg);
		
		
		float width = 200;
		if(scrWitdh < 2048)
			width = scrWitdh/10;
		
		forButton = new Image (Assets.BUTTONS2[5]);
		forButton.setName("forw");
		forButton.setSize(width, width);
		forButton.setOrigin(forButton.getWidth()/2, forButton.getHeight()/2);
		forButton.setPosition(scrWitdh - forButton.getWidth() - 20 , 10);
		forButton.addListener(myInputListener);
		stage.addActor(forButton);
		
		//System.out.println(forButton.getWidth());
		
		if(MyFirstGame.SOUNDS_ON) {
			/*Assets.commentSnd.loop();
			Assets.commentSnd.play(MyFirstGame.VOLUME);	*/
			Assets.CommentMusic.setVolume(MyFirstGame.VOLUME+0.4f);
			Assets.CommentMusic.setLooping(true);
			Assets.CommentMusic.play();			
		}
	
	}
	
	public InputListener myInputListener = new InputListener() {		
		
		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			// TODO Auto-generated method stub
			if(levelNo == 1){
				picIndex++;
				if(picIndex <= 2){
					pictures[picIndex] = new Texture(Gdx.files.internal(imgName+String.valueOf(picIndex)+".png"),true);
					pictures[picIndex].setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
					commentImg.setDrawable(new SpriteDrawable(new Sprite(pictures[picIndex])));
					//System.out.println(commentImg.getX());
				}
				else{
					game.setScreen(new FirstScreen(game, 1));
					dispose();
				}				
			}			
			else { 
				picIndex++;
				if(picIndex < 2){
					pictures[picIndex] = new Texture(Gdx.files.internal(imgName+String.valueOf(picIndex)+".png"),true);
					pictures[picIndex].setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
					commentImg.setDrawable(new SpriteDrawable(new Sprite(pictures[picIndex])));
				}
				else if(levelNo >= 120){ // game is finished
					game.setScreen(new CreditsScreen(game));
					dispose();
				}			
				else {
					game.setScreen(new FirstScreen(game, levelNo));
					dispose();
				}
			}			
						
			return true;
		}
		
		@Override
		public void	enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
			if(MyFirstGame.SOUNDS_ON)
				Assets.buttonSnd.play(MyFirstGame.VOLUME+0.4f);
			event.getListenerActor().addAction(Actions.color(Color.YELLOW));
		}
		@Override
		public void	exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
			event.getListenerActor().addAction(Actions.color(Color.WHITE));			
		}
	};

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
		Assets.CommentMusic.stop();
		for(int i=0; i<pictures.length;i++)
			if(pictures[i] != null){
				pictures[i].dispose();
				pictures[i] = null;				
			}
		stage.clear();
		stage.dispose();
	}
	
	
}
