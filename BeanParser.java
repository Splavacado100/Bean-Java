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
			
			//Augmented assignment
			
			if (this.tokenList.get(1) instanceof MultiEqualsToken) {
				ArrayList<Token> temp = new ArrayList<Token>();
				temp.add(this.tokenList.get(0));
				temp.add(new MultiToken());
				temp.add(out);
				out = expr(temp);
			} else if (this.tokenList.get(1) instanceof DivEqualsToken) {
				ArrayList<Token> temp = new ArrayList<Token>();
				temp.add(this.tokenList.get(0));
				temp.add(new DivToken());
				temp.add(out);
				out = expr(temp);
			} else if (this.tokenList.get(1) instanceof ModEqualsToken) {
				ArrayList<Token> temp = new ArrayList<Token>();
				temp.add(this.tokenList.get(0));
				temp.add(new ModToken());
				temp.add(out);
				out = expr(temp);
			} else if (this.tokenList.get(1) instanceof PlusEqualsToken) {
				ArrayList<Token> temp = new ArrayList<Token>();
				temp.add(this.tokenList.get(0));
				temp.add(new PlusToken());
				temp.add(out);
				out = expr(temp);
			} else if (this.tokenList.get(1) instanceof MinusEqualsToken) {
				ArrayList<Token> temp = new ArrayList<Token>();
				temp.add(this.tokenList.get(0));
				temp.add(new MinusToken());
				temp.add(out);
				out = expr(temp);
			} else {
				//Regular equals
			}
			
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
		//While loop
		else if (this.tokenList.get(0) instanceof WhileToken && this.tokenList.get(1) instanceof OpenParenthesesToken && this.tokenList.get(this.size - 2) instanceof ClosedParenthesesToken && this.tokenList.get(this.size - 1) instanceof OpenBraceToken) {
			BeanInterpreter.curScope++;
			ArrayList<Token> condition = new ArrayList<Token>();
			for (int i = 2; i < this.size - 2; i++) {
				condition.add(tokenList.get(i));
			}
			boolean result = ((BooleanToken)DatatypeDict.aBool(this.expr(condition))).value;
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
		
		//not
		index = first14(expression);
		while (index != -1) {
			Token out = expression.get(index + 1);
			Token outNew = DatatypeDict.aBool(out);
			if (outNew instanceof EOFToken) {
				BeanInterpreter.exception(out.type + " must be type BOOLEAN");
			}
			boolean newVal = !((BooleanToken)outNew).value;
			expression.remove(index);
			expression.set(index, new BooleanToken(newVal));
			index = first14(expression);
			
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
		
		//LESS/GTR THAN, LESS/GTR THAN OR EQUALS TO
		index = this.first9(expression);
		while (index != -1) {
			one = expression.get(index - 1);
			two = expression.get(index + 1);
			//LESS THAN: <
			if (expression.get(index) instanceof LessToken) {
				if (one instanceof DoubleToken || two instanceof DoubleToken) {
					Token oneNew = DatatypeDict.aDouble(one);
					Token twoNew = DatatypeDict.aDouble(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((DoubleToken)oneNew).value < ((DoubleToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof IntegerToken || two instanceof IntegerToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value < ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof CharacterToken || two instanceof CharacterToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value < ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else {
					BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
				}
			}
			//GREATER THAN: >
			else if (expression.get(index) instanceof GreaterToken) {
				if (one instanceof DoubleToken || two instanceof DoubleToken) {
					Token oneNew = DatatypeDict.aDouble(one);
					Token twoNew = DatatypeDict.aDouble(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((DoubleToken)oneNew).value > ((DoubleToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof IntegerToken || two instanceof IntegerToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value > ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof CharacterToken || two instanceof CharacterToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value > ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else {
					BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
				}
			}
			//LESS THAN EQUALS: <=
			else if (expression.get(index) instanceof LessToken) {
				if (one instanceof DoubleToken || two instanceof DoubleToken) {
					Token oneNew = DatatypeDict.aDouble(one);
					Token twoNew = DatatypeDict.aDouble(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((DoubleToken)oneNew).value <= ((DoubleToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof IntegerToken || two instanceof IntegerToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value <= ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof CharacterToken || two instanceof CharacterToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value <= ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else {
					BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
				}
			}
			//GREATER THAN EQUALS: >=
			else if (expression.get(index) instanceof GreaterToken) {
				if (one instanceof DoubleToken || two instanceof DoubleToken) {
					Token oneNew = DatatypeDict.aDouble(one);
					Token twoNew = DatatypeDict.aDouble(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((DoubleToken)oneNew).value >= ((DoubleToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof IntegerToken || two instanceof IntegerToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value >= ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof CharacterToken || two instanceof CharacterToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value >= ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else {
					BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
				}
			}
			expression.remove(index + 1);
			expression.remove(index - 1);
			index = this.first9(expression);
		}
		
		//EQUALS TO, NOT EQUALS TO
		index = this.first8(expression);
		while (index != -1) {
			one = expression.get(index - 1);
			two = expression.get(index + 1);
			//EQUALS TO: ==
			if (expression.get(index) instanceof EqualsEqualsToken) {
				if (one instanceof DoubleToken || two instanceof DoubleToken) {
					Token oneNew = DatatypeDict.aDouble(one);
					Token twoNew = DatatypeDict.aDouble(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((DoubleToken)oneNew).value == ((DoubleToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof IntegerToken || two instanceof IntegerToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value == ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof CharacterToken || two instanceof CharacterToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value == ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof StringToken && two instanceof StringToken) {
					String oneNew = "" + one;
					String twoNew = "" + two;
					expression.set(index, new BooleanToken(oneNew.equals(twoNew)));
				} else {
					BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
				}
			}
			//NOT EQUALS TO: !=
			else if (expression.get(index) instanceof NotEqualsToken) {
				if (one instanceof DoubleToken || two instanceof DoubleToken) {
					Token oneNew = DatatypeDict.aDouble(one);
					Token twoNew = DatatypeDict.aDouble(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((DoubleToken)oneNew).value != ((DoubleToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof IntegerToken || two instanceof IntegerToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value != ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof CharacterToken || two instanceof CharacterToken) {
					Token oneNew = DatatypeDict.aInt(one);
					Token twoNew = DatatypeDict.aInt(two);
					if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
						BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
					}
					boolean newVal = ((IntegerToken)oneNew).value != ((IntegerToken)twoNew).value;
					expression.set(index, new BooleanToken(newVal));
				} else if (one instanceof StringToken && two instanceof StringToken) {
					String oneNew = "" + one;
					String twoNew = "" + two;
					expression.set(index, new BooleanToken(!oneNew.equals(twoNew)));
				} else {
					BeanInterpreter.exception(one.type + " cannot be compared to " + two.type);
				}
			}
			expression.remove(index + 1);
			expression.remove(index - 1);
			index = this.first8(expression);
		}
		
		//AND
		index = this.first4(expression);
		while (index != -1) {
			one = expression.get(index - 1);
			two = expression.get(index + 1);
			
			Token oneNew = DatatypeDict.aBool(one);
			Token twoNew = DatatypeDict.aBool(two);
			
			if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
				BeanInterpreter.exception(one.type + " cannot be ANDed by " + two.type);
			}
			
			boolean newVal = ((BooleanToken)oneNew).value && ((BooleanToken)twoNew).value;
			
			expression.set(index, new BooleanToken(newVal));
			
			expression.remove(index + 1);
			expression.remove(index - 1);
			index = this.first4(expression);
		}
		
		//OR
		index = this.first3(expression);
		while (index != -1) {
			one = expression.get(index - 1);
			two = expression.get(index + 1);
			
			Token oneNew = DatatypeDict.aBool(one);
			Token twoNew = DatatypeDict.aBool(two);
			
			if (oneNew instanceof EOFToken || twoNew instanceof EOFToken) {
				BeanInterpreter.exception(one.type + " cannot be ORed by " + two.type);
			}
			
			boolean newVal = ((BooleanToken)oneNew).value || ((BooleanToken)twoNew).value;
			
			expression.set(index, new BooleanToken(newVal));
			
			expression.remove(index + 1);
			expression.remove(index - 1);
			index = this.first3(expression);
		}
		
		if (expression.size() > 1) {
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
	
	private int first14(ArrayList<Token> exprTokenList) {
		for (int i = 0; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i) instanceof NotToken) {
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
	
	private int first9(ArrayList<Token> exprTokenList) {
		for (int i = 0; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i).type.equals("CMP")) {
				return i;
			}
		}
		return -1;
	}
	
	private int first8(ArrayList<Token> exprTokenList) {
		for (int i = 0; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i).type.equals("TST")) {
				return i;
			}
		}
		return -1;
	}
	
	private int first4(ArrayList<Token> exprTokenList) {
		for (int i = 0; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i) instanceof AndToken) {
				return i;
			}
		}
		return -1;
	}
	
	private int first3(ArrayList<Token> exprTokenList) {
		for (int i = 0; i < exprTokenList.size(); i++) {
			if (exprTokenList.get(i) instanceof OrToken) {
				return i;
			}
		}
		return -1;
	}
}
