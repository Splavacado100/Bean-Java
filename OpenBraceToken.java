public class OpenBraceToken extends Token {
	public String value = "{";
	
	public OpenBraceToken() {
		super("SEP");
	}
	
	public String value() {
		return this.value;
	}
}
