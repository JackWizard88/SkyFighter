package ru.geekbrains.screen;

import com.badlogic.gdx.graphics.Texture;

import ru.geekbrains.ScreenController;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;

public class GameScreen extends BaseScreen {

    private Texture bg;
    private Background background;


    public GameScreen(ScreenController controller) {
        super(controller);
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/backgroundGame.jpg");

        background = new Background(bg);
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        super.dispose();
    }

}
