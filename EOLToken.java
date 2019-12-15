public class EOLToken extends Token {
	public String value = "";
	
	public EOLToken() {
		super("NEXTLINE");
	}
	
	public String toString() {
		return this.type;
	}
}
