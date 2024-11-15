package com.vanarp.model;

import java.util.Objects;

/**
 * This class represents a single pixel in an image, which shows its red, green, and blue color
 * components.
 * <p>
 * A pixel is defined by its RGB values, each in the range of 0-255. This class provides methods to
 * access and modify the individual color components (red, green, and blue). It can be used to
 * manipulate the pixel data of an image for tasks such as image processing, color transformations,
 * and filtering.
 * </p>
 *
 * @author Pranav Viswanathan, Saran
 */
public class Pixel implements PixelInterface {

  private final int red;
  private final int green;
  private final int blue;

  /**
   * This constructor is used to represent single Pixel. The clamp function ensures that the values
   * are always between a 0 to 255 range.
   *
   * @param red   the red value of the pixel.
   * @param green the green value of the pixel.
   * @param blue  the blue value of the pixel.
   */
  public Pixel(int red, int green, int blue) {
    this.red = clamp(red);
    this.green = clamp(green);
    this.blue = clamp(blue);
  }

  /**
   * This constructor is used to represent single Pixel. It takes a rgb parameter.
   *
   * @param rgb combined rbg value.
   */
  public Pixel(int rgb) {
    this.red = clamp((rgb >> 16) & 0xFF);
    this.green = clamp((rgb >> 8) & 0xFF);
    this.blue = clamp(rgb & 0xFF);
  }

  /**
   * A method to get the pixels red component.
   *
   * @return the red value.
   */
  public int getRed() {
    return red;
  }

  /**
   * A method to get the pixels green component.
   *
   * @return the red value.
   */
  public int getGreen() {
    return green;
  }

  /**
   * A method to get the pixels blue component.
   *
   * @return the red value.
   */
  public int getBlue() {
    return blue;
  }


  private int clamp(int value) {
    return Math.max(0, Math.min(255, value));
  }

  /**
   * Return a string representation of the image.
   *
   * @return a formated string
   */
  @Override
  public String toString() {
    return "Pixel [R=" + red + ", G=" + green + ", B=" + blue + "]";
  }


  @Override
  public int hashCode() {
    return Objects.hash(red, green, blue);
  }

  /**
   * A methiod that checks if two Pixel Objects are equal to each other.
   *
   * @param obj the object with which we need to check equality
   * @return a boolean value
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Pixel other = (Pixel) obj;
    return this.getRed() == other.getRed()
            && this.getGreen() == other.getGreen()
            && this.getBlue() == other.getBlue();
  }

}
