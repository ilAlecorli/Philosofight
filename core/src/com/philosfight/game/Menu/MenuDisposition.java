package com.philosfight.game.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuDisposition {
	//Utile nelle exeptions per mostrare il nome dell'oggetto
	public static final String TAG = MenuDisposition.class.getName();

	//La batch con cui disegnare il Menù
	private SpriteBatch batch;
	//In quadratura del Menù
	protected OrthographicCamera cameraMenu;

	/**
	 * Costruttore
	 * @param batch	La batch con cui disegnare il menù
	 * @param cameraMenu La telecamera su cui renderizzare
	 */
	public MenuDisposition(SpriteBatch batch, OrthographicCamera cameraMenu) {
		this.batch = batch;
		this.cameraMenu = cameraMenu;
		Gdx.app.debug(TAG, "Menù is initialized" );
	}

	public void render() {

		batch.setProjectionMatrix(cameraMenu.combined);
		batch.begin();
		batch.draw(MenuAsset.instance.rectangleButton.rectangleButton, 0 ,0, 371, 136);
		batch.end();
	}

	public void resize(int width, int height){
		cameraMenu.viewportWidth = width;
		cameraMenu.viewportHeight = height;
	}

	public void dispose(){
		batch.dispose();
	}


}
