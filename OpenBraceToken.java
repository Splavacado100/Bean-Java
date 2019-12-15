public class OpenBraceToken extends Token {
	public String value = "{";
	
	public OpenBraceToken() {
		super("SEP");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
}
