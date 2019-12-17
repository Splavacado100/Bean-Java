import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;

public class BeanInterpreter {
	public static int lineNum = 1;
	public static int curScope = 0;
	public static String line;
	public static ArrayList<String> lines = new ArrayList<String>();
	
	public static File prg;
	public static Scanner scn;
	
	public static ArrayList<Token> tokensLine;
	public static ArrayList<Token> tokensStatement = new ArrayList<Token>();
	
	public static DatatypeDict variables = new DatatypeDict();
	
	public static void main(String[] args) throws Exception {
		if (!(args[0].substring(args[0].length() - 4, args[0].length())).equals(".txt")) {
			args[0] += ".bean";
		}
		prg = new File(args[0]);
		scn = new Scanner(prg);
		
		while (scn.hasNextLine()) {
			line = scn.nextLine();
			lines.add(line);
		}
		
		BeanLexer lexer;
		BeanParser parser;
		
		while (lineNum <= lines.size()) {
			line = lines.get(lineNum - 1);
			
			if (line.length() > 0) {
				if (line.charAt(0) == 47 && line.charAt(1) == 47) {
					continue;
				}
				tokensLine = new ArrayList<Token>();
				lexer = new BeanLexer(line.trim());
				lexer.run();
				
				Token curToken = tokensLine.get(0);
				Token prevToken = new EmptyToken();
				boolean parseLine = true;
				
				while (parseLine) {
					if (curToken instanceof WhileToken) {
						parser = new BeanParser(tokensLine);
						parser.run();
						tokensStatement = new ArrayList<Token>();
						parseLine = false;
						break;
					}
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
						parser = new BeanParser(tokensStatement);
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
		System.out.print("Line " + lineNum + ": ");
		System.out.println(s);
		System.out.println(line);
		System.exit(0);
	}
}
