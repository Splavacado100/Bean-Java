public class NotToken extends Token {
	public String value = "!";
	
	public NotToken() {
		super("LOGICAL");
	}
	
	public String value() {
		return this.value;
	}
}
