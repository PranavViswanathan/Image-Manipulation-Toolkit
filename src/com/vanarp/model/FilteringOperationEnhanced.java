package com.vanarp.model;

import java.io.IOException;

/**
 * The FilteringOperationEnhanced interface extends the {@link FilteringOperations} interface
 * to provide additional image filtering operations, specifically for adjusting levels and
 * applying color correction to images.
 */
public interface FilteringOperationEnhanced extends FilteringOperations {

  /**
   * Adjusts the levels of the specified image based on the provided shadow, mid, and highlight
   * values.
   *
   * @param image the input {@link ImageRepresentation} to adjust
   * @param b     the shadow point (0-255)
   * @param m     the mid point (0-255)
   * @param w     the highlight point (0-255)
   * @return a new {@link ImageRepresentation} with adjusted levels
   * @throws IOException if an error occurs during image processing
   */
  ImageRepresentation levelsAdjust(ImageRepresentation image, int b, int m, int w)
          throws IOException;

  /**
   * Applies color correction to the image for improved color balance and visual appeal.
   *
   * @param image the input {@link ImageRepresentation} to color correct
   * @return a new {@link ImageRepresentation} with color correction applied
   * @throws IOException if an error occurs during image processing
   */
  ImageRepresentation colorCorrect(ImageRepresentation image) throws IOException;

}