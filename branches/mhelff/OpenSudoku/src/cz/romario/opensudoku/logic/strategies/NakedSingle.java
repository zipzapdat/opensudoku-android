/**
 * Adopted to opensudoku-android from
 * Mobile Sudoku - A J2ME implementation of Sudoku.
 * Copyright (C) 2006 Matt Parker
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU General Public License 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package cz.romario.opensudoku.logic.strategies;

import cz.romario.opensudoku.logic.PossibleValues;
import cz.romario.opensudoku.logic.Strategy;

public class NakedSingle extends Strategy
{   
    public NakedSingle(boolean suppressDebug)
    {
    }
    
    public int getDifficulty()
    {
        return (1);
    }
    
    public boolean applyStrategy(PossibleValues possibleValues, int[][] gameData)
    { 
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                int[] cellPossibles = possibleValues.getPossibleValues(x, y);
                
                if ((cellPossibles != null) && (cellPossibles.length == 1))
                {
                    int value = cellPossibles[0];

                    gameData[x][y] = value;
                    possibleValues.processNewCellValue(x, y, value);
                    
                    return (true);
                }
            }
        }
        
        return (false);
    }
}
