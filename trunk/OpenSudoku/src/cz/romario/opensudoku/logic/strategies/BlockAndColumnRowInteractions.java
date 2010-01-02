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

/**
 *
 * @author  mparker
 */
public class BlockAndColumnRowInteractions extends Strategy
{
    public int getDifficulty()
    {
        return (3);
    }
    
    public boolean applyStrategy(PossibleValues possibleValues, int[][] gameData)
    {   
        for (int x = 0; x < 9; x += 3)
        {
            for (int y = 0; y < 9; y += 3)
            {
                for (int n = 1; n <= 9; n++)
                {
                    if (!blockContains(gameData, x, y, n))
                    {
                        int count = 0;
                        int xPos = 0;
                        int yPos = 0;
                        
                        // Check rows
                        for (int i = 0; i < 3; i++)
                        {
                            if (possibleValues.isValuePossible(x, (y + i), n) ||
                                possibleValues.isValuePossible((x + 1), (y + i), n) ||
                                possibleValues.isValuePossible((x + 2), (y + i), n))
                            {
                                count++;
                                
                                if (count > 1)
                                {
                                    break;
                                }
                                
                                yPos = y + i;
                            }
                        }
                        
                        if (count == 1)
                        {
                            boolean foundInRestOfRow = false;
                            
                            for (int index = 0; index < 9; index++)
                            {
                                if ((index != x) && (index != (x + 1)) && (index != (x + 2)))
                                {
                                    if (possibleValues.isValuePossible(index, yPos, n))
                                    {
                                        foundInRestOfRow = true;
                                        possibleValues.removePossibleValue(index, yPos, n);
                                    }
                                }
                            }
                            
                            if (foundInRestOfRow)
                            {
                                return (true);
                            }
                        }
                        
                        // Reset count
                        count = 0;
                        
                        // Check columns
                        for (int j = 0; j < 3; j++)
                        {
                            if (possibleValues.isValuePossible((x + j), y, n) ||
                                possibleValues.isValuePossible((x + j), (y + 1), n) ||
                                possibleValues.isValuePossible((x + j), (y + 2), n))
                            {
                                count++;
                                
                                if (count > 1)
                                {
                                    break;
                                }
                                
                                xPos = x + j;
                            }
                        }
                        
                        if (count == 1)
                        {
                            boolean foundInRestOfColumn = false;
                            
                            for (int index = 0; index < 9; index++)
                            {
                                if ((index != y) && (index != (y + 1)) && (index != (y + 2)))
                                {
                                    if (possibleValues.isValuePossible(xPos, index, n))
                                    {
                                        foundInRestOfColumn = true;
                                        possibleValues.removePossibleValue(xPos, index, n);
                                    }
                                }
                            }
                            
                            if (foundInRestOfColumn)
                            {
                                return (true);
                            }
                        }
                    }
                }
            }
        }
        
        return (false);
    }
}
