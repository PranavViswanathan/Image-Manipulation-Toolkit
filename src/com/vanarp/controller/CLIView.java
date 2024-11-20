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

  public CLIView(ImageCommandProcessor commandProcessor) {
    this.commandProcessor = commandProcessor;
    this.commandMap = new HashMap<>();
    initializeCommands();
  }

  public void processCommand(String input) {
    String[] tokens = input.split("\\s+");
    if (tokens.length == 0) {
      handleException("processCommand", new IllegalArgumentException("Invalid command."));
      return;
    }

    String command = tokens[0].toLowerCase();
    Consumer<String[]> commandAction = commandMap.get(command);

    if (commandAction != null) {
      commandAction.accept(tokens);
    } else {
      handleException("processCommand",
          new IllegalArgumentException("Unknown command: " + command));
    }
  }

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

  private void handleLoad(String[] tokens) {
    if (tokens.length == 3) {
      try {
        commandProcessor.loadImage(tokens[1], tokens[2]);
        System.out.println("Image loaded successfully.");
      } catch (IOException e) {
        handleException("handleLoad", e);
      }
    } else {
      handleException("handleLoad",
          new IllegalArgumentException("Usage: load <image-path> <image-name>"));
    }
  }

  private void handleSave(String[] tokens) {
    if (tokens.length == 3) {
      String format = getFormatFromFileName(tokens[2]);
      try {
        commandProcessor.saveImage(tokens[2], tokens[1], format);
        System.out.println("Image saved successfully.");
      } catch (IOException e) {
        handleException("handleSave", e);
      }
    } else {
      handleException("handleSave",
          new IllegalArgumentException("Usage: save <image-name> <image-path>"));
    }
  }

  private void handleComponent(String[] tokens, String component) {
    if (tokens.length >= 3 && tokens.length <= 4) {
      String imageName = tokens[1];
      String destImageName = tokens[2];
      String maskImageName = tokens.length == 4 ? tokens[3] : null;

      try {
        if (maskImageName != null) {
          commandProcessor.extractComponent(imageName, destImageName, component, maskImageName);
          System.out.println(component.substring(0, 1).toUpperCase() + component.substring(1)
              + " component applied to the image with mask.");
        } else {
          commandProcessor.extractComponent(imageName, destImageName, component, null);
          System.out.println(component.substring(0, 1).toUpperCase() + component.substring(1)
              + " component applied to the image.");
        }
      } catch (IOException e) {
        handleException("handleComponent", e);
      }
    } else {
      handleException("handleComponent", new IllegalArgumentException(
          "Usage: " + component + "-component <image-name> <dest-image-name> [<mask-image-name>]"));
    }
  }

  private void handleFlip(String[] tokens, String direction) {
    if (tokens.length == 3) {
      try {
        commandProcessor.flipImage(tokens[1], tokens[2], direction);
        System.out.println("Image flipped " + direction + " and saved as " + tokens[2]);
      } catch (IOException e) {
        handleException("handleFlip", e);
      }
    } else {
      handleException("handleFlip", new IllegalArgumentException(
          "Usage: " + direction + "-flip <image-name> <dest-image-name>"));
    }
  }

  private void handleBrighten(String[] tokens) {
    if (tokens.length == 4) {
      try {
        int increment = Integer.parseInt(tokens[1]);
        commandProcessor.brightenImage(tokens[2], increment, tokens[3]);
        System.out.println("Image brightened by " + increment + " and saved as " + tokens[3]);
      } catch (NumberFormatException e) {
        handleException("handleBrighten",
            new IllegalArgumentException("Invalid increment value. It should be an integer."));
      } catch (IOException e) {
        handleException("handleBrighten", e);
      }
    } else {
      handleException("handleBrighten", new IllegalArgumentException(
          "Usage: brighten <increment> <image-name> <dest-image-name>"));
    }
  }

  private void handleRgbSplit(String[] tokens) {
    if (tokens.length == 5) {
      try {
        commandProcessor.rgbSplit(tokens[1], tokens[2], tokens[3], tokens[4]);
        System.out.println(
            "RGB split completed. Exists in cache as " + tokens[2] + ", " + tokens[3] + ", "
                + tokens[4]);
      } catch (IOException e) {
        handleException("handleRgbSplit", e);
      }
    } else {
      handleException("handleRgbSplit", new IllegalArgumentException(
          "Usage: rgb-split <image-name> <dest-red-image> <dest-green-image> <dest-blue-image>"));
    }
  }

  private void handleFilter(String[] tokens, String filter) {
    if (tokens.length == 3 || (tokens.length == 4) || (tokens.length == 5
        && tokens[3].equalsIgnoreCase("split"))) {
      try {
        if (tokens.length == 3) {
          commandProcessor.applyFilter(tokens[1], tokens[2], filter, null, null);
        } else if (tokens.length == 4) {
          String maskImageName = tokens[2];
          String destImageName = tokens[3];
          commandProcessor.applyFilter(tokens[1], destImageName, filter, null, maskImageName);
        } else if (tokens[3].equalsIgnoreCase("split")) {
          int splitPercent = Integer.parseInt(tokens[4]);
          commandProcessor.applyFilter(tokens[1], tokens[2], filter, splitPercent, null);
        }
        System.out.println("Image " + filter + "ed and saved as " + tokens[2]);
      } catch (IOException | IllegalArgumentException e) {
        handleException("handleFilter", e);
      }
    } else {
      handleException("handleFilter", new IllegalArgumentException(
          "Usage: " + filter + " <source-image> <mask-image> <dest-image> [split <percentage>]"));
    }
  }

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
      } catch (IOException | IllegalArgumentException e) {
        handleException("handleColorCorrect", e);
      }
    } else {
      handleException("handleColorCorrect", new IllegalArgumentException(
          "Usage: color-correct <image-name> <dest-image-name> [split <percentage>]"));
    }
  }

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
      } catch (IOException | IllegalArgumentException e) {
        handleException("handleLevelsAdjust", e);
      }
    } else {
      handleException("handleLevelsAdjust", new IllegalArgumentException(
          "Usage: levels-adjust <b> <m> <w> <image-name> <dest-image-name> [split <percentage>]"));
    }
  }

  private void handleRgbCombine(String[] tokens) {
    if (tokens.length == 5) {
      try {
        String imageName = tokens[1];
        String redImageName = tokens[2];
        String greenImageName = tokens[3];
        String blueImageName = tokens[4];
        commandProcessor.rgbCombine(imageName, redImageName, greenImageName, blueImageName);
        System.out.println("RGB components combined successfully into '" + imageName + "'.");
      } catch (IOException | IllegalArgumentException e) {
        handleException("handleRgbCombine", e);
      }
    } else {
      handleException("handleRgbCombine", new IllegalArgumentException(
          "Usage: rgb-combine <image-name> <red-image> <green-image> <blue-image>"));
    }
  }

  public void handleScript(String[] tokens) {
    if (tokens.length == 2) {
      try {
        ScriptProcessor scriptProcessor = new ScriptProcessor(this);
        scriptProcessor.executeScript(tokens[1]);
        System.out.println("Script executed successfully.");
      } catch (IOException e) {
        handleException("handleScript", e);
      }
    } else {
      handleException("handleScript",
          new IllegalArgumentException("Usage: -file <script-file-path>"));
    }
  }

  private void handleHistogram(String[] tokens) {
    if (tokens.length == 3) {
      try {
        commandProcessor.getHistogram(tokens[1], tokens[2]);
        System.out.println("Histogram created from " + tokens[1] + " and saved as " + tokens[2]);
      } catch (IOException e) {
        handleException("handleHistogram", e);
      }
    } else {
      handleException("handleHistogram",
          new IllegalArgumentException("Usage: histogram <image-name> <dest-image-name>"));
    }
  }

  private void handleCompress(String[] tokens) {
    if (tokens.length == 4) {
      try {
        int percentage = Integer.parseInt(tokens[1]);
        if (percentage < 0 || percentage > 100) {
          handleException("handleCompress",
              new IllegalArgumentException("Compression percentage must be between 0 and 100."));
          return;
        }
        String imageName = tokens[2];
        String destName = tokens[3];
        String inputFormat = getFormatFromFileName(imageName);
        String outputFormat = getFormatFromFileName(destName);

        if (inputFormat.equals("PPM") || outputFormat.equals("PPM")) {
          handleException("handleCompress", new UnsupportedOperationException(
              "Compression is not supported for PPM format. Please use JPG or PNG files."));
          return;
        }

        commandProcessor.compressImage(percentage, imageName, destName);
        System.out.println("Image compressed and saved as " + destName);
      } catch (IOException | IllegalArgumentException | UnsupportedOperationException e) {
        handleException("handleCompress", e);
      }
    } else {
      handleException("handleCompress", new IllegalArgumentException(
          "Usage: compress <percentage> <image-name> <dest-image-name>"));
    }
  }

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
        handleException("handleDownscale", new IllegalArgumentException(
            "Invalid width or height value. They should be integers."));
      } catch (IOException | IllegalArgumentException e) {
        handleException("handleDownscale", e);
      }
    } else {
      handleException("handleDownscale", new IllegalArgumentException(
          "Usage: downscale <image-name> <dest-image-name> <new-width> <new-height>"));
    }
  }

  private String getFormatFromFileName(String filePath) {
    for (Map.Entry<String, String> entry : EXTENSION_TO_FORMAT.entrySet()) {
      if (filePath.endsWith(entry.getKey())) {
        return entry.getValue();
      }
    }
    return "PNG";
  }

  private void handleException(String context, Exception e) {
    System.out.println("Error in " + context + ": " + e.getMessage());
  }
}