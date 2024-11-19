package com.vanarp.model;

/**
 * A functional interface representing an operation that can be applied to a pixel.
 */
@FunctionalInterface
public interface PixelOperation {
  /**
   * Applies the operation to the given pixel.
   *
   * @param pixel the pixel to transform
   * @return the transformed pixel
   */
  PixelInterface apply(PixelInterface pixel);
}