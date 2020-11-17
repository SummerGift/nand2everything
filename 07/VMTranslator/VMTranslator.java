import java.io.*;
import java.util.ArrayList;

public class VMTranslator {

    public static String fileMerge(String pathName) {
        String outAsmFileName;
        File file = new File(pathName);
        ArrayList<String> vmFiles = new ArrayList<String>();
        outAsmFileName = file.getPath() + "/" + file.getName() + ".asm";

        System.out.println("merge all asm files.\n");
        System.out.println("outAsmFileName: " + outAsmFileName + "\n");

        File[] fs = file.listFiles();
        for (File f : fs) {
            if (!f.isDirectory()) {
                if (f.getName().endsWith(".asm")) {
                    vmFiles.add(f.toString());
                }
            }
        }

        // merge all asm files into one
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outAsmFileName));

            for (int i = 0; i < vmFiles.size(); i++) {
                System.out.println("Path: " + vmFiles.get(i));

                File fileMerge = new File(vmFiles.get(i));
                BufferedReader bufferedReader = new BufferedReader(new FileReader(fileMerge));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    bw.write(line);
                    bw.newLine();
                }
                bufferedReader.close();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // write startup code to call sys.init
        String startAsmFileName = file.getPath() + "/startup.asm";
        CodeWriter.writeStartUpCode(startAsmFileName);

        return outAsmFileName;
    }

    public static void main(String[] args) {
        // if (args.length == 0) {
        // System.out.println("\nPlease specific .vm filename or folder!\n");
        // return;
        // }
        // String pathName = args[0];

        String pathName = "/Users/mac/work/nand2everything/08/FunctionCalls/StaticsTest";

        File file = new File(pathName);
        if (file.isFile()) {
            System.out.println("\nPath is a filename.\n");
        }

        if (file.isDirectory()) {

            System.out.println("Path is a folder, we will merge all vm files.\n");

            ArrayList<String> vmFiles = new ArrayList<String>();
            File[] fs = file.listFiles();
            for (File f : fs) {
                if (!f.isDirectory()) {
                    if (f.getName().endsWith(".vm")) {
                        vmFiles.add(f.toString());
                    }
                }
            }

            for (int i = 0; i < vmFiles.size(); i++) {
                System.out.println("Path: " + vmFiles.get(i));

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
                        case C_FUNCTION:
                            arg2 = myParser.arg2(readCommand, commandType);
                            myCodeWriter.writeFunction(arg1, arg2);
                            break;
                        case C_RETURN:
                            myCodeWriter.writeReturn(arg1);
                            break;
                        case C_CALL:
                            arg2 = myParser.arg2(readCommand, commandType);
                            myCodeWriter.writeCall(arg1, arg2);
                            break;
                        default:
                            System.out.println("undefined command!");
                    }
                }

                myCodeWriter.close();
                myParser.close();
            }

            // merge .asm files and add startup code
            fileMerge(pathName);
        }

        return;
    }
}