/**
 * Adopted to opensudoku-android from
 * Mobile Sudoku - A J2ME implementation of Sudoku.
 * Copyright (C) 2006 Matt Parker
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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

package cz.romario.opensudoku.logic;

import java.util.Enumeration;

/**
 *
 * @author Matt Parker
 */
public class Solver
{
    private int difficultyLevel;
    
    public Solver()
    {
        difficultyLevel = 0;
    }

    public int[][] solveGame(int[][] gameData, boolean generating)
    {   
        PossibleValues possibleValues = getPossibleValues(gameData);

        boolean gridUpdated = true;
        int numberFilled = getNumberOfSquaresFilled(gameData);
        StringBuffer difficulty = new StringBuffer();

        while (gridUpdated && (numberFilled < 81))
        {
            gridUpdated = false;

            for (Enumeration strategies = StrategyFactory.getStrategies(); strategies.hasMoreElements();)
            {
                Strategy nextStrategy = (Strategy)strategies.nextElement();

                if (!generating || (generating && (nextStrategy.getDifficulty() < 5)))
                {
                    if (nextStrategy.applyStrategy(possibleValues, gameData))
                    {
                        difficulty.append(nextStrategy.getDifficulty());
                        gridUpdated = true;
                        
                        break;
                    }
                }
            }

            numberFilled = getNumberOfSquaresFilled(gameData);
        }

        if (numberFilled < 81)
        {
            difficultyLevel = -1;
        }
        else
        {
            String difficultyString = difficulty.toString();
            difficultyLevel = difficulty.length();

            for (int i = 0; i < difficultyString.length(); i++)
            {
                int nextValue = Integer.valueOf(String.valueOf(difficultyString.charAt(i))).intValue();
                
                if (nextValue > 1)
                {
                    difficultyLevel += (nextValue * 2);
                }
            }
        }

        return (gameData);
    }

    public PossibleValues getPossibleValues(int[][] gameData)
    {
        return (new PossibleValues(gameData));
    }
    
    public int getDifficultyLevel()
    {
        return (difficultyLevel);
    }
    
    private int getNumberOfSquaresFilled(int[][] gameData)
    {
        int numberFilled = 0;
        
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                if (gameData[x][y] > 0)
                {
                    numberFilled++;
                }
            }
        }
        
        return (numberFilled);
    }
}

