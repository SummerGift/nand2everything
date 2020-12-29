
/**
 * CompilationEngine
 */

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompilationEngine {

    public static void main(String[] args) {

        String sourFile = "D:\\work\\nand2everything\\10\\Test\\ArrayTest\\Main.jack";
        String desFile = "D:\\work\\nand2everything\\10\\Test\\ArrayTest\\Main.jack_temp";

        try {
            String stringLine = null;
            String wtrteLine = null;

            InputStreamReader isr = new InputStreamReader(new FileInputStream(sourFile));
            BufferedReader read = new BufferedReader(isr);
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(desFile));
            BufferedWriter write = new BufferedWriter(osw);

            StringBuilder fBuilder = new StringBuilder();

            while ((stringLine = read.readLine()) != null) {

                if (stringLine.equals("")) {
                    continue;
                }

                if (stringLine.startsWith("//")) {
                    continue;
                }

                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(stringLine);
                wtrteLine = m.replaceAll("");

                fBuilder.append(wtrteLine);
            }

            String sourConString = fBuilder.toString();
            Pattern p = Pattern.compile("(?<!:)\\/\\/.*|\\/\\*(\\s|.)*?\\*\\/");
            Matcher m = p.matcher(sourConString);
            sourConString = m.replaceAll("");

            write.write(sourConString);
            write.close();
            read.close();

            System.out.println(sourConString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}