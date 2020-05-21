package ru.geekbrains.sprite.gameObjects.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class HpPlaneGUI extends Sprite {

    private final float MARGIN = 0.01f;
    private Rect worldBounds;


    public HpPlaneGUI(TextureAtlas atlas) {
        super(atlas.findRegion("HpPlane"));
    }

    public void draw(SpriteBatch batch, int i) {
        super.draw(batch);
        pos.set(worldBounds.getLeft() + 0.1f + (MARGIN + getWidth()) * i, worldBounds.getTop() - MARGIN - getHalfHeight());
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        this.worldBounds = worldBounds;
        super.resize(worldBounds);
    }
}
