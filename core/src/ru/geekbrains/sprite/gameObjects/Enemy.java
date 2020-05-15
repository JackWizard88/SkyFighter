package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.controllers.SoundController;
import ru.geekbrains.math.Rect;

public class Enemy extends Sprite {

    private Rect worldBounds;
    private Vector2 v;
    private final Vector2 grav = new Vector2(0, -0.0001f);
    private Vector2 grav1;
    private int health;

    private boolean isFalling = false;

    //sounds
    private Sound soundFlying;
    private Sound soundShooting;
    private Sound soundExplosion;

    public Enemy() {
        regions = new TextureRegion[1];
        v = new Vector2();
        grav1 = new Vector2();
        this.health = 5;
        soundFlying = SoundController.getSoundEnemyFlying();
        soundExplosion = SoundController.getSoundEnemyExplosion();
        soundShooting = SoundController.getSoundEnemyShooting();
    }

    @Override
    public void update(float delta) {

        if (isFalling) {
            if (angle < 60f) angle += 0.2f;
            if (grav1.len() < 10f) grav1.add(grav);
            v.add(grav1);
        }

        pos.mulAdd(v, delta);

        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public void resize() {
        soundShooting.resume();
        soundFlying.resume();
        soundExplosion.resume();
    }

    public void hide() {
        soundShooting.pause();
        soundFlying.pause();
        soundExplosion.pause();
    }

    public void dispose() {
        soundExplosion.dispose();
        soundFlying.dispose();
        soundShooting.dispose();
    }

    public void set(
            TextureRegion region,
            Vector2 pos0,
            Vector2 v,
            int health,
            float height,
            Rect worldBounds
    ) {
        this.regions[0] = region;
        this.pos.set(pos0);
        this.v.set(v);
        this.health = health;
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        grav1.setZero();
        isFalling = false;
        angle = 0;
        soundFlying.play(0.5f);
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void damage() {
        this.health -= 1;
        if (health <= 0) {
            isFalling = true;
            soundExplosion.play(1f);
            soundFlying.stop();
        }
    }
}
