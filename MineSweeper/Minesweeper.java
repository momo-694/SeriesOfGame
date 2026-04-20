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
	private final int tSize = 36;
	private int tRow;
	private int tCol;
	private int boardHeight;
	private int boardWidth;
	private int mineCount;
	private int flagRemaining;
	private MineTile[][] board;
	private ArrayList<MineTile> mineList;
	private int tileClicked = 0;
	private boolean isGameOver = false;
	private boolean didWon = false;
	private String time = "00:00:00";

	//components
	private JLabel lblTime, lblFlag, lblDifficulty;
	private JPanel pnlText, pnlBoard, pnlGameOver, pnlRetryButton, pnlDifficulty;
	private Timer timer;
	private JFrame tryAgainFrame, difficultyFrame;
	
	public Minesweeper() {
		showDifficulties();
	}

	public void showDifficulties() {
		difficultyFrame = new JFrame("Mine");

		difficultyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		difficultyFrame.setSize(250, 300);
		difficultyFrame.setLocationRelativeTo(null);
		difficultyFrame.setLayout(new FlowLayout());
		difficultyFrame.getContentPane().setBackground(Color.GRAY);

		lblDifficulty = new JLabel("SELECT DIFFICULTY");
		lblDifficulty.setFont(new Font("Times New Roman", Font.BOLD, 20));

		pnlDifficulty = new JPanel(new GridLayout(4, 1, 0, 6));
		pnlDifficulty.setBackground(Color.GRAY);
		pnlDifficulty.add(new JButton("EASY"));
		pnlDifficulty.add(new JButton("MEDUIM"));
		pnlDifficulty.add(new JButton("HARD"));
		pnlDifficulty.add(new JButton("EXTREME"));

		for(Component com : pnlDifficulty.getComponents()) {
			((JButton) com).setBackground(Color.LIGHT_GRAY);
			((JButton) com).setFocusable(false);
			((JButton) com).setPreferredSize(new Dimension(170, 50));
			((JButton) com).setFont(new Font("Times New Roman", Font.PLAIN, 16));
			((JButton) com).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switch(e.getActionCommand().toLowerCase()) {
						case "easy":
							tRow = 8;
							mineCount = 10;
							break;
						case "meduim":
							tRow = 10;
							mineCount = 15;
							break;
						case "hard":
							tRow = 12;
							mineCount = 20;
							break;
						case "extreme":
							tRow = 18;
							mineCount = 50;
							break;
					}
					tCol = tRow;
					flagRemaining = mineCount;
					boardHeight = tRow*tSize;
					boardWidth = tCol*tSize;
					difficultyFrame.dispose();
					initGame();
				}
			});
		}

		difficultyFrame.add(lblDifficulty);
		difficultyFrame.add(pnlDifficulty);
		difficultyFrame.setVisible(true);
	}

	public void initGame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Minesweeper");
		setSize(boardHeight, boardWidth);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		board = new MineTile[tRow][tCol];
					
		pnlText = new JPanel(new GridLayout(1, 2));
		pnlText.setBackground(Color.GRAY);
		lblTime = new JLabel("TIME: " + time);
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
									if(tile.isEnabled()) {
										if(tile.getIcon() == null) {
											tile.setIcon(flag);
											--flagRemaining;
										}else if(tile.getIcon().toString().equals(flag.toString())) {
											tile.setIcon(null);
											++flagRemaining;
										}
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

		if(!tile.isEnabled() || tile.getIcon()!=null) {
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
			didWon = true;
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

		String mess = (didWon ? "You won!" : "You lose!");
		tryAgainFrame = new JFrame("Game Over");
		tryAgainFrame.setSize(300, 200);
		tryAgainFrame.setLocationRelativeTo(null);
		tryAgainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tryAgainFrame.setResizable(false);

		pnlGameOver = new JPanel(new GridLayout(3, 1));
		pnlGameOver.add(new JLabel(mess));
		pnlGameOver.add(new JLabel("Time used: " + time));
		pnlGameOver.add(new JLabel("Try again?"));

		for(Component com : pnlGameOver.getComponents()) {
			((JLabel) com).setHorizontalAlignment(JLabel.CENTER);
			((JLabel) com).setFont(new Font("Times New Roman", Font.PLAIN, 24));
		}

		pnlRetryButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlRetryButton.add(new JButton("YES"));
		pnlRetryButton.add(new JButton("EXIT"));

		for(Component com : pnlRetryButton.getComponents()) {
			((JButton) com).setFocusable(false);
			((JButton) com).setBackground(Color.GRAY);
			((JButton) com).setPreferredSize(new Dimension(100, 50));
			((JButton) com).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switch (e.getActionCommand()) {
						case "YES":
							dispose();
							new Minesweeper();
							break;
					}
					tryAgainFrame.dispose();
					dispose();
				}
			});
		}

		tryAgainFrame.add(pnlGameOver, BorderLayout.NORTH);
		tryAgainFrame.add(pnlRetryButton);
		tryAgainFrame.setVisible(true);
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

				time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
				lblTime.setText("TIME: " + time);
			}
		});
		timer.start();
	}
}