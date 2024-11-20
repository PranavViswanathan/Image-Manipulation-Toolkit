package com.vanarp.controller;

import com.vanarp.model.ImageOperations;
import com.vanarp.model.ImageRepresentation;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * CommandProcessor is responsible for processing image commands, performing various image
 * operations, and managing an in-memory cache of images.
 */
public class CommandProcessor implements ImageCommandProcessor {

  final Map<String, ImageRepresentation> imageCache;
  private final ImageOperations operations;

  /**
   * Constructs a CommandProcessor with the specified ImageOperations.
   *
   * @param operations the ImageOperations to be used for processing image commands.
   */
  public CommandProcessor(ImageOperations operations) {
    this.operations = operations;
    this.imageCache = new HashMap<>();
  }

  @Override
  public void loadImage(String filePath, String imageName) throws IOException {
    ImageRepresentation image = operations.loadImage(filePath);
    imageCache.put(imageName, image);
  }

  @Override
  public void saveImage(String imageName, String filePath, String format) throws IOException {
    ImageRepresentation image = getImage(imageName);
    operations.saveImage(image, filePath, format);
  }

  public ImageRepresentation getImage(String imageName) {
    if (!imageCache.containsKey(imageName)) {
      throw new IllegalArgumentException("Image with name " + imageName + " not found.");
    }
    return imageCache.get(imageName);
  }

  void putImage(String imageName, ImageRepresentation image) {
    imageCache.put(imageName, image);
  }

  @Override
  public void extractComponent(String imageName, String destName, String componentType,
      String maskName) throws IOException {
    ImageRepresentation image = getImage(imageName);
    ImageRepresentation result;

    if (maskName != null && !maskName.isEmpty()) {
      ImageRepresentation maskImage = getImage(maskName);
      switch (componentType.toLowerCase()) {
        case "red":
          result = operations.redComponentWithMask(image, maskImage);
          break;
        case "green":
          result = operations.greenComponentWithMask(image, maskImage);
          break;
        case "blue":
          result = operations.blueComponentWithMask(image, maskImage);
          break;
        case "value":
          result = operations.valueComponent(image);
          break;
        case "luma":
          result = operations.lumaComponent(image);
          break;
        case "intensity":
          result = operations.intensityComponent(
              image);
          break;
        default:
          throw new IllegalArgumentException("Unknown component type: " + componentType);
      }
    } else {
      switch (componentType.toLowerCase()) {
        case "red":
          result = operations.redComponent(image);
          break;
        case "green":
          result = operations.greenComponent(image);
          break;
        case "blue":
          result = operations.blueComponent(image);
          break;
        case "value":
          result = operations.valueComponent(image);
          break;
        case "luma":
          result = operations.lumaComponent(image);
          break;
        case "intensity":
          result = operations.intensityComponent(image);
          break;
        default:
          throw new IllegalArgumentException("Unknown component type: " + componentType);
      }
    }

    putImage(destName, result);
  }

  @Override
  public void applyFilter(String imageName, String destName, String filterType,
      Integer splitPercent, String maskImageName) throws IOException {

    if (splitPercent != null) {
      processSplitOperation(filterType, imageName, destName, splitPercent);
      return;
    }

    ImageRepresentation image = getImage(imageName);
    ImageRepresentation result;

    if (maskImageName != null && !maskImageName.isEmpty()) {
      ImageRepresentation maskImage = getImage(maskImageName);
      switch (filterType.toLowerCase()) {
        case "blur":
          result = operations.applyBlurWithMask(image, maskImage);
          break;
        case "sharpen":
          result = operations.applySharpenWithMask(image, maskImage);
          break;
        case "sepia":
          result = operations.applySepiaWithMask(image, maskImage);
          break;
        case "greyscale":
          result = operations.applyGreyscaleWithMask(image, maskImage);
          break;
        default:
          throw new IllegalArgumentException("Unknown filter type: " + filterType);
      }
    } else {
      switch (filterType.toLowerCase()) {
        case "blur":
          result = operations.blur(image);
          break;
        case "sharpen":
          result = operations.sharpen(image);
          break;
        case "sepia":
          result = operations.applySepia(image);
          break;
        case "greyscale":
          result = operations.applyGreyScale(image);
          break;
        default:
          throw new IllegalArgumentException("Unknown filter type: " + filterType);
      }
    }

    putImage(destName, result);
  }

  @Override
  public void flipImage(String imageName, String destName, String direction) throws IOException {
    ImageRepresentation image = getImage(imageName);
    ImageRepresentation result;
    switch (direction.toLowerCase()) {
      case "horizontal":
        result = operations.flipHorizontally(image);
        break;
      case "vertical":
        result = operations.flipVertically(image);
        break;
      default:
        throw new IllegalArgumentException("Unknown flip direction: " + direction);
    }
    putImage(destName, result);
  }

  @Override
  public void brightenImage(String imageName, int increment, String destName) throws IOException {
    ImageRepresentation image = getImage(imageName);
    ImageRepresentation brightenedImage = operations.brightenImage(image, increment);
    putImage(destName, brightenedImage);
  }

  @Override
  public void rgbSplit(String imageName, String redName, String greenName, String blueName)
      throws IOException {
    ImageRepresentation image = getImage(imageName);
    ImageRepresentation[] rgbComponents = operations.rgbSplit(image);
    putImage(redName, rgbComponents[0]);
    putImage(greenName, rgbComponents[1]);
    putImage(blueName, rgbComponents[2]);
  }

  @Override
  public void rgbCombine(String destName, String redImageName, String greenImageName,
      String blueImageName) throws IOException {
    ImageRepresentation redImage = getImage(redImageName);
    ImageRepresentation greenImage = getImage(greenImageName);
    ImageRepresentation blueImage = getImage(blueImageName);
    ImageRepresentation combinedImage = operations.combineRgb(redImage, greenImage, blueImage);
    putImage(destName, combinedImage);
  }

  @Override
  public void levelsAdjust(String imageName, int brightness, int midtone, int whitePoint,
      String destName, Integer splitPercent) throws IOException {
    if (splitPercent != null) {
      processSplitOperation("levels-adjust", imageName, destName, splitPercent, brightness, midtone,
          whitePoint);
    } else {
      ImageRepresentation image = getImage(imageName);
      ImageRepresentation adjustedImage = operations.levelsAdjust(image, brightness,
          midtone, whitePoint);
      putImage(destName, adjustedImage);
    }
  }

  @Override
  public void colorCorrectImage(String imageName, String destName, Integer splitPercent)
      throws IOException {
    if (splitPercent != null) {
      processSplitOperation("color-correct", imageName, destName, splitPercent);
    } else {
      ImageRepresentation image = getImage(imageName);
      ImageRepresentation colorCorrectedImage = operations.colorCorrect(image);
      putImage(destName, colorCorrectedImage);
    }
  }

  public void processSplitOperation(String operation, String imageName, String destName,
      int splitPercent, Integer... params) throws IOException {
    ImageRepresentation originalImage = getImage(imageName);
    ImageRepresentation transformedImage;

    switch (operation.toLowerCase()) {
      case "blur":
        transformedImage = operations.blur(originalImage);
        break;
      case "sharpen":
        transformedImage = operations.sharpen(originalImage);
        break;
      case "sepia":
        transformedImage = operations.applySepia(originalImage);
        break;
      case "greyscale":
        transformedImage = operations.applyGreyScale(originalImage);
        break;
      case "color-correct":
        transformedImage = operations.colorCorrect(originalImage);
        break;
      case "levels-adjust":
        if (params.length != 3) {
          throw new IllegalArgumentException(
              "Levels adjustment requires brightness, midtone, and white point values");
        }
        transformedImage = operations.levelsAdjust(originalImage, params[0], params[1],
            params[2]);
        break;
      default:
        throw new IllegalArgumentException("Unsupported operation: " + operation);
    }

    ImageRepresentation splitResult = operations.splitImages(transformedImage, originalImage,
        splitPercent);
    putImage(destName, splitResult);
  }

  @Override
  public void getHistogram(String imageName, String destName) throws IOException {
    ImageRepresentation image = getImage(imageName);
    ImageRepresentation histogramImage = operations.getHistogram(image);
    putImage(destName, histogramImage);
  }

  @Override
  public void compressImage(float percentage, String imageName, String destName)
      throws IOException {
    ImageRepresentation image = getImage(imageName);
    ImageRepresentation compressedImage = operations.compressImage(image, percentage);
    putImage(destName, compressedImage);
  }

  /**
   * Downscales the specified image to the given width and height and stores it in the cache.
   *
   * @param imageName the name of the image to downscale
   * @param destName  the name to store the downscaled image
   * @param newWidth  the desired width of the downscaled image
   * @param newHeight the desired height of the downscaled image
   * @throws IOException if an error occurs during downscaling
   */
  public void downscaleImage(String imageName, String destName, int newWidth, int newHeight)
      throws IOException {
    ImageRepresentation image = getImage(imageName);
    ImageRepresentation downscaledImage = operations.downscaleImage(image, newWidth, newHeight);
    putImage(destName, downscaledImage);
  }
}
