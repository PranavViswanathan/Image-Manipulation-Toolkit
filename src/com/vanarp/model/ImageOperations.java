package com.vanarp.model;

import java.io.IOException;

/**
 * The {@code ImageOperations} interface defines the operations for manipulating images.
 * <p>
 * This interface provides methods for loading, saving, and manipulating image data, including
 * operations to extract color components, apply filters, and perform geometric transformations.
 * Implementations of this interface will provide the actual logic for these image operations.
 * </p>
 *
 * <p>
 * Author: Pranav Viswanathan, Saran Jagadeesan Uma
 * </p>
 */
public interface ImageOperations {

  /**
   * Loads an image from the specified file path.
   *
   * @param filePath the path to the image file
   * @return the loaded {@link Image}
   * @throws IOException if an error occurs while loading the image
   */
  ImageRepresentation loadImage(String filePath) throws IOException;

  /**
   * Saves the given image to the specified file path in the specified format.
   *
   * @param image    the {@link Image} to save
   * @param filePath the path where the image will be saved
   * @param format   the format in which to save the image (e.g., jpg, png)
   * @throws IOException if an error occurs while saving the image
   */
  void saveImage(ImageRepresentation image, String filePath, String format) throws IOException;

  /**
   * Extracts the red component from the specified image.
   *
   * @param image the {@link Image} from which to extract the red component
   * @return a new {@link Image} containing only the red component
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation redComponent(ImageRepresentation image) throws IOException;

  /**
   * Extracts the green component from the specified image.
   *
   * @param image the {@link Image} from which to extract the green component
   * @return a new {@link Image} containing only the green component
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation greenComponent(ImageRepresentation image) throws IOException;

  /**
   * Extracts the blue component from the specified image.
   *
   * @param image the {@link Image} from which to extract the blue component
   * @return a new {@link Image} containing only the blue component
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation blueComponent(ImageRepresentation image) throws IOException;

  /**
   * Extracts the value component from the specified image.
   *
   * @param image the {@link Image} from which to extract the value component
   * @return a new {@link Image} containing only the value component
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation valueComponent(ImageRepresentation image) throws IOException;

  /**
   * Extracts the luma component from the specified image.
   *
   * @param image the {@link Image} from which to extract the luma component
   * @return a new {@link Image} containing only the luma component
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation lumaComponent(ImageRepresentation image) throws IOException;

  /**
   * Extracts the intensity component from the specified image.
   *
   * @param image the {@link Image} from which to extract the intensity component
   * @return a new {@link Image} containing only the intensity component
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation intensityComponent(ImageRepresentation image) throws IOException;

  /**
   * Flips the specified image horizontally.
   *
   * @param image the {@link Image} to flip
   * @return a new {@link Image} that is flipped horizontally
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation flipHorizontally(ImageRepresentation image) throws IOException;

  /**
   * Flips the specified image vertically.
   *
   * @param image the {@link Image} to flip
   * @return a new {@link Image} that is flipped vertically
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation flipVertically(ImageRepresentation image) throws IOException;

  /**
   * Brightens the specified image by a given increment.
   *
   * @param image     the {@link Image} to brighten
   * @param increment the amount to brighten the image
   * @return a new {@link Image} that is brightened
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation brightenImage(ImageRepresentation image, int increment) throws IOException;

  /**
   * Splits the specified image into its RGB components.
   *
   * @param image the {@link Image} to split
   * @return an array of {@link Image} containing the red, green, and blue components
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation[] rgbSplit(ImageRepresentation image) throws IOException;

  /**
   * Applies a sepia tone filter to the specified image.
   *
   * @param image the {@link Image} to apply the sepia filter to
   * @return a new {@link Image} that has the sepia effect applied
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation applySepia(ImageRepresentation image) throws IOException;

  /**
   * Blurs the specified image.
   *
   * @param image the {@link Image} to blur
   * @return a new {@link Image} that is blurred
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation blur(ImageRepresentation image) throws IOException;

  /**
   * Sharpens the specified image.
   *
   * @param image the {@link Image} to sharpen
   * @return a new {@link Image} that is sharpened
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation sharpen(ImageRepresentation image) throws IOException;

  /**
   * Applies a grayscale filter to the specified image.
   *
   * @param image the {@link Image} to apply the grayscale filter to
   * @return a new {@link Image} that has the grayscale effect applied
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation applyGreyScale(ImageRepresentation image) throws IOException;

  /**
   * Combines the red, green, and blue images into a single image.
   *
   * @param redImage   the {@link Image} containing the red component
   * @param greenImage the {@link Image} containing the green component
   * @param blueImage  the {@link Image} containing the blue component
   * @return a new {@link Image} that combines the red, green, and blue components
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation combineRgb(ImageRepresentation redImage, ImageRepresentation greenImage,
      ImageRepresentation blueImage) throws IOException;

  /**
   * Adjusts the levels of the specified image.
   *
   * @param image the {@link Image} to adjust
   * @param b     the black level
   * @param m     the mid level
   * @param w     the white level
   * @return a new {@link Image} with adjusted levels
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation levelsAdjust(ImageRepresentation image, int b, int m, int w)
      throws IOException;

  /**
   * Applies color correction to the specified image.
   *
   * @param image the {@link Image} to correct
   * @return a new {@link Image} that has been color corrected
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation colorCorrect(ImageRepresentation image) throws IOException;

  /**
   * Retrieves the histogram of the specified image.
   *
   * @param image the {@link Image} to analyze
   * @return a new {@link Image} representing the histogram
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation getHistogram(ImageRepresentation image) throws IOException;

  /**
   * Splits two images at a specified percentage and returns a combined image.
   *
   * @param image1  the first {@link ImageRepresentation} to split
   * @param image2  the second {@link ImageRepresentation} to split
   * @param percent the percentage at which to split the images
   * @return a new {@link ImageRepresentation} that combines parts of both images
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation splitImages(ImageRepresentation image1, ImageRepresentation image2,
      int percent) throws IOException;

  /**
   * Compresses the specified image based on the given quality factor.
   *
   * @param image   the {@link ImageRepresentation} to compress
   * @param quality the quality factor (0-1)
   * @return a new {@link ImageRepresentation} that is compressed
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation compressImage(ImageRepresentation image, float quality)
      throws IOException;

  /**
   * Downscales the specified image to the given width and height.
   *
   * @param image     the {@link ImageRepresentation} to downscale
   * @param newWidth  the desired width of the downscaled image
   * @param newHeight the desired height of the downscaled image
   * @return a new {@link ImageRepresentation} that is downscaled
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation downscaleImage(ImageRepresentation image, int newWidth, int newHeight)
      throws IOException;

  /**
   * Applies a sharpen effect to the source image using a mask and saves the result to the destination image.
   *
   * @param sourceImageName The name of the source image.
   * @param maskImageName   The name of the mask image.
   * @throws IOException If an error occurs during processing.
   */
  ImageRepresentation applySharpenWithMask(ImageRepresentation sourceImageName, ImageRepresentation maskImageName) throws IOException;

  /**
   * Applies a sepia effect to the source image using a mask and saves the result to the destination image.
   *
   * @param sourceImageName The name of the source image.
   * @param maskImageName   The name of the mask image.
   * @throws IOException If an error occurs during processing.
   */
  ImageRepresentation applySepiaWithMask(ImageRepresentation sourceImageName, ImageRepresentation maskImageName) throws IOException;

  /**
   * Applies a grayscale effect to the source image using a mask and saves the result to the destination image.
   *
   * @param sourceImageName The name of the source image.
   * @param maskImageName   The name of the mask image.
   * @throws IOException If an error occurs during processing.
   */
  ImageRepresentation applyGreyscaleWithMask(ImageRepresentation sourceImageName, ImageRepresentation maskImageName) throws IOException;
  ImageRepresentation applyBlurWithMask(ImageRepresentation sourceImageName, ImageRepresentation maskImageName) throws IOException;
  ImageRepresentation blueComponentWithMask(ImageRepresentation sourceImage, ImageRepresentation maskImage) throws IOException;
  ImageRepresentation redComponentWithMask(ImageRepresentation sourceImage, ImageRepresentation maskImage) throws IOException;
  ImageRepresentation greenComponentWithMask(ImageRepresentation sourceImage, ImageRepresentation maskImage) throws IOException;

}