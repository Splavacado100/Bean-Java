public class ClosedParenthesesToken extends Token {
	public String value = ")";
	
	public ClosedParenthesesToken() {
		super("SEP");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
}
