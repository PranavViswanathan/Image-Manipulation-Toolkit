package com.vanarp.controller;

import com.vanarp.model.ImageRepresentation;
import com.vanarp.model.PixelOperation;

import java.io.IOException;

/**
 * An interface defining operations for processing images.
 */
public interface ImageCommandProcessor {

  /**
   * Loads an image from the specified file path and stores it in the cache with the given name.
   *
   * @param filePath  the path of the file to load
   * @param imageName the name to assign to the loaded image in the cache
   * @throws IOException if an error occurs during the image loading operation
   */
  void loadImage(String filePath, String imageName) throws IOException;

  /**
   * Saves the specified image to a file in the given format.
   *
   * @param imageName the name of the image in the cache to save
   * @param filePath  the path where the image will be saved
   * @param format    the format to save the image in (e.g., "png", "jpg")
   * @throws IOException if an error occurs during the image saving operation
   */
  void saveImage(String imageName, String filePath, String format) throws IOException;

  /**
   * Retrieves an image from the cache by its name.
   *
   * @param imageName the name of the image to retrieve
   * @return the requested ImageRepresentation
   * @throws IllegalArgumentException if the image is not found in the cache
   */
  ImageRepresentation getImage(String imageName);

  /**
   * Extracts a specific color component from the image and stores it in the cache.
   *
   * @param imageName     the name of the source image
   * @param destName      the name to assign to the extracted component image in the cache
   * @param componentType the type of component to extract (e.g., "red", "green", "blue")
   * @throws IOException if an error occurs during the extraction operation
   */
  void extractComponent(String imageName, String destName, String componentType) throws IOException;

  /**
   * Applies a filter to the specified image and stores the result in the cache.
   *
   * @param imageName    the name of the source image
   * @param destName     the name to assign to the filtered image in the cache
   * @param filterType   the type of filter to apply (e.g., "blur", "sharpen")
   * @param splitPercent optional parameter for split view (0-100)
   * @throws IOException if an error occurs during the filter application
   */
  void applyFilter(String imageName, String destName, String filterType, Integer splitPercent)
      throws IOException;

  /**
   * Flips the specified image in the given direction and stores the result in the cache.
   *
   * @param imageName the name of the source image
   * @param destName  the name to assign to the flipped image in the cache
   * @param direction the direction to flip the image (e.g., "horizontal", "vertical")
   * @throws IOException if an error occurs during the flip operation
   */
  void flipImage(String imageName, String destName, String direction) throws IOException;

  /**
   * Brightens the specified image by a given increment and stores the result in the cache.
   *
   * @param imageName the name of the source image
   * @param increment the amount to brighten the image
   * @param destName  the name to assign to the brightened image in the cache
   * @throws IOException if an error occurs during the brightening operation
   */
  void brightenImage(String imageName, int increment, String destName) throws IOException;

  /**
   * Splits the RGB components of the specified image and stores them in the cache.
   *
   * @param imageName the name of the source image
   * @param redName   the name to assign to the red component image
   * @param greenName the name to assign to the green component image
   * @param blueName  the name to assign to the blue component image
   * @throws IOException if an error occurs during the RGB split operation
   */
  void rgbSplit(String imageName, String redName, String greenName, String blueName)
      throws IOException;

  /**
   * Combines the RGB components into a single image and stores the result in the cache.
   *
   * @param destName       the name to assign to the combined image in the cache
   * @param redImageName   the name of the red component image
   * @param greenImageName the name of the green component image
   * @param blueImageName  the name of the blue component image
   * @throws IOException if an error occurs during the RGB combination operation
   */
  void rgbCombine(String destName, String redImageName, String greenImageName, String blueImageName)
      throws IOException;

  /**
   * Adjusts the levels of the specified image by modifying its brightness, midtone, and white point
   * values, and stores the result in the cache.
   *
   * @param imageName    the name of the image in the cache to adjust
   * @param brightness   the brightness adjustment value
   * @param midtone      the midtone adjustment value
   * @param whitePoint   the white point adjustment value
   * @param destName     the name to assign to the adjusted image in the cache
   * @param splitPercent optional parameter for split view (0-100)
   * @throws IOException if an error occurs during the levels adjustment operation
   */
  void levelsAdjust(String imageName, int brightness, int midtone, int whitePoint, String destName,
      Integer splitPercent) throws IOException;

  /**
   * Applies color correction to the specified image and stores the result in the cache.
   *
   * @param imageName    the name of the image in the cache to color correct
   * @param destName     the name to assign to the color-corrected image in the cache
   * @param splitPercent optional parameter for split view (0-100)
   * @throws IOException if an error occurs during the operation
   */
  void colorCorrectImage(String imageName, String destName, Integer splitPercent)
      throws IOException;

  /**
   * Processes a split operation for various image transformations.
   *
   * @param operation    the type of operation to perform ("blur", "sharpen", "sepia", etc.)
   * @param imageName    the name of the source image
   * @param destName     the name for the destination image
   * @param splitPercent the percentage point for the split (0-100)
   * @param params       additional parameters for the operation (e.g., levels adjustment values)
   * @throws IOException if an error occurs during the operation
   */
  void processSplitOperation(String operation, String imageName, String destName, int splitPercent,
      Integer... params) throws IOException;

  /**
   * Generates a histogram image from the specified image and stores it in the cache.
   *
   * @param imageName the name of the source image
   * @param destName  the name to assign to the histogram image in the cache
   * @throws IOException if an error occurs during the histogram generation
   */
  void getHistogram(String imageName, String destName) throws IOException;

  /**
   * Compresses the specified image by a given percentage and stores the result in the cache.
   *
   * @param percentage the compression percentage (0-100)
   * @param imageName  the name of the source image
   * @param destName   the name to assign to the compressed image in the cache
   * @throws IOException if an error occurs during the compression operation
   */
  void compressImage(float percentage, String imageName, String destName) throws IOException;

  /**
   * Downscales the specified image to the given width and height and stores the result in the
   * cache.
   *
   * @param imageName the name of the source image
   * @param destName  the name to assign to the downscaled image in the cache
   * @param newWidth  the desired new width of the image
   * @param newHeight the desired new height of the image
   * @throws IOException if an error occurs during the downscaling operation
   */
  void downscaleImage(String imageName, String destName, int newWidth, int newHeight)
      throws IOException;

  void applyMaskOperation(String sourceImageName, String maskImageName, String destImageName, PixelOperation operation) throws IOException;
}