public class MultiToken extends Token {
	public String value = "*";
	
	public MultiToken() {
		super("OP");
	}
	
	public String value() {
		return this.value;
	}
}
