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
    private TextureRegion enemyRegion;
    private Rect worldBounds;
    private Vector2 spawnCoordinates;
    private Vector2 velocity;
    private float timerSpawn = 0;

    public EnemyController(GameScreen gameScreen, Rect worldBounds) {
        this.gameScreen = gameScreen;
        this.worldBounds = worldBounds;
        spawnCoordinates = new Vector2();
        enemyRegion = gameScreen.getAtlas().findRegion("enemyPlaneBody1");
        velocity = new Vector2();
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
                velocity.set(Rnd.nextFloat(-0.02f, -0.1f), 0);
                enemyPlane.set(enemyRegion, getSpawnCoordinates(enemyPlane), velocity, 7, 0.09f, worldBounds);
            }
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
        spawnCoordinates.x = worldBounds.getRight() + enemyPlane.getWidth();
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
