package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.ScreenController;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.ExitButton;
import ru.geekbrains.sprite.StartButton;

public class MenuScreen extends BaseScreen {

    private Texture bg;
    private Texture buttonStart;
    private Texture buttonExit;
    private Background background;
    private StartButton startButton;
    private ExitButton exitButton;

    public MenuScreen(ScreenController controller) {
        super(controller);
        bg = new Texture("textures/backgroundMenu.jpg");
        buttonStart = new Texture(("textures/buttonStart.png"));
        buttonExit = new Texture(("textures/buttonExit.png"));
        background = new Background(bg);
        startButton = new StartButton(buttonStart ,controller);
        exitButton = new ExitButton(buttonExit, controller);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        startButton.resize(worldBounds);
        exitButton.resize(worldBounds);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        startButton.draw(batch);
        exitButton.draw(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        buttonStart.dispose();
        buttonExit.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        startButton.touchDown(touch, pointer, button);
        exitButton.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        startButton.touchUp(touch, pointer, button);
        exitButton.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
}
