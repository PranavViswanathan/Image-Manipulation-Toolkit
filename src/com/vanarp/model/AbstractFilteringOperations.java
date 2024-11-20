package com.vanarp.model;

import java.io.IOException;

/**
 * The AbstractFilteringOperations class provides a base implementation for filtering operations
 * that manipulate images at the pixel level. It defines a common method to process images by
 * transforming each pixel using a specified pixel transformer.
 */
public abstract class AbstractFilteringOperations implements ImageMaskingFiltering {

  /**
   * Processes the given image by applying a pixel transformation to each pixel. A new image is
   * created where each pixel is the result of the transformation.
   *
   * @param image       the image to process
   * @param transformer the transformer that defines how each pixel is modified
   * @return a new Image object containing the transformed pixels
   * @throws IOException              if an error occurs during the operation
   * @throws IllegalArgumentException if the provided image is null
   */
  protected ImageRepresentation processImage(ImageRepresentation image,
                                             PixelTransformer transformer) throws IOException {
    if (image == null) {
      throw new IllegalArgumentException("No image provided.");
    }
    int width = image.getWidth();
    int height = image.getHeight();
    ImageRepresentation resultImage = new Image(width, height);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        PixelInterface pixel = image.getPixel(x, y);
        PixelInterface transformedPixel = transformer.transformPixel((Pixel) pixel);
        resultImage.setPixel(x, y, transformedPixel);
      }
    }
    return resultImage;
  }

  /**
   * An interface representing a transformation that can be applied to a pixel. Implementations of
   * this interface define how a pixel is modified.
   */
  protected interface PixelTransformer {

    /**
     * Transforms the given pixel and returns the transformed result.
     *
     * @param pixel the pixel to transform
     * @return the transformed pixel
     */
    PixelInterface transformPixel(Pixel pixel);
  }
}
