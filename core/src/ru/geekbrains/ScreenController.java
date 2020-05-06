package ru.geekbrains;

import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.screen.MenuScreen;

public class ScreenController {

    private final StarFighter game;
    private final MenuScreen menuScreen;
    private GameScreen gameScreen;

    public void setGameScreen() {
        if (gameScreen == null) {
            gameScreen = new GameScreen(this);
        }
        game.setScreen(gameScreen);
    }

    public void setMenuScreen() {
        game.setScreen(menuScreen);
    }


    public ScreenController(StarFighter starFighter) {
        this.menuScreen = new MenuScreen(this);
        this.game = starFighter;
        game.setScreen(menuScreen);
    }
}
