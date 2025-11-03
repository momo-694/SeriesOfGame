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
	private final int boardHeight = tSize*tRow;
	private final int boardWidth = tSize*tCol;
	private final int mineCount = 10;
	private MineTile[][] board = new MineTile[tRow][tCol];
	private ArrayList<MineTile> mineList;
	
	//components
	private JLabel lblText;
	private JPanel pnlText, pnlBoard;
	
	public Minesweeper() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Minesweeper");
		setLocationRelativeTo(null);
		setSize(boardHeight, boardWidth);
		setLayout(new BorderLayout());
		
		pnl = new JPanel(new BorderLayout());
		lblText = new JLabel("Minesweeper" + mineCount);
		pnlText.add(lblText);
		
		pnlBoard = new JPanel(new GridLayout(tRow, tCol));
		setBoardTiles();
		//TODO
		
		add(pnlText, BorderLayout.NORTH);
		add(pnlBoard);
		setVisible(true);
	}
	
	public void setBoardTiles() {
		for(int r = 0; r < tRow; r++) {
			for(int c = 0; c < tCol; c++) {
				MineTile tile = new MineTile(r, c);
				tile.setFocusable(false);
				tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
				tile.addMouseListener(new Mouse adapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						//TODO
					}
				});
			}
			pnlBoard.add(tile);
		}
	}
}