/**
 * @author Szymon Laszczynski
 * 
 * Used in SudokuField class to indicate field state.
 * EMPTY		- Field is empty (value never inserted or erased) 
 * INITIAL		- Field was set an initial value and should not be changed further
 * REFUSING		- Field has value conflicts with other fields
 * ACCEPTING	- Field has no value conflicts with other fields
 */
enum FieldState {
	EMPTY,
	INITIAL,
	REFUSING,
	ACCEPTING;
}