import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class VMTranslator {

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("\nPlease specific .vm filename or folder!\n");
      return;
    }

    ArrayList<String> vmFiles = new ArrayList<String>();

    File file = new File(args[0]);
    if (file.isDirectory()) {
      System.out.println("Path is a folder.\n");
      File[] fs = file.listFiles();
      for (File f : fs) {
        if (!f.isDirectory()) {
          if (f.getName().endsWith(".vm")) {
            vmFiles.add(f.toString());
          }
        }
      }
    }

    if (file.isFile()) {
      System.out.println("Path is a filename.\n");
      vmFiles.add(args[0]);
    }

    for (int i = 0; i < vmFiles.size(); i++) {
      System.out.println(vmFiles.get(i));

      Parser myParser = new Parser(vmFiles.get(i));
      myParser.readFile();
      CodeWriter myCodeWriter = new CodeWriter(myParser.getFileName());

      while (myParser.hasMoreCommands()) {
        String readCommand = myParser.advance();

        myCodeWriter.addCodeLine();

        commandTypeTranslator commandType = myParser.commandType(readCommand);

        String arg1 = myParser.arg1(readCommand, commandType);
        if (commandType == commandTypeTranslator.C_ARITHMETIC) {
          myCodeWriter.writeArithetic(arg1);
        }

        if (commandType == commandTypeTranslator.C_PUSH) {
          String arg2 = myParser.arg2(readCommand, commandType);
          myCodeWriter.writePush(arg1, arg2);
        }

        if (commandType == commandTypeTranslator.C_POP) {
          String arg2 = myParser.arg2(readCommand, commandType);
          myCodeWriter.writePop(arg1, arg2);
        }
      }

      myCodeWriter.close();
      myParser.close();
    }

    return;
  }
}

class CodeWriter {
  String outPutFile;
  String fileName;
  OutputStreamWriter writer;
  FileOutputStream fop;
  public int codeLineCount = 0;

  public CodeWriter(String outFile) {
    this.outPutFile = this.setFileName(outFile);
    try {
      File f = new File(this.outPutFile);
      this.fop = new FileOutputStream(f);
      this.writer = new OutputStreamWriter(fop, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String setFileName(String outFile) {
    File tempFile = new File(outFile.trim());
    this.fileName = tempFile.getName();

    return outFile.replace(".vm", ".asm");
  }

  public void addCodeLine() {
    this.codeLineCount += 1;
  }

  public void deepStack() {
    try {
      this.writer.append("@SP" + "\n");
      this.writer.append("M=M+1" + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void backStack() {
    try {
      this.writer.append("@SP" + "\n");
      this.writer.append("M=M-1" + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeArithetic(String arg1) {
    System.out.println(arg1);

    try {
      switch (arg1) {
        case "add":
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=M+D" + "\n");
          deepStack();
          break;
        case "sub":
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=M-D" + "\n");
          deepStack();
          break;
        case "neg":
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=-M" + "\n");
          deepStack();
          break;
        case "eq":
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M-D" + "\n");
          // Determine whether M is equal to D
          this.writer.append("@END_EQ_" + this.codeLineCount + "\n");
          this.writer.append("D;JEQ" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=0" + "\n");
          this.writer.append("@END_NOT_EQ_" + this.codeLineCount + "\n");
          this.writer.append("0;JMP" + "\n");
          this.writer.append("(END_EQ_" + this.codeLineCount + ")\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=-1" + "\n");
          this.writer.append("(END_NOT_EQ_" + this.codeLineCount + ")\n");

          deepStack();
          break;
        case "gt":
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M-D" + "\n");
          // Determine whether M is great than D
          this.writer.append("@END_GT_" + this.codeLineCount + "\n");
          this.writer.append("D;JGT" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=0" + "\n");
          this.writer.append("@END_NOT_GT_" + this.codeLineCount + "\n");
          this.writer.append("0;JMP" + "\n");
          this.writer.append("(END_GT_" + this.codeLineCount + ")\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=-1" + "\n");
          this.writer.append("(END_NOT_GT_" + this.codeLineCount + ")\n");
          deepStack();
          break;
        case "lt":
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M-D" + "\n");
          // Determine whether M is Less than D
          this.writer.append("@END_LT_" + this.codeLineCount + "\n");
          this.writer.append("D;JLT" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=0" + "\n");
          this.writer.append("@END_NOT_LT_" + this.codeLineCount + "\n");
          this.writer.append("0;JMP" + "\n");
          this.writer.append("(END_LT_" + this.codeLineCount + ")\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=-1" + "\n");
          this.writer.append("(END_NOT_LT_" + this.codeLineCount + ")\n");
          deepStack();
          break;
        case "and":
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=M&D" + "\n");
          deepStack();
          break;
        case "or":
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=M|D" + "\n");
          deepStack();
          break;
        case "not":
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=!M" + "\n");
          deepStack();
          break;
        default:
          System.out.println("undefined arithetic command!");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writePush(String arg1, String arg2) {
    System.out.println("push " + arg1);
    try {
      switch (arg1) {
        case "constant":
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          deepStack();
          break;
        case "local":
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@LCL" + "\n");
          this.writer.append("A=D+M" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          deepStack();
          break;
        case "argument":
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@ARG" + "\n");
          this.writer.append("A=D+M" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          deepStack();
          break;
        case "this":
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@THIS" + "\n");
          this.writer.append("A=D+M" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          deepStack();
          break;
        case "that":
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@THAT" + "\n");
          this.writer.append("A=D+M" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          deepStack();
          break;
        case "temp":
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@5" + "\n");
          this.writer.append("A=D+A" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          deepStack();
          break;
        case "pointer":
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@3" + "\n");
          this.writer.append("A=D+A" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          deepStack();
          break;
        case "static":
          String staticName = this.fileName.replace(".vm", "");
          this.writer.append("@" + staticName + "." + arg2 + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          deepStack();
          break;
        default:
          System.out.println("undefined push argument!");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writePop(String arg1, String arg2) {
    System.out.println("pop " + arg1);
    try {
      switch (arg1) {
        case "local":
          backStack();
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@LCL" + "\n");
          this.writer.append("A=D+M" + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("M=D" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          break;
        case "argument":
          backStack();
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@ARG" + "\n");
          this.writer.append("A=D+M" + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("M=D" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          break;
        case "this":
          backStack();
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@THIS" + "\n");
          this.writer.append("A=D+M" + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("M=D" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          break;
        case "that":
          backStack();
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@THAT" + "\n");
          this.writer.append("A=D+M" + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("M=D" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          break;
        case "temp":
          backStack();
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@5" + "\n");
          this.writer.append("D=D+A" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("M=D" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          break;
        case "pointer":
          backStack();
          this.writer.append("@" + arg2 + "\n");
          this.writer.append("D=A" + "\n");
          this.writer.append("@3" + "\n");
          this.writer.append("D=D+A" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("M=D" + "\n");
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@R13" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("M=D" + "\n");
          break;
        case "static":
          String staticName = this.fileName.replace(".vm", "");
          backStack();
          this.writer.append("@SP" + "\n");
          this.writer.append("A=M" + "\n");
          this.writer.append("D=M" + "\n");
          this.writer.append("@" + staticName + "." + arg2 + "\n");
          this.writer.append("M=D" + "\n");
          break;
        default:
          System.out.println("undefined pop argument!");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    try {
      this.writer.close();
      this.fop.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
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
  C_NULL,
}

class Parser {
  String file;
  String curCommand = null;
  BufferedReader reader;

  public Parser(String file) {
    this.file = file;
  }

  public String[] cArithmetic = {
    "add",
    "sub",
    "neg",
    "eq",
    "gt",
    "lt",
    "and",
    "or",
    "not",
  };

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
    String firstCommand = command.split(" ")[0];

    if (firstCommand.equals("push")) {
      return commandTypeTranslator.C_PUSH;
    }

    if (firstCommand.equals("pop")) {
      return commandTypeTranslator.C_POP;
    }

    if (Arrays.asList(this.cArithmetic).contains(firstCommand)) {
      return commandTypeTranslator.C_ARITHMETIC;
    }

    if (firstCommand.equals("label")) {
      return commandTypeTranslator.C_LABLE;
    }

    if (firstCommand.equals("goto")) {
      return commandTypeTranslator.C_GOTO;
    }

    if (firstCommand.equals("if")) {
      return commandTypeTranslator.C_IF;
    }

    if (firstCommand.equals("function")) {
      return commandTypeTranslator.C_FUNCTION;
    }

    if (firstCommand.equals("return")) {
      return commandTypeTranslator.C_RETURN;
    }

    if (firstCommand.equals("call")) {
      return commandTypeTranslator.C_CALL;
    }

    return commandTypeTranslator.C_NULL;
  }

  public String arg1(String command, commandTypeTranslator commandType) {
    if (commandType == commandTypeTranslator.C_RETURN) {
      return null;
    }

    if (commandType == commandTypeTranslator.C_ARITHMETIC) {
      String firstItem = command.split(" ")[0];
      return firstItem;
    }

    String secondItem = command.split(" ")[1];
    return secondItem;
  }

  public String arg2(String command, commandTypeTranslator commandType) {
    if (
      commandType == commandTypeTranslator.C_PUSH ||
      commandType == commandTypeTranslator.C_POP ||
      commandType == commandTypeTranslator.C_FUNCTION ||
      commandType == commandTypeTranslator.C_CALL
    ) {
      String thirdCommand = command.split(" ")[2];
      return thirdCommand;
    }

    return null;
  }

  public static void readToBuffer(StringBuffer buffer, String filePath)
    throws IOException {
    InputStream is = new FileInputStream(filePath);
    String line;
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    line = reader.readLine();
    while (line != null) {
      if (!(line.startsWith("//") || line.equals(""))) {
        buffer.append(line);
        buffer.append("\n");
      }
      line = reader.readLine();
    }
    reader.close();
    is.close();
  }

  public String getFileName() {
    return this.file;
  }

  public void readFile() {
    try {
      StringBuffer sb = new StringBuffer();
      readToBuffer(sb, this.file);
      Reader inputString = new StringReader(sb.toString());
      this.reader = new BufferedReader(inputString);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    try {
      this.reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
