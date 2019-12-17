public class GreaterEqualsToken extends Token {
	public String value = ">=";
	
	public GreaterEqualsToken() {
		super("CMP");
	}
	
	public String value() {
		return this.value;
	}
}
