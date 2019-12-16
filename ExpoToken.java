public class ExpoToken extends Token {
	public String value = "**";
	
	public ExpoToken() {
		super("OP");
	}
	
	public String value() {
		return this.value;
	}
}
