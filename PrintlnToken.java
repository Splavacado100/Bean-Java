public class PrintlnToken extends Token{
	public String value = "";
	
	public PrintlnToken() {
		super("PRINTLN");
	}
	
	public String toString() {
		return this.type;
	}
}
