package com.vanarp.model;

import java.awt.image.BufferedImage;

/**
 * The {@code ImageRepresentation} interface defines the essential operations for manipulating and
 * accessing pixel data in an image. This interface is designed to be implemented by various classes
 * that represent images, regardless of whether they are compressed or uncompressed and independent
 * of the image format.
 *
 * @author Pranav Viswanathan, Saran
 */
public interface ImageRepresentation {

  /**
   * Sets a specific pixel in the image at the given (x, y) coordinates.
   *
   * @param x     the x-coordinate of the pixel to be set.
   * @param y     the y-coordinate of the pixel to be set.
   * @param pixel the pixel representation of the pixel to be stored.
   * @throws IndexOutOfBoundsException if the specified coordinates are out of the image bounds.
   */
  void setPixel(int x, int y, PixelInterface pixel);

  /**
   * Retrieves a specific pixel from the image at the given (x, y) coordinates.
   *
   * @param x the x-coordinate of the pixel to be retrieved.
   * @param y the y-coordinate of the pixel to be retrieved.
   * @return the pixel representation of the pixel at the specified coordinates.
   * @throws IllegalArgumentException if the specified coordinates are out of the image bounds.
   */
  PixelInterface getPixel(int x, int y);

  /**
   * Returns the width of this image.
   *
   * @return the width of the image in pixels.
   */
  int getWidth();

  /**
   * Returns the height of this image.
   *
   * @return the height of the image in pixels.
   */
  int getHeight();

  BufferedImage toBufferedImage();
}
