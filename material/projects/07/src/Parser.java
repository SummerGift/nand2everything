package com.journaldev.readfileslinebyline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {

    String file;
    String curCommand = null;
    BufferedReader reader;

    public Parser(String file){
        this.file = file;
     }

    public boolean hasMoreCommands(){
        try {
            this.curCommand = this.reader.readLine();
		} catch (IOException e) {
            e.printStackTrace();
        }
        
        if (this.curCommand != null){
            return true;
        }else{
            return false;
        }
    }

    public String advance(){
        return this.curCommand;
    }

    public void readFile(){
        System.out.println(file);
        
		try {
			this.reader = new BufferedReader(new FileReader(this.file));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void closeFile(){
        try {
            this.reader.close();
		} catch (IOException e) {
			e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Parser myParser = new Parser("../MemoryAccess/BasicTest/BasicTest.vm");
        myParser.readFile();

        while(myParser.hasMoreCommands()){
            System.out.println(myParser.advance());
        }

        myParser.closeFile();
    }
}
