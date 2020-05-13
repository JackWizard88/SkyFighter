package ru.geekbrains;

import com.badlogic.gdx.Game;


public class StarFighter extends Game {

	@Override
	public void create() {
		new ScreenController(this);
	}
}
