package ru.geekbrains;

import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.screen.MenuScreen;

public class ScreenController {

    private StarFighter starFighter;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    public void setGameScreen() {
        starFighter.setScreen(gameScreen);
    }

    public void setMenuScreen() {
        starFighter.setScreen(menuScreen);
    }


    public ScreenController(StarFighter starFighter) {
        this.menuScreen = new MenuScreen(this);
        this.gameScreen = new GameScreen(this);
        this.starFighter = starFighter;
        starFighter.setScreen(menuScreen);
    }
}
