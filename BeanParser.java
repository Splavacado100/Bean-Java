import java.util.ArrayList;

public class BeanParser {
	private ArrayList<Token> tokenList;
	private int size;
	
	public BeanParser(ArrayList<Token> tokenList) {
		this.tokenList = tokenList;
		this.size = tokenList.size();
	}
	
	public void run() {
		//print function
		if (this.tokenList.get(0) instanceof PrintToken && this.tokenList.get(1) instanceof OpenParenthesesToken && this.tokenList.get(this.size - 2) instanceof ClosedParenthesesToken && this.tokenList.get(this.size - 1) instanceof EmptyToken) {
			ArrayList<Token> expression = new ArrayList<Token>();
			for (int i = 2; i < this.size - 2; i++) {
				expression.add(tokenList.get(i));
			}
			Token out = expr(expression);
			System.out.println("" + out);
		}
		//variable declaration
		else if (this.tokenList.get(0).type.equals("TYPE") && this.tokenList.get(1) instanceof VariableToken && this.tokenList.get(2) instanceof EmptyToken && this.tokenList.get(tokenList.size() - 1) instanceof EmptyToken) {
			String name = ((VariableToken)this.tokenList.get(1)).value;
			Token type = this.tokenList.get(0);
			if (type instanceof IntTypeToken) {
				BeanInterpreter.variables.addVar(new IntegerToken(), name);
			} else if (type instanceof DoubleTypeToken) {
				BeanInterpreter.variables.addVar(new DoubleToken(), name);
			} else if (type instanceof StrTypeToken) {
				BeanInterpreter.variables.addVar(new StringToken(), name);
			} else if (type instanceof CharTypeToken) {
				BeanInterpreter.variables.addVar(new CharacterToken(), name);
			} else if (type instanceof BoolTypeToken) {
				BeanInterpreter.variables.addVar(new BooleanToken(), name);
			} else {
				//Unreachable state
				BeanInterpreter.exception("Unidentified type");
			}
		}
		//variable declaration and assignment
		else if (this.tokenList.get(0).type.equals("TYPE") && this.tokenList.get(1) instanceof VariableToken && this.tokenList.get(2) instanceof EqualsToken && this.tokenList.get(this.size - 1) instanceof EmptyToken) {
			ArrayList<Token> expression = new ArrayList<Token>();
			for (int i = 3; i < this.size - 1; i++) {
				expression.add(tokenList.get(i));
			}
			Token out = expr(expression);
			String name = ((VariableToken)this.tokenList.get(1)).value;
			Token type = this.tokenList.get(0);
			
			if (type instanceof IntTypeToken) {
				out = DatatypeDict.cInt(out);
				BeanInterpreter.variables.addVar(out, name);
			} else if (type instanceof DoubleTypeToken) {
				out = DatatypeDict.cDouble(out);
				BeanInterpreter.variables.addVar(out, name);
			} else if (type instanceof StrTypeToken) {
				out = DatatypeDict.cStr(out);
				BeanInterpreter.variables.addVar(out, name);
			} else if (type instanceof CharTypeToken) {
				out = DatatypeDict.cChar(out);
				BeanInterpreter.variables.addVar(out, name);
			} else if (type instanceof BoolTypeToken) {
				out = DatatypeDict.cBool(out);
				BeanInterpreter.variables.addVar(out, name);
			} else {
				//Unreachable state
				BeanInterpreter.exception("Unidentified type");
			}
		}
		//variable assignment
		else if (this.tokenList.get(0) instanceof VariableToken && this.tokenList.get(1).type.equals("ASSIGN") && this.tokenList.get(this.size - 1) instanceof EmptyToken) {
			ArrayList<Token> expression = new ArrayList<Token>();
			for (int i = 2; i < this.size - 1; i++) {
				expression.add(tokenList.get(i));
			}
			Token out = expr(expression);
			String name = ((VariableToken)this.tokenList.get(0)).value;
			Token type = BeanInterpreter.variables.getVar(name);
			
			if (type instanceof IntegerToken) {
				out = DatatypeDict.cInt(out);
				BeanInterpreter.variables.setVar(out, name);
			} else if (type instanceof DoubleToken) {
				out = DatatypeDict.cDouble(out);
				BeanInterpreter.variables.setVar(out, name);
			} else if (type instanceof StringToken) {
				out = DatatypeDict.cStr(out);
				BeanInterpreter.variables.setVar(out, name);
			} else if (type instanceof CharacterToken) {
				out = DatatypeDict.cChar(out);
				BeanInterpreter.variables.setVar(out, name);
			} else if (type instanceof BooleanToken) {
				out = DatatypeDict.cBool(out);
				BeanInterpreter.variables.setVar(out, name);
			} else {
				//Unreachable state
				BeanInterpreter.exception("Unidentified type");
			}
		}
		else {
			BeanInterpreter.exception("BAD STATEMENT");
		}
	}
	
	private Token expr(ArrayList<Token> expression) {
		if (expression.size() == 1) {
			return expression.get(0);
		}
		int index;
		
		//VARIABLE REMOVAL
		index = findVars(expression);
		while (index != -1) {
			Token replace = BeanInterpreter.variables.getVar(((VariableToken)expression.get(index)).value);
			expression.set(index, replace);
			index = findVars(expression);
		}
		
		this.checkParentheses(expression);
		Token eval;
		ArrayList<Token> temp;
		int open;
		int closed;
		
		//Parentheses
		open = this.findOpen(expression);
		closed = this.findClosed(expression, open);
		while (open != -1) {
			temp = new ArrayList<Token>();
			for (int i = open + 1; i < closed; i++) {
				temp.add(expression.get(i));
			}
			eval = this.expr(temp);
			for (int i = 0; i < closed - open + 1; i++) {
				expression.remove(open);
			}
			expression.add(open, eval);
			open = this.findOpen(expression);
			closed = this.findClosed(expression, open);
		}
		
		//casting
		index = first13(expression);
		while (index != -1) {
			Token type = expression.get(index);
			Token out = expression.get(index + 1);
			
			if (type instanceof IntTypeToken) {
				out = DatatypeDict.mInt(out);
			} else if (type instanceof DoubleTypeToken) {
				out = DatatypeDict.mDouble(out);
			} else if (type instanceof StrTypeToken) {
				out = DatatypeDict.mStr(out);
			} else if (type instanceof CharTypeToken) {
				out = DatatypeDict.mChar(out);
			} else if (type instanceof BoolTypeToken) {
				out = DatatypeDict.mBool(out);
			} else {
				//Unreachable state
				BeanInterpreter.exception("Unidentified type");
			}
			expression.remove(index);
			expression.set(index, out);
			index = first13(expression);
		}
		
		Token one;
		Token two;
		
		//MULTIPLICATION, DIVISION, AND MOD
		index = this.first12(expression);
		while (index != -1) {
			one = expression.get(index - 1);
			two = expression.get(index + 1);
			//MULTIPLY
			if (expression.get(index) instanceof MultiToken) {
				if (one instanceof DoubleToken || two instanceof DoubleToken) {
					Token oneNew = DatatypeDict.aDouble(one);
					Token twoNew = DatatypeDict.aDouble(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be multiplied by " + two.type);
					}
					double newVal = ((DoubleToken)oneNew).value * ((DoubleToken)twoNew).value;
					expression.set(index, new DoubleToken(newVal));
				} else if (one instanceof IntegerToken || two instanceof IntegerToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be multiplied by " + two.type);
					}
					int newVal = ((IntegerToken)oneNew).value * ((IntegerToken)twoNew).value;
					expression.set(index, new IntegerToken(newVal));
				} else if (one instanceof CharacterToken || two instanceof CharacterToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be multiplied by " + two.type);
					}
					int newVal = ((IntegerToken)oneNew).value * ((IntegerToken)twoNew).value;
					expression.set(index, new IntegerToken(newVal));
				} else {
					BeanInterpreter.exception(one.type + " cannot be multiplied by " + two.type);
				}
			}
			//DIVIDE
			else if (expression.get(index) instanceof DivToken) {
				if (one instanceof DoubleToken || two instanceof DoubleToken) {
					Token oneNew = DatatypeDict.aDouble(one);
					Token twoNew = DatatypeDict.aDouble(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be divided by " + two.type);
					}
					double newVal = ((DoubleToken)oneNew).value / ((DoubleToken)twoNew).value;
					expression.set(index, new DoubleToken(newVal));
				} else if (one instanceof IntegerToken || two instanceof IntegerToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be divided by " + two.type);
					}
					int newVal = ((IntegerToken)oneNew).value / ((IntegerToken)twoNew).value;
					expression.set(index, new IntegerToken(newVal));
				} else if (one instanceof CharacterToken || two instanceof CharacterToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be divided by " + two.type);
					}
					int newVal = ((IntegerToken)oneNew).value / ((IntegerToken)twoNew).value;
					expression.set(index, new IntegerToken(newVal));
				} else {
					BeanInterpreter.exception(one.type + " cannot be divided by " + two.type);
				}
			}
			//MOD
			else if (expression.get(index) instanceof ModToken) {
				if (one instanceof DoubleToken || two instanceof DoubleToken) {
					Token oneNew = DatatypeDict.aDouble(one);
					Token twoNew = DatatypeDict.aDouble(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be modded by " + two.type);
					}
					double newVal = ((DoubleToken)oneNew).value % ((DoubleToken)twoNew).value;
					expression.set(index, new DoubleToken(newVal));
				} else if (one instanceof IntegerToken || two instanceof IntegerToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be modded by " + two.type);
					}
					int newVal = ((IntegerToken)oneNew).value % ((IntegerToken)twoNew).value;
					expression.set(index, new IntegerToken(newVal));
				} else if (one instanceof CharacterToken || two instanceof CharacterToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be modded by " + two.type);
					}
					int newVal = ((IntegerToken)oneNew).value % ((IntegerToken)twoNew).value;
					expression.set(index, new IntegerToken(newVal));
				} else {
					BeanInterpreter.exception(one.type + " cannot be modded by " + two.type);
				}
			}
			expression.remove(index + 1);
			expression.remove(index - 1);
			index = this.first12(expression);
		}
		
		//PLUS, MINUS, AND STRING ADD
		index = this.first11(expression);
		while (index != -1) {
			one = expression.get(index - 1);
			two = expression.get(index + 1);
			//PLUS AND STRING ADD
			if (expression.get(index) instanceof PlusToken) {
				if (one instanceof StringToken || two instanceof StringToken) {
					String newVal = "" + one + two;
					expression.set(index, new StringToken(newVal));
				} else if (one instanceof DoubleToken || two instanceof DoubleToken) {
					Token oneNew = DatatypeDict.aDouble(one);
					Token twoNew = DatatypeDict.aDouble(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be added to " + two.type);
					}
					double newVal = ((DoubleToken)oneNew).value + ((DoubleToken)twoNew).value;
					expression.set(index, new DoubleToken(newVal));
				} else if (one instanceof IntegerToken || two instanceof IntegerToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be added to " + two.type);
					}
					int newVal = ((IntegerToken)oneNew).value + ((IntegerToken)twoNew).value;
					expression.set(index, new IntegerToken(newVal));
				} else if (one instanceof CharacterToken || two instanceof CharacterToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be added to " + two.type);
					}
					int newVal = ((IntegerToken)oneNew).value + ((IntegerToken)twoNew).value;
					expression.set(index, new IntegerToken(newVal));
				} else {
					BeanInterpreter.exception(one.type + " cannot be added to " + two.type);
				}
			}
			//MINUS
			else if (expression.get(index) instanceof DivToken) {
				if (one instanceof DoubleToken || two instanceof DoubleToken) {
					Token oneNew = DatatypeDict.aDouble(one);
					Token twoNew = DatatypeDict.aDouble(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be subtracted by " + two.type);
					}
					double newVal = ((DoubleToken)oneNew).value - ((DoubleToken)twoNew).value;
					expression.set(index, new DoubleToken(newVal));
				} else if (one instanceof IntegerToken || two instanceof IntegerToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be subtracted by " + two.type);
					}
					int newVal = ((IntegerToken)oneNew).value - ((IntegerToken)twoNew).value;
					expression.set(index, new IntegerToken(newVal));
				} else if (one instanceof CharacterToken || two instanceof CharacterToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be subtracted by " + two.type);
					}
					int newVal = ((IntegerToken)oneNew).value - ((IntegerToken)twoNew).value;
					expression.set(index, new IntegerToken(newVal));
				} else {
					BeanInterpreter.exception(one.type + " cannot be subtracted by " + two.type);
				}
			}
			expression.remove(index + 1);
			expression.remove(index - 1);
			index = this.first11(expression);
		}
		if (expression.size() > 1) {
			System.out.println(expression);
			BeanInterpreter.exception("BAD STATEMENT");
		}
		return expression.get(0);
	}
	
	private int findVars(ArrayList<Token> exprTokenList) {
		for (int i = 0; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i) instanceof VariableToken) {
				return i;
			}
		}
		return -1;
	}
	
	private void checkParentheses(ArrayList<Token> exprTokenList) {
		int open = 0;
		int close = 0;
		for (int i = 0; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i) instanceof OpenParenthesesToken) {
				open++;
			}
			if (exprTokenList.get(i) instanceof ClosedParenthesesToken) {
				close++;
			}
		}
		if (open != close) {
			BeanInterpreter.exception("Unmatched parentheses");
		}
	}
	
	private int findOpen(ArrayList<Token> exprTokenList) {
		for (int i = exprTokenList.size() - 1; i >= 0; i--) {
			if (exprTokenList.get(i) instanceof OpenParenthesesToken) {
				return i;
			}
		}
		return -1;
	}
	
	private int findClosed(ArrayList<Token> exprTokenList, int start) {
		if (start == -1) {
			return -1;
		}
		for (int i = start; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i) instanceof ClosedParenthesesToken) {
				return i;
			}
		}
		BeanInterpreter.exception("Unmatched parentheses");
		return -1;
	}
	
	private int first15(ArrayList<Token> exprTokenList) {
		for (int i = 0; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i) instanceof ExpoToken) {
				return i;
			}
		}
		return -1;
	}
	
	private int first13(ArrayList<Token> exprTokenList) {
		for (int i = 0; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i).type.equals("TYPE")) {
				return i;
			}
		}
		return -1;
	}
	
	private int first12(ArrayList<Token> exprTokenList) {
		for (int i = 0; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i) instanceof MultiToken || exprTokenList.get(i) instanceof DivToken || exprTokenList.get(i) instanceof ModToken) {
				return i;
			}
		}
		return -1;
	}
	
	private int first11(ArrayList<Token> exprTokenList) {
		for (int i = 0; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i) instanceof PlusToken || exprTokenList.get(i) instanceof MinusToken) {
				return i;
			}
		}
		return -1;
	}
}
