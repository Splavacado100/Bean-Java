public class OpenParenthesesToken extends Token {
	public String value = "(";
	
	public OpenParenthesesToken() {
		super("SEP");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
}
