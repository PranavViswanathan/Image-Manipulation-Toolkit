package com.vanarp.controller;

import com.vanarp.model.Image;
import com.vanarp.model.ImageRepresentation;
import com.vanarp.model.Pixel;
import com.vanarp.model.PixelInterface;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The CompressedImageIO class provides methods to load and save compressed image files using the
 * {@link BufferedImage} class. It implements the {@link ImageFileIO} interface for reading and
 * writing images in compressed formats like JPEG and PNG.
 */
public class CompressedImageIO implements ImageFileIO {

  /**
   * Loads an image from the specified file path into a custom {@link Image} object. The image is
   * read using Java's {@link ImageIO} class and then converted to a custom image format with pixels
   * mapped to a {@link Pixel} object.
   *
   * @param filePath the path of the file to load
   * @return an {@link Image} object containing the loaded image data
   * @throws IOException if an error occurs during reading the file
   */
  @Override
  public ImageRepresentation loadImage(String filePath) throws IOException {
    BufferedImage bufferedImage = ImageIO.read(new File(filePath));
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    Image image = new Image(width, height);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb = bufferedImage.getRGB(x, y);
        Pixel pixel = new Pixel(rgb);
        image.setPixel(x, y, pixel);
      }
    }
    return image;
  }

  /**
   * Saves the given {@link Image} object to the specified file path in the provided format. The
   * image is converted from the custom image format to a {@link BufferedImage}, and saved using the
   * {@link ImageIO} class.
   *
   * @param image    the {@link Image} to save
   * @param filePath the destination file path
   * @param format   the format of the image to save (e.g., "jpg", "png")
   * @throws IOException if an error occurs during writing the file
   */
  @Override
  public void saveImage(ImageRepresentation image, String filePath, String format)
      throws IOException {
    BufferedImage bufferedImage =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        PixelInterface pixel = image.getPixel(x, y);
        int rgb = (pixel.getRed() << 16) | (pixel.getGreen() << 8) | pixel.getBlue();
        bufferedImage.setRGB(x, y, rgb);
      }
    }
    ImageIO.write(bufferedImage, format, new File(filePath));
  }
}
