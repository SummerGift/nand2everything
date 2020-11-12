import java.io.*;

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
