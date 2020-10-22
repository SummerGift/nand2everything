package com.journaldev.readfileslinebyline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {

    String name;

    public Parser(String name){
        this.name = name;
     }

    public void hello(){
        System.out.println(this.name);
    }

    public void readFile(String file){
        System.out.println(file);
        BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        Parser myParser = new Parser("hello world");
        myParser.hello();
        myParser.readFile("../MemoryAccess/BasicTest/BasicTest.vm");
    }
}
