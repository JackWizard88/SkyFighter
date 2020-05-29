package ru.geekbrains.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundController {

    private static SoundController soundController = new SoundController();

    private static Sound soundEnemyFlying;
    private static Sound soundEnemyShooting;
    private static Sound soundEnemyShootingTriple;
    private static Sound soundEnemyExplosion;
    private static Sound soundHit;
    private static Sound soundPlayerFlying;
    private static Sound soundPlayerShooting;
    private static Sound soundPlayerExplosion;
    private static Sound soundPlayerReload;
    private static Sound soundPlayerRepair;
    private static Sound soundPlayerCooldown;

    private SoundController() {

        soundEnemyExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion2.mp3"));
        soundEnemyFlying = Gdx.audio.newSound(Gdx.files.internal("sounds/flying2.mp3"));
        soundEnemyShooting = Gdx.audio.newSound(Gdx.files.internal("sounds/shooting_single.mp3"));
        soundEnemyShootingTriple = Gdx.audio.newSound(Gdx.files.internal("sounds/shooting_triple.mp3"));

        soundPlayerFlying = Gdx.audio.newSound(Gdx.files.internal("sounds/flying1.mp3"));
        soundPlayerShooting = Gdx.audio.newSound(Gdx.files.internal("sounds/shooting1.mp3"));
        soundPlayerExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.mp3"));
        soundPlayerReload = Gdx.audio.newSound(Gdx.files.internal("sounds/reload.mp3"));
        soundPlayerRepair = Gdx.audio.newSound(Gdx.files.internal("sounds/repair.mp3"));
        soundPlayerCooldown = Gdx.audio.newSound(Gdx.files.internal("sounds/cooldown.mp3"));

        soundHit = Gdx.audio.newSound(Gdx.files.internal("sounds/hit2.mp3"));

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

    public static Sound getSoundEnemyShootingTriple() {
        return soundEnemyShootingTriple;
    }

    public static Sound getSoundEnemyExplosion() {
        return soundEnemyExplosion;
    }

    public static Sound getSoundHit() {
        return soundHit;
    }

    public static Sound getSoundPlayerFlying() {
        return soundPlayerFlying;
    }

    public static Sound getSoundPlayerShooting() {
        return soundPlayerShooting;
    }

    public static Sound getSoundPlayerExplosion() {
        return soundPlayerExplosion;
    }

    public static Sound getSoundPlayerReload() {
        return soundPlayerReload;
    }

    public static Sound getSoundPlayerRepair() {
        return soundPlayerRepair;
    }

    public static Sound getSoundPlayerCooldown() {
        return soundPlayerCooldown;
    }

    public static void dispose() {
        soundEnemyExplosion.dispose();
        soundEnemyFlying.dispose();
        soundEnemyShooting.dispose();
        soundEnemyShootingTriple.dispose();
        soundHit.dispose();
        soundPlayerExplosion.dispose();
        soundPlayerFlying.dispose();
        soundPlayerShooting.dispose();
        soundPlayerReload.dispose();
        soundPlayerRepair.dispose();
        soundPlayerCooldown.dispose();
    }
}
