public class BooleanToken extends Token {
	public boolean value;
	
	public BooleanToken() {
		super("BOOLEAN");
		this.value = false;
	}
	
	public BooleanToken(boolean value) {
		super("BOOLEAN");
		this.value = value;
	}
	
	public String toString() {
		return "" + this.value;
	}
}
