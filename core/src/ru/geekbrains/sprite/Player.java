package ru.geekbrains.sprite;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import javax.swing.SpringLayout;

public class Player extends Sprite {

    private Rect worldBounds;

    private boolean isKeyUpPressed = false;
    private boolean isKeyDownPressed = false;
    private boolean isKeyLeftPressed = false;
    private boolean isKeyRightPressed = false;

    private Vector2 shipSpeed;

    private final float SHIP_MAXSPEED = 1f;
    private final float SHIP_SPEED_STEP = 0.05f;
    private final float SHIP_BREAK = 0.01f;

    public Player(Texture texture) {
        super(new TextureRegion(texture));
        shipSpeed = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        this.worldBounds = worldBounds;
    }

    @Override
    public void draw(SpriteBatch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    @Override
    public void update(float delta) {
        shipControl(delta);
        checkBounds();
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == 19) {
            isKeyUpPressed = true;
        }
        if (keycode == 20) {
            isKeyDownPressed = true;
        }
        if (keycode == 21) {
            isKeyLeftPressed = true;
        }
        if (keycode == 22) {
            isKeyRightPressed = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == 19) {
            isKeyUpPressed = false;
        }
        if (keycode == 20) {
            isKeyDownPressed = false;
        }
        if (keycode == 21) {
            isKeyLeftPressed = false;
        }
        if (keycode == 22) {
            isKeyRightPressed = false;
        }

        return false;
    }


    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    private void shipControl(float delta) {

        if (isKeyUpPressed && shipSpeed.y < SHIP_MAXSPEED) {
            shipSpeed.y += SHIP_SPEED_STEP;
        } else if (shipSpeed.y > 0) {
            shipSpeed.y -= SHIP_BREAK;
        }

        if (isKeyDownPressed && shipSpeed.y > -SHIP_MAXSPEED) {
            shipSpeed.y -= SHIP_SPEED_STEP;
        } else if (shipSpeed.y < 0) {
            shipSpeed.y += SHIP_BREAK;
        }

        if (isKeyLeftPressed && shipSpeed.x > -SHIP_MAXSPEED) {
            shipSpeed.x -= SHIP_SPEED_STEP;
        } else if (shipSpeed.x < 0) {
            shipSpeed.x += SHIP_BREAK;
        }

        if (isKeyRightPressed && shipSpeed.x < SHIP_MAXSPEED) {
            shipSpeed.x += SHIP_SPEED_STEP;
        } else if (shipSpeed.x > 0) {
            shipSpeed.x -= SHIP_BREAK;
        }

        pos.mulAdd(shipSpeed, delta);

        if (shipSpeed.len() < SHIP_BREAK ) shipSpeed.set(0,0);


    }

    private void checkBounds() {
        if (pos.x < worldBounds.getLeft() + halfWidth) {
            pos.x = worldBounds.getLeft() + halfWidth;
            shipSpeed.x = -shipSpeed.x / 3;
        }

        if (pos.x > worldBounds.getRight() - halfWidth) {
            pos.x = worldBounds.getRight() - halfWidth;
            shipSpeed.x = -shipSpeed.x / 3;
        }

        if (pos.y < worldBounds.getBottom() + halfHeight) {
            pos.y = worldBounds.getBottom() + halfHeight;
            shipSpeed.y = -shipSpeed.y / 3;
        }
        if (pos.y > worldBounds.getTop() - halfHeight) {
            pos.y = worldBounds.getTop() - halfHeight;
            shipSpeed.y = -shipSpeed.y / 3;
        }
    }
}
