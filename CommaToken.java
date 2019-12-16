public class CommaToken extends Token {
	public String value = ",";
	
	public CommaToken() {
		super("SEP");
	}
	
	public String value() {
		return this.value;
	}
}
