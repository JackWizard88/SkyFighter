package ru.geekbrains.sprite.gameObjects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Propeller extends Sprite {

    private Sprite owner;
    private Vector2 shift;
    private int frames;

    public Propeller(TextureRegion region, int rows, int cols, int frames, Sprite owner) {
        super(region, rows, cols, frames);
        this.owner = owner;
        this.frames = frames;
        shift = new Vector2();
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        this.angle = owner.getAngle();
        shift.set(owner.getHalfWidth() + getHalfWidth(), - owner.getHalfHeight() / 3);
        this.pos.set(owner.pos).add(shift);

            frame = (frame + 1) % frames;

    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                -owner.getHalfWidth(), getHalfHeight() + owner.getHalfHeight() / 3,
                getWidth(), getHeight(),
                scale, scale,
                angle
        );
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        super.resize(worldBounds);
    }
}
