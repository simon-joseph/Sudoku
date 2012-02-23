import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyVetoException;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * @author Szymon Laszczynski
 *
 */
public class Sudoku extends JFrame {
	private static final long serialVersionUID = 4729877586818948835L;
	
	private SudokuField[][] board;	// board holding all fields
	private ButtonGroup selectableFields;	// button group to make fields selectable and ensure only one is selectet at once
	private SudokuUI ui;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Sudoku( "Sudoku Game", new SudokuUI() );
	}
	
	public Sudoku(String name, SudokuUI ui) {
		super(name);
		this.setUi(ui);
		initComponents();
	}
	
	private void initComponents() {
		// init new default buttongroup
		selectableFields = new ButtonGroup();
		// init board
		board = new SudokuField[9][9];
		for(byte column = 0; column < 9; column++){
			for(byte row = 0; row < 9; row++){
				// Assign new SudokuField
				board[column][row] = new SudokuField();
				try {
					board[column][row].setPosition(new Point(column, row));
				} catch (PropertyVetoException e) {
					System.out.println("This should not happen. Make sure you're setting up this value for the first time.");
					e.printStackTrace();
				}
				board[column][row].addKeyListener(ui);
				// Add field as element of this JFrame
				this.add(board[column][row]);
				// Add field to buttonGruop
				selectableFields.add(board[column][row]);
				// Add this as 
			//	board[column][row].addActionListener(this);
			}
		}
		//selectableFields.setSelected((ButtonModel) board[0][0], true);
		// Add Property and Vetoable Change Listeners
		for(byte column = 0; column < 9; column++)
			for(byte row = 0; row < 9; row++){
				for(int z = 0; z < 9; z++){
					if(row != z) {
						board[column][row].addVetoableChangeListener(board[column][z]);
						board[column][row].addPropertyChangeListener(board[column][z]);
					}
					if(column != z) {
						board[column][row].addVetoableChangeListener(board[z][row]);
						board[column][row].addPropertyChangeListener(board[z][row]);
					}
				}
				Point topLeft = SimpleSudokuGenerator.groupTopLeft(SimpleSudokuGenerator.computeSquareID(column, row));
				for(int x = topLeft.x; x < topLeft.x + 3; x++)
					for( int y = topLeft.y; y < topLeft.y + 3; y++ )
						if( x != column && y != row ){
							board[column][row].addVetoableChangeListener(board[x][y]);
							board[column][row].addPropertyChangeListener(board[x][y]);
						}
			}
		// Generate random sudoku puzzle
		int[][] puzzle = SimpleSudokuGenerator.generate(System.currentTimeMillis());
		// Initialize fields with values form generated puzzle
		for(byte column = 0; column < 9; column++){
			for(byte row = 0; row < 9; row++){
				if(puzzle[column][row] != -1){
					try {
						board[column][row].setValue(FieldValue.fromInt(puzzle[column][row]));
						board[column][row].setState(FieldState.INITIAL);
					} catch (PropertyVetoException e) {
						System.err.println("This should not happen with the initial values - check puzzle generator. At : " + column +" , "+row);
						e.printStackTrace();
					}
				} else {
					board[column][row].setState(FieldState.EMPTY);
				}
			}
		}
		//Make sure application is closed with this jFrame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Configure JFrame
		this.setLocation(0, 0);
		this.setSize(new Dimension(SudokuUI.BACKGROUND_IMG.getIconWidth(), SudokuUI.BACKGROUND_IMG.getIconHeight()));
        this.setResizable(false);
    //    this.addKeyListener(this);
        this.setFocusable(true);
        //Create background 
        JLabel background = new JLabel();
        background.setIcon(SudokuUI.BACKGROUND_IMG);
        this.add(background, null);
        // Finalize JFrame configuration
        this.pack();
        this.setVisible(true);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = Sudoku.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

	public SudokuUI getUi() {
		return ui;
	}

	public void setUi(SudokuUI ui) {
		this.ui = ui;
	}

}
