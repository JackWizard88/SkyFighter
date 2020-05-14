package ru.geekbrains.sprite.gameObjects;

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

    private final float SHIP_MAXSPEED = 0.5f;
    private final float SHIP_SPEED_STEP_BACK = 0.02f;
    private final float SHIP_SPEED_STEP_FORWARD = 0.01f;
    private final float SHIP_SPEED_STEP_UP = 0.015f;
    private final float SHIP_SPEED_STEP_DOWN = 0.025f;
    private final float SHIP_BREAK =  0.01f;
    private final float SHIP_SPEED_UP = 0.3f;
    private final float SHIP_SPEED_DOWN = -0.75f;
    public static final float STABILAZE_ANGLE = 0.3f;
    public static final float MAX_ANGLE = 10f;
    public static final float FALL_SPEED = 0.01f;

    public Player(TextureRegion region) {
        super(region);
        shipSpeed = new Vector2();
        pos.x = -0.5f;
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

    private void shipControl(float delta) {

        pos.y -= FALL_SPEED * delta;

        if (isKeyUpPressed) {
            if (angle < MAX_ANGLE) {
                angle += 0.5f;
            }
        } else if (isKeyDownPressed) {
            if (angle > -MAX_ANGLE) {
                angle -= 0.75f;
            }
        } else {

            if (angle > -STABILAZE_ANGLE && angle < STABILAZE_ANGLE) {
                angle = 0f;
            } else if (angle > STABILAZE_ANGLE) {
                angle -= STABILAZE_ANGLE;
            } else if (angle < STABILAZE_ANGLE) {
                angle += STABILAZE_ANGLE;
            }
        }

        if (isKeyUpPressed && shipSpeed.y < SHIP_SPEED_UP) {
            shipSpeed.y += SHIP_SPEED_STEP_UP;
        } else if (shipSpeed.y > 0) {
            shipSpeed.y -= SHIP_BREAK;
        }

        if (isKeyDownPressed && shipSpeed.y > SHIP_SPEED_DOWN) {
            shipSpeed.y -= SHIP_SPEED_STEP_DOWN;
        } else if (shipSpeed.y < 0) {
            shipSpeed.y += SHIP_BREAK;
        }

        if (isKeyLeftPressed && shipSpeed.x > (-2) * SHIP_MAXSPEED) {
            shipSpeed.x -= SHIP_SPEED_STEP_BACK;
        } else if (shipSpeed.x < 0) {
            shipSpeed.x += SHIP_BREAK;
        }

        if (isKeyRightPressed && shipSpeed.x < SHIP_MAXSPEED) {
            shipSpeed.x += SHIP_SPEED_STEP_FORWARD;
        } else if (shipSpeed.x > 0) {
            shipSpeed.x -= SHIP_BREAK;
        }

        if (shipSpeed.len() < SHIP_BREAK) shipSpeed.set(0,0f);

        pos.mulAdd(shipSpeed, delta);


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
