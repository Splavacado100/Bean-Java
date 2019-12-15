public class MinusToken extends Token {
	public String value = "-";
	
	public MinusToken() {
		super("OP");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
}
