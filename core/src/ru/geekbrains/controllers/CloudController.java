package ru.geekbrains.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.base.Layer;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.gameObjects.Cloud;

public class CloudController {

    private TextureAtlas.AtlasRegion cloudTextureRegion;

    private Cloud[] cloudsForeground;
    private Cloud[] cloudsMiddle;
    private Cloud[] cloudsBackground;

    private int FOREGROUND_CLOUDS_COUNT;
    private int MIDDLE_CLOUDS_COUNT;
    private int BACKGROUND_CLOUDS_COUNT;

    private final float FOREGROUND_CLOUDS_OPACY = 0.9f;
    private final float FOREGROUND_CLOUDS_SHADE = 1f;
    private final float MIDDLE_CLOUDS_OPACY = 0.95f;
    private final float MIDDLE_CLOUDS_SHADE = 0.8f;
    private final float BACKGROUND_CLOUDS_OPACY = 1f;
    private final float BACKGROUND_CLOUDS_SHADE = 0.7f;

    public CloudController(int cloudsBack, int cloudsMiddle, int cloudsForeground) {

        FOREGROUND_CLOUDS_COUNT = cloudsForeground;
        MIDDLE_CLOUDS_COUNT = cloudsMiddle;
        BACKGROUND_CLOUDS_COUNT = cloudsBack;

        this.cloudsForeground = new Cloud[FOREGROUND_CLOUDS_COUNT];
        this.cloudsMiddle = new Cloud[MIDDLE_CLOUDS_COUNT];
        this.cloudsBackground = new Cloud[BACKGROUND_CLOUDS_COUNT];

        for (int i = 0; i < FOREGROUND_CLOUDS_COUNT; i++) {
            cloudTextureRegion = getRandomCloudTexture();
            this.cloudsForeground[i] = new Cloud(cloudTextureRegion, Layer.FOREGROUND);
        }
        for (int i = 0; i < MIDDLE_CLOUDS_COUNT; i++) {
            cloudTextureRegion = getRandomCloudTexture();
            this.cloudsMiddle[i] = new Cloud(cloudTextureRegion, Layer.MIDDLE);
        }
        for (int i = 0; i < BACKGROUND_CLOUDS_COUNT; i++) {
            cloudTextureRegion = getRandomCloudTexture();
            this.cloudsBackground[i] = new Cloud(cloudTextureRegion, Layer.BACKGROUND);
        }
    }

    private TextureAtlas.AtlasRegion getRandomCloudTexture() {
        String path = "cloud" + (int)((Math.random() * 4) + 1);
        return new TextureAtlas.AtlasRegion(ScreenController.getAtlas().findRegion(path));
    }

    public void resize(Rect worldBounds) {
        for (Cloud cloud: cloudsBackground) {
            cloud.resize(worldBounds);
        }
        for (Cloud cloud: cloudsMiddle) {
            cloud.resize(worldBounds);
        }
        for (Cloud cloud: cloudsForeground) {
            cloud.resize(worldBounds);
        }
    }

    public void drawBackgroundClouds(SpriteBatch batch) {
        for (Cloud cloud: cloudsBackground) {
            batch.setColor(BACKGROUND_CLOUDS_SHADE,BACKGROUND_CLOUDS_SHADE,BACKGROUND_CLOUDS_SHADE,BACKGROUND_CLOUDS_OPACY);
            cloud.draw(batch);
            batch.setColor(1,1,1,1);
        }
    }

    public void drawMiddleClouds(SpriteBatch batch) {
        for (Cloud cloud: cloudsMiddle) {
            batch.setColor(MIDDLE_CLOUDS_SHADE,MIDDLE_CLOUDS_SHADE,MIDDLE_CLOUDS_SHADE,MIDDLE_CLOUDS_OPACY);
            cloud.draw(batch);
            batch.setColor(1,1,1,1);
        }
    }

    public void drawForegroundClouds(SpriteBatch batch) {
        for (Cloud cloud: cloudsForeground) {
            batch.setColor(FOREGROUND_CLOUDS_SHADE,FOREGROUND_CLOUDS_SHADE,FOREGROUND_CLOUDS_SHADE,FOREGROUND_CLOUDS_OPACY);
            cloud.draw(batch);
            batch.setColor(1,1,1,1);
        }
    }
}
