package com.vanarp;

import com.vanarp.controller.CLIView;
import com.vanarp.controller.CommandProcessor;
import com.vanarp.controller.CompressedImageIO;
import com.vanarp.controller.GUIController;
import com.vanarp.controller.ImageCommandProcessor;
import com.vanarp.controller.ImageFileIO;
import com.vanarp.controller.UncompressedImageIO;
import com.vanarp.model.Filtering;
import com.vanarp.model.ImageCompression;
import com.vanarp.model.ImageCompressionFunctionality;
import com.vanarp.model.Operations;
import com.vanarp.model.Transform;
import com.vanarp.viewer.CommandInputHandler;
import com.vanarp.viewer.GUIView;
import com.vanarp.viewer.GUIViewInterface;
import javax.swing.SwingUtilities;

public class Main {

  public static void main(String[] args) {
    if (args.length == 0) {
      launchGUI();
    } else if (args.length == 2 && args[0].equals("-file")) {
      executeScriptFile(args[1]);
    } else if (args.length == 1 && args[0].equals("-text")) {
      launchTextMode();
    } else {
      System.err.println(
          "Invalid command-line arguments. Use -file <path>, -text, or no arguments for GUI.");
      System.exit(1);
    }
  }

  private static void launchGUI() {
    ImageCommandProcessor commandProcessor = createCommandProcessor();
    SwingUtilities.invokeLater(() -> {
      GUIView view = new GUIView();
      new GUIController(commandProcessor, view);
    });
  }

  private static void executeScriptFile(String scriptFilePath) {
    try {
      ImageCommandProcessor commandProcessor = createCommandProcessor();
      CLIView cliView = new CLIView(commandProcessor);
      cliView.handleScript(new String[]{"-file", scriptFilePath});
    } catch (Exception e) {
      System.err.println("Error executing script file: " + e.getMessage());
      System.exit(1);
    }
  }

  private static void launchTextMode() {
    ImageCommandProcessor commandProcessor = createCommandProcessor();
    CLIView cliView = new CLIView(commandProcessor);
    CommandInputHandler inputHandler = new CommandInputHandler(cliView);
    inputHandler.start();
  }

  private static CommandProcessor createCommandProcessor() {
    ImageFileIO compressedIO = new CompressedImageIO();
    ImageFileIO uncompressedIO = new UncompressedImageIO();
    Transform transformation = new Transform();
    Filtering filtering = new Filtering();
    ImageCompressionFunctionality compress = new ImageCompression();

    Operations operations = new Operations(transformation, filtering, compressedIO, uncompressedIO,
        compress);
    return new CommandProcessor(operations);
  }
}