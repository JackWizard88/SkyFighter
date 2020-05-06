package ru.geekbrains.sprite;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import javax.swing.SpringLayout;

public class Player extends Sprite {

    private Vector2 destination;
    private Vector2 distance;
    private Vector2 direction;
    private Vector2 speed;
    private float velocity = 0f;
    private final float accel = 0.01f;
    private final float maxSpeed = 0.5f;

    public Player(Texture texture) {
        super(new TextureRegion(texture));
        this.destination = pos;
        this.distance = new Vector2();
        this.speed = new Vector2();
        this.direction = new Vector2();
    }

    public void setDestination(Vector2 destination) {
        this.destination = destination;

    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        this.pos.set(worldBounds.pos);
    }

    @Override
    public void draw(SpriteBatch batch) {

        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    @Override
    public void update(float delta) {

        if (pos != destination) {

            distance.set(destination).sub(pos);
            direction.set(distance).nor();

            if (distance.len() <= velocity * delta) {
                pos.set(destination);
                velocity = 0f;
                return;
            }

            if (distance.len() <= (Math.pow(velocity * delta, 2) / (2 * accel * delta))) {
                    velocity -= accel;
            } else {
                if (velocity < maxSpeed) {
                    velocity += accel;
                }
            }

            speed.add(direction).scl(velocity);
            pos.mulAdd(speed, delta);
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        destination = touch;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
}
