package ru.geekbrains.sprite.gameObjects.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class HpGUI extends Sprite {

    private final float MARGIN = 0.01f;

    public HpGUI(TextureAtlas atlas) {
        super(atlas.findRegion("Hp"));
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        pos.set(worldBounds.getLeft() + MARGIN + getHalfHeight(), worldBounds.getTop() - getHalfHeight() - MARGIN);
        super.resize(worldBounds);
    }
}
