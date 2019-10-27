package com.philosfight.game.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class MenuRenderer implements Disposable {
	private static String TAG = MenuRenderer.class.getName();

	//Telecamera di menù
	private OrthographicCamera camera;
	//Batch per renderizzare
	private SpriteBatch batch;

	//Position
	public Vector2 enterButtonPosition;
	public Vector2 enterButtonSize;

	/**
	 * Inizializzazione del Renderizzatore di Menù
	 */
	public MenuRenderer() {
		init();
	}

	/**
	 * Metodo per l'inizializzazione degli sprite di gioco
	 */
	private void init(){
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera.position.set(0, 0, 0);
		camera.translate(camera.viewportWidth/2,camera.viewportHeight/2);
		enterButtonPosition= new Vector2(camera.viewportWidth/2.7f,camera.viewportHeight/4);
		enterButtonSize = new Vector2(MenuAsset.instance.rectangleButton.rectangleButton.originalWidth,
				MenuAsset.instance.rectangleButton.rectangleButton.originalHeight);
	}

	/**
	 * Metodo per decretare l'ordine di renderizzazione degli oggetti
	 */
	public void render(){
		camera.update();
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		batch.draw(MenuAsset.instance.menuBackground.menuBackground,0,0,camera.viewportWidth,camera.viewportHeight);
		batch.draw(MenuAsset.instance.rectangleButton.rectangleButton, enterButtonPosition.x ,enterButtonPosition.y , enterButtonSize.x,enterButtonSize.y);

		batch.end();
	}

	/**
	 *  Metodo di resize di renderer nel caso ci siano delle modifiche alla finestra di gioco
	 */
	public void resize(int width, int height){
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}

	/**
	 * Metodo per deallocare correttamente la memoria alla fine dell'esecuzione
	 */
	@Override
	public void dispose(){ batch.dispose(); }
}
