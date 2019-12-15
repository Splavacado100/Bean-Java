public class EmptyToken extends Token {
	public String value = ";";
	
	public EmptyToken() {
		super("END");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
}
