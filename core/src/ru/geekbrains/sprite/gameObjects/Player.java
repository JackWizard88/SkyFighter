package ru.geekbrains.sprite.gameObjects;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {

    //worldBounds
    private Rect worldBounds;

    //keyControl booleans
    private boolean isKeyUpPressed = false;
    private boolean isKeyDownPressed = false;
    private boolean isKeyLeftPressed = false;
    private boolean isKeyRightPressed = false;
    private boolean isKeySpacePressed = false;

    //plane fields
    private Vector2 shipSpeed;
    private int score;
    private int health;

    //sounds
    private Sound soundFlying;
    private Sound soundShooting;
    private Sound soundExplosion;
    long idSoundFlying;
    long idShooting;

    //projectiles
    private TextureRegion bulletRegion;
    private final float bulletV = 1f;
    private Vector2 dir;
    private Vector2 bulletPos0;
    private float timer = 0f;
    private float scoreTimer = 0f;

    //constants
    private static final float MARGIN = 0.05f;
    private final float SHIP_MAXSPEED = 0.5f;
    private final float SHIP_SPEED_STEP_BACK = 0.02f;
    private final float SHIP_SPEED_STEP_FORWARD = 0.01f;
    private final float SHIP_SPEED_STEP_UP = 0.015f;
    private final float SHIP_SPEED_STEP_DOWN = 0.025f;
    private final float SHIP_BREAK =  0.005f;
    private final float SHIP_SPEED_UP = 0.3f;
    private final float SHIP_SPEED_DOWN = -0.75f;
    private static final float STABILAZE_ANGLE = 0.3f;
    private static final float MAX_ANGLE = 10f;
    private static final float FALL_SPEED = 0.01f;

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public Player(TextureAtlas atlas) {
        super(atlas.findRegion("plane3"), 1, 1, 1);
        shipSpeed = new Vector2();
        dir = new Vector2();
        bulletPos0 = new Vector2();
        pos.set(-0.5f, 0);
        this.score = 0;
        this.health = 3;
        soundFlying = Gdx.audio.newSound(Gdx.files.internal("sounds/flying1.mp3"));
        soundShooting = Gdx.audio.newSound(Gdx.files.internal("sounds/shooting1.mp3"));
        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.mp3"));
    }

    public void show() {
        idSoundFlying = soundFlying.play(0.9f);
        soundFlying.setLooping(idSoundFlying, true);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.075f);
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
        checkShooting(delta);
        planeControl(delta);
        checkBounds();

        scoreTimer += delta;
        if (scoreTimer >= 1f) {
            this.score += 1;
            scoreTimer = 0;
            System.out.println(score);
        }

        soundFlying.setPitch(idSoundFlying, 1 - (shipSpeed.x + shipSpeed.y)/6);
    }

    private void checkShooting(float delta) {

        if (isKeySpacePressed) {
            timer += delta;
            if (timer >= 0.1f) {
                shoot();
                timer = 0f;
            }
        }

    }

    public void hide() {
        soundFlying.pause();
    }

    public void dispose() {
        soundFlying.dispose();
        soundExplosion.dispose();
        soundShooting.dispose();
    }

    private void shoot() {
        Bullet bullet = GameScreen.getGameScreen().getBulletPool().obtain();
        bulletRegion = GameScreen.getGameScreen().getAtlas().findRegion("bullet2");
        //смещение точки выстрела в зависимости от угла
        bulletPos0.set(pos.x + halfWidth * 0.95f, pos.y + getHeight() / 5 + getHeight() * (float) Math.sin(Math.toRadians(angle)));
        //поворот аектора направления полета снаряда
        dir.set((float) Math.cos(Math.toRadians(angle)), (float) Math.sin(Math.toRadians(angle))).nor();
        bullet.set(this, bulletRegion, bulletPos0, bulletV, angle, dir, 0.01f, worldBounds, 1);
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
            case (Input.Keys.SPACE):
                isKeySpacePressed = true;
                idShooting = soundShooting.play();
                soundShooting.setLooping(idShooting, true);
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
            case (Input.Keys.SPACE):
                isKeySpacePressed = false;
                soundShooting.stop();
                timer = 0f;
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

    public void addScore(int amount) {
        this.score += amount;
        System.out.println(score);
    }
}
