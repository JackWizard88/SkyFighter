package ru.geekbrains.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.controllers.EnemyController;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.base.Layer;
import ru.geekbrains.controllers.SoundController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.gameObjects.Background;
import ru.geekbrains.sprite.gameObjects.Cloud;
import ru.geekbrains.sprite.gameObjects.Player;

public class GameScreen extends BaseScreen {

    private static GameScreen gameScreen;

    //textures and atlas

    private Texture bg;
    private TextureAtlas atlas;
    private Background background;
    private Player player;
    private TextureAtlas.AtlasRegion cloudTextureRegion;
    //sounds

    private Music windSound;
    //pools

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    //controllers

    private EnemyController enemyController;
    //clouds

    private Cloud[] cloudsForeground;
    private Cloud[] cloudsMiddle;
    private Cloud[] cloudsBackground;
    private final int FOREGROUND_CLOUDS_COUNT = 5;
    private final int MIDDLE_CLOUDS_COUNT = 7;
    private final int BACKGROUND_CLOUDS_COUNT = 10;
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private GameScreen(TextureAtlas atlas, ScreenController controller) {
        super(controller);
        this.atlas = atlas;
        SoundController.getSoundController();
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool();
        enemyController = new EnemyController(this, worldBounds);
        bg = new Texture("textures/skyGrey.png");
        windSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/wind.mp3"));
        windSound.setVolume(0.9f);
        background = new Background(bg);
        player = new Player(atlas);

        cloudsForeground = new Cloud[FOREGROUND_CLOUDS_COUNT];
        cloudsMiddle = new Cloud[MIDDLE_CLOUDS_COUNT];
        cloudsBackground = new Cloud[BACKGROUND_CLOUDS_COUNT];
        for (int i = 0; i < FOREGROUND_CLOUDS_COUNT; i++) {
            cloudTextureRegion = getRandomCloudTexture();
            cloudsForeground[i] = new Cloud(cloudTextureRegion, Layer.FOREGROUND);
        }
        for (int i = 0; i < MIDDLE_CLOUDS_COUNT; i++) {
            cloudTextureRegion = getRandomCloudTexture();
            cloudsMiddle[i] = new Cloud(cloudTextureRegion, Layer.MIDDLE);
        }
        for (int i = 0; i < BACKGROUND_CLOUDS_COUNT; i++) {
            cloudTextureRegion = getRandomCloudTexture();
            cloudsBackground[i] = new Cloud(cloudTextureRegion, Layer.BACKGROUND);
        }

    }

    public static GameScreen getGameScreen() {
        return gameScreen;
    }

    public static GameScreen getGameScreen(TextureAtlas atlas, ScreenController controller) {
        gameScreen = new GameScreen(atlas, controller);
        return gameScreen;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public Player getPlayer() {
        return player;
    }

    public BulletPool getBulletPool() {
        return bulletPool;
    }

    public EnemyPool getEnemyPool() {
        return enemyPool;
    }


    private TextureAtlas.AtlasRegion getRandomCloudTexture() {
        String path = "cloud" + (int)((Math.random() * 4) + 1);
        return new TextureAtlas.AtlasRegion(atlas.findRegion(path));
    }

    @Override
    public void show() {
        super.show();
        player.show();
        windSound.play();
        windSound.setLooping(true);
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (Cloud cloud: cloudsBackground) {
            cloud.resize(worldBounds);
        }
        for (Cloud cloud: cloudsMiddle) {
            cloud.resize(worldBounds);
        }
        enemyController.resize();
        player.resize(worldBounds);
        for (Cloud cloud: cloudsForeground) {
            cloud.resize(worldBounds);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        free();
        enemyController.checkEnemies(delta);
        draw();
    }

    private void update(float delta) {
        bulletPool.updateActiveSprites(delta);
        enemyController.updateActiveSprites(delta);
    }

    private void free() {
        bulletPool.freeAllDestroyed();
        enemyController.freeAllDestroyed();
    }

    public void draw() {
        batch.begin();
        background.draw(batch);
        for (Cloud cloud: cloudsBackground) {
            batch.setColor(0.7f,0.7f,0.7f,1);
            cloud.draw(batch);
            batch.setColor(1,1,1,1);
        }
        for (Cloud cloud: cloudsMiddle) {
            batch.setColor(0.8f,0.8f,0.8f,0.97f);
            cloud.draw(batch);
            batch.setColor(1,1,1,1);
        }
        bulletPool.drawActiveSprites(batch);
        enemyController.drawActiveSprites(batch);
        player.draw(batch);
        for (Cloud cloud: cloudsForeground) {
            batch.setColor(1,1,1,0.93f);
            cloud.draw(batch);
            batch.setColor(1,1,1,1);
        }
        batch.end();
    }

    @Override
    public void hide() {
        super.hide();
        enemyController.hide();
        player.hide();
        windSound.pause();
    }

    @Override
    public void dispose() {
        super.dispose();
        enemyController.dispose();
        player.dispose();
        windSound.dispose();
        enemyPool.dispose();
        bulletPool.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        player.keyDown(keycode);
        if (keycode == Input.Keys.ESCAPE) {
            controller.setMenuScreen();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        player.keyUp(keycode);
        return false;
    }
}
