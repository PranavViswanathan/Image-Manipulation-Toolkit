package com.vanarp.model;

import java.io.IOException;

/**
 * The {@code ImageTransformationEnhanced} interface extends the {@link ImageTransformation}
 * interface to provide additional methods for enhanced image transformation operations.
 * <p>
 * This interface includes methods for retrieving the histogram of an image and for creating a split
 * view of two images based on a specified percentage. Implementations of this interface should
 * provide the actual logic for these operations.
 * </p>
 */
public interface ImageTransformationEnhanced extends ImageTransformation {

  /**
   * Retrieves the histogram of the specified image.
   *
   * @param image the {@link ImageRepresentation} to analyze
   * @return a new {@link ImageRepresentation} representing the histogram of the input image
   */
  ImageRepresentation getHistogram(ImageRepresentation image);

  /**
   * Creates a split view of two images at a specified percentage.
   *
   * @param image1  the first {@link ImageRepresentation} to split
   * @param image2  the second {@link ImageRepresentation} to split
   * @param percent the percentage at which to split the images (0-100)
   * @return a new {@link ImageRepresentation} that combines parts of both images based on the
   *        specified percentage
   * @throws IOException if an error occurs during the operation
   */
  ImageRepresentation getSplitView(ImageRepresentation image1, ImageRepresentation image2,
      int percent) throws IOException;
}