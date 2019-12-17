public class ModEqualsToken extends Token {
	public String value = "%=";
	
	public ModEqualsToken() {
		super("ASSIGN");
	}
	
	public String value() {
		return this.value;
	}
}
