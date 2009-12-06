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

public class HiddenSingle extends Strategy
{
    public int getDifficulty()
    {
        return (2);
    }
    
    public boolean applyStrategy(PossibleValues possibleValues, int[][] gameData)
    {   
        // Check blocks first
        for (int x = 0; x < 9; x += 3)
        {
            for (int y = 0; y < 9; y += 3)
            {
                for (int n = 1; n <= 9; n++)
                {
                    beginCheckBlockValue: if (!blockContains(gameData, x, y, n))
                    {
                        int count = 0;
                        int xPos = 0;
                        int yPos = 0;
                        
                        for (int i = 0; i < 3; i++)
                        {
                            for (int j = 0; j < 3; j++)
                            {
                                if ((gameData[x + i][y + j] == 0) &&
                                    possibleValues.isValuePossible(x + i, y + j, n))
                                {
                                    count++;
                                    
                                    if (count > 1)
                                    {
                                        n++;
                                        
                                        break beginCheckBlockValue;
                                    }
                                    
                                    xPos = x + i;
                                    yPos = y + j;
                                }
                            }
                        }
                        
                        if (count == 1)
                        {
                            gameData[xPos][yPos] = n;
                            possibleValues.processNewCellValue(xPos, yPos, n);

                            return (true);
                        }
                    }
                }
            }
        }
        
        // Check rows
        for (int y = 0; y < 9; y++)
        {
            for (int n = 1; n <= 9; n++)
            {
                beginCheckRowValue: if (!rowContains(gameData, y, n))
                {
                    int count = 0;
                    int xPos = 0;
                    
                    for (int x = 0; x < 9; x++)
                    {
                        if ((gameData[x][y] == 0) &&
                            possibleValues.isValuePossible(x, y, n))
                        {
                            count++;
                            
                            if (count > 1)
                            {
                                n++;
                                
                                break beginCheckRowValue;
                            }
                            
                            xPos = x;
                        }
                    }
                    
                    if (count == 1)
                    {
                        gameData[xPos][y] = n;
                        possibleValues.processNewCellValue(xPos, y, n);

                        return (true);
                    }
                }
            }
        }
        
        // Check columns
        for (int x = 0; x < 9; x++)
        {
            for (int n = 1; n <= 9; n++)
            {
                beginCheckColumnValue: if (!columnContains(gameData, x, n))
                {
                    int count = 0;
                    int yPos = 0;
                    
                    for (int y = 0; y < 9; y++)
                    {
                        if ((gameData[x][y] == 0) &&
                            possibleValues.isValuePossible(x, y, n))
                        {
                            count++;
                            
                            if (count > 1)
                            {
                                n++;
                                
                                break beginCheckColumnValue;
                            }
                            
                            yPos = y;
                        }
                    }
                    
                    if (count == 1)
                    {
                        gameData[x][yPos] = n;
                        possibleValues.processNewCellValue(x, yPos, n);

                        return (true);
                    }
                }
            }
        }

        return (false);
    }
}
