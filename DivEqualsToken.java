public class DivEqualsToken extends Token {
	public String value = "/=";
	
	public DivEqualsToken() {
		super("ASSIGN");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
