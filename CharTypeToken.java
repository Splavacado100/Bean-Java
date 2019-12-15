public class CharTypeToken extends Token {
	public String value = "CHAR";
	
	public CharTypeToken() {
		super("TYPE");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
