public class ClosedParenthesesToken extends Token {
	public String value = ")";
	
	public ClosedParenthesesToken() {
		super("SEP");
	}
	
	public String value() {
		return this.value;
	}
}
