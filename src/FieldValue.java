/**
 * @author Szymon Laszczynski
 *
 * Used as value in SudokuField class.
 * NULL value corresponds to empty field other values correspond to their meaning
 */
enum FieldValue {
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
	
	/**
	 * Used for UI elements positioning
	 */
	private final byte number;
	
	/**
	 * Instantiates a new field value.
	 *
	 * @param number the number for UI elements positioning
	 */
	private FieldValue(byte number) {
		this.number = number;
	}
	
	/**
	 * Gets the number.
	 *
	 * @return the number
	 */
	public byte getNumber() {
		return number;
	}

	
	/**
	 * Returns field value corresponding to input number
	 *
	 * @param num the number corresponding to field value
	 * @return corresponding field value, otherwise null
	 */
	public static FieldValue fromInt(int num) {
		for(int i = 0; i < 10; i++)
			if( FieldValue.values()[i].getNumber() == num )
				return FieldValue.values()[i];
		return null;
	}
}
