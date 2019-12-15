public class DoubleTypeToken extends Token {
	public String value = "DOUBLE";
	
	public DoubleTypeToken() {
		super("TYPE");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
