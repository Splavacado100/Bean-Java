public class NotEqualsToken extends Token {
	public String value = "!=";
	
	public NotEqualsToken() {
		super("TST");
	}
	
	public String value() {
		return this.value;
	}
}
