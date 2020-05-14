package ru.geekbrains.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.buttons.Button;
import ru.geekbrains.sprite.buttons.ExitButton;
import ru.geekbrains.sprite.buttons.ResumeButton;
import ru.geekbrains.sprite.buttons.StartButton;

public class MenuButtonController {

    private ScreenController screenController;
    private TextureAtlas atlas;
    private ArrayList<Button> buttonList;
    private TextureAtlas.AtlasRegion regionButtonStart;
    private TextureAtlas.AtlasRegion regionButtonExit;
    private TextureAtlas.AtlasRegion regionButtonResume;

    private boolean isGameExists;

    public MenuButtonController(TextureAtlas atlas, ScreenController screenController) {
        buttonList = new ArrayList<>();
        this.screenController = screenController;
        this.atlas = atlas;
        isGameExists = false;
        createButtons();
    }

    public void setGameExists(boolean gameExists) {
        isGameExists = gameExists;
    }

    private void createButtons() {
        regionButtonStart = atlas.findRegion("buttonStart");
        regionButtonExit = atlas.findRegion("buttonExit");
        regionButtonResume = atlas.findRegion("buttonContinue");
        buttonList.add(0, new ExitButton(regionButtonExit, screenController));
        buttonList.add(1, new StartButton(regionButtonStart, screenController));
    }

    public void resize(Rect worldBounds) {
        if (isGameExists) {
            buttonList.add(2, new ResumeButton(regionButtonResume, screenController));
        }
        for (Button butt : buttonList) {
            butt.resize(worldBounds);
        }
    }

    public void draw(SpriteBatch batch) {
        update(Gdx.graphics.getDeltaTime());
        for (Button butt : buttonList) {
            butt.draw(batch);
        }
    }

    public void update(float delta) {
        for (Button butt : buttonList) {
            butt.update(delta);
        }
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        for (Button butt : buttonList) {
            butt.touchDown(touch, pointer, button);
        }
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        for (Button butt : buttonList) {
            butt.touchUp(touch, pointer, button);
        }
        return false;
    }
}
