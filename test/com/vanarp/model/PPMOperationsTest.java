package com.vanarp.model;

import org.junit.Before;
import com.vanarp.controller.CompressedImageIO;
import com.vanarp.controller.ImageFileIO;
import com.vanarp.controller.UncompressedImageIO;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Class to test ppm operations.
 */
public class PPMOperationsTest {

  private ImageRepresentation testImagePpm;
  private ImageOperations operations;
  private final String validPpmPath = "test/com/vanarp/model/TestResources/Source/P3.ppm";
  private final String outputDirectory = "test/com/vanarp/model/TestResources/SampleOperations/";

  @Before
  public void setUp() throws IOException {
    ImageTransformationEnhanced transform = new Transform();
    FilteringOperationEnhanced filter = new Filtering();
    ImageFileIO compressedIO = new CompressedImageIO();
    ImageFileIO uncompressedIO = new UncompressedImageIO();
    ImageCompressionFunctionality compress = new ImageCompression();

    operations = new Operations(transform, filter, compressedIO, uncompressedIO, compress);
    testImagePpm = operations.loadImage(validPpmPath);
  }

  private ImageRepresentation createImageWithColor(int red, int green, int blue) {
    ImageRepresentation image = new Image(1, 1);
    image.setPixel(0, 0, new Pixel(red, green, blue));
    return image;
  }

  private ImageRepresentation createImageWithSize(int width, int height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Image dimensions must be positive.");
    }
    return new Image(width, height);
  }

  // Test cases for PPM

  @Test(expected = IOException.class)
  public void testLoadNonExistentFile() throws IOException {
    String nonExistentFilePath = "test/com/vanarp/model/TestResources/Source/nonexistent.ppm";
    operations.loadImage(nonExistentFilePath);
  }

  @Test(expected = IOException.class)
  public void testLoadInvalidFileFormat() throws IOException {
    String invalidFilePath = "test/com/vanarp/model/TestResources/Invalid/bird.ppm";
    operations.loadImage(invalidFilePath);
  }

  @Test
  public void testLoadValidPpmFile() throws IOException {
    ImageRepresentation image = operations.loadImage(validPpmPath);
    assertEquals(3, image.getWidth());
    assertEquals(2, image.getHeight());
  }

  @Test(expected = IOException.class)
  public void testLoadEmptyDirectory() throws IOException {
    String emptyDirectory = "";
    operations.loadImage(emptyDirectory);
  }

  @Test(expected = IOException.class)
  public void testSaveToInvalidPath() throws IOException {
    ImageRepresentation testImage = operations.loadImage(validPpmPath);
    String invalidOutputPath = "/invalid/directory/output.ppm";
    operations.saveImage(testImage, invalidOutputPath, "ppm");
  }

  @Test(expected = IOException.class)
  public void testSaveWithInvalidExtension() throws IOException {
    ImageRepresentation testImage = operations.loadImage(validPpmPath);
    operations.saveImage(testImage, outputDirectory + "output.abc", "abc");
  }


  @Test
  public void testRedComponentForPpm() throws IOException {
    ImageRepresentation redImage = operations.redComponent(testImagePpm);
    ImageRepresentation expectedRedImage = operations.loadImage(
        outputDirectory + "redComponent.ppm");

    assertEquals(expectedRedImage, redImage);
  }

  @Test
  public void testGreenComponentForPpm() throws IOException {
    ImageRepresentation greenImage = operations.greenComponent(testImagePpm);
    ImageRepresentation expectedGreenImage = operations.loadImage(
        outputDirectory + "greenComponent.ppm");

    assertEquals(expectedGreenImage, greenImage);
  }

  @Test
  public void testBlueComponentForPpm() throws IOException {
    ImageRepresentation blueImage = operations.blueComponent(testImagePpm);
    ImageRepresentation expectedBlueImage = operations.loadImage(
        outputDirectory + "blueComponent.ppm");

    assertEquals(expectedBlueImage, blueImage);
  }

  @Test
  public void testValueComponentForPpm() throws IOException {
    ImageRepresentation valueImage = operations.valueComponent(testImagePpm);
    ImageRepresentation expectedValueImage = operations.loadImage(
        outputDirectory + "valueComponent.ppm");

    assertEquals(expectedValueImage, valueImage);
  }

  @Test
  public void testLumaComponentForPpm() throws IOException {
    ImageRepresentation lumaImage = operations.lumaComponent(testImagePpm);
    ImageRepresentation expectedLumaImage = operations.loadImage(
        outputDirectory + "lumaComponent.ppm");

    assertEquals(expectedLumaImage, lumaImage);
  }

  @Test
  public void testIntensityComponentForPpm() throws IOException {
    ImageRepresentation intensityImage = operations.intensityComponent(testImagePpm);
    ImageRepresentation expectedIntensityImage = operations.loadImage(outputDirectory
        + "intensityComponent.ppm");

    assertEquals(expectedIntensityImage, intensityImage);
  }

  @Test
  public void testFlipHorizontallyForPpm() throws IOException {
    ImageRepresentation flippedImage = operations.flipHorizontally(testImagePpm);
    ImageRepresentation expectedFlippedImage = operations.loadImage(outputDirectory
        + "flippedHorizontally.ppm");

    assertEquals(expectedFlippedImage, flippedImage);
  }

  @Test
  public void testFlipVerticallyForPpm() throws IOException {
    ImageRepresentation flippedImage = operations.flipVertically(testImagePpm);
    ImageRepresentation expectedFlippedImage = operations.loadImage(outputDirectory
        + "flippedVertically.ppm");

    assertEquals(expectedFlippedImage, flippedImage);
  }

  @Test
  public void testBrightenImageForPpm() throws IOException {
    ImageRepresentation brightenedImage = operations.brightenImage(testImagePpm, 10);
    ImageRepresentation expectedBrightenedImage = operations.loadImage(outputDirectory
        + "brightenedImage.ppm");

    assertEquals(expectedBrightenedImage, brightenedImage);
  }

  @Test
  public void testApplySepiaForPpm() throws IOException {
    ImageRepresentation sepiaImage = operations.applySepia(testImagePpm);
    ImageRepresentation expectedSepiaImage = operations.loadImage(outputDirectory
        + "sepiaImage.ppm");

    assertEquals(expectedSepiaImage, sepiaImage);
  }

  @Test
  public void testBlurForPpm() throws IOException {
    ImageRepresentation blurredImage = operations.blur(testImagePpm);
    ImageRepresentation expectedBlurredImage = operations.loadImage(outputDirectory
        + "blurredImage.ppm");

    assertEquals(expectedBlurredImage, blurredImage);
  }

  @Test
  public void testSharpenForPpm() throws IOException {
    ImageRepresentation sharpenedImage = operations.sharpen(testImagePpm);
    ImageRepresentation expectedSharpenedImage = operations.loadImage(outputDirectory
        + "sharpenedImage.ppm");

    assertEquals(expectedSharpenedImage, sharpenedImage);
  }

  @Test
  public void testApplyGreyScaleForPpm() throws IOException {
    ImageRepresentation greyScaleImage = operations.applyGreyScale(testImagePpm);
    ImageRepresentation expectedGreyScaleImage = operations.loadImage(outputDirectory
        + "greyScaleImage.ppm");

    assertEquals(expectedGreyScaleImage, greyScaleImage);
  }

  @Test
  public void testRgbCombineForPpm() throws IOException {
    ImageRepresentation redImage = operations.loadImage(outputDirectory
        + "redComponent.ppm");
    ImageRepresentation greenImage = operations.loadImage(outputDirectory
        + "greenComponent.ppm");
    ImageRepresentation blueImage = operations.loadImage(outputDirectory
        + "blueComponent.ppm");

    ImageRepresentation combinedImage = operations.combineRgb(redImage, greenImage, blueImage);

    assertEquals(testImagePpm, combinedImage);
  }

  @Test
  public void testSplitRgbForPpm() throws IOException {
    ImageRepresentation[] splitImages = operations.rgbSplit(testImagePpm);

    ImageRepresentation expectedRedImage = operations.loadImage(
        outputDirectory + "redComponent.ppm");
    ImageRepresentation expectedGreenImage = operations.loadImage(
        outputDirectory + "greenComponent.ppm");
    ImageRepresentation expectedBlueImage = operations.loadImage(
        outputDirectory + "blueComponent.ppm");
    assertEquals(expectedRedImage, splitImages[0]);
    assertEquals(expectedGreenImage, splitImages[1]);
    assertEquals(expectedBlueImage, splitImages[2]);
  }

  @Test
  public void testMultipleHorizontalFlips() throws IOException {
    ImageRepresentation originalImage = operations.loadImage(validPpmPath);
    ImageRepresentation flippedImage = operations.flipHorizontally(originalImage);
    ImageRepresentation doubleFlippedImage = operations.flipHorizontally(flippedImage);

    assertEquals(originalImage, doubleFlippedImage);
  }

  @Test
  public void testMultipleVerticalFlips() throws IOException {
    ImageRepresentation originalImage = operations.loadImage(validPpmPath);
    ImageRepresentation flippedImage = operations.flipVertically(originalImage);
    ImageRepresentation doubleFlippedImage = operations.flipVertically(flippedImage);

    assertEquals(originalImage, doubleFlippedImage);
  }

  @Test
  public void testFlippingAlreadyFlippedImageHorizontally() throws IOException {
    ImageRepresentation originalImage = operations.loadImage(validPpmPath);
    ImageRepresentation flippedImage = operations.flipHorizontally(originalImage);
    ImageRepresentation reFlippedImage = operations.flipHorizontally(flippedImage);

    assertEquals(originalImage, reFlippedImage);
  }

  @Test
  public void testFlippingAlreadyFlippedImageVertically() throws IOException {
    ImageRepresentation originalImage = operations.loadImage(validPpmPath);
    ImageRepresentation flippedImage = operations.flipVertically(originalImage);
    ImageRepresentation reFlippedImage = operations.flipVertically(flippedImage);

    assertEquals(originalImage, reFlippedImage);
  }

  @Test
  public void testRgbSplitAndCombine() throws IOException {
    ImageRepresentation originalImage = operations.loadImage(validPpmPath);
    ImageRepresentation[] splitImages = operations.rgbSplit(originalImage);
    ImageRepresentation combinedImage = operations.combineRgb(splitImages[0], splitImages[1],
        splitImages[2]);
    assertEquals(originalImage, combinedImage);
  }

  @Test
  public void testSavePpmAsPpmAndLoadBack() throws IOException {
    ImageRepresentation originalImage = operations.loadImage(validPpmPath);
    String ppmOutputPath = outputDirectory + "outputImage.ppm";
    operations.saveImage(originalImage, ppmOutputPath, "ppm");
    ImageRepresentation loadedPpmImage = operations.loadImage(ppmOutputPath);
    assertEquals(originalImage, loadedPpmImage);
  }
}
