public class MinusToken extends Token {
	public String value = "-";
	
	public MinusToken() {
		super("OP");
	}
	
	public String value() {
		return this.value;
	}
}
