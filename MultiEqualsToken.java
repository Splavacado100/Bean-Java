public class MultiEqualsToken extends Token {
	public String value = "*=";
	
	public MultiEqualsToken() {
		super("ASSIGN");
	}
	
	public String value() {
		return this.value;
	}
}
