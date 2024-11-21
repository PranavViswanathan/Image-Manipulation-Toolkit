package com.vanarp.controller;

import com.vanarp.model.ImageOperations;
import com.vanarp.model.ImageRepresentation;
import com.vanarp.viewer.GUIView;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

/**
 * GUIController is responsible for managing the interactions between the GUI, image operations, and
 * command processing. It handles user actions such as loading, saving, and manipulating images
 * while maintaining a history of image states.
 */
public class GUIController extends CommandProcessor {

  private final GUIView view;
  private final ImageCommandProcessor commandProcessor;
  private final Stack<Object[]> imageHistory;
  private ImageRepresentation loadedImage;
  private String currentImageName;

  /**
   * Constructs a GUIController with the specified image operations, command processor, and GUI
   * view.
   *
   * @param operations       the ImageOperations to be used for processing image commands
   * @param commandProcessor the command processor that executes image commands
   * @param view             the GUIView for user interaction and display
   */
  public GUIController(ImageOperations operations, ImageCommandProcessor commandProcessor,
      GUIView view) {
    super(operations);
    this.commandProcessor = commandProcessor;
    this.view = view;
    this.imageHistory = new Stack<>();
    setupListeners();
    view.setVisible(true);
  }

  /**
   * Sets up action listeners for the GUI buttons and window events.
   */
  private void setupListeners() {
    view.createButtons(e -> {
      switch (e.getActionCommand()) {
        case "Load":
          loadImage();
          break;
        case "Save":
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
        case "Downscale":
          downscaleImage();
          break;
        case "Compress":
          compressImageDialog();
          break;
        case "Blur Preview":
          applyFilterPreview("blur");
          break;
        case "Sharpen Preview":
          applyFilterPreview("sharpen");
          break;
        case "Sepia Preview":
          applyFilterPreview("sepia");
          break;
        case "Greyscale Preview":
          applyFilterPreview("greyscale");
          break;
        case "Color Correct Preview":
          colorCorrectPreview();
          break;
        case "Adjust Levels Preview":
          adjustLevelsPreview();
          break;
        default:
          System.out.println("Unrecognized action command: " + e.getActionCommand());
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

  /**
   * Opens a dialog for the user to input a compression percentage and compresses the loaded image.
   */
  private void compressImageDialog() {
    String input = JOptionPane.showInputDialog(view, "Enter the compression percentage (0-100):",
        "Compress Image", JOptionPane.PLAIN_MESSAGE);
    if (input != null) {
      try {
        int percentage = Integer.parseInt(input);
        if (percentage < 0 || percentage > 100) {
          showError("Please enter a valid percentage between 0 and 100.");
        } else {
          compressImage(percentage);
        }
      } catch (NumberFormatException ex) {
        showError("Invalid input. Please enter a valid number.");
      }
    }
  }

  /**
   * Loads an image from the file system and updates the GUI with the loaded image.
   */
  private void loadImage() {
    view.showFileChooser((file) -> {
      currentImageName = file.getName();
      try {
        commandProcessor.loadImage(file.getAbsolutePath(), currentImageName);
        loadedImage = commandProcessor.getImage(currentImageName);
        if (loadedImage != null) {
          imageHistory.push(new Object[]{loadedImage, currentImageName});
          view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
          generateHistogram();
        } else {
          showError("Failed to load image.");
        }
      } catch (IOException | IllegalArgumentException e) {
        showError("Failed to load image: " + e.getMessage());
      }
    });
  }

  /**
   * Saves the currently loaded image to a specified file location.
   */
  private void saveImage() {
    if (loadedImage != null) {
      view.showSaveFileChooser((file) -> {
        try {
          commandProcessor.saveImage(currentImageName, file.getAbsolutePath(), "png");
          view.showMessage("Image saved successfully.");
        } catch (IOException | IllegalArgumentException e) {
          showError("Failed to save image: " + e.getMessage());
        }
      });
    } else {
      showError("No image to save.");
    }
  }

  /**
   * Undoes the last image operation by reverting to the previous image state.
   */
  private void undo() {
    if (!imageHistory.isEmpty()) {
      Object[] previousState = imageHistory.pop();
      loadedImage = (ImageRepresentation) previousState[0];
      currentImageName = (String) previousState[1];
      view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
      generateHistogram();
      JOptionPane.showMessageDialog(view, "Undo successful.");
    } else {
      showError("No actions to undo.");
    }
  }

  /**
   * Reverts the loaded image to its original state.
   */
  private void revertToOriginal() {
    if (!imageHistory.isEmpty()) {
      Object[] originalState = imageHistory.firstElement();
      loadedImage = (ImageRepresentation) originalState[0];
      currentImageName = (String) originalState[1];
      view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
      generateHistogram();
      JOptionPane.showMessageDialog(view, "Reverted to original image.");
    } else {
      showError("No original image to revert to.");
    }
  }

  /**
   * Saves the current image state to the history stack.
   */
  private void saveImageState() {
    if (loadedImage != null) {
      imageHistory.push(new Object[]{loadedImage, currentImageName});
    }
  }

  /**
   * Extracts a specific color component from the loaded image.
   *
   * @param componentType the type of color component to extract (e.g., red, green, blue)
   */
  private void extractComponent(String componentType) {
    saveImageState();
    if (loadedImage != null) {
      String destName = generateDestinationName(componentType);
      try {
        commandProcessor.extractComponent(currentImageName, destName, componentType, null);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
        generateHistogram();
        JOptionPane.showMessageDialog(view, componentType + " component extracted successfully.");
      } catch (IOException | IllegalArgumentException e) {
        showError("Failed to extract component: " + e.getMessage());
      }
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Applies a specified filter to the loaded image.
   *
   * @param filterType the type of filter to apply (e.g., blur, sharpen)
   */
  private void applyFilter(String filterType) {
    saveImageState();
    if (loadedImage != null) {
      String destName = generateDestinationName(filterType);
      try {
        commandProcessor.applyFilter(currentImageName, destName, filterType, null, null);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
        generateHistogram();
        JOptionPane.showMessageDialog(view, filterType + " filter applied successfully.");
      } catch (IOException | IllegalArgumentException e) {
        showError("Failed to apply filter: " + e.getMessage());
      }
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Flips the loaded image in the specified direction.
   *
   * @param direction the direction to flip the image (horizontal or vertical)
   */
  private void flipImage(String direction) {
    saveImageState();
    if (loadedImage != null) {
      String destName = generateDestinationName("flipped");
      try {
        commandProcessor.flipImage(currentImageName, destName, direction);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
        generateHistogram();
        JOptionPane.showMessageDialog(view, "Image flipped successfully.");
      } catch (IOException | IllegalArgumentException e) {
        showError("Failed to flip image: " + e.getMessage());
      }
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Brightens the loaded image by a specified increment value.
   */
  private void brightenImage() {
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
          view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
          generateHistogram();
          JOptionPane.showMessageDialog(view, "Image brightened successfully.");
        } catch (IOException | IllegalArgumentException e) {
          showError("Failed to brighten image: " + e.getMessage());
        }
      }
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Compresses the loaded image by a specified percentage.
   *
   * @param percentage the percentage to compress the image (0-100)
   */
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
      } catch (IOException | IllegalArgumentException e) {
        showError("Failed to compress image: " + e.getMessage());
      }
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Applies color correction to the loaded image.
   */
  private void colorCorrectImage() {
    saveImageState();
    if (loadedImage != null) {
      String destName = generateDestinationName("color_corrected");
      try {
        commandProcessor.colorCorrectImage(currentImageName, destName, null);
        loadedImage = commandProcessor.getImage(destName);
        currentImageName = destName;
        view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
        generateHistogram();
        JOptionPane.showMessageDialog(view, "Color correction applied successfully.");
      } catch (IOException | IllegalArgumentException e) {
        showError("Failed to apply color correction: " + e.getMessage());
      }
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Adjusts the brightness, midtone, and white point levels of the loaded image.
   */
  private void adjustLevels() {
    saveImageState();
    if (loadedImage != null) {
      String inputBrightness = JOptionPane.showInputDialog(view,
          "Enter brightness adjustment value (0-255):", "Adjust Levels", JOptionPane.PLAIN_MESSAGE);
      String inputMidtone = JOptionPane.showInputDialog(view,
          "Enter midtone adjustment value (0-255):", "Adjust Levels", JOptionPane.PLAIN_MESSAGE);
      String inputWhitePoint = JOptionPane.showInputDialog(view,
          "Enter white point adjustment value (0-255):", "Adjust Levels",
          JOptionPane.PLAIN_MESSAGE);
      if (inputBrightness != null && inputMidtone != null && inputWhitePoint != null) {
        try {
          int brightness = Integer.parseInt(inputBrightness);
          int midtone = Integer.parseInt(inputMidtone);
          int whitePoint = Integer.parseInt(inputWhitePoint);
          String destName = generateDestinationName("levels_adjusted");
          commandProcessor.levelsAdjust(currentImageName, brightness, midtone, whitePoint, destName,
              null);
          loadedImage = commandProcessor.getImage(destName);
          currentImageName = destName;
          view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
          generateHistogram();
          JOptionPane.showMessageDialog(view, "Levels adjusted successfully .");
        } catch (IOException | IllegalArgumentException e) {
          showError("Failed to adjust levels: " + e.getMessage());
        }
      }
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Downscales the loaded image to specified dimensions.
   */
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
          String destName = generateDestinationName("downscaled");
          commandProcessor.downscaleImage(currentImageName, destName, newWidth, newHeight);
          loadedImage = commandProcessor.getImage(destName);
          currentImageName = destName;
          view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
          generateHistogram();
          JOptionPane.showMessageDialog(view, "Image downscaled successfully.");
        } catch (IOException | IllegalArgumentException e) {
          showError("Failed to downscale image: " + e.getMessage());
        }
      }
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Generates a histogram for the currently loaded image.
   */
  private void generateHistogram() {
    if (loadedImage != null) {
      String destName = generateDestinationName("histogram");
      try {
        commandProcessor.getHistogram(currentImageName, destName);
        ImageRepresentation histogramImage = commandProcessor.getImage(destName);
        view.setHistogramIcon(new ImageIcon(histogramImage.toBufferedImage()));
      } catch (IOException | IllegalArgumentException e) {
        showError("Failed to generate histogram: " + e.getMessage());
      }
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Previews a specified filter effect on the loaded image.
   *
   * @param filterType the type of filter to preview
   */
  private void applyFilterPreview(String filterType) {
    if (loadedImage != null) {
      createSliderDialog(filterType, (splitPercent, sliderDialog) -> {
        String previewName = generateDestinationName(filterType + "_preview");
        try {
          commandProcessor.applyFilter(currentImageName, previewName, filterType, splitPercent,
              null);
          showPreview(previewName, "Preview - " + filterType, sliderDialog, (name) -> {
            applyFilter(filterType);
          });
        } catch (IOException | IllegalArgumentException e) {
          showError("Failed to generate preview: " + e.getMessage());
        }
      });
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Previews color correction on the loaded image.
   */
  private void colorCorrectPreview() {
    if (loadedImage != null) {
      createSliderDialog("Color Correct Image", (splitPercent, sliderDialog) -> {
        String previewName = generateDestinationName("color-correct_preview");
        try {
          commandProcessor.colorCorrectImage(currentImageName, previewName, splitPercent);
          showPreview(previewName, "Preview - Color-Correct", sliderDialog, (name) -> {
            commandProcessor.colorCorrectImage(currentImageName, name, null);
            loadedImage = commandProcessor.getImage(name);
            currentImageName = name;
            view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
            generateHistogram();
          });
        } catch (IOException | IllegalArgumentException e) {
          showError("Failed to generate preview: " + e.getMessage());
        }
      });
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Previews level adjustments on the loaded image.
   */
  private void adjustLevelsPreview() {
    if (loadedImage != null) {
      createSliderDialog("Levels Adjustment", (splitPercent, sliderDialog) -> {
        String inputBrightness = JOptionPane.showInputDialog(view,
            "Enter brightness adjustment value (0-255):", "Adjust Levels",
            JOptionPane.PLAIN_MESSAGE);
        String inputMidtone = JOptionPane.showInputDialog(view,
            "Enter midtone adjustment value (0-255):", "Adjust Levels", JOptionPane.PLAIN_MESSAGE);
        String inputWhitePoint = JOptionPane.showInputDialog(view,
            "Enter white point adjustment value (0-255):", "Adjust Levels",
            JOptionPane.PLAIN_MESSAGE);
        if (inputBrightness != null && inputMidtone != null && inputWhitePoint != null) {
          try {
            int brightness = Integer.parseInt(inputBrightness);
            int midtone = Integer.parseInt(inputMidtone);
            int whitePoint = Integer.parseInt(inputWhitePoint);
            String previewName = generateDestinationName("levels_adjusted_preview");
            commandProcessor.levelsAdjust(currentImageName, brightness, midtone, whitePoint,
                previewName, splitPercent);
            showPreview(previewName, "Preview - Levels Adjusted", sliderDialog, (name) -> {
              commandProcessor.levelsAdjust(currentImageName, brightness, midtone, whitePoint, name,
                  null);
              loadedImage = commandProcessor.getImage(name);
              currentImageName = name;
              view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));
              generateHistogram();
            });
          } catch (IOException | IllegalArgumentException e) {
            showError("Invalid input. Please enter valid numbers.");
          }
        }
      });
    } else {
      showError("No image selected.");
    }
  }

  /**
   * Creates a dialog with a slider for adjusting parameters for filters or adjustments.
   *
   * @param title  the title of the dialog
   * @param action the action to perform when the slider value is used
   */
  private void createSliderDialog(String title, SliderAction action) {
    JDialog sliderDialog = new JDialog(view, "Set Split Percentage for " + title, true);
    sliderDialog.setSize(400, 200);
    sliderDialog.setLayout(new BorderLayout());
    JSlider splitSlider = new JSlider(0, 100, 50);
    splitSlider.setMajorTickSpacing(10);
    splitSlider.setPaintTicks(true);
    splitSlider.setPaintLabels(true);
    sliderDialog.add(splitSlider, BorderLayout.CENTER);
    JButton previewButton = new JButton("Preview");
    previewButton.addActionListener(e -> action.execute(splitSlider.getValue(), sliderDialog));
    sliderDialog.add(previewButton, BorderLayout.SOUTH);
    sliderDialog.setLocationRelativeTo(view);
    sliderDialog.setVisible(true);
  }

  /**
   * Displays a preview of the image after applying a filter or adjustment.
   *
   * @param previewName  the name of the preview image
   * @param title        the title of the preview dialog
   * @param sliderDialog the dialog containing the slider
   * @param applyAction  the action to apply when the user confirms the preview
   */
  private void showPreview(String previewName, String title, JDialog sliderDialog,
      ApplyAction applyAction) {
    ImageRepresentation previewImage = commandProcessor.getImage(previewName);
    JDialog previewDialog = new JDialog(view, title, true);
    previewDialog.setSize(600, 400);
    previewDialog.setLayout(new BorderLayout());

    JLabel previewLabel = new JLabel(new ImageIcon(previewImage.toBufferedImage()));
    JScrollPane scrollPane = new JScrollPane(previewLabel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    previewDialog.add(scrollPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new GridLayout(1, 3));

    JButton closeButton = new JButton("Close");
    JButton applyButton = new JButton("Apply");
    JButton goBackButton = new JButton("Change Value");

    closeButton.addActionListener(ev -> {
      previewDialog.dispose();
      sliderDialog.dispose();
    });

    applyButton.addActionListener(ev -> {
      try {
        applyAction.apply(previewName);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      previewDialog.dispose();
      sliderDialog.dispose();
    });

    goBackButton.addActionListener(ev -> {
      previewDialog.dispose();
      sliderDialog.setVisible(true);
    });

    buttonPanel.add(goBackButton);
    buttonPanel.add(applyButton);
    buttonPanel.add(closeButton);

    previewDialog.add(buttonPanel, BorderLayout.SOUTH);

    previewDialog.setLocationRelativeTo(view);
    previewDialog.setVisible(true);
    view.untickCheckBoxes();
  }

  /**
   * Generates a destination name for a new image based on the current image name and a suffix.
   *
   * @param suffix the suffix to append to the current image name
   * @return the generated destination name
   */
  private String generateDestinationName(String suffix) {
    int lastDotIndex = currentImageName.lastIndexOf('.');
    return (lastDotIndex != -1 ? currentImageName.substring(0, lastDotIndex) : currentImageName)
        + "_" + suffix + (lastDotIndex != -1 ? currentImageName.substring(lastDotIndex) : "");
  }

  /**
   * Displays an error message in a dialog.
   *
   * @param message the error message to display
   */
  private void showError(String message) {
    view.showErrorDialog(message);
  }

  /**
   * Prompts the user to save the image before exiting the application.
   */
  private void promptBeforeExit() {
    int option = JOptionPane.showConfirmDialog(view,
        "Do you want to save the image before exiting?", "Exit Confirmation",
        JOptionPane.YES_NO_CANCEL_OPTION);
    if (option == JOptionPane.YES_OPTION) {
      saveImage();
      System.exit(0);
    } else if (option == JOptionPane.NO_OPTION) {
      System.exit(0);
    }
  }
}