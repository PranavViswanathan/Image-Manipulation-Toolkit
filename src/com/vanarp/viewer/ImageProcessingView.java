package com.vanarp.viewer;

import com.vanarp.controller.CommandProcessor;
import com.vanarp.controller.CompressedImageIO;
import com.vanarp.controller.ImageCommandProcessor;
import com.vanarp.controller.ImageFileIO;
import com.vanarp.controller.ImageProcessingController;
import com.vanarp.controller.UncompressedImageIO;
import com.vanarp.model.Filtering;
import com.vanarp.model.ImageCompression;
import com.vanarp.model.ImageCompressionFunctionality;
import com.vanarp.model.ImageRepresentation;
import com.vanarp.model.Operations;
import com.vanarp.model.Transform;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 * The ImageProcessingView class represents the graphical user interface for the image processing
 * application. It allows users to interact with the application by loading, saving, and
 * manipulating images.
 */
public class ImageProcessingView extends JFrame {

  private final JLabel imageLabel;
  private final JLabel histogramLabel;
  private ImageProcessingController controller;

  /**
   * Constructs an ImageProcessingView with the specified controller.
   *
   */
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

    JPanel buttonPanel = createButtonPanel();

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(buttonPanel, BorderLayout.WEST);
    mainPanel.add(imageAndHistogramPane, BorderLayout.CENTER);

    add(mainPanel, BorderLayout.CENTER);
    addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent e) {
        promptBeforeExit();
      }
    });
  }

  /**
   * Creates the button panel containing action buttons for image processing.
   *
   * @return the JPanel containing buttons
   */
  private JPanel createButtonPanel() {
    JPanel buttonPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.gridx = 0;
    gbc.gridy = 0;

    gbc.gridwidth = 2;
    buttonPanel.add(createButton("Load Image", e -> controller.loadImage()), gbc);
    gbc.gridwidth = 1;
    gbc.gridy++;
    buttonPanel.add(createButton("Save Image", e -> controller.saveImage()), gbc);
    gbc.gridy++;
    buttonPanel.add(createButton("Undo", e -> controller.undo()), gbc);
    gbc.gridy++;
    buttonPanel.add(createButton("Revert to Original", e -> controller.revertToOriginal()), gbc);

    gbc.gridy++;
    buttonPanel.add(createLabel("Extract Color Components:"), gbc);
    gbc.gridy++;
    buttonPanel.add(createButton("Extract Red", e -> controller.extractComponent("red")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Extract Green", e -> controller.extractComponent("green")), gbc);
    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createButton("Extract Blue", e -> controller.extractComponent("blue")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Extract Luma", e -> controller.extractComponent("luma")), gbc);
    gbc.gridx = 0;
    buttonPanel.add(
        createButton("Extract Intensity", e -> controller.extractComponent("intensity")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Extract Value", e -> controller.extractComponent("value")), gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createLabel("Filters:"), gbc);
    gbc.gridy++;
    buttonPanel.add(createButton("Blur", e -> controller.applyFilter("blur")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Sharpen", e -> controller.applyFilter("sharpen")), gbc);
    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createButton("Sepia", e -> controller.applyFilter("sepia")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Greyscale", e -> controller.applyFilter("greyscale")), gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createLabel("Transformations:"), gbc);
    gbc.gridy++;
    buttonPanel.add(createButton("Flip Horizontal", e -> controller.flipImage("horizontal")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Flip Vertical", e -> controller.flipImage("vertical")), gbc);
    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createButton("Adjust Brightness", e -> controller.brightenImage()), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Compress Image", e -> {
      String input = JOptionPane.showInputDialog(this, "Enter the compression percentage (0-100):",
          "Compress Image", JOptionPane.PLAIN_MESSAGE);
      if (input != null) {
        try {
          int percentage = Integer.parseInt(input);
          if (percentage < 0 || percentage > 100) {
            showErrorDialog("Please enter a valid percentage between 0 and 100.");
          } else {
            controller.compressImage(percentage);
          }
        } catch (NumberFormatException ex) {
          showErrorDialog("Invalid input. Please enter a valid number.");
        } catch (IllegalArgumentException ex) {
          showErrorDialog("Invalid argument: " + ex.getMessage());
        }
      }
    }), gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createButton("Generate Histogram", e -> controller.generateHistogram()), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Color Correct", e -> controller.colorCorrectImage()), gbc);
    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createButton("Adjust Levels", e -> controller.adjustLevels()), gbc);

    return buttonPanel;
  }

  private JButton createButton(String text, ActionListener action) {
    JButton button = new JButton(text);
    button.addActionListener(action);
    return button;
  }

  private JLabel createLabel(String text) {
    return new JLabel(text);
  }

  public void setImage(ImageRepresentation image) {
    imageLabel.setIcon(new ImageIcon(image.toBufferedImage()));
  }

  public void setHistogram(ImageRepresentation histogram) {
    histogramLabel.setIcon(new ImageIcon(histogram.toBufferedImage()));
  }

  public void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public void promptBeforeExit() {
    int option = JOptionPane.showConfirmDialog(
        this,
        "Do you want to save the image before exiting?",
        "Exit Confirmation",
        JOptionPane.YES_NO_CANCEL_OPTION);

    if (option == JOptionPane.YES_OPTION) {
      controller.saveImage();
      System.exit(0);
    } else if (option == JOptionPane.NO_OPTION) {
      System.exit(0);
    }
  }

  public static void main(String[] args) {
    ImageFileIO compressedIO = new CompressedImageIO();
    ImageFileIO uncompressedIO = new UncompressedImageIO();
    Transform transformation = new Transform();
    Filtering filtering = new Filtering();
    ImageCompressionFunctionality compress = new ImageCompression();

    Operations operations = new Operations(transformation, filtering, compressedIO, uncompressedIO,
        compress);
    SwingUtilities.invokeLater(() -> {
      ImageCommandProcessor commandProcessor = new CommandProcessor(operations);
      ImageProcessingView gui = new ImageProcessingView();
      ImageProcessingController controller1 = new ImageProcessingController(commandProcessor,gui);

      gui.setVisible(true);
    });
  }
}