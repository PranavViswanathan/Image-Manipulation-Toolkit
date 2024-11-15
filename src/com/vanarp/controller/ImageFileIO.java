package com.vanarp.controller;

import com.vanarp.model.ImageRepresentation;

import java.io.IOException;

/**
 * This interface provides a list of functionalites for image IO.
 */
public interface ImageFileIO {
  /**
   * Loads an image from a specified file path.
   *
   * @param filePath the path of the image file to load.
   * @return the loaded Image object.
   * @throws IOException if an error occurs while loading the image.
   */
  ImageRepresentation loadImage(String filePath) throws IOException;

  /**
   * Saves an Image object to a file in the specified format.
   *
   * @param image    the Image object to save.
   * @param filePath the destination path for saving the image.
   * @param format   the format to save the image (e.g., "PNG").
   * @throws IOException if an error occurs during saving.
   */
  void saveImage(ImageRepresentation image, String filePath, String format) throws IOException;

}
