package com.vanarp.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.vanarp.model.Filtering;
import com.vanarp.model.ImageCompression;
import com.vanarp.model.ImageCompressionFunctionality;
import com.vanarp.model.ImageMaskingFiltering;
import com.vanarp.model.ImageOperations;
import com.vanarp.model.ImageRepresentation;
import com.vanarp.model.ImageTransformationEnhanced;
import com.vanarp.model.Operations;
import com.vanarp.model.Transform;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;

/**
 * This class contains unit tests for the functionality of the Image Command Processor and CLI View.
 * It tests various image operations such as loading, saving, transforming, and applying filters to
 * images. The tests ensure that the commands issued through the CLI are processed correctly and
 * that the expected results are achieved.
 *
 * <p>The tests also validate the behavior of the image cache and the output of the command
 * processor when executing scripts.
 */
public class ControllerTest {

  private ImageCommandProcessor commandProcessor;
  private CLIView cliView;
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    ImageTransformationEnhanced transform = new Transform();
    ImageMaskingFiltering filter = new Filtering();
    ImageCompressionFunctionality compression = new ImageCompression();
    ImageOperations operations = new Operations(transform, filter, compression);

    commandProcessor = new CommandProcessor(operations);
    cliView = new CLIView(commandProcessor);

    System.setOut(new PrintStream(outputStream));
  }

  private ImageRepresentation loadImage(String path) throws IOException {
    commandProcessor.loadImage(path, "tempImage");
    return commandProcessor.getImage("tempImage");
  }

  @Test
  public void testLoadImage() throws IOException {
    cliView.processCommand("load test/com/vanarp/model/TestResources/Source/bird.png testImage");
    assertNotNull("Loaded image should not be null", commandProcessor.getImage("testImage"));
    ImageRepresentation expectedImage = loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation actualImage = commandProcessor.getImage("testImage");
    assertEquals(expectedImage, actualImage);
  }

  @Test
  public void testSaveImage() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png", "testImage");
    cliView.processCommand("save output.png testImage");
    ImageRepresentation savedImage = loadImage("output.png");
    ImageRepresentation expectedImage = loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    assertEquals(expectedImage, savedImage);
  }

  @Test
  public void testRedComponent() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("red-component testImage redImage");
    assertNotNull("Red image should not be null", commandProcessor.getImage("redImage"));
    ImageRepresentation expectedRedImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/redComponent.png");
    ImageRepresentation actualRedImage = commandProcessor.getImage("redImage");
    assertEquals(expectedRedImage, actualRedImage);
  }

  @Test
  public void testGreenComponent() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("green-component testImage greenImage");
    assertNotNull("Green image should not be null", commandProcessor.getImage("greenImage"));
    ImageRepresentation expectedGreenImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/greenComponent.png");
    ImageRepresentation actualGreenImage = commandProcessor.getImage("greenImage");
    assertEquals(expectedGreenImage, actualGreenImage);
  }

  @Test
  public void testBlueComponent() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("blue-component testImage blueImage");
    assertNotNull("Blue image should not be null", commandProcessor.getImage("blueImage"));
    ImageRepresentation expectedBlueImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/blueComponent.png");
    ImageRepresentation actualBlueImage = commandProcessor.getImage("blueImage");
    assertEquals(expectedBlueImage, actualBlueImage);
  }

  @Test
  public void testHorizontalFlip() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("horizontal-flip testImage flippedImage");
    assertNotNull("Flipped image should not be null", commandProcessor.getImage("flippedImage"));
    ImageRepresentation expectedFlippedImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/flippedHorizontally.png");
    ImageRepresentation actualFlippedImage = commandProcessor.getImage("flippedImage");
    assertEquals(expectedFlippedImage, actualFlippedImage);
  }

  @Test
  public void testVerticalFlip() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("vertical-flip testImage flippedImage");
    assertNotNull("Flipped image should not be null", commandProcessor.getImage("flippedImage"));
    ImageRepresentation expectedFlippedImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/flippedVertically.png");
    ImageRepresentation actualFlippedImage = commandProcessor.getImage("flippedImage");
    assertEquals(expectedFlippedImage, actualFlippedImage);
  }

  @Test
  public void testBrighten() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("brighten 10 testImage brightenedImage");
    assertNotNull("Brightened image should not be null",
        commandProcessor.getImage("brightenedImage"));
    ImageRepresentation expectedBrightenedImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/brightenedImage.png");
    ImageRepresentation actualBrightenedImage = commandProcessor.getImage("brightenedImage");
    assertEquals(expectedBrightenedImage, actualBrightenedImage);
  }

  @Test
  public void testRgbSplit() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("rgb-split testImage redImage greenImage blueImage");
    assertNotNull("Red image should not be null", commandProcessor.getImage("redImage"));
    assertNotNull("Green image should not be null", commandProcessor.getImage("greenImage"));
    assertNotNull("Blue image should not be null", commandProcessor.getImage("blueImage"));

    ImageRepresentation expectedRedImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/redComponent.png");
    ImageRepresentation expectedGreenImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/greenComponent.png");
    ImageRepresentation expectedBlueImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/blueComponent.png");

    assertEquals(expectedRedImage, commandProcessor.getImage("redImage"));
    assertEquals(expectedGreenImage, commandProcessor.getImage("greenImage"));
    assertEquals(expectedBlueImage, commandProcessor.getImage("blueImage"));
  }

  @Test
  public void testBlur() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("blur testImage blurredImage");
    assertNotNull("Blurred image should not be null", commandProcessor.getImage("blurredImage"));
    ImageRepresentation expectedBlurredImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/blurredImage.png");
    ImageRepresentation actualBlurredImage = commandProcessor.getImage("blurredImage");
    assertEquals(expectedBlurredImage, actualBlurredImage);
  }

  @Test
  public void testSharpen() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("sharpen testImage sharpenedImage");
    assertNotNull("Sharpened image should not be null",
        commandProcessor.getImage("sharpenedImage"));
    ImageRepresentation expectedSharpenedImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/sharpenedImage.png");
    ImageRepresentation actualSharpenedImage = commandProcessor.getImage("sharpenedImage");
    assertEquals(expectedSharpenedImage, actualSharpenedImage);
  }

  @Test
  public void testApplySepia() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("sepia testImage sepiaImage");
    assertNotNull("Sepia image should not be null", commandProcessor.getImage("sepiaImage"));
    ImageRepresentation expectedSepiaImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/sepiaImage.png");
    ImageRepresentation actualSepiaImage = commandProcessor.getImage("sepiaImage");
    assertEquals(expectedSepiaImage, actualSepiaImage);
  }

  @Test
  public void testApplyGreyscale() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("greyscale testImage greyscaleImage");
    assertNotNull("Greyscale image should not be null",
        commandProcessor.getImage("greyscaleImage"));
    ImageRepresentation expectedGreyscaleImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/greyscaleImage.png");
    ImageRepresentation actualGreyscaleImage = commandProcessor.getImage("greyscaleImage");
    assertEquals(expectedGreyscaleImage, actualGreyscaleImage);
  }

  @Test
  public void testValueComponent() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("value-component testImage valueImage");
    assertNotNull("Value image should not be null", commandProcessor.getImage("valueImage"));
    ImageRepresentation expectedValueImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/valueComponent.png");
    ImageRepresentation actualValueImage = commandProcessor.getImage("valueImage");
    assertEquals(expectedValueImage, actualValueImage);
  }

  @Test
  public void testLumaComponent() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("luma-component testImage lumaImage");
    assertNotNull("Luma image should not be null", commandProcessor.getImage("lumaImage"));
    ImageRepresentation expectedLumaImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/lumaComponent.png");
    ImageRepresentation actualLumaImage = commandProcessor.getImage("lumaImage");
    assertEquals(expectedLumaImage, actualLumaImage);
  }

  @Test
  public void testIntensityComponent() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("intensity-component testImage intensityImage");
    assertNotNull("Intensity image should not be null",
        commandProcessor.getImage("intensityImage"));
    ImageRepresentation expectedIntensityImage = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/intensityComponent.png");
    ImageRepresentation actualIntensityImage = commandProcessor.getImage("intensityImage");
    assertEquals(expectedIntensityImage, actualIntensityImage);
  }

  @Test
  public void testRgbCombine() throws IOException {
    commandProcessor.loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/redComponent.png",
        "redImage");
    commandProcessor.loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/greenComponent.png",
        "greenImage");
    commandProcessor.loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/blueComponent.png",
        "blueImage");
    cliView.processCommand("rgb-combine combinedImage redImage greenImage blueImage");
    assertNotNull("Combined image should not be null", commandProcessor.getImage
        ("combinedImage"));
    ImageRepresentation expectedCombinedImage = loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png");
    ImageRepresentation actualCombinedImage = commandProcessor.getImage("combinedImage");
    assertEquals(expectedCombinedImage, actualCombinedImage);
  }

  @Test
  public void testScriptExecution() throws IOException {
    cliView.processCommand("-file test/com/vanarp/model/TestResources/Script/Script1.txt");
    String expectedOutput =
        "Image loaded successfully.\n"
            + "Image brightened by 100 and saved as bird-brighter\n"
            + "Image flipped vertical and saved as bird-vertical\n"
            + "Image flipped horizontal and saved as bird-vertical-horizontal\n"
            + "Value component applied to the image.\n"
            + "Image saved successfully.\n"
            + "Image saved successfully.\n"
            + "Image loaded successfully.\n"
            + "RGB split completed. Exists in cache as bird-red, bird-green, bird-blue\n"
            + "Image brightened by 50 and saved as bird-red\n"
            + "RGB components combined successfully into 'bird-red-tint'.\n"
            + "Image saved successfully.\n"
            + "Script executed successfully.";

    String actualOutput = outputStream.toString().trim();
    expectedOutput = expectedOutput.replace("\r\n", "\n").replace("\r",
        "\n");
    actualOutput = actualOutput.replace("\r\n", "\n").replace("\r",
        "\n");
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testScriptExecutionAllCommand() throws IOException {
    cliView.processCommand("-file test/com/vanarp/model/TestResources/Script/Script2.txt");
    String expectedOutput =
        "Image loaded successfully.\n"
            + "Red component applied to the image.\n"
            + "Image saved successfully.\n"
            + "Green component applied to the image.\n"
            + "Image saved successfully.\n"
            + "Blue component applied to the image.\n"
            + "Image saved successfully.\n"
            + "Image blured and saved as l1-blur\n"
            + "Image saved successfully.\n"
            + "Image sharpened and saved as l1-sharper\n"
            + "Image saved successfully.\n"
            + "Image brightened by 20 and saved as l1-brighter\n"
            + "Image saved successfully.\n"
            + "RGB split completed. Exists in cache as l1-red-split, l1-green-split, "
            + "l1-blue-split\n"
            + "Image saved successfully.\n"
            + "Image saved successfully.\n"
            + "Image saved successfully.\n"
            + "RGB components combined successfully into 'l1-combine'.\n"
            + "Image saved successfully.\n"
            + "Image flipped vertical and saved as l1-vertical-flip\n"
            + "Image saved successfully.\n"
            + "Image flipped horizontal and saved as l1-horizontal-flip\n"
            + "Image saved successfully.\n"
            + "Image greyscaleed and saved as l1-greyscale\n"
            + "Image saved successfully.\n"
            + "Image sepiaed and saved as l1-sepia\n"
            + "Image saved successfully.\n"
            + "Luma component applied to the image.\n"
            + "Image saved successfully.\n"
            + "Value component applied to the image.\n"
            + "Image saved successfully.\n"
            + "Intensity component applied to the image.\n"
            + "Image saved successfully.\n"
            + "Script executed successfully.";
    String actualOutput = outputStream.toString().trim();
    expectedOutput = expectedOutput.replace("\r\n", "\n")
        .replace("\r", "\n");
    actualOutput = actualOutput.replace("\r\n", "\n")
        .replace("\r", "\n");
    assertEquals(expectedOutput, actualOutput);
  }


  @Test
  public void testInvalidCommand() {
    cliView.processCommand("invalid-command");
    String expectedOutput = "Error in processCommand: Unknown command: invalid-command";
    String actualOutput = outputStream.toString().trim();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testImageCacheBehavior() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    assertNotNull("Image should be cached after loading", commandProcessor
        .getImage("testImage"));

    cliView.processCommand("red-component testImage redImage");
    assertNotNull("Red component image should be cached", commandProcessor
        .getImage("redImage"));
  }

  @Test
  public void testHistogram() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/"
            + "manhattan-small.png",
        "testImage");
    cliView.processCommand("histogram testImage histogramImage");
    assertNotNull("Histogram image should not be null",
        commandProcessor.getImage("histogramImage"));
    ImageRepresentation expectedHistogram = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/ManhattanHistogram.png");
    ImageRepresentation actualHistogram = commandProcessor.getImage("histogramImage");
    assertEquals(expectedHistogram, actualHistogram);
  }

  @Test
  public void testColorCorrect() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/"
            + "manhattan-small.png",
        "testImage");
    cliView.processCommand("color-correct testImage colorCorrectedImage");
    assertNotNull("Color corrected image should not be null",
        commandProcessor.getImage("colorCorrectedImage"));
    ImageRepresentation expectedColorCorrected = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/ColorCorrect.png");
    ImageRepresentation actualColorCorrected = commandProcessor
        .getImage("colorCorrectedImage");
    assertEquals(expectedColorCorrected, actualColorCorrected);
  }

  @Test
  public void testLevelsAdjust() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/"
            + "manhattan-small.png",
        "testImage");
    cliView.processCommand("levels-adjust 20 100 255 testImage adjustedImage");
    assertNotNull("Levels adjusted image should not be null",
        commandProcessor.getImage("adjustedImage"));
    ImageRepresentation expectedAdjusted = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/levelsAdjust.png");
    ImageRepresentation actualAdjusted = commandProcessor.getImage("adjustedImage");
    assertEquals(expectedAdjusted, actualAdjusted);
  }

  @Test
  public void testColorCorrectHistogram() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/Galaxy.png",
        "testImage");
    cliView.processCommand("color-correct testImage colorCorrectedImage");
    cliView.processCommand("histogram colorCorrectedImage histogramImage");
    assertNotNull("Color correct histogram should not be null",
        commandProcessor.getImage("histogramImage"));
    ImageRepresentation expectedHistogram = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/colorCorrectHistogram.png");
    ImageRepresentation actualHistogram = commandProcessor.getImage("histogramImage");
    assertEquals(expectedHistogram, actualHistogram);
  }

  @Test
  public void testAdjustValuesHistogram() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/Galaxy.png",
        "testImage");
    cliView.processCommand("levels-adjust 20 100 255 testImage adjustedImage ");
    cliView.processCommand("histogram adjustedImage histogramImage");
    assertNotNull("Adjusted values histogram should not be null",
        commandProcessor.getImage("histogramImage"));
    ImageRepresentation expectedHistogram = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/AdjustedValuesHistogram.png");
    ImageRepresentation actualHistogram = commandProcessor.getImage("histogramImage");
    assertEquals(expectedHistogram, actualHistogram);
  }

  @Test
  public void testBlurSplit() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/"
            + "manhattan-small.png",
        "testImage");
    cliView.processCommand("blur testImage blurredImageSplit split 25");
    assertNotNull("Split blur image should not be null",
        commandProcessor.getImage("blurredImageSplit"));
    ImageRepresentation expectedSplit = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/Split25.png");
    ImageRepresentation actualSplit = commandProcessor.getImage("blurredImageSplit");
    assertEquals(expectedSplit, actualSplit);
  }

  @Test
  public void testLevelAdjustSplit() throws IOException {
    commandProcessor.loadImage("test/com/vanarp/model/TestResources/Source/"
            + "manhattan-small.png",
        "testImage");
    cliView.processCommand("levels-adjust 20 100 255 testImage adjustedImageSplit split 70");
    assertNotNull("Split levels adjust image should not be null",
        commandProcessor.getImage("adjustedImageSplit"));
    ImageRepresentation expectedSplit = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/Split70.png");
    ImageRepresentation actualSplit = commandProcessor.getImage("adjustedImageSplit");
    assertEquals(expectedSplit, actualSplit);
  }

  @Test
  public void testCompress() throws IOException {
    commandProcessor.loadImage(
        "test/com/vanarp/model/TestResources/Source/bird.png",
        "testImage");
    cliView.processCommand("compress 100 testImage compressedImage");
    assertNotNull("Compressed image should not be null",
        commandProcessor.getImage("compressedImage"));
    ImageRepresentation expectedSplit = loadImage(
        "test/com/vanarp/model/TestResources/SampleOperations/bird-compress.png");
    ImageRepresentation actualSplit = commandProcessor.getImage("compressedImage");
    assertEquals(expectedSplit, actualSplit);
  }
}