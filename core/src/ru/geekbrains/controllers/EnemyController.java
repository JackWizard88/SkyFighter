package ru.geekbrains.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.sprite.gameObjects.Bullet;
import ru.geekbrains.sprite.gameObjects.Enemy;

public class EnemyController {

    private GameScreen gameScreen;

    private final int ENEMY_LIMIT = 3;
    private TextureRegion enemyRegion;
    private Rect worldbounds;
    private Vector2 spawnCoordinates;
    private Vector2 velocity;
    private float timerSpawn = 0;

    public EnemyController(GameScreen gameScreen, Rect worldbounds) {
        this.gameScreen = gameScreen;
        this.worldbounds = worldbounds;
        spawnCoordinates = new Vector2();
        enemyRegion = gameScreen.getAtlas().findRegion("plane4f");
        velocity = new Vector2(-0.15f, 0);
    }

    public void checkEnemies(float delta) {
        //проверка попадания
        for (Enemy enemy : gameScreen.getEnemyPool().getActiveObjects()) {
            checkCollisions(enemy);
        }

        //спаунер врагов в случайной координате по таймеру
        timerSpawn += delta;
        if (timerSpawn >= Rnd.nextFloat(1.5f, 3f)) {
            timerSpawn = 0;
            if (gameScreen.getEnemyPool().getSize() < ENEMY_LIMIT) {
                Enemy enemy = gameScreen.getEnemyPool().obtain();
                enemy.set(enemyRegion, getSpawnCoordinates(enemy), velocity, 10, 0.1f, worldbounds);
            }
        }
    }

    private void checkCollisions(Enemy enemy) {
        for (Bullet bullet : gameScreen.getBulletPool().getActiveObjects()) {
            if (enemy.isMe(bullet.pos) && !enemy.isFalling()) {
                enemy.damage();
                bullet.destroy();
            }
        }
    }

    public void resize() {
        for (Enemy enemy : gameScreen.getEnemyPool().getActiveObjects()) {
            enemy.resize(worldbounds);
        }
    }

    public void hide() {
        for (Enemy enemy : gameScreen.getEnemyPool().getActiveObjects()) {
            enemy.hide();
        }
    }

    public void dispose() {
        for (Enemy enemy : gameScreen.getEnemyPool().getActiveObjects()) {
            enemy.dispose();
        }
    }

    private Vector2 getSpawnCoordinates(Enemy enemy) {
        spawnCoordinates.x = worldbounds.getRight();
        spawnCoordinates.y = Rnd.nextFloat(worldbounds.getBottom() + enemy.getHalfHeight(), worldbounds.getTop() - enemy.getHalfHeight());
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
