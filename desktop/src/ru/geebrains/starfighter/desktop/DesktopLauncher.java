package ru.geebrains.starfighter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.geekbrains.StarFighter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new StarFighter(), config);
		config.height = 800;
		config.width = 1280;
		config.foregroundFPS = 100;
		config.vSyncEnabled = false;
//		config.resizable = false;
	}
}
