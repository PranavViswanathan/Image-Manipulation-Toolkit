package com.vanarp.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * CLIView is responsible for providing a command-line interface for the image processing
 * application. It allows users to load, save, and manipulate images through various commands.
 */
public class CLIView {

  private final ImageCommandProcessor commandProcessor;
  private final Map<String, Consumer<String[]>> commandMap;
  private static final Map<String, String> EXTENSION_TO_FORMAT = new HashMap<>();

  static {
    EXTENSION_TO_FORMAT.put(".ppm", "PPM");
    EXTENSION_TO_FORMAT.put(".png", "PNG");
    EXTENSION_TO_FORMAT.put(".jpg", "JPG");
    EXTENSION_TO_FORMAT.put(".jpeg", "JPG");
  }

  /**
   * Constructs a CLIView with the specified ImageCommandProcessor.
   *
   * @param commandProcessor the ImageCommandProcessor to be used for processing commands.
   */
  public CLIView(ImageCommandProcessor commandProcessor) {
    this.commandProcessor = commandProcessor;
    this.commandMap = new HashMap<>();
    initializeCommands();
  }

  /**
   * Processes a command input by the user.
   *
   * @param input the command input as a string.
   */
  public void processCommand(String input) {
    String[] tokens = input.split("\\s+");
    if (tokens.length == 0) {
      System.out.println("Invalid command.");
      return;
    }

    String command = tokens[0].toLowerCase();
    Consumer<String[]> commandAction = commandMap.get(command);

    if (commandAction != null) {
      commandAction.accept(tokens);
    } else {
      System.out.println("Unknown command: " + command);
    }
  }


  /**
   * Initializes the command map with available commands and their respective handlers.
   */
  private void initializeCommands() {
    commandMap.put("load", tokens -> handleLoad(tokens));
    commandMap.put("save", tokens -> handleSave(tokens));
    commandMap.put("red-component", tokens -> handleComponent(tokens, "red"));
    commandMap.put("green-component", tokens -> handleComponent(tokens, "green"));
    commandMap.put("blue-component", tokens -> handleComponent(tokens, "blue"));
    commandMap.put("horizontal-flip", tokens -> handleFlip(tokens, "horizontal"));
    commandMap.put("vertical-flip", tokens -> handleFlip(tokens, "vertical"));
    commandMap.put("brighten", tokens -> handleBrighten(tokens));
    commandMap.put("rgb-split", tokens -> handleRgbSplit(tokens));
    commandMap.put("blur", tokens -> handleFilter(tokens, "blur"));
    commandMap.put("sharpen", tokens -> handleFilter(tokens, "sharpen"));
    commandMap.put("sepia", tokens -> handleFilter(tokens, "sepia"));
    commandMap.put("greyscale", tokens -> handleFilter(tokens, "greyscale"));
    commandMap.put("color-correct", tokens -> handleColorCorrect(tokens));
    commandMap.put("levels-adjust", tokens -> handleLevelsAdjust(tokens));
    commandMap.put("value-component", tokens -> handleComponent(tokens, "value"));
    commandMap.put("luma-component", tokens -> handleComponent(tokens, "luma"));
    commandMap.put("intensity-component", tokens -> handleComponent(tokens, "intensity"));
    commandMap.put("rgb-combine", tokens -> handleRgbCombine(tokens));
    commandMap.put("-file", tokens -> handleScript(tokens));
    commandMap.put("histogram", tokens -> handleHistogram(tokens));
    commandMap.put("compress", tokens -> handleCompress(tokens));
    commandMap.put("downscale", tokens -> handleDownscale(tokens));
  }

  /**
   * Handles the load command to load an image from a specified path.
   *
   * @param tokens the command tokens containing the image path and name.
   */
  private void handleLoad(String[] tokens) {
    if (tokens.length == 3) {
      try {
        commandProcessor.loadImage(tokens[1], tokens[2]);
        System.out.println("Image loaded successfully.");
      } catch (IOException e) {
        System.out.println("Failed to load image: " + e.getMessage());
      }
    } else {
      System.out.println("Usage: load <image-path> <image-name>");
    }
  }

  /**
   * Handles the save command to save an image to a specified path.
   *
   * @param tokens the command tokens containing the image name and path.
   */
  private void handleSave(String[] tokens) {
    if (tokens.length == 3) {
      String format = getFormatFromFileName(tokens[2]);
      try {
        commandProcessor.saveImage(tokens[2], tokens[1], format);
        System.out.println("Image saved successfully.");
      } catch (IOException e) {
        System.out.println("Failed to save image: " + e.getMessage());
      }
    } else {
      System.out.println("Usage: save <image-name> <image-path>");
    }
  }

  /**
   * Handles the extraction of a specific color component from an image.
   *
   * @param tokens    the command tokens containing the image name and destination name.
   * @param component the color component to extract (e.g., red, green, blue).
   */
  private void handleComponent(String[] tokens, String component) {
    if (tokens.length >= 3 && tokens.length <= 4) {
      String imageName = tokens[1];
      String destImageName = tokens[2];
      String maskImageName = tokens.length == 4 ? tokens[3] : null;

      try {

        if (maskImageName != null) {
          commandProcessor.extractComponent(imageName, destImageName, component, maskImageName);
          System.out.println(component.substring(0, 1).toUpperCase()
              + component.substring(1)
              + " component applied to the image with mask.");
        } else {
          commandProcessor.extractComponent(imageName, destImageName, component, null);
          System.out.println(component.substring(0, 1).toUpperCase()
              + component.substring(1)
              + " component applied to the image.");
        }
      } catch (IOException e) {
        System.out.println("Failed to apply " + component + " component: " + e.getMessage());
      }
    } else {
      System.out.println(
          "Usage: " + component + "-component <image-name> <dest-image-name> [<mask-image-name>]");
    }
  }

  /**
   * Handles the flipping of an image in a specified direction.
   *
   * @param tokens    the command tokens containing the image name and destination name.
   * @param direction the direction to flip the image (horizontal or vertical).
   */
  private void handleFlip(String[] tokens, String direction) {
    if (tokens.length == 3) {
      try {
        commandProcessor.flipImage(tokens[1], tokens[2], direction);
        System.out.println("Image flipped " + direction + " and saved as " + tokens[2]);
      } catch (IOException e) {
        System.out.println("Failed to flip image " + direction + ": " + e.getMessage());
      }
    } else {
      System.out.println("Usage: " + direction + "-flip <image-name> <dest-image-name>");
    }
  }

  /**
   * Handles the brightening of an image by a specified increment.
   *
   * @param tokens the command tokens containing the increment, image name, and destination name.
   */
  private void handleBrighten(String[] tokens) {
    if (tokens.length == 4) {
      try {
        int increment = Integer.parseInt(tokens[1]);
        commandProcessor.brightenImage(tokens[2], increment, tokens[3]);
        System.out.println("Image brightened by " + increment + " and saved as " + tokens[3]);
      } catch (NumberFormatException e) {
        System.out.println("Invalid increment value. It should be an integer.");
      } catch (IOException e) {
        System.out.println("Failed to brighten image: " + e.getMessage());
      }
    } else {
      System.out.println("Usage: brighten <increment> <image-name> <dest-image-name>");
    }
  }

  /**
   * Handles the RGB splitting of an image into its red, green, and blue components.
   *
   * @param tokens the command tokens containing the image name and destination names for each
   *               component.
   */
  private void handleRgbSplit(String[] tokens) {
    if (tokens.length == 5) {
      try {
        commandProcessor.rgbSplit(tokens[1], tokens[2], tokens[3], tokens[4]);
        System.out.println(
            "RGB split completed. Exists in cache as " + tokens[2] + ", " + tokens[3] + ", "
                + tokens[4]);
      } catch (IOException e) {
        System.out.println("Failed to split RGB: " + e.getMessage());
      }
    } else {
      System.out.println(
          "Usage: rgb-split <image-name> <dest-red-image> <dest-green-image> <dest-blue-image>");
    }
  }

  /**
   * Handles the application of a filter to an image.
   *
   * @param tokens the command tokens containing the image name, destination name, and optional
   *               split percentage.
   * @param filter the filter to apply (e.g., blur, sharpen).
   */
  private void handleFilter(String[] tokens, String filter) {
    if (tokens.length == 3 ||
        (tokens.length == 4) ||
        (tokens.length == 5 && tokens[3].equalsIgnoreCase("split"))) {
      try {
        if (tokens.length == 3) {
          commandProcessor.applyFilter(tokens[1], tokens[2], filter, null, null);
        } else if (tokens.length == 4) {
          String maskImageName = tokens[2]; // mask image is now at index 2
          String destImageName = tokens[3]; // destination image is at index 3
          commandProcessor.applyFilter(tokens[1], destImageName, filter, null, maskImageName);
        } else if (tokens.length == 5 && tokens[3].equalsIgnoreCase("split")) {
          int splitPercent = Integer.parseInt(tokens[4]);
          commandProcessor.applyFilter(tokens[1], tokens[2], filter, splitPercent, null);
        }
        System.out.println("Image " + filter + "ed and saved as " + tokens[2]);
      } catch (IOException e) {
        System.out.println("Failed to " + filter + " image: " + e.getMessage());
      } catch (NumberFormatException e) {
        System.out.println("Invalid split percentage. It should be an integer.");
      }
    } else {
      System.out.println("Usage: " + filter
          + " <source-image> <mask-image> <dest-image> [split <percentage>]");
    }
  }

  /**
   * Handles the color correction of an image.
   *
   * @param tokens the command tokens containing the image name, destination name, and optional
   *               split percentage.
   */
  private void handleColorCorrect(String[] tokens) {
    if (tokens.length == 3 || (tokens.length == 5 && tokens[3].equalsIgnoreCase("split"))) {
      try {
        if (tokens.length == 3) {
          commandProcessor.colorCorrectImage(tokens[1], tokens[2], null);
        } else {
          int splitPercent = Integer.parseInt(tokens[4]);
          commandProcessor.colorCorrectImage(tokens[1], tokens[2], splitPercent);
        }
        System.out.println("Color correction applied and saved as " + tokens[2]);
      } catch (IOException e) {
        System.out.println("Failed to apply color correction: " + e.getMessage());
      }
    } else {
      System.out.println(
          "Usage: color-correct <image-name> <dest-image-name> [split <percentage>]");
    }
  }

  /**
   * Handles the adjustment of levels in an image.
   *
   * @param tokens the command tokens containing the black, mid, white levels, image name,
   *               destination name, and optional split percentage.
   */
  private void handleLevelsAdjust(String[] tokens) {
    if (tokens.length == 6 || (tokens.length == 8 && tokens[6].equalsIgnoreCase("split"))) {
      try {
        int b = Integer.parseInt(tokens[1]);
        int m = Integer.parseInt(tokens[2]);
        int w = Integer.parseInt(tokens[3]);
        String imageName = tokens[4];
        String destName = tokens[5];

        if (tokens.length == 6) {
          commandProcessor.levelsAdjust(imageName, b, m, w, destName, null);
        } else {
          int splitPercent = Integer.parseInt(tokens[7]);
          commandProcessor.levelsAdjust(imageName, b, m, w, destName, splitPercent);
        }
        System.out.println("Levels adjusted and saved as " + destName);
      } catch (NumberFormatException e) {
        System.out.println("Invalid level values. They should be integers.");
      } catch (IOException e) {
        System.out.println("Failed to adjust levels: " + e.getMessage());
      }
    } else {
      System.out.println(
          "Usage: levels-adjust <b> <m> <w> <image-name> <dest-image-name> [split <percentage>]");
    }
  }

  /**
   * Handles the combination of RGB components into a single image.
   *
   * @param tokens the command tokens containing the image name and the names of the red, green, and
   *               blue images.
   */
  private void handleRgbCombine(String[] tokens) {
    if (tokens.length == 5) {
      try {
        String imageName = tokens[1];
        String redImageName = tokens[2];
        String greenImageName = tokens[3];
        String blueImageName = tokens[4];
        commandProcessor.rgbCombine(imageName, redImageName, greenImageName, blueImageName);
        System.out.println("RGB components combined successfully into '" + imageName + "'.");
      } catch (IOException e) {
        System.out.println("Failed to combine images: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    } else {
      System.out.println("Usage: rgb-combine <image-name> <red-image> <green-image> <blue-image>");
    }
  }

  /**
   * Handles the execution of a script file containing a series of commands.
   *
   * @param tokens the command tokens containing the script file path.
   */
  public void handleScript(String[] tokens) {
    if (tokens.length == 2) {
      try {
        ScriptProcessor scriptProcessor = new ScriptProcessor(this);
        scriptProcessor.executeScript(tokens[1]);
        System.out.println("Script executed successfully.");
      } catch (IOException e) {
        System.out.println("Failed to execute script: " + e.getMessage());
      }
    } else {
      System.out.println("Usage: -file <script-file-path>");
    }
  }

  /**
   * Handles the creation of a histogram from an image.
   *
   * @param tokens the command tokens containing the image name and destination name for the
   *               histogram.
   */
  private void handleHistogram(String[] tokens) {
    if (tokens.length == 3) {
      try {
        commandProcessor.getHistogram(tokens[1], tokens[2]);
        System.out.println("Histogram created from " + tokens[1] + " and saved as " + tokens[2]);
      } catch (IOException e) {
        System.out.println("Failed to create histogram: " + e.getMessage());
      }
    } else {
      System.out.println("Usage: histogram <image-name> <dest-image-name>");
    }
  }

  /**
   * Handles the compression of an image by a specified percentage.
   *
   * @param tokens the command tokens containing the compression percentage, image name, and
   *               destination name.
   */
  private void handleCompress(String[] tokens) {
    if (tokens.length == 4) {
      try {
        int percentage = Integer.parseInt(tokens[1]);
        if (percentage < 0 || percentage > 100) {
          System.out.println("Compression percentage must be between 0 and 100.");
          return;
        }
        String imageName = tokens[2];
        String destName = tokens[3];
        String inputFormat = getFormatFromFileName(imageName);
        String outputFormat = getFormatFromFileName(destName);

        if (inputFormat.equals("PPM") || outputFormat.equals("PPM")) {
          System.out.println(
              "Compression is not supported for PPM format. Please use JPG or PNG files.");
          return;
        }

        commandProcessor.compressImage(percentage, imageName, destName);
        System.out.println("Image compressed and saved as " + destName);
      } catch (NumberFormatException e) {
        System.out.println(
            "Invalid compression percentage. Please provide an integer between 0 and 100.");
      } catch (IOException e) {
        System.out.println("Failed to compress image: " + e.getMessage());
      } catch (UnsupportedOperationException e) {
        System.out.println(e.getMessage());
      }
    } else {
      System.out.println("Usage: compress <percentage> <image-name> <dest-image-name>");
    }
  }

  /**
   * Handles the downscaling of an image to the specified width and height.
   *
   * @param tokens the command tokens containing the image name, destination name, width, and
   *               height.
   */
  private void handleDownscale(String[] tokens) {
    if (tokens.length == 5) {
      try {
        String imageName = tokens[1];
        String destName = tokens[2];
        int newWidth = Integer.parseInt(tokens[3]);
        int newHeight = Integer.parseInt(tokens[4]);
        commandProcessor.downscaleImage(imageName, destName, newWidth, newHeight);
        System.out.println("Image downscaled and saved as " + destName);
      } catch (NumberFormatException e) {
        System.out.println("Invalid width or height value. They should be integers.");
      } catch (IOException e) {
        System.out.println("Failed to downscale image: " + e.getMessage());
      }
    } else {
      System.out.println(
          "Usage: downscale <image-name> <dest-image-name> <new-width> <new-height>");
    }
  }


  /**
   * Retrieves the image format based on the file extension.
   *
   * @param filePath the file path of the image.
   * @return the format of the image as a string.
   */
  private String getFormatFromFileName(String filePath) {
    for (Map.Entry<String, String> entry : EXTENSION_TO_FORMAT.entrySet()) {
      if (filePath.endsWith(entry.getKey())) {
        return entry.getValue();
      }
    }
    return "PNG";
  }
}