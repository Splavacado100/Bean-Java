public class ExpoToken extends Token {
	public String value = "**";
	
	public ExpoToken() {
		super("OP");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
