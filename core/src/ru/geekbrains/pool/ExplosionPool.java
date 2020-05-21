package ru.geekbrains.pool;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.sprite.gameObjects.Explosion;

public class ExplosionPool extends SpritesPool<Explosion> {

    @Override
    protected Explosion newObject() {
        return new Explosion();
    }
}
