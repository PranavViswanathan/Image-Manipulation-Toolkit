package com.vanarp.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.vanarp.controller.CompressedImageIO;
import com.vanarp.controller.UncompressedImageIO;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the operations of the program.
 */
public class OperationsTest {

  private CompressedImageIO compressedImageIO;
  private UncompressedImageIO uncompressedImageIO;
  private ImageRepresentation testImagePng;
  private ImageOperations operations;
  private final String validPngPath = "test/com/vanarp/model/TestResources/Source/bird.png";
  private final String ManhattanPath = "test/com/vanarp/model/TestResources/Source/"
      + "manhattan-small.png";

  private final String GalaxyPath = "test/com/vanarp/model/TestResources/Source/galaxy.png";
  private final String outputDirectory = "test/com/vanarp/model/TestResources/SampleOperations/";

  @Before
  public void setUp() throws IOException {
    ImageTransformationEnhanced transform = new Transform();
    ImageMaskingFiltering filter = new Filtering();
    ImageCompressionFunctionality compress = new ImageCompression();
    compressedImageIO = new CompressedImageIO();
    uncompressedImageIO = new UncompressedImageIO();
    operations = new Operations(transform, filter, compress);
    testImagePng = compressedImageIO.loadImage(validPngPath);
  }

  private ImageRepresentation createImageWithColor(int red, int green, int blue) {
    ImageRepresentation image = new Image(1, 1);
    image.setPixel(0, 0, new Pixel(red, green, blue));
    return image;
  }

  private Image createImageWithSize(int width, int height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Image dimensions must be positive.");
    }
    return new Image(width, height);
  }


  @Test(expected = IOException.class)
  public void testLoadNonExistentFile() throws IOException {
    String nonExistentFilePath = "test/com/vanarp/model/TestResources/Source/nonexistent.png";
    compressedImageIO.loadImage(nonExistentFilePath);
  }

  @Test(expected = IOException.class)
  public void testLoadInvalidFileFormat() throws IOException {
    String invalidFilePath = "test/com/vanarp/model/TestResources/Invalid/bird.png";
    compressedImageIO.loadImage(invalidFilePath);
  }

  @Test
  public void testLoadValidPngFile() throws IOException {
    ImageRepresentation image = compressedImageIO.loadImage(validPngPath);
    assertEquals(632, image.getWidth());
    assertEquals(632, image.getHeight());
  }

  @Test(expected = IOException.class)
  public void testLoadEmptyDirectory() throws IOException {
    String emptyDirectory = "";
    compressedImageIO.loadImage(emptyDirectory);
  }

  @Test(expected = IOException.class)
  public void testSaveToInvalidPath() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(validPngPath);
    String invalidOutputPath = "/invalid/directory/output.png";
    compressedImageIO.saveImage(testImage, invalidOutputPath, "png");
  }


  @Test
  public void testRedComponentForPng() throws IOException {
    ImageRepresentation redImage = operations.redComponent(testImagePng);
    ImageRepresentation expectedRedImage = compressedImageIO.loadImage(
        outputDirectory + "redComponent.png");

    assertEquals(expectedRedImage, redImage);
  }

  @Test
  public void testGreenComponentForPng() throws IOException {
    ImageRepresentation greenImage = operations.greenComponent(testImagePng);
    ImageRepresentation expectedGreenImage = compressedImageIO.loadImage(
        outputDirectory + "greenComponent.png");

    assertEquals(expectedGreenImage, greenImage);
  }

  @Test
  public void testBlueComponentForPng() throws IOException {
    ImageRepresentation blueImage = operations.blueComponent(testImagePng);
    ImageRepresentation expectedBlueImage = compressedImageIO.loadImage(
        outputDirectory + "blueComponent.png");

    assertEquals(expectedBlueImage, blueImage);
  }

  @Test
  public void testValueComponentForPng() throws IOException {
    ImageRepresentation valueImage = operations.valueComponent(testImagePng);
    ImageRepresentation expectedValueImage = compressedImageIO.loadImage(
        outputDirectory + "valueComponent.png");

    assertEquals(expectedValueImage, valueImage);
  }

  @Test
  public void testLumaComponentForPng() throws IOException {
    ImageRepresentation lumaImage = operations.lumaComponent(testImagePng);
    ImageRepresentation expectedLumaImage = compressedImageIO.loadImage(
        outputDirectory + "lumaComponent.png");

    assertEquals(expectedLumaImage, lumaImage);
  }

  @Test
  public void testIntensityComponentForPng() throws IOException {
    ImageRepresentation intensityImage = operations.intensityComponent(testImagePng);
    ImageRepresentation expectedIntensityImage = compressedImageIO.loadImage(
        outputDirectory + "intensityComponent.png");

    assertEquals(expectedIntensityImage, intensityImage);
  }

  @Test
  public void testFlipHorizontallyForPng() throws IOException {
    ImageRepresentation flippedImage = operations.flipHorizontally(testImagePng);
    ImageRepresentation expectedFlippedImage = compressedImageIO.loadImage(
        outputDirectory + "flippedHorizontally.png");

    assertEquals(expectedFlippedImage, flippedImage);
  }

  @Test
  public void testFlipVerticallyForPng() throws IOException {
    ImageRepresentation flippedImage = operations.flipVertically(testImagePng);
    ImageRepresentation expectedFlippedImage = compressedImageIO.loadImage(
        outputDirectory + "flippedVertically.png");

    assertEquals(expectedFlippedImage, flippedImage);
  }

  @Test
  public void testBrightenImageForPng() throws IOException {
    ImageRepresentation brightenedImage = operations.brightenImage(testImagePng, 10);
    ImageRepresentation expectedBrightenedImage = compressedImageIO.loadImage(
        outputDirectory + "brightenedImage.png");

    assertEquals(expectedBrightenedImage, brightenedImage);
  }

  @Test
  public void testApplySepiaForPng() throws IOException {
    ImageRepresentation sepiaImage = operations.applySepia(testImagePng);
    ImageRepresentation expectedSepiaImage = compressedImageIO.loadImage(
        outputDirectory + "sepiaImage.png");

    assertEquals(expectedSepiaImage, sepiaImage);
  }

  @Test
  public void testBlurForPng() throws IOException {
    ImageRepresentation blurredImage = operations.blur(testImagePng);
    ImageRepresentation expectedBlurredImage = compressedImageIO.loadImage(
        outputDirectory + "blurredImage.png");

    assertEquals(expectedBlurredImage, blurredImage);
  }

  @Test
  public void testSharpenForPng() throws IOException {
    ImageRepresentation sharpenedImage = operations.sharpen(testImagePng);
    ImageRepresentation expectedSharpenedImage = compressedImageIO.loadImage(
        outputDirectory + "sharpenedImage.png");

    assertEquals(expectedSharpenedImage, sharpenedImage);
  }

  @Test
  public void testApplyGreyScaleForPng() throws IOException {
    ImageRepresentation greyScaleImage = operations.applyGreyScale(testImagePng);
    ImageRepresentation expectedGreyScaleImage = compressedImageIO.loadImage(
        outputDirectory + "greyScaleImage.png");

    assertEquals(expectedGreyScaleImage, greyScaleImage);
  }

  @Test
  public void testRgbCombineForPng() throws IOException {
    ImageRepresentation redImage = compressedImageIO.loadImage(
        outputDirectory + "redComponent.png");
    ImageRepresentation greenImage = compressedImageIO.loadImage(
        outputDirectory + "greenComponent.png");
    ImageRepresentation blueImage = compressedImageIO.loadImage(
        outputDirectory + "blueComponent.png");

    ImageRepresentation combinedImage = operations.combineRgb(redImage, greenImage, blueImage);

    assertEquals(testImagePng, combinedImage);
  }

  @Test
  public void testSplitRgbForPng() throws IOException {
    ImageRepresentation[] splitImages = operations.rgbSplit(testImagePng);

    ImageRepresentation expectedRedImage = compressedImageIO.loadImage(
        outputDirectory + "redComponent.png");
    ImageRepresentation expectedGreenImage = compressedImageIO.loadImage(
        outputDirectory + "greenComponent.png");
    ImageRepresentation expectedBlueImage = compressedImageIO.loadImage(
        outputDirectory + "blueComponent.png");

    assertEquals(expectedRedImage, splitImages[0]);
    assertEquals(expectedGreenImage, splitImages[1]);
    assertEquals(expectedBlueImage, splitImages[2]);
  }

  @Test
  public void testExtractRedComponentFromRedImage() throws IOException {
    ImageRepresentation redImage = createImageWithColor(255, 0, 0);
    ImageRepresentation extractedRed = operations.redComponent(redImage);
    ImageRepresentation expectedRed = createImageWithColor(255, 255, 255);

    assertEquals(expectedRed, extractedRed);
  }

  @Test
  public void testExtractGreenComponentFromGreenImage() throws IOException {
    ImageRepresentation greenImage = createImageWithColor(0, 255, 0);
    ImageRepresentation extractedGreen = operations.greenComponent(greenImage);
    ImageRepresentation expectedGreen = createImageWithColor(255, 255, 255);

    assertEquals(expectedGreen, extractedGreen);
  }

  @Test
  public void testExtractBlueComponentFromBlueImage() throws IOException {
    ImageRepresentation blueImage = createImageWithColor(0, 0, 255);
    ImageRepresentation extractedBlue = operations.blueComponent(blueImage);
    ImageRepresentation expectedBlue = createImageWithColor(255, 255, 255);

    assertEquals(expectedBlue, extractedBlue);
  }

  @Test
  public void testExtractComponentsFromWhiteImage() throws IOException {
    ImageRepresentation whiteImage = createImageWithColor(255, 255, 255);
    ImageRepresentation extractedRed = operations.redComponent(whiteImage);
    ImageRepresentation extractedGreen = operations.greenComponent(whiteImage);
    ImageRepresentation extractedBlue = operations.blueComponent(whiteImage);

    assertEquals(whiteImage, extractedRed);
    assertEquals(whiteImage, extractedGreen);
    assertEquals(whiteImage, extractedBlue);
  }

  @Test
  public void testExtractComponentsFromBlackImage() throws IOException {
    ImageRepresentation blackImage = createImageWithColor(0, 0, 0);
    ImageRepresentation extractedRed = operations.redComponent(blackImage);
    ImageRepresentation extractedGreen = operations.greenComponent(blackImage);
    ImageRepresentation extractedBlue = operations.blueComponent(blackImage);

    assertEquals(blackImage, extractedRed);
    assertEquals(blackImage, extractedGreen);
    assertEquals(blackImage, extractedBlue);
  }

  @Test
  public void testExtractComponentsFromGrayscaleImage() throws IOException {
    ImageRepresentation grayImage = createImageWithColor(128, 128, 128);
    ImageRepresentation extractedRed = operations.redComponent(grayImage);
    ImageRepresentation extractedGreen = operations.greenComponent(grayImage);
    ImageRepresentation extractedBlue = operations.blueComponent(grayImage);

    assertEquals(grayImage, extractedRed);
    assertEquals(grayImage, extractedGreen);
    assertEquals(grayImage, extractedBlue);
  }

  @Test
  public void testValueComponentCalculation() throws IOException {
    ImageRepresentation whiteImage = createImageWithColor(255, 255, 255);
    ImageRepresentation valueComponent = operations.valueComponent(whiteImage);
    ImageRepresentation expectedValue = createImageWithColor(255, 255, 255);

    assertEquals(expectedValue, valueComponent);
  }

  @Test
  public void testIntensityCalculationWithVariousRGB() throws IOException {
    ImageRepresentation mixedColorImage = createImageWithColor(100, 150, 200);
    ImageRepresentation intensityComponent = operations.intensityComponent(mixedColorImage);
    int expectedIntensityValue = (100 + 150 + 200) / 3;
    ImageRepresentation expectedIntensityImage = createImageWithColor(expectedIntensityValue,
        expectedIntensityValue, expectedIntensityValue);

    assertEquals(expectedIntensityImage, intensityComponent);
  }

  @Test
  public void testLumaCalculationAccuracyWithTolerance() throws IOException {
    ImageRepresentation mixedColorImage = createImageWithColor(100, 150, 200);
    ImageRepresentation lumaComponent = operations.lumaComponent(mixedColorImage);
    int expectedLumaValue = (int) (0.299 * 100 + 0.587 * 150 + 0.114 * 200);
    ImageRepresentation expectedLumaImage = createImageWithColor(expectedLumaValue,
        expectedLumaValue,
        expectedLumaValue);
    int delta = 2;
    for (int y = 0; y < expectedLumaImage.getHeight(); y++) {
      for (int x = 0; x < expectedLumaImage.getWidth(); x++) {
        PixelInterface actualPixel = lumaComponent.getPixel(x, y);
        PixelInterface expectedPixel = expectedLumaImage.getPixel(x, y);

        assertTrue(Math.abs(actualPixel.getRed() - expectedPixel.getRed()) <= delta);
        assertTrue(Math.abs(actualPixel.getGreen() - expectedPixel.getGreen()) <= delta);
        assertTrue(Math.abs(actualPixel.getBlue() - expectedPixel.getBlue()) <= delta);
      }
    }
  }


  @Test
  public void testComponentExtractionWithNegativeRGBValues() throws IOException {
    ImageRepresentation invalidNegativeImage = createImageWithColor(-10, -20, -30);
    ImageRepresentation redComponent = operations.redComponent(invalidNegativeImage);
    ImageRepresentation greenComponent = operations.greenComponent(invalidNegativeImage);
    ImageRepresentation blueComponent = operations.blueComponent(invalidNegativeImage);

    ImageRepresentation expectedClampedImage = createImageWithColor(0, 0, 0);
    assertEquals(expectedClampedImage, redComponent);
    assertEquals(expectedClampedImage, greenComponent);
    assertEquals(expectedClampedImage, blueComponent);
  }

  @Test
  public void testComponentExtractionWithRGBValuesAbove255() throws IOException {
    ImageRepresentation invalidHighImage = createImageWithColor(300, 400, 500);
    ImageRepresentation redComponent = operations.redComponent(invalidHighImage);
    ImageRepresentation greenComponent = operations.greenComponent(invalidHighImage);
    ImageRepresentation blueComponent = operations.blueComponent(invalidHighImage);

    ImageRepresentation expectedClampedImage = createImageWithColor(255, 255, 255);
    assertEquals(expectedClampedImage, redComponent);
    assertEquals(expectedClampedImage, greenComponent);
    assertEquals(expectedClampedImage, blueComponent);
  }

  @Test
  public void testMultipleHorizontalFlips() throws IOException {
    ImageRepresentation originalImage = compressedImageIO.loadImage(validPngPath);
    ImageRepresentation flippedImage = operations.flipHorizontally(originalImage);
    ImageRepresentation doubleFlippedImage = operations.flipHorizontally(flippedImage);

    assertEquals(originalImage, doubleFlippedImage);
  }

  @Test
  public void testMultipleVerticalFlips() throws IOException {
    ImageRepresentation originalImage = compressedImageIO.loadImage(validPngPath);
    ImageRepresentation flippedImage = operations.flipVertically(originalImage);
    ImageRepresentation doubleFlippedImage = operations.flipVertically(flippedImage);

    assertEquals(originalImage, doubleFlippedImage);
  }

  @Test
  public void testFlippingAlreadyFlippedImageHorizontally() throws IOException {
    ImageRepresentation originalImage = compressedImageIO.loadImage(validPngPath);
    ImageRepresentation flippedImage = operations.flipHorizontally(originalImage);
    ImageRepresentation reFlippedImage = operations.flipHorizontally(flippedImage);

    assertEquals(originalImage, reFlippedImage);
  }

  @Test
  public void testFlippingAlreadyFlippedImageVertically() throws IOException {
    ImageRepresentation originalImage = compressedImageIO.loadImage(validPngPath);
    ImageRepresentation flippedImage = operations.flipVertically(originalImage);
    ImageRepresentation reFlippedImage = operations.flipVertically(flippedImage);

    assertEquals(originalImage, reFlippedImage);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFlippingNullImageHorizontally() throws IOException {
    operations.flipHorizontally(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFlippingNullImageVertically() throws IOException {
    operations.flipVertically(null);
  }

  @Test
  public void testBrighteningByPositiveValue() throws IOException {
    ImageRepresentation image = createImageWithColor(100, 100, 100);
    ImageRepresentation brightenedImage = operations.brightenImage(image, 50);

    ImageRepresentation expectedImage = createImageWithColor(150, 150, 150);
    assertEquals(expectedImage, brightenedImage);
  }

  @Test
  public void testDarkeningByNegativeValue() throws IOException {
    ImageRepresentation image = createImageWithColor(100, 100, 100);
    ImageRepresentation darkenedImage = operations.brightenImage(image, -50);

    ImageRepresentation expectedImage = createImageWithColor(50, 50, 50);
    assertEquals(expectedImage, darkenedImage);
  }

  @Test
  public void testBrightnessAdjustmentOfZero() throws IOException {
    ImageRepresentation image = createImageWithColor(100, 100, 100);
    ImageRepresentation adjustedImage = operations.brightenImage(image, 0);

    ImageRepresentation expectedImage = createImageWithColor(100, 100, 100);
    assertEquals(expectedImage, adjustedImage);
  }

  @Test
  public void testBrighteningBeyondMaxValue() throws IOException {
    ImageRepresentation image = createImageWithColor(230, 230, 230);
    ImageRepresentation brightenedImage = operations.brightenImage(image, 50);

    ImageRepresentation expectedImage = createImageWithColor(255, 255, 255);
    assertEquals(expectedImage, brightenedImage);
  }

  @Test
  public void testDarkeningBeyondMinValue() throws IOException {
    ImageRepresentation image = createImageWithColor(30, 30, 30);
    ImageRepresentation darkenedImage = operations.brightenImage(image, -50);

    ImageRepresentation expectedImage = createImageWithColor(0, 0, 0);
    assertEquals(expectedImage, darkenedImage);
  }

  @Test
  public void testBrightnessOnMaxBrightImage() throws IOException {
    ImageRepresentation image = createImageWithColor(255, 255, 255);
    ImageRepresentation brightenedImage = operations.brightenImage(image, 50);

    ImageRepresentation expectedImage = createImageWithColor(255, 255, 255);
    assertEquals(expectedImage, brightenedImage);
  }

  @Test
  public void testBrightnessOnCompletelyDarkImage() throws IOException {
    ImageRepresentation image = createImageWithColor(0, 0, 0);
    ImageRepresentation brightenedImage = operations.brightenImage(image, 50);

    ImageRepresentation expectedImage = createImageWithColor(50, 50, 50);
    assertEquals(expectedImage, brightenedImage);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidBrightnessValues() throws IOException {
    ImageRepresentation image = createImageWithColor(100, 100, 100);
    operations.brightenImage(image, Integer.parseInt("non-numeric"));
  }

  @Test
  public void testExtremeBrightnessValues() throws IOException {
    ImageRepresentation image = createImageWithColor(100, 100, 100);

    ImageRepresentation brightenedImage = operations.brightenImage(image, 10000);
    ImageRepresentation expectedBrightenedImage = createImageWithColor(255, 255, 255);
    assertEquals(expectedBrightenedImage, brightenedImage);

    ImageRepresentation darkenedImage = operations.brightenImage(image, -10000);
    ImageRepresentation expectedDarkenedImage = createImageWithColor(0, 0, 0);
    assertEquals(expectedDarkenedImage, darkenedImage);
  }

  @Test
  public void testSplittingRGBImageIntoThreeChannels() throws IOException {
    ImageRepresentation rgbImage = createImageWithColor(100, 150, 200);
    ImageRepresentation[] channels = operations.rgbSplit(rgbImage);

    ImageRepresentation expectedRedComponent = createImageWithColor(100, 100, 100);
    ImageRepresentation expectedGreenComponent = createImageWithColor(150, 150, 150);
    ImageRepresentation expectedBlueComponent = createImageWithColor(200, 200, 200);

    assertEquals(expectedRedComponent, channels[0]);
    assertEquals(expectedGreenComponent, channels[1]);
    assertEquals(expectedBlueComponent, channels[2]);
  }

  @Test
  public void testCombiningThreeGrayscaleImagesIntoRGB() throws IOException {
    ImageRepresentation redComponent = createImageWithColor(100, 100, 100);
    ImageRepresentation greenComponent = createImageWithColor(150, 150, 150);
    ImageRepresentation blueComponent = createImageWithColor(200, 200, 200);

    ImageRepresentation rgbImage = operations.combineRgb(redComponent, greenComponent,
        blueComponent);

    ImageRepresentation expectedImage = createImageWithColor(100, 150, 200);
    assertEquals(expectedImage, rgbImage);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCombiningWithInvalidChannelImages() throws IOException {
    ImageRepresentation invalidComponent = createImageWithColor(100, 150, 200);
    ImageRepresentation grayscaleImage = createImageWithColor(100, 100, 100);

    operations.combineRgb(invalidComponent, grayscaleImage, grayscaleImage);
  }

  @Test(expected = NullPointerException.class)
  public void testCombiningImagesOfDifferentSizes() throws IOException {
    ImageRepresentation smallImage = createImageWithSize(100, 100);
    ImageRepresentation largeImage = createImageWithSize(200, 200);

    operations.combineRgb(smallImage, largeImage, largeImage);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSplittingNullImage() throws IOException {
    operations.rgbSplit(null);
  }

  @Test(expected = IOException.class)
  public void testCombiningWithNullImages() throws IOException {
    ImageRepresentation validImage = createImageWithColor(100, 100, 100);
    operations.combineRgb(validImage, null, validImage);
  }

  @Test
  public void testBlurOnUniformColorImage() throws IOException {
    ImageRepresentation uniformImage = createImageWithColor(100, 100, 100);
    ImageRepresentation blurredImage = operations.blur(uniformImage);
    assertEquals(uniformImage, blurredImage);
  }

  @Test
  public void testBlurOnAlreadyBlurredImage() throws IOException {
    ImageRepresentation blurredImage = createImageWithColor(150, 150, 150);
    ImageRepresentation newBlurredImage = operations.blur(blurredImage);
    assertEquals(blurredImage, newBlurredImage);
  }

  @Test
  public void testBlurOn1x1Image() throws IOException {
    ImageRepresentation oneByOneImage = createImageWithColor(50, 100, 150);
    ImageRepresentation blurredImage = operations.blur(oneByOneImage);
    assertEquals(oneByOneImage, blurredImage);
  }

  @Test
  public void testSharpenOnUniformColorImage() throws IOException {
    ImageRepresentation uniformImage = createImageWithColor(100, 100, 100);
    ImageRepresentation sharpenedImage = operations.sharpen(uniformImage);
    assertEquals(uniformImage, sharpenedImage);
  }

  @Test
  public void testSharpenOnAlreadySharpenedImage() throws IOException {
    ImageRepresentation sharpenedImage = createImageWithColor(200, 200, 200);
    ImageRepresentation newSharpenedImage = operations.sharpen(
        sharpenedImage);
    assertEquals(sharpenedImage, newSharpenedImage);
  }

  @Test
  public void testSharpenOn1x1Image() throws IOException {
    ImageRepresentation oneByOneImage = createImageWithColor(50, 100, 150);
    ImageRepresentation sharpenedImage = operations.sharpen(oneByOneImage);
    assertEquals(oneByOneImage, sharpenedImage);
  }

  @Test
  public void testMultipleConsecutiveBlurSharpenOperations() throws IOException {
    ImageRepresentation initialImage = createImageWithColor(255, 0, 0);
    ImageRepresentation blurredImage = operations.blur(initialImage);
    ImageRepresentation sharpenedImage = operations.sharpen(blurredImage);
    ImageRepresentation expectedImage = createImageWithColor(255, 0, 0);
    assertEquals(expectedImage, sharpenedImage);
  }


  @Test
  public void testSepiaOnCompletelyBlackImage() throws IOException {
    ImageRepresentation blackImage = createImageWithColor(0, 0, 0);
    ImageRepresentation sepiaImage = operations.applySepia(blackImage);

    ImageRepresentation expectedImage = createImageWithColor(0, 0, 0);
    assertEquals(expectedImage, sepiaImage);
  }

  @Test
  public void testSepiaOnGrayscaleImage() throws IOException {
    ImageRepresentation grayscaleImage = createImageWithColor(128, 128, 128);
    ImageRepresentation sepiaImage = operations.applySepia(grayscaleImage);

    ImageRepresentation expectedImage = createImageWithColor(172, 153, 119);
    assertEquals(expectedImage, sepiaImage);
  }

  @Test
  public void testSepiaOnAlreadySepiaTonedImage() throws IOException {
    ImageRepresentation sepiaImage = createImageWithColor(112, 96, 80);
    ImageRepresentation newSepiaImage = operations.applySepia(sepiaImage);

    ImageRepresentation expectedImage = createImageWithColor(132, 118, 92);
    assertEquals(expectedImage, newSepiaImage);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOperationsOnZeroSizedImage() throws IOException {
    Image zeroSizeImage = new Image(0, 0);
    operations.blur(zeroSizeImage);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithPartialIncompleteImageData() {
    createImageWithSize(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNonStandardImageDimensions() {
    createImageWithSize(-10, 20);
  }

  @Test
  public void testWithImagesHavingMaximumPossiblePixelValues() throws IOException {
    ImageRepresentation maxColorImage = createImageWithColor(255, 255, 255);
    ImageRepresentation processedImage = operations.blur(maxColorImage);
    ImageRepresentation expectedImage = createImageWithColor(255, 255, 255);
    assertEquals(expectedImage, processedImage);
  }

  @Test
  public void testWithImagesHavingMinimumPossiblePixelValues() throws IOException {
    ImageRepresentation minColorImage = createImageWithColor(0, 0, 0);
    ImageRepresentation processedImage = operations.blur(minColorImage);
    ImageRepresentation expectedImage = createImageWithColor(0, 0, 0);
    assertEquals(expectedImage, processedImage);
  }

  @Test
  public void testBrightnessMaxAndMin() throws IOException {
    ImageRepresentation maxColorImage = createImageWithColor(255, 255, 255);
    ImageRepresentation processedMaxColor = operations.brightenImage(maxColorImage, 10);
    ImageRepresentation expectedMaxColor = createImageWithColor(255, 255, 255);
    assertEquals(expectedMaxColor, processedMaxColor);

    ImageRepresentation minColorImage = createImageWithColor(0, 0, 0);
    ImageRepresentation processedMinColor = operations.brightenImage(minColorImage, -10);
    ImageRepresentation expectedMinColor = createImageWithColor(0, 0, 0);
    assertEquals(expectedMinColor, processedMinColor);
  }

  @Test
  public void testClamp() throws IOException {
    ImageRepresentation clampImage = createImageWithColor(255, 0, 0);
    ImageRepresentation processedClamp = operations.brightenImage(clampImage, 100);
    ImageRepresentation expectedClamp = createImageWithColor(255, 100, 100);
    assertEquals(expectedClamp, processedClamp);
  }

  @Test
  public void testRgbSplitAndCombine() throws IOException {
    ImageRepresentation originalImage = compressedImageIO.loadImage(validPngPath);
    ImageRepresentation[] splitImages = operations.rgbSplit(originalImage);
    ImageRepresentation combinedImage = operations.combineRgb(splitImages[0], splitImages[1],
        splitImages[2]);
    assertEquals(originalImage, combinedImage);
  }

  @Test
  public void testSavePngAsPpmAndLoadBack() throws IOException {
    ImageRepresentation originalImage = compressedImageIO.loadImage(validPngPath);
    String ppmOutputPath = outputDirectory + "outputImage.ppm";
    uncompressedImageIO.saveImage(originalImage, ppmOutputPath, "ppm");
    ImageRepresentation loadedPpmImage = uncompressedImageIO.loadImage(ppmOutputPath);
    assertEquals(originalImage, loadedPpmImage);
  }

  @Test
  public void testHistogram() throws IOException {
    ImageRepresentation image = compressedImageIO.loadImage(ManhattanPath);
    ImageRepresentation histogram = operations.getHistogram(image);
    ImageRepresentation expectedHistogram = compressedImageIO.loadImage(
        outputDirectory + "ManhattanHistogram.png");
    assertEquals(expectedHistogram, histogram);
  }

  @Test
  public void testColorCorrect() throws IOException {
    ImageRepresentation image = compressedImageIO.loadImage(ManhattanPath);
    ImageRepresentation colorCorrect = operations.colorCorrect(image);
    ImageRepresentation expectedColorCorrected = compressedImageIO.loadImage(
        outputDirectory + "ColorCorrect.png");
    assertEquals(expectedColorCorrected, colorCorrect);
  }

  @Test
  public void testLevelsAdjust() throws IOException {
    ImageRepresentation image = compressedImageIO.loadImage(ManhattanPath);
    ImageRepresentation levelsAdjust = operations.levelsAdjust(image, 20, 100, 255);
    ImageRepresentation expectedLevelsAdjust = compressedImageIO.loadImage(
        outputDirectory + "levelsAdjust.png");
    assertEquals(expectedLevelsAdjust, levelsAdjust);
  }

  @Test
  public void testColorCorrectHistogram() throws IOException {
    ImageRepresentation image = compressedImageIO.loadImage(GalaxyPath);
    ImageRepresentation colorCorrect = operations.colorCorrect(image);
    ImageRepresentation colorCorrectHistogram = operations.getHistogram(colorCorrect);
    ImageRepresentation expectedcolorCorrectHistogram = compressedImageIO.loadImage(
        outputDirectory + "colorCorrectHistogram.png");
    assertEquals(expectedcolorCorrectHistogram, colorCorrectHistogram);
  }

  @Test
  public void testAdjustValuesHistogram() throws IOException {
    ImageRepresentation image = compressedImageIO.loadImage(GalaxyPath);
    ImageRepresentation levelsAdjust = operations.levelsAdjust(image, 20, 100, 255);
    ImageRepresentation levelsAdjustHistogram = operations.getHistogram(levelsAdjust);
    ImageRepresentation expectedlevelsAdjustHistogram = compressedImageIO.loadImage(
        outputDirectory + "AdjustedValuesHistogram.png");
    assertEquals(expectedlevelsAdjustHistogram, levelsAdjustHistogram);
  }

  @Test
  public void testBlurSplit() throws IOException {
    ImageRepresentation image = compressedImageIO.loadImage(ManhattanPath);
    ImageRepresentation blur = operations.blur(image);
    ImageRepresentation splitBlur = operations.splitImages(blur, image, 25);
    ImageRepresentation expectedSplitBlur = compressedImageIO.loadImage(
        outputDirectory + "Split25.png");
    assertEquals(expectedSplitBlur, splitBlur);
  }

  @Test
  public void testLevelAdjustSplit() throws IOException {
    ImageRepresentation image = compressedImageIO.loadImage(ManhattanPath);
    ImageRepresentation levelsAdjust = operations.levelsAdjust(image, 20, 100, 255);
    ImageRepresentation splitlevelsAdjust = operations.splitImages(levelsAdjust, image, 70);
    ImageRepresentation expectedSplitlevelsAdjust = compressedImageIO.loadImage(
        outputDirectory + "Split70.png");
    assertEquals(expectedSplitlevelsAdjust, splitlevelsAdjust);
  }

  @Test
  public void testGetPixelReturnsDeepCopy() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(ManhattanPath);
    PixelInterface originalPixel = testImage.getPixel(0, 0);
    PixelInterface pixelCopy = testImage.getPixel(0, 0);
    assertNotSame(originalPixel, pixelCopy);
  }

  @Test
  public void testCompress() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation compressedImage = operations.compressImage(testImage, 100);
    ImageRepresentation expectedCompressedImage = compressedImageIO.loadImage(
        outputDirectory + "bird-compress.png");
    assertEquals(expectedCompressedImage, compressedImage);
  }

  @Test
  public void testDownscaling() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation downscaledImage = operations.downscaleImage(testImage, 10, 10);
    ImageRepresentation expectedDownscaledImage = compressedImageIO.loadImage(
        outputDirectory + "bird-downscale.png");
    assertEquals(expectedDownscaledImage, downscaledImage);
  }

  @Test
  public void testDownscalingToDifferentAspectRatio() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation downscaledImage = operations.downscaleImage(testImage, 100,
        50); // Downscale to 100x50
    ImageRepresentation expectedDownscaledImage = compressedImageIO.loadImage(
        outputDirectory + "bird-downscale-100x50.png");
    assertEquals(expectedDownscaledImage, downscaledImage);
  }

  @Test
  public void testDownscalingWithInvalidDimensions() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      operations.downscaleImage(testImage, -10, -10); // Invalid dimensions
    });
    assertEquals("New dimensions must be greater than zero.", exception.getMessage());
  }

  @Test
  public void testDownscalingWithSameDimensions() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation downscaledImage = operations.downscaleImage(testImage, 632,
        632);
    assertEquals(testImage, downscaledImage);
  }

  @Test
  public void testMaskingBlur() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation maskImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird-masked.png");
    ImageRepresentation blurWithMask = operations.applyBlurWithMask(testImage, maskImage);
    ImageRepresentation expectedImage = compressedImageIO.loadImage(
        outputDirectory + "bird-blur-mask.png");
    assertEquals(expectedImage, blurWithMask);
  }

  @Test
  public void testMaskingSepia() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation maskImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird-masked.png");
    ImageRepresentation sepiaWithMask = operations.applySepiaWithMask(testImage, maskImage);
    ImageRepresentation expectedImage = compressedImageIO.loadImage(
        outputDirectory + "bird-sepia-mask.png");
    assertEquals(expectedImage, sepiaWithMask);
  }

  @Test
  public void testMaskingGreyscale() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation maskImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird-masked.png");
    ImageRepresentation greyscaleWithMask = operations.applyGreyscaleWithMask(testImage, maskImage);
    ImageRepresentation expectedImage = compressedImageIO.loadImage(
        outputDirectory + "bird-greyscale-mask.png");
    assertEquals(expectedImage, greyscaleWithMask);
  }

  @Test
  public void testMaskingSharpen() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation maskImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird-masked.png");
    ImageRepresentation sharpenWithMask = operations.applySharpenWithMask(testImage, maskImage);
    ImageRepresentation expectedImage = compressedImageIO.loadImage(
        outputDirectory + "bird-sharpen-mask.png");
    assertEquals(expectedImage, sharpenWithMask);
  }

  @Test
  public void testMaskingBlueComponent() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation maskImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird-masked.png");
    ImageRepresentation blueComponentWithMask = operations.blueComponentWithMask(testImage,
        maskImage);
    ImageRepresentation expectedImage = compressedImageIO.loadImage(
        outputDirectory + "bird-blueComponent-mask.png");
    assertEquals(expectedImage, blueComponentWithMask);
  }

  @Test
  public void testMaskingRedComponent() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation maskImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird-masked.png");
    ImageRepresentation redComponentWithMask = operations.redComponentWithMask(testImage,
        maskImage);
    ImageRepresentation expectedImage = compressedImageIO.loadImage(
        outputDirectory + "bird-redComponent-mask.png");
    assertEquals(expectedImage, redComponentWithMask);
  }

  @Test
  public void testMaskingGreenComponent() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation maskImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird-masked.png");
    ImageRepresentation greenComponentWithMask = operations.greenComponentWithMask(testImage,
        maskImage);
    ImageRepresentation expectedImage = compressedImageIO.loadImage(
        outputDirectory + "bird-greenComponent-mask.png");
    assertEquals(expectedImage, greenComponentWithMask);
  }

  @Test
  public void testMaskingValueComponent() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation maskImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird-masked.png");
    ImageRepresentation valueComponentWithMask = operations.valueComponentWithMask(testImage,
        maskImage);
    ImageRepresentation expectedImage = compressedImageIO.loadImage(
        outputDirectory + "bird-valueComponent-mask.png");
    assertEquals(expectedImage, valueComponentWithMask);
  }

  @Test
  public void testMaskingLumaComponent() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation maskImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird-masked.png");
    ImageRepresentation lumaComponentWithMask = operations.lumaComponentWithMask(testImage,
        maskImage);
    ImageRepresentation expectedImage = compressedImageIO.loadImage(
        outputDirectory + "bird-lumaComponent-mask.png");
    assertEquals(expectedImage, lumaComponentWithMask);
  }

  @Test
  public void testMaskingIntensityComponent() throws IOException {
    ImageRepresentation testImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation maskImage = compressedImageIO.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird-masked.png");
    ImageRepresentation intensityComponentWithMask = operations.intensityComponentWithMask(
        testImage,
        maskImage);
    ImageRepresentation expectedImage = compressedImageIO.loadImage(
        outputDirectory + "bird-intensityComponent-mask.png");
    assertEquals(expectedImage, intensityComponentWithMask);
  }
}