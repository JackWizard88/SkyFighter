package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.controllers.BonusController;
import ru.geekbrains.controllers.CloudController;
import ru.geekbrains.controllers.EnemyController;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BonusPool;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.gameObjects.Background;
import ru.geekbrains.sprite.gameObjects.PlayerPlane;

public class GameScreen extends BaseScreen {

    //textures and atlas
    private Texture bg;
    private Texture bg2;
    private Background background;
    private Background background2;

    private PlayerPlane player;

    //sounds
    private Music windSound;

    //pools
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private BonusPool bonusPool;

    //controllers
    private EnemyController enemyController;
    private BonusController bonusController;
    private CloudController cloudController;

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public GameScreen(TextureAtlas atlas, ScreenController controller) {
        super(controller);

        //POOLS
        explosionPool = new ExplosionPool();
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool();
        bonusPool = new BonusPool();
      
        //controllers
        enemyController = new EnemyController(this, worldBounds);
        bonusController = new BonusController(this, worldBounds);
      
        //sound
        windSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/wind.mp3"));
        windSound.setVolume(0.8f);
      
        //background
        bg = new Texture("textures/skyGrey.png");
        background = new Background(bg);
        bg2 = new Texture("textures/skyBlue.png");
        background2 = new Background(bg2);
      
        //player
        player = new PlayerPlane(atlas);
      
        //clouds
        cloudController = new CloudController(10, 7, 5);
      
        //sounds
        windSound.setLooping(true);
        windSound.play();
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

    public BonusPool getBonusPool() {
        return bonusPool;
    }

    @Override
    public void show() {
        super.show();
        player.show();
        windSound.play();
        Controllers.addListener(this);
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        background2.resize(worldBounds);
        cloudController.resize(worldBounds);
        enemyController.resize(worldBounds);
        bonusController.resize(worldBounds);
        player.resize(worldBounds);
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
        bonusController.updateActiveSprites(delta);
    }

    private void free() {
        bulletPool.freeAllDestroyed();
        enemyController.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
        bonusController.freeAllDestroyed();
    }

    public void draw() {
        batch.begin();
        background.draw(batch);
        batch.setColor(1,1,1, player.getScore() / (float) 1000);
        background2.draw(batch);
        batch.setColor(1,1,1, 1);
        cloudController.drawBackgroundClouds(batch);
        cloudController.drawMiddleClouds(batch);
        bonusController.drawActiveSprites(batch);
        enemyController.drawActiveSprites(batch);
        player.draw(batch);
        bulletPool.drawActiveSprites(batch);
        explosionPool.drawActiveSprites(batch);
        cloudController.drawForegroundClouds(batch);
        player.drawGUI(batch);
        batch.end();
    }

    @Override
    public void hide() {
        super.hide();
        enemyController.hide();
        bonusController.hide();
        player.hide();
        windSound.stop();
    }

    @Override
    public void dispose() {
        super.dispose();
        enemyController.dispose();
        player.dispose();
        windSound.dispose();
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
            screenController.setMenuScreen();
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        player.keyUp(keycode);
        return super.keyUp(keycode);
    }

    private void checkHP() {
        if (player.getHealth() <= 0) {
            screenController.gameOver();
        }
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        player.buttonDown(controller, buttonCode);
//        if (buttonCode == XBox360Pad.BUTTON_START) screenController.setMenuScreen();
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        player.buttonUp(controller, buttonCode);
        return false;
    }

}