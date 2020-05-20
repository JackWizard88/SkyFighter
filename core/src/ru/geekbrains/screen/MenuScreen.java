package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.controllers.MenuButtonController;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.gameObjects.Background;


public class MenuScreen extends BaseScreen {

    private Texture bg;
    private TextureAtlas atlas;

    private MenuButtonController menuButtonController;
    private Music menuMusic;

    private Background background;
    public MenuScreen(TextureAtlas atlas, ScreenController controller) {
        super(controller);
        this.atlas = atlas;
        menuButtonController = new MenuButtonController(atlas, controller);
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
        menuMusic.pause();
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

}
