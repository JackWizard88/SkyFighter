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
    private Vector2 maxSpeed;
    private Vector2 accel;
    private Vector2 accel2;

    private final float SHIP_MAXSPEED = 15.0f;
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
        maxSpeed = new Vector2();
        accel = new Vector2();
        accel2 = new Vector2();

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
        touch.set(screenX , Gdx.graphics.getHeight() - screenY).sub(shipCenter);
        return false;
    }

    private void calculateShipCoordinates() {

        if (shipCoordinates != touch) {

            distance.set(touch).sub(shipCoordinates);       //расчет дистанции от корабля до точки
            direction.set(distance).nor();                  //нормируем вектор дистанции чтобы поулчить направление
            accel.set(direction).scl(SHIP_ACCEL);           //основной вектор ускорения направлен на точку назначения

            if (distance.dot(shipSpeed) < 1) {                  //Если вектор не направлен в точку то есть вектор который нам надо скомпенисировать чтобы направить в точку.
                accel2.set(shipSpeed).scl(-1);                  //вектор компенсирующий скорость
                accel.add(accel2).nor().scl(SHIP_ACCEL);        //суммируем вектора ускорений, нормируем и скалируем до величины нормального ускорения.
            }

            maxSpeed.set(accel).nor().scl(SHIP_MAXSPEED);     //расчитываем вектор максимальной скорости с учетом направления

            // если дистанция до точки меньше или равно скорости корабля, то мы ставим корабль в точку
            if (distance.len2() <= shipSpeed.len2() && direction.dot(shipSpeed) >= 0.999f) {
                shipSpeed.set(0, 0);
                shipCoordinates.set(touch);
                return;
            }

            // если дистанция меньше или равна точке торможения и корабль направлен на точку, то мы тормозим
            if (distance.len() <= Math.pow(shipSpeed.len(), 2) / (2 * accel.len()) && direction.dot(shipSpeed) == 1) {
                if (shipSpeed.len() > 0) {
                    shipSpeed.sub(accel);
                }
            } else {
                //иначе если скорость меньше максимальной или вектор движения направлен не на точку, то мы добавляем ускорение
                if (shipSpeed.len() < maxSpeed.len() || direction.dot(shipSpeed) < 1 ) {
                    shipSpeed.add(accel);
                }
            }

            shipCoordinates.add(shipSpeed);
        }
    }
}
