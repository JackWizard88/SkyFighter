package ru.geebrains.starfighter;

import com.badlogic.gdx.Game;
import ru.geebrains.starfighter.screen.MenuScreen;

public class StarFighter extends Game {

	@Override
	public void create() {
		setScreen(new MenuScreen());
	}
}
