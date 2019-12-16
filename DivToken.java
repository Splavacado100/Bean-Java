public class DivToken extends Token {
	public String value = "" + (char)(47);
	
	public DivToken() {
		super("OP");
	}
	
	public String value() {
		return this.value;
	}
}
