public class VoidToken extends Token {
	public String value = "";
	
	public VoidToken() {
		super("VOID");
	}
	
	public String toString() {
		return this.value;
	}
}
