package com.vanarp.model;

import java.io.IOException;

/**
 * This interface defines the contract for filtering operations that can be applied to images.
 * Implementing classes are expected to provide specific algorithms for various filtering effects.
 */
public interface FilteringOperations {

  /**
   * Extracts the red component of the given image.
   */
  ImageRepresentation redComponent(ImageRepresentation image) throws IOException;

  /**
   * Extracts the green component of the given image.
   */
  ImageRepresentation greenComponent(ImageRepresentation image) throws IOException;

  /**
   * Extracts the blue component of the given image.
   */
  ImageRepresentation blueComponent(ImageRepresentation image) throws IOException;

  /**
   * Extracts the value component (max RGB value) and returns a greyscale image.
   */
  ImageRepresentation valueComponent(ImageRepresentation image) throws IOException;

  /**
   * Extracts the luma component (brightness based on perceived luminance) and returns a greyscale
   * image.
   */
  ImageRepresentation lumaComponent(ImageRepresentation image) throws IOException;

  /**
   * Extracts the intensity component (average RGB value) and returns a greyscale image.
   */
  ImageRepresentation intensityComponent(ImageRepresentation image) throws IOException;

  /**
   * Applies sepia tone to the image.
   */
  ImageRepresentation applySepia(ImageRepresentation image) throws IOException;

  /**
   * Combines individaul RGB components into a single image.
   */
  ImageRepresentation rgbCombine(ImageRepresentation redImage, ImageRepresentation greenImage,
                                 ImageRepresentation blueImage) throws IOException;
}

