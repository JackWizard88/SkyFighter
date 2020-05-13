package ru.geekbrains.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.ScreenController;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.base.Layer;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Cloud;
import ru.geekbrains.sprite.PauseButton;
import ru.geekbrains.sprite.Player;

public class GameScreen extends BaseScreen {

    private Texture bg;
    private Texture buttonPause;
    private Texture playerTexture;
    private Background background;
    private Player player;
    private PauseButton pauseButton;
    private Texture cloudTexture;
    private Cloud[] cloudsForeground;
    private Cloud[] cloudsBackground;
    private final int FOREGROUND_CLOUDS_COUNT = 7;
    private final int BACKGROUND_CLOUDS_COUNT = 7;

    public GameScreen(ScreenController controller) {
        super(controller);
        bg = new Texture("textures/sky.png");
        playerTexture = new Texture("textures/plane1.png");
        buttonPause = new Texture("textures/buttonPause.png");
        cloudTexture = new Texture("textures/cloud1.png");
        background = new Background(bg);
        player = new Player(playerTexture);
        pauseButton = new PauseButton(buttonPause, controller);
        cloudsForeground = new Cloud[FOREGROUND_CLOUDS_COUNT];
        cloudsBackground = new Cloud[BACKGROUND_CLOUDS_COUNT];
        for (int i = 0; i < FOREGROUND_CLOUDS_COUNT; i++) {
            cloudsForeground[i] = new Cloud(cloudTexture, worldBounds, Layer.FOREGROUND);
        }
        for (int i = 0; i < BACKGROUND_CLOUDS_COUNT; i++) {
            cloudsBackground[i] = new Cloud(cloudTexture, worldBounds, Layer.BACKGROUND);
        }

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (Cloud cloud: cloudsBackground) {
            cloud.resize(worldBounds);
        }
        player.resize(worldBounds);
        for (Cloud cloud: cloudsForeground) {
            cloud.resize(worldBounds);
        }
        pauseButton.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        for (Cloud cloud: cloudsBackground) {
            cloud.draw(batch);
        }
        player.draw(batch);
        for (Cloud cloud: cloudsForeground) {
            cloud.draw(batch);
        }
        pauseButton.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        buttonPause.dispose();
        playerTexture.dispose();
        cloudTexture.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        pauseButton.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        pauseButton.touchUp(touch, pointer, button);
        if (!pauseButton.isMe(touch)) {
            player.touchUp(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        player.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        player.keyUp(keycode);
        return false;
    }
}
