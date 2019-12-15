public class PrintToken extends Token{
	public String value = "";
	
	public PrintToken() {
		super("PRINT");
	}
	
	public String toString() {
		return this.type;
	}
}
