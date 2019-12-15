public class MinusEqualsToken extends Token {
	public String value = "+=";
	
	public MinusEqualsToken() {
		super("ASSIGN");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
