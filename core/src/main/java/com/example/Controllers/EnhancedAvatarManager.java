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
    private static final String CUSTOM_AVATAR_DIR = "avatars/custom/";
    private static final String PREDEFINED_AVATAR_DIR = "avatars/predefined/";
    private static final int MAX_AVATAR_SIZE = 512; // Maximum width/height
    private static final int THUMBNAIL_SIZE = 128;
    private static EnhancedAvatarManager instance;
    private Array<String> predefinedAvatars;
    private Map<String, Texture> loadedTextures;
    private Map<String, Long> textureTimestamps;

    private EnhancedAvatarManager() {
        loadedTextures = new HashMap<>();
        textureTimestamps = new HashMap<>();
        initializePredefinedAvatars();
        ensureDirectoriesExist();
    }

    public static EnhancedAvatarManager getInstance() {
        if (instance == null) {
            instance = new EnhancedAvatarManager();
        }
        return instance;
    }

    private void initializePredefinedAvatars() {
        predefinedAvatars = new Array<>();
        // Create default avatars if they don't exist
        createDefaultAvatars();

        for (int i = 1; i <= 10; i++) {
            predefinedAvatars.add(PREDEFINED_AVATAR_DIR + "avatar_" + i + ".png");
        }
    }

    private void createDefaultAvatars() {
        try {
            FileHandle predefinedDir = Gdx.files.local(PREDEFINED_AVATAR_DIR);
            if (!predefinedDir.exists()) {
                predefinedDir.mkdirs();
            }

            // Create 10 different colored default avatars
            Color[] colors = {
                new Color(0.8f, 0.2f, 0.2f, 1f), // Red
                new Color(0.2f, 0.8f, 0.2f, 1f), // Green
                new Color(0.2f, 0.2f, 0.8f, 1f), // Blue
                new Color(0.8f, 0.8f, 0.2f, 1f), // Yellow
                new Color(0.8f, 0.2f, 0.8f, 1f), // Magenta
                new Color(0.2f, 0.8f, 0.8f, 1f), // Cyan
                new Color(0.8f, 0.5f, 0.2f, 1f), // Orange
                new Color(0.5f, 0.2f, 0.8f, 1f), // Purple
                new Color(0.2f, 0.5f, 0.8f, 1f), // Light Blue
                new Color(0.5f, 0.8f, 0.2f, 1f)  // Light Green
            };

            for (int i = 0; i < 10; i++) {
                String avatarPath = PREDEFINED_AVATAR_DIR + "avatar_" + (i + 1) + ".png";
                FileHandle avatarFile = Gdx.files.local(avatarPath);

                if (!avatarFile.exists()) {
                    createDefaultAvatarTexture(avatarFile, colors[i], i + 1);
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating default avatars: " + e.getMessage());
        }
    }

    private void createDefaultAvatarTexture(FileHandle file, Color color, int number) {
        try {
            Pixmap pixmap = new Pixmap(128, 128, Pixmap.Format.RGBA8888);

            // Background circle
            pixmap.setColor(com.badlogic.gdx.graphics.Color.CHARTREUSE);
            pixmap.fillCircle(64, 64, 60);

            // Border
            pixmap.setColor(1f, 1f, 1f, 1f);
            pixmap.drawCircle(64, 64, 60);
            pixmap.drawCircle(64, 64, 58);

            com.badlogic.gdx.graphics.PixmapIO.writePNG(file, pixmap);
//            file.writeBytes(pixmapBytes, false);

            pixmap.dispose();
        } catch (Exception e) {
            System.err.println("Error creating avatar texture: " + e.getMessage());
        }
    }

    private void ensureDirectoriesExist() {
        try {
            FileHandle customDir = Gdx.files.local(CUSTOM_AVATAR_DIR);
            if (!customDir.exists()) {
                customDir.mkdirs();
            }

            FileHandle predefinedDir = Gdx.files.local(PREDEFINED_AVATAR_DIR);
            if (!predefinedDir.exists()) {
                predefinedDir.mkdirs();
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

        // Check cache first
        if (loadedTextures.containsKey(avatarPath)) {
            return loadedTextures.get(avatarPath);
        }

        try {
            Texture texture = loadAvatarTexture(avatarPath);
            if (texture != null) {
                loadedTextures.put(avatarPath, texture);
                textureTimestamps.put(avatarPath, System.currentTimeMillis());
            }
            return texture != null ? texture : getDefaultAvatar();
        } catch (Exception e) {
            System.err.println("Error loading avatar texture: " + e.getMessage());
            return getDefaultAvatar();
        }
    }

    private Texture loadAvatarTexture(String avatarPath) {
        try {
            FileHandle file;
            if (avatarPath.startsWith(CUSTOM_AVATAR_DIR)) {
                file = Gdx.files.local(avatarPath);
            } else {
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
                return null;
            }

            // Validate and resize image if needed
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

    private String getFileExtension(String filePath) {
        int lastDot = filePath.lastIndexOf('.');
        return lastDot > 0 ? filePath.substring(lastDot) : ".png";
    }

    private Texture getDefaultAvatar() {
        String defaultPath = PREDEFINED_AVATAR_DIR + "avatar_1.png";
        if (!loadedTextures.containsKey(defaultPath)) {
            try {
                Texture texture = loadAvatarTexture(defaultPath);
                if (texture == null) {
                    texture = createFallbackTexture();
                }
                loadedTextures.put(defaultPath, texture);
            } catch (Exception e) {
                System.err.println("Error creating default avatar: " + e.getMessage());
                loadedTextures.put(defaultPath, createFallbackTexture());
            }
        }
        return loadedTextures.get(defaultPath);
    }

    private Texture createFallbackTexture() {
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.5f, 0.5f, 0.8f, 1f);
        pixmap.fillCircle(32, 32, 30);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public boolean isValidImageFile(String filePath) {
        if (filePath == null) return false;
        String extension = getFileExtension(filePath).toLowerCase();
        return extension.equals(".png") || extension.equals(".jpg") ||
            extension.equals(".jpeg") || extension.equals(".bmp");
    }

    public void clearCache() {
        for (Texture texture : loadedTextures.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        loadedTextures.clear();
        textureTimestamps.clear();
    }

    @Override
    public void dispose() {
        clearCache();
    }

    // Color class for creating default avatars
    private static class Color {
        float r, g, b, a;

        Color(float r, float g, float b, float a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }
    }
}
