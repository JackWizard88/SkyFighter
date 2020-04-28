package ru.geebrains.starfighter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geebrains.starfighter.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture backgroundMain;
    private Texture playerShip;

    private final int spaceShipWidth = 200;
    private final int spaceShipHeight = 100;
    private final Vector2 shipCenter = new Vector2(spaceShipWidth / 2.0f, spaceShipHeight / 2.0f);

    private Vector2 shipCoordinates;
    private Vector2 destinationCoordinates;
    private Vector2 shipSpeed;
    private Vector2 touch;

    private final float SHIP_MAXSPEED = 15.0f;
    private final float SHIP_ACCEL = 0.75f;

    @Override
    public void show() {
        super.show();
        backgroundMain = new Texture("background.jpg");
        playerShip = new Texture("playerSpaceship.png");
        touch = new Vector2();
        shipSpeed = new Vector2();
        destinationCoordinates = new Vector2((Gdx.graphics.getWidth() - spaceShipWidth) / 2.0f, (Gdx.graphics.getHeight() - spaceShipHeight) / 2.0f);
        shipCoordinates = new Vector2(destinationCoordinates.x, destinationCoordinates.y);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        calculateShipCoordinates();

        batch.begin();
        batch.draw(backgroundMain, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(playerShip, shipCoordinates.x, shipCoordinates.y , spaceShipWidth, spaceShipHeight);
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
        touch.set(screenX , Gdx.graphics.getHeight() - screenY);
        destinationCoordinates = touch.sub(shipCenter);
        return false;
    }

    private void calculateShipCoordinates() {

        if (shipCoordinates != destinationCoordinates) {

            Vector2 distance = destinationCoordinates.cpy().sub(shipCoordinates);
            Vector2 direction = distance.cpy().nor();
            Vector2 move = new Vector2();

            if (distance.len() <= 10.0f) {
                shipCoordinates.set(destinationCoordinates);
                return;
            }

            if (distance.len() >= 50) {

                if (shipSpeed.x < SHIP_MAXSPEED ) {
                    shipSpeed.x = shipSpeed.x + SHIP_ACCEL;
                }

                if (shipSpeed.y < SHIP_MAXSPEED) {
                    shipSpeed.y = shipSpeed.y + SHIP_ACCEL;
                }

            } else {
                if (shipSpeed.x > SHIP_ACCEL + 1.0f) {
                    shipSpeed.x -= SHIP_ACCEL;
                } else shipSpeed.x = 1.0f;

                if (shipSpeed.y > SHIP_ACCEL + 1.0f) {
                    shipSpeed.y -= SHIP_ACCEL;
                } else shipSpeed.y = 1.0f;
            }

            move.set(direction.x * shipSpeed.x, direction.y * shipSpeed.y);
            shipCoordinates.add(move);
        }
    }
}
