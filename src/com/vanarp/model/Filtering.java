package com.vanarp.model;

import java.io.IOException;

/**
 * The Filtering class provides various image filtering operations such as extracting color
 * components (red, green, blue), applying grayscale, sepia toning, and calculating luma and
 * intensity values. It extends the {@link AbstractFilteringOperations} to leverage common image
 * processing functionalities.
 */
public class Filtering extends AbstractFilteringOperations {

  /**
   * Extracts the red component from the given image.
   *
   * @param image the input {@link ImageRepresentation}
   * @return a new {@link ImageRepresentation} containing only the red component
   * @throws IOException if an error occurs during image processing
   */
  @Override
  public ImageRepresentation redComponent(ImageRepresentation image) throws IOException {
    return processImage(image, pixel -> {
      int red = pixel.getRed();
      return new Pixel(red, red, red);
    });
  }

  /**
   * Extracts the green component from the given image.
   *
   * @param image the input {@link ImageRepresentation}
   * @return a new {@link ImageRepresentation} containing only the green component
   * @throws IOException if an error occurs during image processing
   */
  @Override
  public ImageRepresentation greenComponent(ImageRepresentation image) throws IOException {
    return processImage(image, pixel -> {
      int green = pixel.getGreen();
      return new Pixel(green, green, green);
    });
  }

  /**
   * Extracts the blue component from the given image.
   *
   * @param image the input {@link ImageRepresentation}
   * @return a new {@link ImageRepresentation} containing only the blue component
   * @throws IOException if an error occurs during image processing
   */
  @Override
  public ImageRepresentation blueComponent(ImageRepresentation image) throws IOException {
    return processImage(image, pixel -> {
      int blue = pixel.getBlue();
      return new Pixel(blue, blue, blue);
    });
  }

  /**
   * Computes the value component (maximum RGB value) for each pixel in the image.
   *
   * @param image the input {@link ImageRepresentation}
   * @return a new {@link ImageRepresentation} representing the value component
   * @throws IOException if an error occurs during image processing
   */
  @Override
  public ImageRepresentation valueComponent(ImageRepresentation image) throws IOException {
    return processImage(image, pixel -> {
      int value = Math.max(Math.max(pixel.getRed(), pixel.getGreen()), pixel.getBlue());
      return new Pixel(value, value, value);
    });
  }

  /**
   * Computes the luma component (perceived brightness) for each pixel in the image.
   *
   * @param image the input {@link ImageRepresentation}
   * @return a new {@link ImageRepresentation} representing the luma component
   * @throws IOException if an error occurs during image processing
   */
  @Override
  public ImageRepresentation lumaComponent(ImageRepresentation image) throws IOException {
    return processImage(image, pixel -> {
      int luma = (int) (0.2126 * pixel.getRed() + 0.7152 * pixel.getGreen()
          + 0.0722 * pixel.getBlue());
      return new Pixel(luma, luma, luma);
    });
  }

  /**
   * Computes the intensity component (average of RGB values) for each pixel in the image.
   *
   * @param image the input {@link ImageRepresentation}
   * @return a new {@link ImageRepresentation} representing the intensity component
   * @throws IOException if an error occurs during image processing
   */
  @Override
  public ImageRepresentation intensityComponent(ImageRepresentation image) throws IOException {
    return processImage(image, pixel -> {
      int intensity = (pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3;
      return new Pixel(intensity, intensity, intensity);
    });
  }

  /**
   * Applies a sepia tone effect to the given image.
   *
   * @param image the input {@link ImageRepresentation}
   * @return a new {@link ImageRepresentation} with the sepia effect applied
   * @throws IOException if an error occurs during image processing
   */
  @Override
  public ImageRepresentation applySepia(ImageRepresentation image) throws IOException {
    return processImage(image, pixel -> {
      int r = pixel.getRed();
      int g = pixel.getGreen();
      int b = pixel.getBlue();
      int newRed = (int) Math.min(255, (r * 0.393 + g * 0.769 + b * 0.189));
      int newGreen = (int) Math.min(255, (r * 0.349 + g * 0.686 + b * 0.168));
      int newBlue = (int) Math.min(255, (r * 0.272 + g * 0.534 + b * 0.131));
      return new Pixel(newRed, newGreen, newBlue);
    });
  }

  /**
   * Combines three images (red, green, and blue components) into a single RGB image.
   *
   * @param redImage   the image containing the red component
   * @param greenImage the image containing the green component
   * @param blueImage  the image containing the blue component
   * @return a new {@link ImageRepresentation} combining the three color components
   * @throws IOException              if an error occurs during image processing
   * @throws IllegalArgumentException if the dimensions of the input images do not match or if any
   *                                  channel is not grayscale
   */
  public ImageRepresentation rgbCombine(ImageRepresentation redImage,
      ImageRepresentation greenImage,
      ImageRepresentation blueImage) throws IOException {
    if (redImage == null || greenImage == null || blueImage == null) {
      throw new IOException("One or more input images are null.");
    }
    validateGrayscale(redImage);
    validateGrayscale(greenImage);
    validateGrayscale(blueImage);

    if (redImage.getWidth() != greenImage.getWidth()
        || redImage.getHeight() != greenImage.getHeight()
        || redImage.getWidth() != blueImage.getWidth()
        || redImage.getHeight() != blueImage.getHeight()) {
      throw new IllegalArgumentException("All images must have the same dimensions.");
    }

    ImageRepresentation result = new Image(redImage.getWidth(), redImage.getHeight());

    for (int y = 0; y < result.getHeight(); y++) {
      for (int x = 0; x < result.getWidth(); x++) {
        PixelInterface redPixel = redImage.getPixel(x, y);
        PixelInterface greenPixel = greenImage.getPixel(x, y);
        PixelInterface bluePixel = blueImage.getPixel(x, y);

        if (redPixel == null || greenPixel == null || bluePixel == null) {
          throw new IllegalArgumentException(
              "One of the pixels is null at coordinates (" + x + ", " + y + ").");
        }

        result.setPixel(x, y, new Pixel(
            redPixel.getRed(),
            greenPixel.getGreen(),
            bluePixel.getBlue()
        ));
      }
    }
    return result;
  }

  private void validateGrayscale(ImageRepresentation channel) {
    for (int y = 0; y < channel.getHeight(); y++) {
      for (int x = 0; x < channel.getWidth(); x++) {
        PixelInterface pixel = channel.getPixel(x, y);
        if (pixel == null) {
          throw new IllegalArgumentException("Pixel at (" + x + ", " + y + ") is null.");
        }
        int r = pixel.getRed();
        int g = pixel.getGreen();
        int b = pixel.getBlue();
        if (r != g || g != b) {
          throw new IllegalArgumentException("Channel image is not grayscale.");
        }
      }
    }
  }

  private double a;
  private double b2;
  private double c;
  private int b;
  private int m;
  private int w;

  /**
   * Adjusts the levels of the image based on the provided shadow, mid, and highlight values.
   *
   * @param image     the input {@link ImageRepresentation}
   * @param shadow    the shadow point (0-255)
   * @param mid       the mid point (0-255)
   * @param highlight the highlight point (0-255)
   * @return a new {@link ImageRepresentation} with adjusted levels
   * @throws IOException if an error occurs during image processing
   */
  public ImageRepresentation levelsAdjust(ImageRepresentation image, int shadow, int mid,
      int highlight) throws IOException {
    if (shadow < 0 || shadow > 255 || mid < 0 || mid > 255 || highlight < 0 || highlight > 255) {
      throw new IllegalArgumentException(
          "Shadow, mid, and highlight values must be between 0 and 255.");
    }
    if (shadow >= mid || mid >= highlight) {
      throw new IllegalArgumentException(
          "Values must be in ascending order: shadow < mid < highlight.");
    }
    this.b = shadow;
    this.m = mid;
    this.w = highlight;
    calculateQuadraticCoefficients();
    return processImage(image, pixel -> {
      int red = adjustChannel(pixel.getRed());
      int green = adjustChannel(pixel.getGreen());
      int blue = adjustChannel(pixel.getBlue());
      return new Pixel(red, green, blue);
    });
  }

  private void calculateQuadraticCoefficients() {
    double[][] matrix = {
        {b * b, b, 1},
        {m * m, m, 1},
        {w * w, w, 1}
    };
    double[] values = {0, 128, 255};
    double det = matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
        - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
        + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);

    a = ((values[0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]))
        - (matrix[0][1] * (values[1] * matrix[2][2] - matrix[1][2] * values[2]))
        + (matrix[0][2] * (values[1] * matrix[2][1] - matrix[1][1] * values[2]))) / det;

    b2 = ((matrix[0][0] * (values[1] * matrix[2][2] - matrix[1][2] * values[2]))
        - (values[0] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]))
        + (matrix[0][2] * (matrix[1][0] * values[2] - values[1] * matrix[2][0]))) / det;

    c = ((matrix[0][0] * (matrix[1][1] * values[2] - values[1] * matrix[2][1]))
        - (matrix[0][1] * (matrix[1][0] * values[2] - values[1] * matrix[2][0]))
        + (values[0] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]))) / det;
  }

  private int adjustChannel(int value) {
    double adjusted = a * value * value + b2 * value + c;
    return (int) Math.max(0, Math.min(255, Math.round(adjusted)));
  }

  /**
   * Applies color correction to the image for improved color balance and visual appeal.
   *
   * @param image the input {@link ImageRepresentation}
   * @return a new {@link ImageRepresentation} with color correction applied
   * @throws IOException if an error occurs during image processing
   */
  @Override
  public ImageRepresentation colorCorrect(ImageRepresentation image) throws IOException {
    int[] redPeak = {0};
    int[] greenPeak = {0};
    int[] bluePeak = {0};
    int[] redCounts = new int[256];
    int[] greenCounts = new int[256];
    int[] blueCounts = new int[256];

    int maxRedCount = 0;
    int maxGreenCount = 0;
    int maxBlueCount = 0;

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        PixelInterface pixel = image.getPixel(x, y);
        redCounts[pixel.getRed()]++;
        greenCounts[pixel.getGreen()]++;
        blueCounts[pixel.getBlue()]++;

        if (isMeaningfulPeak(pixel.getRed()) && redCounts[pixel.getRed()] > maxRedCount) {
          maxRedCount = redCounts[pixel.getRed()];
          redPeak[0] = pixel.getRed();
        }
        if (isMeaningfulPeak(pixel.getGreen()) && greenCounts[pixel.getGreen()] > maxGreenCount) {
          maxGreenCount = greenCounts[pixel.getGreen()];
          greenPeak[0] = pixel.getGreen();
        }
        if (isMeaningfulPeak(pixel.getBlue()) && blueCounts[pixel.getBlue()] > maxBlueCount) {
          maxBlueCount = blueCounts[pixel.getBlue()];
          bluePeak[0] = pixel.getBlue();
        }
      }
    }
    int avgPeak = (redPeak[0] + greenPeak[0] + bluePeak[0]) / 3;
    return (processImage(image, pixel -> {
      int red = (int) Math.min(255, Math.max(0, pixel.getRed() + (avgPeak - redPeak[0])));
      int green = (int) Math.min(255, Math.max(0, pixel.getGreen() + (avgPeak - greenPeak[0])));
      int blue = (int) Math.min(255, Math.max(0, pixel.getBlue() + (avgPeak - bluePeak[0])));
      return new Pixel(red, green, blue);
    }));
  }

  private boolean isMeaningfulPeak(int value) {
    return value > 0;
  }

  @Override
  public ImageRepresentation applyBlurWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    Transform transform = new Transform();
    ImageRepresentation bluredImage = transform.blur(sourceImage);
    return blendWithMask(sourceImage, bluredImage, maskImage);
  }

  @Override
  public ImageRepresentation blueComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    Filtering filter = new Filtering();
    ImageRepresentation blue = filter.blueComponent(sourceImage);
    return blendWithMask(sourceImage, blue, maskImage);
  }

  @Override
  public ImageRepresentation redComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    Filtering filter = new Filtering();
    ImageRepresentation red = filter.blueComponent(sourceImage);
    return blendWithMask(sourceImage, red, maskImage);
  }

  @Override
  public ImageRepresentation greenComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    Filtering filter = new Filtering();
    ImageRepresentation green = filter.greenComponent(sourceImage);
    return blendWithMask(sourceImage, green, maskImage);
  }

  @Override
  public ImageRepresentation applySharpenWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    Transform transform = new Transform();
    ImageRepresentation sharpenedImage = transform.sharpen(sourceImage);
    return blendWithMask(sourceImage, sharpenedImage, maskImage);
  }

  @Override
  public ImageRepresentation applySepiaWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    Filtering filtering = new Filtering();
    ImageRepresentation sepiaImage = filtering.applySepia(sourceImage);
    return blendWithMask(sourceImage, sepiaImage, maskImage);
  }

  @Override
  public ImageRepresentation applyGreyscaleWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    ImageRepresentation greyscaleImage = processImage(sourceImage, pixel -> {
      int grey = (int) (0.2126 * pixel.getRed() + 0.7152 * pixel.getGreen()
          + 0.0722 * pixel.getBlue());
      return new Pixel(grey, grey, grey);
    });
    return blendWithMask(sourceImage, greyscaleImage, maskImage);
  }

  @Override
  public ImageRepresentation valueComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    Filtering filter = new Filtering();
    ImageRepresentation green = filter.valueComponent(sourceImage);
    return blendWithMask(sourceImage, green, maskImage);
  }

  @Override
  public ImageRepresentation lumaComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    Filtering filter = new Filtering();
    ImageRepresentation green = filter.lumaComponent(sourceImage);
    return blendWithMask(sourceImage, green, maskImage);
  }

  @Override
  public ImageRepresentation intensityComponentWithMask(ImageRepresentation sourceImage,
      ImageRepresentation maskImage) throws IOException {
    Filtering filter = new Filtering();
    ImageRepresentation green = filter.intensityComponent(sourceImage);
    return blendWithMask(sourceImage, green, maskImage);
  }

  /**
   * Blends two images based on a mask.
   *
   * @param originalImage  The original image.
   * @param processedImage The processed image (sharpened, sepia, or greyscale).
   * @param maskImage      The mask image.
   * @return A new {@link ImageRepresentation} that is the result of blending.
   * @throws IOException if an error occurs during processing.
   */
  private ImageRepresentation blendWithMask(ImageRepresentation originalImage,
      ImageRepresentation processedImage,
      ImageRepresentation maskImage) throws IOException {
    if (originalImage.getWidth() != maskImage.getWidth()
        || originalImage.getHeight() != maskImage.getHeight()) {
      throw new IllegalArgumentException("Original image and mask image "
          + "must have the same dimensions.");
    }

    ImageRepresentation resultImage = new Image(originalImage.getWidth(),
        originalImage.getHeight());

    for (int y = 0; y < resultImage.getHeight(); y++) {
      for (int x = 0; x < resultImage.getWidth(); x++) {
        PixelInterface originalPixel = originalImage.getPixel(x, y);
        PixelInterface processedPixel = processedImage.getPixel(x, y);
        PixelInterface maskPixel = maskImage.getPixel(x, y);
        int maskValue = maskPixel.getRed();

        Pixel blendedPixel;

        if (maskValue == 0) {
          blendedPixel = new Pixel(processedPixel.getRed(), processedPixel.getGreen(),
              processedPixel.getBlue());
        } else {
          int blendedRed = (originalPixel.getRed() * (255 - maskValue)
              + processedPixel.getRed() * maskValue) / 255;
          int blendedGreen = (originalPixel.getGreen() * (255 - maskValue)
              + processedPixel.getGreen() * maskValue) / 255;
          int blendedBlue = (originalPixel.getBlue() * (255 - maskValue)
              + processedPixel.getBlue() * maskValue) / 255;

          blendedPixel = new Pixel(blendedRed, blendedGreen, blendedBlue);
        }
        resultImage.setPixel(x, y, blendedPixel);
      }
    }
    return resultImage;
  }
}