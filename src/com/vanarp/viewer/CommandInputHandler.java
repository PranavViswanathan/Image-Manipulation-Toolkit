package com.vanarp.viewer;

import com.vanarp.controller.CLIView;
import com.vanarp.controller.CommandProcessor;
import com.vanarp.controller.CompressedImageIO;
import com.vanarp.controller.ImageFileIO;
import com.vanarp.controller.UncompressedImageIO;
import com.vanarp.model.Filtering;
import com.vanarp.model.ImageCompression;
import com.vanarp.model.ImageCompressionFunctionality;
import com.vanarp.model.Operations;
import com.vanarp.model.Transform;

import java.util.Scanner;

/**
 * CommandInputHandler is responsible for handling user input from the command line.
 */
public class CommandInputHandler {

  private final Scanner scanner;
  private final CLIView cliView;

  /**
   * Constructs a CommandInputHandler with the specified CLIView.
   *
   * @param cliView the CLIView to handle commands
   */
  public CommandInputHandler(CLIView cliView) {
    this.scanner = new Scanner(System.in);
    this.cliView = cliView;
  }

  /**
   * Starts reading commands from the command line.
   */
  public void start() {
    System.out.println("Welcome to the Image Processing Application.");
    System.out.println("Enter your commands. Type 'exit' to quit.");

    while (true) {
      System.out.print("> ");
      String input = scanner.nextLine().trim();
      if (input.equalsIgnoreCase("exit")) {
        System.out.println("Exiting application.");
        break;
      }
      cliView.processCommand(input);
    }
    scanner.close();
  }

  /**
   * The main method to start the application. Initializes the necessary components and starts the
   * CLI.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    ImageFileIO compressedIO = new CompressedImageIO();
    ImageFileIO uncompressedIO = new UncompressedImageIO();
    Transform transformation = new Transform();
    Filtering filtering = new Filtering();
    ImageCompressionFunctionality compress = new ImageCompression();

    Operations operations = new Operations(transformation, filtering, compressedIO, uncompressedIO,
            compress);
    CommandProcessor commandProcessor = new CommandProcessor(operations);

    CLIView cliView = new CLIView(commandProcessor);
    CommandInputHandler inputHandler = new CommandInputHandler(cliView);
    inputHandler.start();
  }
}