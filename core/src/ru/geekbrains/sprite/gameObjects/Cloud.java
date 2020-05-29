package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Layer;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Cloud extends Sprite {

    private Rect worldBounds;
    private float scale;
    private Vector2 velocity;

    public Cloud(TextureRegion region, Layer layer) {
        super(region);
        if (layer == Layer.FOREGROUND) {
            this.scale = Rnd.nextFloat(0.15f, 0.18f);
            this.velocity = new Vector2();
            this.velocity.x = Rnd.nextFloat(-0.2f, -0.3f);
            pos.set(Rnd.nextFloat(1f, 2f), getRandomYPos());
        }
        if (layer == Layer.BACKGROUND) {
            this.scale = Rnd.nextFloat(0.1f, 0.12f);
            this.velocity = new Vector2();
            this.velocity.x = Rnd.nextFloat(-0.025f, -0.05f);
            pos.set(Rnd.nextFloat(-0.75f, 1f), getRandomYPos());
        }
        if (layer == Layer.MIDDLE) {
            this.scale = Rnd.nextFloat(0.12f, 0.14f);
            this.velocity = new Vector2();
            this.velocity.x = Rnd.nextFloat(-0.04f, -0.075f);
            pos.set(Rnd.nextFloat(-0.5f, 1f), getRandomYPos());
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
        pos.mulAdd(velocity, delta);
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
