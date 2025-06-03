package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.example.Main;
import com.example.Models.App;
import com.example.Models.User;
import com.example.Models.utilities.AvatarManager;

import java.util.function.Consumer;

public class AvatarSelectionWindow extends Window {
    private User user;
    private Consumer<String> onAvatarSelected;
    private Table avatarsTable;
    private Image currentAvatarImage;
    private Label currentAvatarLabel;
    private Label statusLabel;
    private TextButton confirmButton;
    private TextButton cancelButton;
    private String selectedAvatarPath;
    private Array<ImageButton> avatarButtons;

    public AvatarSelectionWindow(Skin skin, User user, Consumer<String> onAvatarSelected) {
        super("Choose Your Avatar", skin);
        this.user = user;
        this.onAvatarSelected = onAvatarSelected;
        this.selectedAvatarPath = user.getAvatarPath();
        this.avatarButtons = new Array<>();

        // If user doesn't have an avatar, set default
        if (this.selectedAvatarPath == null || this.selectedAvatarPath.isEmpty()) {
            this.selectedAvatarPath = AvatarManager.getInstance().getDefaultAvatarPath();
        }

        initializeUI(skin);
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
        // Title
        Label titleLabel = new Label("Choose Your Avatar", skin, "title");
        titleLabel.setColor(Color.CYAN);

        // Current avatar display
        Label currentLabel = new Label("Current Avatar:", skin);
        currentLabel.setColor(Color.WHITE);

        currentAvatarImage = new Image();
        currentAvatarLabel = new Label("", skin);
        currentAvatarLabel.setColor(Color.YELLOW);
        updateCurrentAvatarDisplay();

        // Available avatars section
        Label availableLabel = new Label("Available Avatars:", skin);
        availableLabel.setColor(Color.WHITE);

        avatarsTable = new Table();
        createAvatarsGrid(skin);

        ScrollPane avatarsScrollPane = new ScrollPane(avatarsTable, skin);
        avatarsScrollPane.setScrollingDisabled(false, false);
        avatarsScrollPane.setFadeScrollBars(false);

        // Status and buttons
        statusLabel = new Label("Select an avatar from the options above", skin);
        statusLabel.setColor(Color.LIGHT_GRAY);

        confirmButton = new TextButton("Apply Avatar", skin);
        confirmButton.setColor(Color.GREEN);

        cancelButton = new TextButton("Cancel", skin);
        cancelButton.setColor(Color.RED);

        // Layout
        this.add(titleLabel).colspan(2).center().padBottom(20);
        this.row();

        // Current avatar section
        Table currentAvatarSection = new Table();
        currentAvatarSection.add(currentLabel).padBottom(10);
        currentAvatarSection.row();
        currentAvatarSection.add(currentAvatarImage).size(120, 120).padBottom(10);
        currentAvatarSection.row();
        currentAvatarSection.add(currentAvatarLabel).center();

        this.add(currentAvatarSection).colspan(2).center().padBottom(25);
        this.row();

        this.add(availableLabel).colspan(2).left().padBottom(10);
        this.row();
        this.add(avatarsScrollPane).size(700, 300).colspan(2).padBottom(25);
        this.row();

        this.add(statusLabel).colspan(2).center().padBottom(20);
        this.row();

        Table buttonTable = new Table();
        buttonTable.add(confirmButton).width(150).height(50).pad(10);
        buttonTable.add(cancelButton).width(150).height(50).pad(10);

        this.add(buttonTable).colspan(2).center();
    }

    private void createAvatarsGrid(Skin skin) {
        Array<String> availableAvatars = AvatarManager.getInstance().getAvailableAvatars();
        int columns = 4;
        int currentColumn = 0;

        for (int i = 0; i < availableAvatars.size; i++) {
            String avatarPath = availableAvatars.get(i);
            Texture avatarTexture = AvatarManager.getInstance().getAvatarTexture(avatarPath);

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
            avatarsTable.add(avatarButton).size(120, 120).pad(8);

            // Add avatar name below the button
            Label nameLabel = new Label(AvatarManager.getInstance().getAvatarDisplayName(avatarPath), skin);
            nameLabel.setColor(Color.WHITE);
            nameLabel.setFontScale(0.8f);
            avatarsTable.row();
            avatarsTable.add(nameLabel).center().padBottom(10);

            currentColumn++;
            if (currentColumn >= columns) {
                avatarsTable.row();
                currentColumn = 0;
            }
        }
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

    private void selectAvatar(String avatarPath) {
        selectedAvatarPath = avatarPath;
        updateCurrentAvatarDisplay();
        statusLabel.setText("✓ Avatar selected: " + AvatarManager.getInstance().getAvatarDisplayName(avatarPath));
        statusLabel.setColor(Color.GREEN);
    }

    private void updateCurrentAvatarDisplay() {
        if (selectedAvatarPath != null) {
            Texture avatarTexture = AvatarManager.getInstance().getAvatarTexture(selectedAvatarPath);
            currentAvatarImage.setDrawable(new TextureRegionDrawable(avatarTexture));
            currentAvatarLabel.setText(AvatarManager.getInstance().getAvatarDisplayName(selectedAvatarPath));
        }
    }

    private void setupListeners() {
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

    private void confirmSelection() {
        if (selectedAvatarPath != null) {
            // Update user's avatar
            user.setAvatarPath(selectedAvatarPath);
            user.setCustomAvatar(false); // These are predefined avatars

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

    // Getters for backward compatibility
    public TextButton getConfirmButton() {
        return confirmButton;
    }

    public TextButton getCancelButton() {
        return cancelButton;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }
}
