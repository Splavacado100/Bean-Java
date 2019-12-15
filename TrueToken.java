public class TrueToken extends Token {
	public String value = "TRUE";
	
	public TrueToken() {
		super("BOOLEAN");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
