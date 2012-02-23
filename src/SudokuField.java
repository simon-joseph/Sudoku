import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;


/**
 * @author Szymon Laszczynski
 *
 */
public class SudokuField extends JRadioButton implements Serializable, VetoableChangeListener, PropertyChangeListener {
	/* Property names */
	public static final String PROP_STATE = "state";
	public static final String PROP_VALUE = "value";
	public static final String PROP_POSITION = "position";
	/* Properties */
	private FieldState state;		// state of field
	private FieldValue value;		// value of field
	private Point position;			// Position on board
	/* Other fields */
	private short conflicts;				// conflicts this field is in with other fields

	private BufferedImage icon;				// icon for this field
	private BufferedImage selecetedIcon;	// icon for this field when selected
	private Graphics iconG2D;				// graphics for changing icon image
	private Graphics selecetedIconG2D;		// graphics for changing selectedIcon image

	private static final long serialVersionUID = 7685578674436288016L;

	public SudokuField() {
		// Initialize fields
		this.init();
		// Set JRadioButton properties
		setOpaque(false);
		this.setLayout(null);
		this.setBorder(null);
		this.setSize(SudokuUI.ICONSIZE);
		setVisible(true);
		// Add key listener
		//this.addKeyListener(this);
		// Update state and graphics
		this.updateGraphics();
	}

	public short getConflicts() {
		return conflicts;
	}

	public Point getPosition() {
		return position;
	}


	public FieldState getState() {
		return state;
	}

	public FieldValue getValue() {
		return value;
	}

	private void init() {
		this.state = FieldState.EMPTY;
		this.value = FieldValue.NULL;
		conflicts = 0;
		icon = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
		selecetedIcon = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
		iconG2D = (Graphics2D) icon.createGraphics();
		selecetedIconG2D = (Graphics2D) selecetedIcon.createGraphics();
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// If "value" property was changed somewhere
		if ( e.getPropertyName() == "value" ) {
			if ( this.value == null || this.value == FieldValue.NULL || this.state == FieldState.INITIAL ) {
				// We have nothing to worry about - we have no value or it's 'hard-coded' since we have initial state
			} else {
				if ( e.getNewValue() == null || e.getNewValue() == FieldValue.NULL) {
					// They changed to null - nothing to worry
				} else {
					if ( e.getNewValue() == this.value ) {
						System.out.println("New conflict " + (((SudokuField) e.getSource()).getPosition()));
						this.setConflicts( (short) (this.conflicts + 1) );
						this.setState(FieldState.REFUSING);
						this.updateGraphics();
						((SudokuField) e.getSource()).setConflicts((short) (((SudokuField) e.getSource()).getConflicts() + 1));
						((SudokuField) e.getSource()).setState(FieldState.REFUSING);
						((SudokuField) e.getSource()).updateGraphics();
					}
				}
				if ( e.getOldValue() == null && e.getOldValue() == FieldValue.NULL ) {
					// We couldn't have had conflict with them
				} else {
					if ( e.getOldValue() == this.value ) {
						// We have been in conflict with them
						System.out.println("Passed conflict " + (((SudokuField) e.getSource()).getPosition()));
						this.setConflicts((short) (this.conflicts - 1));
						this.updateGraphics();
						//((SudokuField) e.getSource()).setConflicts((short) (0));
						((SudokuField) e.getSource()).updateGraphics();
					}
				}
			}
		} 
	}

	public void setConflicts(short conflicts) {
		this.conflicts = conflicts;
	}

	public void setPosition(Point position) throws PropertyVetoException {
		this.fireVetoableChange("position", this.position, position);
		Point oldPosition = this.position;
		this.position = position;
		this.firePropertyChange("value", oldPosition, position);
		this.setLocation(
				SudokuUI.LEFTMARGIN + this.position.x * SudokuUI.FIELDSGAP,
				SudokuUI.TOPMARGIN + this.position.y * SudokuUI.FIELDSGAP);
	} 


	public void setState(FieldState state) {
		this.state = state;
		this.updateGraphics();
	}

	public void setValue(FieldValue value) throws PropertyVetoException {
		FieldValue oldValue = this.value;
		this.fireVetoableChange("value", oldValue, value);
		this.value = value;
		if( value != oldValue )
			this.setConflicts((short) 0);
		this.firePropertyChange("value", oldValue, value);
		this.updateGraphics();
	}

	public void updateGraphics(){
		// update state
		if( this.state != FieldState.INITIAL )
			if ( this.value == FieldValue.NULL )
				this.state = FieldState.EMPTY;
			else if ( conflicts > 0 )
				this.state = FieldState.REFUSING;
			else
				this.state = FieldState.ACCEPTING;
		// update graphics
		iconG2D.drawImage(SudokuUI.NUMBERS_IMG.getImage(), 0, 0, 27, 27,
				1 + SudokuUI.valueBasedWidth(this.value), SudokuUI.stateBasedHeight(this.state),
				25 + SudokuUI.valueBasedWidth(this.value), 23 + SudokuUI.stateBasedHeight(this.state),
				null);
		selecetedIconG2D.drawImage(SudokuUI.NUMBERS_SELECTED_IMG.getImage(), 0, 0, 27, 27,
				1 + SudokuUI.valueBasedWidth(this.value), SudokuUI.stateBasedHeight(this.state),
				25 + SudokuUI.valueBasedWidth(this.value), 23 + SudokuUI.stateBasedHeight(this.state),
				null);
		this.setIcon(new ImageIcon(icon));
		this.setSelectedIcon(new ImageIcon(selecetedIcon));
	}

	@Override
	public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
		if( e.getPropertyName() == "value" && this.value != null && e.getNewValue() != null && 
				((FieldValue) e.getNewValue()).getNumber() == this.value.getNumber()) {
			if ( this.state == FieldState.INITIAL ) {
				throw new PropertyVetoException("Collision with initial value.", e);
			}
		} else if( e.getPropertyName() == "position") {
			if ( this.position != null && this.position.x == ((Point) e.getNewValue()).x && this.position.y == ((Point) e.getNewValue()).y ) {
				this.setSelected(true);
				this.requestFocus();
				throw new PropertyVetoException("Focus change", e);
			}
		}
	}

}
