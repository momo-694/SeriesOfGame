import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class Minesweeper extends JFrame {
	private final int tSize = 40;
	private final int tRow = 8;
	private final int tCol = 8;
	private final int wHeight = tSize * tRow;
	private final int wWidth = tSize * tCol;
	
	public Minesweeper() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Minesweeper");
		setLocationRelativeTo(null);
		setSize(wHeight, wWidth);
		
		setVisible(true);
	}
}