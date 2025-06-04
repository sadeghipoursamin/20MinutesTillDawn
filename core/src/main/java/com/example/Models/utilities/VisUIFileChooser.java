package com.example.Models.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;


public class VisUIFileChooser {

    private static boolean visUIInitialized = false;

    public static void initializeVisUI() {
        if (!visUIInitialized) {
            try {
                VisUI.load();
                visUIInitialized = true;
                System.out.println("VisUI initialized successfully");
            } catch (Exception e) {
                System.err.println("Failed to initialize VisUI: " + e.getMessage());
                throw new RuntimeException("VisUI initialization failed", e);
            }
        }
    }


    public static void chooseImageFile(Stage stage, FileChosenCallback callback) {
        chooseFile(stage, callback, FileChooser.Mode.OPEN,
            createImageFilter(), "Choose Image File");
    }

    public static void chooseFile(Stage stage, FileChosenCallback callback,
                                  FileChooser.Mode mode, FileTypeFilter filter, String title) {
        initializeVisUI();

        try {
            FileChooser fileChooser = new FileChooser(mode);
            fileChooser.setMultiSelectionEnabled(false);

            if (filter != null) {
                fileChooser.setFileTypeFilter(filter);
            }

            if (title != null && !title.isEmpty()) {
                fileChooser.getTitleLabel().setText(title);
            }

            // Set initial directory to user's pictures folder
            try {
                FileHandle picturesDir = getPicturesDirectory();
                if (picturesDir.exists() && picturesDir.isDirectory()) {
                    fileChooser.setDirectory(picturesDir);
                }
            } catch (Exception e) {
                System.out.println("Could not set pictures directory: " + e.getMessage());
            }

            fileChooser.setListener(new FileChooserAdapter() {

                public void selected(FileHandle file) {
                    try {
                        if (file != null && file.exists()) {
                            callback.onFileChosen(file);
                        } else {
                            callback.onError("Selected file does not exist");
                        }
                    } catch (Exception e) {
                        callback.onError("Error processing selected file: " + e.getMessage());
                    }
                }

                @Override
                public void canceled() {
                    callback.onCancelled();
                }
            });

            stage.addActor(fileChooser.fadeIn());

        } catch (Exception e) {
            callback.onError("Error creating file chooser: " + e.getMessage());
        }
    }

    /**
     * Show save dialog for files
     */
    public static void saveFile(Stage stage, FileChosenCallback callback,
                                FileTypeFilter filter, String title, String defaultName) {
        initializeVisUI();

        try {
            FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
            fileChooser.setMultiSelectionEnabled(false);

            if (filter != null) {
                fileChooser.setFileTypeFilter(filter);
            }

            if (title != null && !title.isEmpty()) {
                fileChooser.getTitleLabel().setText(title);
            }

            if (defaultName != null && !defaultName.isEmpty()) {
                fileChooser.setSelectedFiles(FileHandle.tempFile(defaultName));
            }

            fileChooser.setListener(new FileChooserAdapter() {
                public void selected(FileHandle file) {
                    callback.onFileChosen(file);
                }

                @Override
                public void canceled() {
                    callback.onCancelled();
                }
            });

            stage.addActor(fileChooser.fadeIn());

        } catch (Exception e) {
            callback.onError("Error creating save dialog: " + e.getMessage());
        }
    }

    public static FileTypeFilter createImageFilter() {
        FileTypeFilter filter = new FileTypeFilter(false);
        filter.addRule("Image Files", "png", "jpg", "jpeg", "bmp", "gif", "tiff");
        return filter;
    }


    public static FileTypeFilter createCustomFilter(String description, String... extensions) {
        return new FileTypeFilter(false) {
            public boolean accept(FileHandle file) {
                if (file.isDirectory()) return true;

                String extension = file.extension().toLowerCase();
                for (String ext : extensions) {
                    if (extension.equals(ext.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }

            public String getDescription() {
                return description;
            }
        };
    }


    private static FileHandle getPicturesDirectory() {
        switch (Gdx.app.getType()) {
            case Desktop:
                String userHome = System.getProperty("user.home");
                FileHandle picturesDir = Gdx.files.absolute(userHome + "/Pictures");
                if (picturesDir.exists()) return picturesDir;

                // Fallback to user home
                return Gdx.files.absolute(userHome);

            case Android:
                // Android external storage
                return Gdx.files.external("Pictures");

            case iOS:
                // iOS documents directory
                return Gdx.files.local("Documents");

            default:
                // Fallback to external root
                return Gdx.files.external("");
        }
    }

    public static boolean isValidImageFile(FileHandle file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }

        String extension = file.extension().toLowerCase();
        return extension.equals("png") || extension.equals("jpg") ||
            extension.equals("jpeg") || extension.equals("bmp");
    }


    public static void dispose() {
        if (visUIInitialized) {
            try {
                VisUI.dispose();
                visUIInitialized = false;
                System.out.println("VisUI disposed");
            } catch (Exception e) {
                System.err.println("Error disposing VisUI: " + e.getMessage());
            }
        }
    }


    public static boolean isInitialized() {
        return visUIInitialized;
    }

    public interface FileChosenCallback {
        void onFileChosen(FileHandle file);

        void onCancelled();

        void onError(String error);
    }


    public static class EnhancedFileChooser extends FileChooser {

        public EnhancedFileChooser(Mode mode) {
            super(mode);
            setupEnhancements();
        }

        public static void showWithPreview(Stage stage, FileChosenCallback callback) {
            VisUIFileChooser.initializeVisUI();

            EnhancedFileChooser fileChooser = new EnhancedFileChooser(Mode.OPEN);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileTypeFilter(createImageFilter());
            fileChooser.getTitleLabel().setText("Choose Avatar Image");

            // Set up the listener
            fileChooser.setListener(new FileChooserAdapter() {
                public void selected(FileHandle file) {
                    try {
                        if (file == null || !file.exists()) {
                            callback.onError("Selected file does not exist");
                            return;
                        }

                        // Rule 1: File must be under 5MB
                        if (file.length() > 5 * 1024 * 1024) {
                            callback.onError("File is too large (max 5MB)");
                            return;
                        }

                        // Rule 2: File must not be hidden
                        if (file.name().startsWith(".")) {
                            callback.onError("Hidden files are not allowed");
                            return;
                        }

                        // Rule 3: Custom filename pattern (e.g., no spaces)
                        if (file.name().contains(" ")) {
                            callback.onError("File name should not contain spaces");
                            return;
                        }

                        // If all rules pass
                        callback.onFileChosen(file);

                    } catch (Exception e) {
                        callback.onError("Error processing selected file: " + e.getMessage());
                    }
                }

                @Override
                public void canceled() {
                    callback.onCancelled();
                }
            });


            stage.addActor(fileChooser.fadeIn());
        }

        private void setupEnhancements() {
            // Enable favorites (bookmarks)
            setFavoritesPrefsName("avatar_file_chooser");
        }
    }
}
