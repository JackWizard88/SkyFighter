package ru.geekbrains.sprite.gameObjects;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.controllers.SoundController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.gameObjects.gui.HpGUI;
import ru.geekbrains.sprite.gameObjects.gui.HpPlaneGUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PlayerPlane extends Sprite {

    //worldBounds
    private Rect worldBounds;

    //keyControl booleans
    private boolean isKeyUpPressed = false;
    private boolean isKeyDownPressed = false;
    private boolean isKeyLeftPressed = false;
    private boolean isKeyRightPressed = false;
    private boolean isKeySpacePressed = false;

    //GUI
    private HpGUI hp;
    private HpPlaneGUI hpPlane;

    //plane fields
    private Vector2 shipSpeed;
    private int score;
    private int health;

    //objects
    private Propeller propeller;
    private PilotHead pilotHead;
    private Vector2 PILOT_POS = new Vector2(getHalfWidth() / 6, getHalfHeight() / 2);

    private BitmapFont font;
    private StringBuilder strBuilder;

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
    private final float SHIP_MAXSPEED = 0.5f;
    private final float SHIP_SPEED_STEP_BACK = 0.02f;
    private final float SHIP_SPEED_STEP_FORWARD = 0.01f;
    private final float SHIP_SPEED_STEP_UP = 0.015f;
    private final float SHIP_SPEED_STEP_DOWN = 0.025f;
    private final float SHIP_BREAK =  0.005f;
    private final float SHIP_SPEED_UP = 0.3f;
    private final float SHIP_SPEED_DOWN = -0.75f;
    private final float STABILIZE_ANGLE = 0.3f;
    private final float MAX_ANGLE = 10f;
    private final float FALL_SPEED = 0.01f;
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public PlayerPlane(TextureAtlas atlas) {
        super(atlas.findRegion("playerPlaneBody"));

        //GUI
        hp = new HpGUI(atlas);
        hpPlane = new HpPlaneGUI(atlas);

        strBuilder = new StringBuilder();
        font = new BitmapFont(Gdx.files.internal("fonts/font02.fnt"));

        shipSpeed = new Vector2();
        dir = new Vector2();
        bulletPos0 = new Vector2();


        pos.set(-0.5f, 0);
        this.score = 0;
        this.health = 10;

        //Sounds
        soundFlying = Gdx.audio.newSound(Gdx.files.internal("sounds/flying1.mp3"));
        soundShooting = Gdx.audio.newSound(Gdx.files.internal("sounds/shooting1.mp3"));
        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.mp3"));

        propeller = new Propeller(atlas.findRegion("playerPlanePropeller"), 1, 11, 11, this);
        propeller.setShift(getHalfWidth(), - getHalfHeight() / 3);

        pilotHead = new PilotHead(atlas.findRegion("pilotHead"), 2, 6, 12, this);
        pilotHead.setPilotPos(PILOT_POS.x, PILOT_POS.y);

    }

    public int getScore() {
        return score;
    }

    public int getHealth() {
        return health;
    }

    public void show() {
        idSoundFlying = soundFlying.play(0.8f);
        soundFlying.setLooping(idSoundFlying, true);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        this.worldBounds = worldBounds;
        soundFlying.resume();
        hp.resize(worldBounds);
        hpPlane.resize(worldBounds);
        font.getData().setScale(0.05f);
        pilotHead.resize(worldBounds);
        propeller.resize(worldBounds);
        propeller.setShift(getHalfWidth(), -getHalfHeight() / 3);
    }

    @Override
    public void draw(SpriteBatch batch) {
        update(Gdx.graphics.getDeltaTime());
        propeller.draw(batch);
        pilotHead.draw(batch);
        super.draw(batch);
    }

    public void drawGUI(SpriteBatch batch) {
        hp.draw(batch);
        for (int i = 0; i < health; i++) {
            hpPlane.draw(batch, i);
        }
//        strBuilder.setLength(0);
//        strBuilder.append("SCORE: ").append(score).append("\nHP: ").append(health);
//        font.draw(batch, strBuilder, 0, 0);
    }

    @Override
    public void update(float delta) {
        checkShooting(delta);
        planeControl(delta);
        checkBounds();
        checkCollisions();
        propeller.update(delta);
        pilotHead.update(delta);

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

    private void checkCollisions() {
        for (Bullet bullet : ScreenController.getGameScreen().getBulletPool().getActiveObjects()) {
            if (this.isMe(bullet.pos) && bullet.getOwner() != this) {
                this.damage();
                SoundController.getSoundHit().play();
                bullet.destroy();
            }
        }

        for (EnemyPlane enemyPlane : ScreenController.getGameScreen().getEnemyPool().getActiveObjects()) {
            if (!this.isOutside(enemyPlane) && !enemyPlane.isFalling()) {
                this.damage();
                enemyPlane.kill();
            }
        }
    }

    private void damage() {
        this.health -= 1;
    }

    public void hide() {
        soundFlying.pause();
        soundShooting.pause();
        soundExplosion.pause();
    }

    public void dispose() {
        soundFlying.dispose();
        soundExplosion.dispose();
        soundShooting.dispose();
    }

    private void shoot() {
        Bullet bullet = ScreenController.getGameScreen().getBulletPool().obtain();
        bulletRegion = ScreenController.getGameScreen().getAtlas().findRegion("bullets");
        //смещение точки выстрела в зависимости от угла
        bulletPos0.set(pos.x + halfWidth * 0.95f, pos.y + getHeight() / 5 + getHeight() * (float) Math.sin(Math.toRadians(angle)));
        //поворот аектора направления полета снаряда
        dir.set((float) Math.cos(Math.toRadians(angle)), (float) Math.sin(Math.toRadians(angle))).nor();
        bullet.set(this, bulletRegion, 3, 1, 3, bulletPos0, bulletV, angle, dir, 0.003f, worldBounds, 1);
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

            if (angle > -STABILIZE_ANGLE && angle < STABILIZE_ANGLE) {
                angle = 0f;
            } else if (angle > STABILIZE_ANGLE) {
                angle -= STABILIZE_ANGLE;
            } else if (angle < STABILIZE_ANGLE) {
                angle += STABILIZE_ANGLE;
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
