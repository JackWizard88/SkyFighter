package ru.geekbrains.sprite;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {

    private Vector2 coordinates;
    private Vector2 touch;
    private float velocity = 100f;

    public Player(Texture texture) {
        super(new TextureRegion(texture));
        this.coordinates = new Vector2();
        this.touch = coordinates;
    }

    public void setTouch(Vector2 touch) {
        this.touch = touch;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        this.pos.set(worldBounds.pos);
    }

    @Override
    public void draw(SpriteBatch batch) {
        update();
        super.draw(batch);
    }

    private void update() {
        if (coordinates != touch) {

        }
    }
}
