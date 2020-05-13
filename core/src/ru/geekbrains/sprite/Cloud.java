package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.base.Layer;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Cloud extends Sprite {

    private Rect worldBounds;
    private float scale;
    private float velocity;

    public Cloud(Texture texture, Rect worldBounds, Layer layer) {
        super(new TextureRegion(texture));
        this.worldBounds = worldBounds;
        if (layer == Layer.FOREGROUND) {
            this.scale = Rnd.nextFloat(0.15f, 0.2f);
            this.velocity = Rnd.nextFloat(0.2f, 0.3f);
        }
        if (layer == Layer.BACKGROUND) {
            this.scale = Rnd.nextFloat(0.1f, 0.15f);
            this.velocity = Rnd.nextFloat(0.1f, 0.2f);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(scale);
        this.worldBounds = worldBounds;
        pos.set(Rnd.nextFloat(worldBounds.getRight(), worldBounds.getRight() + worldBounds.getHalfHeight()), getRandomYPos());
    }

    @Override
    public void draw(SpriteBatch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    @Override
    public void update(float delta) {
        pos.x -= velocity * delta;
        checkBounds();
    }

    private void checkBounds() {
        if (pos.x < worldBounds.getLeft() - getHalfWidth()) {
            pos.x = worldBounds.getRight() + getHalfWidth();
            pos.y = getRandomYPos();
        }
    }

    private float getRandomYPos() {
        return Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
    }
}
