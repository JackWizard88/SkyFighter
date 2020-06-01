package ru.geekbrains.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.sprite.gameObjects.Bonus;
import ru.geekbrains.sprite.gameObjects.Bullet;

public class BonusController {

    private GameScreen gameScreen;

    private final int BOX_LIMIT = 1;
    private Rect worldBounds;
    private float timerSpawn = 0;

    public BonusController(GameScreen gameScreen, Rect worldBounds) {
        this.gameScreen = gameScreen;
        this.worldBounds = worldBounds;
    }

    public void checkBonuses(float delta) {
        //спаунер бонуса в случайной координате по таймеру
        timerSpawn += delta;
        if (timerSpawn >= Rnd.nextFloat(10f, 25f)) {
            timerSpawn = 0;
            if (gameScreen.getBonusPool().getSize() < BOX_LIMIT) {
                Bonus bonus = gameScreen.getBonusPool().obtain();
                bonus.set(worldBounds);
            }
        }
    }

    private void checkCollisions() {
        for (Bonus bonus : gameScreen.getBonusPool().getActiveObjects()) {
            bonus.checkCollisions();
        }

        for (Bonus bonus : gameScreen.getBonusPool().getActiveObjects()) {
            for (Bullet bullet : gameScreen.getBulletPool().getActiveObjects()) {
                if (bonus.isMe(bullet.pos) && bullet.getOwner() == gameScreen.getPlayer()) {
                    bullet.destroy();
                    bonus.explode();
                    bonus.destroy();
                }
            }
        }
    }

    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        for (Bonus bonus : gameScreen.getBonusPool().getActiveObjects()) {
            bonus.resize(worldBounds);
        }
    }

    public void hide() {
        for (Bonus bonus : gameScreen.getBonusPool().getActiveObjects()) {
            bonus.hide();
        }
    }

    public void updateActiveSprites(float delta) {
        checkBonuses(delta);
        checkCollisions();
        gameScreen.getBonusPool().updateActiveSprites(delta);
    }

    public void freeAllDestroyed() {
        gameScreen.getBonusPool().freeAllDestroyed();
    }

    public void drawActiveSprites(SpriteBatch batch) {
        gameScreen.getBonusPool().drawActiveSprites(batch);

    }

}
