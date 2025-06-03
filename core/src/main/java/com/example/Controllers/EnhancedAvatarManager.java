package com.example.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EnhancedAvatarManager implements Disposable {
    private static final String PRESET_AVATAR_DIR = "avatars/";  // Assets folder
    private static final String CUSTOM_AVATAR_DIR = "avatars/custom/";  // Local storage
    private static final int MAX_AVATAR_SIZE = 512;
    private static final int THUMBNAIL_SIZE = 128;

    private static EnhancedAvatarManager instance;
    private Array<String> availableAvatars;
    private Map<String, Texture> loadedTextures;
    private Map<String, String> avatarDisplayNames;

    private EnhancedAvatarManager() {
        loadedTextures = new HashMap<>();
        avatarDisplayNames = new HashMap<>();
        initializeAvatars();
        ensureCustomDirectoryExists();
    }

    public static EnhancedAvatarManager getInstance() {
        if (instance == null) {
            instance = new EnhancedAvatarManager();
        }
        return instance;
    }

    private void initializeAvatars() {
        availableAvatars = new Array<>();

        try {
            // Load preset avatars from assets folder
            FileHandle avatarDir = Gdx.files.internal(PRESET_AVATAR_DIR);
            if (avatarDir.exists() && avatarDir.isDirectory()) {
                FileHandle[] files = avatarDir.list();
                for (FileHandle file : files) {
                    if (isImageFile(file.name())) {
                        String avatarPath = PRESET_AVATAR_DIR + file.name();
                        availableAvatars.add(avatarPath);

                        String displayName = createDisplayName(file.nameWithoutExtension());
                        avatarDisplayNames.put(avatarPath, displayName);

                        System.out.println("Found preset avatar: " + avatarPath);
                    }
                }
            }

            // Load custom avatars from local storage
            loadCustomAvatars();

            // Create default avatars if none found
            if (availableAvatars.size == 0) {
                createDefaultAvatars();
            }

            System.out.println("Initialized " + availableAvatars.size + " avatars");

        } catch (Exception e) {
            System.err.println("Error initializing avatars: " + e.getMessage());
            createDefaultAvatars();
        }
    }

    private void loadCustomAvatars() {
        try {
            FileHandle customDir = Gdx.files.local(CUSTOM_AVATAR_DIR);
            if (customDir.exists() && customDir.isDirectory()) {
                FileHandle[] files = customDir.list();
                for (FileHandle file : files) {
                    if (isImageFile(file.name())) {
                        String avatarPath = CUSTOM_AVATAR_DIR + file.name();
                        availableAvatars.add(avatarPath);

                        String displayName = "Custom: " + createDisplayName(file.nameWithoutExtension());
                        avatarDisplayNames.put(avatarPath, displayName);

                        System.out.println("Found custom avatar: " + avatarPath);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading custom avatars: " + e.getMessage());
        }
    }

    private void ensureCustomDirectoryExists() {
        try {
            FileHandle customDir = Gdx.files.local(CUSTOM_AVATAR_DIR);
            if (!customDir.exists()) {
                customDir.mkdirs();
                System.out.println("Created custom avatar directory: " + customDir.path());
            }
        } catch (Exception e) {
            System.err.println("Error creating custom avatar directory: " + e.getMessage());
        }
    }

    private void createDefaultAvatars() {
        // Create procedural default avatars
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
            FileHandle file;

            if (avatarPath.startsWith(CUSTOM_AVATAR_DIR)) {
                // Custom avatar - load from local storage
                file = Gdx.files.local(avatarPath);
            } else if (avatarPath.startsWith(PRESET_AVATAR_DIR)) {
                // Preset avatar - load from assets
                file = Gdx.files.internal(avatarPath);
            } else if (avatarPath.startsWith("default_")) {
                // Procedural default avatar
                return createDefaultAvatarTexture(avatarPath);
            } else {
                // Try internal first, then local
                file = Gdx.files.internal(avatarPath);
                if (!file.exists()) {
                    file = Gdx.files.local(avatarPath);
                }
            }

            if (file.exists()) {
                return new Texture(file);
            }

        } catch (Exception e) {
            System.err.println("Error loading texture from path: " + avatarPath + " - " + e.getMessage());
        }
        return null;
    }

    public String saveCustomAvatar(String sourcePath) {
        try {
            File sourceFile = new File(sourcePath);
            if (!sourceFile.exists()) {
                System.err.println("Source file does not exist: " + sourcePath);
                return null;
            }

            // Process and resize image if needed
            BufferedImage processedImage = processImage(sourceFile);
            if (processedImage == null) {
                return null;
            }

            // Generate unique filename
            String fileName = "custom_" + System.currentTimeMillis() + ".png";
            String targetPath = CUSTOM_AVATAR_DIR + fileName;

            // Save processed image
            FileHandle targetFile = Gdx.files.local(targetPath);
            File outputFile = targetFile.file();

            // Ensure parent directory exists
            outputFile.getParentFile().mkdirs();

            // Save as PNG
            ImageIO.write(processedImage, "PNG", outputFile);

            // Add to available avatars
            availableAvatars.add(targetPath);
            avatarDisplayNames.put(targetPath, "Custom: " + fileName);

            System.out.println("Saved custom avatar: " + targetPath);
            return targetPath;

        } catch (Exception e) {
            System.err.println("Error saving custom avatar: " + e.getMessage());
            return null;
        }
    }

    private BufferedImage processImage(File sourceFile) {
        try {
            BufferedImage originalImage = ImageIO.read(sourceFile);
            if (originalImage == null) {
                return null;
            }

            // Calculate new dimensions maintaining aspect ratio
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            int newWidth, newHeight;

            if (width > height) {
                newWidth = Math.min(width, MAX_AVATAR_SIZE);
                newHeight = (height * newWidth) / width;
            } else {
                newHeight = Math.min(height, MAX_AVATAR_SIZE);
                newWidth = (width * newHeight) / height;
            }

            // Create resized image
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g2d = resizedImage.createGraphics();

            // Enable anti-aliasing for better quality
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            return resizedImage;

        } catch (Exception e) {
            System.err.println("Error processing image: " + e.getMessage());
            return null;
        }
    }

    private Texture createDefaultAvatarTexture(String avatarPath) {
        // Create a simple colored circle as default avatar
        int size = 128;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);

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

    public boolean isValidImageFile(String filePath) {
        if (filePath == null) return false;
        String extension = getFileExtension(filePath).toLowerCase();
        return extension.equals(".png") || extension.equals(".jpg") ||
            extension.equals(".jpeg") || extension.equals(".bmp");
    }

    private String getFileExtension(String filePath) {
        int lastDot = filePath.lastIndexOf('.');
        return lastDot > 0 ? filePath.substring(lastDot) : ".png";
    }

    public void clearCache() {
        for (Texture texture : loadedTextures.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        loadedTextures.clear();
    }

    @Override
    public void dispose() {
        clearCache();
        avatarDisplayNames.clear();
        availableAvatars.clear();
    }
}
