public class CharTypeToken extends Token {
	public String value = "CHAR";
	
	public CharTypeToken() {
		super("TYPE");
	}
	
	public String value() {
		return this.value;
	}
}
