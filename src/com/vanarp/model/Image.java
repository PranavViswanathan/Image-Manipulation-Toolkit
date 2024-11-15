package com.vanarp.model;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Represents an image consisting of a grid of pixels. Implements the ImageRepresentation interface
 * to provide methods for manipulating and accessing pixel data.
 */
public class Image implements ImageRepresentation {

  private final int width;
  private final int height;
  private final PixelInterface[][] pixels;

  /**
   * Constructs an Image object with the specified width and height.
   *
   * @param width  the width of the image
   * @param height the height of the image
   */
  public Image(int width, int height) {
    this.width = width;
    this.height = height;
    this.pixels = new PixelInterface[height][width];
  }

  /**
   * Constructs an Image object from a BufferedImage.
   *
   * @param bufferedImage the BufferedImage to convert into an Image
   */
  public Image(BufferedImage bufferedImage) {
    this.width = bufferedImage.getWidth();
    this.height = bufferedImage.getHeight();
    this.pixels = new PixelInterface[height][width];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb = bufferedImage.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        this.pixels[y][x] = new Pixel(red, green, blue);
      }
    }
  }

  /**
   * Sets a pixel at the specified coordinates.
   *
   * @param x     the x-coordinate of the pixel
   * @param y     the y-coordinate of the pixel
   * @param pixel the pixel to set at the specified coordinates
   * @throws IndexOutOfBoundsException if the specified coordinates are out of bounds
   */
  @Override
  public void setPixel(int x, int y, PixelInterface pixel) {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      throw new IndexOutOfBoundsException("Pixel coordinates are out of bounds.");
    }
    pixels[y][x] = pixel;
  }

  /**
   * Retrieves the pixel at the specified coordinates.
   *
   * @param x the x-coordinate of the pixel to retrieve
   * @param y the y-coordinate of the pixel to retrieve
   * @return the pixel at the specified coordinates
   * @throws IllegalArgumentException if the specified coordinates are out of bounds
   */
  @Override
  public PixelInterface getPixel(int x, int y) {
    if (x >= 0 && x < width && y >= 0 && y < height) {
      PixelInterface originalPixel = pixels[y][x];
      return new Pixel(originalPixel.getRed(), originalPixel.getGreen(), originalPixel.getBlue());
    } else {
      throw new IllegalArgumentException("Pixel coordinates out of bounds");
    }
  }

  /**
   * Returns the width of the image.
   *
   * @return the width of the image
   */
  @Override
  public int getWidth() {
    return width;
  }

  /**
   * Returns the height of the image.
   *
   * @return the height of the image
   */
  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public BufferedImage toBufferedImage() {
    // Create a new BufferedImage with the same dimensions as this image
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    // Loop through each pixel and set its RGB value in the BufferedImage
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        PixelInterface pixel = pixels[y][x];
        if (pixel != null) {
          // Get the RGB values from the pixel
          int red = pixel.getRed();
          int green = pixel.getGreen();
          int blue = pixel.getBlue();

          // Combine the RGB values into a single integer
          int rgb = (red << 16) | (green << 8) | blue; // Shift bits to create an RGB value

          // Set the pixel in the BufferedImage
          bufferedImage.setRGB(x, y, rgb);
        }
      }
    }

    return bufferedImage; // Return the constructed BufferedImage
  }

  /**
   * Returns a string representation of the image, including its dimensions and pixel data.
   *
   * @return a string representation of the image
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Image [Width=").append(width).append(", Height=").append(height).append("]\n");
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        sb.append(pixels[y][x] != null ? pixels[y][x].toString() : "null").append(" ");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  /**
   * Compares this image to the specified object for equality.
   *
   * @param obj the object to compare this image against
   * @return true if the specified object is equal to this image, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Image)) {
      return false;
    }
    Image other = (Image) obj;
    if (width != other.width || height != other.height) {
      return false;
    }
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        PixelInterface thisPixel = this.getPixel(x, y);
        PixelInterface otherPixel = other.getPixel(x, y);
        if (!thisPixel.equals(otherPixel)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Returns a hash code value for this image.
   *
   * @return a hash code value for this image
   */
  @Override
  public int hashCode() {
    return Objects.hash(width, height);
  }
}