package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.controllers.SoundController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Bonus extends Sprite {

    public enum BonusType {
        boxAmmo, boxRepair;
    }

    private Rect worldBounds;
    private TextureRegion explosionRegion;

    private Vector2 velocity;
    private BonusType bonusType;
    private Vector2 spawnCoordinates;
    private final float gravity = -0.09f;
    private final int AMOUNT_AMMO = 50;
    private final int AMOUNT_HEALTH = 3;

    private Sound soundReload;
    private Sound soundRepair;
    private long idSoundReload;
    private long idSoundRepair;

    public Bonus() {
        velocity = new Vector2();
        spawnCoordinates = new Vector2();
        regions = new TextureRegion[1];
        soundReload = SoundController.getSoundPlayerReload();
        soundRepair = SoundController.getSoundPlayerRepair();
    }

    public void set(Rect worldBounds) {

        switch ((int) Math.round(Math.random())) {
            case 0:
                this.regions[0] = ScreenController.getAtlas().findRegion("boxAmmo");
                this.bonusType = BonusType.boxAmmo;
                break;
            case 1:
                this.regions[0] = ScreenController.getAtlas().findRegion("boxRepair");
                this.bonusType = BonusType.boxRepair;
                break;
        }

        resize(worldBounds);
        this.velocity.set(Rnd.nextFloat(-0.1f, -0.2f), gravity);
        this.pos.set(getSpawnCoordinates(worldBounds));
    }

    public void checkCollisions() {

        if (!ScreenController.getGameScreen().getPlayer().isOutside(this)) {
            switch (bonusType) {
                case boxRepair:
                    if (ScreenController.getGameScreen().getPlayer().getHealth() < ScreenController.getGameScreen().getPlayer().getMAX_HEALTH()) {
                        ScreenController.getGameScreen().getPlayer().addHealth(AMOUNT_HEALTH);
                        idSoundRepair = soundRepair.play();
                        destroy();
                    }
                    break;
                case boxAmmo:
                    if (ScreenController.getGameScreen().getPlayer().getAmmo() < ScreenController.getGameScreen().getPlayer().getMAX_AMMO()) {
                        ScreenController.getGameScreen().getPlayer().addAmmo(AMOUNT_AMMO);
                        idSoundReload = soundReload.play();
                        destroy();
                    }
                    break;
            }
        }
    }

    private Vector2 getSpawnCoordinates(Rect worldBounds) {
        spawnCoordinates.x = Rnd.nextFloat(worldBounds.pos.x, worldBounds.getRight());
        spawnCoordinates.y = worldBounds.getTop() + halfHeight;
        return spawnCoordinates;
    }

    @Override
    public void update(float delta) {
        this.pos.mulAdd(velocity, delta);
        checkBounds();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(0.05f);
    }

    public void hide() {
        soundRepair.stop(idSoundRepair);
        soundReload.stop(idSoundReload);
    }

    public void checkBounds() {
        if (pos.x + halfWidth < worldBounds.getLeft() || pos.y + halfHeight < worldBounds.getBottom()) {
            destroy();
        }
    }

    public void explode() {
        Explosion explosion = ScreenController.getGameScreen().getExplosionPool().obtain();
        explosionRegion = ScreenController.getAtlas().findRegion("explosion");
        explosion.set(this, explosionRegion, 3, 5, 12, 0.1f);
        SoundController.getSoundEnemyExplosion().play();
    }

}
