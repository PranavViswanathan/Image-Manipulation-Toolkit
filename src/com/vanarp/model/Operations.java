package com.vanarp.model;

import com.vanarp.controller.ImageFileIO;
import java.io.IOException;

/**
 * The Operations class provides methods to perform various image operations such as loading,
 * saving, filtering, and transforming images. It delegates specific tasks to the respective
 * {@link Transform}, {@link Filtering}, and {@link ImageFileIO} implementations.
 */
public class Operations implements ImageOperations {

  private final ImageTransformationEnhanced transformation;
  private final ImageMaskingFiltering filtering;
  private final ImageCompressionFunctionality compression;


  /**
   * Constructs an Operations instance with the specified components.
   *
   * @param transformation the transformation operations
   * @param filtering      the filtering operations
   */

  public Operations(ImageTransformationEnhanced transformation, ImageMaskingFiltering filtering,
      ImageCompressionFunctionality compression) {
    this.transformation = transformation;
    this.filtering = filtering;
    this.compression = compression;
  }

  /**
   * Extracts the red component from the given image.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} containing only the red component
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation redComponent(ImageRepresentation image) throws IOException {
    return filtering.redComponent(image);
  }

  /**
   * Extracts the green component from the given image.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} containing only the green component
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation greenComponent(ImageRepresentation image) throws IOException {
    return filtering.greenComponent(image);
  }

  /**
   * Extracts the blue component from the given image.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} containing only the blue component
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation blueComponent(ImageRepresentation image) throws IOException {
    return filtering.blueComponent(image);
  }

  /**
   * Computes the value component (maximum RGB value) for each pixel in the image.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} representing the value component
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation valueComponent(ImageRepresentation image) throws IOException {
    return filtering.valueComponent(image);
  }

  /**
   * Computes the luma component (perceived brightness) for each pixel in the image.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} representing the luma component
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation lumaComponent(ImageRepresentation image) throws IOException {
    return filtering.lumaComponent(image);
  }

  /**
   * Computes the intensity component (average of RGB values) for each pixel in the image.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} representing the intensity component
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation intensityComponent(ImageRepresentation image) throws IOException {
    return filtering.intensityComponent(image);
  }

  /**
   * Flips the given image horizontally.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} flipped horizontally
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation flipHorizontally(ImageRepresentation image) throws IOException {
    return transformation.flipHorizontally(image);
  }

  /**
   * Flips the given image vertically.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} flipped vertically
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation flipVertically(ImageRepresentation image) throws IOException {
    return transformation.flipVertically(image);
  }

  /**
   * Brightens the given image by the specified increment.
   *
   * @param image     the input {@link Image}
   * @param increment the amount to increase brightness
   * @return a new {@link Image} with adjusted brightness
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation brightenImage(ImageRepresentation image, int increment)
      throws IOException {
    return transformation.adjustBrightness(image, increment);
  }

  /**
   * Splits the RGB components of the given image and saves them to separate files.
   *
   * @param image the input {@link Image}
   * @return The split check images.
   * @throws IOException if an error occurs during image processing or saving
   */
  public ImageRepresentation[] rgbSplit(ImageRepresentation image) throws IOException {
    ImageRepresentation redImage = filtering.redComponent(image);
    ImageRepresentation greenImage = filtering.greenComponent(image);
    ImageRepresentation blueImage = filtering.blueComponent(image);
    return new ImageRepresentation[]{redImage, greenImage, blueImage};
  }

  /**
   * Applies a sepia tone effect to the given image.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} with the sepia effect applied
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation applySepia(ImageRepresentation image) throws IOException {
    return filtering.applySepia(image);
  }

  /**
   * Applies a blur effect to the given image.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} with the blur effect applied
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation blur(ImageRepresentation image) throws IOException {
    return transformation.blur(image);
  }

  /**
   * Applies a sharpen effect to the given image.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} with the sharpen effect applied
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation sharpen(ImageRepresentation image) throws IOException {
    return transformation.sharpen(image);
  }

  /**
   * Applies a grayscale effect to the given image.
   *
   * @param image the input {@link Image}
   * @return a new {@link Image} with the grayscale effect applied
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation applyGreyScale(ImageRepresentation image) throws IOException {
    return filtering.lumaComponent(image);
  }

  /**
   * Combines three separate RGB images into a single image.
   *
   * @param redImage   the image containing the red component
   * @param greenImage the image containing the green component
   * @param blueImage  the image containing the blue component
   * @return a new {@link Image} combining the three color components
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation combineRgb(ImageRepresentation redImage,
      ImageRepresentation greenImage, ImageRepresentation blueImage) throws IOException {
    return filtering.rgbCombine(redImage, greenImage, blueImage);
  }

  /**
   * Adjusts the levels of each pixel in the image based on black, mid-tone, and white values.
   * Delegates to the filtering instance.
   *
   * @param image the image to process
   * @param b     the black level threshold (0-255)
   * @param m     the mid-tone level (0-255)
   * @param w     the white level threshold (0-255)
   * @return a new {@link Image} with adjusted levels
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation levelsAdjust(ImageRepresentation image, int b, int m, int w)
      throws IOException {
    return filtering.levelsAdjust(image, b, m, w);
  }

  /**
   * Applies color correction to the image.
   *
   * @param image the image to color correct
   * @return a new {@link Image} with color correction applied
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation colorCorrect(ImageRepresentation image) throws IOException {
    return filtering.colorCorrect(image);
  }

  /**
   * Generates a histogram of the given image.
   *
   * @param image the input {@link ImageRepresentation} for which the histogram is to be generated
   * @return a new {@link ImageRepresentation} representing the histogram of the input image
   */
  public ImageRepresentation getHistogram(ImageRepresentation image) {
    return transformation.getHistogram(image);
  }


  /**
   * Generates a split view of two images based on the given percentage.
   *
   * @param image1  the first image to use for the left side of the split
   * @param image2  the second image to use for the right side of the split
   * @param percent the percentage to determine the split position (0-100)
   * @return a new {@link Image} representing the split view of the two images
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation splitImages(ImageRepresentation image1, ImageRepresentation image2,
      int percent) throws IOException {
    return transformation.getSplitView(image1, image2, percent);
  }

  /**
   * Compresses the given image using the specified compression algorithm and quality settings.
   *
   * @param image   the input {@link ImageRepresentation} to be compressed
   * @param quality the quality level for compression, typically between 0 and 1 (higher values
   *                indicate better quality)
   * @return a new {@link ImageRepresentation} representing the compressed image
   * @throws IOException if an error occurs during compression or saving the compressed image
   */
  public ImageRepresentation compressImage(ImageRepresentation image, float quality)
      throws IOException {
    return compression.apply((Image) image, quality);
  }

  /**
   * Downscales the given image to the specified width and height.
   *
   * @param image     the input {@link ImageRepresentation} to downscale
   * @param newWidth  the desired width of the downscaled image
   * @param newHeight the desired height of the downscaled image
   * @return a new {@link ImageRepresentation} that is downscaled
   * @throws IOException if an error occurs during the image processing
   */
  public ImageRepresentation downscaleImage(ImageRepresentation image, int newWidth, int newHeight)
      throws IOException {
    return transformation.downscale(image, newWidth, newHeight);
  }

  @Override
  public ImageRepresentation applySharpenWithMask(ImageRepresentation sourceImageName,
      ImageRepresentation maskImageName) throws IOException {
    return filtering.applySharpenWithMask(sourceImageName, maskImageName);
  }

  @Override
  public ImageRepresentation applySepiaWithMask(ImageRepresentation sourceImageName,
      ImageRepresentation maskImageName) throws IOException {
    return filtering.applySepiaWithMask(sourceImageName, maskImageName);
  }

  @Override
  public ImageRepresentation applyGreyscaleWithMask(ImageRepresentation sourceImageName,
      ImageRepresentation maskImageName) throws IOException {
    return filtering.applyGreyscaleWithMask(sourceImageName, maskImageName);
  }

  @Override
  public ImageRepresentation applyBlurWithMask(ImageRepresentation sourceImageName,
      ImageRepresentation maskImageName) throws IOException {
    return filtering.applyBlurWithMask(sourceImageName, maskImageName);
  }

  @Override
  public ImageRepresentation blueComponentWithMask(ImageRepresentation sourceImageName,
      ImageRepresentation maskImageName) throws IOException {
    return filtering.blueComponentWithMask(sourceImageName, maskImageName);
  }

  @Override
  public ImageRepresentation redComponentWithMask(ImageRepresentation sourceImageName,
      ImageRepresentation maskImageName) throws IOException {
    return filtering.redComponentWithMask(sourceImageName, maskImageName);
  }

  @Override
  public ImageRepresentation greenComponentWithMask(ImageRepresentation sourceImageName,
      ImageRepresentation maskImageName) throws IOException {
    return filtering.greenComponentWithMask(sourceImageName, maskImageName);
  }

  @Override
  public ImageRepresentation lumaComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    return filtering.lumaComponentWithMask(sourceImage, maskImage);
  }

  @Override
  public ImageRepresentation intensityComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    return filtering.intensityComponentWithMask(sourceImage, maskImage);
  }

  @Override
  public ImageRepresentation valueComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    return filtering.valueComponentWithMask(sourceImage, maskImage);
  }
}
