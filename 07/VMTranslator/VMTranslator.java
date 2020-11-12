import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class VMTranslator {

    public static void main(String[] args) {
        // if (args.length == 0) {
        // System.out.println("\nPlease specific .vm filename or folder!\n");
        // return;
        // }
        // String pathName = args[0];

        String pathName = "/Users/mac/work/nand2everything/07/MemoryAccess/BasicTest/BasicTest.vm";

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
            System.out.println("Path is a filename.\n");
            vmFiles.add(pathName);
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