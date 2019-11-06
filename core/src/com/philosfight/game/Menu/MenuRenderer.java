package com.philosfight.game.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

	//Tempo di ciclo del luccichio
	private float blinkingTime;
	//Flag colore luccichio
	private boolean blinkingColorFlag;
	//Timer di intervallo del blinking
	private final float TIMER_BLINKING = 30;

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

		//Inizializzare gli attributi per il ciclo
		blinkingTime = 0;
		blinkingColorFlag = true;
	}

	/**
	 * Metodo per decretare l'ordine di renderizzazione degli oggetti
	 */
	public void render(){
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(MenuAsset.instance.menuBackground.menuBackground,0,0,camera.viewportWidth,camera.viewportHeight);
		renderButton();
		renderTitle();
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

	/**
	 * Renderizzazione Bottone
	 */
	private void renderButton(){
		batch.draw(MenuAsset.instance.rectangleButton.rectangleButton, enterButtonPosition.x, enterButtonPosition.y, enterButtonSize.x, enterButtonSize.y);
		renderButtonText();
	}

	/**
	 * Renderizzazione testo bottone
	 */
	private void renderButtonText() {
		//Aggiorna il counter del ciclo di Blinking
		blinkingTime += 1;

		//Inizializza l'asset del Font della scritta
		BitmapFont nameFont = new BitmapFont(
				Gdx.files.internal("Images/arial-15.fnt"), false);
		//Smussalo per renderlo più bello
		nameFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		if (blinkingColorFlag){
			nameFont.setColor(Color.GOLDENROD);
		}

		if (blinkingTime > TIMER_BLINKING) {
			blinkingTime = 0;
			if (blinkingColorFlag == false){
				//Torna a oro
				blinkingColorFlag = true;
			} else {
				//Vai a bianco
				blinkingColorFlag = false;
				//Colore
				nameFont.setColor(Color.WHITE);
			}
		}

		//Grandezza X x Y:
		nameFont.getData().setScale(2.5f);
		//Scritta con coordinate settate ovest
		nameFont.draw(batch,"Press Enter to play" , Gdx.graphics.getWidth()/2 - 130, Gdx.graphics.getHeight()/2 - 95);

	}
	private void renderTitle() {

		//Player1:
		BitmapFont nameFont = new BitmapFont(
				Gdx.files.internal("Images/arial-15.fnt"), false);
		//Colore
		nameFont.setColor(Color.CORAL);
		//Grandezza X x Y:
		nameFont.getData().setScale(11f);
		//Scritta con coordinate settate ovest
		nameFont.draw(batch,"Philosofight" , Gdx.graphics.getWidth()/2 -350, Gdx.graphics.getHeight()/2 + 200);

	}
}



