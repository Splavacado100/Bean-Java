import java.util.ArrayList;

public class DatatypeDict {
	public ArrayList<String> varNames = new ArrayList<String>();
	public ArrayList<Token> varValues = new ArrayList<Token>();
	
	public DatatypeDict() {}
	
	private int findName(String s) {
		for (int i = 0; i < varNames.size(); i++) {
			if (s.equals(varNames.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	public void addVar(Token value, String name) {
		if (this.findName(name) == -1) {
			varNames.add(name);
			varValues.add(value);
		} else {
			BeanInterpreter.exception("Variable '" + name + "' has already been defined");
		}
	}
	
	public void setVar(Token value, String name) {
		int index = findName(name);
		if (findName(name) != -1) {
			varValues.set(index, value);
		} else {
			BeanInterpreter.exception("Variable '" + name + "' has not been defined");
		}
	}
	
	public Token getVar(String name) {
		int index = findName(name);
		if (findName(name) != -1) {
			return varValues.get(index);
		} else {
			BeanInterpreter.exception("Variable '" + name + "' has not been defined");
			return new EOFToken();
		}
	}
	
	public static Token toChar(Token token) {
		if (token instanceof IntegerToken) {
			int val = ((IntegerToken)token).value;
			return new CharacterToken((char)val);
		} else if (token instanceof DoubleToken) {
			int val = (int)(((DoubleToken)token).value);
			return new CharacterToken((char)val);
		} else if (token instanceof StringToken) {
			BeanInterpreter.exception("str cannot be converted to char");
			return new EOFToken();
		} else if (token instanceof CharacterToken) {
			return token;
		} else if (token instanceof BooleanToken) {
			BeanInterpreter.exception("bool cannot be converted to char");
			return new EOFToken();
		} else {
			//I don't know what cases would cause it to reach this state
			BeanInterpreter.exception("Unknown error");
			return new EOFToken();
		}
	}
	
	public static Token toInt(Token token) {
		if (token instanceof IntegerToken) {
			return token;
		} else if (token instanceof DoubleToken) {
			double val = ((DoubleToken)token).value;
			return new IntegerToken((int)val);
		} else if (token instanceof StringToken) {
			BeanInterpreter.exception("str cannot be converted to int");
			return new EOFToken();
		} else if (token instanceof CharacterToken) {
			char val = ((CharacterToken)token).value;
			return new IntegerToken((int)val);
		} else if (token instanceof BooleanToken) {
			BeanInterpreter.exception("bool cannot be converted to int");
			return new EOFToken();
		} else {
			//I don't know what cases would cause it to reach this state
			BeanInterpreter.exception("Unknown error");
			return new EOFToken();
		}
	}
	
	public static Token toDouble(Token token) {
		if (token instanceof IntegerToken) {
			int val = ((IntegerToken)token).value;
			return new DoubleToken((double)val);
		} else if (token instanceof DoubleToken) {
			return token;
		} else if (token instanceof StringToken) {
			BeanInterpreter.exception("str cannot be converted to double");
			return new EOFToken();
		} else if (token instanceof CharacterToken) {
			char val = ((CharacterToken)token).value;
			return new DoubleToken((double)val);
		} else if (token instanceof BooleanToken) {
			BeanInterpreter.exception("bool cannot be converted to double");
			return new EOFToken();
		} else {
			//I don't know what cases would cause it to reach this state
			BeanInterpreter.exception("Unknown error");
			return new EOFToken();
		}
	}
}
