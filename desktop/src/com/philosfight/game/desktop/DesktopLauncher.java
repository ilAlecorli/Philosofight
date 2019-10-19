package com.philosfight.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.philosfight.game.PhilosofightMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		//Rapporto di grandezza schermo (più piccolo è rate, più grande è lo schermo
		float rate = 1.5f;

		//Configurazione larghezza e grandezza schermo di gioco all'avvio
		config.width = ((int) (1920 / rate));
		config.height = ((int) (1080 / rate));
		config.resizable = false;
		new LwjglApplication(new PhilosofightMain(), config);
	}
}
