public class MultiToken extends Token {
	public String value = "*";
	
	public MultiToken() {
		super("OP");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
