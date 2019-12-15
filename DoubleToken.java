public class DoubleToken extends Token {
	public double value;
	
	public DoubleToken() {
		super("DOUBLE");
		this.value = 0.0;
	}
	
	public DoubleToken(double value) {
		super("DOUBLE");
		this.value = value;
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
}
