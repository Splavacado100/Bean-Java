public class DivEqualsToken extends Token {
	public String value = "/=";
	
	public DivEqualsToken() {
		super("ASSIGN");
	}
	
	public String value() {
		return this.value;
	}
}
