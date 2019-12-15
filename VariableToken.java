public class VariableToken extends Token {
	public String value;
	
	public VariableToken(String value) {
		super("VARIABLE");
		this.value = value;
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
}
