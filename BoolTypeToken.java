public class BoolTypeToken extends Token {
	public String value = "BOOL";
	
	public BoolTypeToken() {
		super("TYPE");
	}
	
	public String value() {
		return this.value;
	}
}
