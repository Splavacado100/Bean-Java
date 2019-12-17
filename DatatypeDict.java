import java.util.ArrayList;

public class DatatypeDict {
	public ArrayList<String> varNames = new ArrayList<String>();
	public ArrayList<Token> varValues = new ArrayList<Token>();
	public ArrayList<Integer> varScopes = new ArrayList<Integer>();
	
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
			varScopes.add(BeanInterpreter.curScope);
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
	
	public static void gc() {
		for (int i = 0; i < BeanInterpreter.variables.varScopes.size(); i++) {
			if (BeanInterpreter.variables.varScopes.get(i).intValue() > BeanInterpreter.curScope) {
				BeanInterpreter.variables.varNames.remove(i);
				BeanInterpreter.variables.varValues.remove(i);
				BeanInterpreter.variables.varScopes.remove(i);
			}
		}
	}
	
	//Operations: Automatic widening
	
	public static Token aInt(Token token) {
		if (token instanceof IntegerToken) {
			return token;
		}
		else if (token instanceof CharacterToken) {
			char newVal = ((CharacterToken)token).value;
			return new IntegerToken((int)newVal);
		}
		else {
			//Double type: unreachable, parser would call aDouble()
			//String type: invalid, return EOFToken
			//Boolean type: invalid, return EOFToken
			return new EOFToken();
		}
	}
	
	public static Token aDouble(Token token) {
		if (token instanceof DoubleToken) {
			return token;
		}
		else if (token instanceof IntegerToken) {
			int newVal = ((IntegerToken)token).value;
			return new DoubleToken((double)newVal);
		}
		else if (token instanceof CharacterToken) {
			char newVal = ((CharacterToken)token).value;
			return new DoubleToken((double)newVal);
		}
		else {
			//String type: invalid, return EOFToken
			//Boolean type: invalid, return EOFToken
			return new EOFToken();
		}
	}
	
	public static Token aChar(Token token) {
		if (token instanceof CharacterToken) {
			char newVal = ((CharacterToken)token).value;
			return new IntegerToken((int)newVal);
		}
		else {
			//Integer type: unreachable, parser would call aInt()
			//Double type: unreachable, parser would call aDouble()
			//String type: invalid, return EOFToken
			//Boolean type: invalid, return EOFToken
			return new EOFToken();
		}
	}
	
	
	//Typecast: Manual narrowing
	
	public static Token mInt(Token token) {
		if (token instanceof IntegerToken) {
			return token;
		}
		else if (token instanceof DoubleToken) {
			double newVal = ((DoubleToken)token).value;
			return new IntegerToken((int)newVal);
		}
		else if (token instanceof CharacterToken) {
			char newVal = ((CharacterToken)token).value;
			return new IntegerToken((int)newVal);
		}
		else {
			//String-Invalid
			//Boolean-Invalid
			BeanInterpreter.exception("Invalid conversion type: " + token.type + " to INTEGER");
			return new EOFToken();
		}
	}
	
	public static Token mDouble(Token token) {
		if (token instanceof IntegerToken) {
			int newVal = ((IntegerToken)token).value;
			return new DoubleToken((double)newVal);
		}
		else if (token instanceof DoubleToken) {
			return token;
		}
		else if (token instanceof CharacterToken) {
			char newVal = ((CharacterToken)token).value;
			return new DoubleToken((double)newVal);
		}
		else {
			//String-Invalid
			//Boolean-Invalid
			BeanInterpreter.exception("Invalid conversion type: " + token.type + " to DOUBLE");
			return new EOFToken();
		}
	}
	
	public static Token mStr(Token token) {
		if (token instanceof StringToken) {
			return token;
		}
		else {
			//Integer-Invalid
			//Double-Invalid
			//Character-Invalid
			//Boolean-Invalid
			BeanInterpreter.exception("Invalid conversion type: " + token.type + " to STRING");
			return new EOFToken();
		}
	}
	
	public static Token mChar(Token token) {
		if (token instanceof IntegerToken) {
			int newVal = ((IntegerToken)token).value;
			return new CharacterToken((char)newVal);
		}
		else if (token instanceof DoubleToken) {
			double newVal = ((DoubleToken)token).value;
			return new CharacterToken((char)newVal);
		}
		else if (token instanceof CharacterToken) {
			return token;
		}
		else {
			//String-Invalid
			//Boolean-Invalid
			BeanInterpreter.exception("Invalid conversion type: " + token.type + " to INTEGER");
			return new EOFToken();
		}
	}
	
	public static Token mBool(Token token) {
		if (token instanceof BooleanToken) {
			return token;
		}
		else {
			//Integer-Invalid
			//Double-Invalid
			//String-Invalid
			//Character-Invalid
			BeanInterpreter.exception("Invalid conversion type: " + token.type + " to STRING");
			return new EOFToken();
		}
	}
	
	//Assignment: Check if narrowing
	
	public static Token cInt(Token token) {
		if (token instanceof IntegerToken) {
			return token;
		}
		else if (token instanceof DoubleToken) {
			//Lossy Conversion
			BeanInterpreter.exception("Lossy conversion: DOUBLE to INTEGER");
			return new EOFToken();
		}
		else if (token instanceof CharacterToken) {
			char newVal = ((CharacterToken)token).value;
			return new IntegerToken((int)newVal);
		}
		else {
			//String-Invalid
			//Boolean-Invalid
			BeanInterpreter.exception("Invalid conversion type: " + token.type + " to INTEGER");
			return new EOFToken();
		}
	}
	
	public static Token cDouble(Token token) {
		if (token instanceof IntegerToken) {
			int newVal = ((IntegerToken)token).value;
			return new DoubleToken((double)newVal);
		}
		else if (token instanceof DoubleToken) {
			return token;
		}
		else if (token instanceof CharacterToken) {
			char newVal = ((CharacterToken)token).value;
			return new DoubleToken((double)newVal);
		}
		else {
			//String-Invalid
			//Boolean-Invalid
			BeanInterpreter.exception("Invalid conversion type: " + token.type + " to DOUBLE");
			return new EOFToken();
		}
	}
	
	public static Token cStr(Token token) {
		if (token instanceof StringToken) {
			return token;
		}
		else {
			//Integer-Invalid
			//Double-Invalid
			//Character-Invalid
			//Boolean-Invalid
			BeanInterpreter.exception("Invalid conversion type: " + token.type + " to STRING");
			return new EOFToken();
		}
	}
	
	public static Token cChar(Token token) {
		if (token instanceof IntegerToken) {
			int newVal = ((IntegerToken)token).value;
			return new CharacterToken((char)newVal);
		}
		else if (token instanceof DoubleToken) {
			//Lossy Conversion
			BeanInterpreter.exception("Lossy conversion: DOUBLE to CHAR");
			return new EOFToken();
		}
		else if (token instanceof CharacterToken) {
			return token;
		}
		else {
			//String-Invalid
			//Boolean-Invalid
			BeanInterpreter.exception("Invalid conversion type: " + token.type + " to INTEGER");
			return new EOFToken();
		}
	}
	
	public static Token cBool(Token token) {
		if (token instanceof BooleanToken) {
			return token;
		}
		else {
			//Integer-Invalid
			//Double-Invalid
			//String-Invalid
			//Character-Invalid
			BeanInterpreter.exception("Invalid conversion type: " + token.type + " to STRING");
			return new EOFToken();
		}
	}
}
