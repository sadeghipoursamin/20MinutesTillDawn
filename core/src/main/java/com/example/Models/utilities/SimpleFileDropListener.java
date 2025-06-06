package com.example.Models.utilities;

import com.badlogic.gdx.Gdx;

import java.util.function.Consumer;

/**
 * Simple file drop listener that works with LibGDX's built-in file drop support
 */
public class SimpleFileDropListener {

    private static Consumer<String> currentCallback;

    /**
     * Enable file drop listening with a callback
     */
    public static void enableFileDrop(Consumer<String> onFileDropped) {
        currentCallback = onFileDropped;

        // Note: This is a simplified version. For actual implementation,
        // you would need to use platform-specific code or libraries
        System.out.println("File drop listener enabled (simplified version)");
    }

    /**
     * Disable file drop listening
     */
    public static void disableFileDrop() {
        currentCallback = null;
        System.out.println("File drop listener disabled");
    }

    /**
     * Handle a dropped file (call this from platform-specific code)
     */
    public static void handleDroppedFile(String filePath) {
        if (currentCallback != null) {
            // Execute on LibGDX thread
            Gdx.app.postRunnable(() -> {
                currentCallback.accept(filePath);
            });
        }
    }

    /**
     * Check if a file is a valid image
     */
    public static boolean isValidImageFile(String filePath) {
        if (filePath == null) return false;
        String lower = filePath.toLowerCase();
        return lower.endsWith(".png") || lower.endsWith(".jpg") ||
            lower.endsWith(".jpeg") || lower.endsWith(".bmp");
    }
}
