public class ModToken extends Token {
	public String value = "%";
	
	public ModToken() {
		super("OP");
	}
	
	public String toString() {
		return this.type + ": " + this.value;
	}
	
	public String value() {
		return this.value;
	}
}
