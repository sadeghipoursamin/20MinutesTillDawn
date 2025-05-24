package com.example.Models.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class AvatarManager implements Disposable {
    private static final String CUSTOM_AVATAR_DIR = "avatars/custom/";
    private static final String PREDEFINED_AVATAR_DIR = "avatars/predefined/";
    private static AvatarManager instance;
    private Array<String> predefinedAvatars;
    private Map<String, Texture> loadedTextures;

    private AvatarManager() {
        loadedTextures = new HashMap<>();
        initializePredefinedAvatars();
        ensureDirectoriesExist();
    }

    public static AvatarManager getInstance() {
        if (instance == null) {
            instance = new AvatarManager();
        }
        return instance;
    }

    private void initializePredefinedAvatars() {
        predefinedAvatars = new Array<>();
        // Add predefined avatar paths
        for (int i = 1; i <= 10; i++) {
            predefinedAvatars.add(PREDEFINED_AVATAR_DIR + "avatar_" + i + ".png");
        }
    }

    private void ensureDirectoriesExist() {
        try {
            FileHandle customDir = Gdx.files.local(CUSTOM_AVATAR_DIR);
            if (!customDir.exists()) {
                customDir.mkdirs();
            }
        } catch (Exception e) {
            System.err.println("Error creating avatar directories: " + e.getMessage());
        }
    }

    public Array<String> getPredefinedAvatars() {
        return new Array<>(predefinedAvatars);
    }

    public Texture getAvatarTexture(String avatarPath) {
        if (avatarPath == null || avatarPath.isEmpty()) {
            return getDefaultAvatar();
        }

        if (loadedTextures.containsKey(avatarPath)) {
            return loadedTextures.get(avatarPath);
        }

        try {
            Texture texture;
            if (avatarPath.startsWith(CUSTOM_AVATAR_DIR)) {
                FileHandle file = Gdx.files.local(avatarPath);
                if (file.exists()) {
                    texture = new Texture(file);
                } else {
                    texture = getDefaultAvatar();
                }
            } else {
                FileHandle file = Gdx.files.internal(avatarPath);
                if (file.exists()) {
                    texture = new Texture(file);
                } else {
                    texture = getDefaultAvatar();
                }
            }

            loadedTextures.put(avatarPath, texture);
            return texture;
        } catch (Exception e) {
            System.err.println("Error loading avatar texture: " + e.getMessage());
            return getDefaultAvatar();
        }
    }

    public String saveCustomAvatar(String sourcePath) {
        try {
            File sourceFile = new File(sourcePath);
            if (!sourceFile.exists()) {
                return null;
            }

            // Generate unique filename
            String fileName = "custom_" + System.currentTimeMillis() + getFileExtension(sourcePath);
            String targetPath = CUSTOM_AVATAR_DIR + fileName;

            FileHandle targetFile = Gdx.files.local(targetPath);
            Files.copy(sourceFile.toPath(),
                new File(targetFile.file().getAbsolutePath()).toPath(),
                StandardCopyOption.REPLACE_EXISTING);

            return targetPath;
        } catch (IOException e) {
            System.err.println("Error saving custom avatar: " + e.getMessage());
            return null;
        }
    }

    private String getFileExtension(String filePath) {
        int lastDot = filePath.lastIndexOf('.');
        return lastDot > 0 ? filePath.substring(lastDot) : ".png";
    }

    private Texture getDefaultAvatar() {
        String defaultPath = PREDEFINED_AVATAR_DIR + "avatar_1.png";
        if (!loadedTextures.containsKey(defaultPath)) {
            try {
                // Create a simple default texture if file doesn't exist
                loadedTextures.put(defaultPath, createDefaultTexture());
            } catch (Exception e) {
                System.err.println("Error creating default avatar: " + e.getMessage());
            }
        }
        return loadedTextures.get(defaultPath);
    }

    private Texture createDefaultTexture() {
        // Create a simple colored texture as fallback
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(64, 64, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(0.5f, 0.5f, 0.8f, 1f);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public boolean isValidImageFile(String filePath) {
        String extension = getFileExtension(filePath).toLowerCase();
        return extension.equals(".png") || extension.equals(".jpg") ||
            extension.equals(".jpeg") || extension.equals(".bmp");
    }

    @Override
    public void dispose() {
        for (Texture texture : loadedTextures.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        loadedTextures.clear();
    }
}
