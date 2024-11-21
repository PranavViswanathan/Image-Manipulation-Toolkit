package com.vanarp.model;

import java.io.IOException;

/**
 * The {@code ImageMaskingFiltering} interface extends {@code FilteringOperationEnhanced} and
 * provides methods to apply various image filtering effects with masking functionality. Each method
 * uses a source image and a mask image to determine which parts of the image will be affected by
 * the operation.
 */
public interface ImageMaskingFiltering extends FilteringOperationEnhanced {

  /**
   * Applies a sharpen effect to the source image using a mask.
   *
   * @param sourceImage the source {@code ImageRepresentation}.
   * @param maskImage   the mask {@code ImageRepresentation} defining the regions to apply the
   *                    effect.
   * @return the resulting {@code ImageRepresentation} with the sharpen effect applied.
   * @throws IOException if an error occurs during processing.
   */
  ImageRepresentation applySharpenWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  /**
   * Applies a sepia effect to the source image using a mask.
   *
   * @param sourceImage the source {@code ImageRepresentation}.
   * @param maskImage   the mask {@code ImageRepresentation} defining the regions to apply the
   *                    effect.
   * @return the resulting {@code ImageRepresentation} with the sepia effect applied.
   * @throws IOException if an error occurs during processing.
   */
  ImageRepresentation applySepiaWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  /**
   * Applies a grayscale effect to the source image using a mask.
   *
   * @param sourceImage the source {@code ImageRepresentation}.
   * @param maskImage   the mask {@code ImageRepresentation} defining the regions to apply the
   *                    effect.
   * @return the resulting {@code ImageRepresentation} with the grayscale effect applied.
   * @throws IOException if an error occurs during processing.
   */
  ImageRepresentation applyGreyscaleWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  /**
   * Applies a blur effect to the source image using a mask.
   *
   * @param sourceImage the source {@code ImageRepresentation}.
   * @param maskImage   the mask {@code ImageRepresentation} defining the regions to apply the
   *                    effect.
   * @return the resulting {@code ImageRepresentation} with the blur effect applied.
   * @throws IOException if an error occurs during processing.
   */
  ImageRepresentation applyBlurWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  /**
   * Applies a blue component visualization to the source image using a mask.
   *
   * @param sourceImage the source {@code ImageRepresentation}.
   * @param maskImage   the mask {@code ImageRepresentation} defining the regions to apply the
   *                    effect.
   * @return the resulting {@code ImageRepresentation} with the blue component visualized.
   * @throws IOException if an error occurs during processing.
   */
  ImageRepresentation blueComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  /**
   * Applies a red component visualization to the source image using a mask.
   *
   * @param sourceImage the source {@code ImageRepresentation}.
   * @param maskImage   the mask {@code ImageRepresentation} defining the regions to apply the
   *                    effect.
   * @return the resulting {@code ImageRepresentation} with the red component visualized.
   * @throws IOException if an error occurs during processing.
   */
  ImageRepresentation redComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  /**
   * Applies a green component visualization to the source image using a mask.
   *
   * @param sourceImage the source {@code ImageRepresentation}.
   * @param maskImage   the mask {@code ImageRepresentation} defining the regions to apply the
   *                    effect.
   * @return the resulting {@code ImageRepresentation} with the green component visualized.
   * @throws IOException if an error occurs during processing.
   */
  ImageRepresentation greenComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  /**
   * Applies a value component visualization to the source image using a mask.
   *
   * @param sourceImage the source {@code ImageRepresentation}.
   * @param maskImage   the mask {@code ImageRepresentation} defining the regions to apply the
   *                    effect.
   * @return the resulting {@code ImageRepresentation} with the value component visualized.
   * @throws IOException if an error occurs during processing.
   */
  ImageRepresentation valueComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  /**
   * Applies a luma component visualization to the source image using a mask.
   *
   * @param sourceImage the source {@code ImageRepresentation}.
   * @param maskImage   the mask {@code ImageRepresentation} defining the regions to apply the
   *                    effect.
   * @return the resulting {@code ImageRepresentation} with the luma component visualized.
   * @throws IOException if an error occurs during processing.
   */
  ImageRepresentation lumaComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;

  /**
   * Applies an intensity component visualization to the source image using a mask.
   *
   * @param sourceImage the source {@code ImageRepresentation}.
   * @param maskImage   the mask {@code ImageRepresentation} defining the regions to apply the
   *                    effect.
   * @return the resulting {@code ImageRepresentation} with the intensity component visualized.
   * @throws IOException if an error occurs during processing.
   */
  ImageRepresentation intensityComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException;
}
