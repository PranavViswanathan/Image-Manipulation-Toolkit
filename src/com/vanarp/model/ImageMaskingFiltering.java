package com.vanarp.model;

import java.io.IOException;

public interface ImageMaskingFiltering extends FilteringOperationEnhanced {

  /**
   * Applies a sharpen effect to the source image using a mask and saves the result to the
   * destination image.
   *
   * @param sourceImageName The name of the source image.
   * @param maskImageName   The name of the mask image.
   * @throws IOException If an error occurs during processing.
   */
  ImageRepresentation applySharpenWithMask(ImageRepresentation sourceImageName,
      ImageRepresentation maskImageName) throws IOException;

  /**
   * Applies a sepia effect to the source image using a mask and saves the result to the destination
   * image.
   *
   * @param sourceImageName The name of the source image.
   * @param maskImageName   The name of the mask image.
   * @throws IOException If an error occurs during processing.
   */
  ImageRepresentation applySepiaWithMask(ImageRepresentation sourceImageName,
      ImageRepresentation maskImageName) throws IOException;

  /**
   * Applies a grayscale effect to the source image using a mask and saves the result to the
   * destination image.
   *
   * @param sourceImageName The name of the source image.
   * @param maskImageName   The name of the mask image.
   * @throws IOException If an error occurs during processing.
   */
  ImageRepresentation applyGreyscaleWithMask(ImageRepresentation sourceImageName,
      ImageRepresentation maskImageName) throws IOException;

  ImageRepresentation applyBlurWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  ImageRepresentation blueComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  ImageRepresentation redComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  ImageRepresentation greenComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  ImageRepresentation valueComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  ImageRepresentation lumaComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  ImageRepresentation intensityComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;
}