package com.vanarp.model;

import java.io.IOException;

/**
 * AbstractImageTransformation provides common functionality for applying pixel-level transformation
 * and kernel-based operations to images. It includes helper methods to validate images, apply
 * transformations,and create new images.
 */
abstract class AbstractImageTransformation implements ImageTransformationEnhanced {

  /**
   * Validates the provided image. If the image is null, an exception is thrown.
   *
   * @param image the image to validate
   * @throws IllegalArgumentException if the image is null
   */
  protected void validateImage(ImageRepresentation image) throws IOException {
    if (image == null) {
      throw new IllegalArgumentException("No image provided.");
    }
  }

  /**
   * Applies a pixel transformation to each pixel in the given image using the provided
   * transformer.
   *
   * @param image       the image to be transformed
   * @param transformer the transformer that defines the pixel transformation logic
   * @return a new Image with the transformed pixels
   * @throws IOException if an error occurs during transformation
   */
  protected ImageRepresentation applyTransformation(ImageRepresentation image,
                                                    PixelTransformer transformer)
          throws IOException {
    ImageRepresentation transformedImage = createNewImage(image);

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        transformedImage.setPixel(x, y, transformer.transform(x, y));
      }
    }
    return transformedImage;
  }

  /**
   * Creates a new image with the same dimensions as the source image.
   *
   * @param source the source image to copy dimensions from
   * @return a new Image object with the same width and height as the source image
   */
  protected ImageRepresentation createNewImage(ImageRepresentation source) {
    return new Image(source.getWidth(), source.getHeight());
  }

  /**
   * Creates a bounded pixel using the specified red, green, and blue values. The values are cast to
   * integers.
   *
   * @param red   the red value of the pixel
   * @param green the green value of the pixel
   * @param blue  the blue value of the pixel
   * @return a Pixel object created with the specified red, green, and blue values
   */
  protected PixelInterface createBoundedPixel(double red, double green, double blue) {
    return new Pixel((int) red, (int) green, (int) blue);
  }

  /**
   * Applies a convolution kernel to the image and returns the transformed image. The kernel is
   * applied to each pixel and its neighbors, using the kernel as weights.
   *
   * @param image  the image to apply the kernel to
   * @param kernel the kernel matrix to be applied to each pixel
   * @return a new Image object with the kernel applied
   * @throws IOException if an error occurs during the kernel operation
   */
  protected ImageRepresentation applyKernel(ImageRepresentation image, double[][] kernel)
          throws IOException {
    validateImage(image);
    int width = image.getWidth();
    int height = image.getHeight();
    ImageRepresentation result = createNewImage(image);
    int kernelSize = kernel.length;
    int kernelRadius = kernelSize / 2;

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        double red = 0;
        double green = 0;
        double blue = 0;

        for (int ky = 0; ky < kernelSize; ky++) {
          for (int kx = 0; kx < kernelSize; kx++) {
            int imageX = Math.min(Math.max(x + kx - kernelRadius, 0), width - 1);
            int imageY = Math.min(Math.max(y + ky - kernelRadius, 0), height - 1);
            PixelInterface pixel = image.getPixel(imageX, imageY);
            double weight = kernel[ky][kx];
            red += pixel.getRed() * weight;
            green += pixel.getGreen() * weight;
            blue += pixel.getBlue() * weight;
          }
        }

        result.setPixel(x, y, createBoundedPixel(red, green, blue));
      }
    }
    return result;
  }

  /**
   * An interface for transforming pixels during image processing. Implementing classes define the
   * logic for how each pixel is transformed.
   */
  public interface PixelTransformer {

    /**
     * Transforms a pixel at the specified coordinates.
     *
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @return the transformed Pixel
     */
    PixelInterface transform(int x, int y);
  }
}
