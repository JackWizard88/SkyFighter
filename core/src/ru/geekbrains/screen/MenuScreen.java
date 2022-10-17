package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.controllers.MenuButtonController;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.gameObjects.Background;


public class MenuScreen extends BaseScreen {

    private Texture bg;

    private MenuButtonController menuButtonController;
    private Music menuMusic;

    private Background background;

    public MenuScreen(ScreenController controller) {
        super(controller);
        menuButtonController = new MenuButtonController(controller);
        bg = new Texture("textures/backgroundMenu2.jpg");
        background = new Background(bg);
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menuMusic.mp3"));
    }

    public MenuButtonController getMenuButtonController() {
        return menuButtonController;
    }

    @Override
    public void show() {
        super.show();
        menuMusic.play();
        menuMusic.setLooping(true);
        Controllers.addListener(this);
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        menuButtonController.resize(worldBounds);
        menuMusic.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        menuButtonController.draw(batch);
        batch.end();
    }

    @Override
    public void hide() {
        super.hide();
        menuMusic.stop();
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        menuMusic.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        menuButtonController.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        menuButtonController.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {

//        if (buttonCode == XBox360Pad.BUTTON_A) {
//            screenController.newGameScreen();
//        }
//        if (buttonCode == XBox360Pad.BUTTON_Y) System.exit(1);

        return false;
    }
}
