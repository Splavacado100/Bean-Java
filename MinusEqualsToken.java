public class MinusEqualsToken extends Token {
	public String value = "+=";
	
	public MinusEqualsToken() {
		super("ASSIGN");
	}
	
	public String value() {
		return this.value;
	}
}
