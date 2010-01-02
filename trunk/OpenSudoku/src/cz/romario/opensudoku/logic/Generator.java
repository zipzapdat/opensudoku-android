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
 * 
 */

package cz.romario.opensudoku.logic;

import java.util.Random;
import java.util.Vector;


import android.util.Log;

/**
*
* @author Matt Parker
* @author Victor Ferrer
* @author Martin Helff
*/
public class Generator// extends Random
{
    private int[][] gameData;
    private int[][] solvedGrid;
    private Solver solver;
    private Random random;
    
    public static final int DIFFICULTY_EASY         = 1;
    public static final int DIFFICULTY_MEDIUM       = 2;
    public static final int DIFFICULTY_HARD         = 3;
    
    public Generator()
    {
        gameData = new int[9][9];
        solvedGrid = new int[9][9];
        solver = new Solver();
        random = new Random();
    }
    
    public void generate(int difficultyLevel)
    {
        random.setSeed(System.currentTimeMillis());
        
        int maxAttempts = (2 * difficultyLevel * difficultyLevel) * (4 + difficultyLevel * 2);
        int hardestDifficulty = 0;
        int[][] workingArray = new int[9][9];
        int[][] hardestGameFound = new int[9][9];
        int[][] hardestSolvedGrid = new int[9][9];
        boolean firstRunThrough = true;
        
        while (hardestDifficulty == 0)
        {
            for (int i = 0; i < maxAttempts; i++)
            {
                gameData = new int[9][9];
                generateGrid(gameData, 0);
                solvedGrid = Generator.cloneArray(gameData);
                workingArray = generateInitialPositions(Generator.cloneArray(gameData), difficultyLevel);
                
                if (workingArray == null)
                {
                    continue;
                }

                gameData = Generator.cloneArray(workingArray);
                solver.solveGame(Generator.cloneArray(workingArray), true);
                
                if (convertDifficultyLevel(solver.getDifficultyLevel()) == difficultyLevel)
                {   
                    gameData = Generator.cloneArray(workingArray);
                    
                    return;
                }
                else if (solver.getDifficultyLevel() > hardestDifficulty)
                {   
                    hardestDifficulty = solver.getDifficultyLevel();
                    hardestGameFound = Generator.cloneArray(workingArray);
                    hardestSolvedGrid = Generator.cloneArray(solvedGrid);
                }
                
                if (!firstRunThrough && (hardestDifficulty > 0))
                {
                    // Set the loop variables so we jump out of the loop
                    i = maxAttempts;
                }
            }
            
            firstRunThrough = false;
        }
        
        gameData = Generator.cloneArray(hardestGameFound);
        solvedGrid = Generator.cloneArray(hardestSolvedGrid);
    }
    
    private int convertDifficultyLevel(int solvedDifficultyLevel)
    {
        if ((solvedDifficultyLevel > 55) && (solvedDifficultyLevel <= 90))
        {
            return (DIFFICULTY_EASY);
        }
        else if ((solvedDifficultyLevel > 90) && (solvedDifficultyLevel <= 130))
        {
            return (DIFFICULTY_MEDIUM);
        }
        else if (solvedDifficultyLevel > 130)
        {
            return (DIFFICULTY_HARD);
        }
        else
        {
            return (-1);
        }
    }
    
    public int[][] getGeneratedGame()
    {
        return (gameData);
    }
    
    public int[][] getSolvedGrid()
    {
        return (solvedGrid);
    }

    private boolean generateGrid(int[][] array, int position)
    {
        if (position == 81)
        {
            return (true);
        }
        
        int x = position / 9;
        int y = position % 9;

        Vector possiblesVector = getCorrectValuesForArray(x, y, array);

        while (!possiblesVector.isEmpty())
        {
            int index = random.nextInt(possiblesVector.size());
            int nextCandidate = ((Integer)possiblesVector.elementAt(index)).intValue();

            array[x][y] = nextCandidate;
            
            if (isCorrect(x, y, nextCandidate, array))
            {
                if (generateGrid(array, (position + 1)))
                {
                    return (true);
                }
            }

            possiblesVector.removeElementAt(index);
        }
        
        gameData[x][y] = 0;
        
        return (false);
    }
    
    private int[][] generateInitialPositions(int[][] array, int difficultyLevel)
    {
        int maxFilledPositions = 35 - (difficultyLevel * 3);
        boolean[] attempted = new boolean[81];
        int noOfFilled = 81;
        
        for (int i = 0; i < attempted.length; i++)
        {
            attempted[i] = false;
        }
        
        while ((noOfFilled > maxFilledPositions) && (noOfFilled > 1))
        {
            // Check if there are any cells not attempted
            boolean attemptedAll = true;
            
            for (int i = 0; i < attempted.length; i++)
            {
                if (!attempted[i])
                {
                    attemptedAll = false;
                    break;
                }
            }
            
            if (attemptedAll)
            {
                return (null);
            }
            
            int position = random.nextInt(81);
            
            do
            {
                if (position < 80)
                {
                    position++;
                }
                else
                {
                    position = 0;
                }
            } while (attempted[position]);
            
            int x = position / 9;
            int y = position % 9;
            int normalValue = array[x][y];
            int oppositeValue = 0;
            
            array[x][y] = 0;
            attempted[position] = true;
            noOfFilled--;

            if ((x != 4) || (y != 4))
            {
                oppositeValue = array[8 - x][8 - y];
                array[8 - x][8 - y] = 0;
                attempted[(9 * (8 - x)) + (8 - y)] = true;
                noOfFilled--;
            }
            
            int numberOfSolutions = solver.getPossibleValues(array).getNumberOfSolutions(-1);
            
            if (numberOfSolutions > 1)
            {
                if (normalValue > 0)
                {
                    array[x][y] = normalValue;
                    noOfFilled++;
                }
                
                if (oppositeValue > 0)
                {
                    array[8 - x][8 - y] = oppositeValue;
                    noOfFilled++;
                }
            }
        }
        
        return (array);
    }
    
    private Vector getCorrectValuesForArray(int x, int y, int[][] array)
    {
        Vector correctValues = new Vector();
        
        if (array[x][y] != 0) 
        {
            return (correctValues);
        }
        
        for (int i = 1; i <= 9; i++)
        {
            if (isCorrect(x, y, i, array))
            {
                correctValues.addElement(new Integer(i));
            }
        }
        
        return (correctValues);
    }
    
    private boolean isCorrect(int x, int y, int value, int[][] array)
    {
        for (int i = 0; i < 9; i++)
        {
            if ((array[x][i] == value) && (i != y)) 
            {
                return (false);
            }
            
            if ((array[i][y] == value) && (i != x))
            {
                return (false);
            }
        }
        
        for (int i = (x - (x % 3)); i < ((x - (x % 3)) + 3); i++)
        {
            for(int j = (y - (y % 3)); j < ((y - (y % 3)) + 3); j++)
            {
                if ((array[i][j] == value) && ((i != x) || (j != y))) 
                {
                    return (false);
                }
            }
        }
        
        return (true);
    }

	public static int[][] cloneArray(int[][] inputArray)
	{
	    if (inputArray == null)
	    {
	        return (null);
	    }
	    
	    int[][] outputArray = new int[inputArray.length][inputArray[0].length];
	    
	    for (int x = 0; x < outputArray.length; x++)
	    {
	        for (int y = 0; y < outputArray[0].length; y++)
	        {
	            outputArray[x][y] = inputArray[x][y];
	        }
	    }
	    
	    return (outputArray);
	}
}
