import java.io.*;

class CodeWriter {
    String outPutFile;
    String fileName;
    OutputStreamWriter writer;
    FileOutputStream fop;
    public int codeLineCount = 0;
    public int callCount = 0;

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

    public static void writeStartUpCode(String outPutFile) {
        try {
            File f = new File(outPutFile);
            FileOutputStream fop = new FileOutputStream(f);
            OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
            writer.append("@261" + "\n");
            writer.append("D=A" + "\n");
            writer.append("@SP" + "\n");
            writer.append("M=D" + "\n");

            writer.append("@261" + "\n");
            writer.append("D=A" + "\n");
            writer.append("@LCL" + "\n");
            writer.append("M=D" + "\n");

            writer.append("@256" + "\n");
            writer.append("D=A" + "\n");
            writer.append("@ARG" + "\n");
            writer.append("M=D" + "\n");
        
            writer.append("@1234" + "\n");
            writer.append("D=A" + "\n");
            writer.append("@256" + "\n");
            writer.append("M=D" + "\n");
        
            writer.append("@1" + "\n");
            writer.append("D=A" + "\n");
            writer.append("@257" + "\n");
            writer.append("M=D" + "\n");
        
            writer.append("@2" + "\n");
            writer.append("D=A" + "\n");
            writer.append("@258" + "\n");
            writer.append("M=D" + "\n");
        
            writer.append("@3" + "\n");
            writer.append("D=A" + "\n");
            writer.append("@259" + "\n");
            writer.append("M=D" + "\n");
        
            writer.append("@4" + "\n");
            writer.append("D=A" + "\n");
            writer.append("@260" + "\n");
            writer.append("M=D" + "\n");

            writer.append("@Sys.init" + "\n");
            writer.append("0;JMP" + "\n");

            writer.close();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void writeIf(String arg1) {
        System.out.println("if-goto " + arg1);
        try {
            backStack();
            this.writer.append("@SP" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@" + arg1 + "\n");
            this.writer.append("D;JNE" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeGoto(String arg1) {
        System.out.println("goto " + arg1);
        try {
            this.writer.append("@" + arg1 + "\n");
            this.writer.append("0;JMP" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLabel(String arg1) {
        System.out.println("label " + arg1);
        try {
            this.writer.append("(" + arg1 + ")\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFunction(String arg1, String arg2) {
        System.out.println("function " + arg1 + " " + arg2);
        try {
            this.writer.append("(" + arg1 + ")\n");

            // set 0
            this.writer.append("@0" + "\n");
            this.writer.append("D=A" + "\n");

            // get address from LCL
            this.writer.append("@LCL" + "\n");
            this.writer.append("A=M" + "\n");

            // clear local
            for (int i = 0; i < Integer.parseInt(arg2); i++) {
                // set 0 to loacl
                this.writer.append("M=D" + "\n");
                this.writer.append("A=A+1" + "\n");
            }

            for (int i = 0; i < Integer.parseInt(arg2); i++) {
                deepStack();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeReturn(String arg1) {
        System.out.println("return");
        try {
            backStack();
            // move return value to the address of ARG register
            this.writer.append("@SP" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@ARG" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("M=D" + "\n");

            // update SP register
            this.writer.append("@ARG" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@SP" + "\n");
            this.writer.append("M=D+1" + "\n");

            // save the LCL's value
            this.writer.append("@LCL" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@R13" + "\n");
            this.writer.append("M=D" + "\n");

            // restore THAT
            this.writer.append("@LCL" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("A=A-1" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@THAT" + "\n");
            this.writer.append("M=D" + "\n");

            // restore THIS
            this.writer.append("@LCL" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("A=A-1" + "\n");
            this.writer.append("A=A-1" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@THIS" + "\n");
            this.writer.append("M=D" + "\n");

            // restore ARG
            this.writer.append("@LCL" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("A=A-1" + "\n");
            this.writer.append("A=A-1" + "\n");
            this.writer.append("A=A-1" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@ARG" + "\n");
            this.writer.append("M=D" + "\n");

            // restore LCL
            this.writer.append("@LCL" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("A=A-1" + "\n");
            this.writer.append("A=A-1" + "\n");
            this.writer.append("A=A-1" + "\n");
            this.writer.append("A=A-1" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@LCL" + "\n");
            this.writer.append("M=D" + "\n");

            // goto return address
            this.writer.append("@R13" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@5" + "\n");
            this.writer.append("AD=D-A" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("0;JMP" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeCall(String arg1, String arg2) {
        System.out.println("call " + arg1 + " " + arg2);
        try {
            // save number of argument to R13
            this.writer.append("@" + arg2 + "\n");
            this.writer.append("D=A" + "\n");
            this.writer.append("@R13" + "\n");
            this.writer.append("M=D" + "\n");

            // save callee adderss to R14
            this.writer.append("@" + arg1 + "\n");
            this.writer.append("D=A" + "\n");
            this.writer.append("@R14" + "\n");
            this.writer.append("M=D" + "\n");

            // if arg number is 0, still need to store return value
            if (Integer.parseInt(arg2) == 0) {
                this.writer.append("@0" + "\n");
                this.writer.append("D=A" + "\n");
                this.writer.append("@SP" + "\n");
                this.writer.append("A=M" + "\n");
                this.writer.append("M=D" + "\n");
                deepStack();
            }

            // push return address
            if (arg1.equals("Sys.init")){
                this.writer.append("@0" + "\n");
                this.writer.append("D=A" + "\n");
                this.writer.append("@SP" + "\n");
                this.writer.append("A=M" + "\n");
                this.writer.append("M=D" + "\n");
            }else{
                this.writer.append("@RET_ADDRESS_CALL" + this.callCount + "\n");
                this.writer.append("D=A" + "\n");
                this.writer.append("@SP" + "\n");
                this.writer.append("A=M" + "\n");
                this.writer.append("M=D" + "\n");
            }
            deepStack();

            // push LCL
            this.writer.append("@LCL" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@SP" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("M=D" + "\n");
            deepStack();

            // push ARG
            this.writer.append("@ARG" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@SP" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("M=D" + "\n");
            deepStack();

            // push THIS
            this.writer.append("@THIS" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@SP" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("M=D" + "\n");
            deepStack();

            // push THAT
            this.writer.append("@THAT" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@SP" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("M=D" + "\n");
            deepStack();

            // change LCL to callee context
            this.writer.append("@SP" + "\n");
            this.writer.append("D=M" + "\n");
            this.writer.append("@LCL" + "\n");
            this.writer.append("M=D" + "\n");

            // change ARG to callee context
            this.writer.append("@5" + "\n");
            this.writer.append("D=A" + "\n");

            // if arg number is 0, still need store return value
            if (Integer.parseInt(arg2) == 0) {
                this.writer.append("@1" + "\n");
            } else {
                this.writer.append("@" + arg2 + "\n");
            }

            this.writer.append("D=D+A" + "\n");
            this.writer.append("@SP" + "\n");
            this.writer.append("D=M-D" + "\n");
            this.writer.append("@ARG" + "\n");
            this.writer.append("M=D" + "\n");

            // goto callee function
            this.writer.append("@R14" + "\n");
            this.writer.append("A=M" + "\n");
            this.writer.append("0;JMP" + "\n");

            // add return label
            this.writer.append("(RET_ADDRESS_CALL" + this.callCount + ")\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.callCount++;
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
