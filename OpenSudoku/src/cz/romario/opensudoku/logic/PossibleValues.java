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
package cz.romario.opensudoku.logic;

public class PossibleValues
{
    public static final int NOT_POSSIBLE = 0;
    public static final int POSSIBLE = 1;

    // This array is [x][y] with the third component being
    // between 0 and 9. Position 0 within the third component
    // signifies either the number of possible values for that
    // cell or if the number is negative - the number already
    // calculated for that cell. Positions 1 to 9 hold either
    // a 0 if that value is not valid for the cell, or 1 if it
    // is a valid number for the cell
    private int[][][] possibleValues;
    
    public PossibleValues(int [][] gameData)
    {
        possibleValues = new int[9][9][10];
        
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                if (gameData[x][y] > 0)
                {
                    possibleValues[x][y][0] = -gameData[x][y];
                }
                else
                {
                    possibleValues[x][y][0] = 9;

                    for (int n = 1; n <= 9; n++)
                    {
                        possibleValues[x][y][n] = POSSIBLE;
                        
                        if (rowContains(gameData, y, n) ||
                            columnContains(gameData, x, n) ||
                            blockContains(gameData, (x / 3), (y / 3), n))
                        {
                            possibleValues[x][y][0]--;
                            possibleValues[x][y][n] = NOT_POSSIBLE;
                        }
                    }
                }
            }
        }
    }
    
    private PossibleValues(int [][][] possibleValues)
    {
        this.possibleValues = possibleValues;
    }
    
    private boolean rowContains(int[][] gameData, int y, int value)
    {
        for (int x = 0; x < 9; x++)
        {
            if (gameData[x][y] == value)
            {
                return (true);
            }
        }
        
        return (false);
    }
    
    private boolean columnContains(int[][] gameData, int x, int value)
    {
        for (int y = 0; y < 9; y++)
        {
            if (gameData[x][y] == value)
            {
                return (true);
            }
        }
        
        return (false);
    }
    
    private boolean blockContains(int[][] gameData, int x, int y, int value)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (gameData[(x * 3) + i][(y * 3) + j] == value)
                {
                    return (true);
                }
            }
        }
        
        return (false);
    }

    public void processNewCellValue(int x, int y, int value)
    {
        possibleValues[x][y][0] = -value;
        
        for (int n = 1; n <= 9; n++)
        {
            possibleValues[x][y][n] = NOT_POSSIBLE;
        }
        
        // Clear block
        int xStart = (x / 3) * 3;
        int yStart = (y / 3) * 3;
        
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (((xStart + i) != x) && ((yStart + j) != y))
                {
                    int xPos = xStart + i;
                    int yPos = yStart + j;

                    if (possibleValues[xPos][yPos][value] == POSSIBLE)
                    {
                        possibleValues[xPos][yPos][0]--;
                        possibleValues[xPos][yPos][value] = NOT_POSSIBLE;
                    }
                }
            }
        }
        
        // Clear the row
        for (int i = 0; i < 9; i++)
        {
            if ((i != x) && (possibleValues[i][y][value] == POSSIBLE))
            {
                possibleValues[i][y][0]--;
                possibleValues[i][y][value] = NOT_POSSIBLE;
            }
        }
        
        // Clear the column
        for (int i = 0; i < 9; i++)
        {
            if ((i != y) && (possibleValues[x][i][value] == POSSIBLE))
            {
                possibleValues[x][i][0]--;
                possibleValues[x][i][value] = NOT_POSSIBLE;
            }
        }
    }
    
    public boolean isValuePossible(int x, int y, int value)
    {
        return (possibleValues[x][y][value] == POSSIBLE);
    }
    
    public void removePossibleValue(int x, int y, int value)
    {
        if (possibleValues[x][y][value] == POSSIBLE)
        {
            possibleValues[x][y][0]--;
            possibleValues[x][y][value] = NOT_POSSIBLE;
        }
    }

    public int[] getPossibleValues(int x, int y)
    {
        int numberOfValues = possibleValues[x][y][0];
        
        if (numberOfValues < 1)
        {
            return (null);
        }
        
        int[] possiblesArray = new int[numberOfValues];
        int index = 0;
        
        for (int n = 1; n <= 9; n++)
        {
            if (possibleValues[x][y][n] == POSSIBLE)
            {
                possiblesArray[index++] = n;
            }
        }
        
        return (possiblesArray);
    }

    public int getNumberOfSolutions(int depth)
    {
        int row = -1;
        int column = -1;
        
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                if (possibleValues[x][y][0] == 0)
                {
                    return (0);
                }

                // We ignore solved cells
                if ((possibleValues[x][y][0] > 0) &&
                    ((row == -1) || (possibleValues[x][y][0] < possibleValues[row][column][0])))
                {
                    row = x;
                    column = y;
                }
            }
        }

        if (row == -1) 
        {
            // Found a solution
            return (1);
        }

        int numberOfSolutions = 0;
        int numberOfPossibles = possibleValues[row][column][0];
        
        for (int n = 1; n <= 9; n++)
        {
            if (possibleValues[row][column][n] != 1)
            {
                continue;
            }
            
            possibleValues[row][column][0] = -n;

            for (int i = 0; i < 9; i++)
            {
                int[] possiblesArray = possibleValues[row][i];
                
                if ((possiblesArray[0] >= 0) && (possiblesArray[n] == PossibleValues.POSSIBLE))
                {
                    possiblesArray[0]--;
                    possiblesArray[n] = depth;
                }
                
                possiblesArray = possibleValues[i][column];
                
                if ((possiblesArray[0] >= 0) && (possiblesArray[n] == PossibleValues.POSSIBLE))
                {
                    possiblesArray[0]--;
                    possiblesArray[n] = depth;
                }
                
                possiblesArray = possibleValues[((row / 3) * 3) + (i / 3)][((column / 3) * 3) + (i % 3)];
                
                if ((possiblesArray[0] >= 0) && (possiblesArray[n] == PossibleValues.POSSIBLE))
                {
                    possiblesArray[0]--;
                    possiblesArray[n] = depth;
                }
            }
            
            numberOfSolutions += getNumberOfSolutions(depth - 1);
            
            for (int i = 0; i < 9; i++)
            {
                int[] possiblesArray = possibleValues[row][i];
                
                if ((possiblesArray[0] >= 0) && (possiblesArray[n] == depth))
                {
                    possiblesArray[0]++;
                    possiblesArray[n] = PossibleValues.POSSIBLE;
                }
                
                possiblesArray = possibleValues[i][column];
                
                if ((possiblesArray[0] >= 0) && (possiblesArray[n] == depth))
                {
                    possiblesArray[0]++;
                    possiblesArray[n] = PossibleValues.POSSIBLE;
                }
                
                possiblesArray = possibleValues[((row / 3) * 3) + (i / 3)][((column / 3) * 3) + (i % 3)];
                
                if ((possiblesArray[0] >= 0) && (possiblesArray[n] == depth))
                {
                    possiblesArray[0]++;
                    possiblesArray[n] = PossibleValues.POSSIBLE;
                }
            }
            
            if (numberOfSolutions > 1)
            {
                break;
            }
        }

        possibleValues[row][column][0] = numberOfPossibles;
        
        return (numberOfSolutions);
    }
    
    public PossibleValues getClone()
    {
        int[][][] clonedPossibles = new int[9][9][10];
        
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 9; y++)
            {
                for (int z = 0; z < 10; z++)
                {
                    clonedPossibles[x][y][z] = possibleValues[x][y][z];
                }
            }
        }
        
        return (new PossibleValues(clonedPossibles));
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("\n-------------------------------------------------------\n");
        
        for (int j = 0; j < 9; j++)
        {
            for (int subLine = 0; subLine < 3; subLine++)
            {
                buffer.append("| ");
                
                for (int i = 0; i < 9; i++)
                {
                    for (int subCol = 0; subCol < 3; subCol++)
                    {
                        int value = (subLine * 3) + subCol + 1;
                        
                        if (possibleValues[i][j][value] == POSSIBLE)
                        {
                            buffer.append(value);
                        }
                        else
                        {
                            buffer.append(" ");
                        }
                    }
                    
                    buffer.append(" | ");
                }
                
                buffer.append("\n");
            }
            
            buffer.append("-------------------------------------------------------\n");
        }
        
        return (buffer.toString());
    }
}
