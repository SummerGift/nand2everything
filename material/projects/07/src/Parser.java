package com.journaldev.readfileslinebyline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
  String file;
  String curCommand = null;
  BufferedReader reader;

  public Parser(String file) {
    this.file = file;
  }

  enum commandTypeTranslator {
    C_ARITHMETIC,
    C_PUSH,
    C_POP,
    C_LABLE,
    C_GOTO,
    C_IF,
    C_FUNCTION,
    C_RETURN,
    C_CALL,
  }

  public boolean hasMoreCommands() {
    try {
      this.curCommand = this.reader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (this.curCommand != null) {
      return true;
    } else {
      return false;
    }
  }

  public String advance() {
    if (this.curCommand.startsWith("//")) {
      return null;
    }

    if (this.curCommand.equals("")) {
      return null;
    }

    return this.curCommand;
  }

  public commandTypeTranslator commandType(String command) {
    String firstCommand = null;

    for (String retval : command.split(" ")) {
      firstCommand = retval;
      break;
    }

    if (firstCommand.equals("push")) {
      return commandTypeTranslator.C_PUSH;
    }

    if (firstCommand.equals("pop")) {
      return commandTypeTranslator.C_POP;
    }

    if (firstCommand.equals("add") || firstCommand.equals("sub")) {
      return commandTypeTranslator.C_ARITHMETIC;
    }
    return null;
  }

  public void readFile() {
    try {
      this.reader = new BufferedReader(new FileReader(this.file));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void closeFile() {
    try {
      this.reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Parser myParser = new Parser("../MemoryAccess/BasicTest/BasicTest.vm");
    myParser.readFile();

    while (myParser.hasMoreCommands()) {
      String curCommand = myParser.advance();
      if (curCommand != null) {
        String readCommand = myParser.advance();
        System.out.println(readCommand);
        commandTypeTranslator commandType = myParser.commandType(readCommand);
        System.out.println(commandType);
      }
    }

    myParser.closeFile();
  }
}
