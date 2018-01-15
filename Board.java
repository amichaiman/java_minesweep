import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Board extends Frame implements ActionListener{
	private Spot[][] board;
	private Button [][] buttons;
	private int boardSize;
	private int numberOfMines;
	private int spotsRevealed;
	private static int MIN_NUMBER_OF_MINES = 1;
	private static int DEFAULT_BOARD_SIZE = 10;
	private static final int NUMBER_OF_NEIGHBORS = 8;
	
	public Board(int boardSize) {
		this.boardSize = boardSize;
		setBoard();
	
	}
	
	public Board() {
		boardSize = DEFAULT_BOARD_SIZE;
		setBoard();
	}
	
	private void setBoard(){
		spotsRevealed = 0;
		setLayout(new GridLayout(boardSize,boardSize,2,2));

		board = new Spot[boardSize][boardSize];
		buttons = new Button[boardSize][boardSize];

		for (int i=0; i<boardSize; i++) {
			for (int j=0; j<boardSize; j++) {
				board[i][j] = new Spot();
				buttons[i][j] = new Button();
				buttons[i][j].setActionCommand(Integer.toString(i) + " " + Integer.toString(j));
				buttons[i][j].addActionListener(this);
				add(buttons[i][j]);
			}
		}

		setTitle("Minesweep");
		setSize(boardSize*50, boardSize*50);
		setVisible(true);
	}
	
	public void setNumberOfMines(int numberOfMines) {
		if (numberOfMines < MIN_NUMBER_OF_MINES) {
			this.numberOfMines = MIN_NUMBER_OF_MINES;
		} else if (numberOfMines > boardSize*boardSize) {
			this.numberOfMines = boardSize*boardSize;
		} else {
			this.numberOfMines = numberOfMines;
		}
		plantMines();
		setNumberOfNeighborBombs();
	}
	
	private void plantMines() {
		Random r = new Random();
		int i,j;
		
		for (int k=0; k<numberOfMines; k++) {
			do {
				i = Math.abs(r.nextInt())%boardSize;
				j = Math.abs(r.nextInt())%boardSize;
			} while (board[i][j].getBombStatus() == true);
			board[i][j].setBombStatus(true);
		}
	}

	private void setNumberOfNeighborBombs() {
		int []offsetI = {-1,-1,-1,0,0,1,1,1};
		int []offsetJ = {-1,0,1,-1,1,-1,0,1};
		
		for (int i=0; i<boardSize; i++) {
			for (int j=0; j<boardSize; j++) {
				if (board[i][j].getBombStatus() == false) {
					for (int k=0; k < NUMBER_OF_NEIGHBORS; k++) {
						if (isInBoard(i+offsetI[k],j+offsetJ[k])) {
							if (board[i+offsetI[k]][j+offsetJ[k]].getBombStatus() == true) {
								board[i][j].setNumberOfNeighborBombs(board[i][j].getNumberOfNeighborBombs()+1);
							}
						}
					}
				}
			}
		}
		
		
	}
	
	private boolean isInBoard(int i, int j) {
		return i < 0 || j < 0 || i > boardSize-1 || j > boardSize-1 ? false : true;
	}
	
	
	
	private void revealRec(int i, int j) {
		spotsRevealed++;
		buttons[i][j].disable();
		setBackground(i,j);
		buttons[i][j].setLabel(Integer.toString(board[i][j].getNumberOfNeighborBombs()));
		
		if (spotsRevealed == boardSize*boardSize-numberOfMines) {
			colorGreen();
			return;
		}
		
		if (board[i][j].getNumberOfNeighborBombs() != 0) {
			return;
		}

		int []offsetI = {-1,-1,-1,0,0,1,1,1};
		int []offsetJ = {-1,0,1,-1,1,-1,0,1};
		
		for (int k=0; k<NUMBER_OF_NEIGHBORS; k++) {
			if (isInBoard(i+offsetI[k],j+offsetJ[k])) {
				if (buttons[i+offsetI[k]][j+offsetJ[k]].isEnabled() == true) {
					revealRec(i+offsetI[k],j+offsetJ[k]);
				}
			}
		}
	}
	private void setBackground(int i, int j) {
		buttons[i][j].setBackground(new Color(255-board[i][j].getNumberOfNeighborBombs()*30, 255-board[i][j].getNumberOfNeighborBombs()*10, board[i][j].getNumberOfNeighborBombs()*30));
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		System.out.println(event.getWhen());
		
		int i = Integer.parseInt(event.getActionCommand().split(" ")[0]);
		int j = Integer.parseInt(event.getActionCommand().split(" ")[1]);
		
		
		while (board[i][j].getBombStatus() == true && spotsRevealed == 0) {
				replantMines();
		}
		if (board[i][j].getBombStatus() == true) {
			colorRed();
			return;
		}
		revealRec(i,j);
	}
	
	private void colorRed() {
		for (int i=0; i<boardSize; i++) {
			for (int j=0; j<boardSize; j++) {
				buttons[i][j].setBackground(Color.RED);
				buttons[i][j].disable();
			}
		}
		
	}
	
	private void colorGreen() {
		for (int i=0; i<boardSize; i++) {
			for (int j=0; j<boardSize; j++) {
				buttons[i][j].setBackground(Color.GREEN);
				buttons[i][j].disable();
			}
		}
	}
	
	private void replantMines() {
		for (int i=0; i<boardSize; i++) {
			for (int j=0; j<boardSize; j++) {
				board[i][j].setBombStatus(false);
			}
		}
		plantMines();
	}
}
