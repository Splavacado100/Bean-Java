public class IntTypeToken extends Token {
	public String value = "INT";
	
	public IntTypeToken() {
		super("TYPE");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
