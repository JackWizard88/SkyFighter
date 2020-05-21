package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.utils.Regions;

public class Explosion extends Sprite {

    private Sprite owner;
    private float timer = 0;

    public Explosion() {
    }

    public void set(
            Sprite owner,
            TextureRegion region,
            int rows,
            int cols,
            int frames,
            float height

    ) {
        this.regions = Regions.split(region, rows, cols, frames);
        this.owner = owner;
        frame = 0;
        setHeightProportion(height);
    }

    @Override
    public void update(float delta) {

        timer += delta;

        pos.set(owner.pos.x, owner.pos.y + owner.getHalfHeight());

        if (timer >= 0.1f) {
            if (frame < regions.length - 1) {
                frame = (frame + 1);
            }
            timer = 0;
        }

        if (owner.isDestroyed()) {
            destroy();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                getHalfWidth(), getHalfHeight(),
                getWidth(), getHeight(),
                scale, scale,
                angle
        );
    }
}
