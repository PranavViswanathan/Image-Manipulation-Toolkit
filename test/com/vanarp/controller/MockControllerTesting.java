package com.vanarp.controller;

import static org.junit.Assert.assertEquals;

import com.vanarp.model.ImageOperations;
import com.vanarp.model.ImageRepresentation;
import com.vanarp.model.PixelInterface;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.junit.Test;

/**
 * This class contains unit tests for the CommandProcessor and CLIView using a mock implementation
 * of the ImageOperations interface. The tests validate the behavior of various image processing
 * commands and ensure that the correct operations are logged.
 *
 * <p>The mock implementation simulates the behavior of image operations without performing actual
 * image processing, allowing for isolated testing of command handling and output logging.
 */
public class MockControllerTesting {

  CommandProcessor commandProcessor;
  CLIView cliView;

  static class MockImageOperations implements ImageOperations {

    private final StringBuilder log;
    private final ImageRepresentation testImage;

    public MockImageOperations(StringBuilder log) {
      this.log = log;
      this.testImage = new ImageRepresentation() {
        private final PixelInterface[][] pixels = new PixelInterface[100][100];

        @Override
        public void setPixel(int x, int y, PixelInterface pixel) {
          if (x >= 0 && x < pixels.length && y >= 0 && y < pixels[0].length) {
            pixels[x][y] = pixel;
          } else {
            log.append("Attempted to set pixel out of bounds: (").append(x).append(", ").append(y)
                .append(")").append(System.lineSeparator());
          }
        }

        @Override
        public PixelInterface getPixel(int x, int y) {
          if (x >= 0 && x < pixels.length && y >= 0 && y < pixels[0].length) {
            return pixels[x][y];
          } else {
            log.append("Attempted to get pixel out of bounds: (").append(x).append(", ").append(y)
                .append(")").append(System.lineSeparator());
            return null;
          }
        }

        @Override
        public int getWidth() {
          return pixels.length;
        }

        @Override
        public int getHeight() {
          return pixels[0].length;
        }

        @Override
        public BufferedImage toBufferedImage() {
          return null;
        }
      };
    }


    @Override
    public ImageRepresentation redComponent(ImageRepresentation image) {
      log.append("Red component applied to the image.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation greenComponent(ImageRepresentation image) throws IOException {
      log.append("Green component applied to the image.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation blueComponent(ImageRepresentation image) throws IOException {
      log.append("Blue component applied to the image.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation valueComponent(ImageRepresentation image) throws IOException {
      log.append("Value component applied to the image.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation lumaComponent(ImageRepresentation image) throws IOException {
      log.append("Luma component applied to the image.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation intensityComponent(ImageRepresentation image) throws IOException {
      log.append("Intensity component applied to the image.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation flipHorizontally(ImageRepresentation image) throws IOException {
      log.append("Image flipped horizontally.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation flipVertically(ImageRepresentation image) throws IOException {
      log.append("Image flipped vertically.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation brightenImage(ImageRepresentation image, int increment)
        throws IOException {
      log.append("Image brightened by ").append(increment).append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation[] rgbSplit(ImageRepresentation image) throws IOException {
      log.append("RGB split applied.").append(System.lineSeparator());
      return new ImageRepresentation[]{image, image, image};
    }

    @Override
    public ImageRepresentation applySepia(ImageRepresentation image) throws IOException {
      log.append("Sepia applied to the image.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation blur(ImageRepresentation image) throws IOException {
      log.append("Image blurred.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation sharpen(ImageRepresentation image) throws IOException {
      log.append("Image sharpened.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation applyGreyScale(ImageRepresentation image) throws IOException {
      log.append("Greyscale applied to the image.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation combineRgb(ImageRepresentation redImage,
        ImageRepresentation greenImage, ImageRepresentation blueImage) throws IOException {
      log.append("RGB combined.").append(System.lineSeparator());
      return testImage;
    }

    @Override
    public ImageRepresentation levelsAdjust(ImageRepresentation image, int b, int m, int w)
        throws IOException {
      log.append("Levels adjusted with b: ").append(b).append(", m: ").append(m).append(", w: ")
          .append(w).append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation colorCorrect(ImageRepresentation image) throws IOException {
      log.append("Color correction applied to the image.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation getHistogram(ImageRepresentation image) throws IOException {
      log.append("Histogram generated for the image.").append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation splitImages(ImageRepresentation image1, ImageRepresentation image2,
        int percent) throws IOException {
      log.append("Images split with percent: ").append(percent).append(System.lineSeparator());
      return image1;
    }

    @Override
    public ImageRepresentation compressImage(ImageRepresentation image, float quality)
        throws IOException {
      log.append("Image compressed with quality: ").append(quality).append("%")
          .append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation downscaleImage(ImageRepresentation image, int newWidth,
        int newHeight) throws IOException {
      log.append("Image downscaled to width: ").append(newWidth).append(", height: ")
          .append(newHeight)
          .append(System.lineSeparator());
      return image;
    }

    @Override
    public ImageRepresentation applySharpenWithMask(ImageRepresentation sourceImageName,
        ImageRepresentation maskImageName) throws IOException {
      log.append("Sharpen applied with mask to the image.").append(System.lineSeparator());
      return sourceImageName;
    }

    @Override
    public ImageRepresentation applySepiaWithMask(ImageRepresentation sourceImageName,
        ImageRepresentation maskImageName) throws IOException {
      log.append("Sepia applied with mask to the image.").append(System.lineSeparator());
      return sourceImageName;
    }

    @Override
    public ImageRepresentation applyGreyscaleWithMask(ImageRepresentation sourceImageName,
        ImageRepresentation maskImageName) throws IOException {
      log.append("Greyscale applied with mask to the image.").append(System.lineSeparator());
      return sourceImageName;
    }

    @Override
    public ImageRepresentation applyBlurWithMask(ImageRepresentation sourceImageName,
        ImageRepresentation maskImageName) throws IOException {
      log.append("Blur applied with mask to the image.").append(System.lineSeparator());
      return sourceImageName;
    }

    @Override
    public ImageRepresentation blueComponentWithMask(ImageRepresentation sourceImage,
        ImageRepresentation maskImage) throws IOException {
      log.append("Blue component applied with mask to the image.").append(System.lineSeparator());
      return sourceImage;
    }

    @Override
    public ImageRepresentation redComponentWithMask(ImageRepresentation sourceImage,
        ImageRepresentation maskImage) throws IOException {
      log.append("Red component applied with mask to the image.").append(System.lineSeparator());
      return sourceImage;
    }

    @Override
    public ImageRepresentation greenComponentWithMask(ImageRepresentation sourceImage,
        ImageRepresentation maskImage) throws IOException {
      log.append("Green component applied with mask to the image.").append(System.lineSeparator());
      return sourceImage;
    }

    @Override
    public ImageRepresentation lumaComponentWithMask(ImageRepresentation sourceImage,
        ImageRepresentation maskImage) throws IOException {
      log.append("Luma component applied with mask to the image.").append(System.lineSeparator());
      return sourceImage;
    }

    @Override
    public ImageRepresentation intensityComponentWithMask(ImageRepresentation sourceImage,
        ImageRepresentation maskImage) throws IOException {
      log.append("Intensity component applied with mask to the image.")
          .append(System.lineSeparator());
      return sourceImage;
    }

    @Override
    public ImageRepresentation valueComponentWithMask(ImageRepresentation sourceImage,
        ImageRepresentation maskImage) throws IOException {
      log.append("Value component applied with mask to the image.").append(System.lineSeparator());
      return sourceImage;
    }
  }

  @Test
  public void testRedComponentMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "red-component testImage redImage\n");
    assertEquals("Red component applied to the image."
            + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testGreenComponentMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "green-component testImage greenImage\n");
    assertEquals("Green component applied to the image."
            + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testBlueComponentMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "blue-component testImage blueImage\n");
    assertEquals("Blue component applied to the image."
            + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testRgbSplitMock() throws IOException {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "rgb-split testImage redImage greenImage blueImage\n");
    assertEquals("RGB split applied."
        + System.lineSeparator(), log.toString());
  }

  @Test
  public void testValueComponentMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage \n "
            + "value-component testImage ValueImage");
    assertEquals("Value component applied to the image."
            + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testIntensityComponentMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage \n "
            + "intensity-component testImage IntensityImage");
    assertEquals("Intensity component applied to the image."
        + System.lineSeparator(), log.toString());
  }

  @Test
  public void testLumaComponentMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage \n "
            + "luma-component testImage LumaImage");
    assertEquals("Luma component applied to the image."
            + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testBrightenImage() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "brighten 10 testImage brightenedImage\n");
    assertEquals("Image brightened by 10" + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testFlipHorizontally() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "horizontal-flip testImage flippedImage\n");
    assertEquals("Image flipped horizontally." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testFlipVertically() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "vertical-flip testImage flippedImage\n");
    assertEquals("Image flipped vertically." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testApplySepia() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "sepia testImage sepiaImage\n");
    assertEquals("Sepia applied to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testBlur() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\nblur testImage "
            + "blurredImage\n");
    assertEquals("Image blurred." + System.lineSeparator(), log.toString());
  }

  @Test
  public void testSharpen() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "sharpen testImage sharpenedImage\n");
    assertEquals("Image sharpened." + System.lineSeparator(), log.toString());
  }

  @Test
  public void testApplyGreyScale() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "greyscale testImage greyImage\n");
    assertEquals("Greyscale applied to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testLevelsAdjust() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "levels-adjust 0 1 255 testImage outputImage\n");
    assertEquals("Levels adjusted with b: 0, m: 1, w: 255"
        + System.lineSeparator(), log.toString());
  }

  @Test
  public void testColorCorrect() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "color-correct testImage correctedImage\n");
    assertEquals("Color correction applied to the image."
        + System.lineSeparator(), log.toString());
  }

  @Test
  public void testGetHistogram() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "histogram testImage newImage\n");
    assertEquals("Histogram generated for the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testBlurSplit() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n "
            + "blur testImage blurImageSplit split 50 \n");
    assertEquals("Image blurred."
        + System.lineSeparator()
        + "Images split with percent: 50"
        + System.lineSeparator(), log.toString());
  }

  @Test
  public void testSharpenSplit() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n "
            + "sharpen testImage sharpenedImageSplit split 50 \n");
    assertEquals("Image sharpened."
        + System.lineSeparator()
        + "Images split with percent: 50"
        + System.lineSeparator(), log.toString());
  }

  @Test
  public void testSepiaSplit() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage \n "
            + "sepia testImage sepiaImageSplit split 50 \n");
    assertEquals("Sepia applied to the image."
        + System.lineSeparator()
        + "Images split with percent: 50"
        + System.lineSeparator(), log.toString());
  }

  @Test
  public void testGreyscaleSplit() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage \n "
            + "greyscale testImage greyscaleImageSplit split 50 \n");
    assertEquals("Greyscale applied to the image."
        + System.lineSeparator()
        + "Images split with percent: 50"
        + System.lineSeparator(), log.toString());
  }


  @Test
  public void testColorCorrectionSplit() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage \n "
            + "color-correct testImage colorCorrectSplit split 50 \n");
    assertEquals("Color correction applied to the image."
        + System.lineSeparator()
        + "Images split with percent: 50"
        + System.lineSeparator(), log.toString());
  }

  /*
    @Test
    public void testRGBCombine() {
      StringBuilder log = getOutput(
          "load test/com/vanarp/model/TestResources/SampleOperations/redComponent.png red\n"
              + "load test/com/vanarp/model/TestResources/SampleOperations/greenComponent.png
              + green\n"
              + "load test/com/vanarp/model/TestResources/SampleOperations/blueComponent.png blue\n"
              + "rgb-combine bird red green blue\n");
      assertEquals("RGB combined."
          + System.lineSeparator(), log.toString());
    }
  */
  @Test
  public void testLevelsAdjustmentSplit() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n "
            + "levels-adjust 20 100 200 testImage leveladjustbirdSplit split 50 \n");
    assertEquals("Levels adjusted with b: 20, m: 100, w: 200"
            + System.lineSeparator()
            + "Images split with percent: 50" + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testSplitImages() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "sharpen testImage testImagesharpensplit split 50\n");
    assertEquals("Image sharpened."
        + System.lineSeparator()
        + "Images split with percent: 50"
        + System.lineSeparator(), log.toString());
  }

  @Test
  public void testUnknownCommand() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "unknown-command\n");
    assertEquals("", log.toString());
  }

  @Test
  public void testInvalidCommand() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage\n"
            + "save testImage\n");
    assertEquals("", log.toString());
  }

  @Test
  public void testCompress() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png testImage \n "
            + "compress 10 testImage CompressedTest \n");
    assertEquals("Image compressed with quality: 10.0%"
            + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testRunScriptFileParsing() {
    StringBuilder log = getOutput(
        "-file test/com/vanarp/model/TestResources/Script/Script1.txt \n");

    String expectedOutput = String.join(System.lineSeparator(),
        "Image brightened by 100", "Image flipped vertically.", "Image flipped horizontally.",
        "Value component applied to the image.", "RGB split applied.",
        "Image brightened by 50", "RGB combined.",
        "Exception occurred: Cannot invoke \"com.vanarp.model.PixelInterface.getRed()\" "
            + "because \"pixel\" is null",
        ""
    );
    assertEquals(expectedOutput, log.toString());
  }

  @Test
  public void testApplySharpenWithMaskMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png sourceImage\n"
            + "load test/com/vanarp/model/TestResources/Source/bird-masked.png maskImage\n"
            + "sharpen sourceImage maskImage sharpenedImage\n");
    assertEquals("Sharpen applied with mask to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testApplySepiaWithMaskMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png sourceImage\n"
            + "load test/com/vanarp/model/TestResources/Source/bird-masked.png maskImage\n"
            + "sepia sourceImage maskImage sepiaImage\n");
    assertEquals("Sepia applied with mask to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testApplyGreyscaleWithMaskMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png sourceImage\n"
            + "load test/com/vanarp/model/TestResources/Source/bird-masked.png maskImage\n"
            + "greyscale sourceImage maskImage greyscaleImage\n");
    assertEquals("Greyscale applied with mask to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testApplyBlurWithMaskMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png sourceImage\n"
            + "load test/com/vanarp/model/TestResources/Source/bird-masked.png maskImage\n"
            + "blur sourceImage maskImage blurredImage\n");
    assertEquals("Blur applied with mask to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testBlueComponentWithMaskMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png sourceImage\n"
            + "load test/com/vanarp/model/TestResources/Source/bird-masked.png maskImage\n"
            + "blue-component sourceImage maskImage blueImage\n");
    assertEquals("Blue component applied with mask to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testRedComponentWithMaskMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png sourceImage\n"
            + "load test/com/vanarp/model/TestResources/Source/bird-masked.png maskImage\n"
            + "red-component sourceImage maskImage redImage\n");
    assertEquals("Red component applied with mask to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testGreenComponentWithMaskMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png sourceImage\n"
            + "load test/com/vanarp/model/TestResources/Source/bird-masked.png maskImage\n"
            + "green-component sourceImage maskImage greenImage\n");
    assertEquals("Green component applied with mask to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testLumaComponentWithMaskMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png sourceImage\n"
            + "load test/com/vanarp/model/TestResources/Source/bird-masked.png maskImage\n"
            + "luma-component sourceImage maskImage lumaImage\n");
    assertEquals("Luma component applied with mask to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testIntensityComponentWithMaskMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png sourceImage\n"
            + "load test/com/vanarp/model/TestResources/Source/bird-masked.png maskImage\n"
            + "intensity-component sourceImage maskImage intensityImage\n");
    assertEquals("Intensity component applied with mask to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testValueComponentWithMaskMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png sourceImage\n"
            + "load test/com/vanarp/model/TestResources/Source/bird-masked.png maskImage\n"
            + "value-component sourceImage maskImage valueImage\n");
    assertEquals("Value component applied with mask to the image." + System.lineSeparator(),
        log.toString());
  }

  @Test
  public void testDownscaleImageMock() {
    StringBuilder log = getOutput(
        "load test/com/vanarp/model/TestResources/Source/bird.png sourceImage\n"
            + "downscale sourceImage downscaledImage 100 100 \n");
    assertEquals("Image downscaled to width: 100, height: 100" + System.lineSeparator(),
        log.toString());
  }

  private StringBuilder getOutput(String s) {
    StringBuilder log = new StringBuilder();
    MockImageOperations mockImageOperations = new MockImageOperations(log);
    commandProcessor = new CommandProcessor(mockImageOperations);
    cliView = new CLIView(commandProcessor);

    try {
      String[] commands = s.split("\n");
      for (String command : commands) {
        command = command.trim();
        if (!command.isEmpty()) {
          System.out.println("Processing command: " + command);
          cliView.processCommand(command);
        }
      }
    } catch (Exception e) {
      log.append("Exception occurred: ").append(e.getMessage()).append(System.lineSeparator());
    }
    return log;
  }
}