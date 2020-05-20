package ru.geekbrains.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundController {

    private static SoundController soundController = new SoundController();

    private static Sound soundEnemyFlying;
    private static Sound soundEnemyShooting;
    private static Sound soundEnemyExplosion;

    private SoundController() {
        soundEnemyExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion2.mp3"));
        soundEnemyFlying = Gdx.audio.newSound(Gdx.files.internal("sounds/flying2.mp3"));
        soundEnemyShooting = Gdx.audio.newSound(Gdx.files.internal("sounds/shooting1.mp3"));
    }


    public static SoundController getSoundController() {
        return soundController;
    }

    public static Sound getSoundEnemyFlying() {
        return soundEnemyFlying;
    }

    public static Sound getSoundEnemyShooting() {
        return soundEnemyShooting;
    }

    public static Sound getSoundEnemyExplosion() {
        return soundEnemyExplosion;
    }

}
