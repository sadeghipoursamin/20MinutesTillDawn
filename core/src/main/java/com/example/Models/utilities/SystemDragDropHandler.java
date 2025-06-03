// SystemDragDropHandler.java
// This class handles system-level drag and drop for desktop platforms

package com.example.Models.utilities;

import com.badlogic.gdx.Gdx;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class SystemDragDropHandler implements DropTargetListener {
    private Consumer<String> onFileDropped;
    private DropTarget dropTarget;
    private boolean isActive = false;

    public SystemDragDropHandler(Consumer<String> onFileDropped) {
        this.onFileDropped = onFileDropped;
    }

    public void activate() {
        try {
            // This would need to be integrated with the actual window/canvas
            // For now, we'll simulate with a file watcher approach
            isActive = true;
            System.out.println("Drag and drop handler activated");
        } catch (Exception e) {
            System.err.println("Error activating drag and drop: " + e.getMessage());
        }
    }

    public void deactivate() {
        isActive = false;
        if (dropTarget != null) {
            dropTarget.setActive(false);
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        if (isValidDrag(dtde)) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        } else {
            dtde.rejectDrag();
        }
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        // Visual feedback could be implemented here
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // Handle action change if needed
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        // Reset visual feedback
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            dtde.acceptDrop(DnDConstants.ACTION_COPY);

            Transferable transferable = dtde.getTransferable();

            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                @SuppressWarnings("unchecked")
                List<File> droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                if (!droppedFiles.isEmpty()) {
                    File file = droppedFiles.get(0); // Take the first file

                    if (isValidImageFile(file)) {
                        // Execute on libGDX thread
                        Gdx.app.postRunnable(() -> {
                            if (onFileDropped != null) {
                                onFileDropped.accept(file.getAbsolutePath());
                            }
                        });

                        dtde.dropComplete(true);
                        return;
                    }
                }
            }

            dtde.dropComplete(false);

        } catch (Exception e) {
            System.err.println("Error handling file drop: " + e.getMessage());
            dtde.dropComplete(false);
        }
    }

    private boolean isValidDrag(DropTargetDragEvent dtde) {
        return dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    private boolean isValidImageFile(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }

        String name = file.getName().toLowerCase();
        return name.endsWith(".png") || name.endsWith(".jpg") ||
            name.endsWith(".jpeg") || name.endsWith(".bmp");
    }
}
