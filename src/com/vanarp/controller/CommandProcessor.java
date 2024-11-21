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
  private final CompressedImageIO compressedImageIO;
  private final UncompressedImageIO uncompressedImageIO;

  /**
   * Constructs a CommandProcessor with the specified ImageOperations.
   *
   * @param operations the ImageOperations to be used for processing image commands.
   */
  public CommandProcessor(ImageOperations operations) {
    this.operations = operations;
    this.imageCache = new HashMap<>();
    this.compressedImageIO = new CompressedImageIO();
    this.uncompressedImageIO = new UncompressedImageIO();
  }

  @Override
  public void loadImage(String filePath, String imageName) throws IOException {
    if (filePath.endsWith(".jpg") || filePath.endsWith(".png")) {
      ImageRepresentation img = compressedImageIO.loadImage(filePath);
      imageCache.put(imageName, img);
    } else if (filePath.endsWith(".ppm")) {
      ImageRepresentation img = uncompressedImageIO.loadImage(filePath);
      imageCache.put(imageName, img);
    } else {
      throw new IOException("Unsupported file format");
    }
  }

  @Override
  public void saveImage(String imageName, String filePath, String format) throws IOException {
    ImageRepresentation image = getImage(imageName);
    format = format.toLowerCase();
    if (format.equals("jpg") || format.equals("png")) {
      compressedImageIO.saveImage(image, filePath, format);
    } else if (format.equals("ppm")) {
      uncompressedImageIO.saveImage(image, filePath, format);
    } else {
      System.out.println("Format provided: " + format);
      throw new IOException("Unsupported format");
    }
  }

  /**
   * Retrieves an image from the cache by its name.
   *
   * @param imageName the name of the image to retrieve
   * @return the ImageRepresentation associated with the specified name
   * @throws IllegalArgumentException if the image is not found in the cache
   */
  public ImageRepresentation getImage(String imageName) {
    if (!imageCache.containsKey(imageName)) {
      throw new IllegalArgumentException("Image with name " + imageName + " not found.");
    }
    return imageCache.get(imageName);
  }

  /**
   * Puts an image into the cache with the specified name.
   *
   * @param imageName the name to associate with the image
   * @param image     the ImageRepresentation to store in the cache
   */
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
      result = switch (componentType.toLowerCase()) {
        case "red" -> operations.redComponentWithMask(image, maskImage);
        case "green" -> operations.greenComponentWithMask(image, maskImage);
        case "blue" -> operations.blueComponentWithMask(image, maskImage);
        case "value" -> operations.valueComponentWithMask(image, maskImage);
        case "luma" -> operations.lumaComponentWithMask(image, maskImage);
        case "intensity" -> operations.intensityComponentWithMask(image, maskImage);
        default -> throw new IllegalArgumentException("Unknown component type: " + componentType);
      };
    } else {
      result = switch (componentType.toLowerCase()) {
        case "red" -> operations.redComponent(image);
        case "green" -> operations.greenComponent(image);
        case "blue" -> operations.blueComponent(image);
        case "value" -> operations.valueComponent(image);
        case "luma" -> operations.lumaComponent(image);
        case "intensity" -> operations.intensityComponent(image);
        default -> throw new IllegalArgumentException("Unknown component type: " + componentType);
      };
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
      result = switch (filterType.toLowerCase()) {
        case "blur" -> operations.applyBlurWithMask(image, maskImage);
        case "sharpen" -> operations.applySharpenWithMask(image, maskImage);
        case "sepia" -> operations.applySepiaWithMask(image, maskImage);
        case "greyscale" -> operations.applyGreyscaleWithMask(image, maskImage);
        default -> throw new IllegalArgumentException("Unknown filter type: " + filterType);
      };
    } else {
      result = switch (filterType.toLowerCase()) {
        case "blur" -> operations.blur(image);
        case "sharpen" -> operations.sharpen(image);
        case "sepia" -> operations.applySepia(image);
        case "greyscale" -> operations.applyGreyScale(image);
        default -> throw new IllegalArgumentException("Unknown filter type: " + filterType);
      };
    }

    putImage(destName, result);
  }

  /**
   * Flips an image in the specified direction and stores the result in the cache.
   *
   * @param imageName the name of the image to flip
   * @param destName  the name to store the flipped image
   * @param direction the direction to flip the image (horizontal or vertical)
   * @throws IOException if an error occurs during flipping
   */
  @Override
  public void flipImage(String imageName, String destName, String direction) throws IOException {
    ImageRepresentation image = getImage(imageName);
    ImageRepresentation result = switch (direction.toLowerCase()) {
      case "horizontal" -> operations.flipHorizontally(image);
      case "vertical" -> operations.flipVertically(image);
      default -> throw new IllegalArgumentException("Unknown flip direction: " + direction);
    };
    putImage(destName, result);
  }

  /**
   * Brightens an image by a specified increment and stores the result in the cache.
   *
   * @param imageName the name of the image to brighten
   * @param increment the amount to brighten the image
   * @param destName  the name to store the brightened image
   * @throws IOException if an error occurs during brightening
   */
  @Override
  public void brightenImage(String imageName, int increment, String destName) throws IOException {
    ImageRepresentation image = getImage(imageName);
    ImageRepresentation brightenedImage = operations.brightenImage(image, increment);
    putImage(destName, brightenedImage);
  }

  /**
   * Splits an image into its RGB components and stores them in the cache.
   *
   * @param imageName the name of the image to split
   * @param redName   the name to store the red component
   * @param greenName the name to store the green component
   * @param blueName  the name to store the blue component
   * @throws IOException if an error occurs during splitting
   */
  @Override
  public void rgbSplit(String imageName, String redName, String greenName, String blueName)
          throws IOException {
    ImageRepresentation image = getImage(imageName);
    ImageRepresentation[] rgbComponents = operations.rgbSplit(image);
    putImage(redName, rgbComponents[0]);
    putImage(greenName, rgbComponents[1]);
    putImage(blueName, rgbComponents[2]);
  }

  /**
   * Combines RGB components into a single image and stores it in the cache.
   *
   * @param destName       the name to store the combined image
   * @param redImageName   the name of the red component image
   * @param greenImageName the name of the green component image
   * @param blueImageName  the name of the blue component image
   * @throws IOException if an error occurs during combining
   */
  @Override
  public void rgbCombine(String destName, String redImageName, String greenImageName,
                         String blueImageName) throws IOException {
    ImageRepresentation redImage = getImage(redImageName);
    ImageRepresentation greenImage = getImage(greenImageName);
    ImageRepresentation blueImage = getImage(blueImageName);
    ImageRepresentation combinedImage = operations.combineRgb(redImage, greenImage, blueImage);
    putImage(destName, combinedImage);
  }

  /**
   * Adjusts the levels of an image and stores the result in the cache.
   *
   * @param imageName    the name of the image to adjust
   * @param brightness   the brightness level to adjust
   * @param midtone      the midtone level to adjust
   * @param whitePoint   the white point level to adjust
   * @param destName     the name to store the adjusted image
   * @param splitPercent optional percentage for split operation
   * @throws IOException if an error occurs during level adjustment
   */
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

  /**
   * Applies color correction to an image and stores the result in the cache.
   *
   * @param imageName    the name of the image to correct
   * @param destName     the name to store the color-corrected image
   * @param splitPercent optional percentage for split operation
   * @throws IOException if an error occurs during color correction
   */
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

  /**
   * Processes split operations for various image transformations.
   *
   * @param operation    the type of operation to perform
   * @param imageName    the name of the image to process
   * @param destName     the name to store the processed image
   * @param splitPercent the percentage for splitting
   * @param params       optional parameters for specific operations
   * @throws IOException if an error occurs during processing
   */
  public void processSplitOperation(String operation, String imageName, String destName,
                                    int splitPercent, Integer... params) throws IOException {
    ImageRepresentation originalImage = getImage(imageName);
    ImageRepresentation transformedImage = switch (operation.toLowerCase()) {
      case "blur" -> operations.blur(originalImage);
      case "sharpen" -> operations.sharpen(originalImage);
      case "sepia" -> operations.applySepia(originalImage);
      case "greyscale" -> operations.applyGreyScale(originalImage);
      case "color-correct" -> operations.colorCorrect(originalImage);
      case "levels-adjust" -> {
        if (params.length != 3) {
          throw new IllegalArgumentException(
                  "Levels adjustment requires brightness, midtone, and white point values");
        }
        yield operations.levelsAdjust(originalImage, params[0], params[1],
                params[2]);
      }
      default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
    };

    ImageRepresentation splitResult = operations.splitImages(transformedImage, originalImage,
            splitPercent);
    putImage(destName, splitResult);
  }

  /**
   * Generates a histogram from an image and stores it in the cache.
   *
   * @param imageName the name of the image to generate a histogram from
   * @param destName  the name to store the histogram image
   * @throws IOException if an error occurs during histogram generation
   */
  @Override
  public void getHistogram(String imageName, String destName) throws IOException {
    ImageRepresentation image = getImage(imageName);
    ImageRepresentation histogramImage = operations.getHistogram(image);
    putImage(destName, histogramImage);
  }

  /**
   * Compresses an image by a specified percentage and stores the result in the cache.
   *
   * @param percentage the percentage to compress the image
   * @param imageName  the name of the image to compress
   * @param destName   the name to store the compressed image
   * @throws IOException if an error occurs during compression
   */
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