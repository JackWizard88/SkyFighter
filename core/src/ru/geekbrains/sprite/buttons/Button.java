package ru.geekbrains.sprite.buttons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.math.Rect;

public abstract class Button extends Sprite {

    protected final float MARGIN = 0.025f;
    protected ScreenController controller;
    protected boolean isActivated = false;

    public Button(TextureRegion region, ScreenController controller) {
        super(region);
        this.controller = controller;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight() * 0.08f);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (isMe(touch)) {
            setScale(0.9f);
            isActivated = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        isActivated = false;
        setScale(1f);
        return false;
    }
}
