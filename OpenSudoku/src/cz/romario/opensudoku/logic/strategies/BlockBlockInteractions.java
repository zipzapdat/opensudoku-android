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

import java.util.Vector;

import cz.romario.opensudoku.logic.PossibleValues;
import cz.romario.opensudoku.logic.Strategy;

/**
 *
 * @author  mparker
 */
public class BlockBlockInteractions extends Strategy
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
                        CountContainer blockContainer = getRowAndColumnCounts(possibleValues, 
                            gameData, x, y, n);
                        
                        int blockRowCount = 0;
                        int blockColumnCount = 0;
                        
                        for (int i = 0; i < 3; i++)
                        {
                            if (blockContainer.getRowCounts()[i] > 0)
                            {
                                blockRowCount++;
                            }
                            
                            if (blockContainer.getColumnCounts()[i] > 0)
                            {
                                blockColumnCount++;
                            }
                        }
                        
                        if (blockRowCount == 2)
                        {
                            // Check surrounding blocks
                            for (int i = 0; i < 9; i += 3)
                            {
                                if (i == x)
                                {
                                    continue;
                                }
                                
                                CountContainer targetContainer = getRowAndColumnCounts(
                                    possibleValues, gameData, i, y, n);
                                
                                int matchingCounts = 0;
                                
                                for (int j = 0; j < 3; j++)
                                {
                                    int blockCount = blockContainer.getRowCounts()[j];
                                    int targetCount = targetContainer.getRowCounts()[j];

                                    if (((blockCount > 0) && (targetCount > 0)) ||
                                        ((blockCount == 0) && (targetCount == 0)))
                                    {
                                        matchingCounts++;
                                    }
                                }
                                
                                if (matchingCounts == 3)
                                {
                                    // Check if remaining block already contains
                                    // the value
                                    for (int j = 0; j < 9; j += 3)
                                    {
                                        if ((j != x) && (j != i) && !blockContains(gameData, j, y, n))
                                        {
                                            boolean updated = false;

                                            for (int k = 0; k < 3; k++)
                                            {
                                                if (targetContainer.getRowCounts()[k] > 0)
                                                {
                                                    for (int l = 0; l < 3; l++)
                                                    {
                                                        if (possibleValues.isValuePossible(j + l, y + k, n))
                                                        {
                                                            possibleValues.removePossibleValue(j + l, y + k, n);
                                                            updated = true;
                                                        }
                                                    }
                                                }
                                            }
                                            
                                            if (updated)
                                            {
                                                return (true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        if (blockColumnCount == 2)
                        {
                            // Check surrounding blocks
                            for (int i = 0; i < 9; i += 3)
                            {
                                if (i == y)
                                {
                                    continue;
                                }
                                
                                CountContainer targetContainer = getRowAndColumnCounts(
                                    possibleValues, gameData, x, i, n);
                                
                                int matchingCounts = 0;
                                
                                for (int j = 0; j < 3; j++)
                                {
                                    int blockCount = blockContainer.getColumnCounts()[j];
                                    int targetCount = targetContainer.getColumnCounts()[j];

                                    if (((blockCount > 0) && (targetCount > 0)) ||
                                        ((blockCount == 0) && (targetCount == 0)))
                                    {
                                        matchingCounts++;
                                    }
                                }
                                
                                if (matchingCounts == 3)
                                {
                                    // Check if remaining block already contains
                                    // the value
                                    for (int j = 0; j < 9; j += 3)
                                    {
                                        if ((j != y) && (j != i) && !blockContains(gameData, x, j, n))
                                        {
                                            boolean updated = false;

                                            for (int k = 0; k < 3; k++)
                                            {
                                                if (targetContainer.getColumnCounts()[k] > 0)
                                                {
                                                    for (int l = 0; l < 3; l++)
                                                    {
                                                        if (possibleValues.isValuePossible(x + k, j + l, n))
                                                        {
                                                            possibleValues.removePossibleValue(x + k, j + l, n);
                                                            updated = true;
                                                        }
                                                    }
                                                }
                                            }
                                            
                                            if (updated)
                                            {
                                                return (true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return (false);
    }
    
    private CountContainer getRowAndColumnCounts(PossibleValues possibleValues, int[][] gameData, 
        int x, int y, int n)
    {
        Vector<Candidate> blockCandidates = getCandidatesForBlock(possibleValues, gameData, 
            x, y, n);

        int[] rowCounts = new int[3];
        int[] columnCounts = new int[3];

        for (int i = 0; i < blockCandidates.size(); i++)
        {
            Candidate nextCandidate = (Candidate)blockCandidates.elementAt(i);

            rowCounts[nextCandidate.getY() % 3]++;
            columnCounts[nextCandidate.getX() % 3]++;
        }
        
        return (new CountContainer(rowCounts, columnCounts));
    }
}
