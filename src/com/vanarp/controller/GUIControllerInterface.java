package com.vanarp.controller;

public interface GUIControllerInterface {

  /**
   * Loads an image from a file.
   */
  void loadImage();

  /**
   * Saves the currently loaded image to a specified file.
   */
  void saveImage();

  /**
   * Undoes the last action performed on the image.
   */
  void undo();

  /**
   * Reverts the currently loaded image to its original state.
   */
  void revertToOriginal();

  /**
   * Extracts a specific color component from the currently loaded image.
   *
   * @param componentType The type of color component to extract (e.g., "red", "green", "blue",
   *                      etc.)
   */
  void extractComponent(String componentType);

  /**
   * Applies a specified filter to the currently loaded image.
   *
   * @param filterType The type of filter to apply (e.g., "blur", "sharpen", etc.)
   */
  void applyFilter(String filterType);

  /**
   * Flips the currently loaded image in a specified direction.
   *
   * @param direction The direction to flip the image (e.g., "horizontal", "vertical").
   */
  void flipImage(String direction);

  /**
   * Brightens the currently loaded image by a specified increment.
   */
  void brightenImage();

  /**
   * Color corrects the currently loaded image.
   */
  void colorCorrectImage();

  /**
   * Adjusts the levels of the currently loaded image.
   */
  void adjustLevels();

  /**
   * Downscales the currently loaded image to a specified width and height.
   */
  void downscaleImage();

  /**
   * Compresses the currently loaded image by a specified percentage.
   *
   * @param percentage The percentage to compress the image (0-100).
   */
  void compressImage(int percentage);

  /**
   * Generates a histogram for the currently loaded image.
   */
  void generateHistogram();
}