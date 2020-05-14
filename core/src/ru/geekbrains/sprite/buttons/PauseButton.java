package ru.geekbrains.sprite.buttons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.math.Rect;

public class PauseButton extends Button{


    public PauseButton(TextureRegion region, ScreenController controller) {
        super(region, controller);

    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight() * 0.1f);
        this.pos.set(worldBounds.getRight() - halfWidth, worldBounds.getTop() - halfHeight);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (isActivated && isMe(touch)) {
            controller.setMenuScreen();
        }
        return super.touchUp(touch, pointer, button);
    }
}
