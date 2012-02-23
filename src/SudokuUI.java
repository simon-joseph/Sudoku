import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;



public class SudokuUI implements KeyListener {

	public static final ImageIcon BACKGROUND_IMG  = new ImageIcon( SudokuUI.class.getClassLoader().getResource("table.jpg") );
	public static final ImageIcon NUMBERS_IMG = new ImageIcon( SudokuUI.class.getClassLoader().getResource("numbers.png") );
	public static final ImageIcon NUMBERS_SELECTED_IMG = new ImageIcon( SudokuUI.class.getClassLoader().getResource("numbers selected.png") );
	public static final String CHANGE_CLIP = "click.wav";
	public static final String ERROR_CLIP = "error.wav";

	// SudokuField configuration
	protected static final short EMPTYHEIGHT = 1;	// numbers sprite image dependent - empty sprite starting height
	protected static final short INITIALHEIGHT = 25;	// numbers sprite image dependent - initial sprite starting height
	protected static final short REFUSINGHEIGHT = 49;	// numbers sprite image dependent - refusing sprite starting height
	protected static final short ACCCEPTINGHEIGHT = 73;	// numbers sprite image dependent - accepting sprite starting height
	public static final short LEFTMARGIN = 14;		// table image dependent - left margin for board drawing
	public static final short TOPMARGIN = 19;			// table image dependent - top margin for board drawing
	public static final short FIELDSGAP = 32;			// table image dependent - gap for numbers on board
	public static final Dimension ICONSIZE = new Dimension(32, 28);	// numbers sprite image dependent - size of number sprite

	public SudokuUI() {}

	protected static final short stateBasedHeight(FieldState state) {
		switch(state) {
		case EMPTY :
			return SudokuUI.EMPTYHEIGHT;
		case INITIAL :
			return SudokuUI.INITIALHEIGHT;
		case ACCEPTING :
			return SudokuUI.ACCCEPTINGHEIGHT;
		case REFUSING :
			return SudokuUI.REFUSINGHEIGHT;
		default :
			return SudokuUI.EMPTYHEIGHT;
		}
	}

	protected static final short valueBasedWidth( FieldValue value) {
		if(value == FieldValue.NULL || value == null)
			return 0;
		else
			return (short) ((value.getNumber() - 1) * SudokuUI.FIELDSGAP);
	}

	public static synchronized void playSound(final String url) {
		//new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
		//public void run() {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(SudokuUI.class.getResourceAsStream(url));
			clip.open(inputStream);
			clip.start(); 
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		//  }
		//}).start();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		FieldValue oldValue = ((SudokuField) e.getSource()).getValue();
		Point position = ((SudokuField) e.getSource()).getPosition();
		try {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT :
				((SudokuField) e.getSource()).setPosition(guardFieldBorder(new Point(position.x - 1, position.y)));
				break;
			case KeyEvent.VK_RIGHT :
				((SudokuField) e.getSource()).setPosition(guardFieldBorder(new Point(position.x + 1, position.y)));
				break;
			case KeyEvent.VK_UP :
				((SudokuField) e.getSource()).setPosition(guardFieldBorder(new Point(position.x, position.y - 1)));
				break;
			case KeyEvent.VK_DOWN :
				((SudokuField) e.getSource()).setPosition(guardFieldBorder(new Point(position.x, position.y + 1)));
				break;
			default :
				if( ((SudokuField) e.getSource()).getState() != FieldState.INITIAL )
					switch(e.getKeyCode()) {
					case KeyEvent.VK_1 :
						((SudokuField) e.getSource()).setValue(FieldValue.ONE);
						break;
					case KeyEvent.VK_2 :
						((SudokuField) e.getSource()).setValue(FieldValue.TWO);
						break;
					case KeyEvent.VK_3 :
						((SudokuField) e.getSource()).setValue(FieldValue.THREE);
						break;
					case KeyEvent.VK_4 :
						((SudokuField) e.getSource()).setValue(FieldValue.FOUR);
						break;
					case KeyEvent.VK_5 :
						((SudokuField) e.getSource()).setValue(FieldValue.FIVE);
						break;
					case KeyEvent.VK_6 :
						((SudokuField) e.getSource()).setValue(FieldValue.SIX);
						break;
					case KeyEvent.VK_7 :
						((SudokuField) e.getSource()).setValue(FieldValue.SEVEN);
						break;
					case KeyEvent.VK_8 :
						((SudokuField) e.getSource()).setValue(FieldValue.EIGHT);
						break;
					case KeyEvent.VK_9 :
						((SudokuField) e.getSource()).setValue(FieldValue.NINE);
						break;
					case KeyEvent.VK_0 :
						((SudokuField) e.getSource()).setValue(FieldValue.NULL);
						break;
					default :
						break;
					}
				break;
			}
		} catch (PropertyVetoException e1) {
			if(e1.getPropertyChangeEvent().getPropertyName() == "value")
				SudokuUI.playSound(SudokuUI.ERROR_CLIP);
		} finally {
			FieldValue newValue = ((SudokuField) e.getSource()).getValue();
			if(oldValue != newValue)
				SudokuUI.playSound(SudokuUI.CHANGE_CLIP);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	private int guardBorder(int x){
		if ( x < 0 )
			x = 0;
		else if ( x > 8 )
			x = 8;
		return x;
	}

	private Point guardFieldBorder(Point position){
		position.setLocation(new Point(guardBorder(position.x), guardBorder(position.y)));
		return position;
	}

}

