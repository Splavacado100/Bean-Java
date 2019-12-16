public class VariableToken extends Token {
	public String value;
	
	public VariableToken(String value) {
		super("VARIABLE");
		this.value = value;
	}
}
