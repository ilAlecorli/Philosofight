package com.philosfight.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philosfight.game.game.Arena;
import com.philosfight.game.game.Assets;
import com.philosfight.game.game.objects.Player;

public class GameGUI {
	//Utile nelle exeptions per mostrare il nome dell'oggetto
	public static final String TAG = GameGUI.class.getName();

	//La batch con cui disegnare la GUI
	private SpriteBatch batch;
	//La telecamera su cui renderizzare
	private OrthographicCamera cameraGUI;
	//L'arena da cui estrarre i dati da renderizzare
	private Arena arena;

	/**
	 * Costruttore
	 * @param batch	La batch con cui disegnare la GUI
	 * @param cameraGUI La telecamera su cui renderizzare
	 * @param arena L'arena da cui estrarre i dati da renderizzare
	 */
	public GameGUI(SpriteBatch batch, OrthographicCamera cameraGUI, Arena arena) {
		this.batch = batch;
		this.cameraGUI = cameraGUI;
		this.arena = arena;
		Gdx.app.debug(TAG, "GameGUI is initialized" );
	}

	public void renderGui() {

		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		//Disegna i nomi dei players
		renderNamePlayers();
		//Disegna la vita dei players
		renderHealth();
		//Disegna il mana dei players
		renderMana();
		//Draw FPS text
		renderGuiFpsCounter();
		batch.end();
	}

	/**
	 * Renderizzazione nomi giocatori
	 * -    Lunghezza lettera singola in font Arial: Big: 14 pxl
	 * -    Altezza Parola in font Arial: Big: 25 pxl
	 */
	private void renderNamePlayers () {
		//Nomi
		String name1 = arena.player1.getNamePlayer();
		String name2 = arena.player2.getNamePlayer();
		//Lunghezza nomi
		int lung1 = arena.player1.getNamePlayer().length();
		int lung2 = arena.player2.getNamePlayer().length();

		//Player1:
		BitmapFont nameFont = Assets.instance.fonts.defaultBig;
		//Scritta con coordinate settate nord-ovest
		nameFont.draw(batch, name1, lung1 - 10, 1);
		nameFont.setColor(Color.CORAL);

		//Player2:
		nameFont = Assets.instance.fonts.defaultBig;
		//Scritta con coordinate settate sud-est
		nameFont.draw(batch, name2, cameraGUI.viewportWidth - lung2*14, cameraGUI.viewportHeight - 52); //larghezza in pixel font * numero lettere parola
		nameFont.setColor(Color.CORAL);
	}

	/**
	 * Metodo per renderizzare l'healthbar dei giocatori
	 */
	private void  renderHealth(){
		//Inserimento delle vite dei Player giocanti
		float healthP1 = arena.player1.getHealthPlayer();
		float healthP2 = arena.player2.getHealthPlayer();
		//Vita iniziale Players
		float intialHealthP1 = Player.healthMax;
		float intialHealthP2 = Player.healthMax;
		//Dimensioni barra da visualizzare
		float heightBar = 10;
		float widthBar = 4.5f;
		//Disegna la vita del player 1
		if (healthP1 >= intialHealthP1 *2/3) {
			//Vita iniziale a più 2/3
			batch.setColor(0, 1, 0, 1);
		} else if (healthP1 >= intialHealthP1/3) {
			//Vita iniziale a più 1/3
			batch.setColor(1, 1, 0, 1);
		} else {
			//Vita meno di un 1/3
			batch.setColor(1, 0, 0, 1);
		}
		//In alto a sinistra
		batch.draw(Assets.instance.blank.blank, 0,4 + 25,widthBar * healthP1,1 * heightBar );
		batch.setColor(Color.WHITE);
		//Disegna la vita del player 2
		if (healthP2 >= intialHealthP2 * 2/3) {
			//Vita iniziale a più 2/3
			batch.setColor(0, 1, 0, 1);
		} else if (healthP2 >= intialHealthP2 /3) {
			//Vita iniziale a più 1/3
			batch.setColor(1, 1, 0, 1);
		} else {
			//Vita meno di un 1/3
			batch.setColor(1, 0, 0, 1);
		}
		//In basso a destra
		batch.draw(Assets.instance.blank.blank, 1 * cameraGUI.viewportWidth - widthBar * healthP2 ,1 * cameraGUI.viewportHeight - heightBar - 4 - 10,
				widthBar * healthP2,1*heightBar );
		batch.setColor(Color.WHITE);
	}


	/**
	 * Metodo per renderizzare la barra del Mana dei giocatori
	 */
	private void  renderMana(){
		//Inserimento delle vite dei Player giocanti
		float manaP1 = arena.player1.shooting.getMana();
		float manaP2 = arena.player2.shooting.getMana();
		//Dimensioni barra da visualizzare
		float heightBar = 10;
		float widthBar = 12;

		//Disegna la vita del player 1
		batch.setColor(Color.ROYAL);
		//In alto a sinistra
		batch.draw(Assets.instance.blank.blank, 0,4 + 35,widthBar * manaP1,1 * heightBar );
		batch.setColor(Color.WHITE);

		//Disegna la vita del player 2
		batch.setColor(Color.ROYAL);
		//In basso a destra
		batch.draw(Assets.instance.blank.blank, 1 * cameraGUI.viewportWidth - widthBar * manaP2 ,1 * cameraGUI.viewportHeight - heightBar - 4,
				widthBar * manaP2,1*heightBar );
		batch.setColor(Color.WHITE);
	}

	/**
	 * Renderizzazione FPS del gioco
	 * -    Lunghezza lettera singola in font Arial: 9 pxl
	 * -    Altezza Parola in font Arial: Normal: 15 pxl
	 */
	private void renderGuiFpsCounter () {
		//Coordinate schermo: nord-est;
		float x = cameraGUI.viewportWidth - 9 * 6;           //larghezza in pixel font * numero lettere parola
		float y = 0;
		int fps = com.badlogic.gdx.Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		//Colorazioni differenti per velocità
		if (fps >= 45) {
			// 45 or more FPS show up in green
			fpsFont.setColor(0, 1, 0, 1);
		} else if (fps >= 30) {
			// 30 or more FPS show up in yellow
			fpsFont.setColor(1, 1, 0, 1);
		} else {
			// less than 30 FPS show up in red
			fpsFont.setColor(1, 0, 0, 1);
		}
		//Scritta con coordinate settate
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1); // white
	}




}
