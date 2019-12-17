public class AndToken extends Token {
	public String value = "&&";
	
	public AndToken() {
		super("LOGICAL");
	}
	
	public String value() {
		return this.value;
	}
}
