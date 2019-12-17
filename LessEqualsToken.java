public class LessEqualsToken extends Token {
	public String value = "<=";
	
	public LessEqualsToken() {
		super("CMP");
	}
	
	public String value() {
		return this.value;
	}
}
