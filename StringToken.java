public class StringToken extends Token {
	public String value;
	
	public StringToken() {
		super("STRING");
		this.value = "";
	}
	
	public StringToken(String value) {
		super("STRING");
		this.value = value;
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
}
