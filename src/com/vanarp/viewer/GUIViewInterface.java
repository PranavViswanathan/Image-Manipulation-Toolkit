package com.vanarp.viewer;

import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;

/**
 * The {@code GUIViewInterface} defines the contract for the graphical user interface (GUI)
 * components of the application. It includes methods for interacting with file choosers,
 * displaying messages, and managing GUI elements such as buttons and labels.
 */
public interface GUIViewInterface {

  /**
   * Displays a file chooser dialog and executes the specified callback when a file is selected.
   *
   * @param callback the callback to handle the file selection event.
   */
  void showFileChooser(FileChooserCallback callback);

  /**
   * Displays a save file chooser dialog and executes the specified callback when a file is chosen.
   *
   * @param callback the callback to handle the save file selection event.
   */
  void showSaveFileChooser(FileChooserCallback callback);

  /**
   * Sets the icon of the main image label to the specified {@code ImageIcon}.
   *
   * @param icon the {@code ImageIcon} to be displayed in the main image label.
   */
  void setImageIcon(ImageIcon icon);

  /**
   * Sets the icon of the histogram label to the specified {@code ImageIcon}.
   *
   * @param icon the {@code ImageIcon} to be displayed in the histogram label.
   */
  void setHistogramIcon(ImageIcon icon);

  /**
   * Displays an error dialog with the specified error message.
   *
   * @param message the error message to be displayed in the dialog.
   */
  void showErrorDialog(String message);

  /**
   * Displays an information dialog with the specified message.
   *
   * @param message the information message to be displayed in the dialog.
   */
  void showMessage(String message);

  /**
   * Creates and initializes buttons in the GUI, associating them with the specified action listener.
   *
   * @param listener the {@code ActionListener} to handle button events.
   */
  void createButtons(ActionListener listener);

  /**
   * Unselects all checkboxes used for split-view or similar options in the interface.
   */
  void untickCheckBoxes();

  /**
   * A functional interface to handle file selection events from file choosers.
   */
  @FunctionalInterface
  interface FileChooserCallback {

    /**
     * Called when a file is chosen through the file chooser dialog.
     *
     * @param file the selected {@code File}.
     */
    void onFileChosen(File file);
  }
}
