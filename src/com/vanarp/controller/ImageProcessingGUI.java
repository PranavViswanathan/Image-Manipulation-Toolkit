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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class ImageProcessingGUI extends JFrame {

  private JLabel imageLabel;
  private ImageRepresentation loadedImage; // Store the currently displayed image
  private ImageCache imageCache; // Use ImageCache instead of HashMap
  private JComboBox<String> imageSelector; // Dropdown to select images
  private ImageCommandProcessor commandProcessor;

  public ImageProcessingGUI(ImageCommandProcessor commandProcessor) {
    this.commandProcessor = commandProcessor; // Initialize the command processor
    this.imageCache = new ImageCache(); // Initialize the ImageCache
    setTitle("Image Processing GUI");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(JLabel.CENTER);

    // Create a panel for the histogram
    JPanel histogramPanel = new JPanel();
    histogramPanel.setPreferredSize(new Dimension(200, 600));

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(imageLabel),
            new JScrollPane(histogramPanel));
    splitPane.setDividerLocation(600);
    add(splitPane, BorderLayout.CENTER);

    // Button panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(3, 3, 10, 10));

    JButton loadImageButton = new JButton("Load Image");
    loadImageButton.addActionListener(e -> loadImage());
    buttonPanel.add(loadImageButton);

    // Image selector dropdown
    imageSelector = new JComboBox<>();
    imageSelector.addActionListener(e -> selectImage());
    buttonPanel.add(imageSelector);

    JButton saveImageButton = new JButton("Save Image");
    saveImageButton.addActionListener(e -> saveImage());
    buttonPanel.add(saveImageButton);

    // Add buttons for extracting color components
    JButton extractRedButton = new JButton("Extract Red");
    extractRedButton.addActionListener(e -> extractComponent("red"));
    buttonPanel.add(extractRedButton);

    JButton extractGreenButton = new JButton("Extract Green");
    extractGreenButton.addActionListener(e -> extractComponent("green"));
    buttonPanel.add(extractGreenButton);

    JButton extractBlueButton = new JButton("Extract Blue");
    extractBlueButton.addActionListener(e -> extractComponent("blue"));
    buttonPanel.add(extractBlueButton);

    // Add specific filter buttons
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

    JButton flipImageButton = new JButton("Flip Image");
    flipImageButton.addActionListener(e -> flipImage("horizontal")); // Example direction
    buttonPanel.add(flipImageButton);

    JButton brightenImageButton = new JButton("Brighten Image");
    brightenImageButton.addActionListener(e -> brightenImage());
    buttonPanel.add(brightenImageButton);

    JButton compressImageButton = new JButton("Compress Image");
    compressImageButton.addActionListener(e -> compressImage(50)); // Example percentage
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

  private void loadImage() {
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      String imageName = file.getName(); // Use file name as key
      try {
        commandProcessor.loadImage(file.getAbsolutePath(), imageName); // Load image through command processor
        loadedImage = commandProcessor.getImage(imageName); // Get the image using command processor
        if (loadedImage != null) {
          imageCache.putImage(imageName, loadedImage); // Store the image in the cache
          imageSelector.addItem(imageName); // Add the image name to the dropdown
          selectImage(); // Update the displayed image
        } else {
          JOptionPane.showMessageDialog(this, "Failed to load image.");
        }
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to load image: " + e.getMessage());
      }
    }
  }

  private void selectImage() {
    String selectedImageName = (String) imageSelector.getSelectedItem();
    if (selectedImageName != null) {
      try {
        loadedImage = imageCache.getImage(selectedImageName); // Get image from the cache
        // Assuming loadedImage has a method to get a BufferedImage
        BufferedImage bufferedImage = loadedImage.toBufferedImage(); // Convert to BufferedImage
        imageLabel.setIcon(new ImageIcon(bufferedImage)); // Create ImageIcon and set it
      } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
      }
    }
  }

  private void saveImage() {
    String selectedImageName = (String) imageSelector.getSelectedItem();
    if (selectedImageName != null) {
      JFileChooser fileChooser = new JFileChooser();
      if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        String format = "png";
        try {
          commandProcessor.saveImage(selectedImageName, file.getAbsolutePath(), format);
          JOptionPane.showMessageDialog(this, "Image saved successfully.");
        } catch (IOException e) {
          JOptionPane.showMessageDialog(this, "Failed to save image: " + e.getMessage());
        }
      }
    }
  }

  private void extractComponent(String componentType) {
    String selectedImageName = (String) imageSelector.getSelectedItem();
    if (selectedImageName != null) {
      String destName = selectedImageName.substring(0, selectedImageName.lastIndexOf('.'))
              + "_" + componentType
              + selectedImageName.substring(selectedImageName.lastIndexOf('.'));
      try {
        // Call the command processor to extract the component
        commandProcessor.extractComponent(selectedImageName, destName, componentType);

        // Retrieve the extracted image using the command processor
        loadedImage = commandProcessor.getImage(destName); // Get the new image

        // Store the new image in cache
        imageCache.putImage(destName, loadedImage);

        // Add new image to dropdown
        imageSelector.addItem(destName);

        // Use toBufferedImage to get the BufferedImage for display
        imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage())); // Update the displayed image

        JOptionPane.showMessageDialog(this, componentType + " component extracted successfully.");
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to extract component: " + e.getMessage());
      }
    } else {
      JOptionPane.showMessageDialog(this, "No image selected.");
    }
  }

  private void applyFilter(String filterType) {
    String selectedImageName = (String) imageSelector.getSelectedItem();
    if (selectedImageName != null) {
      String destName = selectedImageName.substring(0, selectedImageName.lastIndexOf('.'))
              + "_" + filterType + selectedImageName.substring(selectedImageName.lastIndexOf('.'));

      try {
        // Call the command processor to apply the filter
        commandProcessor.applyFilter(selectedImageName, destName, filterType, null);

        // Retrieve the new image using the command processor
        loadedImage = commandProcessor.getImage(destName);

        // Store the new image in cache
        imageCache.putImage(destName, loadedImage);

        // Add new image to dropdown
        imageSelector.addItem(destName);

        // Use toBufferedImage to get the BufferedImage for display
        imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage())); // Update the displayed image

        JOptionPane.showMessageDialog(this, filterType + " filter applied successfully.");
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to apply filter: " + e.getMessage());
      }
    } else {
      JOptionPane.showMessageDialog(this, "No image selected.");
    }
  }
  private void flipImage(String direction) {
    String selectedImageName = (String) imageSelector.getSelectedItem();
    if (selectedImageName != null) {
      String destName =
              selectedImageName.substring(0, selectedImageName.lastIndexOf('.')) + "_flipped"
                      + selectedImageName.substring(selectedImageName.lastIndexOf('.'));

      try {
        // Call the command processor to flip the image
        commandProcessor.flipImage(selectedImageName, destName, direction);

        // Retrieve the new image using the command processor
        loadedImage = commandProcessor.getImage(destName); // Get the new image

        // Store the new image in cache
        imageCache.putImage(destName, loadedImage);

        // Add new image to dropdown
        imageSelector.addItem(destName);

        // Use toBufferedImage to get the BufferedImage for display
        imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage())); // Update the displayed image

        JOptionPane.showMessageDialog(this, "Image flipped successfully.");
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to flip image: " + e.getMessage());
      }
    } else {
      JOptionPane.showMessageDialog(this, "No image selected.");
    }
  }
  private void brightenImage() {
    String selectedImageName = (String) imageSelector.getSelectedItem();
    if (selectedImageName != null) {
      String input = JOptionPane.showInputDialog(this, "Enter the increment value for brightness:",
              "Brighten Image", JOptionPane.PLAIN_MESSAGE);

      if (input != null) {
        try {
          int increment = Integer.parseInt(input);
          String destName =
                  selectedImageName.substring(0, selectedImageName.lastIndexOf('.')) + "_brightened"
                          + selectedImageName.substring(selectedImageName.lastIndexOf('.'));

          // Assuming commandProcessor.brightenImage modifies the image and saves it
          commandProcessor.brightenImage(selectedImageName, increment, destName);

          // Retrieve the brightened image using the command processor
          loadedImage = commandProcessor.getImage(destName); // Get the new brightened image
          imageCache.putImage(destName, loadedImage); // Store the new image in cache
          imageSelector.addItem(destName); // Add new image to dropdown

          // Use toBufferedImage to get the BufferedImage for display
          imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage())); // Update the displayed image

          JOptionPane.showMessageDialog(this, "Image brightened successfully.");
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        } catch (IOException e) {
          JOptionPane.showMessageDialog(this, "Failed to brighten image: " + e.getMessage());
        }
      }
    } else {
      JOptionPane.showMessageDialog(this, "No image selected.");
    }
  }

  private void compressImage(int percentage) {
    String selectedImageName = (String) imageSelector.getSelectedItem();
    if (selectedImageName != null) {
      String destName =
              selectedImageName + "_compressed"; // Create a new name for the compressed image
      try {
        // Call the command processor to compress the image
        commandProcessor.compressImage(percentage, selectedImageName, destName); // Compress image through command processor

        // Retrieve the new image using the command processor
        loadedImage = commandProcessor.getImage(destName); // Get the new image

        // Store the new image in cache
        imageCache.putImage(destName, loadedImage);

        // Add new image to dropdown
        imageSelector.addItem(destName);

        // Use toBufferedImage to get the BufferedImage for display
        imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage())); // Update the displayed image

        JOptionPane.showMessageDialog(this, "Image compressed successfully.");
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to compress image: " + e.getMessage());
      }
    } else {
      JOptionPane.showMessageDialog(this, "No image selected.");
    }
  }

  private void generateHistogram() {
    String selectedImageName = (String) imageSelector.getSelectedItem();
    if (selectedImageName != null) {
      String destName = selectedImageName.substring(0, selectedImageName.lastIndexOf('.'))
              + "_histogram" + selectedImageName.substring(selectedImageName.lastIndexOf('.'));
      try {
        // Generate the histogram through the command processor
        commandProcessor.getHistogram(selectedImageName, destName);

        // Load the generated histogram image
        loadedImage = commandProcessor.getImage(destName); // Get the histogram image using command processor

        // Store the histogram in the cache
        imageCache.putImage(destName, loadedImage);

        // Add the histogram to the dropdown
        imageSelector.addItem(destName);

        // Use toBufferedImage to get the BufferedImage for display
        imageLabel.setIcon(new ImageIcon(loadedImage.toBufferedImage())); // Update the displayed image

        JOptionPane.showMessageDialog(this, "Histogram generated successfully.");
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to generate histogram: " + e.getMessage());
      }
    } else {
      JOptionPane.showMessageDialog(this, "No image selected.");
    }
  }

  private void colorCorrectImage() {
    // Implement color correction logic here
    JOptionPane.showMessageDialog(this, "Color correction is not yet implemented.");
  }

  private void adjustLevels() {
    // Implement levels adjustment logic here
    JOptionPane.showMessageDialog(this, "Levels adjustment is not yet implemented.");
  }

  public static void main(String[] args) {
    ImageFileIO compressedIO = new CompressedImageIO();
    ImageFileIO uncompressedIO = new UncompressedImageIO();
    Transform transformation = new Transform();
    Filtering filtering = new Filtering();
    ImageCompressionFunctionality compress = new ImageCompression();

    Operations operations = new Operations(transformation, filtering, compressedIO, uncompressedIO, compress);
    SwingUtilities.invokeLater(() -> {
      ImageCommandProcessor commandProcessor = new CommandProcessor(operations); // Initialize your command processor
      ImageProcessingGUI gui = new ImageProcessingGUI(commandProcessor);
      gui.setVisible(true);
    });
  }
}