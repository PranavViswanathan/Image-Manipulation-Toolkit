package com.vanarp.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GUIView extends JFrame implements GUIViewInterface {

  private final JLabel imageLabel;
  private final JLabel histogramLabel;
  private JButton loadButton;
  private JButton saveButton;
  private JButton undoButton;
  private JButton revertButton;
  private JButton extractRedButton;
  private JButton extractGreenButton;
  private JButton extractBlueButton;
  private JButton extractLumaButton;
  private JButton extractIntensityButton;
  private JButton extractValueButton;
  private JButton blurButton;
  private JButton sharpenButton;
  private JButton sepiaButton;
  private JButton greyscaleButton;
  private JButton flipHorizontalButton;
  private JButton flipVerticalButton;
  private JButton adjustBrightnessButton;
  private JButton colorCorrectButton;
  private JButton adjustLevelsButton;
  private JButton downscaleButton;
  private JButton compressButton;

  private JCheckBox blurSplitCheckBox;
  private JCheckBox sharpenSplitCheckBox;
  private JCheckBox sepiaSplitCheckBox;
  private JCheckBox greyscaleSplitCheckBox;
  private JCheckBox colorCorrectSplitCheckBox;
  private JCheckBox adjustLevelsSplitCheckBox;

  /**
   * Constructs a GUIView for the Image Processing Application.
   * This initializes the main window, sets up the layout, and creates
   * all necessary components, including buttons, panels, and labels.
   */
  public GUIView() {
    setTitle("Image Processing Application");
    setSize(1075, 600);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setLayout(new BorderLayout());

    JPanel imagePanel = new JPanel();
    imagePanel.setLayout(new BorderLayout());
    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    imageScrollPane.setPreferredSize(new Dimension(300, 50));
    imagePanel.add(imageScrollPane, BorderLayout.CENTER);

    JPanel histogramPanel = new JPanel();
    histogramPanel.setLayout(new BorderLayout());
    histogramLabel = new JLabel();
    histogramLabel.setHorizontalAlignment(JLabel.CENTER);
    JScrollPane histogramScrollPane = new JScrollPane(histogramLabel);
    histogramScrollPane.setPreferredSize(new Dimension(300, 25));
    histogramPanel.add(histogramScrollPane, BorderLayout.EAST);

    JPanel displayPanel = new JPanel();
    displayPanel.setLayout(new BorderLayout());
    displayPanel.add(imagePanel, BorderLayout.CENTER);
    displayPanel.add(histogramPanel, BorderLayout.EAST);
    add(displayPanel, BorderLayout.CENTER);

    initializeButtons();
    createCheckBoxes();

    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);

    addButtonGroup(controlPanel, gbc, "File Operations",
        new JButton[]{loadButton, saveButton, undoButton, revertButton});
    addButtonGroup(controlPanel, gbc, "Color Extraction",
        new JButton[]{extractRedButton, extractGreenButton, extractBlueButton, extractLumaButton,
            extractIntensityButton, extractValueButton});
    addButtonGroup(controlPanel, gbc, "Transformations",
        new JButton[]{flipHorizontalButton, flipVerticalButton});
    addButtonGroup(controlPanel, gbc, "Reductions",
        new JButton[]{downscaleButton, compressButton});

    gbc.gridy++;
    gbc.gridx = 0;
    controlPanel.add(adjustBrightnessButton, gbc);

    addImageEffectsGroup(controlPanel, gbc);
    addAdjustmentsGroup(controlPanel, gbc);

    JScrollPane scrollPane = new JScrollPane(controlPanel);
    scrollPane.setPreferredSize(new Dimension(320, 600));
    add(scrollPane, BorderLayout.WEST);

    addCheckBoxListeners();
  }

  /**
   * Displays a file chooser dialog that allows the user to select a file.
   * If the user selects a file and approves the selection, the chosen file
   * is passed to the provided callback function for further processing.
   *
   * @param callback the callback to handle the selected file. This should implement
   *                 the {@link FileChooserCallback} interface, which defines the
   *                 file chooser method.
   */
  public void showFileChooser(FileChooserCallback callback) {
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      callback.onFileChosen(fileChooser.getSelectedFile());
    }
  }

  /**
   * Displays a file chooser dialog that allows the user to select a location
   * and filename for saving a file. If the user selects a file and approves
   * the selection, the chosen file is passed to the provided callback function
   * for further processing.
   *
   * @param callback the callback to handle the selected file. This should implement
   *                 the {@link FileChooserCallback} interface, which defines the
   *                 {@link FileChooserCallback#onFileChosen(File)} method.
   */
  public void showSaveFileChooser(FileChooserCallback callback) {
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      callback.onFileChosen(fileChooser.getSelectedFile());
    }
  }

  private void addCheckBoxListeners() {
    ActionListener checkBoxListener = e -> {
    };

    blurSplitCheckBox.addActionListener(checkBoxListener);
    sharpenSplitCheckBox.addActionListener(checkBoxListener);
    sepiaSplitCheckBox.addActionListener(checkBoxListener);
    greyscaleSplitCheckBox.addActionListener(checkBoxListener);
    colorCorrectSplitCheckBox.addActionListener(checkBoxListener);
    adjustLevelsSplitCheckBox.addActionListener(checkBoxListener);
  }

  private void initializeButtons() {
    loadButton = createButton("Load");
    saveButton = createButton("Save");
    undoButton = createButton("Undo");
    revertButton = createButton("Revert to Original");
    extractRedButton = createButton("Extract Red");
    extractGreenButton = createButton("Extract Green");
    extractBlueButton = createButton("Extract Blue");
    extractLumaButton = createButton("Extract Luma");
    extractIntensityButton = createButton("Extract Intensity");
    extractValueButton = createButton("Extract Value");
    blurButton = createButton("Blur");
    sharpenButton = createButton("Sharpen");
    sepiaButton = createButton("Sepia");
    greyscaleButton = createButton("Greyscale");
    flipHorizontalButton = createButton("Flip Horizontal");
    flipVerticalButton = createButton("Flip Vertical");
    adjustBrightnessButton = createButton("Adjust Brightness");
    colorCorrectButton = createButton("Color Correct");
    adjustLevelsButton = createButton("Adjust Levels");
    downscaleButton = createButton("Downscale");
    compressButton = createButton("Compress");
  }

  private JButton createButton(String text) {
    return new JButton(text);
  }

  private void createCheckBoxes() {
    blurSplitCheckBox = new JCheckBox("Blur Preview");
    sharpenSplitCheckBox = new JCheckBox("Sharpen Preview");
    sepiaSplitCheckBox = new JCheckBox("Sepia Preview");
    greyscaleSplitCheckBox = new JCheckBox("Greyscale Preview");
    colorCorrectSplitCheckBox = new JCheckBox("Color Correct Preview");
    adjustLevelsSplitCheckBox = new JCheckBox("Adjust Levels Preview");
  }

  private void addImageEffectsGroup(JPanel panel, GridBagConstraints gbc) {
    gbc.gridy++;
    gbc.gridx = 0;

    panel.add(blurButton, gbc);
    gbc.gridx++;
    panel.add(blurSplitCheckBox, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(sharpenButton, gbc);
    gbc.gridx++;
    panel.add(sharpenSplitCheckBox, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(sepiaButton, gbc);
    gbc.gridx++;
    panel.add(sepiaSplitCheckBox, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(greyscaleButton, gbc);
    gbc.gridx++;
    panel.add(greyscaleSplitCheckBox, gbc);

    gbc.gridy++;
    gbc.insets = new Insets(10, 5, 5, 5);
  }

  private void addAdjustmentsGroup(JPanel panel, GridBagConstraints gbc) {
    gbc.gridy++;
    gbc.gridx = 0;

    panel.add(colorCorrectButton, gbc);
    gbc.gridx++;
    panel.add(colorCorrectSplitCheckBox, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(adjustLevelsButton, gbc);
    gbc.gridx++;
    panel.add(adjustLevelsSplitCheckBox, gbc);

    gbc.gridy++;
    gbc.insets = new Insets(10, 5, 5, 5);
  }

  private void addButtonGroup(JPanel panel, GridBagConstraints gbc, String title,
      JButton[] buttons) {
    gbc.gridx = 0;
    gbc.gridy++;

    int buttonsPerRow = 2;
    for (int i = 0; i < buttons.length; i++) {
      gbc.gridx = i % buttonsPerRow;
      gbc.gridy += (i % buttonsPerRow == 0 && i != 0) ? 1 : 0;

      gbc.insets = new Insets(5, 5, 5, 5);
      buttons[i].setMinimumSize(new Dimension(100, 30));
      panel.add(buttons[i], gbc);
    }

    gbc.gridy++;
    gbc.insets = new Insets(10, 5, 5, 5);
  }

  public void createButtons(ActionListener listener) {
    loadButton.addActionListener(listener);
    saveButton.addActionListener(listener);
    undoButton.addActionListener(listener);
    revertButton.addActionListener(listener);
    extractRedButton.addActionListener(listener);
    extractGreenButton.addActionListener(listener);
    extractBlueButton.addActionListener(listener);
    extractLumaButton.addActionListener(listener);
    extractIntensityButton.addActionListener(listener);
    extractValueButton.addActionListener(listener);

    blurButton.addActionListener(e -> {
      if (blurSplitCheckBox.isSelected()) {
        listener.actionPerformed(
            new ActionEvent(blurButton, ActionEvent.ACTION_PERFORMED, "Blur Preview"));
      } else {
        listener.actionPerformed(new ActionEvent(blurButton, ActionEvent.ACTION_PERFORMED, "Blur"));
      }
    });

    sharpenButton.addActionListener(e -> {
      if (sharpenSplitCheckBox.isSelected()) {
        listener.actionPerformed(
            new ActionEvent(sharpenButton, ActionEvent.ACTION_PERFORMED, "Sharpen Preview"));
      } else {
        listener.actionPerformed(
            new ActionEvent(sharpenButton, ActionEvent.ACTION_PERFORMED, "Sharpen"));
      }
    });

    sepiaButton.addActionListener(e -> {
      if (sepiaSplitCheckBox.isSelected()) {
        listener.actionPerformed(
            new ActionEvent(sepiaButton, ActionEvent.ACTION_PERFORMED, "Sepia Preview"));
      } else {
        listener.actionPerformed(
            new ActionEvent(sepiaButton, ActionEvent.ACTION_PERFORMED, "Sepia"));
      }
    });

    greyscaleButton.addActionListener(e -> {
      if (greyscaleSplitCheckBox.isSelected()) {
        listener.actionPerformed(
            new ActionEvent(greyscaleButton, ActionEvent.ACTION_PERFORMED, "Greyscale Preview"));
      } else {
        listener.actionPerformed(
            new ActionEvent(greyscaleButton, ActionEvent.ACTION_PERFORMED, "Greyscale"));
      }
    });

    colorCorrectButton.addActionListener(e -> {
     if (colorCorrectSplitCheckBox.isSelected()) {
        listener.actionPerformed(new ActionEvent(colorCorrectButton, ActionEvent.ACTION_PERFORMED,
            "Color Correct Preview"));
      } else {
        listener.actionPerformed(
            new ActionEvent(colorCorrectButton, ActionEvent.ACTION_PERFORMED, "Color Correct"));
      }
    });

    adjustLevelsButton.addActionListener(e -> {
      if (adjustLevelsSplitCheckBox.isSelected()) {
        listener.actionPerformed(new ActionEvent(adjustLevelsButton, ActionEvent.ACTION_PERFORMED,
            "Adjust Levels Preview"));
      } else {
        listener.actionPerformed(
            new ActionEvent(adjustLevelsButton, ActionEvent.ACTION_PERFORMED, "Adjust Levels"));
      }
    });

    flipHorizontalButton.addActionListener(listener);
    flipVerticalButton.addActionListener(listener);
    adjustBrightnessButton.addActionListener(listener);
    downscaleButton.addActionListener(listener);
    compressButton.addActionListener(listener);
  }

  /**
   * Sets the icon of the main image label to the specified {@code ImageIcon}.
   *
   * @param icon the {@code ImageIcon} to be displayed in the main image label.
   */
  public void setImageIcon(ImageIcon icon) {
    imageLabel.setIcon(icon);
  }

  /**
   * Sets the icon of the histogram label to the specified {@code ImageIcon}.
   *
   * @param icon the {@code ImageIcon} to be displayed in the histogram label.
   */
  public void setHistogramIcon(ImageIcon icon) {
    histogramLabel.setIcon(icon);
  }

  /**
   * Displays an error dialog with the specified error message.
   *
   * @param message the error message to be displayed in the dialog.
   */
  public void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Unselects all checkboxes used for split-view options in the interface.
   */
  public void untickCheckBoxes() {
    blurSplitCheckBox.setSelected(false);
    sharpenSplitCheckBox.setSelected(false);
    sepiaSplitCheckBox.setSelected(false);
    greyscaleSplitCheckBox.setSelected(false);
    colorCorrectSplitCheckBox.setSelected(false);
    adjustLevelsSplitCheckBox.setSelected(false);
  }

  /**
   * Displays an information dialog with the specified message.
   *
   * @param message the information message to be displayed in the dialog.
   */
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
  }

}