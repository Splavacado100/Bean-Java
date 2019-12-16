public class EqualsToken extends Token {
	public String value = "=";
	
	public EqualsToken() {
		super("ASSIGN");
	}
	
	public String value() {
		return this.value;
	}
}
