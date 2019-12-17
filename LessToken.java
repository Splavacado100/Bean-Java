public class LessToken extends Token {
	public String value = "<";
	
	public LessToken() {
		super("CMP");
	}
	
	public String value() {
		return this.value;
	}
}
