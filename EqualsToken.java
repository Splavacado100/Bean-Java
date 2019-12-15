public class EqualsToken extends Token {
	public String value = "=";
	
	public EqualsToken() {
		super("ASSIGN");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
