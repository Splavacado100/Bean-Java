public class MultiEqualsToken extends Token {
	public String value = "*=";
	
	public MultiEqualsToken() {
		super("ASSIGN");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
