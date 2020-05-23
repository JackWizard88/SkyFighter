package ru.geekbrains.screen;

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
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.gameObjects.Background;
import ru.geekbrains.sprite.gameObjects.Cloud;
import ru.geekbrains.sprite.gameObjects.PlayerPlane;

public class GameScreen extends BaseScreen {

    //textures and atlas
    private Texture bg;
    private Texture bg2;
    private TextureAtlas atlas;
    private Background background;
    private Background background2;

    private PlayerPlane player;
    private TextureAtlas.AtlasRegion cloudTextureRegion;

    //sounds
    private Music windSound;
    private Music battleMusic;

    //pools
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;

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
    public GameScreen(TextureAtlas atlas, ScreenController controller) {
        super(controller);
        this.atlas = atlas;
        //POOLS
        explosionPool = new ExplosionPool();
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool();
        //controllers
        SoundController.getSoundController();
        enemyController = new EnemyController(this, worldBounds);
        //sound
        windSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/wind.mp3"));
        windSound.setVolume(0.8f);
        battleMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/battleMusic2.mp3"));
        battleMusic.setVolume(1f);
        //background
        bg = new Texture("textures/skyGrey.png");
        background = new Background(bg);
        bg2 = new Texture("textures/skyBlue.png");
        background2 = new Background(bg2);
        //player
        player = new PlayerPlane(atlas);
        //clouds
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

    public PlayerPlane getPlayer() {
        return player;
    }

    public BulletPool getBulletPool() {
        return bulletPool;
    }

    public EnemyPool getEnemyPool() {
        return enemyPool;
    }

    public ExplosionPool getExplosionPool() {
        return explosionPool;
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
        battleMusic.play();
        battleMusic.setLooping(true);
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        background2.resize(worldBounds);
        for (Cloud cloud: cloudsBackground) {
            cloud.resize(worldBounds);
        }
        for (Cloud cloud: cloudsMiddle) {
            cloud.resize(worldBounds);
        }
        enemyController.resize(worldBounds);
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
        draw();
        checkHP();
    }

    private void update(float delta) {
        bulletPool.updateActiveSprites(delta);
        enemyController.updateActiveSprites(delta);
        explosionPool.updateActiveSprites(delta);
    }

    private void free() {
        bulletPool.freeAllDestroyed();
        enemyController.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
    }

    public void draw() {
        batch.begin();
        background.draw(batch);
        batch.setColor(1,1,1, player.getScore() / (float) 3000);
        background2.draw(batch);
        batch.setColor(1,1,1, 1);
        for (Cloud cloud: cloudsBackground) {
            batch.setColor(0.7f,0.7f,0.7f,1);
            cloud.draw(batch);
            batch.setColor(1,1,1,1);
        }
        for (Cloud cloud: cloudsMiddle) {
            batch.setColor(0.8f,0.8f,0.8f,0.95f);
            cloud.draw(batch);
            batch.setColor(1,1,1,1);
        }
        enemyController.drawActiveSprites(batch);
        player.draw(batch);
        bulletPool.drawActiveSprites(batch);
        explosionPool.drawActiveSprites(batch);
        for (Cloud cloud: cloudsForeground) {
            batch.setColor(1,1,1,0.9f);
            cloud.draw(batch);
            batch.setColor(1,1,1,1);
        }
        player.drawGUI(batch);
        batch.end();
    }

    @Override
    public void hide() {
        super.hide();
        enemyController.hide();
        player.hide();
        windSound.pause();
        battleMusic.pause();
    }

    @Override
    public void dispose() {
        super.dispose();
        enemyController.dispose();
        player.dispose();
        windSound.dispose();
        battleMusic.dispose();
        enemyPool.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
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


    private void checkHP() {
        if (player.getHealth() <= 0) {
            controller.gameOver();
        }
    }
}