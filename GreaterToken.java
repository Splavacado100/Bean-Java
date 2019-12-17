public class GreaterToken extends Token {
	public String value = ">";
	
	public GreaterToken() {
		super("CMP");
	}
	
	public String value() {
		return this.value;
	}
}
