import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class Minesweeper extends JFrame {
	class MineTile extends JButton {
		int r, c;
		
		public MineTile(int r, int c) {
			this.r = r;
			this.c = c;
		}
		
	}
	
	//data
	private final int tSize = 40;
	private final int tRow = 8;
	private final int tCol = 8;
	private final int wHeight = tSize*tRow;
	private final int wWidth = tSize*tCol;
	private final int mineCount = 10;
	private MineTile[][] mTiles = new MineTile[tRow][tCol];
	private ArrayList<MineTile> mineList;
	
	//components
	private JLabel lblText;
	private JPanel pnlText, pnlTiles;
	
	public Minesweeper() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Minesweeper");
		setLocationRelativeTo(null);
		setSize(wHeight, wWidth);
		setLayout(new BorderLayout());
		
		lblText = new JLabel("Minesweeper" + mineCount);
		//TODO
		
		setVisible(true);
	}
}