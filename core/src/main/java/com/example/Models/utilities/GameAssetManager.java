package com.example.Models.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.example.Models.App;

import java.util.HashMap;
import java.util.Map;

public class GameAssetManager {
    private static GameAssetManager gameAssetManager;
    private Skin skin;

    // Music and sound management
    private Map<String, Music> musicTracks;
    private Music currentMusic;
    private String currentMusicName;
    private float currentMusicVolume;

    private GameAssetManager() {
        skin = new Skin(Gdx.files.internal("Skin/pixthulhu-ui.json"));

        // Initialize music tracks
        musicTracks = new HashMap<>();

        // Load music tracks - placeholder filenames, replace with actual files
        loadMusicTracks();

        // Set initial volume from settings
        currentMusicVolume = App.getSettings().getMusicVolume();

        // Start playing the default music track
        currentMusicName = App.getSettings().getCurrentMusic();
        playMusic(currentMusicName);
    }

    public static GameAssetManager getGameAssetManager() {
        if (gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
        }
        return gameAssetManager;
    }

    private void loadMusicTracks() {
        // Load all music tracks into memory
        // These are placeholder paths - replace with actual music file paths
        try {
            musicTracks.put("Main Theme", Gdx.audio.newMusic(Gdx.files.internal("sounds/music/main_theme.mp3")));
            musicTracks.put("Intense Battle", Gdx.audio.newMusic(Gdx.files.internal("sounds/music/battle.mp3")));
            musicTracks.put("Stealth Mission", Gdx.audio.newMusic(Gdx.files.internal("sounds/music/stealth.mp3")));
            musicTracks.put("Victory", Gdx.audio.newMusic(Gdx.files.internal("sounds/music/victory.mp3")));
        } catch (Exception e) {
            System.err.println("Could not load music tracks: " + e.getMessage());
            // Continue without music if files not found
        }
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public void playMusic(String musicName) {
        if (currentMusic != null) {
            currentMusic.stop();
        }

        Music music = musicTracks.get(musicName);
        if (music != null) {
            currentMusic = music;
            currentMusicName = musicName;
            currentMusic.setLooping(true);
            currentMusic.setVolume(currentMusicVolume);
            currentMusic.play();
        }
    }

    public void changeMusic(String musicName) {
        if (!musicName.equals(currentMusicName)) {
            playMusic(musicName);
        }
    }

    public void setMusicVolume(float volume) {
        currentMusicVolume = volume;
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }

    public Sound loadSound(String fileName) {
        try {
            return Gdx.audio.newSound(Gdx.files.internal(fileName));
        } catch (Exception e) {
            System.err.println("Could not load sound: " + fileName);
            return null;
        }
    }

    public void playSound(Sound sound) {
        if (App.getSettings().isSfxEnabled() && sound != null) {
            sound.play();
        }
    }

    public void dispose() {
        if (skin != null) {
            skin.dispose();
        }

        for (Music music : musicTracks.values()) {
            if (music != null) {
                music.dispose();
            }
        }
    }
}
