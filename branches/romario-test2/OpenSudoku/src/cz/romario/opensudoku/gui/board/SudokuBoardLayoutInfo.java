package cz.romario.opensudoku.gui.board;

/**
 * Layout info computed by {@link SudokuBoardView2}. It tells to overlays (see {@link BoardOverlay})
 * where cells are.
 * 
 * @author romario
 *
 */
class SudokuBoardLayoutInfo {
	public int left;
	public int top;
	public int right;
	public int bottom;
	
	public float cellWidth;
	public float cellHeight;
	
	public int sectorLineWidth;
}
