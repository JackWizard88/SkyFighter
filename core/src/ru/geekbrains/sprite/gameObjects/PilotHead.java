package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class PilotHead extends Sprite {

    private float animateTimer = 0;
    private Sprite owner;
    private Vector2 pilotPos = new Vector2();

    public PilotHead(TextureRegion region, int rows, int cols, int frames, Sprite owner) {
        super(region, rows, cols, frames);
        this.owner = owner;
        pos.set(2f, 2f);
    }

    public void setPilotPos(float pilotPosX, float pilotPosY) {
        this.pilotPos.set(pilotPosX, pilotPosY);
        setHeightProportion(0.02f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.set(owner.pos).add(pilotPos);
        pos.add(getHalfWidth(), getHalfHeight());
        this.angle = owner.getAngle();

        animateTimer += delta;
        if (animateTimer >= 0.05f) {
            animateTimer = 0;
            frame = (frame + 1) % regions.length;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                -pilotPos.x, -pilotPos.y,
                getWidth(), getHeight(),
                scale, scale,
                angle
        );
    }

    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
    }
}
