package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class ExitButton extends Sprite{

    public ExitButton(Texture texture) {
        super(new TextureRegion(texture));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight() * 0.15f);
        this.pos.set(worldBounds.getRight() - halfWidth, worldBounds.getBottom() + halfHeight);
    }
}
