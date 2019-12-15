import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;

public class BeanInterpreter {
	public static int lineNum = 1;
	public static String line;
	
	public static File prg;
	public static Scanner scn;
	
	public static ArrayList<Token> tokensLine;
	public static ArrayList<Token> tokensStatement = new ArrayList<Token>();
	
	public static DatatypeDict variables = new DatatypeDict();
	
	public static void main(String[] args) throws Exception {
		prg = new File(args[0] + ".bean");
		scn = new Scanner(prg);
		
		BeanLexer lexer;
		BeanParser parser;
		
		while (scn.hasNextLine()) {
			line = scn.nextLine();
			line = line.trim();
			
			if (line.length() > 0) {
				if (line.charAt(0) == 47 && line.charAt(1) == 47) {
					continue;
				}
				tokensLine = new ArrayList<Token>();
				lexer = new BeanLexer(line, tokensLine, lineNum);
				lexer.run();
				
				Token curToken = tokensLine.get(0);
				Token prevToken = new EmptyToken();
				boolean parseLine = true;
				
				while (parseLine) {
					if (curToken instanceof EOLToken) {
						if (prevToken instanceof EmptyToken) {
							parseLine = false;
							break;
						} else {
							exception("Reached end of line while parsing - Missing semicolon");
						}
					} else if (curToken instanceof EmptyToken) {
						tokensStatement.add(curToken);
						tokensLine.remove(0);
						prevToken = curToken;
						curToken = tokensLine.get(0);
						parser = new BeanParser(tokensStatement, variables, lineNum);
						parser.run();
						tokensStatement = new ArrayList<Token>();
						continue;
					} else {
						tokensStatement.add(curToken);
						tokensLine.remove(0);
						prevToken = curToken;
						curToken = tokensLine.get(0);
					}
				}
			}
			lineNum++;
		}
	}
	
	public static void exception(String s) {
		//System.out.println();
		System.out.print("Line " + lineNum + ": ");
		//System.out.println("ERROR");
		System.out.println(s);
		System.exit(0);
	}
	
	public static void end() {
		System.exit(0);
	}
}
