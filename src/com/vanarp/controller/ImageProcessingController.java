package com.vanarp.controller;
import com.vanarp.model.ImageRepresentation;
import com.vanarp.viewer.ImageProcessingView;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ImageProcessingController {

  private final ImageCommandProcessor commandProcessor;
  private final Stack<Object[]> imageHistory;
  private ImageRepresentation loadedImage;
  private String currentImageName;
  private final ImageProcessingView view;

  public ImageProcessingController(ImageCommandProcessor commandProcessor,
      Stack<Object[]> imageHistory, ImageProcessingView view) {
    this.commandProcessor = commandProcessor;
    this.imageHistory = imageHistory;
    this.view = view;
  }

  // Image Loading Methods
  public void loadImage() {
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      currentImageName = file.getName();
      try {
        // Load image through command processor
        commandProcessor.loadImage(file.getAbsolutePath(), currentImageName);
        loadedImage = commandProcessor.getImage(currentImageName);

        if (loadedImage != null) {
          // Save original image to history
          imageHistory.push(new Object[]{loadedImage, currentImageName});
          view.setImage(loadedImage);
          generateHistogram();
        } else {
          view.showErrorDialog("Failed to load image.");
        }
      } catch (IOException e) {
        view.showErrorDialog("Failed to load image: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        view.showErrorDialog("Invalid argument: " + e.getMessage());
      }
    }
  }

  // Image Saving Methods
  public void saveImage() {
    if (loadedImage != null) {
      JFileChooser fileChooser = new JFileChooser();
      if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        String format = "png";
        try {
          commandProcessor.saveImage(currentImageName, file.getAbsolutePath(), format);
          JOptionPane.showMessageDialog(view, "Image saved successfully.");
        } catch (IOException e) {
          view.showErrorDialog("Failed to save image: " + e.getMessage());
        } catch (IllegalArgumentException e) {
          view.showErrorDialog("Invalid argument: " + e.getMessage());
        }
      }
    } else {
      view.showErrorDialog("No image to save.");
    }
  }

  // Image History and Undo Methods
  public void undo() {
    if (!imageHistory.isEmpty()) {
      Object[] previousState = imageHistory.pop();
      loadedImage = (ImageRepresentation) previousState[0];
      currentImageName = (String) previousState[1];
      view.setImage(loadedImage);
      generateHistogram();
      JOptionPane.showMessageDialog(view, "Undo successful.");
    } else {
      view.showErrorDialog("No actions to undo.");
    }
  }

  public void revertToOriginal() {
    if (!imageHistory.isEmpty()) {
      Object[] originalState = imageHistory.firstElement();
      loadedImage = (ImageRepresentation) originalState[0];
      currentImageName = (String) originalState[1];
      view.setImage(loadedImage);
      generateHistogram();
      JOptionPane.showMessageDialog(view, "Reverted to original image.");
    } else {
      view.showErrorDialog("No original image to revert to.");
    }
  }

  // Image Processing Methods
  public void extractComponent(String componentType) {
    saveImageState();
    if (loadedImage != null) {
      String destName = generateDestinationName(componentType);
      try {
        commandProcessor.extractComponent(currentImageName, destName, componentType);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        view.setImage(loadedImage);
        generateHistogram();
        JOptionPane.showMessageDialog(view, componentType + " component extracted successfully.");
      } catch (IOException e) {
        view.showErrorDialog("Failed to extract component: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        view.showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      view.showErrorDialog("No image selected.");
    }
  }

  public void applyFilter(String filterType) {
    saveImageState();
    if (loadedImage != null) {
      String destName = generateDestinationName(filterType);
      try {
        commandProcessor.applyFilter(currentImageName, destName, filterType, null);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        view.setImage(loadedImage);
        generateHistogram();
        JOptionPane.showMessageDialog(view, filterType + " filter applied successfully.");
      } catch (IOException e) {
        view.showErrorDialog("Failed to apply filter: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        view.showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      view.showErrorDialog("No image selected.");
    }
  }

  public void flipImage(String direction) {
    saveImageState();
    if (loadedImage != null) {
      String destName = generateDestinationName("flipped");
      try {
        commandProcessor.flipImage(currentImageName, destName, direction);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        view.setImage(loadedImage);
        generateHistogram();
        JOptionPane.showMessageDialog(view, "Image flipped successfully.");
      } catch (IOException e) {
        view.showErrorDialog("Failed to flip image: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        view.showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      view.showErrorDialog("No image selected.");
    }
  }

  public void brightenImage() {
    saveImageState();
    if (loadedImage != null) {
      String input = JOptionPane.showInputDialog(view, "Enter the increment value for brightness:",
          "Brighten Image", JOptionPane.PLAIN_MESSAGE);
      if (input != null) {
        try {
          int increment = Integer.parseInt(input);
          String destName = generateDestinationName("brightened");
          commandProcessor.brightenImage(currentImageName, increment, destName);
          loadedImage = commandProcessor.getImage(destName);
          currentImageName = destName;
          view.setImage(loadedImage);
          generateHistogram();
          JOptionPane.showMessageDialog(view, "Image brightened successfully.");
        } catch (NumberFormatException e) {
          view.showErrorDialog("Invalid input. Please enter a valid number.");
        } catch (IOException e) {
          view.showErrorDialog("Failed to brighten image: " + e.getMessage());
        } catch (IllegalArgumentException e) {
          view.showErrorDialog("Invalid argument: " + e.getMessage());
        }
      }
    } else {
      view.showErrorDialog("No image selected.");
    }
  }

  public void compressImage(int percentage) {
    saveImageState();
    if (loadedImage != null) {
      String destName = generateDestinationName("compressed");
      try {
        commandProcessor.compressImage(percentage, destName, currentImageName);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        view.setImage(loadedImage);
        generateHistogram();
        JOptionPane.showMessageDialog(view, "Image compressed successfully.");
      } catch (IOException e) {
        view.showErrorDialog("Failed to compress image: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        view.showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      view.showErrorDialog("No image selected.");
    }
  }

  public void generateHistogram() {
    if (loadedImage != null) {
      String histogramName = currentImageName + "_histogram";
      try {
        commandProcessor.getHistogram(currentImageName, histogramName);
        ImageRepresentation histogram = commandProcessor.getImage(histogramName);
        view.setHistogram(histogram);
      } catch (IOException e) {
        view.showErrorDialog("Failed to generate histogram: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        view.showErrorDialog("Invalid argument: " + e.getMessage());
      }
    }
  }

  public void colorCorrectImage() {
    saveImageState();
    if (loadedImage != null) {
      String destName = generateDestinationName("color_corrected");
      try {
        commandProcessor.colorCorrectImage(currentImageName, destName, null);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        view.setImage(loadedImage);
        generateHistogram();
        JOptionPane.showMessageDialog(view, "Color correction applied successfully.");
      } catch (IOException e) {
        view.showErrorDialog("Failed to color correct image: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        view.showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      view.showErrorDialog("No image selected.");
    }
  }

  public void adjustLevels() {
    saveImageState(); // Save the current state of the image
    if (loadedImage != null) { // Check if an image is loaded
      // Prompt user for brightness, midtone, and white point adjustments
      String inputBrightness = JOptionPane.showInputDialog(view,
          "Enter brightness adjustment value (0-255):", "Adjust Levels", JOptionPane.PLAIN_MESSAGE);
      String inputMidtone = JOptionPane.showInputDialog(view,
          "Enter midtone adjustment value (0-255):", "Adjust Levels", JOptionPane.PLAIN_MESSAGE);
      String inputWhitePoint = JOptionPane.showInputDialog(view,
          "Enter white point adjustment value (0-255):", "Adjust Levels",
          JOptionPane.PLAIN_MESSAGE);

      if (inputBrightness != null && inputMidtone != null && inputWhitePoint != null) {
        try {
          // Parse the input values
          int brightness = Integer.parseInt(inputBrightness);
          int midtone = Integer.parseInt(inputMidtone);
          int whitePoint = Integer.parseInt(inputWhitePoint);
          String destName = generateDestinationName("levels_adjusted");
          commandProcessor.levelsAdjust(currentImageName, brightness, midtone, whitePoint,
              destName, null);
          loadedImage = commandProcessor.getImage(destName);
          currentImageName = destName;
          view.setImage(loadedImage);
          generateHistogram();

          // Show success message
          JOptionPane.showMessageDialog(view, "Levels adjusted successfully.");
        } catch (NumberFormatException e) {
          view.showErrorDialog(
              "Invalid input. Please enter valid numbers."); // Handle invalid number format
        } catch (IOException e) {
          view.showErrorDialog(
              "Failed to adjust levels: " + e.getMessage()); // Handle IO exceptions
        } catch (IllegalArgumentException e) {
          view.showErrorDialog(
              "Invalid argument: " + e.getMessage()); // Handle invalid argument exceptions
        }
      }
    } else {
      view.showErrorDialog("No image selected."); // Handle case where no image is loaded
    }
  }

  private void saveImageState() {
    if (loadedImage != null) {
      imageHistory.push(new Object[]{loadedImage, currentImageName});
    }
  }

  private String generateDestinationName(String action) {
    return currentImageName + "_" + action;
  }
}