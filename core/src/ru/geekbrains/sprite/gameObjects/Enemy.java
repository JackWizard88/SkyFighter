package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Enemy extends Sprite {

    private Rect worldBounds;
    private Vector2 v;
    private int health;

    public Enemy() {
        regions = new TextureRegion[1];
        v = new Vector2();
        this.health = 10;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (isOutside(worldBounds) || health <= 0) {
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
    }

    public void damage() {
        this.health -= 1;
    }
}
