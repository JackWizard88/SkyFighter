package ru.geekbrains.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.StarFighter;
import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.screen.MenuScreen;

public class ScreenController {

    private TextureAtlas atlas;
    private final StarFighter game;
    private final MenuScreen menuScreen;
    private GameScreen gameScreen;

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setGameScreen() {
        if (gameScreen == null) {
            gameScreen = new GameScreen(atlas, this);
        }
        game.setScreen(gameScreen);
    }

    public void setMenuScreen() {
        game.setScreen(menuScreen);
    }


    public ScreenController(StarFighter starFighter) {
        this.atlas = new TextureAtlas(Gdx.files.internal("textures/atlas.atlas"));
        this.menuScreen = new MenuScreen(atlas, this);
        this.game = starFighter;
        game.setScreen(menuScreen);
    }
}
