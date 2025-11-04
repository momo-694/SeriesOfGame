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
		setSize(boardHeight, boardWidth);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		pnlText = new JPanel(new BorderLayout());
		lblText = new JLabel("MINESWEEPER: " + mineCount);
		lblText.setHorizontalAlignment(JLabel.CENTER);
		lblText.setBackground(Color.DARK_GRAY);
		pnlText.add(lblText);
		
		pnlBoard = new JPanel(new GridLayout(tRow, tCol));
		setBoardTiles();
		setMines();
		//TODO
		
		add(pnlText, BorderLayout.NORTH);
		add(pnlBoard);
		setVisible(true);
	}
	
	public void setBoardTiles() {
		for(int r = 0; r < tRow; r++) {
			for(int c = 0; c < tCol; c++) {
				MineTile tile = new MineTile(r, c);
				board[r][c] = tile;
				
				tile.setFocusable(false);
				tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
				tile.setBackground(Color.LIGHT_GRAY);
				tile.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						MineTile tile = (MineTile) e.getSource();

						if(e.getButton() == MouseEvent.BUTTON1) {
							if(tile.getText() == "") {
								if(mineList.contains(tile)) {
									revealMines();
								}
							}
						}else if(e.getButton() == MouseEvent.BUTTON2) {
							System.out.println("heh");
						}
					}
				});
				pnlBoard.add(tile);
			}
		}
	}

	public void setMines() {
		mineList = new ArrayList<MineTile>();

		for(int i = 0; i < mineCount; i++) {
			MineTile mine = board[new Random().nextInt(tRow)][new Random().nextInt(tCol)];
			if(!mineList.contains(mine)) {
				mineList.add(mine);
			}
		}
	}

	public void revealMines() {
		for(MineTile mine : mineList) {
			mine.setIcon(new ImageIcon("mine.png"));
		}
	}

	public static void main(String[] args) {
		new Minesweeper();
	}
}