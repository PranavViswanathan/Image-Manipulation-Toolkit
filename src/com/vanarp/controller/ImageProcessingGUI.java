package com.vanarp.controller;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;


public class ImageProcessingGUI extends JFrame {

  private final JLabel imageLabel;
  private final JLabel histogramLabel;
  private ImageRepresentation loadedImage;
  private String currentImageName;
  private final ImageCommandProcessor commandProcessor;
  private final Stack<Object[]> imageHistory;

  public ImageProcessingGUI(ImageCommandProcessor commandProcessor) {
    this.commandProcessor = commandProcessor;
    this.imageHistory = new Stack<>();
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

    JPanel buttonPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.gridx = 0;
    gbc.gridy = 0;

    gbc.gridwidth = 2;
    buttonPanel.add(createButton("Load Image", e -> loadImage()), gbc);
    gbc.gridwidth = 1;
    gbc.gridy++;
    buttonPanel.add(createButton("Save Image", e -> saveImage()), gbc);
    gbc.gridy++;
    buttonPanel.add(createButton("Undo", e -> undo()), gbc);
    gbc.gridy++;
    buttonPanel.add(createButton("Revert to Original", e -> revertToOriginal()), gbc);

    gbc.gridy++;
    buttonPanel.add(createLabel("Extract Color Components:"), gbc);
    gbc.gridy++;
    buttonPanel.add(createButton("Extract Red", e -> extractComponent("red")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Extract Green", e -> extractComponent("green")), gbc);
    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createButton("Extract Blue", e -> extractComponent("blue")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Extract Luma", e -> extractComponent("luma")), gbc);
    gbc.gridx = 0;
    buttonPanel.add(createButton("Extract Intensity", e -> extractComponent("intensity")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Extract Value", e -> extractComponent("value")), gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createLabel("Filters:"), gbc);
    gbc.gridy++;
    buttonPanel.add(createButton("Blur", e -> applyFilter("blur")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Sharpen", e -> applyFilter("sharpen")), gbc);
    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createButton("Sepia", e -> applyFilter("sepia")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Greyscale", e -> applyFilter("greyscale")), gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createLabel("Transformations:"), gbc);
    gbc.gridy++;
    buttonPanel.add(createButton("Flip Horizontal", e -> flipImage("horizontal")), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Flip Vertical", e -> flipImage("vertical")), gbc);
    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createButton("Adjust Brightness", e -> brightenImage()), gbc);
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
            compressImage(percentage);
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
    buttonPanel.add(createButton("Generate Histogram", e -> generateHistogram()), gbc);
    gbc.gridx = 1;
    buttonPanel.add(createButton("Color Correct", e -> colorCorrectImage()), gbc);
    gbc.gridx = 0;
    gbc.gridy++;
    buttonPanel.add(createButton("Adjust Levels", e -> adjustLevels()), gbc);

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(buttonPanel, BorderLayout.WEST);
    mainPanel.add(imageAndHistogramPane, BorderLayout.CENTER);

    add(mainPanel, BorderLayout.CENTER);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        promptBeforeExit();
      }
    });
  }

  private void promptBeforeExit() {
    int option = JOptionPane.showConfirmDialog(
        this,
        "Do you want to save the image before exiting?",
        "Exit Confirmation",
        JOptionPane.YES_NO_CANCEL_OPTION);

    if (option == JOptionPane.YES_OPTION) {
      saveImage();
      System.exit(0);
    } else if (option == JOptionPane.NO_OPTION) {
      System.exit(0);
    }
  }

  private JButton createButton(String text, ActionListener action) {
    JButton button = new JButton(text);
    button.addActionListener(action);
    return button;
  }

  private JLabel createLabel(String text) {
    return new JLabel(text);
  }

  private void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void loadImage() {
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      currentImageName = file.getName();
      try {
        commandProcessor.loadImage(file.getAbsolutePath(), currentImageName);
        loadedImage = commandProcessor.getImage(currentImageName);
        if (loadedImage != null) {
          // Push the original image onto the stack
          imageHistory.push(new Object[]{loadedImage, currentImageName});
          imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage()));
          generateHistogram();
        } else {
          showErrorDialog("Failed to load image.");
        }
      } catch (IOException e) {
        showErrorDialog("Failed to load image: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        showErrorDialog("Invalid argument: " + e.getMessage());
      }
    }
  }

  private void revertToOriginal() {
    if (!imageHistory.isEmpty()) {
      // Get the first element from the stack (the original image)
      Object[] originalState = imageHistory.firstElement(); // Get the original image state
      loadedImage = (ImageRepresentation) originalState[0];
      currentImageName = (String) originalState[1];
      imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage()));
      generateHistogram();
      JOptionPane.showMessageDialog(this, "Reverted to original image.");
    } else {
      showErrorDialog("No original image to revert to.");
    }
  }

  private void saveImage() {
    if (loadedImage != null) {
      JFileChooser fileChooser = new JFileChooser();
      if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        String format = "png";
        try {
          commandProcessor.saveImage(currentImageName, file.getAbsolutePath(), format);
          JOptionPane.showMessageDialog(this, "Image saved successfully.");
        } catch (IOException e) {
          showErrorDialog("Failed to save image: " + e.getMessage());
        } catch (IllegalArgumentException e) {
          showErrorDialog("Invalid argument: " + e.getMessage());
        }
      }
    } else {
      showErrorDialog("No image to save.");
    }
  }

  private void saveImageState() {
    if (loadedImage != null) {
      imageHistory.push(new Object[]{loadedImage, currentImageName});
    }
  }

  private void undo() {
    if (!imageHistory.isEmpty()) {
      Object[] previousState = imageHistory.pop();
      loadedImage = (ImageRepresentation) previousState[0];
      currentImageName = (String) previousState[1];
      imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage()));
      generateHistogram();
      JOptionPane.showMessageDialog(this, "Undo successful.");
    } else {
      showErrorDialog("No actions to undo.");
    }
  }

  private void extractComponent(String componentType) {
    saveImageState();
    if (loadedImage != null) {
      String destName;
      int lastDotIndex = currentImageName.lastIndexOf('.');

      if (lastDotIndex != -1) {
        destName = currentImageName.substring(0, lastDotIndex) + "_" + componentType
            + currentImageName.substring(lastDotIndex);
      } else {
        destName =
            currentImageName + "_" + componentType;
      }

      try {
        commandProcessor.extractComponent(currentImageName, destName, componentType);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage()));
        generateHistogram();
        JOptionPane.showMessageDialog(this, componentType + " component extracted successfully.");
      } catch (IOException e) {
        showErrorDialog("Failed to extract component: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      showErrorDialog("No image selected.");
    }
  }

  private void applyFilter(String filterType) {
    saveImageState();
    if (loadedImage != null) {
      String destName;
      int lastDotIndex = currentImageName.lastIndexOf('.');

      if (lastDotIndex != -1) {
        destName = currentImageName.substring(0, lastDotIndex) + "_" + filterType
            + currentImageName.substring(lastDotIndex);
      } else {
        destName = currentImageName + "_" + filterType;
      }

      try {
        commandProcessor.applyFilter(currentImageName, destName, filterType, null);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage()));
        generateHistogram();
        JOptionPane.showMessageDialog(this, filterType + " filter applied successfully.");
      } catch (IOException e) {
        showErrorDialog("Failed to apply filter: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      showErrorDialog("No image selected.");
    }
  }

  private void flipImage(String direction) {
    saveImageState();
    if (loadedImage != null) {
      String destName;
      int lastDotIndex = currentImageName.lastIndexOf('.');

      if (lastDotIndex != -1) {
        destName =
            currentImageName.substring(0, lastDotIndex) + "_flipped" + currentImageName.substring(
                lastDotIndex);
      } else {
        destName = currentImageName + "_flipped";
      }

      try {
        commandProcessor.flipImage(currentImageName, destName, direction);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage()));
        generateHistogram();
        JOptionPane.showMessageDialog(this, "Image flipped successfully.");
      } catch (IOException e) {
        showErrorDialog("Failed to flip image: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      showErrorDialog("No image selected.");
    }
  }

  private void brightenImage() {
    saveImageState();
    if (loadedImage != null) {
      String input = JOptionPane.showInputDialog(this, "Enter the increment value for brightness:",
          "Brighten Image", JOptionPane.PLAIN_MESSAGE);
      if (input != null) {
        try {
          int increment = Integer.parseInt(input);
          String destName;
          int lastDotIndex = currentImageName.lastIndexOf('.');

          if (lastDotIndex != -1) {
            destName = currentImageName.substring(0, lastDotIndex) + "_brightened"
                + currentImageName.substring(lastDotIndex);
          } else {
            destName = currentImageName + "_brightened";
          }
          commandProcessor.brightenImage(currentImageName, increment, destName);
          loadedImage = commandProcessor.getImage(destName);
          currentImageName = destName;
          imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage()));
          generateHistogram();
          JOptionPane.showMessageDialog(this, "Image brightened successfully.");
        } catch (NumberFormatException e) {
          showErrorDialog("Invalid input. Please enter a valid number.");
        } catch (IOException e) {
          showErrorDialog("Failed to brighten image: " + e.getMessage());
        } catch (IllegalArgumentException e) {
          showErrorDialog("Invalid argument: " + e.getMessage());
        }
      }
    } else {
      showErrorDialog("No image selected.");
    }
  }

  private void compressImage(int percentage) {
    saveImageState();
    if (loadedImage != null) {
      String destName = currentImageName + "_compressed";
      try {
        commandProcessor.compressImage(percentage, currentImageName, destName);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage()));
        generateHistogram();
        JOptionPane.showMessageDialog(this, "Image compressed successfully.");
      } catch (IOException e) {
        showErrorDialog("Failed to compress image: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      showErrorDialog("No image selected.");
    }
  }

  private void generateHistogram() {
    if (loadedImage != null) {
      String destName = currentImageName.substring(0, currentImageName.lastIndexOf('.'))
          + "_histogram" + currentImageName.substring(currentImageName.lastIndexOf('.'));
      try {
        commandProcessor.getHistogram(currentImageName, destName);
        ImageRepresentation histogramImage = commandProcessor.getImage(destName);
        histogramLabel.setIcon(new ImageIcon(histogramImage.toBufferedImage()));
      } catch (IOException e) {
        showErrorDialog("Failed to generate histogram: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      showErrorDialog("No image selected.");
    }
  }

  private void colorCorrectImage() {
    saveImageState();
    if (loadedImage != null) {
      String destName;
      int lastDotIndex = currentImageName.lastIndexOf('.');

      if (lastDotIndex != -1) {
        destName = currentImageName.substring(0, lastDotIndex) + "_color_corrected"
            + currentImageName.substring(lastDotIndex);
      } else {
        destName = currentImageName + "_color_corrected";
      }

      try {
        commandProcessor.colorCorrectImage(currentImageName, destName, null);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage()));
        generateHistogram();
        JOptionPane.showMessageDialog(this, "Color correction applied successfully.");
      } catch (IOException e) {
        showErrorDialog("Failed to apply color correction: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      showErrorDialog("No image selected.");
    }
  }

  private void adjustLevels() {
    saveImageState();
    if (loadedImage != null) {
      String inputBrightness = JOptionPane.showInputDialog(this,
          "Enter brightness adjustment value:(0-255)",
          "Adjust Levels", JOptionPane.PLAIN_MESSAGE);
      String inputMidtone = JOptionPane.showInputDialog(this,
          "Enter midtone adjustment value:(0-255)",
          "Adjust Levels", JOptionPane.PLAIN_MESSAGE);
      String inputWhitePoint = JOptionPane.showInputDialog(this,
          "Enter white point adjustment value:(0-255)",
          "Adjust Levels", JOptionPane.PLAIN_MESSAGE);

      if (inputBrightness != null && inputMidtone != null && inputWhitePoint != null) {
        try {
          int brightness = Integer.parseInt(inputBrightness);
          int midtone = Integer.parseInt(inputMidtone);
          int whitePoint = Integer.parseInt(inputWhitePoint);

          String destName = currentImageName.substring(0, currentImageName.lastIndexOf('.'))
              + "_levels_adjusted" + currentImageName.substring(currentImageName.lastIndexOf('.'));
          commandProcessor.levelsAdjust(currentImageName, brightness, midtone, whitePoint, destName,
              null);
          loadedImage = commandProcessor.getImage(destName);
          currentImageName = destName;
          imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage()));
          generateHistogram();

          JOptionPane.showMessageDialog(this, "Levels adjusted successfully.");
        } catch (NumberFormatException e) {
          showErrorDialog("Invalid input. Please enter valid numbers.");
        } catch (IOException e) {
          showErrorDialog("Failed to adjust levels: " + e.getMessage());
        } catch (IllegalArgumentException e) {
          showErrorDialog("Invalid argument: " + e.getMessage());
        }
      }
    } else {
      showErrorDialog("No image selected.");
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
      ImageProcessingGUI gui = new ImageProcessingGUI(commandProcessor);
      gui.setVisible(true);
    });
  }
}