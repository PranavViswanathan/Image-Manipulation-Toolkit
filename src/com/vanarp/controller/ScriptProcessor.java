  package com.vanarp.controller;

  import java.io.BufferedReader;
  import java.io.FileReader;
  import java.io.IOException;

  /**
   * The ScriptProcessor class is responsible for executing a series of commands
   * from a script file in the context of the CLIView.
   */
  public class ScriptProcessor {

    private final CLIView cliView;

    /**
     * Constructs a ScriptProcessor with the specified CLIView.
     *
     * @param cliView the CLIView instance used to process commands
     */
    public ScriptProcessor(CLIView cliView) {
      this.cliView = cliView;
    }

    /**
     * Executes commands from the specified script file. Each line in the script
     * is processed as a command if it is not empty and does not start with a comment (#).
     *
     * @param filePath the path to the script file to be executed
     * @throws IOException if an error occurs while reading the script file
     */
    public void executeScript(String filePath) throws IOException {
      try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
          line = line.trim();
          if (!line.isEmpty() && !line.startsWith("#")) {
            cliView.processCommand(line);
          }
        }
      }
    }
  }
