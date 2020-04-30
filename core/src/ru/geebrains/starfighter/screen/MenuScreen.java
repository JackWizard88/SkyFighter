package ru.geebrains.starfighter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import ru.geebrains.starfighter.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture backgroundMain;
    private Texture playerShip;
    private boolean isShipFlipped = false;

    private final int spaceShipWidth = 200;
    private final int spaceShipHeight = 100;
    private final Vector2 shipCenter = new Vector2(spaceShipWidth / 2.0f, spaceShipHeight / 2.0f);

    private Vector2 shipCoordinates;
    private Vector2 shipSpeed;
    private Vector2 touch;
    private Vector2 distance;
    private Vector2 direction;

    private final float SHIP_MAXSPEED = 50.0f;
    private final float SHIP_MINSPEED = 2.0f;
    private final float SHIP_ACCEL = 0.5f;

    private BitmapFont font2;

    @Override
    public void show() {
        super.show();
        backgroundMain = new Texture("backgroundGame.jpg");
        playerShip = new Texture("playerSpaceship.png");
        shipSpeed = new Vector2();
        touch = new Vector2((Gdx.graphics.getWidth() - spaceShipWidth) / 2.0f, (Gdx.graphics.getHeight() - spaceShipHeight) / 2.0f);
        shipCoordinates = new Vector2(touch);
        distance = new Vector2();
        direction = new Vector2();
        font2 = new BitmapFont();
        font2.setColor(Color.GREEN);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        calculateShipCoordinates();

        batch.begin();
        batch.draw(backgroundMain, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Sprite sprite = new Sprite(playerShip);

        if (touch.x > shipCoordinates.x) {
            isShipFlipped = true;
        } else if (touch.x < shipCoordinates.x) {
            isShipFlipped = false;
        }
        sprite.flip(isShipFlipped, false);

        batch.draw(sprite, shipCoordinates.x, shipCoordinates.y , spaceShipWidth, spaceShipHeight);
        batch.end();
    }

    @Override
    public void dispose() {
        playerShip.dispose();
        backgroundMain.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        shipSpeed.set(0, 0);
        touch.set(screenX , Gdx.graphics.getHeight() - screenY).sub(shipCenter);;
        return false;
    }

    private void calculateShipCoordinates() {

            if (shipCoordinates != touch) {

                distance.set(touch).sub(shipCoordinates);
                direction.set(distance).nor();

                if (distance.len() <=  SHIP_MINSPEED) {
                    shipCoordinates.set(touch);
                    return;
                }

                if (distance.len() > Math.pow(shipSpeed.len(), 2) / (2 * SHIP_ACCEL)) {
                    if (shipSpeed.len() < SHIP_MAXSPEED) {
                        shipSpeed.add(direction.scl(SHIP_ACCEL));
                    }
                } else {
                    if (shipSpeed.len() > SHIP_MINSPEED) {
                        shipSpeed.sub(direction.scl(SHIP_ACCEL));
                    }
                }

                shipCoordinates.add(shipSpeed);
            }
    }
}
