package com.vanarp.model;

/**
 * Interface for image compression operations.
 * <p>
 * This interface defines a method for applying compression to an image based on a specified
 * compression ratio. Implementing classes should provide the actual compression logic.
 * </p>
 */
public interface ImageCompressionFunctionality {

  /**
   * Applies compression to the given image based on the specified compression ratio.
   *
   * @param originalImage   the original {@link Image} to be compressed
   * @param compressionRatio the ratio of compression to apply (0-100)
   * @return a new {@link Image} that has been compressed
   */
  ImageRepresentation apply(ImageRepresentation originalImage, float compressionRatio);
}