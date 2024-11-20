package com.vanarp.viewer;

import com.vanarp.controller.CLIView;
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
}