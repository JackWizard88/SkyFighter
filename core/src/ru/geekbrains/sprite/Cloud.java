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

    public Cloud(Texture texture, Layer layer) {
        super(new TextureRegion(texture));
        if (layer == Layer.FOREGROUND) {
            this.scale = Rnd.nextFloat(0.15f, 0.2f);
            this.velocity = Rnd.nextFloat(0.2f, 0.3f);
            pos.set(Rnd.nextFloat(1f, 2f), getRandomYPos());
        }
        if (layer == Layer.BACKGROUND) {
            this.scale = Rnd.nextFloat(0.1f, 0.12f);
            this.velocity = Rnd.nextFloat(0.025f, 0.075f);
            pos.set(Rnd.nextFloat(0f, 1f), getRandomYPos());
        }

    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(scale);
        this.worldBounds = worldBounds;
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
        return Rnd.nextFloat(-0.3f, 0.5f);
    }
}
