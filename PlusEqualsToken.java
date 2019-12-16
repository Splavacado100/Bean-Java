public class PlusEqualsToken extends Token {
	public String value = "+=";
	
	public PlusEqualsToken() {
		super("ASSIGN");
	}
	
	public String value() {
		return this.value;
	}
}
