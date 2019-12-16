public class OpenParenthesesToken extends Token {
	public String value = "(";
	
	public OpenParenthesesToken() {
		super("SEP");
	}
	
	public String value() {
		return this.value;
	}
}
