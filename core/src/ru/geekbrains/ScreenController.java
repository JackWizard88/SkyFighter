package ru.geekbrains;

import com.badlogic.gdx.Screen;

import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.screen.MenuScreen;

public class ScreenController {

    private MenuScreen menuScreen;

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    private GameScreen gameScreen;
    private StarFighter starFighter;

    private Screen screen;

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public ScreenController(StarFighter starFighter) {
        this.menuScreen = new MenuScreen(this);
        this.gameScreen = new GameScreen(this);
        this.starFighter = starFighter;
        this.screen = menuScreen;
        starFighter.setScreen(menuScreen);
    }
}
