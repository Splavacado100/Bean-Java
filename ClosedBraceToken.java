public class ClosedBraceToken extends Token {
	public String value = "}";
	
	public ClosedBraceToken() {
		super("SEP");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
}
