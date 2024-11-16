package com.vanarp.controller;

import com.vanarp.model.Filtering;
import com.vanarp.model.ImageCompression;
import com.vanarp.model.ImageCompressionFunctionality;
import com.vanarp.model.ImageRepresentation;
import com.vanarp.model.Operations;
import com.vanarp.model.Transform;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class ImageProcessingGUI extends JFrame {

  private final JLabel imageLabel;
  private final JLabel histogramLabel;
  private ImageRepresentation loadedImage;
  private String currentImageName;
  private final ImageCommandProcessor commandProcessor;

  public ImageProcessingGUI(ImageCommandProcessor commandProcessor) {
    this.commandProcessor = commandProcessor;
    ImageCache imageCache = new ImageCache();
    setTitle("Image Processing GUI");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(JLabel.CENTER);

    histogramLabel = new JLabel();
    histogramLabel.setHorizontalAlignment(JLabel.CENTER);

    JPanel histogramPanel = new JPanel();
    histogramPanel.setPreferredSize(new Dimension(200, 600));
    histogramPanel.add(histogramLabel);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(imageLabel),
        new JScrollPane(histogramPanel));
    splitPane.setDividerLocation(600);
    add(splitPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(4, 4, 10, 10));

    JButton loadImageButton = new JButton("Load Image");
    loadImageButton.addActionListener(e -> loadImage());
    buttonPanel.add(loadImageButton);

    JButton saveImageButton = new JButton("Save Image");
    saveImageButton.addActionListener(e -> saveImage());
    buttonPanel.add(saveImageButton);

    JButton extractRedButton = new JButton("Extract Red");
    extractRedButton.addActionListener(e -> extractComponent("red"));
    buttonPanel.add(extractRedButton);

    JButton extractGreenButton = new JButton("Extract Green");
    extractGreenButton.addActionListener(e -> extractComponent("green"));
    buttonPanel.add(extractGreenButton);

    JButton extractBlueButton = new JButton("Extract Blue");
    extractBlueButton.addActionListener(e -> extractComponent("blue"));
    buttonPanel.add(extractBlueButton);

    JButton extractLumaButton = new JButton("Extract Luma");
    extractLumaButton.addActionListener(e -> extractComponent("luma"));
    buttonPanel.add(extractLumaButton);

    JButton extractIntensityButton = new JButton("Extract Intensity");
    extractIntensityButton.addActionListener(e -> extractComponent("intensity"));
    buttonPanel.add(extractIntensityButton);

    JButton extractValueButton = new JButton("Extract Value");
    extractValueButton.addActionListener(e -> extractComponent("value"));
    buttonPanel.add(extractValueButton);

    JButton blurButton = new JButton("Blur");
    blurButton.addActionListener(e -> applyFilter("blur"));
    buttonPanel.add(blurButton);

    JButton sharpenButton = new JButton("Sharpen");
    sharpenButton.addActionListener(e -> applyFilter("sharpen"));
    buttonPanel.add(sharpenButton);

    JButton sepiaButton = new JButton("Sepia");
    sepiaButton.addActionListener(e -> applyFilter("sepia"));
    buttonPanel.add(sepiaButton);

    JButton greyscaleButton = new JButton("Greyscale");
    greyscaleButton.addActionListener(e -> applyFilter("greyscale"));
    buttonPanel.add(greyscaleButton);

    JButton flipHorizontalButton = new JButton("Flip Horizontal");
    flipHorizontalButton.addActionListener(e -> flipImage("horizontal"));
    buttonPanel.add(flipHorizontalButton);

    JButton flipVerticalButton = new JButton("Flip Vertical");
    flipVerticalButton.addActionListener(e -> flipImage("vertical"));
    buttonPanel.add(flipVerticalButton);

    JButton brightenImageButton = new JButton("Adjust Brightness");
    brightenImageButton.addActionListener(e -> brightenImage());
    buttonPanel.add(brightenImageButton);

    JButton compressImageButton = new JButton("Compress Image");
    compressImageButton.addActionListener(e -> {
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
    });
    buttonPanel.add(compressImageButton);

    JButton generateHistogramButton = new JButton("Generate Histogram");
    generateHistogramButton.addActionListener(e -> generateHistogram());
    buttonPanel.add(generateHistogramButton);

    JButton colorCorrectButton = new JButton("Color Correct");
    colorCorrectButton.addActionListener(e -> colorCorrectImage());
    buttonPanel.add(colorCorrectButton);

    JButton levelsAdjustButton = new JButton("Adjust Levels");
    levelsAdjustButton.addActionListener(e -> adjustLevels());
    buttonPanel.add(levelsAdjustButton);
    
    add(buttonPanel, BorderLayout.SOUTH);
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

  private void extractComponent(String componentType) {
    if (loadedImage != null) {
      String destName = currentImageName.substring(0, currentImageName.lastIndexOf('.'))
          + "_" + componentType + currentImageName.substring(currentImageName.lastIndexOf('.'));
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
    if (loadedImage != null) {
      String destName = currentImageName.substring(0, currentImageName.lastIndexOf('.'))
          + "_" + filterType + currentImageName.substring(currentImageName.lastIndexOf('.'));
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
    if (loadedImage != null) {
      String destName =
          currentImageName.substring(0, currentImageName.lastIndexOf('.')) + "_flipped"
              + currentImageName.substring(currentImageName.lastIndexOf('.'));
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
    if (loadedImage != null) {
      String input = JOptionPane.showInputDialog(this, "Enter the increment value for brightness:",
          "Brighten Image", JOptionPane.PLAIN_MESSAGE);
      if (input != null) {
        try {
          int increment = Integer.parseInt(input);
          String destName =
              currentImageName.substring(0, currentImageName.lastIndexOf('.')) + "_brightened"
                  + currentImageName.substring(currentImageName.lastIndexOf('.'));
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
    if (loadedImage != null) {
      String destName = currentImageName.substring(0, currentImageName.lastIndexOf('.'))
          + "_color_corrected" + currentImageName.substring(currentImageName.lastIndexOf('.'));
      try {
        commandProcessor.colorCorrectImage(currentImageName, destName, null); // No split for now
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