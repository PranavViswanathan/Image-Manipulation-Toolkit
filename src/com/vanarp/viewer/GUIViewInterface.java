package com.vanarp.viewer;

import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;

public interface GUIViewInterface {

  // Methods for file operations
  void showFileChooser(FileChooserCallback callback);

  void showSaveFileChooser(FileChooserCallback callback);

  // Methods for setting images and histograms
  void setImageIcon(ImageIcon icon);

  void setHistogramIcon(ImageIcon icon);

  // Methods for displaying messages
  void showErrorDialog(String message);

  void showMessage(String message);

  // Method to create button action listeners
  void createButtons(ActionListener listener);

  // Method to untick all checkboxes
  void untickCheckBoxes();

  // Callback interface for file chooser
  @FunctionalInterface
  interface FileChooserCallback {

    void onFileChosen(File file);
  }
}