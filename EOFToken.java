public class EOFToken extends Token {
	public String value = "";
	
	public EOFToken() {
		super("NULL");
	}
	
	public String toString() {
		return this.type;
	}
}
