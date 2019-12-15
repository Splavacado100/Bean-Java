public class FalseToken extends Token {
	public String value = "FALSE";
	
	public FalseToken() {
		super("BOOLEAN");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
