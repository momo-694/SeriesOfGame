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
	private int flagRemaining = mineCount;
	private MineTile[][] board = new MineTile[tRow][tCol];
	private ArrayList<MineTile> mineList;
	private int tileClicked = 0;
	private boolean isGameOver = false;

	//components
	private JLabel lblTime, lblFlag;
	private JPanel pnlText, pnlBoard;
	private Timer timer;
	
	public Minesweeper() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Minesweeper");
		setSize(boardHeight, boardWidth);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		pnlText = new JPanel(new GridLayout(1, 2));
		pnlText.setBackground(Color.GRAY);
		lblTime = new JLabel("TIME: 00:00:00");
		lblTime.setHorizontalAlignment(JLabel.CENTER);

		lblFlag = new JLabel("FLAG LEFT: " + flagRemaining);
		lblFlag.setHorizontalAlignment(JLabel.CENTER);

		pnlText.add(lblTime);
		pnlText.add(lblFlag);
		
		pnlBoard = new JPanel(new GridLayout(tRow, tCol));
		setBoardTiles();
		setMine();
		
		add(pnlText, BorderLayout.NORTH);
		add(pnlBoard);

		setVisible(true);
		startTimer();
	}
	
	public void setBoardTiles() {
		for(int r = 0; r < tRow; r++) {
			for(int c = 0; c < tCol; c++) {
				MineTile tile = new MineTile(r, c);
				board[r][c] = tile;
				
				tile.setFocusable(false);
				tile.setFont(new Font("Times New Roman", Font.BOLD, 24));
				tile.setMargin(new Insets(0,0, 0, 0));
				tile.setBackground(Color.LIGHT_GRAY);
				tile.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						MineTile tile = (MineTile) e.getSource();

						if(!isGameOver) {
							if(e.getButton() == MouseEvent.BUTTON1) {
								if(tile.getIcon() == null) {
									if(mineList.contains(tile)) {
										revealMine();
									}else {
										checkMine(tile.r, tile.c);
									}
								}
							}else if(e.getButton() == MouseEvent.BUTTON3) {
								Icon flag = (Icon) new ImageIcon("flag.png");

								if(flagRemaining >= 0) {
									if(tile.getIcon() == null && tile.isEnabled()) {
										tile.setIcon(flag);
										--flagRemaining;
									}else if(tile.getIcon().toString().equals(flag.toString())) {
										tile.setIcon(null);
										++flagRemaining;
									}
									lblFlag.setText("FLAG LEFT: " + flagRemaining);
								}
							}
						}
					}
				});
				pnlBoard.add(tile);
			}
		}
	}

	public void setMine() {
		mineList = new ArrayList<MineTile>();

		for(int i = 0; i < mineCount; i++) {
			MineTile mine = board[new Random().nextInt(tRow)][new Random().nextInt(tCol)];
			if(!mineList.contains(mine)) {
				mineList.add(mine);
			}else {
				i--;
			}
		}
	}

	public void revealMine() {
		for(MineTile mine : mineList) {
			mine.setIcon(new ImageIcon("mine.png"));
		}

		isGameOver = true;
		timer.stop();
		gameOver();
	}

	public void checkMine(int r, int c) {
		if(r < 0 || r >= tRow || c < 0 || c >= tCol) {
			return;
		}

		MineTile tile = board[r][c];

		if(!tile.isEnabled()) {
			return;
		}

		tile.setBackground(Color.DARK_GRAY);
		tile.setEnabled(false);
		tileClicked++;

		int minesFound = 0;

		minesFound += countMine(r-1, c-1);
		minesFound += countMine(r-1, c);
		minesFound += countMine(r-1, c+1);

		minesFound += countMine(r, c-1);
		minesFound += countMine(r, c+1);

		minesFound += countMine(r+1, c-1);
		minesFound += countMine(r+1, c);
		minesFound += countMine(r+1, c+1);

		if(!(minesFound == 0)) {
			tile.setText("" + minesFound);
		}else {
			tile.setText("");

			checkMine(r-1, c-1);
			checkMine(r-1, c);
			checkMine(r-1, c+1);

			checkMine(r, c+1);
			checkMine(r, c-1);

			checkMine(r+1, c-1);
			checkMine(r+1, c);
			checkMine(r+1, c+1);
		}

		if(tileClicked == (tRow*tCol-mineList.size())) {
			isGameOver = true;
			timer.stop();
			gameOver();
		}
	}
	public int countMine(int r, int c) {
		if(r < 0 || r >= tRow || c < 0 || c >= tCol) {
			return 0;
		}else if(mineList.contains(board[r][c])) {
			return 1;
		}
		return 0;
	}

	public void gameOver() {
		pnlText.setLayout(new BorderLayout());
		pnlText.removeAll();
		lblTime.setText("GAME OVER!");
		pnlText.add(lblTime);
	}

	public void startTimer() {
		timer = new Timer(1000, new ActionListener() {
			int seconds = 0;
			int minutes = 0;
			int hours = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				seconds++;

				if(seconds == 60) {
					seconds = 0;
					minutes++;
					if(minutes == 60) {
						minutes = 0;
						hours++;
					}
				}

				String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
				lblTime.setText("TIME: " + time);
			}
		});
		timer.start();
	}

	public static void main(String[] args) {
		new Minesweeper();
	}
}