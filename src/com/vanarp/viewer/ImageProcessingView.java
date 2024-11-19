package com.vanarp.viewer;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class ImageProcessingView extends JFrame {

  private final JLabel imageLabel;
  private final JLabel histogramLabel;
  private final JPanel buttonPanel;

  public ImageProcessingView() {
    setTitle("Image Processing GUI");
    setSize(1000, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    histogramLabel = new JLabel();
    histogramLabel.setHorizontalAlignment(JLabel.CENTER);

    JPanel imagePanel = new JPanel(new BorderLayout());
    imagePanel.add(imageLabel, BorderLayout.CENTER);
    imagePanel.setBorder(BorderFactory.createTitledBorder("Image"));

    JPanel histogramPanel = new JPanel(new BorderLayout());
    histogramPanel.add(histogramLabel, BorderLayout.CENTER);
    histogramPanel.setBorder(BorderFactory.createTitledBorder("Histogram"));

    JSplitPane imageAndHistogramPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imagePanel,
        histogramPanel);
    imageAndHistogramPane.setDividerLocation(600);

    buttonPanel = new JPanel(new GridBagLayout());
    add(buttonPanel, BorderLayout.WEST);
    add(imageAndHistogramPane, BorderLayout.CENTER);
  }

  public void addButton(String text, ActionListener action) {
    JButton button = new JButton(text);
    button.addActionListener(action);
    buttonPanel.add(button);
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

  public void setVisible(boolean visible) {
    super.setVisible(visible);
  }

  public void addWindowListener(WindowAdapter windowAdapter) {
    super.addWindowListener(windowAdapter);
  }

  public void createButtons(ActionListener actionListener) {
    String[] buttonLabels = {
        "Load Image", "Save Image", "Undo", "Revert to Original",
        "Extract Red", "Extract Green", "Extract Blue", "Extract Luma",
        "Extract Intensity", "Extract Value", "Blur", "Sharpen",
        "Sepia", "Greyscale", "Flip Horizontal", "Flip Vertical",
        "Adjust Brightness", "Color Correct", "Adjust Levels",
        "Downscale Image", "Compress Image"
    };

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5); // Add some padding
    gbc.gridx = 0; // Align all buttons to the left
    gbc.gridy = 0; // Start from the first row

    for (String label : buttonLabels) {
      addButton(label, e -> actionListener.actionPerformed(
          new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label)), gbc);
      gbc.gridy++; // Move to the next row for the next button
    }
  }

  private void addButton(String text, ActionListener action, GridBagConstraints gbc) {
    JButton button = new JButton(text);
    button.addActionListener(action);
    buttonPanel.add(button, gbc);
  }
}