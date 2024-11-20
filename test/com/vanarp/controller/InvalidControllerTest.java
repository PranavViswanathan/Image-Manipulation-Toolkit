package com.vanarp.controller;

import static org.junit.Assert.assertEquals;

import com.vanarp.model.Filtering;
import com.vanarp.model.ImageCompression;
import com.vanarp.model.ImageCompressionFunctionality;
import com.vanarp.model.ImageMaskingFiltering;
import com.vanarp.model.ImageOperations;
import com.vanarp.model.ImageTransformationEnhanced;
import com.vanarp.model.Operations;
import com.vanarp.model.Transform;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;

/**
 * This class contains unit tests for handling invalid scenarios in the Image Command Processor and
 * CLI View. It tests various edge cases and error conditions to ensure that the system behaves as
 * expected when invalid commands or parameters are provided.
 *
 * <p>The tests validate that appropriate error messages are returned for invalid inputs,
 * unsupported file formats, and incorrect command usage.
 */
public class InvalidControllerTest {

  private CLIView cliView;
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    ImageTransformationEnhanced transform = new Transform();
    ImageMaskingFiltering filter = new Filtering();
    ImageFileIO compressedIO = new CompressedImageIO();
    ImageFileIO uncompressedIO = new UncompressedImageIO();
    ImageCompressionFunctionality compression = new ImageCompression();
    ImageOperations operations = new Operations(transform, filter, compressedIO, uncompressedIO,
        compression);

    ImageCommandProcessor commandProcessor = new CommandProcessor(operations);
    cliView = new CLIView(commandProcessor);

    System.setOut(new PrintStream(outputStream));
  }

  @Test
  public void loadNoImage() {
    cliView.processCommand("load noImage.png n");
    String expectedOutput = "Error in handleLoad: Can't read input file!";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void loadIllegalNumberArgs() {
    cliView.processCommand("load n");
    String expectedOutput = "Error in handleLoad: Usage: load <image-path> <image-name>";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void loadInvalidImageExtension() {
    cliView.processCommand("load n.exe n");
    String expectedOutput = "Error in handleLoad: Unsupported file format";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test(expected = IllegalArgumentException.class)
  public void saveInvalidImageExtension() {
    cliView.processCommand("save n.exe n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void saveNoImage() {
    cliView.processCommand("load test/com/vanarp/model/TestResources/Source/bird.png testImage");
    cliView.processCommand("save n.png n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void redCompSourceImageNotExists() {
    cliView.processCommand("load test/com/vanarp/model/TestResources/Source/bird.png testImage");
    cliView.processCommand("red-component a b");
  }

  @Test(expected = IllegalArgumentException.class)
  public void greenCompSourceImageNotExists() {
    cliView.processCommand("load test/com/vanarp/model/TestResources/Source/bird.png testImage");
    cliView.processCommand("green-component a b");
  }

  @Test(expected = IllegalArgumentException.class)
  public void blueCompSourceImageNotExists() {
    cliView.processCommand("load test/com/vanarp/model/TestResources/Source/bird.png testImage");
    cliView.processCommand("blue-component a b");
  }

  @Test
  public void testInvalidCommand() {
    cliView.processCommand("invalid-command");
    String expectedOutput = "Error in processCommand: Unknown command: invalid-command";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHorizontalFlipSourceImageNotExists() {
    cliView.processCommand("load test/com/vanarp/model/TestResources/Source/bird.png testImage");
    cliView.processCommand("horizontal-flip nonExistentImage flippedImage");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVerticalFlipSourceImageNotExists() {
    cliView.processCommand("load test/com/vanarp/model/TestResources/Source/bird.png testImage");
    cliView.processCommand("vertical-flip nonExistentImage flippedImage");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBrightenSourceImageNotExists() {
    cliView.processCommand("load test /com/vanarp/model/TestResources/Source/bird.png testImage");
    cliView.processCommand("brighten 10 nonExistentImage brightenedImage");

  }

  @Test(expected = IllegalArgumentException.class)
  public void testRgbSplitSourceImageNotExists() {
    cliView.processCommand("load test/com/vanarp/model/TestResources/Source/bird.png testImage");
    cliView.processCommand("rgb-split nonExistentImage redImage greenImage blueImage");
  }

  @Test
  public void testScriptExecutionWithInvalidCommand() {
    cliView.processCommand("-file test/com/vanarp/model/TestResources/Script/InvalidScript.txt");
    String expectedOutput = "Error in handleScript: test\\com\\vanarp\\model\\TestResources\\Script\\InvalidScript.txt (The system cannot find the file specified)";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testBlurSourceImageNotExists() {
    cliView.processCommand("blur nonExistentImage blurredImage");
    String expectedOutput = "Error in handleFilter: Image with name nonExistentImage not found.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testSharpenSourceImageNotExists() {
    cliView.processCommand("sharpen nonExistentImage sharpenedImage");
    String expectedOutput = "Error in handleFilter: Image with name nonExistentImage not found.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testSepiaSourceImageNotExists() {
    cliView.processCommand("sepia nonExistentImage sepiaImage");
    String expectedOutput = "Error in handleFilter: Image with name nonExistentImage not found.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testGreyscaleSourceImageNotExists() {
    cliView.processCommand("greyscale nonExistentImage greyImage");
    String expectedOutput = "Error in handleFilter: Image with name nonExistentImage not found.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testHistogramSourceImageNotExists() {
    cliView.processCommand("histogram nonExistentImage");
    String expectedOutput = "Error in handleHistogram: Usage: histogram <image-name> <dest-image-name>";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testColorCorrectSourceImageNotExists() {
    cliView.processCommand("color-correct nonExistentImage correctedImage");
    String expectedOutput = "Error in handleColorCorrect: Image with name nonExistentImage not found.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testLevelsAdjustSourceImageNotExists() {
    cliView.processCommand("levels-adjust nonExistentImage adjustedImage");
    String expectedOutput = "Error in handleLevelsAdjust: Usage: levels-adjust <b> <m> <w> <image-name> <dest-image-name> [split <percentage>]";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testCompressSourceImageNotExists() {
    cliView.processCommand("compress nonExistentImage compressedImage");
    String expectedOutput = "Error in handleCompress: Usage: compress <percentage> <image-name> <dest-image-name>";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testRgbCombineMissingChannels() {
    cliView.processCommand("rgb-combine redChannelImage greenChannelImage");
    String expectedOutput = "Error in handleRgbCombine: Usage: rgb-combine <image-name> <red-image> <green-image> <blue-image>";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testBrightenWithInvalidIncrement() {
    cliView.processCommand("brighten ten imageName brightenedImage");
    String expectedOutput = "Error in handleBrighten: Invalid increment value. It should be an integer.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testHandleLoadIncorrectFilePath() {
    cliView.processCommand("load incorrect/path/image.png imageName");
    String expectedOutput = "Failed to load image: Can't read input file!";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testPPMCompression() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/P3.ppm loaded-image.ppm");
    cliView.processCommand("compress 70 loaded-image.ppm compress-loaded-image");
    String expectedOutput = "Image loaded successfully." + System.lineSeparator()
        + "Compression is not supported for PPM format. Please use JPG or PNG files.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testHistogramPeakBoundaries() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("histogram testImage histogramImage");
    String expectedOutput = "Image loaded successfully." + System.lineSeparator()
        + "Histogram created from testImage and saved as histogramImage";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testColorCorrectWithExtremePeaks() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/goat.jpg " + "testImage");
    cliView.processCommand("color-correct testImage correctedImage");
    String expectedOutput = "Image loaded successfully." + System.lineSeparator()
        + "Color correction applied and saved as correctedImage";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHistogramWithInvalidValues() {
    cliView.processCommand("histogram nonexistentImage histogramOutput");
  }

  @Test
  public void testColorCorrectWithInvalidValues() {
    cliView.processCommand("color-correct testImage");
    String expectedOutput =
        "Usage: color-correct <image-name> <dest-image-name> " + "[split <percentage>]";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testLevelsAdjustValidInput() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("levels-adjust 20 128 235 testImage adjustedImage");
    String expectedOutput = "Image loaded successfully." + System.lineSeparator()
        + "Levels adjusted and saved as adjustedImage";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustInvalidOrder() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("levels-adjust 128 20 235 testImage adjustedImage");
    String expectedOutput = "Values must be in ascending order: shadow < mid < highlight.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustOutOfRange() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("levels-adjust -10 128 300 testImage adjustedImage");
    String expectedOutput = "Shadow, mid, and highlight values must be between 0 and 255.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testLevelsAdjustInvalidNumberOfArguments() {
    cliView.processCommand("levels-adjust 20 128 testImage adjustedImage");
    String expectedOutput =
        "Usage: levels-adjust <b> <m> <w> <image-name> <dest-image-name> " + "[split <percentage>]";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustNonExistentImage() {
    cliView.processCommand("levels-adjust 20 128 235 nonExistentImage adjustedImage");
    String expectedOutput = "Image with name nonExistentImage not found.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testLevelsAdjustInvalidValueType() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("levels-adjust 20.5 128 235 testImage adjustedImage");
    String expectedOutput = "Image loaded successfully." + System.lineSeparator()
        + "Invalid level values. They should be integers.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testLevelsAdjustExtremeCases() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("levels-adjust 0 128 255 testImage adjustedImage");
    String expectedOutput = "Image loaded successfully." + System.lineSeparator()
        + "Levels adjusted and saved as adjustedImage";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlurWithSplitGreaterThan100() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("blur testImage blurredImage split 150");
    String expectedOutput = "Image loaded successfully." + System.lineSeparator()
        + "Invalid split percentage: must be between 0 and 100";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testBlurWithNegativeSplit() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("blur testImage blurredImage split -50");
    String expectedOutput = "Image loaded successfully." + System.lineSeparator()
        + "Error in handleFilter: Percentage must be between 0 and 100.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlurWithNonNumericSplit() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("blur testImage blurredImage split abc");

    String expectedOutput =
        "Image loaded successfully." + System.lineSeparator() + "For input string: \"abc\"";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testCompressWithZeroPercentage() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("compress 0 testImage compressedImage");
    String expectedOutput = "Image loaded successfully." + System.lineSeparator()
        + "Image compressed and saved as compressedImage";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testCompressWithNegativePercentage() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("compress -10 testImage compressedImage");
    String expectedOutput = "Image loaded successfully." + System.lineSeparator()
        + "Compression percentage must be between 0 and 100.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }

  @Test
  public void testCompressWithGreaterThan100Percentage() {
    cliView.processCommand(
        "load test/com/vanarp/model/TestResources/Source/bird.png " + "testImage");
    cliView.processCommand("compress 150 testImage compressedImage");
    String expectedOutput = "Image loaded successfully." + System.lineSeparator()
        + "Compression percentage must be between 0 and 100.";
    assertEquals(expectedOutput, outputStream.toString().trim());
  }
}