package com.example.Models.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.example.Models.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameAssetManager {
    private static GameAssetManager gameAssetManager;
    private static Texture map = new Texture(Gdx.files.internal("MapDetails/map.png"));

    // enemies
    private final String enemy1 = "Enemies/TreeMonster_0.png";
    private final Texture enemy1Texture = new Texture(enemy1);
    //characters
    private final Texture shanaTex = new Texture("Characters/Shana/Idle_0.png");
    private final Texture diamondTex = new Texture("Characters/Diamond/Idle_0.png");
    private final Texture dasherTex = new Texture("Characters/Dasher/Idle_0.png");
    private final Texture lilithTex = new Texture("Characters/Lilith/Idle_0.png");
    private final Texture scarletTex = new Texture("Characters/Scarlet/Idle_0.png");

    // weapons
    private final String smg = "Weapons/SMGStill.png";
    private final Texture smgTexture = new Texture(smg);
    private final Texture revolverTexture = new Texture("Weapons/Revolver.png");
    private final Texture shotGunTexture = new Texture("Weapons/Shotgun.png");

    //bullet
    private final String bullet = Gdx.files.internal("Bullets/bullet.png").toString();
    private Map<String, Animation<TextureRegion>> enemyAnimations = new HashMap<>();
    private Sound bulletsound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/single_shot.wav"));

    private Skin skin;
    private Map<String, Music> musicTracks;
    private Music currentMusic;
    private String currentMusicName;
    private float currentMusicVolume;

    private GameAssetManager() {
        skin = new Skin(Gdx.files.internal("Skin/pixthulhu-ui.json"));

        musicTracks = new HashMap<>();
        loadMusicTracks();
        currentMusicVolume = App.getSettings().getMusicVolume();

        currentMusicName = App.getSettings().getCurrentMusic();
        if (currentMusicName == null || !musicTracks.containsKey(currentMusicName)) {
            if (!musicTracks.isEmpty()) {
                currentMusicName = musicTracks.keySet().iterator().next();
            } else {
                currentMusicName = "Main Theme";
            }
        }
        playMusic(currentMusicName);
    }

    public static GameAssetManager getGameAssetManager() {
        if (gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
        }
        return gameAssetManager;
    }

    public void bulletSound() {
        if (App.getSettings().isSfxEnabled()) {
            bulletsound.play();
        }
    }

    private void loadMusicTracks() {
        try {
            FileHandle musicDir = Gdx.files.internal("sounds/musics");
            if (!musicDir.exists() || !musicDir.isDirectory()) {
                System.out.println("Music directory not found. Creating default music map.");
                createDefaultMusicMap();
                return;
            }

            for (FileHandle file : musicDir.list()) {
                if (isMusicFile(file.name())) {
                    try {
                        String musicName = getFormattedMusicName(file.nameWithoutExtension());
                        Music music = Gdx.audio.newMusic(file);
                        musicTracks.put(musicName, music);
                        System.out.println("Loaded music: " + musicName);
                    } catch (Exception e) {
                        System.err.println("Failed to load music file: " + file.name() + " - " + e.getMessage());
                    }
                }
            }

            if (musicTracks.isEmpty()) {
                createDefaultMusicMap();
            }
        } catch (Exception e) {
            System.err.println("Error loading music tracks: " + e.getMessage());
            e.printStackTrace();
            createDefaultMusicMap();
        }
    }

    private void createDefaultMusicMap() {
        try {
            tryCreateSilentMusic("Main Theme");
            tryCreateSilentMusic("Intense Battle");
            tryCreateSilentMusic("Stealth Mission");
            tryCreateSilentMusic("Victory");

            if (!musicTracks.isEmpty()) {
                System.out.println("Created silent placeholder music tracks");
            } else {
                System.err.println("Could not create fallback music tracks");
            }
        } catch (Exception e) {
            System.err.println("Failed to create fallback music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void tryCreateSilentMusic(String trackName) {
        try {
            FileHandle[] possibleFiles = {
                Gdx.files.internal("sounds/effects/click.wav"),
                Gdx.files.internal("click.wav"),
            };

            for (FileHandle file : possibleFiles) {
                if (file.exists()) {
                    Music silentMusic = Gdx.audio.newMusic(file);
                    silentMusic.setVolume(0); // Set to silent
                    musicTracks.put(trackName, silentMusic);
                    return;
                }
            }

            if (!musicTracks.containsKey(trackName)) {
                System.out.println("No suitable fallback sound found for " + trackName);
            }
        } catch (Exception e) {
            System.err.println("Error creating silent music for " + trackName + ": " + e.getMessage());
        }
    }

    private boolean isMusicFile(String fileName) {
        String lowerCase = fileName.toLowerCase();
        return lowerCase.endsWith(".mp3") || lowerCase.endsWith(".wav") || lowerCase.endsWith(".ogg");
    }

    private String getFormattedMusicName(String nameWithoutExtension) {
        String name = nameWithoutExtension.replace('_', ' ');
        StringBuilder formatted = new StringBuilder();
        boolean capitalize = true;

        for (char c : name.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalize = true;
                formatted.append(c);
            } else if (capitalize) {
                formatted.append(Character.toUpperCase(c));
                capitalize = false;
            } else {
                formatted.append(c);
            }
        }

        return formatted.toString();
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public void playMusic(String musicName) {
        if (currentMusic != null) {
            try {
                currentMusic.stop();
            } catch (Exception e) {
                System.err.println("Error stopping current music: " + e.getMessage());
            }
        }

        Music music = musicTracks.get(musicName);
        if (music != null) {
            try {
                currentMusic = music;
                currentMusicName = musicName;
                currentMusic.setLooping(true);
                currentMusic.setVolume(currentMusicVolume);
                currentMusic.play();
                System.out.println("Now playing: " + musicName);
            } catch (Exception e) {
                System.err.println("Error playing music '" + musicName + "': " + e.getMessage());
                currentMusic = null;
            }
        } else {
            System.err.println("Music track not found: " + musicName);
            currentMusic = null;
        }
    }

    public void changeMusic(String musicName) {
        if (musicName == null) {
            return;
        }

        if (currentMusicName != null && currentMusicName.equals(musicName) && currentMusic != null) {
            try {
                if (!currentMusic.isPlaying()) {
                    currentMusic.play();
                    currentMusic.setVolume(currentMusicVolume);
                }
                return;
            } catch (Exception e) {
                System.err.println("Error checking current music: " + e.getMessage());
            }
        }

        playMusic(musicName);
    }

    public void setMusicVolume(float volume) {
        currentMusicVolume = volume;
        if (currentMusic != null) {
            try {
                currentMusic.setVolume(volume);
            } catch (Exception e) {
                System.err.println("Error setting music volume: " + e.getMessage());
            }
        }
    }

    public Sound loadSound(String fileName) {
        try {
            FileHandle file = Gdx.files.internal(fileName);
            if (file.exists()) {
                return Gdx.audio.newSound(file);
            } else {
                System.err.println("Sound file not found: " + fileName);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Could not load sound: " + fileName + " - " + e.getMessage());
            return null;
        }
    }

    public void playSound(Sound sound) {
        if (App.getSettings().isSfxEnabled() && sound != null) {
            try {
                sound.play();
            } catch (Exception e) {
                System.err.println("Error playing sound: " + e.getMessage());
            }
        }
    }

    public String getCurrentMusicName() {
        return currentMusicName;
    }

    public boolean isMusicPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }

    public void pauseMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            try {
                currentMusic.pause();
            } catch (Exception e) {
                System.err.println("Error pausing music: " + e.getMessage());
            }
        }
    }

    public void resumeMusic() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            try {
                currentMusic.play();
                currentMusic.setVolume(currentMusicVolume);
            } catch (Exception e) {
                System.err.println("Error resuming music: " + e.getMessage());
            }
        }
    }

    public String[] getAvailableMusicTracks() {
        return musicTracks.keySet().toArray(new String[0]);
    }

    public void dispose() {
        if (skin != null) {
            try {
                skin.dispose();
            } catch (Exception e) {
                System.err.println("Error disposing skin: " + e.getMessage());
            }
        }

        for (Music music : musicTracks.values()) {
            if (music != null) {
                try {
                    music.dispose();
                } catch (Exception e) {
                    System.err.println("Error disposing music: " + e.getMessage());
                }
            }
        }

        // Fixed disposal of animations - Use a safer approach for TextureRegions
        for (String enemyName : new ArrayList<>(enemyAnimations.keySet())) {
            try {
                // Instead of trying to get the frames directly (which causes ClassCastException),
                // get a frame at a specific time and dispose its texture
                Animation<TextureRegion> animation = enemyAnimations.get(enemyName);
                if (animation != null) {
                    // Get a single frame (first frame) and dispose its texture
                    TextureRegion region = animation.getKeyFrame(0);
                    if (region != null && region.getTexture() != null) {
                        region.getTexture().dispose();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error disposing animation for " + enemyName + ": " + e.getMessage());
            }
        }

        musicTracks.clear();
        currentMusic = null;
        enemyAnimations.clear();

        // Dispose of other textures
        try {
            if (map != null) map.dispose();
            if (enemy1Texture != null) enemy1Texture.dispose();
            if (shanaTex != null) shanaTex.dispose();
            if (diamondTex != null) diamondTex.dispose();
            if (dasherTex != null) dasherTex.dispose();
            if (lilithTex != null) lilithTex.dispose();
            if (scarletTex != null) scarletTex.dispose();
            if (smgTexture != null) smgTexture.dispose();
            if (revolverTexture != null) revolverTexture.dispose();
            if (shotGunTexture != null) shotGunTexture.dispose();
            if (bulletsound != null) bulletsound.dispose();
        } catch (Exception e) {
            System.err.println("Error disposing textures: " + e.getMessage());
        }
    }

    public Texture getShanaTex() {
        return shanaTex;
    }


    public String getSmg() {
        return smg;
    }

    public Texture getSmgTexture() {
        return smgTexture;
    }

    public String getBullet() {
        bulletSound();
        return bullet;
    }

    public Map<String, Music> getMusicTracks() {
        return musicTracks;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public float getCurrentMusicVolume() {
        return currentMusicVolume;
    }

    public Animation<Texture> Idle_animation(String idleName) {
        List<String> idlesString = new ArrayList<>();
        List<Texture> idles = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            idlesString.add("Characters/" + idleName + "/Idle_" + i + ".png");
        }

        for (int i = 0; i < 6; i++) {
            idles.add(new Texture(idlesString.get(i)));
        }
        return new Animation<>(0.1f, idles.get(0),
            idles.get(1), idles.get(2), idles.get(3), idles.get(4), idles.get(5));
    }

    public Animation<Texture> Run_animation(String hero) {
        List<String> idlesString = new ArrayList<>();
        List<Texture> idles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            idlesString.add("Characters/" + hero + "/Run_" + i + ".png");
        }

        for (int i = 0; i < 4; i++) { // Fixed loop bound from 6 to 4
            idles.add(new Texture(idlesString.get(i)));
        }
        return new Animation<>(0.1f, idles.toArray(new Texture[0])); // Fixed array creation
    }

    public Animation<Texture> WalkAnimation(String hero) {
        List<String> idlesString = new ArrayList<>();
        List<Texture> idles = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            idlesString.add("Characters/" + hero + "/Walk_" + i + ".png");
        }

        for (int i = 0; i < 8; i++) { // Fixed loop bound from 6 to 8
            idles.add(new Texture(idlesString.get(i)));
        }
        return new Animation<>(0.1f, idles.toArray(new Texture[0])); // Fixed array creation
    }


    public Animation<TextureRegion> enemyAnimation(String enemy) {
        if (enemyAnimations.containsKey(enemy)) {
            return enemyAnimations.get(enemy);
        }

        // Create a new Array of TextureRegions for the animation
        Array<TextureRegion> enemiesArray = new Array<>(TextureRegion.class);
        int bound = enemy.equals("TreeMonster") ? 3 : 4;

        for (int i = 0; i < bound; i++) {
            Texture texture = new Texture(Gdx.files.internal("Enemies/" + enemy + "_" + i + ".png"));
            TextureRegion region = new TextureRegion(texture);
            enemiesArray.add(region);
        }

        Animation<TextureRegion> animation = new Animation<>(1f, enemiesArray);
        enemyAnimations.put(enemy, animation);
        return animation;
    }


    public Texture getEnemy1Texture() {
        return enemy1Texture;
    }

    public Texture getMap() {
        return map;
    }


    public String getEnemy1() {
        return enemy1;
    }

    public Texture getRevolverTexture() {
        return revolverTexture;
    }

    public Texture getShotGunTexture() {
        return shotGunTexture;
    }

    public Map<String, Animation<TextureRegion>> getEnemyAnimations() {
        return enemyAnimations;
    }

    public Sound getBulletsound() {
        return bulletsound;
    }

    public Texture getDiamondTex() {
        return diamondTex;
    }

    public Texture getDasherTex() {
        return dasherTex;
    }

    public Texture getLilithTex() {
        return lilithTex;
    }

    public Texture getScarletTex() {
        return scarletTex;
    }
}
