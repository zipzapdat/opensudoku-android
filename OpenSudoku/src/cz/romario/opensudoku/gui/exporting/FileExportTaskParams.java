package cz.romario.opensudoku.gui.exporting;

public class FileExportTaskParams {
	
	/**
	 * Id of folder to export. Set to -1, if you want to export all folders.
	 */
	public Long folderID;
	/**
	 * Id of sudoku puzzle to export.
	 */
	public Long sudokuID;
	
	/**
	 * File name (including path) where data should be saved. If not set, temporary
	 * file will be created.
	 */
	public String fileName;

}
