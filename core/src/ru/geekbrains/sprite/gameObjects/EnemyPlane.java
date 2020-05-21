package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.controllers.SoundController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class EnemyPlane extends Sprite {

    private static final int SCORE = 3;
    private static final double MAX_ANGLE = 3f;
    private static final float STABILAZE_ANGLE = 0.2f;
    private float deltaHight;
    private Rect worldBounds;
    private Vector2 v;
    private final Vector2 grav = new Vector2(0, 0.00005f);
    private final Vector2 move = new Vector2(0, 0.1f);
    private Vector2 grav1;
    private Vector2 vertShift;
    private int health;

    //timers
    private float shootTimer = 0;
    private float reloadTimer = 0;
    private int shootingTurn = 0;

    //bullets
    private TextureRegion bulletRegion;
    private Vector2 bulletPos0;
    private final float bulletV = 0.75f;
    private Vector2 dir;
    private final int BULLET_TURN_COUNTER = 3;

    //status
    private boolean isFalling = false;

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
        dir = new Vector2();
        this.health = 7;
        soundFlying = SoundController.getSoundEnemyFlying();
        soundExplosion = SoundController.getSoundEnemyExplosion();
        soundShooting = SoundController.getSoundEnemyShooting();
    }

    @Override
    public void update(float delta) {

        if (isFalling) {
            if (angle < 60f) angle += 0.5f;
            if (grav1.len() < 5f) grav1.sub(grav);
            v.add(grav1);
        } else {

            if (!passedBy()) {
                shoot(delta);

                deltaHight = ScreenController.getGameScreen().getPlayer().pos.y - pos.y;
                vertShift.set(move).scl(deltaHight);

                if (deltaHight > 0.001f) {
                    if (angle > -MAX_ANGLE) {
                        angle -= 0.25f;
                    }
                } else if (deltaHight < -0.001f) {
                    if (angle < MAX_ANGLE) {
                        angle += 0.25f;
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


        if (pos.x < worldBounds.getLeft() - getHalfWidth() || pos.y < worldBounds.getBottom() - getHalfHeight()) {
            destroy();
        }
    }

    public void resize() {
        soundShooting.resume();
        soundFlying.resume();
        soundExplosion.resume();
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
            Rect worldBounds
    ) {
        this.regions[0] = region;
        this.pos.set(pos0);
        this.v.set(v);
        this.health = health;
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        grav1.setZero();
        isFalling = false;
        angle = 0;
        soundFlying.play(0.8f);
    }

    public boolean isFalling() {
        return isFalling;
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

            if (shootTimer >= 0.05f) {
                shootingTurn++;
                shootTimer = 0;
                soundShooting.play();
                Bullet bullet = ScreenController.getGameScreen().getBulletPool().obtain();
                bulletRegion = ScreenController.getGameScreen().getAtlas().findRegion("bullets");
                bulletPos0.set(pos.x - halfWidth * 0.95f, pos.y + getHeight() / 5 + getHeight() * (float) Math.sin(Math.toRadians(angle)));
                dir.set((float) Math.cos(Math.toRadians(angle + 180)), (float) Math.sin(Math.toRadians(180 + angle))).nor();
                bullet.set(this, bulletRegion, 3, 1, 3, bulletPos0, bulletV, angle, dir, -0.003f, worldBounds, 1);
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
}
