public class StrTypeToken extends Token {
	public String value = "STR";
	
	public StrTypeToken() {
		super("TYPE");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
