package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Propeller extends Sprite {

    private Sprite owner;
    private Vector2 shift;
    private Vector2 origin;

    public Propeller(TextureRegion region, int rows, int cols, int frames, Sprite owner) {
        super(region, rows, cols, frames);
        this.owner = owner;
        shift = new Vector2();
        origin = new Vector2();
        pos.set(2f, 2f);
    }

    public void setShift(float shiftX, float shiftY) {
        this.shift.set(shiftX, shiftY);
        origin.set(halfWidth, 0);
        shift.add(origin);
        setHeightProportion(0.05f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        this.angle = owner.getAngle();
        this.pos.set(owner.pos).add(shift);
        frame = (frame + 1) % regions.length;

    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                -shift.x + origin.x, -shift.y + origin.y + halfHeight,
                getWidth(), getHeight(),
                scale, scale,
                angle
        );
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
    }
}
