package com.vanarp.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.vanarp.model.ImageRepresentation;
import com.vanarp.model.PixelInterface;
import com.vanarp.viewer.GUIView;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import org.junit.Before;
import org.junit.Test;

public class GUIControllerTest {

  private GUIView view;
  private ImageCommandProcessor commandProcessor;

  private static class TestView extends GUIView {

    private BufferedImage image;

    @Override
    public void setImageIcon(ImageIcon icon) {
      this.image = (BufferedImage) icon.getImage();
    }

    public BufferedImage getImage() {
      return image;
    }
  }

  private static class TestCommandProcessor implements ImageCommandProcessor {

    private String loadedImagePath;
    private String savedImagePath;
    private String currentImageName;
    private String extractedComponentType;
    private String extractedComponentDestName;
    private String appliedFilterType;
    private String appliedFilterDestName;
    private String flippedDirection;
    private String flippedImageDestName;
    private int brightenedIncrement;
    private String brightenedImageDestName;
    private String rgbSplitRedName;
    private String rgbSplitGreenName;
    private String rgbSplitBlueName;
    private String rgbCombineDestName;
    private String rgbCombineRedName;
    private String rgbCombineGreenName;
    private String rgbCombineBlueName;
    private int levelsAdjustBrightness;
    private int levelsAdjustMidtone;
    private int levelsAdjustWhitePoint;
    private String levelsAdjustDestName;
    private String colorCorrectDestName;
    private String histogramDestName;
    private float compressPercentage;
    private String compressImageDestName;
    private String downscaleImageDestName;
    private int downscaleNewWidth;
    private int downscaleNewHeight;

    @Override
    public void loadImage(String filePath, String imageName) {
      loadedImagePath = filePath;
      currentImageName = imageName;
    }

    @Override
    public void saveImage(String imageName, String filePath, String format) {
      savedImagePath = filePath;
    }

    @Override
    public ImageRepresentation getImage(String imageName) {
      return new ImageRepresentation() {
        @Override
        public void setPixel(int x, int y, PixelInterface pixel) {
        }

        @Override
        public PixelInterface getPixel(int x, int y) {
          return null;
        }

        @Override
        public int getWidth() {
          return 1;
        }

        @Override
        public int getHeight() {
          return 1;
        }

        @Override
        public BufferedImage toBufferedImage() {
          return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        }
      };
    }

    @Override
    public void extractComponent(String imageName, String destName, String componentType,
        String maskName) throws IOException {
      extractedComponentType = componentType;
      extractedComponentDestName = destName;
    }

    @Override
    public void applyFilter(String imageName, String destName, String filterType,
        Integer splitPercent, String maskImageName) throws IOException {
      appliedFilterType = filterType;
      appliedFilterDestName = destName;
    }

    @Override
    public void flipImage(String imageName, String destName, String direction) throws IOException {
      flippedDirection = direction;
      flippedImageDestName = destName;
    }

    @Override
    public void brightenImage(String imageName, int increment, String destName) throws IOException {
      brightenedIncrement = increment;
      brightenedImageDestName = destName;
    }

    @Override
    public void rgbSplit(String imageName, String redName, String greenName, String blueName)
        throws IOException {
      rgbSplitRedName = redName;
      rgbSplitGreenName = greenName;
      rgbSplitBlueName = blueName;
    }

    @Override
    public void rgbCombine(String destName, String redImageName, String greenImageName,
        String blueImageName) throws IOException {
      rgbCombineDestName = destName;
      rgbCombineRedName = redImageName;
      rgbCombineGreenName = greenImageName;
      rgbCombineBlueName = blueImageName;
    }

    @Override
    public void levelsAdjust(String imageName, int brightness, int midtone, int whitePoint,
        String destName, Integer splitPercent) throws IOException {
      levelsAdjustBrightness = brightness;
      levelsAdjustMidtone = midtone;
      levelsAdjustWhitePoint = whitePoint;
      levelsAdjustDestName = destName;
    }

    @Override
    public void colorCorrectImage(String imageName, String destName, Integer splitPercent)
        throws IOException {
      colorCorrectDestName = destName;
    }

    @Override
    public void processSplitOperation(String operation, String imageName, String destName,
        int splitPercent, Integer... params) throws IOException {
      // Implement logic to capture split operation parameters if needed
    }

    @Override
    public void getHistogram(String imageName, String destName) throws IOException {
      histogramDestName = destName;
    }

    @Override
    public void compressImage(float percentage, String imageName, String destName)
        throws IOException {
      compressPercentage = percentage;
      compressImageDestName = destName;
    }

    @Override
    public void downscaleImage(String imageName, String destName, int newWidth, int newHeight)
        throws IOException {
      downscaleImageDestName = destName;
      downscaleNewWidth = newWidth;
      downscaleNewHeight = newHeight;
    }
  }

  @Before
  public void setUp() {
    view = new TestView();
    commandProcessor = new TestCommandProcessor();
    GUIController controller = new GUIController(commandProcessor, view);
  }

  @Test
  public void testLoadImage() throws IOException {
    String testFilePath = "test_image.ppm";
    String testImageName = "Test Image";

    commandProcessor.loadImage(testFilePath, testImageName);

    assertEquals(testFilePath, ((TestCommandProcessor) commandProcessor).loadedImagePath);
    assertEquals(testImageName, ((TestCommandProcessor) commandProcessor).currentImageName);
  }

  @Test
  public void testSaveImage() throws IOException {
    String testImageName = "Test Image";
    String testFilePath = "saved_image.png";

    commandProcessor.saveImage(testImageName, testFilePath, "png");

    assertEquals(testFilePath, ((TestCommandProcessor) commandProcessor).savedImagePath);
  }

  @Test
  public void testExtractRedComponent() throws IOException {
    String testImageName = "Test Image";
    String destName = "Red Component";

    commandProcessor.extractComponent(testImageName, destName, "red", null);

    assertEquals("red", ((TestCommandProcessor) commandProcessor).extractedComponentType);
    assertEquals(destName, ((TestCommandProcessor) commandProcessor).extractedComponentDestName);
  }

  @Test
  public void testApplyFilter() throws IOException {
    String testImageName = "Test Image";
    String destName = "Filtered Image";
    String filterType = "blur";

    commandProcessor.applyFilter(testImageName, destName, filterType, null, null);

    assertEquals(filterType, ((TestCommandProcessor) commandProcessor).appliedFilterType);
    assertEquals(destName, ((TestCommandProcessor) commandProcessor).appliedFilterDestName);
  }

  @Test
  public void testFlipImage() throws IOException {
    String testImageName = "Test Image";
    String destName = "Flipped Image";

    commandProcessor.flipImage(testImageName, destName, "horizontal");

    assertEquals("horizontal", ((TestCommandProcessor) commandProcessor).flippedDirection);
    assertEquals(destName, ((TestCommandProcessor) commandProcessor).flippedImageDestName);
  }

  @Test
  public void testBrightenImage() throws IOException {
    String testImageName = "Test Image";
    String destName = "Brightened Image";
    int increment = 10;

    commandProcessor.brightenImage(testImageName, increment, destName);

    assertEquals(increment, ((TestCommandProcessor) commandProcessor).brightenedIncrement);
    assertEquals(destName, ((TestCommandProcessor) commandProcessor).brightenedImageDestName);
  }

  @Test
  public void testRgbSplit() throws IOException {
    String testImageName = "Test Image";
    String redName = "Red Image";
    String greenName = "Green Image";
    String blueName = "Blue Image";

    commandProcessor.rgbSplit(testImageName, redName, greenName, blueName);

    assertEquals(redName, ((TestCommandProcessor) commandProcessor).rgbSplitRedName);
    assertEquals(greenName, ((TestCommandProcessor) commandProcessor).rgbSplitGreenName);
    assertEquals(blueName, ((TestCommandProcessor) commandProcessor).rgbSplitBlueName);
  }

  @Test
  public void testRgbCombine() throws IOException {
    String destName = "Combined Image";
    String redImageName = "Red Image";
    String greenImageName = "Green Image";
    String blueImageName = "Blue Image";

    commandProcessor.rgbCombine(destName, redImageName, greenImageName, blueImageName);

    assertEquals(destName, ((TestCommandProcessor) commandProcessor).rgbCombineDestName);
    assertEquals(redImageName, ((TestCommandProcessor) commandProcessor).rgbCombineRedName);
    assertEquals(greenImageName, ((TestCommandProcessor) commandProcessor).rgbCombineGreenName);
    assertEquals(blueImageName, ((TestCommandProcessor) commandProcessor).rgbCombineBlueName);
  }

  @Test
  public void testLevelsAdjust() throws IOException {
    String testImageName = "Test Image";
    String destName = "Levels Adjusted Image";
    int brightness = 10;
    int midtone = 128;
    int whitePoint = 255;

    commandProcessor.levelsAdjust(testImageName, brightness, midtone, whitePoint, destName, null);

    assertEquals(brightness, ((TestCommandProcessor) commandProcessor).levelsAdjustBrightness);
    assertEquals(midtone, ((TestCommandProcessor) commandProcessor).levelsAdjustMidtone);
    assertEquals(whitePoint, ((TestCommandProcessor) commandProcessor).levelsAdjustWhitePoint);
    assertEquals(destName, ((TestCommandProcessor) commandProcessor).levelsAdjustDestName);
  }

  @Test
  public void testColorCorrectImage() throws IOException {
    String testImageName = "Test Image";
    String destName = "Color Corrected Image";

    commandProcessor.colorCorrectImage(testImageName, destName, null);

    assertEquals(destName, ((TestCommandProcessor) commandProcessor).colorCorrectDestName);
  }

  @Test
  public void testGetHistogram() throws IOException {
    String testImageName = "Test Image";
    String destName = "Histogram Image";

    commandProcessor.getHistogram(testImageName, destName);

    assertEquals(destName, ((TestCommandProcessor) commandProcessor).histogramDestName);
  }

  @Test
  public void testCompressImage() throws IOException {
    String testImageName = "Test Image";
    String destName = "Compressed Image";
    float percentage = 50.0f;

    commandProcessor.compressImage(percentage, testImageName, destName);

    assertEquals(percentage, ((TestCommandProcessor) commandProcessor).compressPercentage, 0.01);
    assertEquals(destName, ((TestCommandProcessor) commandProcessor).compressImageDestName);
  }

  @Test
  public void testDownscaleImage() throws IOException {
    String testImageName = "Test Image";
    String destName = "Downscaled Image";
    int newWidth = 100;
    int newHeight = 100;

    commandProcessor.downscaleImage(testImageName, destName, newWidth, newHeight);

    assertEquals(destName, ((TestCommandProcessor) commandProcessor).downscaleImageDestName);
    assertEquals(newWidth, ((TestCommandProcessor) commandProcessor).downscaleNewWidth);
    assertEquals(newHeight, ((TestCommandProcessor) commandProcessor).downscaleNewHeight);
  }

  @Test
  public void testImageUpdatedInView() throws IOException {
    String testImageName = "Test Image";
    String testFilePath = "test_image.ppm";

    commandProcessor.loadImage(testFilePath, testImageName);

    ImageRepresentation loadedImage = commandProcessor.getImage(testImageName);
    view.setImageIcon(new ImageIcon(loadedImage.toBufferedImage()));

    assertNotNull(((TestView) view).getImage());
  }
}