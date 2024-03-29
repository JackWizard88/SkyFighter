package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.controllers.SoundController;
import ru.geekbrains.controls.XBox360Pad;
import ru.geekbrains.math.Rect;

public class PlayerPlane extends Sprite implements ControllerListener {

    //worldBounds
    private Rect worldBounds;

    //keyControl booleans
    private boolean isKeyUpPressed = false;
    private boolean isKeyDownPressed = false;
    private boolean isKeyLeftPressed = false;
    private boolean isKeyRightPressed = false;
    private boolean isKeySpacePressed = false;

    //GUI
    private GUI gui;

    //plane fields
    private Vector2 shipSpeed;
    private int score;
    private int health;
    private int shots;
    private int kills;

    //боезапас, здоровье и система перегрева
    private int ammo;
    private float OVERHEAT_CAPACITY = 1f;
    private float OVERHEAT_STEP = 0.02f;
    private float OVERHEAT_COOLDOWN_STEP = 0.15f;
    private int MAX_HEALTH = 10;
    private int MAX_AMMO = 500;
    private int BULLET_DAMAGE = 1;
    private float overheat;
    private boolean isOverheated = false;
    private float overheatTimer = 0;

    //objects
    private Propeller propeller;
    private PilotHead pilotHead;
    private Vector2 PILOT_POS;
    private Vector2 PROPELLER_POS;

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
    private final float SHIP_SPEED_STEP_BACK = 0.015f;
    private final float SHIP_SPEED_STEP_FORWARD = 0.01f;
    private final float SHIP_SPEED_STEP_UP = 0.015f;
    private final float SHIP_SPEED_STEP_DOWN = 0.02f;
    private final float SHIP_BREAK =  0.015f;
    private final float SHIP_SPEED_UP = 0.3f;
    private final float SHIP_SPEED_DOWN = -0.75f;
    private final float STABILIZE_ANGLE = 0.3f;
    private final float MAX_ANGLE = 10f;
    private final float FALL_SPEED = 0.01f;
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public PlayerPlane(TextureAtlas atlas) {
        super(atlas.findRegion("playerPlaneBody"));

        this.gui = new GUI(this);

        shipSpeed = new Vector2();
        dir = new Vector2();
        bulletPos0 = new Vector2();

        pos.set(-0.5f, 0);
        this.score = 0;
        this.health = MAX_HEALTH;
        this.overheat = 0;
        this.ammo = MAX_AMMO;

        //Sounds
        soundFlying = SoundController.getSoundPlayerFlying();
        soundShooting = SoundController.getSoundPlayerShooting();
        soundExplosion = SoundController.getSoundPlayerExplosion();
//TODO        soundShootingEmpty = SoundController.getSoundPlayerNoAmmo();

        //components
        propeller = new Propeller(atlas.findRegion("playerPlanePropeller"), 1, 11, 11, this);
        PROPELLER_POS = new Vector2();

        pilotHead = new PilotHead(atlas.findRegion("pilotHead"), 2, 6, 12, this);
        PILOT_POS = new Vector2();

    }

    public Vector2 getShipSpeed() {
        return shipSpeed;
    }

    public boolean isKeyUpPressed() {
        return isKeyUpPressed;
    }

    public boolean isKeyDownPressed() {
        return isKeyDownPressed;
    }

    public boolean isKeyLeftPressed() {
        return isKeyLeftPressed;
    }

    public boolean isKeyRightPressed() {
        return isKeyRightPressed;
    }

    public boolean isKeySpacePressed() {
        return isKeySpacePressed;
    }

    public int getMAX_HEALTH() {
        return MAX_HEALTH;
    }

    public int getMAX_AMMO() {
        return MAX_AMMO;
    }

    public int getScore() {
        return score;
    }

    public int getHealth() {
        return health;
    }

    public int getShots() {
        return shots;
    }

    public int getKills() {
        return kills;
    }

    public int getAmmo() {
        return ammo;
    }

    public float getOverheat() {
        return overheat;
    }

    public boolean isOverheated() {
        return isOverheated;
    }

    public void addKill() {
        this.kills += 1;
    }

    public void show() {
        idSoundFlying = soundFlying.play(1f);
        soundFlying.setLooping(idSoundFlying, true);
        soundShooting.resume();
        soundExplosion.resume();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(0.05f);
        PROPELLER_POS.set(halfWidth, -halfHeight / 3);
        PILOT_POS.set(0, 0);
        pilotHead.resize(worldBounds);
        propeller.resize(worldBounds);
        gui.resize(worldBounds);
    }

    @Override
    public void draw(SpriteBatch batch) {
        update(Gdx.graphics.getDeltaTime());
        propeller.draw(batch);
        pilotHead.draw(batch);
        super.draw(batch);
    }

    public void drawGUI(SpriteBatch batch) {
        gui.draw(batch);
    }

    @Override
    public void update(float delta) {
        gui.update(delta);
        checkShooting(delta);
        planeControl(delta);
        checkBounds();
        checkCollisions();
        propeller.update(delta);
        pilotHead.update(delta);
        propeller.setShift(PROPELLER_POS.x, PROPELLER_POS.y);
        pilotHead.setPilotPos(PILOT_POS.x, PILOT_POS.y);

        scoreTimer += delta;
        if (scoreTimer >= 1f) {
            this.score += 1;
            scoreTimer = 0;
        }

        soundFlying.setPitch(idSoundFlying, 1 - (shipSpeed.x + shipSpeed.y)/6);
    }

    private void checkShooting(float delta) {

        if (isKeySpacePressed && overheat < OVERHEAT_CAPACITY) {
            timer += delta;
            if (timer >= 0.15f) {
                shoot();
                overheat += OVERHEAT_STEP;
                if (overheat > OVERHEAT_CAPACITY) {
                    soundShooting.stop(idShooting);
                    SoundController.getSoundPlayerCooldown().play();
                    overheat = OVERHEAT_CAPACITY;
                    isOverheated = true;
                }
                timer = 0f;
            }
        } else if (overheat > 0 && !isOverheated) {
            overheat -= OVERHEAT_COOLDOWN_STEP * delta;
            if (overheat < 0) overheat = 0;
        } else if (isOverheated) {
            overheatTimer += delta;
            if (overheatTimer >= 3f) {
                overheatTimer = 0;
                overheat = 0.5f;
                isOverheated = false;
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
        Controllers.getCurrent().startVibration(300, 1f);
        this.health -= 1;
    }

    public void hide() {
        soundFlying.stop();
        soundShooting.stop();
        soundExplosion.pause();
    }

    public void dispose() {
        soundFlying.stop();
        soundShooting.stop();
        soundExplosion.stop();
        gui.dispose();
    }

    private void shoot() {
        if (ammo > 0) {
            ammo -= 1;
            Bullet bullet = ScreenController.getGameScreen().getBulletPool().obtain();
            bulletRegion = ScreenController.getAtlas().findRegion("bullets");
            //смещение точки выстрела в зависимости от угла
            bulletPos0.set(pos.x + halfWidth * 0.95f, pos.y + getHeight() / 5 + getHeight() * (float) Math.sin(Math.toRadians(angle)));
            //поворот вектора направления полета снаряда
            dir.set((float) Math.cos(Math.toRadians(angle)), (float) Math.sin(Math.toRadians(angle))).nor();
            bullet.set(this, bulletRegion, 3, 1, 3, bulletPos0, bulletV, angle, dir, 0.003f, worldBounds, BULLET_DAMAGE);
            shots += 1;
        } else soundShooting.stop(idShooting);
    }

    @Override
    public boolean keyDown(int keycode) {

        if (!Controllers.getCurrent().isConnected()) {
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
                    activateShootingSound();
                    break;
            }
        }

        return false;
    }

    private void activateShootingSound() {
        if (ammo > 0 && overheat < OVERHEAT_CAPACITY) {
            idShooting = soundShooting.play();
            Controllers.getCurrent().startVibration(10000, 0.5f);
            soundShooting.setLooping(idShooting, true);
        } else if (overheat == OVERHEAT_CAPACITY) {
            //нет звука потому что перегрелся
        } else if (ammo == 0) {
            //звук бойка без патронов
        }
    }

    private void stopShootingSound() {
        soundShooting.stop();
        Controllers.getCurrent().cancelVibration();
        timer = 0f;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (!Controllers.getCurrent().isConnected()) {
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
                    stopShootingSound();
                    break;
            }
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

        if (shipSpeed.len() < 0.1 * SHIP_BREAK) shipSpeed.set(0,0f);

        pos.mulAdd(shipSpeed, delta);

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {

        if (controller.isConnected()) {
            if (buttonCode == XBox360Pad.BUTTON_UP) isKeyUpPressed = true;
            if (buttonCode == XBox360Pad.BUTTON_DOWN) isKeyDownPressed = true;
            if (buttonCode == XBox360Pad.BUTTON_LEFT) isKeyLeftPressed = true;
            if (buttonCode == XBox360Pad.BUTTON_RIGHT) isKeyRightPressed = true;
            if (buttonCode == XBox360Pad.BUTTON_A) {
                isKeySpacePressed = true;
                activateShootingSound();
            }
        }

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {

        if (controller.isConnected()) {
            if (buttonCode == XBox360Pad.BUTTON_UP) isKeyUpPressed = false;
            if (buttonCode == XBox360Pad.BUTTON_DOWN) isKeyDownPressed = false;
            if (buttonCode == XBox360Pad.BUTTON_LEFT) isKeyLeftPressed = false;
            if (buttonCode == XBox360Pad.BUTTON_RIGHT) isKeyRightPressed = false;
            if (buttonCode == XBox360Pad.BUTTON_A) {
                isKeySpacePressed = false;
                stopShootingSound();
            }
        }

        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) { return false; }


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
    }

    public void addHealth(int amount) {
        this.health +=amount;
        if (health > MAX_HEALTH) health = MAX_HEALTH;
    }

    public void addAmmo(int amount) {
        this.ammo += amount;
        if (ammo > MAX_AMMO) ammo = MAX_AMMO;
    }

    @Override
    public void connected(Controller controller) {
    }

    @Override
    public void disconnected(Controller controller) {
    }

}
