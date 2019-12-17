public class WhileToken extends Token {
	public String value = "WHILE";
	
	public WhileToken() {
		super("LOOP");
	}
	
	public String value() {
		return this.value;
	}
}
