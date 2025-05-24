package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.utilities.AvatarManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.function.Consumer;

public class AvatarSelectionWindow extends Window {
    private User user;
    private Consumer<String> onAvatarSelected;
    private Table predefinedAvatarsTable;
    private Image currentAvatarImage;
    private Label statusLabel;
    private TextButton selectFileButton;
    private TextButton confirmButton;
    private TextButton cancelButton;
    private String selectedAvatarPath;
    private DragAndDrop dragAndDrop;

    public AvatarSelectionWindow(Skin skin, User user, Consumer<String> onAvatarSelected) {
        super("Select Avatar", skin);
        this.user = user;
        this.onAvatarSelected = onAvatarSelected;
        this.selectedAvatarPath = user.getAvatarPath();

        initializeUI(skin);
        setupDragAndDrop();
        setupListeners();

        this.setSize(800, 600);
        this.setPosition(
            (Gdx.graphics.getWidth() - 800) / 2f,
            (Gdx.graphics.getHeight() - 600) / 2f
        );
        this.setModal(true);
        this.setMovable(true);
    }

    private void initializeUI(Skin skin) {
        // Current avatar display
        Label currentLabel = new Label("Current Avatar:", skin);
        currentAvatarImage = new Image();
        updateCurrentAvatarImage();

        // Predefined avatars section
        Label predefinedLabel = new Label("Choose from Predefined Avatars:", skin);
        predefinedLabel.setColor(Color.WHITE);

        predefinedAvatarsTable = new Table();
        createPredefinedAvatarsGrid(skin);

        ScrollPane predefinedScrollPane = new ScrollPane(predefinedAvatarsTable, skin);
        predefinedScrollPane.setScrollingDisabled(false, true);

        // Custom avatar section
        Label customLabel = new Label("Or Upload Custom Avatar:", skin);
        customLabel.setColor(Color.WHITE);

        selectFileButton = new TextButton("Browse Files", skin);
        selectFileButton.setColor(Color.CYAN);

        // Drag and drop area
        Label dragDropLabel = new Label("Or drag and drop an image file here", skin);
        dragDropLabel.setColor(Color.YELLOW);

        Table dragDropArea = new Table(skin);
        dragDropArea.setBackground("default-round");
        dragDropArea.add(dragDropLabel).pad(20);

        // Status and buttons
        statusLabel = new Label("", skin);
        statusLabel.setColor(Color.GREEN);

        confirmButton = new TextButton("Confirm", skin);
        confirmButton.setColor(Color.GREEN);

        cancelButton = new TextButton("Cancel", skin);
        cancelButton.setColor(Color.RED);

        // Layout
        this.add(currentLabel).left().padBottom(10);
        this.row();
        this.add(currentAvatarImage).size(100, 100).padBottom(20);
        this.row();

        this.add(predefinedLabel).left().padBottom(10);
        this.row();
        this.add(predefinedScrollPane).size(700, 200).padBottom(20);
        this.row();

        this.add(customLabel).left().padBottom(10);
        this.row();
        this.add(selectFileButton).width(200).height(50).padBottom(10);
        this.row();

        this.add(dragDropArea).size(400, 80).padBottom(20);
        this.row();

        this.add(statusLabel).padBottom(20);
        this.row();

        Table buttonTable = new Table();
        buttonTable.add(confirmButton).width(150).height(50).pad(10);
        buttonTable.add(cancelButton).width(150).height(50).pad(10);

        this.add(buttonTable);
    }

    private void createPredefinedAvatarsGrid(Skin skin) {
        Array<String> predefinedAvatars = AvatarManager.getInstance().getPredefinedAvatars();
        int columns = 5;
        int currentColumn = 0;

        for (String avatarPath : predefinedAvatars) {
            Texture avatarTexture = AvatarManager.getInstance().getAvatarTexture(avatarPath);
            ImageButton avatarButton = new ImageButton(new TextureRegionDrawable(avatarTexture));

            avatarButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Main.playSound();
                    selectPredefinedAvatar(avatarPath);
                }
            });

            predefinedAvatarsTable.add(avatarButton).size(80, 80).pad(5);

            currentColumn++;
            if (currentColumn >= columns) {
                predefinedAvatarsTable.row();
                currentColumn = 0;
            }
        }
    }

    private void setupDragAndDrop() {
        dragAndDrop = new DragAndDrop();

        // Note: LibGDX doesn't have native drag-and-drop from OS
        // This is a placeholder for the drag-and-drop functionality
        // In a real implementation, you'd need to use native libraries
        // or implement file watching
    }

    private void setupListeners() {
        selectFileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                openFileChooser();
            }
        });

        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                confirmSelection();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                close();
            }
        });
    }

    private void selectPredefinedAvatar(String avatarPath) {
        selectedAvatarPath = avatarPath;
        updateCurrentAvatarImage();
        statusLabel.setText("Selected predefined avatar");
        statusLabel.setColor(Color.GREEN);
    }

    private void openFileChooser() {
        // This needs to run on a separate thread to avoid blocking the render thread
        Thread fileChooserThread = new Thread(() -> {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter(
                    "Image files", "jpg", "jpeg", "png", "bmp"));

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();

                    // Update UI on the main thread
                    Gdx.app.postRunnable(() -> {
                        handleCustomAvatarSelection(filePath);
                    });
                }
            } catch (Exception e) {
                System.err.println("Error opening file chooser: " + e.getMessage());
                Gdx.app.postRunnable(() -> {
                    statusLabel.setText("Error opening file chooser");
                    statusLabel.setColor(Color.RED);
                });
            }
        });

        fileChooserThread.start();
    }

    private void handleCustomAvatarSelection(String filePath) {
        if (!AvatarManager.getInstance().isValidImageFile(filePath)) {
            statusLabel.setText("Invalid image file format");
            statusLabel.setColor(Color.RED);
            return;
        }

        String savedPath = AvatarManager.getInstance().saveCustomAvatar(filePath);
        if (savedPath != null) {
            selectedAvatarPath = savedPath;
            updateCurrentAvatarImage();
            statusLabel.setText("Custom avatar loaded successfully");
            statusLabel.setColor(Color.GREEN);
        } else {
            statusLabel.setText("Failed to save custom avatar");
            statusLabel.setColor(Color.RED);
        }
    }

    private void updateCurrentAvatarImage() {
        if (selectedAvatarPath != null) {
            Texture avatarTexture = AvatarManager.getInstance().getAvatarTexture(selectedAvatarPath);
            currentAvatarImage.setDrawable(new TextureRegionDrawable(avatarTexture));
        }
    }

    private void confirmSelection() {
        if (selectedAvatarPath != null) {
            user.setAvatarPath(selectedAvatarPath);
            user.setCustomAvatar(!selectedAvatarPath.contains("predefined"));

            if (onAvatarSelected != null) {
                onAvatarSelected.accept(selectedAvatarPath);
            }

            // Save user data
            App.save();

            statusLabel.setText("Avatar updated successfully!");
            statusLabel.setColor(Color.GREEN);

            // Close after a short delay
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                @Override
                public void run() {
                    close();
                }
            }, 1.0f);
        } else {
            statusLabel.setText("Please select an avatar first");
            statusLabel.setColor(Color.RED);
        }
    }

    private void close() {
        this.remove();
    }
}
