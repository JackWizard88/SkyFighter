package ru.geekbrains.sprite.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import ru.geekbrains.base.Font;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.controllers.ScreenController;
import ru.geekbrains.math.Rect;

public class GUI extends Sprite {

    private PlayerPlane player;
    private Rect worldBounds;
    private Font font;
    private StringBuilder strBuilder;
    private StringBuilder strBuilderOverheat;
    private Sprite overheatStatus;
    private Sprite overheatFrame;

    private float animateTimer = 0;

    private final float MARGIN = 0.01f;

    public GUI(PlayerPlane player) {
        this.player = player;
        strBuilder = new StringBuilder();
        strBuilderOverheat = new StringBuilder();
        font = new Font("fonts/font48.fnt",  "fonts/font48.png");
        font.setSize(0.02f);

        overheatStatus = new Sprite(ScreenController.getAtlas().findRegion("overheatBar"), 2, 1, 2);
        overheatFrame = new Sprite(ScreenController.getAtlas().findRegion("overheatBarFrame"), 1, 1, 1);

    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        overheatStatus.setHeightProportion(0.025f);
        overheatFrame.setHeightProportion(0.025f);
        overheatFrame.setWidth(0.3f);
        overheatStatus.setLeft(worldBounds.getLeft() + MARGIN);
        overheatStatus.setBottom(worldBounds.getBottom() + MARGIN);
        overheatFrame.setLeft(worldBounds.getLeft() + MARGIN);
        overheatFrame.setBottom(worldBounds.getBottom() + MARGIN);
    }

    @Override
    public void update(float delta) {
        overheatStatus.setWidth(player.getOverheat() * overheatFrame.getWidth());
        overheatStatus.setLeft(overheatFrame.getLeft());
        overheatStatus.setBottom(overheatFrame.getBottom());

        if (ScreenController.getGameScreen().getPlayer().isOverheated()) {
            overHeatBarBlink(delta);
        } else overheatStatus.setFrame(0);
    }

    @Override
    public void draw(SpriteBatch batch) {
        strBuilder.setLength(0);
        strBuilder.append("СЧЁТ: ").append(player.getScore()).append("\nЖИЗНИ: ").append(player.getHealth()).append("\nБОЕЗАПАС: ").append(player.getAmmo());
        font.draw(batch, strBuilder, worldBounds.getLeft() + MARGIN,worldBounds.getTop() - MARGIN);

        strBuilderOverheat.setLength(0);
        strBuilderOverheat.append("Перегрев пулемета");
        font.draw(batch, strBuilderOverheat, overheatFrame.getRight() + MARGIN, overheatFrame.getTop(), Align.topLeft);
        overheatFrame.draw(batch);
        overheatStatus.draw(batch);
    }

    public void dispose() {
        font.dispose();
    }

    private void overHeatBarBlink(float delta) {
        animateTimer += delta;
        if (animateTimer >= 0.2f) {
            overheatStatus.setFrame((overheatStatus.getFrame() + 1) % overheatStatus.getRegions().length);
            animateTimer = 0;
        }
    }
}
