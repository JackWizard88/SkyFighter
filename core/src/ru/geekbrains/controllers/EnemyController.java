package ru.geekbrains.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.sprite.gameObjects.Bullet;
import ru.geekbrains.sprite.gameObjects.EnemyPlane;

public class EnemyController {

    private GameScreen gameScreen;

    private final int ENEMY_LIMIT = 3;
    private TextureRegion enemyRegion1;
    private TextureRegion enemyRegion2;
    private TextureRegion enemyRegion3;
    private Rect worldBounds;
    private Vector2 spawnCoordinates;
    private Vector2 velocity;
    private float timerSpawn = 0;

    public EnemyController(GameScreen gameScreen, Rect worldBounds) {
        this.gameScreen = gameScreen;
        this.worldBounds = worldBounds;
        spawnCoordinates = new Vector2();
        enemyRegion1 = gameScreen.getAtlas().findRegion("plane4f");
        enemyRegion2 = gameScreen.getAtlas().findRegion("plane5f");
        enemyRegion3 = gameScreen.getAtlas().findRegion("plane6f");
        velocity = new Vector2(-0.2f, 0);
    }

    public void checkEnemies(float delta) {
        //проверка попадания
        for (EnemyPlane enemyPlane : gameScreen.getEnemyPool().getActiveObjects()) {
            checkCollisions(enemyPlane);
        }

        //спаунер врагов в случайной координате по таймеру
        timerSpawn += delta;
        if (timerSpawn >= Rnd.nextFloat(2f, 4f)) {
            timerSpawn = 0;
            if (gameScreen.getEnemyPool().getSize() < ENEMY_LIMIT) {
                EnemyPlane enemyPlane = gameScreen.getEnemyPool().obtain();
                enemyPlane.set(getRandomEnemyRegion(), getSpawnCoordinates(enemyPlane), velocity, 7, 0.066f, worldBounds);
            }
        }
    }

    private TextureRegion getRandomEnemyRegion() {
        switch ((int) (Math.random() * 3)) {
            case 0 :
                return enemyRegion1;

            case 1 :
                return enemyRegion2;

            case 2 :
                return enemyRegion3;

            default:
                return enemyRegion1;

        }
    }

    private void checkCollisions(EnemyPlane enemyPlane) {
        for (Bullet bullet : gameScreen.getBulletPool().getActiveObjects()) {
            if (enemyPlane.isMe(bullet.pos) && !enemyPlane.isFalling() && bullet.getOwner() != enemyPlane) {
                enemyPlane.damage();
                SoundController.getSoundHit().play();
                bullet.destroy();
            }
        }
    }

    public void resize() {
        for (EnemyPlane enemyPlane : gameScreen.getEnemyPool().getActiveObjects()) {
            enemyPlane.resize(worldBounds);
        }
    }

    public void hide() {
        for (EnemyPlane enemyPlane : gameScreen.getEnemyPool().getActiveObjects()) {
            enemyPlane.hide();
        }
    }

    public void dispose() {
        for (EnemyPlane enemyPlane : gameScreen.getEnemyPool().getActiveObjects()) {
            enemyPlane.dispose();
        }
    }

    private Vector2 getSpawnCoordinates(EnemyPlane enemyPlane) {
        spawnCoordinates.x = worldBounds.getRight() + 2 * enemyPlane.getWidth();
        spawnCoordinates.y = Rnd.nextFloat(worldBounds.getBottom() + enemyPlane.getHalfHeight(), worldBounds.getTop() - enemyPlane.getHalfHeight());
        return spawnCoordinates;
    }

    public void updateActiveSprites(float delta) {
        gameScreen.getEnemyPool().updateActiveSprites(delta);
    }

    public void freeAllDestroyed() {
        gameScreen.getEnemyPool().freeAllDestroyed();
    }

    public void drawActiveSprites(SpriteBatch batch) {
        gameScreen.getEnemyPool().drawActiveSprites(batch);
    }
}
