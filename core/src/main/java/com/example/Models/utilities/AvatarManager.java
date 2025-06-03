package com.example.Models.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class AvatarManager implements Disposable {
    private static final String AVATAR_DIR = "avatars/";
    private static AvatarManager instance;
    private Array<String> availableAvatars;
    private Map<String, Texture> loadedTextures;
    private Map<String, String> avatarDisplayNames;

    private AvatarManager() {
        loadedTextures = new HashMap<>();
        avatarDisplayNames = new HashMap<>();
        initializeAvatars();
    }

    public static AvatarManager getInstance() {
        if (instance == null) {
            instance = new AvatarManager();
        }
        return instance;
    }

    private void initializeAvatars() {
        availableAvatars = new Array<>();

        try {
            // Check if avatars directory exists
            FileHandle avatarDir = Gdx.files.internal(AVATAR_DIR);
            if (avatarDir.exists() && avatarDir.isDirectory()) {
                // Load all image files from the avatars directory
                FileHandle[] files = avatarDir.list();
                for (FileHandle file : files) {
                    if (isImageFile(file.name())) {
                        String avatarPath = AVATAR_DIR + file.name();
                        availableAvatars.add(avatarPath);

                        // Create display name from filename
                        String displayName = createDisplayName(file.nameWithoutExtension());
                        avatarDisplayNames.put(avatarPath, displayName);

                        System.out.println("Found avatar: " + avatarPath + " (" + displayName + ")");
                    }
                }
            }

            // If no avatars found, create default ones
            if (availableAvatars.size == 0) {
                createDefaultAvatars();
            }

            System.out.println("Initialized " + availableAvatars.size + " avatars");

        } catch (Exception e) {
            System.err.println("Error initializing avatars: " + e.getMessage());
            createDefaultAvatars();
        }
    }

    private void createDefaultAvatars() {
        // Create some default avatar entries (these would be fallback colored circles)
        String[] defaultNames = {"Knight", "Warrior", "Mage", "Archer", "Rogue"};

        for (int i = 0; i < defaultNames.length; i++) {
            String avatarPath = "default_" + i;
            availableAvatars.add(avatarPath);
            avatarDisplayNames.put(avatarPath, defaultNames[i]);
        }
    }

    private boolean isImageFile(String fileName) {
        String extension = fileName.toLowerCase();
        return extension.endsWith(".png") ||
            extension.endsWith(".jpg") ||
            extension.endsWith(".jpeg") ||
            extension.endsWith(".bmp");
    }

    private String createDisplayName(String filename) {
        // Convert filename to display name (e.g., "knight_avatar" -> "Knight Avatar")
        String name = filename.replace("_", " ").replace("-", " ");
        StringBuilder displayName = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : name.toCharArray()) {
            if (Character.isWhitespace(c)) {
                displayName.append(c);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                displayName.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                displayName.append(Character.toLowerCase(c));
            }
        }

        return displayName.toString().trim();
    }

    public Array<String> getAvailableAvatars() {
        return new Array<>(availableAvatars);
    }

    public String getAvatarDisplayName(String avatarPath) {
        return avatarDisplayNames.getOrDefault(avatarPath, "Unknown Avatar");
    }

    public Texture getAvatarTexture(String avatarPath) {
        if (avatarPath == null || avatarPath.isEmpty()) {
            return getDefaultAvatarTexture();
        }

        // Check cache first
        if (loadedTextures.containsKey(avatarPath)) {
            return loadedTextures.get(avatarPath);
        }

        try {
            Texture texture = loadAvatarTexture(avatarPath);
            if (texture != null) {
                loadedTextures.put(avatarPath, texture);
                return texture;
            }
        } catch (Exception e) {
            System.err.println("Error loading avatar texture: " + e.getMessage());
        }

        return getDefaultAvatarTexture();
    }

    private Texture loadAvatarTexture(String avatarPath) {
        try {
            // Try to load from internal files first
            FileHandle file = Gdx.files.internal(avatarPath);
            if (file.exists()) {
                return new Texture(file);
            }

            // If it's a default avatar, create a procedural one
            if (avatarPath.startsWith("default_")) {
                return createDefaultAvatarTexture(avatarPath);
            }

        } catch (Exception e) {
            System.err.println("Error loading texture from path: " + avatarPath + " - " + e.getMessage());
        }
        return null;
    }

    private Texture createDefaultAvatarTexture(String avatarPath) {
        // Create a simple colored circle as default avatar
        int size = 128;
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(size, size,
            com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);

        // Different colors for different default avatars
        com.badlogic.gdx.graphics.Color color = getDefaultAvatarColor(avatarPath);
        pixmap.setColor(color);
        pixmap.fillCircle(size / 2, size / 2, size / 2 - 4);

        // Add border
        pixmap.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        pixmap.drawCircle(size / 2, size / 2, size / 2 - 2);
        pixmap.drawCircle(size / 2, size / 2, size / 2 - 4);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }

    private com.badlogic.gdx.graphics.Color getDefaultAvatarColor(String avatarPath) {
        // Generate different colors based on avatar path
        switch (avatarPath) {
            case "default_0":
                return com.badlogic.gdx.graphics.Color.BLUE;
            case "default_1":
                return com.badlogic.gdx.graphics.Color.RED;
            case "default_2":
                return com.badlogic.gdx.graphics.Color.GREEN;
            case "default_3":
                return com.badlogic.gdx.graphics.Color.YELLOW;
            case "default_4":
                return com.badlogic.gdx.graphics.Color.PURPLE;
            default:
                return com.badlogic.gdx.graphics.Color.GRAY;
        }
    }

    private Texture getDefaultAvatarTexture() {
        String defaultPath = "default_0";
        if (!loadedTextures.containsKey(defaultPath)) {
            Texture texture = createDefaultAvatarTexture(defaultPath);
            loadedTextures.put(defaultPath, texture);
        }
        return loadedTextures.get(defaultPath);
    }

    public String getDefaultAvatarPath() {
        if (availableAvatars.size > 0) {
            return availableAvatars.get(0);
        }
        return "default_0";
    }

    public boolean isValidAvatar(String avatarPath) {
        return availableAvatars.contains(avatarPath, false);
    }

    @Override
    public void dispose() {
        for (Texture texture : loadedTextures.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        loadedTextures.clear();
        avatarDisplayNames.clear();
        availableAvatars.clear();
    }
}
