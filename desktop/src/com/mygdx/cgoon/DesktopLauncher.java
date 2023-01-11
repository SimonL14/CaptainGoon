package com.mygdx.cgoon;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.cgoon.CaptainGoon;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("CAPTAIN GOOOON");
		config.setWindowedMode(1024, 768);
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setResizable(false);
		new Lwjgl3Application(new CaptainGoon(), config);
	}
}
