package ru.geekbrains;

import com.badlogic.gdx.Game;

import ru.geekbrains.controllers.ScreenController;


public class StarFighter extends Game {

	@Override
	public void create() {
		ScreenController.getInstance(this);
	}
}
