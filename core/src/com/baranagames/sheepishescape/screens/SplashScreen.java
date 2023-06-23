package com.baranagames.sheepishescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.baranagames.sheepishescape.Assets;
import com.baranagames.sheepishescape.MyFirstGame;

public class SplashScreen implements Screen {

	private MyFirstGame game;
	private Stage stage;
	private boolean splashFinished;
	private Texture splashTexture;
	
	public SplashScreen(MyFirstGame game) {
		// TODO Auto-generated constructor stub
		this.game = game;
		splashFinished = false;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor( 1f, 1f,1f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
//System.out.println(splashImage.getX()+"   "+splashImage.getY());        
		stage.act(delta);
		stage.draw();
		if(Assets.isLoaded() && splashFinished){			
        	Assets.setAssets();
        	game.setScreen(new MainMenuScreen(game));
        	dispose();
        }
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	//	stage.setViewport(new ExtendViewport(width, height));

	}
	//Image splashImage ;
	@Override
	public void show() {
		// TODO Auto-generated method stub
		//if(Gdx.app.getType() == ApplicationType.Desktop && !Gdx.graphics.isFullscreen())
			//Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);		
		
		splashTexture = new Texture(Gdx.files.internal("splash.png"),true);
		splashTexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		Image splashImage  = new Image(splashTexture);
		splashImage.setOrigin(0, 0);
		float width = Gdx.graphics.getWidth();
		if(width < 2048)
			splashImage.setSize(width/4, width/8);
		splashImage.setPosition(Gdx.graphics.getWidth()/2 - splashImage.getWidth()/2, Gdx.graphics.getHeight()/2 - splashImage.getHeight()/2);

		splashImage.addAction(Actions.sequence(Actions.delay(2f), Actions.run(onSplashFinishedRunnable) ) );		
		stage.addActor(splashImage);
		
	}
	
	Runnable onSplashFinishedRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//game.setScreen(new MainMenuScreen(game));
			splashFinished = true;
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
		stage.clear();
		splashTexture.dispose();
		stage.dispose();
	}

}
