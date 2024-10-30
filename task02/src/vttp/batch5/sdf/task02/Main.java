package vttp.batch5.sdf.task02;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			System.err.println("No TTT configuration file provided");
			System.err.println("Usage: java -cp classes vttp.batch5.sdf.task02.Main <TTT figure.txt>");
			return;
		}

		String filePath = args[0];
		File file = new File(filePath);

		if (!file.exists()) {
			System.err.println("TTT configuration file provided does not exist");
			return;
		}

		char[][] tttboard = readBoard(file);

		List<Move> moves = boardUtility(tttboard);

		// sort the moves by coordinates (col --> row)
		List<Move> sortedMoves = moves.stream()
			.sorted(Comparator.comparing(Move::getRow).thenComparing(Move::getCol))
			.collect(Collectors.toList());


		// print out desired output
		// print board
		System.out.println();
		System.out.println("Board:");
		for (int i = 0; i < tttboard.length; i++) {
			System.out.println(tttboard[i]);
		}

		// print ------
		System.out.println();
		System.out.println("------------------------------");

		// print coords + utility
		for (Move m : sortedMoves) {
			System.out.printf("y=%d, x=%d, utility=%d\n", m.getRow(), m.getCol(), m.getUtility());

		}

	}

	public static char[][] readBoard(File file) {

		char[][] board = new char[3][3];

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			System.out.println("Processing: " + file.getPath());
			String line;
			int lineCount = 0;

			while ((line = br.readLine()) != null) {

				line = line.trim();

				try {

					for (int i = 0; i < line.length(); i++) {

						char currChar = line.charAt(i);
						board[lineCount][i] = currChar;
					}

				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.println("TTT configuration file provided has > 3 rows or > 3 columns.");
					e.printStackTrace();
					System.exit(-1);
				}
				lineCount++;
			}

		} catch (FileNotFoundException ex) {
			System.err.println("System error: unable to find file");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.err.println("System error: error reading file");
			ex.printStackTrace();
		}

		return board;
	}

	public static List<Move> boardUtility(char[][] board) {

		List<Move> result = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {

				// check if coordinate is empty
				if (board[i][j] == CONSTANTS.EMPTY) {

					// place player move
					board[i][j] = CONSTANTS.PLAYER;

					// find the utility and map into map
					result.add(new Move(i, j, MoveUtility(board)));

					// undo player move
					board[i][j] = CONSTANTS.EMPTY;
				}
			}
		}

		return result;

	}

	// NOTE: have to check player before oppo win --> if not oppo always win
	public static int MoveUtility(char[][] board) {

		char[][] boardClone = board;

		// check for any wins in row
		for (int row = 0; row < 3; row++) {
			if (boardClone[row][0] == boardClone[row][1] && boardClone[row][1] == boardClone[row][2]) {
				if (boardClone[row][0] == CONSTANTS.PLAYER)
					return 1;
			}
		}

		for (int row = 0; row < 3; row++) {
			if (isOppoWin(boardClone[row])) 
				return -1;
		}

		// check for any wins in column
		for (int col = 0; col < 3; col++) {

			if (boardClone[0][col] == boardClone[1][col] && boardClone[1][col] == boardClone[2][col]) {
				if (boardClone[0][col] == CONSTANTS.PLAYER)
					return 1;
			}
		}

		for (int col = 0; col < 3; col++) {
			// prepare column character to check for oppo wins in row
			char[] colChar = new char[3];

			for (int row = 0; row < 3; row++) {
				colChar[row] = boardClone[row][col];
			}
			if (isOppoWin(colChar)) 
				return -1;
			
		}

		// check for any wins in diagonal
			// for player
		if (boardClone[0][0] == boardClone[1][1] && boardClone[1][1] == boardClone[2][2]) {
			if (boardClone[1][1] == CONSTANTS.PLAYER)
				return 1;
		}
		if (boardClone[2][0] == boardClone[1][1] && boardClone[1][1] == boardClone[0][2]) {
			if (boardClone[1][1] == CONSTANTS.PLAYER)
				return 1;
		}
			// for oppo
		char[] diagChar1 = { boardClone[0][0], boardClone[1][1], boardClone[2][2] };
		if (isOppoWin(diagChar1)) 
			return -1;
		
		char[] diagChar2 = { boardClone[2][0], boardClone[1][1], boardClone[0][2] };
		if (isOppoWin(diagChar2)) 
			return -1;
		

		// if no condition fulfills --> no wins yet
		return 0;

	}

	// !!!!!!!!!!!!!!!!!!! CHange logic to permutation --> contains all does NOT
	// work
	public static boolean isOppoWin(char[] line) {

		StringBuilder sequence = new StringBuilder();

		for (char c : line)
			sequence.append(c);

		return CONSTANTS.OPPO_WIN.contains(sequence.toString());
	}

}
