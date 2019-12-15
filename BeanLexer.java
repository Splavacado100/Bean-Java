public class BeanLexer {
	private String line;
	private int pos;
	private Token currentToken;
	private char currentChar;
	
	public BeanLexer(String line) {
		this.line = line;
		this.pos = 0;
		this.currentChar = this.line.charAt(this.pos);
	}
	
	private boolean isSpace(char c) {
		return c == ' ';
	}
	
	private boolean isLetter(char c) {
		if ((c > 64 & c < 91) || (c > 96 && c < 123)) {
			return true;
		}
		return false;
	}
	
	private boolean isAccepted(char c) {
		if (c == 95) {
			return true;
		}
		return false;
	}
	
	private boolean isChar(char c) {
		if (c > 31 && c < 127) {
			return true;
		}
		return false;
	}
	
	private boolean isDigit(char c) {
		if (c > 47 && c < 58) {
			return true;
		}
		return false;
	}
	
	private void advance() {
		this.pos++;
		if (this.pos > this.line.length() - 1) {
			this.currentChar = 0;
		} else {
			this.currentChar = this.line.charAt(this.pos);
		}
	}
	
	private void skipWhitespace() {
		while(this.currentChar != 0 && this.currentChar == ' ') {
			this.advance();
		}
	}
	
	private int getInteger() {
		String result = "";
		while(this.currentChar != 0 && this.isDigit(this.currentChar)) {
			result += this.currentChar;
			this.advance();
		}
		return Integer.parseInt(result);
	}
	
	private double getDouble() {
		String result = "";
		boolean inDecimal = false;
		while(this.currentChar != 0 && this.isDigit(this.currentChar)) {
			result += this.currentChar;
			this.advance();
			if (this.currentChar == '.') {
				result += '.';
				this.advance();
			}
		}
		return Double.parseDouble(result);
	}
	
	private boolean isDouble() {
		int backtrackPos = this.pos;
		boolean inDecimal = false;
		while(this.currentChar != 0 && this.isDigit(this.currentChar)) {
			this.advance();
			if (this.currentChar == '.') {
				inDecimal = true;
				this.advance();
			}
		}
		this.pos = backtrackPos;
		this.currentChar = this.line.charAt(this.pos);
		return inDecimal;
	}
	
	private String getName() {
		String result = "";
		while(this.currentChar != 0 && (this.isLetter(this.currentChar) || this.isAccepted(this.currentChar))) {
			result += this.currentChar;
			this.advance();
		}
		return result;
	}
	
	private String getString() {
		String result = "";
		while(this.currentChar != 0 && this.isChar(this.currentChar)) {
			if (this.currentChar == '"') {
				break;
			}
			if (this.currentChar == 92) {
				this.advance();
				if (this.currentChar == 'n') {
					this.currentChar = (char)10;
				} else if (this.currentChar == 't') {
					this.currentChar = (char)9;
				} else if (this.currentChar == '"') {
					this.currentChar = (char)34;
				} else if (this.currentChar == 39) {
					this.currentChar = (char)39;
				} else if (this.currentChar == 92) {
					this.currentChar = (char)92;
				} else {
					BeanInterpreter.exception("Unknown escape sequence: '\\" + this.currentChar + "'");
				}
			}
			result += this.currentChar;
			this.advance();
		}
		return result;
	}
	
	private char getChar() {
		if (this.currentChar == 92) {
			this.advance();
			if (this.currentChar == 'n') {
				this.currentChar = (char)10;
			} else if (this.currentChar == 't') {
				this.currentChar = (char)9;
			} else if (this.currentChar == '"') {
				this.currentChar = (char)34;
			} else if (this.currentChar == 39) {
				this.currentChar = (char)39;
			} else if (this.currentChar == 92) {
				this.currentChar = (char)92;
			} else {
				BeanInterpreter.exception("Unknown escape sequence: '\\" + this.currentChar + "'");
			}
		} else if (this.isChar(this.currentChar)) {
			;
		} else {
			BeanInterpreter.exception("BAD CHAR");
		}
		return this.currentChar;
	}
	
	private void eat() {
		this.currentToken = this.getNextToken();
	}
	
	private Token getNextToken() {
		while (this.currentChar != 0) {
			if (this.currentChar == ' ') {
				this.skipWhitespace();
				continue;
			}
			if (this.isDigit(this.currentChar)) {
				if (this.isDouble()) {
					return new DoubleToken(this.getDouble());
				} else {
					return new IntegerToken(this.getInteger());
				}
			}
			if (this.currentChar == '"') {
				this.advance();
				String s = this.getString();
				this.advance();
				return new StringToken(s);
			}
			if (this.currentChar == 39) {
				this.advance();
				char c = this.getChar();
				this.advance();
				if (this.currentChar != 39) {
					BeanInterpreter.exception("Unclosed character literal");
				}
				this.advance();
				return new CharacterToken(c);
			}
			if (this.isLetter(this.currentChar)) {
				String name = this.getName();
				if (name.equals("print")) {
					return new PrintToken();
				}
				/*
				if (name.equals("println")) {
					return new PrintlnToken();
				}
				*/
				if (name.equals("int")) {
					return new IntTypeToken();
				}
				if (name.equals("double")) {
					return new DoubleTypeToken();
				}
				if (name.equals("bool")) {
					return new BoolTypeToken();
				}
				if (name.equals("true")) {
					return new TrueToken();
				}
				if (name.equals("false")) {
					return new FalseToken();
				}
				if (name.equals("str")) {
					return new StrTypeToken();
				}
				if (name.equals("char")) {
					return new CharTypeToken();
				}
				if (name.equals("func")) {
					return new FuncTypeToken();
				}
				return new VariableToken(name);
			}
			if (this.currentChar == '(') {
				this.advance();
				return new OpenParenthesesToken();
			}
			if (this.currentChar == ')') {
				this.advance();
				return new ClosedParenthesesToken();
			}
			if (this.currentChar == '{') {
				this.advance();
				return new OpenBraceToken();
			}
			if (this.currentChar == '}') {
				this.advance();
				return new ClosedBraceToken();
			}
			if (this.currentChar == ',') {
				this.advance();
				return new CommaToken();
			}
			if (this.currentChar == ';') {
				this.advance();
				return new EmptyToken();
			}
			if (this.currentChar == '+') {
				this.advance();
				if (this.currentChar == '=') {
					this.advance();
					return new PlusEqualsToken();
				} else {
					return new PlusToken();
				}
			}
			if (this.currentChar == '-') {
				this.advance();
				if (this.isDigit(this.currentChar)) {
					if (this.isDouble()) {
						return new DoubleToken(-1.0 * this.getDouble());
					} else {
						return new IntegerToken(-1 * this.getInteger());
					}
				} else if (this.currentChar == '=') {
					this.advance();
					return new MinusEqualsToken();
				} else {
					return new MinusToken();
				}
			}
			if (this.currentChar == '*') {
				this.advance();
				if (this.currentChar == '=') {
					this.advance();
					return new MultiEqualsToken();
				} else if (this.currentChar == '*') {
					this.advance();
					return new ExpoToken();
				} else {
					return new MultiToken();
				}
			}
			if (this.currentChar == 37) {
				this.advance();
				return new ModToken();
			}
			if (this.currentChar == 47) {
				this.advance();
				if (this.currentChar == '=') {
					this.advance();
					return new DivEqualsToken();
				} else {
					return new DivToken();
				}
			}
			if (this.currentChar == '=') {
				this.advance();
				return new EqualsToken();
			}
			System.out.println("INTERPRETER ERROR: LEXXER");
			BeanInterpreter.exception("Unknown char: " + this.currentChar);
		}
		return new EOLToken();
	}
	
	public void run() {
		this.eat();
		
		while (!(this.currentToken instanceof EOLToken)) {
			BeanInterpreter.tokensLine.add(this.currentToken);
			this.eat();
		}
		BeanInterpreter.tokensLine.add(this.currentToken);
	}
}
