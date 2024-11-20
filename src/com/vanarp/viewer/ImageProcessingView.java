package com.vanarp.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImageProcessingView extends JFrame {

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

  // Changed radio buttons to checkboxes
  private JCheckBox blurSplitCheckBox;
  private JCheckBox sharpenSplitCheckBox;
  private JCheckBox sepiaSplitCheckBox;
  private JCheckBox greyscaleSplitCheckBox;
  private JCheckBox colorCorrectSplitCheckBox; // New checkbox
  private JCheckBox adjustLevelsSplitCheckBox; // New checkbox

  public ImageProcessingView() {
    setTitle("Image Processing Application");
    setSize(1075, 600);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create panels for image and histogram
    JPanel imagePanel = new JPanel();
    imagePanel.setLayout(new BorderLayout());
    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    imageScrollPane.setPreferredSize(new Dimension(300, 50)); // Set preferred size
    imagePanel.add(imageScrollPane, BorderLayout.CENTER);

    JPanel histogramPanel = new JPanel();
    histogramPanel.setLayout(new BorderLayout());
    histogramLabel = new JLabel();
    histogramLabel.setHorizontalAlignment(JLabel.CENTER);
    JScrollPane histogramScrollPane = new JScrollPane(histogramLabel);
    histogramScrollPane.setPreferredSize(new Dimension(300, 25)); // Set preferred size
    histogramPanel.add(histogramScrollPane, BorderLayout.EAST);

    // Add panels to the main frame
    JPanel displayPanel = new JPanel();
    displayPanel.setLayout(new BorderLayout());
    displayPanel.add(imagePanel, BorderLayout.CENTER);
    displayPanel.add(histogramPanel, BorderLayout.EAST);
    add(displayPanel, BorderLayout.CENTER);

    // Initialize buttons
    initializeButtons();
    createCheckBoxes(); // Initialize checkboxes

    // Control panel with GridBagLayout
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

    // Button Groups
    addButtonGroup(controlPanel, gbc, "File Operations",
        new JButton[]{loadButton, saveButton, undoButton, revertButton});
    addButtonGroup(controlPanel, gbc, "Color Extraction",
        new JButton[]{extractRedButton, extractGreenButton, extractBlueButton, extractLumaButton,
            extractIntensityButton, extractValueButton});
    addButtonGroup(controlPanel, gbc, "Transformations",
        new JButton[]{flipHorizontalButton, flipVerticalButton});
    addButtonGroup(controlPanel, gbc, "Reductions",
        new JButton[]{downscaleButton, compressButton});

    gbc.gridy++; // Move to the next row
    gbc.gridx = 0; // Reset column index
    controlPanel.add(adjustBrightnessButton, gbc); // Add Adjust Brightness button

    // Add image effects group
    addImageEffectsGroup(controlPanel, gbc);

    // Add adjustments group (without Adjust Brightness)
    addAdjustmentsGroup(controlPanel, gbc);

    // Wrap control panel in a JScrollPane
    JScrollPane scrollPane = new JScrollPane(controlPanel);
    scrollPane.setPreferredSize(new Dimension(300, 600));
    add(scrollPane, BorderLayout.WEST);

    // Add action listeners to checkboxes
    addCheckBoxListeners();
  }

  private void addCheckBoxListeners() {
    ActionListener checkBoxListener = e -> {
      // Handle checkbox visibility or other logic if needed
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
    colorCorrectSplitCheckBox = new JCheckBox("Color Correct Preview"); // New checkbox
    adjustLevelsSplitCheckBox = new JCheckBox("Adjust Levels Preview"); // New checkbox
  }

  private void addImageEffectsGroup(JPanel panel, GridBagConstraints gbc) {
    gbc.gridy++; // Move to the next row for the image effects
    gbc.gridx = 0; // Reset column index

    // Add buttons and checkboxes for image effects
    panel.add(blurButton, gbc);
    gbc.gridx++;
    panel.add(blurSplitCheckBox, gbc);

    gbc.gridx = 0; // Reset column index for next effect
    gbc.gridy++; // Move to the next row
    panel.add(sharpenButton, gbc);
    gbc.gridx++;
    panel.add(sharpenSplitCheckBox, gbc);

    gbc.gridx = 0; // Reset column index for next effect
    gbc.gridy++; // Move to the next row
    panel.add(sepiaButton, gbc);
    gbc.gridx++;
    panel.add(sepiaSplitCheckBox, gbc);

    gbc.gridx = 0; // Reset column index for next effect
    gbc.gridy++; // Move to the next row
    panel.add(greyscaleButton, gbc);
    gbc.gridx++;
    panel.add(greyscaleSplitCheckBox, gbc);

    // Add space after the image effects group
    gbc.gridy++;
    gbc.insets = new Insets(10, 5, 5, 5); // Add extra space after the group
  }

  private void addAdjustmentsGroup(JPanel panel, GridBagConstraints gbc) {
    gbc.gridy++; // Move to the next row for the adjustments
    gbc.gridx = 0; // Reset column index

    // Add buttons and checkboxes for adjustments
    panel.add(colorCorrectButton, gbc);
    gbc.gridx++;
    panel.add(colorCorrectSplitCheckBox, gbc); // Add Color Correct checkbox

    // Move to the next row for Adjust Levels
    gbc.gridx = 0; // Reset column index for Adjust Levels
    gbc.gridy++; // Increment row for Adjust Levels
    panel.add(adjustLevelsButton, gbc); // Add Adjust Levels button
    gbc.gridx++;
    panel.add(adjustLevelsSplitCheckBox, gbc); // Add Adjust Levels checkbox

    // Add space after the adjustments group
    gbc.gridy++;
    gbc.insets = new Insets(10, 5, 5, 5); // Add extra space after the group
  }

  private void addButtonGroup(JPanel panel, GridBagConstraints gbc, String title,
      JButton[] buttons) {
    gbc.gridx = 0;
    gbc.gridy++;

    // Arrange buttons in two rows for other groups
    int buttonsPerRow = 2; // Number of buttons per row
    for (int i = 0; i < buttons.length; i++) {
      gbc.gridx = i % buttonsPerRow; // Column index (0 or 1)
      gbc.gridy += (i % buttonsPerRow == 0 && i != 0) ? 1
          : 0; // Move to the next row after filling the current one

      // Add padding around buttons
      gbc.insets = new Insets(5, 5, 5, 5); // Add some padding
      buttons[i].setMinimumSize(new Dimension(100, 30)); // Set minimum size for buttons
      panel.add(buttons[i], gbc);
    }

    // Add space after each button group
    gbc.gridy++;
    gbc.insets = new Insets(10, 5, 5, 5); // Add extra space after the group
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
    blurButton.addActionListener(listener);
    sharpenButton.addActionListener(listener);
    sepiaButton.addActionListener(listener);
    greyscaleButton.addActionListener(listener);
    flipHorizontalButton.addActionListener(listener);
    flipVerticalButton.addActionListener(listener);
    adjustBrightnessButton.addActionListener(listener);
    colorCorrectButton.addActionListener(listener);
    adjustLevelsButton.addActionListener(listener);
    downscaleButton.addActionListener(listener);
    compressButton.addActionListener(listener);
    blurSplitCheckBox.addActionListener(listener);
    sharpenSplitCheckBox.addActionListener(listener);
    sepiaSplitCheckBox.addActionListener(listener);
    greyscaleSplitCheckBox.addActionListener(listener);
    colorCorrectSplitCheckBox.addActionListener(listener); // New checkbox action listener
    adjustLevelsSplitCheckBox.addActionListener(listener); // New checkbox action listener
  }

  public void setImageIcon(ImageIcon icon) {
    imageLabel.setIcon(icon);
  }

  public void setHistogramIcon(ImageIcon icon) {
    histogramLabel.setIcon(icon);
  }

  public void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public void untickCheckBoxes() {
    blurSplitCheckBox.setSelected(false);
    sharpenSplitCheckBox.setSelected(false);
    sepiaSplitCheckBox.setSelected(false);
    greyscaleSplitCheckBox.setSelected(false);
    colorCorrectSplitCheckBox.setSelected(false);
    adjustLevelsSplitCheckBox.setSelected(false);
  }
}