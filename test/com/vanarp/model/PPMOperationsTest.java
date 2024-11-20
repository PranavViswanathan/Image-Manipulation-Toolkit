package com.vanarp.model;

import static org.junit.Assert.assertEquals;

import com.vanarp.controller.UncompressedImageIO;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test ppm operations.
 */
public class PPMOperationsTest {

  private UncompressedImageIO uncompressedImageIO;
  private ImageRepresentation testImagePpm;
  private ImageOperations operations;
  private final String validPpmPath = "test/com/vanarp/model/TestResources/Source/P3.ppm";
  private final String outputDirectory = "test/com/vanarp/model/TestResources/SampleOperations/";

  @Before
  public void setUp() throws IOException {
    ImageTransformationEnhanced transform = new Transform();
    ImageMaskingFiltering filter = new Filtering();
    ImageCompressionFunctionality compress = new ImageCompression();
    uncompressedImageIO = new UncompressedImageIO();
    operations = new Operations(transform, filter, compress);
    testImagePpm = uncompressedImageIO.loadImage(validPpmPath);
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

  @Test(expected = IOException.class)
  public void testLoadNonExistentFile() throws IOException {
    String nonExistentFilePath = "test/com/vanarp/model/TestResources/Source/nonexistent.ppm";
    uncompressedImageIO.loadImage(nonExistentFilePath);
  }

  @Test(expected = IOException.class)
  public void testLoadInvalidFileFormat() throws IOException {
    String invalidFilePath = "test/com/vanarp/model/TestResources/Invalid/bird.ppm";
    uncompressedImageIO.loadImage(invalidFilePath);
  }

  @Test
  public void testLoadValidPpmFile() throws IOException {
    ImageRepresentation image = uncompressedImageIO.loadImage(validPpmPath);
    assertEquals(3, image.getWidth());
    assertEquals(2, image.getHeight());
  }

  @Test(expected = IOException.class)
  public void testLoadEmptyDirectory() throws IOException {
    String emptyDirectory = "";
    uncompressedImageIO.loadImage(emptyDirectory);
  }

  @Test(expected = IOException.class)
  public void testSaveToInvalidPath() throws IOException {
    ImageRepresentation testImage = uncompressedImageIO.loadImage(validPpmPath);
    String invalidOutputPath = "/invalid/directory/output.ppm";
    uncompressedImageIO.saveImage(testImage, invalidOutputPath, "ppm");
  }

  @Test(expected = IOException.class)
  public void testSaveWithInvalidExtension() throws IOException {
    ImageRepresentation testImage = uncompressedImageIO.loadImage(validPpmPath);
    uncompressedImageIO.saveImage(testImage, outputDirectory + "output.abc", "abc");
  }


  @Test
  public void testRedComponentForPpm() throws IOException {
    ImageRepresentation redImage = operations.redComponent(testImagePpm);
    ImageRepresentation expectedRedImage = uncompressedImageIO.loadImage(
        outputDirectory + "redComponent.ppm");

    assertEquals(expectedRedImage, redImage);
  }

  @Test
  public void testGreenComponentForPpm() throws IOException {
    ImageRepresentation greenImage = operations.greenComponent(testImagePpm);
    ImageRepresentation expectedGreenImage = uncompressedImageIO.loadImage(
        outputDirectory + "greenComponent.ppm");

    assertEquals(expectedGreenImage, greenImage);
  }

  @Test
  public void testBlueComponentForPpm() throws IOException {
    ImageRepresentation blueImage = operations.blueComponent(testImagePpm);
    ImageRepresentation expectedBlueImage = uncompressedImageIO.loadImage(
        outputDirectory + "blueComponent.ppm");

    assertEquals(expectedBlueImage, blueImage);
  }

  @Test
  public void testValueComponentForPpm() throws IOException {
    ImageRepresentation valueImage = operations.valueComponent(testImagePpm);
    ImageRepresentation expectedValueImage = uncompressedImageIO.loadImage(
        outputDirectory + "valueComponent.ppm");

    assertEquals(expectedValueImage, valueImage);
  }

  @Test
  public void testLumaComponentForPpm() throws IOException {
    ImageRepresentation lumaImage = operations.lumaComponent(testImagePpm);
    ImageRepresentation expectedLumaImage = uncompressedImageIO.loadImage(
        outputDirectory + "lumaComponent.ppm");

    assertEquals(expectedLumaImage, lumaImage);
  }

  @Test
  public void testIntensityComponentForPpm() throws IOException {
    ImageRepresentation intensityImage = operations.intensityComponent(testImagePpm);
    ImageRepresentation expectedIntensityImage = uncompressedImageIO.loadImage(
        outputDirectory + "intensityComponent.ppm");

    assertEquals(expectedIntensityImage, intensityImage);
  }

  @Test
  public void testFlipHorizontallyForPpm() throws IOException {
    ImageRepresentation flippedImage = operations.flipHorizontally(testImagePpm);
    ImageRepresentation expectedFlippedImage = uncompressedImageIO.loadImage(
        outputDirectory + "flippedHorizontally.ppm");

    assertEquals(expectedFlippedImage, flippedImage);
  }

  @Test
  public void testFlipVerticallyForPpm() throws IOException {
    ImageRepresentation flippedImage = operations.flipVertically(testImagePpm);
    ImageRepresentation expectedFlippedImage = uncompressedImageIO.loadImage(
        outputDirectory + "flippedVertically.ppm");

    assertEquals(expectedFlippedImage, flippedImage);
  }

  @Test
  public void testBrightenImageForPpm() throws IOException {
    ImageRepresentation brightenedImage = operations.brightenImage(testImagePpm, 10);
    ImageRepresentation expectedBrightenedImage = uncompressedImageIO.loadImage(
        outputDirectory + "brightenedImage.ppm");

    assertEquals(expectedBrightenedImage, brightenedImage);
  }

  @Test
  public void testApplySepiaForPpm() throws IOException {
    ImageRepresentation sepiaImage = operations.applySepia(testImagePpm);
    ImageRepresentation expectedSepiaImage = uncompressedImageIO.loadImage(
        outputDirectory + "sepiaImage.ppm");

    assertEquals(expectedSepiaImage, sepiaImage);
  }

  @Test
  public void testBlurForPpm() throws IOException {
    ImageRepresentation blurredImage = operations.blur(testImagePpm);
    ImageRepresentation expectedBlurredImage = uncompressedImageIO.loadImage(
        outputDirectory + "blurredImage.ppm");

    assertEquals(expectedBlurredImage, blurredImage);
  }

  @Test
  public void testSharpenForPpm() throws IOException {
    ImageRepresentation sharpenedImage = operations.sharpen(testImagePpm);
    ImageRepresentation expectedSharpenedImage = uncompressedImageIO.loadImage(
        outputDirectory + "sharpenedImage.ppm");

    assertEquals(expectedSharpenedImage, sharpenedImage);
  }

  @Test
  public void testApplyGreyScaleForPpm() throws IOException {
    ImageRepresentation greyScaleImage = operations.applyGreyScale(testImagePpm);
    ImageRepresentation expectedGreyScaleImage = uncompressedImageIO.loadImage(
        outputDirectory + "greyScaleImage.ppm");

    assertEquals(expectedGreyScaleImage, greyScaleImage);
  }

  @Test
  public void testRgbCombineForPpm() throws IOException {
    ImageRepresentation redImage = uncompressedImageIO.loadImage(
        outputDirectory + "redComponent.ppm");
    ImageRepresentation greenImage = uncompressedImageIO.loadImage(
        outputDirectory + "greenComponent.ppm");
    ImageRepresentation blueImage = uncompressedImageIO.loadImage(
        outputDirectory + "blueComponent.ppm");

    ImageRepresentation combinedImage = operations.combineRgb(redImage, greenImage, blueImage);

    assertEquals(testImagePpm, combinedImage);
  }

  @Test
  public void testSplitRgbForPpm() throws IOException {
    ImageRepresentation[] splitImages = operations.rgbSplit(testImagePpm);

    ImageRepresentation expectedRedImage = uncompressedImageIO.loadImage(
        outputDirectory + "redComponent.ppm");
    ImageRepresentation expectedGreenImage = uncompressedImageIO.loadImage(
        outputDirectory + "greenComponent.ppm");
    ImageRepresentation expectedBlueImage = uncompressedImageIO.loadImage(
        outputDirectory + "blueComponent.ppm");
    assertEquals(expectedRedImage, splitImages[0]);
    assertEquals(expectedGreenImage, splitImages[1]);
    assertEquals(expectedBlueImage, splitImages[2]);
  }

  @Test
  public void testMultipleHorizontalFlips() throws IOException {
    ImageRepresentation originalImage = uncompressedImageIO.loadImage(validPpmPath);
    ImageRepresentation flippedImage = operations.flipHorizontally(originalImage);
    ImageRepresentation doubleFlippedImage = operations.flipHorizontally(flippedImage);

    assertEquals(originalImage, doubleFlippedImage);
  }

  @Test
  public void testMultipleVerticalFlips() throws IOException {
    ImageRepresentation originalImage = uncompressedImageIO.loadImage(validPpmPath);
    ImageRepresentation flippedImage = operations.flipVertically(originalImage);
    ImageRepresentation doubleFlippedImage = operations.flipVertically(flippedImage);

    assertEquals(originalImage, doubleFlippedImage);
  }

  @Test
  public void testFlippingAlreadyFlippedImageHorizontally() throws IOException {
    ImageRepresentation originalImage = uncompressedImageIO.loadImage(validPpmPath);
    ImageRepresentation flippedImage = operations.flipHorizontally(originalImage);
    ImageRepresentation reFlippedImage = operations.flipHorizontally(flippedImage);

    assertEquals(originalImage, reFlippedImage);
  }

  @Test
  public void testFlippingAlreadyFlippedImageVertically() throws IOException {
    ImageRepresentation originalImage = uncompressedImageIO.loadImage(validPpmPath);
    ImageRepresentation flippedImage = operations.flipVertically(originalImage);
    ImageRepresentation reFlippedImage = operations.flipVertically(flippedImage);

    assertEquals(originalImage, reFlippedImage);
  }

  @Test
  public void testRgbSplitAndCombine() throws IOException {
    ImageRepresentation originalImage = uncompressedImageIO.loadImage(validPpmPath);
    ImageRepresentation[] splitImages = operations.rgbSplit(originalImage);
    ImageRepresentation combinedImage = operations.combineRgb(splitImages[0], splitImages[1],
        splitImages[2]);
    assertEquals(originalImage, combinedImage);
  }

  @Test
  public void testSavePpmAsPpmAndLoadBack() throws IOException {
    ImageRepresentation originalImage = uncompressedImageIO.loadImage(validPpmPath);
    String ppmOutputPath = outputDirectory + "outputImage.ppm";
    uncompressedImageIO.saveImage(originalImage, ppmOutputPath, "ppm");
    ImageRepresentation loadedPpmImage = uncompressedImageIO.loadImage(ppmOutputPath);
    assertEquals(originalImage, loadedPpmImage);
  }
}
