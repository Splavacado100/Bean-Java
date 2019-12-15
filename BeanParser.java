import java.util.ArrayList;

public class BeanParser {
	private ArrayList<Token> tokenList;
	private DatatypeDict vars;
	int lineNum;
	private int size;
	
	public BeanParser(ArrayList<Token> tokenList, DatatypeDict vars, int lineNum) {
		this.tokenList = tokenList;
		this.vars = vars;
		this.lineNum = lineNum;
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
			if (out instanceof IntegerToken) {
				System.out.println(((IntegerToken)out).value);
			} else if (out instanceof DoubleToken) {
				System.out.println(((DoubleToken)out).value);
			} else if (out instanceof StringToken) {
				System.out.println(((StringToken)out).value);
			} else if (out instanceof CharacterToken) {
				System.out.println(((CharacterToken)out).value);
			} else if (out instanceof TrueToken) {
				System.out.println("true");
			} else if (out instanceof FalseToken) {
				System.out.println("false");
			} else {
				this.exception("BAD TYPE");
			}
		}
		/*
		else if (this.tokenList.get(0) instanceof PrintlnToken && this.tokenList.get(1) instanceof OpenParenthesesToken && this.tokenList.get(this.size - 2) instanceof ClosedParenthesesToken && this.tokenList.get(this.size - 1) instanceof EmptyToken) {
			ArrayList<Token> expression = new ArrayList<Token>();
			for (int i = 2; i < this.size - 2; i++) {
				expression.add(tokenList.get(i));
			}
			Token out = expr(expression);
			if (out instanceof IntegerToken) {
				System.out.println(((IntegerToken)out).value);
			} else if (out instanceof DoubleToken) {
				System.out.println(((DoubleToken)out).value);
			} else if (out instanceof StringToken) {
				System.out.println(((StringToken)out).value);
			} else if (out instanceof CharacterToken) {
				System.out.println(((CharacterToken)out).value);
			} else if (out instanceof TrueToken) {
				System.out.println("true");
			} else if (out instanceof FalseToken) {
				System.out.println("false");
			} else {
				this.exception("BAD TYPE");
			}
		}
		*/
		//variable declaration with default assignment
		else if (this.tokenList.get(0).type.equals("TYPE") && this.tokenList.get(1) instanceof VariableToken && this.tokenList.get(2) instanceof EmptyToken && this.tokenList.get(tokenList.size() - 1) instanceof EmptyToken) {
			String name = ((VariableToken)this.tokenList.get(1)).value;
			Token type = this.tokenList.get(0);
			if (type instanceof IntTypeToken) {
				this.vars.addVar(new IntegerToken(), name);
			} else if (type instanceof DoubleTypeToken) {
				this.vars.addVar(new DoubleToken(), name);
			} else if (type instanceof StrTypeToken) {
				this.vars.addVar(new StringToken(), name);
			} else if (type instanceof CharTypeToken) {
				this.vars.addVar(new CharacterToken(), name);
			} else if (type instanceof BoolTypeToken) {
				this.vars.addVar(new FalseToken(), name);
			} else {
				this.exception("Unidentified type");
			}
		}
		//variable declaration and assignment
		else if (this.tokenList.get(0).type.equals("TYPE") && this.tokenList.get(1) instanceof VariableToken && this.tokenList.get(2).type.equals("ASSIGN") && this.tokenList.get(this.size - 1) instanceof EmptyToken) {
			ArrayList<Token> expression = new ArrayList<Token>();
			for (int i = 3; i < this.size - 1; i++) {
				expression.add(tokenList.get(i));
			}
			Token out = expr(expression);
			String name = ((VariableToken)this.tokenList.get(1)).value;
			Token type = this.tokenList.get(0);
			if (type instanceof IntTypeToken) {
				if (out instanceof IntegerToken) {
					this.vars.addVar(out, name);
				} else if (out instanceof DoubleToken) {
					this.exception("Lossy conversion - type double to int");
				} else if (out instanceof CharacterToken) {
					int value = ((CharacterToken)out).value;
					out = new IntegerToken(value);
					this.vars.addVar(out, name);
				} else {
					this.exception("Invalid type coversion");
				}
			} else if (type instanceof DoubleTypeToken) {
				if (out instanceof IntegerToken) {
					double value = ((IntegerToken)out).value;
					out = new DoubleToken(value);
					this.vars.addVar(out, name);
				} else if (out instanceof DoubleToken) {
					this.vars.addVar(out, name);
				} else if (out instanceof CharacterToken) {
					double value = ((CharacterToken)out).value;
					out = new DoubleToken(value);
					this.vars.addVar(out, name);
				} else {
					this.exception("Invalid type coversion");
				}
			} else if (type instanceof StrTypeToken) {
				if (out instanceof StringToken) {
					this.vars.addVar(out, name);
				} else {
					this.exception("Invalid type conversion");
				}
			} else if (type instanceof CharTypeToken) {
				if (out instanceof IntegerToken) {
					char value = (char)(((IntegerToken)out).value);
					out = new CharacterToken(value);
					this.vars.addVar(out, name);
				} else if (out instanceof CharacterToken) {
					this.vars.addVar(out, name);
				} else {
					this.exception("Invalid type coversion");
				}
			} else if (type instanceof BoolTypeToken) {
				if (out instanceof TrueToken) {
					this.vars.addVar(out, name);
				} else if (out instanceof FalseToken) {
					this.vars.addVar(out, name);
				} else {
					this.exception("Invalid type coversion");
				}
			} else {
				this.exception("Unidentified type");
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
			Token type = this.vars.getVar(name);
			if (type instanceof IntegerToken) {
				if (out instanceof IntegerToken) {
					this.vars.setVar(out, name);
				} else if (out instanceof DoubleToken) {
					this.exception("Lossy conversion - type double to int");
				} else if (out instanceof CharacterToken) {
					int value = ((CharacterToken)out).value;
					out = new IntegerToken(value);
					this.vars.setVar(out, name);
				} else {
					this.exception("Invalid type coversion");
				}
			} else if (type instanceof DoubleToken) {
				if (out instanceof IntegerToken) {
					double value = ((IntegerToken)out).value;
					out = new DoubleToken(value);
					this.vars.setVar(out, name);
				} else if (out instanceof DoubleToken) {
					this.vars.setVar(out, name);
				} else if (out instanceof CharacterToken) {
					double value = ((CharacterToken)out).value;
					out = new DoubleToken(value);
					this.vars.setVar(out, name);
				} else {
					this.exception("Invalid type coversion");
				}
			} else if (type instanceof StringToken) {
				if (out instanceof StringToken) {
					this.vars.setVar(out, name);
				} else {
					this.exception("Invalid type conversion");
				}
			} else if (type instanceof CharacterToken) {
				if (out instanceof IntegerToken) {
					char value = (char)(((IntegerToken)out).value);
					out = new CharacterToken(value);
					this.vars.setVar(out, name);
				} else if (out instanceof CharacterToken) {
					this.vars.setVar(out, name);
				} else {
					this.exception("Invalid type coversion");
				}
			} else if (type instanceof TrueToken || type instanceof FalseToken) {
				if (out instanceof TrueToken) {
					this.vars.setVar(out, name);
				} else if (out instanceof FalseToken) {
					this.vars.setVar(out, name);
				} else {
					this.exception("Invalid type coversion");
				}
			} else {
				this.exception("Unidentified type");
			}
		}
		else {
			this.exception("BAD STATEMENT");
		}
	}
	
	private Token expr(ArrayList<Token> expression) {
		int index;
		
		//VARIABLE REMOVAL
		index = findVars(expression);
		while (index != -1) {
			Token replace = this.vars.getVar(((VariableToken)expression.get(index)).value);
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
		
		Token one;
		Token two;
		
		//EXPONENT
		index = this.first15(expression);
		while (index != -1) {
			one = expression.get(index - 1);
			two = expression.get(index + 1);
			if (one instanceof IntegerToken) {
				if (two instanceof IntegerToken) {
					int newValue = (int)Math.pow(((IntegerToken)one).value, ((IntegerToken)two).value);;
					expression.set(index, new IntegerToken(newValue));
					expression.remove(index + 1);
					expression.remove(index - 1);
				} else if (two instanceof DoubleToken) {
					double newValue = Math.pow(((IntegerToken)one).value, ((DoubleToken)two).value);
					expression.set(index, new DoubleToken(newValue));
					expression.remove(index + 1);
					expression.remove(index - 1);
				} else if (two instanceof CharacterToken) {
					int newValue = (int)Math.pow(((IntegerToken)one).value, (int)(((CharacterToken)two).value));;
					expression.set(index, new IntegerToken(newValue));
					expression.remove(index + 1);
					expression.remove(index - 1);
				} else {
					this.exception("Bad type for binary operator '**'");
				}		
			} else if (one instanceof DoubleToken) {
				if (two instanceof IntegerToken) {
					double newValue = Math.pow(((DoubleToken)one).value, ((IntegerToken)two).value);
					expression.set(index, new DoubleToken(newValue));
					expression.remove(index + 1);
					expression.remove(index - 1);
				} else if (two instanceof DoubleToken) {
					double newValue = Math.pow(((DoubleToken)one).value, ((DoubleToken)two).value);
					expression.set(index, new DoubleToken(newValue));
					expression.remove(index + 1);
					expression.remove(index - 1);
				} else if (two instanceof CharacterToken) {
					double newValue = (int)Math.pow(((DoubleToken)one).value, (int)(((CharacterToken)two).value));;
					expression.set(index, new DoubleToken(newValue));
					expression.remove(index + 1);
					expression.remove(index - 1);
				} else {
					this.exception("Bad type for binary operator '**'");
				}
			} else if (one instanceof CharacterToken) {
				if (two instanceof IntegerToken) {
					int newValue = (int)Math.pow((int)(((CharacterToken)one).value), ((IntegerToken)two).value);
					expression.set(index, new DoubleToken(newValue));
					expression.remove(index + 1);
					expression.remove(index - 1);
				} else if (two instanceof DoubleToken) {
					double newValue = Math.pow((int)(((CharacterToken)one).value), ((DoubleToken)two).value);
					expression.set(index, new DoubleToken(newValue));
					expression.remove(index + 1);
					expression.remove(index - 1);
				} else if (two instanceof CharacterToken) {
					int newValue = (int)Math.pow((int)(((CharacterToken)one).value), (int)(((CharacterToken)two).value));;
					expression.set(index, new IntegerToken(newValue));
					expression.remove(index + 1);
					expression.remove(index - 1);
				} else {
					this.exception("Bad type for binary operator '**'");
				}
			} else {
				this.exception("Bad type for binary operator '**'");
			}
			index = this.first15(expression);
		}
		
		//MULTIPLICATION, DIVISION, AND MOD
		index = this.first12(expression);
		while (index != -1) {
			one = expression.get(index - 1);
			two = expression.get(index + 1);
			//MULTIPLY
			if (expression.get(index) instanceof MultiToken) {
				if (one instanceof IntegerToken) {
					if (two instanceof IntegerToken) {
						int newValue = ((IntegerToken)one).value * ((IntegerToken)two).value;
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = ((IntegerToken)one).value * ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof StringToken) {
						String newString = "";
						String oldString = ((StringToken)two).value;
						int limit = ((IntegerToken)one).value;
						for (int i = 0; i < limit; i++) {
							newString += oldString;
						}
						expression.set(index, new StringToken(newString));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						int newValue = ((IntegerToken)one).value * (int)(((CharacterToken)two).value);
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '*'");
					}		
				} else if (one instanceof DoubleToken) {
					if (two instanceof IntegerToken) {
						double newValue = ((DoubleToken)one).value * ((IntegerToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = ((DoubleToken)one).value * ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						double newValue = ((DoubleToken)one).value * (int)(((CharacterToken)two).value);
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '*'");
					}
				} else if (one instanceof StringToken) {
					if (two instanceof IntegerToken) {
						String newString = "";
						String oldString = ((StringToken)one).value;
						int limit = ((IntegerToken)two).value;
						for (int i = 0; i < limit; i++) {
							newString += oldString;
						}
						expression.set(index, new StringToken(newString));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '*'");
					}
				} else if (one instanceof CharacterToken) {
					if (two instanceof IntegerToken) {
						int newValue = (int)(((CharacterToken)one).value) * ((IntegerToken)two).value;
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = (int)(((CharacterToken)one).value) * ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						int newValue = (int)(((CharacterToken)one).value) * (int)(((CharacterToken)two).value);
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '*'");
					}
				} else {
					this.exception("Bad type for binary operator '*'");
				}
			}
			//DIVIDE
			else if (expression.get(index) instanceof DivToken) {
				if (one instanceof IntegerToken) {
					if (two instanceof IntegerToken) {
						int newValue = ((IntegerToken)one).value / ((IntegerToken)two).value;
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = ((IntegerToken)one).value / ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						int newValue = ((IntegerToken)one).value / (int)(((CharacterToken)two).value);
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '" + (char)47 + "'");
					}		
				} else if (one instanceof DoubleToken) {
					if (two instanceof IntegerToken) {
						double newValue = ((DoubleToken)one).value / ((IntegerToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = ((DoubleToken)one).value / ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						double newValue = ((DoubleToken)one).value / (int)(((CharacterToken)two).value);
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '" + (char)47 + "'");
					}
				} else if (one instanceof CharacterToken) {
					if (two instanceof IntegerToken) {
						int newValue = (int)(((CharacterToken)one).value) / ((IntegerToken)two).value;
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = (int)(((CharacterToken)one).value) / ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						int newValue = (int)(((CharacterToken)one).value) / (int)(((CharacterToken)two).value);
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '" + (char)47 + "'");
					}
				} else {
					this.exception("Bad type for binary operator '" + (char)47 + "'");
				}
			}
			//MOD
			else {
				if (one instanceof IntegerToken) {
					if (two instanceof IntegerToken) {
						int newValue = ((IntegerToken)one).value % ((IntegerToken)two).value;
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = ((IntegerToken)one).value % ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						int newValue = ((IntegerToken)one).value % (int)(((CharacterToken)two).value);
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '" + (char)37 + "'");
					}		
				} else if (one instanceof DoubleToken) {
					if (two instanceof IntegerToken) {
						double newValue = ((DoubleToken)one).value % ((IntegerToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = ((DoubleToken)one).value % ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						double newValue = ((DoubleToken)one).value % (int)(((CharacterToken)two).value);
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '" + (char)37 + "'");
					}
				} else if (one instanceof CharacterToken) {
					if (two instanceof IntegerToken) {
						int newValue = (int)(((CharacterToken)one).value) % ((IntegerToken)two).value;
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = (int)(((CharacterToken)one).value) % ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						int newValue = (int)(((CharacterToken)one).value) % (int)(((CharacterToken)two).value);
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '" + (char)37 + "'");
					}
				} else {
					this.exception("Bad type for binary operator '" + (char)37 + "'");
				}
			}
			index = this.first12(expression);
		}
		
		//ADDING AND SUBTRACTING
		index = this.first11(expression);
		while (index != -1) {
			one = expression.get(index - 1);
			two = expression.get(index + 1);
			//ADDING
			if (expression.get(index) instanceof PlusToken) {
				if (one instanceof IntegerToken) {
					if (two instanceof IntegerToken) {
						int newValue = ((IntegerToken)one).value + ((IntegerToken)two).value;
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = ((IntegerToken)one).value + ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof StringToken) {
						String newValue = ((IntegerToken)one).value + ((StringToken)two).value;
						expression.set(index, new StringToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						int newValue = ((IntegerToken)one).value + (int)(((CharacterToken)two).value);
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '+'");
					}
				} else if (one instanceof DoubleToken) {
					if (two instanceof IntegerToken) {
						double newValue = ((DoubleToken)one).value + ((IntegerToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = ((DoubleToken)one).value + ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof StringToken) {
						String newValue = ((DoubleToken)one).value + ((StringToken)two).value;
						expression.set(index, new StringToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						double newValue = ((DoubleToken)one).value + (int)(((CharacterToken)two).value);
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '+'");
					}
				} else if (one instanceof StringToken) {
					if (two instanceof IntegerToken) {
						String newValue = ((StringToken)one).value + ((IntegerToken)two).value;
						expression.set(index, new StringToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						String newValue = ((StringToken)one).value + ((DoubleToken)two).value;
						expression.set(index, new StringToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof StringToken) {
						String newValue = ((StringToken)one).value + ((StringToken)two).value;
						expression.set(index, new StringToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						String newValue = ((StringToken)one).value + ((CharacterToken)two).value;
						expression.set(index, new StringToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof TrueToken) {
						String newValue = ((StringToken)one).value + "true";
						expression.set(index, new StringToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof FalseToken) {
						String newValue = ((StringToken)one).value + "false";
						expression.set(index, new StringToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '+'");
					}
				} else if (one instanceof CharacterToken) {
					if (two instanceof IntegerToken) {
						int newValue = (int)(((CharacterToken)one).value) + ((IntegerToken)two).value;
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = (int)(((CharacterToken)one).value) + ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof StringToken) {
						String newValue = ((CharacterToken)one).value + ((StringToken)two).value;
						expression.set(index, new StringToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						int newValue = (int)(((CharacterToken)one).value) + (int)(((CharacterToken)two).value);
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '" + (char)47 + "'");
					}
				} else if (one instanceof TrueToken) {
					if (two instanceof StringToken) {
						String newValue = "true" + ((StringToken)two).value;
						expression.set(index, new StringToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '+'");
					}
				} else if (one instanceof FalseToken) {
					if (two instanceof StringToken) {
						String newValue = "false" + ((StringToken)two).value;
						expression.set(index, new StringToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '+'");
					}
				} else {
					this.exception("Bad type for binary operator '+'");
				}
			}
			//SUBTRACTING
			else {
				if (one instanceof IntegerToken) {
					if (two instanceof IntegerToken) {
						int newValue = ((IntegerToken)one).value - ((IntegerToken)two).value;
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = ((IntegerToken)one).value - ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '-'");
					}	
				} else if (one instanceof DoubleToken) {
					if (two instanceof IntegerToken) {
						double newValue = ((DoubleToken)one).value - ((IntegerToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = ((DoubleToken)one).value - ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '-'");
					}	
				} else if (one instanceof CharacterToken) {
					if (two instanceof IntegerToken) {
						int newValue = (int)(((CharacterToken)one).value) - ((IntegerToken)two).value;
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof DoubleToken) {
						double newValue = (int)(((CharacterToken)one).value) - ((DoubleToken)two).value;
						expression.set(index, new DoubleToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else if (two instanceof CharacterToken) {
						int newValue = (int)(((CharacterToken)one).value) - (int)(((CharacterToken)two).value);
						expression.set(index, new IntegerToken(newValue));
						expression.remove(index + 1);
						expression.remove(index - 1);
					} else {
						this.exception("Bad type for binary operator '-'");
					}
				} else {
					this.exception("Bad type for binary operator '-'");
				}
			}
			index = this.first11(expression);
		}
		if (expression.size() > 1) {
			this.exception("BAD STATEMENT");
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
			this.exception("Unmatched parentheses");
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
		this.exception("Unmatched parentheses");
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
	
	private void exception(String s) {
		System.out.print("Line " + this.lineNum + ": ");
		System.out.println(s);
		System.exit(0);
	}
}
