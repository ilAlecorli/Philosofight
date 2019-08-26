package com.philosfight.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.philosfight.game.game.Assets;
import com.philosfight.game.game.WorldController;
import com.philosfight.game.game.WorldRenderer;

public class PhilosofightMain implements ApplicationListener {

	//TAG necessario per i log dell'applicazione
	private static final String TAG = PhilosofightMain.class.getName();
	//Variabile utilizzata per tenere traccia delle pause
	private boolean paused;
	//Oggetti per la gestione del mondo di gioco
	private WorldController worldController;
	private WorldRenderer worldRenderer;

	public PhilosofightMain(){}
/**
 * La fase di create() serve per inizializzare  l'applicazione,
 * ad esempio caricando gli assets di gioco e generando
 * lo stato iniziale  del game world.
 */
	@Override
	public void create () {
//		Imposta i log Arena su DEBUG in modo da avere dei feedback durante l'esecuzione
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
//		Carica gli assets
		Assets.instance.init(new AssetManager());
//		Inizializza il worldController ed il worldRenderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);

		//All'inizio il game world è attivo
		paused = false;
	}

	/**
 *	La fase di render ha due compiti fondamentali:
 *	1) Aggiornare il modello del mondo (world controller),
 *	2) Disegnare le scene con gli oggetti già aggiornati (world renderer).
 */
	@Override
	public void render () {
		if(!paused) {
			//Aggiorno il world controller
			worldController.update(Gdx.graphics.getDeltaTime());

			//Imposto ed eseguo il clear screen: Grigio scuro
			Gdx.gl.glClearColor(99, 99, 99, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			//Renderizzo il game world con il world renderer
			worldRenderer.render();
		}
	}

	/**
	 * La fase di resize() si occupa di riadattare il gioco
	 * 	ad un eventuale cambio di dimensioni della finestra.
	 */
		@Override
	public void resize(int width, int height){
		worldRenderer.resize();
	}

	/**
	 * La fase di pause() si attiva quando
	 * un evento esterno interrompe l'applicazione(per esempio una chiamata)
	 * Questa fase è importante per il salvataggio dei dati che,
	 * altrimenti, andrebbero persi.
	 */
	@Override
	public void pause(){
		paused = true;
	}

/**	La fase di resume() segue la fase di pause()
 *	se l'applicazione non viene terminata e fa riprendere all'utente
 *	il gioco da dove lo aveva lasciato.
 */
	@Override
	public void resume(){
		paused = false;
	}

/**	La fase di dispose si attiva quando l'applicazione viene terminata.
 *	Questo stadio si occupa di liberare nel modo corretto
 *	la memoria allocata e di disimpegnare le risorse utilizzate.
 */
	@Override
	public void dispose () {
		worldRenderer.dispose();
		Assets.instance.dispose();
	}


}
