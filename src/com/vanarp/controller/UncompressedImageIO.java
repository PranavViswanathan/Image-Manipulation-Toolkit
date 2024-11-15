package com.vanarp.controller;

import com.vanarp.model.Image;
import com.vanarp.model.ImageRepresentation;
import com.vanarp.model.Pixel;
import com.vanarp.model.PixelInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * The UncompressedImageIO class provides methods to read and write images
 * in the uncompressed PPM (Portable Pixmap) format.
 * It implements the {@link ImageFileIO} interface.
 */
public class UncompressedImageIO implements ImageFileIO {

  /**
   * Loads an image from the specified PPM file.
   *
   * @param filePath the path to the PPM file to load
   * @return a new {@link Image} object containing the pixel data
   * @throws IOException if an error occurs while reading the file or if the format is invalid
   */
  @Override
  public Image loadImage(String filePath) throws IOException {
    Pixel[][] pixels = load(filePath);
    if (pixels == null) {
      throw new IOException("Failed to load image from " + filePath);
    }

    int width = pixels[0].length;
    int height = pixels.length;
    Image image = new Image(width, height);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        image.setPixel(x, y, pixels[y][x]);
      }
    }
    return image;
  }

  /**
   * Loads pixel data from the specified PPM file.
   *
   * @param fileName the name of the PPM file
   * @return a 2D array of RGBPixel objects representing the image pixels
   */
  public Pixel[][] load(String fileName) {
    Scanner scanner = null;
    try {
      File file = new File(fileName);
      scanner = new Scanner(file);

      String format = scanner.next();
      if (!format.equals("P3")) {
        System.out.println("Not a P3 PPM file.");
        return null;
      }

      while (scanner.hasNext()) {
        String line = scanner.nextLine().trim();
        if (line.isEmpty() || line.startsWith("#")) {
          continue;
        }

        String[] dimensions = line.split("\\s+");
        if (dimensions.length < 2) {
          System.out.println("Invalid PPM file format.");
          return null;
        }
        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);

        if (!scanner.hasNextInt()) {
          System.out.println("Invalid max color value.");
          return null;
        }
        int maxColorValue = scanner.nextInt();
        if (maxColorValue != 255) {
          System.out.println("Unsupported max color value: " + maxColorValue);
          return null;
        }

        Pixel[][] pixels = new Pixel[height][width];

        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            while (scanner.hasNext("#")) {
              scanner.nextLine();
            }

            int red = scanner.nextInt();
            int green = scanner.nextInt();
            int blue = scanner.nextInt();

            pixels[y][x] = new Pixel(red, green, blue);
          }
        }

        return pixels;
      }

    } catch (FileNotFoundException e) {
      System.out.println("File not found: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Error processing the PPM file: " + e.getMessage());
    } finally {
      if (scanner != null) {
        scanner.close();
      }
    }
    return null;
  }

  /**
   * Saves the specified image to a PPM file in the P3 format.
   *
   * @param image    the {@link Image} to save
   * @param filePath the path where the image will be saved
   * @param format   the format of the image, expected to be "ppm"
   * @throws IOException if an error occurs while writing to the file
   */
  @Override
  public void saveImage(ImageRepresentation image, String filePath, String format)
          throws IOException {
    if (!format.equalsIgnoreCase("ppm")) {
      throw new IOException("Unsupported format: " + format);
    }

    if (image == null || image.getWidth() == 0 || image.getHeight() == 0) {
      System.out.println("Invalid image data.");
      return;
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
      bw.write("P3");
      bw.newLine();
      bw.write(image.getWidth() + " " + image.getHeight());
      bw.newLine();
      bw.write("255");
      bw.newLine();

      for (int y = 0; y < image.getHeight(); y++) {
        for (int x = 0; x < image.getWidth(); x++) {
          PixelInterface pixel = image.getPixel(x, y);
          bw.write(pixel.getRed() + " " + pixel.getGreen() + " " + pixel.getBlue() + " ");
        }
        bw.newLine();
      }
      System.out.println("Image saved successfully to " + filePath);
    } catch (IOException e) {
      System.out.println("Error saving the PPM file: " + e.getMessage());
      throw e; // Re-throw to indicate failure
    }
  }
}