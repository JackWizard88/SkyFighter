package ru.geekbrains.sprite;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.ScreenController;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class StartButton extends Sprite implements InputProcessor {

    private final ScreenController controller;

    public StartButton(Texture texture, ScreenController controller) {
        super(new TextureRegion(texture));
        this.controller = controller;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        this.pos.set(worldBounds.pos);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (isMe(touch)) {
            setScale(0.75f);
        }
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (isMe(touch)) {
            setScale(1f);
            controller.setScreen(controller.getGameScreen());
        }
        return super.touchUp(touch, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
