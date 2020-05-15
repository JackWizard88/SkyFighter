package ru.geekbrains.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.gameObjects.Bullet;
import ru.geekbrains.sprite.gameObjects.Enemy;

public class EnemyController {

    private EnemyPool enemyPool;
    private final int ENEMY_LIMIT = 3;
    private TextureRegion enemyRegion;
    private Rect worldbounds;
    private Vector2 spawnCoordinates;
    private Vector2 velocity;
    private float timerSpawn = 0;

    public EnemyController(TextureAtlas atlas, EnemyPool enemyPool, Rect worldbounds) {
        this.worldbounds = worldbounds;
        this.enemyPool = enemyPool;
        spawnCoordinates = new Vector2();
        enemyRegion = atlas.findRegion("plane4f");
        velocity = new Vector2(-0.15f, 0);
    }

    public void checkEnemies(float delta, BulletPool bulletPool) {
        for (Enemy enemy : enemyPool.getActiveObjects()) {
            checkCollisions(bulletPool, enemy);
        }
        timerSpawn += delta;
        if (timerSpawn >= Rnd.nextFloat(1.5f, 3f)) {
            timerSpawn = 0;
            if (enemyPool.getSize() < ENEMY_LIMIT) {
                Enemy enemy = enemyPool.obtain();
                enemy.set(enemyRegion, getSpawnCoordinates(enemy), velocity, 10, 0.1f, worldbounds);
            }
        }
    }

    private void checkCollisions(BulletPool bulletPool, Enemy enemy) {
        for (Bullet bullet : bulletPool.getActiveObjects()) {
            if (enemy.isMe(bullet.pos)) {
                enemy.damage();
                bullet.destroy();
            }
        }
    }

    private Vector2 getSpawnCoordinates(Enemy enemy) {
        spawnCoordinates.x = worldbounds.getRight();
        spawnCoordinates.y = Rnd.nextFloat(worldbounds.getBottom() + enemy.getHalfHeight(), worldbounds.getTop() - enemy.getHalfHeight());
        return spawnCoordinates;
    }

    public void updateActiveSprites(float delta) {
        enemyPool.updateActiveSprites(delta);
    }

    public void freeAllDestroyed() {
        enemyPool.freeAllDestroyed();
    }

    public void drawActiveSprites(SpriteBatch batch) {
        enemyPool.drawActiveSprites(batch);
    }
}
