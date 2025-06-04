package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.Controllers.GameController;
import com.example.Main;
import com.example.Models.utilities.GameAssetManager;
import com.example.Models.utilities.GameLoader;
import com.example.Models.utilities.GameSaveSystem;

import java.util.List;
import java.util.function.Consumer;

public class SaveLoadWindow extends Window {
    private final Mode mode;
    private final GameController gameController;
    private final Consumer<String> onComplete;
    // UI Components
    private Table mainTable;
    private Table saveSlotTable;
    private ScrollPane scrollPane;
    // Save slots
    private List<String> availableSaves;
    private String selectedSave = null;
    // Controls
    private TextField saveNameField;
    private TextButton actionButton;
    private TextButton deleteButton;
    private TextButton cancelButton;
    // Info display
    private Label infoLabel;
    private Label selectedSaveInfo;

    public SaveLoadWindow(Skin skin, Mode mode, GameController gameController, Consumer<String> onComplete) {
        super(mode == Mode.SAVE ? "Save Game" : "Load Game", skin);

        this.mode = mode;
        this.gameController = gameController;
        this.onComplete = onComplete;

        initializeComponents(skin);
        refreshSaveList();
        setupLayout();
        setupListeners();

        this.setSize(800, 600);
        this.setPosition(
            (Gdx.graphics.getWidth() - 800) / 2f,
            (Gdx.graphics.getHeight() - 600) / 2f
        );
        this.setModal(true);
        this.setMovable(true);
    }

    private void initializeComponents(Skin skin) {
        mainTable = new Table();
        saveSlotTable = new Table();

        // Info label
        if (mode == Mode.SAVE) {
            infoLabel = new Label("Choose a slot to save your game:", skin);
        } else {
            infoLabel = new Label("Choose a saved game to load:", skin);
        }
        infoLabel.setColor(Color.CYAN);

        // Save name field (only for save mode)
        if (mode == Mode.SAVE) {
            saveNameField = new TextField("", skin);
            saveNameField.setMessageText("Enter save name...");
        }

        // Action button
        actionButton = new TextButton(mode == Mode.SAVE ? "Save Game" : "Load Game", skin);
        actionButton.setColor(mode == Mode.SAVE ? Color.GREEN : Color.BLUE);
        actionButton.setDisabled(mode == Mode.LOAD); // Disabled until selection for load mode

        // Delete button (for managing saves)
        deleteButton = new TextButton("Delete Save", skin);
        deleteButton.setColor(Color.RED);
        deleteButton.setDisabled(true);

        // Cancel button
        cancelButton = new TextButton("Cancel", skin);
        cancelButton.setColor(Color.GRAY);

        // Selected save info
        selectedSaveInfo = new Label("", skin);
        selectedSaveInfo.setColor(Color.YELLOW);
    }

    private void setupLayout() {
        mainTable.setFillParent(true);
        mainTable.top().pad(20);

        // Title and info
        mainTable.add(infoLabel).colspan(3).center().padBottom(20);
        mainTable.row();

        // Save name field (save mode only)
        if (mode == Mode.SAVE) {
            Label saveNameLabel = new Label("Save Name:", GameAssetManager.getGameAssetManager().getSkin());
            saveNameLabel.setColor(Color.WHITE);
            mainTable.add(saveNameLabel).left().padRight(10);
            mainTable.add(saveNameField).width(300).height(40).colspan(2);
            mainTable.row().padBottom(15);
        }

        // Save slots list
        createSaveSlotsList();
        scrollPane = new ScrollPane(saveSlotTable, GameAssetManager.getGameAssetManager().getSkin());
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        mainTable.add(scrollPane).size(700, 300).colspan(3).padBottom(20);
        mainTable.row();

        // Selected save info
        mainTable.add(selectedSaveInfo).colspan(3).center().padBottom(20);
        mainTable.row();

        // Buttons
        Table buttonTable = new Table();
        buttonTable.add(actionButton).width(150).height(50).pad(10);
        buttonTable.add(deleteButton).width(150).height(50).pad(10);
        buttonTable.add(cancelButton).width(150).height(50).pad(10);

        mainTable.add(buttonTable).colspan(3).center();

        this.add(mainTable);
    }

    private void createSaveSlotsList() {
        saveSlotTable.clear();

        // Header
        Label slotHeader = new Label("Save Slot", GameAssetManager.getGameAssetManager().getSkin());
        slotHeader.setColor(Color.WHITE);
        Label infoHeader = new Label("Save Information", GameAssetManager.getGameAssetManager().getSkin());
        infoHeader.setColor(Color.WHITE);
        Label dateHeader = new Label("Date", GameAssetManager.getGameAssetManager().getSkin());
        dateHeader.setColor(Color.WHITE);

        saveSlotTable.add(slotHeader).width(150).center().pad(5);
        saveSlotTable.add(infoHeader).width(350).center().pad(5);
        saveSlotTable.add(dateHeader).width(150).center().pad(5);
        saveSlotTable.row();

        // Separator
        Label separator = new Label("", GameAssetManager.getGameAssetManager().getSkin());
        saveSlotTable.add(separator).colspan(3).fillX().height(2).pad(5);
        saveSlotTable.row();

        // Create slots (show existing saves + empty slots for save mode)
        int maxSlots = mode == Mode.SAVE ? 10 : availableSaves.size();
        int existingSaves = availableSaves.size();

        for (int i = 0; i < Math.max(maxSlots, existingSaves); i++) {
            String slotName = "save_slot_" + (i + 1);
            boolean hasExistingSave = i < existingSaves;
            String existingSaveName = hasExistingSave ? availableSaves.get(i) : null;

            createSaveSlotRow(slotName, existingSaveName, hasExistingSave);
        }
    }

    private void createSaveSlotRow(String slotName, String existingSaveName, boolean hasExistingSave) {
        // Slot button
        TextButton slotButton = new TextButton(slotName, GameAssetManager.getGameAssetManager().getSkin());
        if (hasExistingSave) {
            slotButton.setColor(Color.LIGHT_GRAY);
        } else {
            slotButton.setColor(Color.DARK_GRAY);
        }

        // Info display
        String infoText = "";
        String dateText = "";

        if (hasExistingSave) {
            GameSaveSystem.SaveFileInfo info = GameSaveSystem.getSaveInfo(existingSaveName);
            if (info != null) {
                infoText = String.format("Lvl %d | %s | %d kills",
                    info.level, info.getFormattedSurvivalTime(), info.kills);
                dateText = info.getFormattedTimestamp();
            }
        } else {
            infoText = "Empty Slot";
            dateText = "";
        }

        Label infoLabel = new Label(infoText, GameAssetManager.getGameAssetManager().getSkin());
        infoLabel.setColor(hasExistingSave ? Color.WHITE : Color.GRAY);

        Label dateLabel = new Label(dateText, GameAssetManager.getGameAssetManager().getSkin());
        dateLabel.setColor(Color.LIGHT_GRAY);

        // Click listener
        slotButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                selectSave(hasExistingSave ? existingSaveName : slotName);
            }
        });

        saveSlotTable.add(slotButton).width(150).height(40).pad(5);
        saveSlotTable.add(infoLabel).width(350).left().pad(5);
        saveSlotTable.add(dateLabel).width(150).center().pad(5);
        saveSlotTable.row();
    }

    private void selectSave(String saveName) {
        selectedSave = saveName;

        // Update UI
        actionButton.setDisabled(false);
        deleteButton.setDisabled(!GameSaveSystem.saveExists(saveName));

        // Update info display
        if (GameSaveSystem.saveExists(saveName)) {
            String detailedInfo = GameLoader.getSaveDisplayInfo(saveName);
            selectedSaveInfo.setText("Selected: " + saveName + "\n" + detailedInfo);
            selectedSaveInfo.setColor(Color.YELLOW);
        } else {
            selectedSaveInfo.setText("Selected empty slot: " + saveName);
            selectedSaveInfo.setColor(Color.GREEN);
        }

        // Auto-fill save name field if in save mode
        if (mode == Mode.SAVE && saveNameField != null) {
            saveNameField.setText(saveName);
        }
    }

    private void setupListeners() {
        // Action button (Save/Load)
        actionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();

                if (mode == Mode.SAVE) {
                    handleSave();
                } else {
                    handleLoad();
                }
            }
        });

        // Delete button
        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                handleDelete();
            }
        });

        // Cancel button
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.playSound();
                close();
            }
        });
    }

    private void handleSave() {
        if (selectedSave == null) {
            showError("Please select a save slot!");
            return;
        }

        String saveName;
        if (saveNameField != null && !saveNameField.getText().trim().isEmpty()) {
            saveName = saveNameField.getText().trim();
        } else {
            saveName = selectedSave;
        }

        // Check if save already exists
        if (GameSaveSystem.saveExists(saveName)) {
            showConfirmDialog("Overwrite Save",
                "A save with this name already exists. Overwrite it?",
                () -> performSave(saveName));
        } else {
            performSave(saveName);
        }
    }

    private void performSave(String saveName) {
        try {
            boolean success = GameSaveSystem.saveGame(saveName, gameController);

            if (success) {
                showSuccess("Game saved successfully!");

                // Refresh the list
                refreshSaveList();
                createSaveSlotsList();

                // Call completion callback after delay
                com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                    @Override
                    public void run() {
                        if (onComplete != null) {
                            onComplete.accept(saveName);
                        }
                        close();
                    }
                }, 1.5f);

            } else {
                showError("Failed to save game!");
            }

        } catch (Exception e) {
            showError("Error saving game: " + e.getMessage());
        }
    }

    private void handleLoad() {
        if (selectedSave == null || !GameSaveSystem.saveExists(selectedSave)) {
            showError("Please select a valid saved game!");
            return;
        }

        try {
            // Validate save file
            if (!GameLoader.isValidSaveFile(selectedSave)) {
                showError("Invalid or corrupted save file!");
                return;
            }

            showSuccess("Loading game...");

            // Call completion callback with selected save
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                @Override
                public void run() {
                    if (onComplete != null) {
                        onComplete.accept(selectedSave);
                    }
                    close();
                }
            }, 1.0f);

        } catch (Exception e) {
            showError("Error loading game: " + e.getMessage());
        }
    }

    private void handleDelete() {
        if (selectedSave == null || !GameSaveSystem.saveExists(selectedSave)) {
            showError("Please select a valid saved game to delete!");
            return;
        }

        showConfirmDialog("Delete Save",
            "Are you sure you want to delete this save?\nThis action cannot be undone!",
            () -> performDelete());
    }

    private void performDelete() {
        try {
            boolean success = GameSaveSystem.deleteSave(selectedSave);

            if (success) {
                showSuccess("Save deleted successfully!");

                // Reset selection
                selectedSave = null;
                actionButton.setDisabled(true);
                deleteButton.setDisabled(true);
                selectedSaveInfo.setText("");

                // Refresh the list
                refreshSaveList();
                createSaveSlotsList();

            } else {
                showError("Failed to delete save!");
            }

        } catch (Exception e) {
            showError("Error deleting save: " + e.getMessage());
        }
    }

    private void refreshSaveList() {
        availableSaves = GameSaveSystem.getAvailableSaves();
    }

    private void showConfirmDialog(String title, String message, Runnable onConfirm) {
        Dialog confirmDialog = new Dialog(title, GameAssetManager.getGameAssetManager().getSkin()) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object && onConfirm != null) {
                    onConfirm.run();
                }
            }
        };

        confirmDialog.text(message);
        confirmDialog.button("Yes", true);
        confirmDialog.button("No", false);
        confirmDialog.show(this.getStage());
    }

    private void showError(String message) {
        selectedSaveInfo.setText("❌ " + message);
        selectedSaveInfo.setColor(Color.RED);

        // Clear error after 3 seconds
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                if (selectedSave != null && GameSaveSystem.saveExists(selectedSave)) {
                    String detailedInfo = GameLoader.getSaveDisplayInfo(selectedSave);
                    selectedSaveInfo.setText("Selected: " + selectedSave + "\n" + detailedInfo);
                    selectedSaveInfo.setColor(Color.YELLOW);
                } else {
                    selectedSaveInfo.setText("");
                }
            }
        }, 3.0f);
    }

    private void showSuccess(String message) {
        selectedSaveInfo.setText("✅ " + message);
        selectedSaveInfo.setColor(Color.GREEN);
    }

    private void close() {
        this.remove();
    }

    public enum Mode {
        SAVE, LOAD
    }
}
