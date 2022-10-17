package ru.geekbrains.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.StarFighter;
import ru.geekbrains.screen.GameOverScreen;
import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.screen.MenuScreen;

public class ScreenController {

    private static TextureAtlas atlas;
    private final StarFighter game;
    private static MenuScreen menuScreen;
    private static GameScreen gameScreen;
    private static GameOverScreen gameOverScreen;
    private static ScreenController instance;
    private ScreenController(StarFighter starFighter) {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        Gdx.graphics.setUndecorated(true);
        this.atlas = new TextureAtlas("textures/atlas.atlas");
        this.menuScreen = new MenuScreen(this);
        this.game = starFighter;
        this.setMenuScreen();
    }
    public static ScreenController getInstance() { return instance; }

    public static ScreenController getInstance(StarFighter starFighter) {
        if (instance == null) {
            instance = new ScreenController(starFighter);
        }
        return instance;
    }

    public StarFighter getGame() {
        return game;
    }

    public static TextureAtlas getAtlas() {
        return atlas;
    }

    public static GameScreen getGameScreen() {
        return gameScreen;
    }

    public void newGameScreen() {
        gameScreen = new GameScreen(atlas, this);
        menuScreen.getMenuButtonController().setGameExists(true);
        setGameScreen();
    }

    public void gameOver() {
        setGameOverScreen();
        gameScreen.dispose();
        gameScreen = null;
        System.gc();
        menuScreen.getMenuButtonController().setGameExists(false);
    }

    public void setGameScreen() {
        game.setScreen(gameScreen);
    }

    public void setMenuScreen() {
        game.setScreen(menuScreen);
    }

    public void setGameOverScreen() {
        gameOverScreen = new GameOverScreen(this);
        gameOverScreen.setPlayerScore(gameScreen.getPlayer().getScore());
        gameOverScreen.setPlayerShots(gameScreen.getPlayer().getShots());
        gameOverScreen.setPlayerKills(gameScreen.getPlayer().getKills());
        game.setScreen(gameOverScreen);
    }

}