public class CommaToken extends Token {
	public String value = ",";
	
	public CommaToken() {
		super("SEP");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
}
