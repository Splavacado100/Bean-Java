public class DoubleTypeToken extends Token {
	public String value = "DOUBLE";
	
	public DoubleTypeToken() {
		super("TYPE");
	}
	
	public String value() {
		return this.value;
	}
}
