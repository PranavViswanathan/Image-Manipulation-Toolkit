package com.vanarp;

import com.vanarp.controller.CommandProcessor;
import com.vanarp.controller.CompressedImageIO;
import com.vanarp.controller.ImageCommandProcessor;
import com.vanarp.controller.ImageFileIO;
import com.vanarp.controller.ImageProcessingController;
import com.vanarp.controller.UncompressedImageIO;
import com.vanarp.model.Filtering;
import com.vanarp.model.ImageCompression;
import com.vanarp.model.ImageCompressionFunctionality;
import com.vanarp.model.Operations;
import com.vanarp.model.Transform;
import com.vanarp.viewer.ImageProcessingView;
import javax.swing.SwingUtilities;

public class main {

  public static void main(String[] args) {
    ImageFileIO compressedIO = new CompressedImageIO();
    ImageFileIO uncompressedIO = new UncompressedImageIO();
    Transform transformation = new Transform();
    Filtering filtering = new Filtering();
    ImageCompressionFunctionality compress = new ImageCompression();

    Operations operations = new Operations(transformation, filtering, compressedIO, uncompressedIO,
        compress);
    SwingUtilities.invokeLater(() -> {
      ImageCommandProcessor commandProcessor = new CommandProcessor(operations);
         ImageProcessingView view = new ImageProcessingView();
      new ImageProcessingController(commandProcessor, view);
    });
  }
}

