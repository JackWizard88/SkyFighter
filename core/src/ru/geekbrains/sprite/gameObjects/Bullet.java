package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.utils.Regions;

public class Bullet extends Sprite {

    private static int damage;
    private Rect worldBounds;
    private Vector2 v;
    private Vector2 dir;
    private Vector2 grav;
    private Sprite owner;
    private final Vector2 grav1 = new Vector2(0, -0.00001f);

    public Bullet() {
        v = new Vector2();
        dir = new Vector2();
        grav = new Vector2();
    }


    @Override
    public void update(float delta) {
        grav.add(grav1);
        v.add(grav);
        pos.mulAdd(v, delta);

        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public void set(
            Sprite owner,
            TextureRegion region,
            int rows,
            int cols,
            int frames,
            Vector2 pos0,
            float velocity,
            float angle,
            Vector2 dir,
            float height,
            Rect worldBounds,
            int damage
    ) {
        this.regions = Regions.split(region, rows, cols, frames);
        this.owner = owner;
        this.pos.set(pos0);
        this.angle = angle;
        this.dir.set(dir);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.damage = damage;
        grav.setZero();
        v.set(dir.scl(velocity));

        //цвет трассера в зависимости от владельца
        if (owner == ScreenController.getGameScreen().getPlayer()) {
            frame = 0;
        } else {
            frame = 2;
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
        this.angle = angle;
        this.dir.set(dir);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.damage = damage;
        grav.setZero();
        v.set(dir.scl(velocity));
    }

    public static int getDamage() {
        return damage;
    }

    public Sprite getOwner() {
        return owner;
    }
}
