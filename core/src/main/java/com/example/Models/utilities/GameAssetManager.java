package com.example.Models.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.example.Models.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameAssetManager implements Disposable {
    private static GameAssetManager gameAssetManager;
    private static Texture map;

    private final List<Texture> loadedTextures = new ArrayList<>();

    // enemies
    private String treeMonster = "Enemies/TreeMonster_0.png";
    private Texture treeMonsterTexture;
    private Texture eyebatTexture;
    private Texture tentacleTexture;
    private Texture elderTexture;
    private Texture eyeSeed;
    private Texture treeSeed;
    private Texture tentacleSeed;
    private Texture elderSeed;
    private Texture eyebatBullet;

    //characters
    private Texture shanaTex;
    private Texture diamondTex;
    private Texture dasherTex;
    private Texture lilithTex;
    private Texture scarletTex;
    private Texture dasherPortraitTex;
    private Texture scarletPortraitTex;
    private Texture lilithPortraitTex;

    // weapons
    private String smgPath = "Weapons/SMGStill.png";
    private Texture smgTexture;
    private Texture revolverTexture;
    private Texture shotGunTexture;

    //effects
    private Texture ammoIcon;
    private Texture zombieSkull;


    //bullet
    private String bulletPath;
    private Map<String, Animation<TextureRegion>> enemyAnimations = new HashMap<>();
    private Map<String, Texture> textureCache = new HashMap<>();
    private Sound bulletsound;
    private Sound reloadSound;
    private Sound batDeathSound;
    private Sound bigBloodSplashSound;
    private Sound popSound;
    private Sound treeDeath;
    private Skin skin;
    private Map<String, Music> musicTracks;
    private Music currentMusic;
    private String currentMusicName;
    private float currentMusicVolume;
    private Texture lightHaloTexture;
    private Texture death0;
    private Texture death1;
    private Texture death2;
    private Texture death3;

    private Texture damage0;
    private Texture damage1;
    private Texture damage2;

    private GameAssetManager() {
        Texture.setAssetManager(new com.badlogic.gdx.assets.AssetManager());

        try {
            skin = new Skin(Gdx.files.internal("Skin/pixthulhu-ui.json"));
        } catch (Exception e) {
            Gdx.app.error("GameAssetManager", "Error loading skin: " + e.getMessage());
        }

        try {
            map = new Texture(Gdx.files.internal("MapDetails/map.png"));
            loadedTextures.add(map);
        } catch (Exception e) {
            Gdx.app.error("GameAssetManager", "Error loading map: " + e.getMessage());
        }

        loadMainTextures();

        bulletPath = Gdx.files.internal("Bullets/bullet.png").toString();

        try {
            bulletsound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/single_shot.wav"));
        } catch (Exception e) {
            Gdx.app.error("GameAssetManager", "Error loading bullet sound: " + e.getMessage());
        }


        try {
            reloadSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/Shotgun_Reload.wav"));
        } catch (Exception e) {
            Gdx.app.error("GameAssetManager", "Error loading reload sound: " + e.getMessage());
        }

        try {
            batDeathSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/Bat_Death.wav"));
        } catch (Exception e) {
            Gdx.app.error("GameAssetManager", "Error loading bat sound: " + e.getMessage());
        }

        try {
            bigBloodSplashSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/Explosion_Blood.wav"));
        } catch (Exception e) {
            Gdx.app.error("GameAssetManager", "Error loading bat sound: " + e.getMessage());
        }

        try {
            treeDeath = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/Spell_Explosion_Magic.wav"));
        } catch (Exception e) {
            Gdx.app.error("GameAssetManager", "Error loading bat sound: " + e.getMessage());
        }

        try {
            popSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effects/Pop.wav"));
        } catch (Exception e) {
            Gdx.app.error("GameAssetManager", "Error loading bat sound: " + e.getMessage());
        }


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

    private void loadMainTextures() {
        try {
            shanaTex = loadTexture("Characters/Shana/Idle_0.png");
            diamondTex = loadTexture("Characters/Diamond/Idle_0.png");
            dasherTex = loadTexture("Characters/Dasher/Idle_0.png");
            lilithTex = loadTexture("Characters/Lilith/Idle_0.png");
            scarletTex = loadTexture("Characters/Scarlet/Idle_0.png");
            lilithPortraitTex = loadTexture("Characters/Lilith/LilithPortrait.png");
            dasherPortraitTex = loadTexture("Characters/Dasher/DasherPortrait.png");
            scarletPortraitTex = loadTexture("Characters/Scarlet/ScarletPortrait.png");

            treeMonsterTexture = loadTexture(treeMonster);
            eyebatTexture = loadTexture("Enemies/EyeBat_0.png");
            tentacleTexture = loadTexture("Enemies/TentacleMonster_0.png");
            elderTexture = loadTexture("Enemies/Elder_0.png");
            eyeSeed = loadTexture("Enemies/EyeMonsterEye.png");
            treeSeed = loadTexture("Enemies/HeartPickup.png");
            tentacleSeed = loadTexture("Enemies/SoulHeartPickup.png");
            elderSeed = loadTexture("Enemies/DevilDealPickup.png");
            eyebatBullet = loadTexture("Bullets/Icon_DoubleShot.png");
            ammoIcon = loadTexture("effects/AmmoIcon.png");
            zombieSkull = loadTexture("effects/SpiritSkull.png");
            death0 = loadTexture("effects/Explosion/DeathFX_0.png");
            death1 = loadTexture("effects/Explosion/DeathFX_1.png");
            death2 = loadTexture("effects/Explosion/DeathFX_2.png");
            death3 = loadTexture("effects/Explosion/DeathFX_3.png");
            damage0 = loadTexture("effects/Damage/T_CurseFX_0.png");
            damage1 = loadTexture("effects/Damage/T_CurseFX_1.png");
            damage2 = loadTexture("effects/Damage/T_CurseFX_2.png");

            smgTexture = loadTexture(smgPath);
            revolverTexture = loadTexture("Weapons/Revolver.png");
            shotGunTexture = loadTexture("Weapons/Shotgun.png");

        } catch (Exception e) {
            Gdx.app.error("GameAssetManager", "Error loading main textures: " + e.getMessage());
        }
    }

    private Texture loadTexture(String path) {
        if (textureCache.containsKey(path)) {
            return textureCache.get(path);
        }

        try {
            Texture texture = new Texture(Gdx.files.internal(path));
            textureCache.put(path, texture);
            loadedTextures.add(texture);
            return texture;
        } catch (Exception e) {
            Gdx.app.error("GameAssetManager", "Failed to load texture: " + path + " - " + e.getMessage());
            return null;
        }
    }

    public void bulletSound() {
        if (App.getSettings().isSfxEnabled() && bulletsound != null) {
            bulletsound.play();
        }
    }

    public void reloadSound() {
        if (App.getSettings().isSfxEnabled() && reloadSound != null) {
            reloadSound.play();
        }
    }

    public void batDeathSound() {
        if (App.getSettings().isSfxEnabled() && batDeathSound != null) {
            batDeathSound.play();
        }
    }

    public void bigBloodSplashSound() {
        if (App.getSettings().isSfxEnabled() && bigBloodSplashSound != null) {
            bigBloodSplashSound.play();
        }
    }

    public void treeDeathSound() {
        if (App.getSettings().isSfxEnabled() && treeDeath != null) {
            treeDeath.play();
        }
    }

    public void popSound() {
        if (App.getSettings().isSfxEnabled() && popSound != null) {
            popSound.play();
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

    @Override
    public void dispose() {
        if (skin != null) {
            try {
                skin.dispose();
                skin = null;
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
        musicTracks.clear();
        currentMusic = null;

        for (Animation<TextureRegion> animation : enemyAnimations.values()) {
            try {
                if (animation != null) {
                    TextureRegion region = animation.getKeyFrame(0);
                    if (region != null && region.getTexture() != null) {
                        region.getTexture().dispose();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error disposing animation: " + e.getMessage());
            }
        }
        enemyAnimations.clear();

        for (Texture texture : loadedTextures) {
            try {
                if (texture != null) {
                    texture.dispose();
                }
            } catch (Exception e) {
                System.err.println("Error disposing texture: " + e.getMessage());
            }
        }
        loadedTextures.clear();
        textureCache.clear();

        if (bulletsound != null) {
            try {
                bulletsound.dispose();
                bulletsound = null;
            } catch (Exception e) {
                System.err.println("Error disposing bullet sound: " + e.getMessage());
            }
        }

        map = null;
    }

    public Animation<Texture> idleAnimation(String idleName) {
        List<Texture> idles = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String path = "Characters/" + idleName + "/Idle_" + i + ".png";
            idles.add(loadTexture(path));
        }

        return new Animation<>(0.1f, idles.toArray(new Texture[0]));
    }

    public Animation<Texture> runAnimation(String hero) {
        List<Texture> idles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String path = "Characters/" + hero + "/Run_" + i + ".png";
            idles.add(loadTexture(path));
        }

        return new Animation<>(0.1f, idles.toArray(new Texture[0]));
    }

    public Animation<Texture> walkAnimation(String hero) {
        List<Texture> idles = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String path = "Characters/" + hero + "/Walk_" + i + ".png";
            idles.add(loadTexture(path));
        }

        return new Animation<>(0.1f, idles.toArray(new Texture[0]));
    }

    public Animation<Texture> heartAnimation() {
        List<Texture> idles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String path = "effects/HeartAnimation_" + i + ".png";
            idles.add(loadTexture(path));
        }
        return new Animation<>(0.3f, idles.toArray(new Texture[0]));
    }

    public Animation<Texture> blackHeartAnimation() {
        List<Texture> idles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String path = "effects/T_SoulHeartAnimation_" + i + ".png";
            idles.add(loadTexture(path));
        }
        return new Animation<>(0.1f, idles.toArray(new Texture[0]));
    }

    public Animation<Texture> deathAnimation() {
        List<Texture> enemies = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String path = "effects/Explosion/DeathFX_" + i + ".png";
            enemies.add(loadTexture(path));
        }
        return new Animation<>(0.1f, enemies.toArray(new Texture[0]));
    }

    public Animation<Texture> damageAnimation() {
        List<Texture> enemies = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String path = "effects/Damage/T_CurseFX_" + i + ".png";
            enemies.add(loadTexture(path));
        }
        return new Animation<>(0.1f, enemies.toArray(new Texture[0]));
    }

    public Animation<Texture> crowAnimation() {
        List<Texture> idles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String path = "effects/T_Crow_" + i + ".png";
            idles.add(loadTexture(path));
        }
        return new Animation<>(0.1f, idles.toArray(new Texture[0]));
    }


    public Animation<Texture> shotgunAnimation() {
        List<Texture> idles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String path = "effects/T_ShoggothLaser_" + i + ".png";
            idles.add(loadTexture(path));
        }
        return new Animation<>(0.1f, idles.toArray(new Texture[0]));
    }

    public Texture createBarrierTexture(int radius, Color color) {
        Pixmap pixmap = new Pixmap(radius * 2, radius * 2, Pixmap.Format.RGBA8888);

        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();

        pixmap.setColor(color);
        for (int angle = 0; angle < 360; angle++) {
            float x = (float) (radius + (radius - 5) * Math.cos(Math.toRadians(angle)));
            float y = (float) (radius + (radius - 5) * Math.sin(Math.toRadians(angle)));
            pixmap.drawPixel((int) x, (int) y);
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        loadedTextures.add(texture);
        return texture;
    }


    public Animation<TextureRegion> enemyAnimation(String enemy) {
        if (enemyAnimations.containsKey(enemy)) {
            return enemyAnimations.get(enemy);
        }

        Array<TextureRegion> enemiesArray = new Array<>(TextureRegion.class);
        int bound = enemy.equals("TreeMonster") ? 3 : 4;

        for (int i = 0; i < bound; i++) {
            String path = "Enemies/" + enemy + "_" + i + ".png";
            Texture texture = loadTexture(path);
            if (texture != null) {
                TextureRegion region = new TextureRegion(texture);
                enemiesArray.add(region);
            }
        }

        Animation<TextureRegion> animation = new Animation<>(0.3f, enemiesArray);
        enemyAnimations.put(enemy, animation);
        return animation;
    }

    public Texture createLightTexture(int radius, Color centerColor, Color edgeColor) {
        Pixmap pixmap = new Pixmap(radius * 2, radius * 2, Pixmap.Format.RGBA8888);

        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();

        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int centerX = pixmap.getWidth() / 2;
                int centerY = pixmap.getHeight() / 2;
                float distance = Vector2.dst(x, y, centerX, centerY);

                if (distance <= radius) {
                    float alpha = 1f - (distance / radius);
                    Color pixelColor = new Color(
                        centerColor.r * alpha + edgeColor.r * (1 - alpha),
                        centerColor.g * alpha + edgeColor.g * (1 - alpha),
                        centerColor.b * alpha + edgeColor.b * (1 - alpha),
                        alpha * centerColor.a
                    );
                    pixmap.setColor(pixelColor);
                    pixmap.drawPixel(x, y);
                }
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        loadedTextures.add(texture);
        return texture;
    }

    public Texture getLightHaloTexture() {
        if (lightHaloTexture == null) {
            lightHaloTexture = createLightTexture(300, new Color(1f, 0.8f, 0.3f, 0.7f),
                new Color(0.8f, 0.3f, 0.1f, 0f));
        }
        return lightHaloTexture;
    }

    public Texture getTreeMonsterTexture() {
        return treeMonsterTexture;
    }

    public Texture getMap() {
        return map;
    }

    public String getEnemy1() {
        return treeMonster;
    }

    public Texture getShanaTex() {
        return shanaTex;
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

    public String getSmg() {
        return smgPath;
    }

    public Texture getSmgTexture() {
        return smgTexture;
    }

    public String getBullet() {
        bulletSound();
        return bulletPath;
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

    public Map<String, Music> getMusicTracks() {
        return musicTracks;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public float getCurrentMusicVolume() {
        return currentMusicVolume;
    }

    public String getTreeMonster() {
        return treeMonster;
    }

    public Texture getEyebatTexture() {
        return eyebatTexture;
    }

    public Texture getTentacleTexture() {
        return tentacleTexture;
    }

    public Texture getElderTexture() {
        return elderTexture;
    }

    public Texture getEyeSeed() {
        return eyeSeed;
    }

    public Texture getTreeSeed() {
        return treeSeed;
    }

    public Texture getTentacleSeed() {
        return tentacleSeed;
    }

    public Texture getElderSeed() {
        return elderSeed;
    }

    public Texture getEyebatBullet() {
        return eyebatBullet;
    }

    public String getBulletTexturePath() {
        return bulletPath;
    }

    public Texture getAmmoIcon() {
        return ammoIcon;
    }

    public Texture getZombieSkull() {
        return zombieSkull;
    }

    public Texture getDasherPortraitTex() {
        return dasherPortraitTex;
    }

    public Texture getScarletPortraitTex() {
        return scarletPortraitTex;
    }

    public Texture getLilithPortraitTex() {
        return lilithPortraitTex;
    }

}
