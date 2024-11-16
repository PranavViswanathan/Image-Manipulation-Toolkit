package com.vanarp.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The {@code Transform} class provides methods to apply various transformations to images, such as
 * flipping, brightness adjustment, blurring, and sharpening. It extends the abstract class
 * {@link AbstractImageTransformation}.
 */
public class Transform extends AbstractImageTransformation {

  /**
   * Flips the given image horizontally.
   *
   * @param image the input {@link ImageRepresentation} to flip
   * @return a new {@link ImageRepresentation} flipped horizontally
   * @throws IOException if an error occurs during the image processing
   */
  @Override
  public ImageRepresentation flipHorizontally(ImageRepresentation image) throws IOException {
    validateImage(image);
    return applyTransformation(image, (x, y) -> image.getPixel(image.getWidth() - 1 - x, y));
  }

  /**
   * Flips the given image vertically.
   *
   * @param image the input {@link ImageRepresentation} to flip
   * @return a new {@link ImageRepresentation} flipped vertically
   * @throws IOException if an error occurs during the image processing
   */
  @Override
  public ImageRepresentation flipVertically(ImageRepresentation image) throws IOException {
    validateImage(image);
    return applyTransformation(image, (x, y) -> image.getPixel(x, image.getHeight() - 1 - y));
  }

  /**
   * Adjusts the brightness of the given image by a specified increment.
   *
   * @param image     the input {@link ImageRepresentation} to adjust
   * @param increment the amount to adjust the brightness (can be negative)
   * @return a new {@link ImageRepresentation} with adjusted brightness
   * @throws IOException if an error occurs during the image processing
   */
  @Override
  public ImageRepresentation adjustBrightness(ImageRepresentation image, int increment)
      throws IOException {
    validateImage(image);
    return applyTransformation(image, (x, y) -> {
      PixelInterface originalPixel = image.getPixel(x, y);
      return createBoundedPixel(
          originalPixel.getRed() + increment,
          originalPixel.getGreen() + increment,
          originalPixel.getBlue() + increment
      );
    });
  }

  /**
   * Applies a blur effect to the given image using a Gaussian kernel.
   *
   * @param original the input {@link ImageRepresentation} to blur
   * @return a new {@link ImageRepresentation} with the blur effect applied
   * @throws IOException if an error occurs during the image processing
   */
  public ImageRepresentation blur(ImageRepresentation original) throws IOException {
    if (original.getWidth() <= 0 || original.getHeight() <= 0) {
      throw new IllegalArgumentException("Image dimensions must be greater than zero.");
    }
    double[][] kernel = {
        {1.0 / 16, 1.0 / 8, 1.0 / 16},
        {1.0 / 8, 1.0 / 4, 1.0 / 8},
        {1.0 / 16, 1.0 / 8, 1.0 / 16}
    };
    return applyKernel(original, kernel);
  }

  /**
   * Applies a sharpen effect to the given image using a sharpening kernel.
   *
   * @param original the input {@link ImageRepresentation} to sharpen
   * @return a new {@link ImageRepresentation} with the sharpen effect applied
   * @throws IOException if an error occurs during the image processing
   */
  public ImageRepresentation sharpen(ImageRepresentation original) throws IOException {
    double[][] kernel = {
        {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8},
        {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
        {-1.0 / 8, 1.0 / 4, 1.0, 1.0 / 4, -1.0 / 8},
        {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
        {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8}
    };
    return applyKernel(original, kernel);
  }

  /**
   * Draws a grid on the provided Graphics2D context.
   *
   * @param g2d the Graphics2D object to draw on
   */
  private void drawGrid(Graphics2D g2d) {
    g2d.setColor(new Color(220, 220, 220));
    g2d.setStroke(new BasicStroke(1.0f));
    for (int x = 0; x < 256; x += 16) {
      g2d.drawLine(x, 0, x, 255);
    }
    for (int y = 0; y < 256; y += 16) {
      g2d.drawLine(0, y, 255, y);
    }
  }

  /**
   * Draws the histogram for a specific color channel.
   *
   * @param g         the Graphics object to draw on
   * @param histogram the histogram data for the color channel
   * @param color     the color to use for the histogram line
   * @param maxHeight the maximum height for scaling the histogram
   */
  private static void drawHistogram(Graphics g, int[] histogram, Color color, int maxHeight) {
    for (int i = 0; i < histogram.length - 1; i++) {
      int y1 = (int) (((double) histogram[i] / (double) maxHeight) * (double) 256);
      int x2 = i + 1;
      int y2 = (int) (((double) histogram[i + 1] / (double) maxHeight) * (double) 256);

      g.setColor(color);
      g.drawLine(i, 256 - y1, x2, 256 - y2);
    }
  }

  @Override
  public ImageRepresentation getHistogram(ImageRepresentation image) {
    int histogramWidth = 256;
    int histogramHeight = 256;
    int[] redHistogram = new int[256];
    int[] greenHistogram = new int[256];
    int[] blueHistogram = new int[256];
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        PixelInterface pixel = image.getPixel(x, y);
        redHistogram[pixel.getRed()]++;
        greenHistogram[pixel.getGreen()]++;
        blueHistogram[pixel.getBlue()]++;
      }
    }
    int maxFrequency = 0;
    for (int i = 0; i < 256; i++) {
      maxFrequency = Math.max(maxFrequency, redHistogram[i]);
      maxFrequency = Math.max(maxFrequency, greenHistogram[i]);
      maxFrequency = Math.max(maxFrequency, blueHistogram[i]);
    }
    BufferedImage histogramImage = new BufferedImage(histogramWidth, histogramHeight,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = histogramImage.createGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, histogramWidth, histogramHeight);
    drawGrid(graphics);
    drawHistogram(graphics, redHistogram, Color.RED, maxFrequency);
    drawHistogram(graphics, greenHistogram, Color.GREEN, maxFrequency);
    drawHistogram(graphics, blueHistogram, Color.BLUE, maxFrequency);

    graphics.dispose();
    return new Image(histogramImage);
  }

  @Override
  public ImageRepresentation getSplitView(ImageRepresentation image1,
      ImageRepresentation image2, int percent)
      throws IOException {
    validateImage(image1);
    validateImage(image2);
    if (percent < 0 || percent > 100) {
      throw new IllegalArgumentException("Percentage must be between 0 and 100.");
    }
    int width1 = (int) (image1.getWidth() * (percent / 100.0));
    int width2 = image2.getWidth() - width1;
    int height = Math.max(image1.getHeight(), image2.getHeight());
    BufferedImage splitImage = new BufferedImage(image1.getWidth(), height,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = splitImage.createGraphics();
    ImageRepresentation transformedImage =
        applyTransformation(image1, (x, y) -> image1.getPixel(x, y));
    for (int x = 0; x < width1; x++) {
      for (int y = 0; y < height; y++) {
        if (x < transformedImage.getWidth() && y < transformedImage.getHeight()) {
          PixelInterface pixel = transformedImage.getPixel(x, y);
          g2d.setColor(new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue()));
          g2d.drawLine(x, y, x, y);
        }
      }
    }
    for (int x = 0; x < width2; x++) {
      for (int y = 0; y < height; y++) {
        if (x < image2.getWidth() && y < image2.getHeight()) {
          PixelInterface pixel = image2.getPixel(x + width1, y);
          g2d.setColor(new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue()));
          g2d.drawLine(x + width1, y, x + width1, y);
        }
      }
    }
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(2.0f));
    g2d.drawLine(width1, 0, width1, height);

    g2d.dispose();
    return new Image(splitImage);
  }

  /**
   * Downscales the given image to the specified width and height.
   *
   * @param original  the input {@link ImageRepresentation} to downscale
   * @param newWidth  the desired width of the downscaled image
   * @param newHeight the desired height of the downscaled image
   * @return a new {@link ImageRepresentation} that is downscaled
   * @throws IOException if an error occurs during the image processing
   */
  @Override
  public ImageRepresentation downscale(ImageRepresentation original, int newWidth, int newHeight)
      throws IOException {
    if (original.getWidth() <= 0 || original.getHeight() <= 0) {
      throw new IllegalArgumentException("Image dimensions must be greater than zero.");
    }
    if (newWidth <= 0 || newHeight <= 0) {
      throw new IllegalArgumentException("New dimensions must be greater than zero.");
    }

    // Create a new BufferedImage for the downscaled image
    BufferedImage downscaledImage = new BufferedImage(newWidth, newHeight,
        BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < newHeight; y++) {
      for (int x = 0; x < newWidth; x++) {
        // Calculate the corresponding pixel in the original image
        int origX = (int) Math.round((double) x * original.getWidth() / newWidth);
        int origY = (int) Math.round((double) y * original.getHeight() / newHeight);

        // Ensure we do not go out of bounds
        origX = Math.min(origX, original.getWidth() - 1);
        origY = Math.min(origY, original.getHeight() - 1);

        // Get the pixel color from the original image
        PixelInterface pixel = original.getPixel(origX, origY);
        downscaledImage.setRGB(x, y,
            new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue()).getRGB());
      }
    }

    return new Image(downscaledImage);
  }
}