public class PlusEqualsToken extends Token {
	public String value = "+=";
	
	public PlusEqualsToken() {
		super("ASSIGN");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
