public class FuncTypeToken extends Token {
	public String value = "FUNC";
	
	public FuncTypeToken() {
		super("TYPE");
	}
	
	public String value() {
		return this.value;
	}
}
