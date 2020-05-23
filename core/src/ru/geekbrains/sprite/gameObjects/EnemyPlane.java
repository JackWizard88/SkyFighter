package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.controllers.SoundController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class EnemyPlane extends Sprite {

    private final int SCORE = 3;
    private final double MAX_ANGLE = 1f;
    private final float STABILAZE_ANGLE = 0.2f;
    private Vector2 gunPosition;
    private float deltaHight;
    private Rect worldBounds;
    private Vector2 v;
    private final Vector2 grav = new Vector2(0, 0.00005f);
    private final Vector2 move = new Vector2(0, 0.1f);
    private Vector2 grav1;
    private Vector2 vertShift;
    private int health;
    private Vector2 fightingPosition;
    private Vector2 rallyingDirection;
    private final float RALLYING_SPEED = 0.15f;

    //timers
    private float shootTimer = 0;
    private float reloadTimer = 0;
    private int shootingTurn = 0;

    //bullets
    private TextureRegion bulletRegion;
    private Vector2 bulletPos0;
    private final float bulletV = 1f;
    private Vector2 dir;
    private int BULLET_TURN_COUNTER;

    //objects
    private Propeller propeller;
    private PilotHead pilotHead;

    //explosion
    private TextureRegion explosionRegion;

    //status
    private boolean isFalling = false;
    private boolean isActive = false;

    //sounds
    private Sound soundFlying;
    private Sound soundShooting;
    private Sound soundExplosion;

    public EnemyPlane() {
        regions = new TextureRegion[1];
        v = new Vector2();
        grav1 = new Vector2();
        vertShift = new Vector2();
        bulletPos0 = new Vector2();
        gunPosition = new Vector2();
        fightingPosition = new Vector2();
        rallyingDirection = new Vector2();
        dir = new Vector2();
        this.health = 7;
        soundFlying = SoundController.getSoundEnemyFlying();
        soundExplosion = SoundController.getSoundEnemyExplosion();
        soundShooting = SoundController.getSoundEnemyShooting();

        pilotHead = new PilotHead(ScreenController.getAtlas().findRegion("pilotHead"), 2, 6, 12, this);
        propeller = new Propeller(ScreenController.getAtlas().findRegion("enemyPlanePropeller"), 1, 11, 11, this);
    }

    @Override
    public void draw(SpriteBatch batch) {
            pilotHead.draw(batch);
            super.draw(batch);
            propeller.draw(batch);
    }

    @Override
    public void update(float delta) {

        if (ScreenController.getGameScreen().getPlayer().pos.x < pos.x) {
            gunPosition.set( -getHalfWidth() / 3, getHalfHeight() / 2);
        } else {
            gunPosition.set(getHalfWidth(), getHalfHeight() / 2);
        }

        if (!isActive) {
            Rallying(delta);
        } else if (isFalling) {
            if (angle > -60f) angle -= 0.5f;
            if (grav1.len() < 5f) grav1.sub(grav);
            v.add(grav1);
        } else {

            shoot(delta);

            if (!passedBy()) {

                deltaHight = ScreenController.getGameScreen().getPlayer().pos.y - pos.y;
                vertShift.set(move).scl(deltaHight);

                if (deltaHight > 0.001f) {
                    if (angle < MAX_ANGLE) {
                        angle += 0.25f;
                    }

                } else if (deltaHight < -0.001f) {
                    if (angle > -MAX_ANGLE) {
                        angle -= 0.25f;
                    }
                } else {
                    if (angle > STABILAZE_ANGLE) {
                        angle -= STABILAZE_ANGLE;
                    } else if (angle < STABILAZE_ANGLE) {
                        angle += STABILAZE_ANGLE;
                    } else angle = 0f;
                }
            }
            pos.mulAdd(vertShift, delta);
        }

        pos.mulAdd(v, delta);
        pilotHead.update(delta);
        propeller.update(delta);

        if (pos.x < worldBounds.getLeft() - getHalfWidth() || pos.y < worldBounds.getBottom() - getHalfHeight()) {
            destroy();
        }
    }

    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        soundShooting.resume();
        soundFlying.resume();
        soundExplosion.resume();
        pilotHead.resize(worldBounds);
        propeller.resize(worldBounds);
    }

    public void hide() {
        soundShooting.pause();
        soundFlying.pause();
        soundExplosion.pause();
    }

    public void dispose() {
        soundExplosion.dispose();
        soundFlying.dispose();
        soundShooting.dispose();
    }

    public void set(
            TextureRegion region,
            Vector2 pos0,
            Vector2 v,
            int health,
            float height,
            Rect worldBounds,
            int turnLength
    ) {
        this.regions[0] = region;
        this.pos.set(pos0);
        this.v.set(v);
        this.health = health;
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        BULLET_TURN_COUNTER = turnLength;

        grav1.setZero();
        isFalling = false;
        isActive = false;
        angle = 0;
        soundFlying.play(0.8f);
        fightingPosition.set(Rnd.nextFloat(worldBounds.getRight() - 0.3f, worldBounds.getRight() - getWidth()), Rnd.nextFloat(pos.y - 0.1f, pos.y + 0.1f));

        pilotHead.setPilotPos(getHalfWidth() / 2.8f, getHalfHeight() / 3.1f);
        propeller.setShift(getHalfWidth() / 1.75f, 0);
        pilotHead.resize(worldBounds);
        propeller.resize(worldBounds);
    }

    public boolean isFalling() {
        return isFalling;
    }

    public boolean isActive() {
        return isActive;
    }

    public void damage() {
        this.health -= 1;
        checkHealth();
    }

    public void kill() {
        this.health = 0;
        checkHealth();
    }

    public void checkHealth() {
        if (health <= 0) {
            explode();
            isFalling = true;
            soundExplosion.play(1f);
            soundFlying.stop();
            ScreenController.getGameScreen().getPlayer().addScore(SCORE);
        }
    }

    public void shoot(float delta) {
        reloadTimer += delta;

        if (reloadTimer >= Rnd.nextFloat(2f, 3f) && !isFalling) {

            shootTimer += delta;

            if (shootTimer >= 0.1f) {
                shootingTurn++;
                shootTimer = 0;
                soundShooting.play();
                Bullet bullet = ScreenController.getGameScreen().getBulletPool().obtain();
                bulletRegion = ScreenController.getAtlas().findRegion("bullets");
                bulletPos0.set(pos).add(gunPosition);
                dir.set(ScreenController.getGameScreen().getPlayer().pos).sub(pos).nor();
                bullet.set(this, bulletRegion, 3, 1, 3, bulletPos0, bulletV, dir.angle(), dir, 0.003f, worldBounds, 1);
            }

            if (shootingTurn >= BULLET_TURN_COUNTER) {
                reloadTimer = 0;
                shootingTurn = 0;
            }
        }
    }

    private boolean passedBy() {
        return ScreenController.getGameScreen().getPlayer().pos.x - pos.x > 0;
    }

    private void explode() {
        Explosion explosion = ScreenController.getGameScreen().getExplosionPool().obtain();
        explosionRegion = ScreenController.getAtlas().findRegion("explosion");
        explosion.set(this, explosionRegion, 2, 5, 9, 0.2f);
    }

    private void Rallying(float delta) {
        if (fightingPosition.dst2(pos) > 0.01f) {
            rallyingDirection.set(fightingPosition).sub(pos).nor();
            pos.mulAdd((rallyingDirection).scl(RALLYING_SPEED), delta);
        } else isActive = true;
    }
}
