package com.vanarp.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.vanarp.model.Filtering;
import com.vanarp.model.ImageCompression;
import com.vanarp.model.ImageCompressionFunctionality;
import com.vanarp.model.ImageRepresentation;
import com.vanarp.model.Operations;
import com.vanarp.model.PixelInterface;
import com.vanarp.model.Transform;
import com.vanarp.viewer.GUIView;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the GUIController. This class contains unit tests for the methods of the
 * GUIController and validates the functionality of the ImageCommandProcessor and GUIView.
 */
public class GUIControllerTest {

  private GUIView view;
  private ImageCommandProcessor commandProcessor;

  /**
   * A test implementation of the GUIView class. This class captures the image set in the view for
   * validation in tests.
   */
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

  /**
   * A mock implementation of the ImageCommandProcessor interface. This class simulates the behavior
   * of the command processor for testing purposes.
   */
  private static class MockTestCommandProcessor implements ImageCommandProcessor {

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
        private final PixelInterface[][] pixels = new PixelInterface[100][100];

        @Override
        public void setPixel(int x, int y, PixelInterface pixel) {
          if (x >= 0 && x < pixels.length && y >= 0 && y < pixels[0].length) {
            pixels[x][y] = pixel;
          } else {
            System.out.println("Attempted to set pixel out of bounds: "
                + (x)
                + (", ")
                + (y)
                + (")")
                + (System.lineSeparator()));
          }
        }

        @Override
        public PixelInterface getPixel(int x, int y) {
          return pixels[x][y];
        }

        @Override
        public int getWidth() {
          return 100;
        }

        @Override
        public int getHeight() {
          return 100;
        }

        @Override
        public BufferedImage toBufferedImage() {
          return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
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
      System.out.println("Processing split: " + operation);
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
    commandProcessor = new MockTestCommandProcessor();
    Transform transformation = new Transform();
    Filtering filtering = new Filtering();
    ImageCompressionFunctionality compress = new ImageCompression();

    Operations operations = new Operations(transformation, filtering,
        compress);
    new GUIController(operations, commandProcessor, view);
  }

  @Test
  public void testLoadImage() throws IOException {
    String testFilePath = "test_image.ppm";
    String testImageName = "Test Image";

    commandProcessor.loadImage(testFilePath, testImageName);

    assertEquals(testFilePath, ((MockTestCommandProcessor) commandProcessor).loadedImagePath);
    assertEquals(testImageName, ((MockTestCommandProcessor) commandProcessor).currentImageName);
  }

  @Test
  public void testSaveImage() throws IOException {
    String testImageName = "Test Image";
    String testFilePath = "saved_image.png";

    commandProcessor.saveImage(testImageName, testFilePath, "png");

    assertEquals(testFilePath, ((MockTestCommandProcessor) commandProcessor).savedImagePath);
  }

  @Test
  public void testExtractRedComponent() throws IOException {
    String testImageName = "Test Image";
    String destName = "Red Component";

    commandProcessor.extractComponent(testImageName, destName, "red", null);

    assertEquals("red", ((MockTestCommandProcessor) commandProcessor).extractedComponentType);
    assertEquals(destName,
        ((MockTestCommandProcessor) commandProcessor).extractedComponentDestName);
  }

  @Test
  public void testApplyFilter() throws IOException {
    String testImageName = "Test Image";
    String destName = "Filtered Image";
    String filterType = "blur";

    commandProcessor.applyFilter(testImageName, destName, filterType, null, null);

    assertEquals(filterType, ((MockTestCommandProcessor) commandProcessor).appliedFilterType);
    assertEquals(destName, ((MockTestCommandProcessor) commandProcessor).appliedFilterDestName);
  }

  @Test
  public void testFlipImage() throws IOException {
    String testImageName = "Test Image";
    String destName = "Flipped Image";

    commandProcessor.flipImage(testImageName, destName, "horizontal");

    assertEquals("horizontal", ((MockTestCommandProcessor) commandProcessor).flippedDirection);
    assertEquals(destName, ((MockTestCommandProcessor) commandProcessor).flippedImageDestName);
  }

  @Test
  public void testBrightenImage() throws IOException {
    String testImageName = "Test Image";
    String destName = "Brightened Image";
    int increment = 10;

    commandProcessor.brightenImage(testImageName, increment, destName);

    assertEquals(increment, ((MockTestCommandProcessor) commandProcessor).brightenedIncrement);
    assertEquals(destName, ((MockTestCommandProcessor) commandProcessor).brightenedImageDestName);
  }

  @Test
  public void testRgbSplit() throws IOException {
    String testImageName = "Test Image";
    String redName = "Red Image";
    String greenName = "Green Image";
    String blueName = "Blue Image";

    commandProcessor.rgbSplit(testImageName, redName, greenName, blueName);

    assertEquals(redName, ((MockTestCommandProcessor) commandProcessor).rgbSplitRedName);
    assertEquals(greenName, ((MockTestCommandProcessor) commandProcessor).rgbSplitGreenName);
    assertEquals(blueName, ((MockTestCommandProcessor) commandProcessor).rgbSplitBlueName);
  }

  @Test
  public void testRgbCombine() throws IOException {
    String destName = "Combined Image";
    String redImageName = "Red Image";
    String greenImageName = "Green Image";
    String blueImageName = "Blue Image";

    commandProcessor.rgbCombine(destName, redImageName, greenImageName, blueImageName);

    assertEquals(destName, ((MockTestCommandProcessor) commandProcessor).rgbCombineDestName);
    assertEquals(redImageName, ((MockTestCommandProcessor) commandProcessor).rgbCombineRedName);
    assertEquals(greenImageName, ((MockTestCommandProcessor) commandProcessor).rgbCombineGreenName);
    assertEquals(blueImageName, ((MockTestCommandProcessor) commandProcessor).rgbCombineBlueName);
  }

  @Test
  public void testLevelsAdjust() throws IOException {
    String testImageName = "Test Image";
    String destName = "Levels Adjusted Image";
    int brightness = 10;
    int midtone = 128;
    int whitePoint = 255;

    commandProcessor.levelsAdjust(testImageName, brightness, midtone, whitePoint, destName, null);

    assertEquals(brightness, ((MockTestCommandProcessor) commandProcessor).levelsAdjustBrightness);
    assertEquals(midtone, ((MockTestCommandProcessor) commandProcessor).levelsAdjustMidtone);
    assertEquals(whitePoint, ((MockTestCommandProcessor) commandProcessor).levelsAdjustWhitePoint);
    assertEquals(destName, ((MockTestCommandProcessor) commandProcessor).levelsAdjustDestName);
  }

  @Test
  public void testColorCorrectImage() throws IOException {
    String testImageName = "Test Image";
    String destName = "Color Corrected Image";

    commandProcessor.colorCorrectImage(testImageName, destName, null);

    assertEquals(destName, ((MockTestCommandProcessor) commandProcessor).colorCorrectDestName);
  }

  @Test
  public void testGetHistogram() throws IOException {
    String testImageName = "Test Image";
    String destName = "Histogram Image";

    commandProcessor.getHistogram(testImageName, destName);

    assertEquals(destName, ((MockTestCommandProcessor) commandProcessor).histogramDestName);
  }

  @Test
  public void testCompressImage() throws IOException {
    String testImageName = "Test Image";
    String destName = "Compressed Image";
    float percentage = 50.0f;

    commandProcessor.compressImage(percentage, testImageName, destName);

    assertEquals(percentage, ((MockTestCommandProcessor) commandProcessor).compressPercentage,
        0.01);
    assertEquals(destName, ((MockTestCommandProcessor) commandProcessor).compressImageDestName);
  }

  @Test
  public void testDownscaleImage() throws IOException {
    String testImageName = "Test Image";
    String destName = "Downscaled Image";
    int newWidth = 100;
    int newHeight = 100;

    commandProcessor.downscaleImage(testImageName, destName, newWidth, newHeight);

    assertEquals(destName, ((MockTestCommandProcessor) commandProcessor).downscaleImageDestName);
    assertEquals(newWidth, ((MockTestCommandProcessor) commandProcessor).downscaleNewWidth);
    assertEquals(newHeight, ((MockTestCommandProcessor) commandProcessor).downscaleNewHeight);
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

  @Test
  public void testLoadInvalidImage() {
    String invalidFilePath = "invalid_image.xyz";
    String testImageName = "Test Image";

    try {
      commandProcessor.loadImage(invalidFilePath, testImageName);
    } catch (IOException e) {
      assertEquals("Invalid image format", e.getMessage());
    }
  }

  @Test
  public void testSaveImageWithNullName() {
    String testFilePath = "saved_image.png";

    try {
      commandProcessor.saveImage(null, testFilePath, "png");
    } catch (IllegalArgumentException e) {
      assertEquals("Image name cannot be null", e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testApplyFilterWithNullImageName() {
    String destName = "Filtered Image";
    String filterType = "blur";

    try {
      commandProcessor.applyFilter(null, destName, filterType, null, null);
    } catch (IllegalArgumentException e) {
      assertEquals("Image name cannot be null", e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testBrightenImageWithNegativeIncrement() {
    String testImageName = "Test Image";
    String destName = "Brightened Image";
    int increment = -10;

    try {
      commandProcessor.brightenImage(testImageName, increment, destName);
    } catch (IllegalArgumentException e) {
      assertEquals("Increment must be non-negative", e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testRgbSplitWithNullImageName() {
    String redName = "Red Image";
    String greenName = "Green Image";
    String blueName = "Blue Image";

    try {
      commandProcessor.rgbSplit(null, redName, greenName, blueName);
    } catch (IllegalArgumentException e) {
      assertEquals("Image name cannot be null", e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testDownscaleImageWithInvalidDimensions() {
    String testImageName = "Test Image";
    String destName = "Downscaled Image";
    int newWidth = -100;
    int newHeight = 100;

    try {
      commandProcessor.downscaleImage(testImageName, destName, newWidth, newHeight);
    } catch (IllegalArgumentException e) {
      assertEquals("Width and height must be positive", e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testGetHistogramWithNullImageName() {
    String destName = "Histogram Image";

    try {
      commandProcessor.getHistogram(null, destName);
    } catch (IllegalArgumentException e) {
      assertEquals("Image name cannot be null", e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testColorCorrectImageWithNullDestName() {
    String testImageName = "Test Image";

    try {
      commandProcessor.colorCorrectImage(testImageName, null, null);
    } catch (IllegalArgumentException e) {
      assertEquals("Destination name cannot be null", e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testExtractComponentWithNullComponentType() {
    String testImageName = "Test Image";
    String destName = "Component Image";

    try {
      commandProcessor.extractComponent(testImageName, destName, null, null);
    } catch (IllegalArgumentException e) {
      assertEquals("Component type cannot be null", e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testFlipImageWithInvalidDirection() {
    String testImageName = "Test Image";
    String destName = "Flipped Image";

    try {
      commandProcessor.flipImage(testImageName, destName, "diagonal");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid flip direction", e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testCompressImageWithNegativePercentage() {
    String testImageName = "Test Image";
    String destName = "Compressed Image";
    float percentage = -50.0f;

    try {
      commandProcessor.compressImage(percentage, testImageName, destName);
    } catch (IllegalArgumentException e) {
      assertEquals("Compression percentage must be between 0 and 100", e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
