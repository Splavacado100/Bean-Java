public class PlusToken extends Token {
	public String value = "+";
	
	public PlusToken() {
		super("OP");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
