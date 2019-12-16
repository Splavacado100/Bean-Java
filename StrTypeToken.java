public class StrTypeToken extends Token {
	public String value = "STR";
	
	public StrTypeToken() {
		super("TYPE");
	}
	
	public String value() {
		return this.value;
	}
}
