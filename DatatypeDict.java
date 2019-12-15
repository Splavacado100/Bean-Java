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
}
