public class OrToken extends Token {
	public String value = "||";
	
	public OrToken() {
		super("LOGICAL");
	}
	
	public String value() {
		return this.value;
	}
}
