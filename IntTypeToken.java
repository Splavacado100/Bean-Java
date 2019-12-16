public class IntTypeToken extends Token {
	public String value = "INT";
	
	public IntTypeToken() {
		super("TYPE");
	}
	
	public String value() {
		return this.value;
	}
}
