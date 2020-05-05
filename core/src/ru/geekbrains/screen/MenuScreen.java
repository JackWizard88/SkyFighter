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
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/backgroundMenu.jpg");
        buttonStart = new Texture(("textures/buttons/buttonStart.png"));
        buttonExit = new Texture(("textures/buttons/buttonExit.png"));
        background = new Background(bg);
        startButton = new StartButton(buttonStart);
        exitButton = new ExitButton(buttonExit);
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
        if (startButton.isMe(touch)) {
            startButton.setScale(0.8f);
        }

        if (exitButton.isMe(touch)) {
            exitButton.setScale(0.8f);
        }
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (startButton.isMe(touch)) {
            controller.setGameScreen();
        }
        if (exitButton.isMe(touch)) {
           Gdx.app.exit();
        }
        startButton.setScale(1f);
        exitButton.setScale(1f);
        return super.touchUp(touch, pointer, button);
    }
}
