package ru.geekbrains.sprite.gameObjects;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {

    private Rect worldBounds;

    private boolean isKeyUpPressed = false;
    private boolean isKeyDownPressed = false;
    private boolean isKeyLeftPressed = false;
    private boolean isKeyRightPressed = false;

    private Vector2 shipSpeed;
    private int score;
    private int health;


    private Sound soundFlying;
    private Sound soundShooting;
    private Sound soundExplosion;
    long idSoundFlying = 1;

    private final float SHIP_MAXSPEED = 0.5f;
    private final float SHIP_SPEED_STEP_BACK = 0.02f;
    private final float SHIP_SPEED_STEP_FORWARD = 0.01f;
    private final float SHIP_SPEED_STEP_UP = 0.015f;
    private final float SHIP_SPEED_STEP_DOWN = 0.025f;
    private final float SHIP_BREAK =  0.005f;
    private final float SHIP_SPEED_UP = 0.3f;
    private final float SHIP_SPEED_DOWN = -0.75f;
    public static final float STABILAZE_ANGLE = 0.3f;
    public static final float MAX_ANGLE = 10f;
    public static final float FALL_SPEED = 0.01f;

    public Player(TextureRegion region, Rect worldBounds) {
        super(region);
        this.worldBounds = worldBounds;
        shipSpeed = new Vector2();
        setLeft(worldBounds.getLeft());
        this.score = 0;
        this.health = 3;
        soundFlying = Gdx.audio.newSound(Gdx.files.internal("sounds/flying1.mp3"));
        soundShooting = Gdx.audio.newSound(Gdx.files.internal("sounds/shooting.mp3"));
        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.mp3"));
    }

    public void show() {
        idSoundFlying = soundFlying.play();
        soundFlying.setLooping(1, true);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        this.worldBounds = worldBounds;
        soundFlying.resume();
    }

    @Override
    public void draw(SpriteBatch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    @Override
    public void update(float delta) {
        planeControl(delta);
        checkBounds();
        this.score += 1;
        soundFlying.setPitch(idSoundFlying, 1 + (shipSpeed.x + shipSpeed.y)/6);
    }

    public void hide() {
        soundFlying.pause();
    }

    public void dispose() {
        soundFlying.dispose();
        soundExplosion.dispose();
        soundShooting.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case (Input.Keys.UP):
            case (Input.Keys.W):
                isKeyUpPressed = true;
                break;
            case (Input.Keys.DOWN):
            case (Input.Keys.S):
                isKeyDownPressed = true;
                break;
            case (Input.Keys.LEFT):
            case (Input.Keys.A):
                isKeyLeftPressed = true;
                break;
            case (Input.Keys.RIGHT):
            case (Input.Keys.D):
                isKeyRightPressed = true;
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case (Input.Keys.UP):
            case (Input.Keys.W):
                    isKeyUpPressed = false;
                    break;
            case (Input.Keys.DOWN):
            case (Input.Keys.S):
                    isKeyDownPressed = false;
                    break;
            case (Input.Keys.LEFT):
            case (Input.Keys.A):
                    isKeyLeftPressed = false;
                    break;
            case (Input.Keys.RIGHT):
            case (Input.Keys.D):
                    isKeyRightPressed = false;
                    break;
        }

        return false;
    }

    private void planeControl(float delta) {

        pos.y -= FALL_SPEED * delta;

        if (isKeyUpPressed && !isKeyDownPressed) {
            if (angle < MAX_ANGLE) {
                angle += 0.5f;
            }
        } else if (isKeyDownPressed && !isKeyUpPressed) {
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

        if (isKeyUpPressed && !isKeyDownPressed && shipSpeed.y < SHIP_SPEED_UP) {
            shipSpeed.y += SHIP_SPEED_STEP_UP;
        } else if (shipSpeed.y > 0) {
            shipSpeed.y -= SHIP_BREAK;
        }

        if (isKeyDownPressed && !isKeyUpPressed && shipSpeed.y > SHIP_SPEED_DOWN) {
            shipSpeed.y -= SHIP_SPEED_STEP_DOWN;
        } else if (shipSpeed.y < 0) {
            shipSpeed.y += SHIP_BREAK;
        }

        if (isKeyLeftPressed && !isKeyRightPressed && shipSpeed.x > (-2) * SHIP_MAXSPEED) {
            shipSpeed.x -= SHIP_SPEED_STEP_BACK;
        } else if (shipSpeed.x < 0) {
            shipSpeed.x += SHIP_BREAK;
        }

        if (isKeyRightPressed && !isKeyLeftPressed && shipSpeed.x < SHIP_MAXSPEED) {
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
