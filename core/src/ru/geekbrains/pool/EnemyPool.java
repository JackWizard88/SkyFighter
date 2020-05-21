package ru.geekbrains.pool;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.sprite.gameObjects.EnemyPlane;

public class EnemyPool extends SpritesPool<EnemyPlane> {
    @Override
    protected EnemyPlane newObject() {
        return new EnemyPlane();
    }
}
