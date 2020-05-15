package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Bullet extends Sprite {

    private Rect worldBounds;
    private Vector2 v;
    private Vector2 dir;
    private float velocity;
    private Vector2 grav;
    private Vector2 friction;
    private int damage;
    private Sprite owner;
    private final Vector2 grav1 = new Vector2(0, -0.001f);
    private final Vector2 friction1 = new Vector2(-0.00001f, 0);
    private final Vector2 correction = new Vector2(0, 0.05f);

    public Bullet() {
        regions = new TextureRegion[1];
        v = new Vector2();
        dir = new Vector2();
        grav = new Vector2();
        friction = new Vector2();
    }

    @Override
    public void update(float delta) {
        v.set(dir.scl(velocity));
        grav.add(grav1);
        friction.add(friction1);
        v.add(grav).add(friction);
        pos.mulAdd(v, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public void set(
            Sprite owner,
            TextureRegion region,
            Vector2 pos0,
            float velocity,
            float angle,
            Vector2 dir,
            float height,
            Rect worldBounds,
            int damage
    ) {
        this.owner = owner;
        this.regions[0] = region;
        this.pos.set(pos0);
        this.velocity = velocity;
        this.angle = angle;
        this.dir.set(dir).add(correction);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.damage = damage;
        grav.setZero();
        friction.setZero();
    }

    public int getDamage() {
        return damage;
    }

    public Sprite getOwner() {
        return owner;
    }
}