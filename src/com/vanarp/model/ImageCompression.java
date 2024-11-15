package com.vanarp.model;

import java.util.Arrays;


/**
 * The ImageCompression class implements the ImageCompressionFunctionality interface to provide
 * methods for compressing images using the Haar wavelet transform.
 */
public class ImageCompression implements ImageCompressionFunctionality {

  private static final int MAX_COLOR_VALUE = 255;

  /**
   * Applies compression to the given image based on the specified compression ratio.
   *
   * @param originalImage    the original {@link Image} to be compressed
   * @param compressionRatio the ratio of compression to apply (0-100)
   * @return a new {@link Image} that has been compressed
   * @throws IllegalArgumentException if the original image is null or has invalid dimensions
   */
  @Override
  public ImageRepresentation apply(ImageRepresentation originalImage, float compressionRatio) {
    if (originalImage == null || originalImage.getHeight() == 0 || originalImage.getWidth() == 0) {
      throw new IllegalArgumentException("Invalid image data");
    }

    int originalHeight = originalImage.getHeight();
    int originalWidth = originalImage.getWidth();

    int paddedHeight = nextPowerOfTwo(originalHeight);
    int paddedWidth = nextPowerOfTwo(originalWidth);
    ImageRepresentation paddedImage = padImage((Image) originalImage, paddedHeight, paddedWidth);

    int[][] redChannel = new int[paddedHeight][paddedWidth];
    int[][] greenChannel = new int[paddedHeight][paddedWidth];
    int[][] blueChannel = new int[paddedHeight][paddedWidth];

    for (int j = 0; j < paddedHeight; j++) {
      for (int i = 0; i < paddedWidth; i++) {
        if (j < paddedImage.getHeight() && i < paddedImage.getWidth()) {
          Pixel pixel = (Pixel) paddedImage.getPixel(j, i);
          redChannel[j][i] = pixel.getRed();
          greenChannel[j][i] = pixel.getGreen();
          blueChannel[j][i] = pixel.getBlue();
        } else {
          redChannel[j][i] = 0;
          greenChannel[j][i] = 0;
          blueChannel[j][i] = 0;
        }
      }
    }

    redChannel = compressChannel(redChannel, compressionRatio);
    greenChannel = compressChannel(greenChannel, compressionRatio);
    blueChannel = compressChannel(blueChannel, compressionRatio);

    ImageRepresentation compressedImage = new Image(paddedHeight, paddedWidth);
    for (int j = 0; j < paddedHeight; j++) {
      for (int i = 0; i < paddedWidth; i++) {
        compressedImage.setPixel(j, i, new Pixel(
                clamp(redChannel[j][i]),
                clamp(greenChannel[j][i]),
                clamp(blueChannel[j][i])
        ));
      }
    }
    return unpadImage((Image) compressedImage, originalHeight, originalWidth);
  }

  private int clamp(int value) {
    return Math.max(0, Math.min(MAX_COLOR_VALUE, value));
  }

  private int[][] compressChannel(int[][] channel, float compressionRatio) {
    int[][] transformed = haarTransform(channel);
    transformed = applyCompression(transformed, compressionRatio);
    return inverseHaarTransform(transformed);
  }

  private int[][] haarTransform(int[][] data) {
    int[][] result = Arrays.copyOf(data, data.length);
    for (int j = 0; j < data.length; j++) {
      haarTransform1D(result[j]);
    }
    for (int i = 0; i < data[0].length; i++) {
      int[] column = new int[data.length];
      for (int j = 0; j < data.length; j++) {
        column[j] = result[j][i];
      }
      haarTransform1D(column);
      for (int j = 0; j < data.length; j++) {
        result[j][i] = column[j];
      }
    }
    return result;
  }

  private void haarTransform1D(int[] data) {
    if (data.length <= 1) {
      return;
    }
    int[] temp = new int[data.length];
    int h = data.length >> 1;
    for (int i = 0; i < h; i++) {
      int k = i << 1;
      temp[i] = (data[k] + data[k + 1]) >> 1;
      temp[i + h] = data[k] - data[k + 1];
    }
    System.arraycopy(temp, 0, data, 0, data.length);
  }

  private int[][] applyCompression(int[][] data, float compressionRatio) {
    int totalElements = data.length * data[0].length;
    int elementsToZero = (int) (totalElements * (compressionRatio / 100.0));
    if (compressionRatio == 100) {
      elementsToZero -= 1;
    }
    int[] allValues = Arrays.stream(data)
            .flatMapToInt(Arrays::stream)
            .map(Math::abs)
            .toArray();
    Arrays.sort(allValues);
    int threshold = allValues[elementsToZero];

    for (int j = 0; j < data.length; j++) {
      for (int i = 0; i < data[0].length; i++) {
        if (Math.abs(data[j][i]) < threshold) {
          data[j][i] = 0;
        }
      }
    }
    return data;
  }

  private int[][] inverseHaarTransform(int[][] data) {
    int[][] result = Arrays.copyOf(data, data.length);
    for (int i = 0; i < data[0].length; i++) {
      int[] column = new int[data.length];
      for (int j = 0; j < data.length; j++) {
        column[j] = result[j][i];
      }
      inverseHaarTransform1D(column);
      for (int j = 0; j < data.length; j++) {
        result[j][i] = column[j];
      }
    }
    for (int j = 0; j < data.length; j++) {
      inverseHaarTransform1D(result[j]);
    }
    return result;
  }

  private void inverseHaarTransform1D(int[] data) {
    if (data.length <= 1) {
      return;
    }
    int[] temp = new int[data.length];
    int h = data.length >> 1;
    for (int i = 0; i < h; i++) {
      int k = i << 1;
      int a = data[i];
      int b = data[i + h];
      temp[k] = a + (b >> 1);
      temp[k + 1] = a - (b >> 1);
    }
    System.arraycopy(temp, 0, data, 0, data.length);
  }

  private int nextPowerOfTwo(int n) {
    int power = 1;
    while (power < n) {
      power <<= 1;
    }
    return power;
  }

  private ImageRepresentation padImage(Image image, int newHeight, int newWidth) {
    int originalHeight = image.getHeight();
    int originalWidth = image.getWidth();
    ImageRepresentation paddedImage = new Image(newHeight, newWidth);

    for (int j = 0; j < newHeight; j++) {
      for (int i = 0; i < newWidth; i++) {
        if (j < originalHeight && i < originalWidth) {
          paddedImage.setPixel(j, i, image.getPixel(i, j));
        } else {
          paddedImage.setPixel(j, i, new Pixel(0, 0, 0));
        }
      }
    }
    return paddedImage;
  }

  private ImageRepresentation unpadImage(Image image, int originalHeight, int originalWidth) {
    ImageRepresentation unpaddedImage = new Image(originalHeight, originalWidth);
    for (int j = 0; j < originalHeight; j++) {
      for (int i = 0; i < originalWidth; i++) {
        unpaddedImage.setPixel(j, i, image.getPixel(j, i));
      }
    }
    return unpaddedImage;
  }
}