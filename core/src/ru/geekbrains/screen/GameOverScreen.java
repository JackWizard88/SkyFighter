package ru.geekbrains.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.base.Font;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.buttons.Button;
import ru.geekbrains.sprite.buttons.MenuButton;
import ru.geekbrains.sprite.gameObjects.Background;

public class GameOverScreen extends BaseScreen {

    //Buttons
    private TextureAtlas.AtlasRegion regionButtonMenu;
    private Button buttonMenu;

    //DATA
    private int playerScore;
    private int playerShots;
    private int playerKills;
    private int accuracy;

    private Font font1;
    private Font font2;
    private StringBuilder strBHeader1 = new StringBuilder();
    private StringBuilder strBHeader2 = new StringBuilder();
    private StringBuilder strBHeader3 = new StringBuilder();
    private StringBuilder strBHeader4 = new StringBuilder();
    private StringBuilder strBText1 = new StringBuilder();
    private StringBuilder strBText2 = new StringBuilder();
    private StringBuilder strBText3 = new StringBuilder();
    private StringBuilder strBText4 = new StringBuilder();

    private final float MARGIN = 0.1f;


    public GameOverScreen(ScreenController controller) {
        super(controller);

        regionButtonMenu = ScreenController.getAtlas().findRegion("buttonMenu");
        buttonMenu = new MenuButton(regionButtonMenu, controller);

        font1 = new Font("fonts/font48.fnt",  "fonts/font48.png");
        font2 = new Font("fonts/font48w.fnt",  "fonts/font48w.png");
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setPlayerShots(int playerShots) {
        this.playerShots = playerShots;
    }

    public void setPlayerKills(int playerKills) {
        this.playerKills = playerKills;
    }

    @Override
    public void show() {
        super.show();
        setText();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        draw();
    }

    public void draw() {
        batch.begin();

        font1.draw(batch, strBHeader1, worldBounds.pos.x,worldBounds.getTop() - 2 * MARGIN, Align.right);
        font2.draw(batch, strBText1, worldBounds.pos.x + MARGIN,worldBounds.getTop() - 2 * MARGIN, Align.left);

        font1.draw(batch, strBHeader2, worldBounds.pos.x,worldBounds.getTop() - 3 * MARGIN, Align.right);
        font2.draw(batch, strBText2, worldBounds.pos.x + MARGIN,worldBounds.getTop() - 3 * MARGIN, Align.left);

        font1.draw(batch, strBHeader3, worldBounds.pos.x,worldBounds.getTop() - 4 * MARGIN, Align.right);
        font2.draw(batch, strBText3, worldBounds.pos.x + MARGIN,worldBounds.getTop() - 4 * MARGIN, Align.left);

        font1.draw(batch, strBHeader4, worldBounds.pos.x,worldBounds.getTop() - 5 * MARGIN, Align.right);
        font2.draw(batch, strBText4, worldBounds.pos.x + MARGIN,worldBounds.getTop() - 5 * MARGIN, Align.left);

        buttonMenu.draw(batch);

        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        font1.setSize(0.04f);
        font2.setSize(0.04f);
        buttonMenu.resize(worldBounds);
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            controller.setMenuScreen();
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        buttonMenu.touchDown(touch, pointer, button);
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonMenu.touchUp(touch, pointer, button);
        return super.touchUp(touch, pointer, button);
    }

    private void setText() {
        strBHeader1.setLength(0);
        strBHeader1.append("Очков получено: ");

        strBText1.setLength(0);
        strBText1.append(playerScore);

        strBHeader2.setLength(0);
        strBHeader2.append("Сбито самолетов: ");

        strBText2.setLength(0);
        strBText2.append(playerKills);

        strBHeader3.setLength(0);
        strBHeader3.append("Выстрелов сделано: ");

        strBText3.setLength(0);
        strBText3.append(playerShots);

        strBHeader4.setLength(0);
        strBHeader4.append("Точность стрельбы: ");

        strBText4.setLength(0);
        if (playerShots == 0) {
            accuracy = 0;
        } else accuracy = Math.round((playerKills * 7 / (float) playerShots) * 100);
        strBText4.append(accuracy).append(" %");
    }
}
