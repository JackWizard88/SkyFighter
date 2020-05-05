package ru.geekbrains.screen;

import com.badlogic.gdx.graphics.Texture;

import ru.geekbrains.ScreenController;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.StartButton;

public class MenuScreen extends BaseScreen {

    private Texture bg;
    private Texture buttonStart;
    private Background background;
    private StartButton startButton;

    public MenuScreen(ScreenController controller) {
        super(controller);
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/background.jpg");
        buttonStart = new Texture(("textures/buttons/buttonStart.png"));
        background = new Background(bg);
        startButton = new StartButton(buttonStart, controller);
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        startButton.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        startButton.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        buttonStart.dispose();
        super.dispose();
    }

}
