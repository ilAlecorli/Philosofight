package com.philosfight.game.Menu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.philosfight.game.utils.Constants;

public class MenuRenderer implements Disposable {
	private static String TAG = MenuRenderer.class.getName();

	//Telecamera di menù
	private OrthographicCamera camera;
	//Batch per renderizzare
	private SpriteBatch batch;


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
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
	}

	/**
	 * Metodo per decretare l'ordine di renderizzazione degli oggetti
	 */
	public void render(){

		batch.begin();
		batch.draw(MenuAsset.instance.menuBackground.menuBackground, Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
		batch.draw(MenuAsset.instance.rectangleButton.rectangleButton, 0, 0);
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
