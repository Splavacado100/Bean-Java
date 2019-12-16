public class ModToken extends Token {
	public String value = "%";
	
	public ModToken() {
		super("OP");
	}
	
	public String value() {
		return this.value;
	}
}
