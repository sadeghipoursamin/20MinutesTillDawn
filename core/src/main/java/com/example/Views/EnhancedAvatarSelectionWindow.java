package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.example.Controllers.EnhancedAvatarManager;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.utilities.VisUIFileChooser;

import java.util.function.Consumer;

public class EnhancedAvatarSelectionWindow extends Window {
    private User user;
    private Consumer<String> onAvatarSelected;

    // UI Components
    private Table mainTable;
    private Table presetAvatarsTable;
    private ScrollPane avatarScrollPane;
    private Image currentAvatarImage;
    private Label currentAvatarLabel;
    private Label statusLabel;

    // Buttons
    private TextButton selectFileButton;
    private TextButton browseAdvancedButton;
    private TextButton confirmButton;
    private TextButton cancelButton;

    // Avatar selection
    private String selectedAvatarPath;
    private Array<ImageButton> avatarButtons;
    private DragAndDrop dragAndDrop;

    // Drop zone for drag and drop
    private Container<Label> dropZone;
    private Label dropZoneLabel;

    // Custom drawables
    private TextureRegionDrawable normalBackground;
    private TextureRegionDrawable highlightBackground;

    public EnhancedAvatarSelectionWindow(Skin skin, User user, Consumer<String> onAvatarSelected) {
        super("Choose Avatar", skin);
        this.user = user;
        this.onAvatarSelected = onAvatarSelected;
        this.selectedAvatarPath = user.getAvatarPath();
        this.avatarButtons = new Array<>();
        this.dragAndDrop = new DragAndDrop();

        // Initialize VisUI
        VisUIFileChooser.initializeVisUI();

        // Initialize with current avatar if null
        if (this.selectedAvatarPath == null || this.selectedAvatarPath.isEmpty()) {
            this.selectedAvatarPath = EnhancedAvatarManager.getInstance().getDefaultAvatarPath();
        }

        createCustomDrawables();
        initializeUI(skin);
        setupDragAndDrop();
        setupListeners();

        this.setSize(1000, 1000);
        this.setPosition(
            (Gdx.graphics.getWidth() - 1000) / 2f,
            (Gdx.graphics.getHeight() - 700) / 2f
        );
        this.setModal(true);
        this.setMovable(true);
    }

    private void createCustomDrawables() {
        // Create normal background
        Pixmap normalPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        normalPixmap.setColor(0.2f, 0.2f, 0.3f, 0.8f);
        normalPixmap.fill();
        Texture normalTexture = new Texture(normalPixmap);
        normalBackground = new TextureRegionDrawable(normalTexture);
        normalPixmap.dispose();

        // Create highlight background
        Pixmap highlightPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        highlightPixmap.setColor(0.3f, 0.5f, 0.8f, 0.9f);
        highlightPixmap.fill();
        Texture highlightTexture = new Texture(highlightPixmap);
        highlightBackground = new TextureRegionDrawable(highlightTexture);
        highlightPixmap.dispose();
    }

    private void initializeUI(Skin skin) {
        mainTable = new Table();

        // Title section
        Label titleLabel = new Label("Choose an Avatar", skin, "title");
        titleLabel.setColor(Color.CYAN);

        // Current avatar display
        Label currentLabel = new Label("Current Avatar:", skin);
        currentLabel.setColor(Color.WHITE);

        currentAvatarImage = new Image();
        currentAvatarLabel = new Label("", skin);
        currentAvatarLabel.setColor(Color.YELLOW);
        updateCurrentAvatarDisplay();

        // Preset avatars section
        createPresetAvatarsSection(skin);

        // Status and buttons
        statusLabel = new Label("Choose an avatar", skin);
        statusLabel.setColor(Color.LIGHT_GRAY);

        confirmButton = new TextButton("Confirm", skin);
        confirmButton.setColor(Color.GREEN);

        cancelButton = new TextButton("Cancel", skin);
        cancelButton.setColor(Color.RED);

        // Layout
        mainTable.add(titleLabel).colspan(3).center().padBottom(20);
        mainTable.row();

        // Current avatar section
        Table currentSection = new Table();
        currentSection.add(currentLabel).padBottom(10);
        currentSection.row();
        currentSection.add(currentAvatarImage).size(120, 120).padBottom(10);
        currentSection.row();
        currentSection.add(currentAvatarLabel).center();

        mainTable.add(currentSection).colspan(3).center().padBottom(25);
        mainTable.row();

        // Selection methods
        mainTable.add(createSelectionMethodsPanel(skin)).colspan(3).fillX().padBottom(20);
        mainTable.row();

        // Preset avatars
        Label presetLabel = new Label("Predefined Avatars:", skin);
        presetLabel.setColor(Color.WHITE);
        mainTable.add(presetLabel).colspan(3).left().padBottom(10);
        mainTable.row();

        avatarScrollPane = new ScrollPane(presetAvatarsTable, skin);
        avatarScrollPane.setScrollingDisabled(false, false);
        avatarScrollPane.setFadeScrollBars(false);
        mainTable.add(avatarScrollPane).size(800, 250).colspan(3).padBottom(25);
        mainTable.row();

        mainTable.add(statusLabel).colspan(3).center().padBottom(20);
        mainTable.row();

        // Buttons
        Table buttonTable = new Table();
        buttonTable.add(confirmButton).width(150).height(50).pad(10);
        buttonTable.add(cancelButton).width(150).height(50).pad(10);
        mainTable.add(buttonTable).colspan(3).center();

        this.add(mainTable);
    }

    private Table createSelectionMethodsPanel(Skin skin) {
        Table methodsPanel = new Table();
        methodsPanel.setBackground(normalBackground);

        Label methodsTitle = new Label("Ways to Choose an Avatar:", skin);
        methodsTitle.setColor(Color.CYAN);
        methodsPanel.add(methodsTitle).colspan(3).center().padBottom(15);
        methodsPanel.row();

        // VisUI file chooser button
        selectFileButton = new TextButton("Select File (VisUI)", skin);
        selectFileButton.setColor(Color.BLUE);
        methodsPanel.add(selectFileButton).width(180).height(60).pad(10);

        // Advanced VisUI browser button
        browseAdvancedButton = new TextButton("Advanced Browser", skin);
        browseAdvancedButton.setColor(Color.PURPLE);
        methodsPanel.add(browseAdvancedButton).width(180).height(60).pad(10);

        // Drag and drop zone
        createDragDropZone(skin);
        methodsPanel.add(dropZone).width(300).height(100).pad(10);

        return methodsPanel;
    }

    private void createDragDropZone(Skin skin) {
        dropZoneLabel = new Label("Drag and Drop a File\n(or click to browse)", skin);
        dropZoneLabel.setColor(Color.LIGHT_GRAY);
        dropZoneLabel.setAlignment(1); // Center alignment

        dropZone = new Container<>(dropZoneLabel);
        dropZone.setBackground(normalBackground);
        dropZone.fill();

        // Visual feedback for drag over and click functionality
        dropZone.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dropZone.setBackground(highlightBackground);
                dropZoneLabel.setColor(Color.YELLOW);
                dropZoneLabel.setText("Opening file chooser...");

                // Trigger VisUI file selection on click
                openVisUIFileChooser();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resetDropZone();
            }
        });
    }

    private void resetDropZone() {
        dropZone.setBackground(normalBackground);
        dropZoneLabel.setColor(Color.LIGHT_GRAY);
        dropZoneLabel.setText("Drag and Drop a File\n(or click to browse)");
    }

    private void createPresetAvatarsSection(Skin skin) {
        presetAvatarsTable = new Table();
        Array<String> availableAvatars = EnhancedAvatarManager.getInstance().getAvailableAvatars();

        int columns = 4;
        int currentColumn = 0;

        for (int i = 0; i < availableAvatars.size; i++) {
            String avatarPath = availableAvatars.get(i);

            try {
                Texture avatarTexture = EnhancedAvatarManager.getInstance().getAvatarTexture(avatarPath);
                ImageButton avatarButton = new ImageButton(new TextureRegionDrawable(avatarTexture));
                avatarButton.getImage().setScaling(com.badlogic.gdx.utils.Scaling.fit);

                // Highlight if this is the currently selected avatar
                if (avatarPath.equals(selectedAvatarPath)) {
                    avatarButton.setColor(Color.YELLOW);
                }

                final String finalAvatarPath = avatarPath;
                final int index = i;
                avatarButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Main.playSound();
                        selectAvatar(finalAvatarPath);
                        highlightSelectedAvatar(index);
                    }
                });

                avatarButtons.add(avatarButton);
                presetAvatarsTable.add(avatarButton).size(120, 120).pad(8);

                // Add avatar name below the button
                Label nameLabel = new Label(
                    EnhancedAvatarManager.getInstance().getAvatarDisplayName(avatarPath),
                    skin
                );
                nameLabel.setColor(Color.WHITE);
                nameLabel.setFontScale(0.8f);
                presetAvatarsTable.row();
                presetAvatarsTable.add(nameLabel).center().padBottom(10);

                currentColumn++;
                if (currentColumn >= columns) {
                    presetAvatarsTable.row();
                    currentColumn = 0;
                }
            } catch (Exception e) {
                System.err.println("Error loading preset avatar: " + avatarPath + " - " + e.getMessage());
            }
        }
    }

    private void setupDragAndDrop() {
        // Create a drag and drop target for the drop zone
        dragAndDrop.addTarget(new DragAndDrop.Target(dropZone) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload,
                                float x, float y, int pointer) {
                // Visual feedback during drag
                dropZone.setBackground(highlightBackground);
                dropZoneLabel.setColor(Color.GREEN);
                dropZoneLabel.setText("Drop Here!");
                return true;
            }

            @Override
            public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                resetDropZone();
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload,
                             float x, float y, int pointer) {
                Object draggedObject = payload.getObject();
                if (draggedObject instanceof String filePath) {
                    handleFileSelection(filePath);
                }
                resetDropZone();
            }
        });
    }

    private void setupListeners() {
        // VisUI file chooser button
        selectFileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                openVisUIFileChooser();
            }
        });

        // Advanced VisUI browser button
        browseAdvancedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                openAdvancedVisUIBrowser();
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

    private void openVisUIFileChooser() {
        statusLabel.setText("Opening VisUI file chooser...");
        statusLabel.setColor(Color.YELLOW);

        VisUIFileChooser.chooseImageFile(this.getStage(), new VisUIFileChooser.FileChosenCallback() {
            @Override
            public void onFileChosen(FileHandle file) {
                handleFileSelection(file.path());
                System.out.println("Selected file: " + file.path());
            }

            @Override
            public void onCancelled() {
                statusLabel.setText("File selection cancelled");
                statusLabel.setColor(Color.GRAY);
            }

            @Override
            public void onError(String error) {
                statusLabel.setText("Error: " + error);
                statusLabel.setColor(Color.RED);
            }
        });
    }

    private void openAdvancedVisUIBrowser() {
        statusLabel.setText("Opening advanced file browser...");
        statusLabel.setColor(Color.YELLOW);

        VisUIFileChooser.EnhancedFileChooser.showWithPreview(this.getStage(),
            new VisUIFileChooser.FileChosenCallback() {
                @Override
                public void onFileChosen(FileHandle file) {
                    handleFileSelection(file.path());
                }

                @Override
                public void onCancelled() {
                    statusLabel.setText("File selection cancelled");
                    statusLabel.setColor(Color.GRAY);
                }

                @Override
                public void onError(String error) {
                    statusLabel.setText("Error: " + error);
                    statusLabel.setColor(Color.RED);
                }
            });
    }

    private void handleFileSelection(String filePath) {
        try {
            FileHandle file = Gdx.files.absolute(filePath);

            if (!VisUIFileChooser.isValidImageFile(file)) {
                statusLabel.setText("Not a valid image file");
                statusLabel.setColor(Color.RED);
                return;
            }

            String savedPath = EnhancedAvatarManager.getInstance().saveCustomAvatar(filePath);

            if (savedPath != null) {
                selectAvatar(savedPath);

                App.getCurrentUser().setAvatarPath(savedPath);
                App.getCurrentUser().setCustomAvatar(true);

                statusLabel.setText("✓ File uploaded successfully");
                statusLabel.setColor(Color.GREEN);
            } else {
                statusLabel.setText("Error saving avatar!");
                statusLabel.setColor(Color.RED);
            }
        } catch (Exception e) {
            System.err.println("Error handling file selection: " + e.getMessage());
            statusLabel.setText("Error handling file selection!");
            statusLabel.setColor(Color.RED);
        }
    }


    private void selectAvatar(String avatarPath) {
        selectedAvatarPath = avatarPath;
        updateCurrentAvatarDisplay();

        String displayName = EnhancedAvatarManager.getInstance().getAvatarDisplayName(avatarPath);
        statusLabel.setText("✓ Avatar chosen: " + displayName);
        statusLabel.setColor(Color.GREEN);
    }

    private void highlightSelectedAvatar(int selectedIndex) {
        // Reset all avatar buttons
        for (ImageButton button : avatarButtons) {
            button.setColor(Color.WHITE);
        }

        // Highlight selected avatar
        if (selectedIndex < avatarButtons.size) {
            avatarButtons.get(selectedIndex).setColor(Color.YELLOW);
        }
    }

    private void updateCurrentAvatarDisplay() {
        if (selectedAvatarPath != null) {
            try {
                Texture avatarTexture = EnhancedAvatarManager.getInstance().getAvatarTexture(selectedAvatarPath);
                currentAvatarImage.setDrawable(new TextureRegionDrawable(avatarTexture));
                currentAvatarLabel.setText(
                    EnhancedAvatarManager.getInstance().getAvatarDisplayName(selectedAvatarPath)
                );
            } catch (Exception e) {
                System.err.println("Error updating avatar display: " + e.getMessage());
            }
        }
    }

    private void confirmSelection() {
        if (selectedAvatarPath != null) {
            // Update user's avatar
            user.setAvatarPath(selectedAvatarPath);

            // Determine if it's a custom avatar
            boolean isCustom = selectedAvatarPath.contains("custom_");
            user.setCustomAvatar(isCustom);

            if (onAvatarSelected != null) {
                onAvatarSelected.accept(selectedAvatarPath);
            }

            // Save user data
            App.updateUser(user);

            statusLabel.setText("✓ Avatar updated successfully!");
            statusLabel.setColor(Color.GREEN);

            // Close after a short delay
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                @Override
                public void run() {
                    close();
                }
            }, 1.0f);
        } else {
            statusLabel.setText("❌ Please select an avatar first");
            statusLabel.setColor(Color.RED);
        }
    }

    private void close() {
        this.remove();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (!visible) {
            // Clean up resources
            if (dragAndDrop != null) {
                dragAndDrop.clear();
            }
            // Dispose custom textures
            if (normalBackground != null) {
                normalBackground.getRegion().getTexture().dispose();
            }
            if (highlightBackground != null) {
                highlightBackground.getRegion().getTexture().dispose();
            }
        }
    }
}
