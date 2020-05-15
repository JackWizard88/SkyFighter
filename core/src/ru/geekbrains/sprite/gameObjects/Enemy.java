package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Enemy extends Sprite {

    private Rect worldBounds;
    private Vector2 v;
    private final Vector2 grav = new Vector2(0, -0.0001f);
    private Vector2 grav1;
    private int health;
    private boolean isFalling = false;

    public Enemy() {
        regions = new TextureRegion[1];
        v = new Vector2();
        grav1 = new Vector2();
        this.health = 10;
    }

    @Override
    public void update(float delta) {

        if (isFalling) {
            if (angle < 60f) angle += 0.2f;
            grav1.add(grav);
            v.add(grav1);
        }

        pos.mulAdd(v, delta);

        if (isOutside(worldBounds)) {
            destroy();
        }
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
    }

    public void damage() {
        this.health -= 1;
        if (health <= 0) isFalling = true;
    }
}
