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
    private Vector2 destinationCoordinates;
    private Vector2 shipSpeed;
    private Vector2 touch;
    private Vector2 distance;

    private final float SHIP_MAXSPEED = 50.0f;
    private final float SHIP_MINSPEED = 1.0f;
    private final float SHIP_ACCEL = 0.25f;

    private BitmapFont font2;

    @Override
    public void show() {
        super.show();
        backgroundMain = new Texture("backgroundGame.jpg");
        playerShip = new Texture("playerSpaceship.png");
        touch = new Vector2();
        shipSpeed = new Vector2();
        destinationCoordinates = new Vector2((Gdx.graphics.getWidth() - spaceShipWidth) / 2.0f, (Gdx.graphics.getHeight() - spaceShipHeight) / 2.0f);
        shipCoordinates = new Vector2(destinationCoordinates.x, destinationCoordinates.y);

        font2 = new BitmapFont();
        font2.setColor(Color.GREEN);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        calculateShipCoordinates();

        batch.begin();
        batch.draw(backgroundMain, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font2.draw(batch, String.format("Curr: X=%s, Y=%s", shipCoordinates.x, shipCoordinates.y), 10, 20);
        font2.draw(batch, String.format("Dest: X=%s, Y=%s", destinationCoordinates.x, destinationCoordinates.y), 10, 35);
        Sprite sprite = new Sprite(playerShip);

        if (destinationCoordinates.x > shipCoordinates.x) {
            isShipFlipped = true;
        } else if (destinationCoordinates.x < shipCoordinates.x) {
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
        touch.set(screenX , Gdx.graphics.getHeight() - screenY);
        destinationCoordinates = touch.sub(shipCenter);
        shipSpeed.set(0, 0);
        return false;
    }

    private void calculateShipCoordinates() {

            if (shipCoordinates != destinationCoordinates) {

                distance = destinationCoordinates.cpy().sub(shipCoordinates);
                Vector2 direction = distance.cpy().nor();

                if (distance.len() <= SHIP_MINSPEED) {
                    shipSpeed.set(0,0);
                    shipCoordinates.set(destinationCoordinates);
                    return;
                }

                if (distance.len() > shipSpeed.len() * shipSpeed.len() * ((1/SHIP_ACCEL)-1)) {

                    if (shipSpeed.x < SHIP_MAXSPEED) {
                        shipSpeed.x += SHIP_ACCEL;
                    }

                    if (shipSpeed.y < SHIP_MAXSPEED) {
                        shipSpeed.y += SHIP_ACCEL;
                    }

                } else {

                    if (shipSpeed.x > SHIP_MINSPEED) {
                        shipSpeed.x -= SHIP_ACCEL;
                    }

                    if (shipSpeed.y > SHIP_MINSPEED) {
                        shipSpeed.y -= SHIP_ACCEL;
                    }

                }

                Vector2 move = new Vector2(direction.x * shipSpeed.x, direction.y * shipSpeed.y);
                shipCoordinates.add(move);
            }
    }
}
