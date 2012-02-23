/**
 * @author Szymon Laszczynski
 *
 */
public enum FieldValue {
	NULL	((byte) 0),
	ONE		((byte) 1),
	TWO		((byte) 2),
	THREE	((byte) 3),
	FOUR	((byte) 4),
	FIVE	((byte) 5),
	SIX		((byte) 6),
	SEVEN	((byte) 7),
	EIGHT	((byte) 8),
	NINE	((byte) 9);
	
	private final byte number;
	
	private FieldValue(byte t) {
		this.number = t;
	}
	
	public byte getNumber() {
		return number;
	}

	public static FieldValue fromInt(int num) {
		for(int i = 0; i < 10; i++)
			if( FieldValue.values()[i].getNumber() == num )
				return FieldValue.values()[i];
		return null;
	}
}
