package com.vanarp.model;

/**
 * An interface representing a pixel in an image, defining methods to access
 * its color components and obtain a string representation.
 */
public interface PixelInterface {

  /**
   * Retrieves the red component of the pixel.
   *
   * @return the red value (0-255) of the pixel.
   */
  int getRed();

  /**
   * Retrieves the green component of the pixel.
   *
   * @return the green value (0-255) of the pixel.
   */
  int getGreen();

  /**
   * Retrieves the blue component of the pixel.
   *
   * @return the blue value (0-255) of the pixel.
   */
  int getBlue();

  /**
   * Returns a string representation of the pixel.
   *
   * @return a formatted string representing the pixel.
   */
  @Override
  String toString();
}
