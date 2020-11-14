import java.io.*;
import java.util.ArrayList;

public class VMTranslator {

    public static void main(String[] args) {
        // if (args.length == 0) {
        // System.out.println("\nPlease specific .vm filename or folder!\n");
        // return;
        // }
        // String pathName = args[0];

        String pathName = "/Users/mac/work/nand2everything/08/ProgramFlow/FibonacciSeries/FibonacciSeries.vm";

        File file = new File(pathName);
        ArrayList<String> vmFiles = new ArrayList<String>();
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
            System.out.println("\nPath is a filename.\n");
            vmFiles.add(pathName);
        }

        for (int i = 0; i < vmFiles.size(); i++) {
            System.out.println("Path: " + vmFiles.get(i) + "\n");

            Parser myParser = new Parser(vmFiles.get(i));
            myParser.readFile();
            CodeWriter myCodeWriter = new CodeWriter(myParser.getFileName());

            while (myParser.hasMoreCommands()) {
                String readCommand = myParser.advance();

                myCodeWriter.addCodeLine();

                commandTypeTranslator commandType = myParser.commandType(readCommand);

                String arg1 = myParser.arg1(readCommand, commandType);
                String arg2;

                switch (commandType) {
                    case C_ARITHMETIC:
                        myCodeWriter.writeArithetic(arg1);
                        break;
                    case C_PUSH:
                        arg2 = myParser.arg2(readCommand, commandType);
                        myCodeWriter.writePush(arg1, arg2);
                        break;
                    case C_POP:
                        arg2 = myParser.arg2(readCommand, commandType);
                        myCodeWriter.writePop(arg1, arg2);
                        break;
                    case C_LABEL:
                        myCodeWriter.writeLabel(arg1);
                        break;
                    case C_IF:
                        myCodeWriter.writeIf(arg1);
                        break;
                    case C_GOTO:
                        myCodeWriter.writeGoto(arg1);
                        break;
                    default:
                        System.out.println("undefined command!");
                }
            }

            myCodeWriter.close();
            myParser.close();
        }

        return;
    }
}