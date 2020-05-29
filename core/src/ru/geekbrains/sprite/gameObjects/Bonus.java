package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Bonus extends Sprite {

    public enum BonusType {
        boxAmmo, boxMedkit;
    }

    private Rect worldBounds;

    private Vector2 velocity;
    private BonusType bonusType;
    private Vector2 spawnCoordinates;
    private final float gravity = -0.09f;
    private final int AMOUNT_AMMO = 50;
    private final int AMOUNT_HEALTH = 3;

    public Bonus() {
        velocity = new Vector2();
        spawnCoordinates = new Vector2();
        regions = new TextureRegion[1];
    }

    public void set(Rect worldBounds) {

        switch ((int) Math.round(Math.random())) {
            case 0:
                this.regions[0] = ScreenController.getAtlas().findRegion("boxAmmo");
                this.bonusType = BonusType.boxAmmo;
                break;
            case 1:
                this.regions[0] = ScreenController.getAtlas().findRegion("boxMedkit");
                this.bonusType = BonusType.boxMedkit;
                break;
        }

        resize(worldBounds);
        this.velocity.set(Rnd.nextFloat(-0.1f, -0.2f), gravity);
        this.pos.set(getSpawnCoordinates(worldBounds));
    }

    public void giveBonus() {

        switch (bonusType) {
            case boxMedkit:
                ScreenController.getGameScreen().getPlayer().addHealth(AMOUNT_HEALTH);
                break;
            case boxAmmo:
                ScreenController.getGameScreen().getPlayer().addAmmo(AMOUNT_AMMO);
                break;
        }
    }


    private Vector2 getSpawnCoordinates(Rect worldBounds) {
        spawnCoordinates.x = Rnd.nextFloat(worldBounds.getLeft() + worldBounds.getHalfHeight() * 3 / 4, worldBounds.getRight() - halfWidth);
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

    public void checkBounds() {
        if (pos.x + halfWidth < worldBounds.getLeft() || pos.y + halfHeight < worldBounds.getBottom()) {
            destroy();
        }
    }

}
