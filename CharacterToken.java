public class CharacterToken extends Token {
	public char value;
	
	public CharacterToken() {
		super("CHARACTER");
		this.value = (char)0;
	}
	
	public CharacterToken(char value) {
		super("CHARACTER");
		this.value = value;
	}
	
	public String toString() {
		return "" + this.value;
	}
}
