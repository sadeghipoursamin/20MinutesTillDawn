// Enhanced Drag and Drop Implementation
// Views/EnhancedAvatarSelectionWindow.java
package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.utilities.AvatarManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class EnhancedAvatarSelectionWindow extends Window {
    private User user;
    private Consumer<String> onAvatarSelected;
    private Table predefinedAvatarsTable;
    private Image currentAvatarImage;
    private Label statusLabel;
    private TextButton selectFileButton;
    private TextButton confirmButton;
    private TextButton cancelButton;
    private String selectedAvatarPath;
    private Table dragDropArea;
    private Label dragDropLabel;
    private boolean isDragActive = false;

    public EnhancedAvatarSelectionWindow(Skin skin, User user, Consumer<String> onAvatarSelected) {
        super("Select Avatar", skin);
        this.user = user;
        this.onAvatarSelected = onAvatarSelected;
        this.selectedAvatarPath = user.getAvatarPath();

        initializeUI(skin);
        SwingDragDropHandler.setupDragDrop(this::handleDroppedFile);
        setupListeners();

        this.setSize(900, 700);
        this.setPosition(
            (Gdx.graphics.getWidth() - 900) / 2f,
            (Gdx.graphics.getHeight() - 700) / 2f
        );
        this.setModal(true);
        this.setMovable(true);
    }

    private void initializeUI(Skin skin) {
        // Title
        Label titleLabel = new Label("Choose Your Avatar", skin, "title");
        titleLabel.setColor(Color.CYAN);

        // Current avatar display
        Label currentLabel = new Label("Current Avatar:", skin);
        currentAvatarImage = new Image();
        updateCurrentAvatarImage();

        // Predefined avatars section
        Label predefinedLabel = new Label("Predefined Avatars:", skin);
        predefinedLabel.setColor(Color.WHITE);

        predefinedAvatarsTable = new Table();
        createPredefinedAvatarsGrid(skin);

        ScrollPane predefinedScrollPane = new ScrollPane(predefinedAvatarsTable, skin);
        predefinedScrollPane.setScrollingDisabled(false, false);
        predefinedScrollPane.setFadeScrollBars(false);

        // Custom avatar section
        Label customLabel = new Label("Custom Avatar Options:", skin);
        customLabel.setColor(Color.WHITE);

        selectFileButton = new TextButton("Browse Computer", skin);
        selectFileButton.setColor(Color.CYAN);

        // Enhanced drag and drop area
        createDragDropArea(skin);

        // Status and buttons
        statusLabel = new Label("Select an avatar from above options", skin);
        statusLabel.setColor(Color.LIGHT_GRAY);

        confirmButton = new TextButton("Apply Changes", skin);
        confirmButton.setColor(Color.GREEN);

        cancelButton = new TextButton("Cancel", skin);
        cancelButton.setColor(Color.RED);

        // Layout with better spacing
        this.add(titleLabel).colspan(2).center().padBottom(20);
        this.row();

        this.add(currentLabel).left().padBottom(5);
        this.add().expandX(); // Spacer
        this.row();
        this.add(currentAvatarImage).size(120, 120).padBottom(25);
        this.add().expandX();
        this.row();

        this.add(predefinedLabel).left().padBottom(10);
        this.add().expandX();
        this.row();
        this.add(predefinedScrollPane).size(800, 180).padBottom(25);
        this.add().expandX();
        this.row();

        this.add(customLabel).left().padBottom(10);
        this.add().expandX();
        this.row();
        this.add(selectFileButton).width(200).height(50).padBottom(15);
        this.add().expandX();
        this.row();

        this.add(dragDropArea).size(600, 100).padBottom(25);
        this.add().expandX();
        this.row();

        this.add(statusLabel).colspan(2).center().padBottom(20);
        this.row();

        Table buttonTable = new Table();
        buttonTable.add(confirmButton).width(180).height(60).pad(10);
        buttonTable.add(cancelButton).width(180).height(60).pad(10);

        this.add(buttonTable).colspan(2).center();
    }

    private void createPredefinedAvatarsGrid(Skin skin) {
        Array<String> predefinedAvatars = AvatarManager.getInstance().getPredefinedAvatars();
        int columns = 6;
        int currentColumn = 0;

        for (int i = 0; i < predefinedAvatars.size; i++) {
            String avatarPath = predefinedAvatars.get(i);
            Texture avatarTexture = AvatarManager.getInstance().getAvatarTexture(avatarPath);

            ImageButton avatarButton = new ImageButton(new TextureRegionDrawable(avatarTexture));
            avatarButton.getImage().setScaling(com.badlogic.gdx.utils.Scaling.fit);

            // Highlight selected avatar
            if (avatarPath.equals(selectedAvatarPath)) {
                avatarButton.setColor(Color.YELLOW);
            }

            final String finalAvatarPath = avatarPath;
            final int index = i;
            avatarButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Main.playSound();
                    selectPredefinedAvatar(finalAvatarPath);
                    highlightSelectedAvatar(index);
                }
            });

            predefinedAvatarsTable.add(avatarButton).size(100, 100).pad(8);

            currentColumn++;
            if (currentColumn >= columns) {
                predefinedAvatarsTable.row();
                currentColumn = 0;
            }
        }
    }

    private void createDragDropArea(Skin skin) {
        dragDropArea = new Table(skin);
        dragDropArea.setBackground("default-round");

        dragDropLabel = new Label("📁 Drag & Drop Image Here\nor click to browse", skin);
        dragDropLabel.setColor(Color.LIGHT_GRAY);
        dragDropLabel.setAlignment(com.badlogic.gdx.utils.Align.center);

        dragDropArea.add(dragDropLabel).expand().fill().pad(20);

        // Add visual feedback for drag operations
        dragDropArea.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                openFileChooser();
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (!isDragActive) {
                    dragDropArea.setColor(Color.CYAN);
                    dragDropLabel.setText("📁 Drop Image Here!");
                    dragDropLabel.setColor(Color.CYAN);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (!isDragActive) {
                    dragDropArea.setColor(Color.WHITE);
                    dragDropLabel.setText("📁 Drag & Drop Image Here\nor click to browse");
                    dragDropLabel.setColor(Color.LIGHT_GRAY);
                }
            }
        });

        // Set up native drag and drop (this would need desktop-specific implementation)
        setupNativeDragDrop();
    }

    private void setupNativeDragDrop() {
        // This is a simplified version - in a real implementation,
        // you'd need to integrate with the desktop application's window
        try {
            // Create a hidden Swing component for drag and drop
            setupSwingDragDrop();
        } catch (Exception e) {
            System.out.println("Native drag and drop not available: " + e.getMessage());
        }
    }

    private void setupSwingDragDrop() {
        // This method sets up Swing-based drag and drop
        // Note: This requires the desktop application to have Swing integration
        SwingDragDropHandler.setupDragDrop(this::handleDroppedFile);
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

    private void highlightSelectedAvatar(int selectedIndex) {
        // Reset all avatar buttons
        for (Actor actor : predefinedAvatarsTable.getChildren()) {
            if (actor instanceof ImageButton) {
                actor.setColor(Color.WHITE);
            }
        }

        // Highlight selected avatar
        if (selectedIndex < predefinedAvatarsTable.getChildren().size) {
            predefinedAvatarsTable.getChildren().get(selectedIndex).setColor(Color.YELLOW);
        }
    }

    private void selectPredefinedAvatar(String avatarPath) {
        selectedAvatarPath = avatarPath;
        updateCurrentAvatarImage();
        statusLabel.setText("✓ Predefined avatar selected");
        statusLabel.setColor(Color.GREEN);
    }

    private void openFileChooser() {
        Thread fileChooserThread = new Thread(() -> {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select Avatar Image");
                fileChooser.setFileFilter(new FileNameExtensionFilter(
                    "Image files (*.jpg, *.jpeg, *.png, *.bmp)",
                    "jpg", "jpeg", "png", "bmp"));

                // Set current directory to user's pictures folder if available
                String userHome = System.getProperty("user.home");
                File picturesDir = new File(userHome, "Pictures");
                if (picturesDir.exists()) {
                    fileChooser.setCurrentDirectory(picturesDir);
                }

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();

                    Gdx.app.postRunnable(() -> {
                        handleCustomAvatarSelection(filePath);
                    });
                }
            } catch (Exception e) {
                System.err.println("Error opening file chooser: " + e.getMessage());
                Gdx.app.postRunnable(() -> {
                    statusLabel.setText("❌ Error opening file browser");
                    statusLabel.setColor(Color.RED);
                });
            }
        });

        fileChooserThread.setDaemon(true);
        fileChooserThread.start();
    }

    private void handleDroppedFile(String filePath) {
        Gdx.app.postRunnable(() -> {
            isDragActive = true;
            dragDropArea.setColor(Color.GREEN);
            dragDropLabel.setText("📁 Processing...");
            dragDropLabel.setColor(Color.GREEN);

            handleCustomAvatarSelection(filePath);

            // Reset drag area after processing
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                @Override
                public void run() {
                    isDragActive = false;
                    dragDropArea.setColor(Color.WHITE);
                    dragDropLabel.setText("📁 Drag & Drop Image Here\nor click to browse");
                    dragDropLabel.setColor(Color.LIGHT_GRAY);
                }
            }, 2.0f);
        });
    }

    private void handleCustomAvatarSelection(String filePath) {
        // Validate file
        if (!AvatarManager.getInstance().isValidImageFile(filePath)) {
            statusLabel.setText("❌ Invalid image format. Use PNG, JPG, or BMP");
            statusLabel.setColor(Color.RED);
            return;
        }

        // Check file size (limit to 5MB)
        File file = new File(filePath);
        if (file.length() > 5 * 1024 * 1024) {
            statusLabel.setText("❌ File too large. Maximum size is 5MB");
            statusLabel.setColor(Color.RED);
            return;
        }

        // Save the custom avatar
        String savedPath = AvatarManager.getInstance().saveCustomAvatar(filePath);
        if (savedPath != null) {
            selectedAvatarPath = savedPath;
            updateCurrentAvatarImage();
            statusLabel.setText("✓ Custom avatar loaded successfully");
            statusLabel.setColor(Color.GREEN);

            // Reset predefined avatar selection
            for (Actor actor : predefinedAvatarsTable.getChildren()) {
                if (actor instanceof ImageButton) {
                    actor.setColor(Color.WHITE);
                }
            }
        } else {
            statusLabel.setText("❌ Failed to save custom avatar");
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

            statusLabel.setText("✓ Avatar updated successfully!");
            statusLabel.setColor(Color.GREEN);

            // Close after a short delay
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                @Override
                public void run() {
                    close();
                }
            }, 1.5f);
        } else {
            statusLabel.setText("❌ Please select an avatar first");
            statusLabel.setColor(Color.RED);
        }
    }

    private void close() {
        this.remove();
    }
}

class SwingDragDropHandler {
    public static void setupDragDrop(java.util.function.Consumer<String> onFileDropped) {
        try {
            // Create invisible Swing frame for drag and drop
            JFrame frame = new JFrame();
            frame.setSize(1, 1);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setVisible(false);

            JPanel panel = new JPanel();
            frame.add(panel);

            // Set up drop target
            DropTarget dropTarget = new DropTarget(panel, new DropTargetListener() {
                @Override
                public void dragEnter(DropTargetDragEvent dtde) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                }

                @Override
                public void dragOver(DropTargetDragEvent dtde) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                }

                @Override
                public void dropActionChanged(DropTargetDragEvent dtde) {
                }

                @Override
                public void dragExit(DropTargetEvent dte) {
                }

                @Override
                public void drop(DropTargetDropEvent dtde) {
                    try {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        Transferable transferable = dtde.getTransferable();

                        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            @SuppressWarnings("unchecked")
                            List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                            if (!files.isEmpty()) {
                                File file = files.get(0);
                                String filePath = file.getAbsolutePath();

                                // Check if it's an image file
                                String extension = filePath.toLowerCase();
                                if (extension.endsWith(".png") || extension.endsWith(".jpg") ||
                                    extension.endsWith(".jpeg") || extension.endsWith(".bmp")) {
                                    onFileDropped.accept(filePath);
                                }
                            }
                        }
                        dtde.dropComplete(true);
                    } catch (Exception e) {
                        System.err.println("Error handling drop: " + e.getMessage());
                        dtde.dropComplete(false);
                    }
                }
            });

        } catch (Exception e) {
            System.err.println("Could not set up drag and drop: " + e.getMessage());
        }
    }
}
