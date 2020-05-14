package ru.geekbrains.sprite.buttons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.math.Rect;

public class StartButton extends Button {


    public StartButton(TextureRegion region, ScreenController controller) {
        super(region, controller);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.pos.set(worldBounds.getLeft() + halfWidth + MARGIN, worldBounds.getBottom() + getHeight() + getHalfHeight() + 2 * MARGIN);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (isActivated && isMe(touch) ) {
            controller.setGameScreen();
        }
        return super.touchUp(touch, pointer, button);
    }
}
