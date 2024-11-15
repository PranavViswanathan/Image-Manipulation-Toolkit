package com.vanarp.model;

import java.io.IOException;

/**
 * Interface to provide functionality towards transforming images.
 */
public interface ImageTransformation {

  /**
   * Flips an image horizontally.
   */
  ImageRepresentation flipHorizontally(ImageRepresentation image) throws IOException;

  /**
   * Flips an image vertically.
   */
  ImageRepresentation flipVertically(ImageRepresentation image) throws IOException;

  /**
   * Brightens or darkens the image based on the given increment.
   *
   * @param increment The value to adjust brightness; positive brightens and negative darkens.
   */
  ImageRepresentation adjustBrightness(ImageRepresentation image, int increment) throws IOException;

  /**
   * Blurs the image.
   */
  ImageRepresentation blur(ImageRepresentation image) throws IOException;

  /**
   * Sharpens the image.
   */
  ImageRepresentation sharpen(ImageRepresentation image) throws IOException;
}
