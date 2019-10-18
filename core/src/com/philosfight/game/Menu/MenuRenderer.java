package com.philosfight.game.Menu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.philosfight.game.utils.Constants;

public class MenuRenderer implements Disposable {
	private static String TAG = MenuRenderer.class.getName();

	//Disposizione degli oggetti del Menù
	private MenuDisposition menu;

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
		menu = new MenuDisposition(new SpriteBatch(), new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));
		menu.cameraMenu.position.set(0, 0, 0);
		menu.cameraMenu.update();
	}

	/**
	 * Metodo per decretare l'ordine di renderizzazione degli oggetti
	 */
	public void render(){
		menu.render();
	}

	/**
	 *  Metodo di resize di renderer nel caso ci siano delle modifiche alla finestra di gioco
	 */
	public void resize(int width, int height){
		menu.resize(width, height);
	}

	/**
	 * Metodo per deallocare correttamente la memoria alla fine dell'esecuzione
	 */
	@Override
	public void dispose(){ menu.dispose(); }
}
