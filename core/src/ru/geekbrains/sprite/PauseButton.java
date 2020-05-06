package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class PauseButton extends Sprite{

    public PauseButton(Texture texture) {
        super(new TextureRegion(texture));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight() * 0.1f);
        this.pos.set(worldBounds.getRight() - halfWidth, worldBounds.getTop() - halfHeight);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        setScale(0.8f);
        return false;
    }
}
