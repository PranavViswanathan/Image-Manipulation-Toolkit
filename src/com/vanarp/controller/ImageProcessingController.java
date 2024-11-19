package com.vanarp.controller;

import com.vanarp.model.ImageRepresentation;
import com.vanarp.viewer.ImageProcessingView;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ImageProcessingController {

  private ImageProcessingView view;
  private final ImageCommandProcessor commandProcessor;
  private final Stack<Object[]> imageHistory;
  private ImageRepresentation loadedImage;
  private String currentImageName;

  public ImageProcessingController(ImageCommandProcessor commandProcessor,
      ImageProcessingView view) {
    this.commandProcessor = commandProcessor;
    this.view = view;
    this.imageHistory = new Stack<>();

    setupListeners();
    view.setVisible(true);
  }

  private void setupListeners() {
    view.createButtons(e -> {
      switch (e.getActionCommand()) {
        case "Load Image":
          loadImage();
          break;
        case "Save Image":
          saveImage();
          break;
        case "Undo":
          undo();
          break;
        case "Revert to Original":
          revertToOriginal();
          break;
        case "Extract Red":
          extractComponent("red");
          break;
        case "Extract Green":
          extractComponent("green");
          break;
        case "Extract Blue":
          extractComponent("blue");
          break;
        case "Extract Luma":
          extractComponent("luma");
          break;
        case "Extract Intensity":
          extractComponent("intensity");
          break;
        case "Extract Value":
          extractComponent("value");
          break;
        case "Blur":
          applyFilter("blur");
          break;
        case "Sharpen":
          applyFilter("sharpen");
          break;
        case "Sepia":
          applyFilter("sepia");
          break;
        case "Greyscale":
          applyFilter("greyscale");
          break;
        case "Flip Horizontal":
          flipImage("horizontal");
          break;
        case "Flip Vertical":
          flipImage("vertical");
          break;
        case "Adjust Brightness":
          brightenImage();
          break;
        case "Color Correct":
          colorCorrectImage();
          break;
        case "Adjust Levels":
          adjustLevels();
          break;
        case "Downscale Image":
          downscaleImage();
          break;
        case "Compress Image":
          compressImageDialog();
          break;
      }
    });

    view.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        promptBeforeExit();
      }
    });
  }

  private void compressImageDialog() {
    String input = JOptionPane.showInputDialog(view, "Enter the compression percentage (0-100):",
        "Compress Image", JOptionPane.PLAIN_MESSAGE);
    if (input != null) {
      try {
        int percentage = Integer.parseInt(input);
        if (percentage < 0 || percentage > 100) {
          view.showErrorDialog("Please enter a valid percentage between 0 and 100.");
        } else {
          compressImage(percentage);
        }
      } catch (NumberFormatException ex) {
        view.showErrorDialog("Invalid input. Please enter a valid number.");
      } catch (IllegalArgumentException ex) {
        view.showErrorDialog("Invalid argument: " + ex.getMessage());
      }
    }
  }

  private void loadImage() {
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      currentImageName = file.getName();
      try {
        commandProcessor.loadImage(file.getAbsolutePath(), currentImageName);
        loadedImage = commandProcessor.getImage(currentImageName);
        if (loadedImage != null) {
          imageHistory.push(new Object[]{loadedImage, currentImageName});
          view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
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

  private void saveImage() {
    if (loadedImage != null) {
      JFileChooser fileChooser = new JFileChooser();
      if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        String format = "png";
        try {
          commandProcessor.saveImage(currentImageName, file.getAbsolutePath(), format);
          JOptionPane.showMessageDialog(view, "Image saved successfully.");
        } catch (IOException e) {
          System.out.println(currentImageName + format);
          view.showErrorDialog("Failed to save image: " + e.getMessage());
        } catch (IllegalArgumentException e) {
          view.showErrorDialog("Invalid argument: " + e.getMessage());
        }
      }
    } else {
      view.showErrorDialog("No image to save.");
    }
  }

  private void undo() {
    if (!imageHistory.isEmpty()) {
      Object[] previousState = imageHistory.pop();
      loadedImage = (ImageRepresentation) previousState[0];
      currentImageName = (String) previousState[1];
      view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
      generateHistogram();
      JOptionPane.showMessageDialog(view, "Undo successful.");
    } else {
      view.showErrorDialog("No actions to undo.");
    }
  }

  private void revertToOriginal() {
    if (!imageHistory.isEmpty()) {
      Object[] originalState = imageHistory.firstElement();
      loadedImage = (ImageRepresentation) originalState[0];
      currentImageName = (String) originalState[1];
      view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
      generateHistogram();
      JOptionPane.showMessageDialog(view, "Reverted to original image.");
    } else {
      view.showErrorDialog("No original image to revert to.");
    }
  }

  private void saveImageState() {
    if (loadedImage != null) {
      imageHistory.push(new Object[]{loadedImage, currentImageName});
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
        destName = currentImageName + "_" + componentType;
      }

      try {
        commandProcessor.extractComponent(currentImageName, destName, componentType);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
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
        view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
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
        view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
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

  private void brightenImage() {
    saveImageState();
    if (loadedImage != null) {
      String input = JOptionPane.showInputDialog(view, "Enter the increment value for brightness:",
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
          view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
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

  private void compressImage(int percentage) {
    saveImageState();
    if (loadedImage != null) {
      String destName = currentImageName + "_compressed";
      try {
        commandProcessor.compressImage(percentage, currentImageName, destName);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
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
        view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
        generateHistogram();
        JOptionPane.showMessageDialog(view, "Color correction applied successfully.");
      } catch (IOException e) {
        view.showErrorDialog("Failed to apply color correction: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        view.showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      view.showErrorDialog("No image selected.");
    }
  }

  private void adjustLevels() {
    saveImageState();
    if (loadedImage != null) {
      String inputBrightness = JOptionPane.showInputDialog(view,
          "Enter brightness adjustment value:(0-255)",
          "Adjust Levels", JOptionPane.PLAIN_MESSAGE);
      String inputMidtone = JOptionPane.showInputDialog(view,
          "Enter midtone adjustment value:(0-255)",
          "Adjust Levels", JOptionPane.PLAIN_MESSAGE);
      String inputWhitePoint = JOptionPane.showInputDialog(view,
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
          view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
          generateHistogram();
          JOptionPane.showMessageDialog(view, "Levels adjusted successfully.");
        } catch (NumberFormatException e) {
          view.showErrorDialog("Invalid input. Please enter valid numbers.");
        } catch (IOException e) {
          view.showErrorDialog("Failed to adjust levels: " + e.getMessage());
        } catch (IllegalArgumentException e) {
          view.showErrorDialog("Invalid argument: " + e.getMessage());
        }
      }
    } else {
      view.showErrorDialog("No image selected.");
    }
  }

  private void downscaleImage() {
    saveImageState();
    if (loadedImage != null) {
      String widthInput = JOptionPane.showInputDialog(view, "Enter the new width:",
          "Downscale Image", JOptionPane.PLAIN_MESSAGE);
      String heightInput = JOptionPane.showInputDialog(view, "Enter the new height:",
          "Downscale Image", JOptionPane.PLAIN_MESSAGE);

      if (widthInput != null && heightInput != null) {
        try {
          int newWidth = Integer.parseInt(widthInput);
          int newHeight = Integer.parseInt(heightInput);

          String destName =
              currentImageName.substring(0, currentImageName.lastIndexOf('.')) + "_downscaled"
                  + currentImageName.substring(currentImageName.lastIndexOf('.'));
          commandProcessor.downscaleImage(currentImageName, destName, newWidth, newHeight);

          loadedImage = commandProcessor.getImage(destName);
          currentImageName = destName;
          view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
          generateHistogram();
          JOptionPane.showMessageDialog(view, "Image downscaled successfully.");
        } catch (NumberFormatException e) {
          view.showErrorDialog("Invalid input. Please enter valid numbers.");
        } catch (IOException e) {
          view.showErrorDialog("Failed to downscale image: " + e.getMessage());
        } catch (IllegalArgumentException e) {
          view.showErrorDialog("Invalid argument: " + e.getMessage());
        }
      }
    } else {
      view.showErrorDialog("No image selected.");
    }
  }

  private void generateHistogram() {
    if (loadedImage != null) {
      String destName = currentImageName.substring(0, currentImageName.lastIndexOf('.'))
          + "_histogram" + currentImageName.substring(currentImageName.lastIndexOf('.'));
      try {
        commandProcessor.getHistogram(currentImageName, destName);
        ImageRepresentation histogramImage = commandProcessor.getImage(destName);
        view.setHistogramIcon(new ImageIcon(histogramImage.toBufferedImage()));
      } catch (IOException e) {
        view.showErrorDialog("Failed to generate histogram: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        view.showErrorDialog("Invalid argument: " + e.getMessage());
      }
    } else {
      view.showErrorDialog("No image selected.");
    }
  }

  private void promptBeforeExit() {
    int option = JOptionPane.showConfirmDialog(
        view,
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
}