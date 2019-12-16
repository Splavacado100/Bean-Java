public class PlusToken extends Token {
	public String value = "+";
	
	public PlusToken() {
		super("OP");
	}
	
	public String value() {
		return this.value;
	}
}
