public class IntegerToken extends Token {
	public int value;
	
	public IntegerToken() {
		super("INTEGER");
		this.value = 0;
	}
	
	public IntegerToken(int value) {
		super("INTEGER");
		this.value = value;
	}
	
	public String toString() {
		return "" + this.value;
	}
}
