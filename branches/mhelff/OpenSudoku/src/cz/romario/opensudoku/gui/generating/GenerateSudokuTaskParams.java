package cz.romario.opensudoku.gui.generating;

import java.io.File;

public class GenerateSudokuTaskParams {
	
	/**
	 * Id of folder to generate sudoku to. Must be a valid folder id
	 */
	public long folderID;
	/**
	 * Difficulty (level) of sudoku puzzle.
	 */
	public int level;
	
	/**
	 * Comment for generated game (optional)
	 */
	public String comment;

}
