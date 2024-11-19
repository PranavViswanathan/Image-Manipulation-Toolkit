package com.vanarp.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

  public ImageProcessingView() {
    setTitle("Image Processing Application");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create panels for image and histogram
    JPanel imagePanel = new JPanel();
    imagePanel.setLayout(new BorderLayout());
    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    imageScrollPane.setPreferredSize(new Dimension(100, 50)); // Set preferred size
    imagePanel.add(imageScrollPane, BorderLayout.CENTER);

    JPanel histogramPanel = new JPanel();
    histogramPanel.setLayout(new BorderLayout());
    histogramLabel = new JLabel();
    histogramLabel.setHorizontalAlignment(JLabel.CENTER);
    JScrollPane histogramScrollPane = new JScrollPane(histogramLabel);
    histogramScrollPane.setPreferredSize(new Dimension(100, 25)); // Set preferred size
    histogramPanel.add(histogramScrollPane, BorderLayout.CENTER);

    // Add panels to the main frame
    JPanel displayPanel = new JPanel();
    displayPanel.setLayout(new BorderLayout());
    displayPanel.add(imagePanel, BorderLayout.CENTER);
    displayPanel.add(histogramPanel, BorderLayout.EAST);
    add(displayPanel, BorderLayout.CENTER);

    // Initialize buttons
    initializeButtons();

    // Control panel with GridBagLayout
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

    // Define button positions in the GridBagLayout
    int row = 0;

    // Add buttons to the control panel using GridBagConstraints
    gbc.gridx = 0;
    gbc.gridy = row;
    controlPanel.add(loadButton, gbc);
    gbc.gridx = 1;
    gbc.gridy = row++;
    controlPanel.add(saveButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = row;
    controlPanel.add(undoButton, gbc);
    gbc.gridx = 1;
    gbc.gridy = row++;
    controlPanel.add(revertButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = row;
    controlPanel.add(extractRedButton, gbc);
    gbc.gridx = 1;
    gbc.gridy = row++;
    controlPanel.add(extractGreenButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = row;
    controlPanel.add(extractBlueButton, gbc);
    gbc.gridx = 1;
    gbc.gridy = row++;
    controlPanel.add(extractLumaButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = row;
    controlPanel.add(extractIntensityButton, gbc);
    gbc.gridx = 1;
    gbc.gridy = row++;
    controlPanel.add(extractValueButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = row;
    controlPanel.add(blurButton, gbc);
    gbc.gridx = 1;
    gbc.gridy = row++;
    controlPanel.add(sharpenButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = row;
    controlPanel.add(sepiaButton, gbc);
    gbc.gridx = 1;
    gbc.gridy = row++;
    controlPanel.add(greyscaleButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = row;
    controlPanel.add(flipHorizontalButton, gbc);
    gbc.gridx = 1;
    gbc.gridy = row++;
    controlPanel.add(flipVerticalButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = row;
    controlPanel.add(adjustBrightnessButton, gbc);
    gbc.gridx = 1;
    gbc.gridy = row++;
    controlPanel.add(colorCorrectButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = row;
    controlPanel.add(adjustLevelsButton, gbc);
    gbc.gridx = 1;
    gbc.gridy = row++;
    controlPanel.add(downscaleButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = row;
    controlPanel.add(compressButton, gbc);

    // Add control panel to the left side
    add(controlPanel, BorderLayout.WEST);
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
    JButton button = new JButton(text);
    return button;
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
}