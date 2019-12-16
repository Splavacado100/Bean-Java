public class ClosedBraceToken extends Token {
	public String value = "}";
	
	public ClosedBraceToken() {
		super("SEP");
	}
	
	public String value() {
		return this.value;
	}
}
