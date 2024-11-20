package com.vanarp.controller;

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
import javax.swing.JSlider;

public class GUIController implements GUIControllerInterface {

  private final GUIView view;
  private final ImageCommandProcessor commandProcessor;
  private final Stack<Object[]> imageHistory;
  private ImageRepresentation loadedImage;
  private String currentImageName;

  public GUIController(ImageCommandProcessor commandProcessor,
      GUIView view) {
    this.commandProcessor = commandProcessor;
    this.view = view;
    this.imageHistory = new Stack<>();
    setupListeners();
    view.setVisible(true);
  }

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
          showError("Please enter a valid percentage between 0 and 100.");
        } else {
          compressImage(percentage);
        }
      } catch (NumberFormatException ex) {
        showError("Invalid input. Please enter a valid number.");
      }
    }
  }

  @Override
  public void loadImage() {
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

  @Override
  public void saveImage() {
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

  @Override
  public void undo() {
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

  @Override
  public void revertToOriginal() {
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

  private void saveImageState() {
    if (loadedImage != null) {
      imageHistory.push(new Object[]{loadedImage, currentImageName});
    }
  }

  @Override
  public void extractComponent(String componentType) {
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

  @Override
  public void applyFilter(String filterType) {
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

  @Override
  public void flipImage(String direction) {
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

  @Override
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

  @Override
  public void compressImage(int percentage) {
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

  @Override
  public void colorCorrectImage() {
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

  @Override
  public void adjustLevels() {
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
          JOptionPane.showMessageDialog(view, "Levels adjusted successfully.");
        } catch (IOException | IllegalArgumentException e) {
          showError("Failed to adjust levels: " + e.getMessage());
        }
      }
    } else {
      showError("No image selected.");
    }
  }

  @Override
  public void downscaleImage() {
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

  @Override
  public void generateHistogram() {
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

  private void applyFilterPreview(String filterType) {
    if (loadedImage != null) {
      createSliderDialog(filterType, (splitPercent, sliderDialog) -> {
        String previewName = generateDestinationName(filterType + "_preview");
        try {
          // Generate the preview image using the specified filter
          commandProcessor.applyFilter(currentImageName, previewName, filterType, splitPercent,
              null);

          // Show the preview dialog with the appropriate apply action
          showPreview(previewName, "Preview - " + filterType, sliderDialog, (name) -> {
            // Apply the filter permanently
            applyFilter(filterType); // Call applyFilter directly here
          });
        } catch (IOException | IllegalArgumentException e) {
          showError("Failed to generate preview: " + e.getMessage());
        }
      });
    } else {
      showError("No image selected.");
    }
  }

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

  private void showPreview(String previewName, String title, JDialog sliderDialog,
      ApplyAction applyAction) {
    ImageRepresentation previewImage = commandProcessor.getImage(previewName);
    JDialog previewDialog = new JDialog(view, title, true);
    previewDialog.setSize(600, 400);
    previewDialog.setLayout(new BorderLayout());

    JLabel previewLabel = new JLabel(new ImageIcon(previewImage.toBufferedImage()));
    previewDialog.add(previewLabel, BorderLayout.CENTER);

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
        System.out.println(previewName);
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

    // Add the button panel to the SOUTH of the preview dialog
    previewDialog.add(buttonPanel, BorderLayout.SOUTH);

    previewDialog.setLocationRelativeTo(view);
    previewDialog.setVisible(true);
    view.untickCheckBoxes();
  }

  private String generateDestinationName(String suffix) {
    int lastDotIndex = currentImageName.lastIndexOf('.');
    return (lastDotIndex != -1 ? currentImageName.substring(0, lastDotIndex) : currentImageName)
        + "_" + suffix + (lastDotIndex != -1 ? currentImageName.substring(lastDotIndex) : "");
  }

  private void showError(String message) {
    view.showErrorDialog(message);
  }

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

  @FunctionalInterface
  private interface SliderAction {

    void execute(int splitPercent, JDialog sliderDialog);
  }

  @FunctionalInterface
  private interface ApplyAction {

    void apply(String previewName) throws IOException;
  }
}