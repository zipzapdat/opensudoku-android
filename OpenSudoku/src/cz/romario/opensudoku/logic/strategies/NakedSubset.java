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
public class NakedSubset extends Strategy
{
    public int getDifficulty()
    {
        return (2);
    }
    
    public boolean applyStrategy(PossibleValues possibleValues, int[][] gameData)
    {
        for (int subset = 2; subset <= 4; subset++)
        {
            // Check blocks
            for (int x = 0; x < 9; x += 3)
            {
                for (int y = 0; y < 9; y += 3)
                {
                    Vector<Subset> blockSubsets = new Vector<Subset>();

                    for (int i = 0; i < 3; i++)
                    {
                        for (int j = 0; j < 3; j++)
                        {
                            int[] cellPossibles = possibleValues.getPossibleValues(x + i, y + j);

                            if ((cellPossibles == null) || (cellPossibles.length != subset))
                            {
                                continue;
                            }

                            Subset newSubset = new Subset(x + i, y + j, cellPossibles);

                            blockSubsets.addElement(newSubset);
                        }
                    }

                    if (blockSubsets.isEmpty())
                    {
                        continue;
                    }

                    int noOfSubsets = blockSubsets.size();

                    for (int i = 0; i < noOfSubsets; i++)
                    {
                        int subsetCount = 0;
                        Subset outerSubset = (Subset)blockSubsets.elementAt(i);

                        for (int j = 0; j < noOfSubsets; j++)
                        {
                            if (i == j)
                            {
                                continue;
                            }

                            if (((Subset)blockSubsets.elementAt(j)).equals(outerSubset))
                            {
                                subsetCount++;
                            }
                        }

                        if (subsetCount == (subset - 1))
                        {
                            // Matching subset(s) found in block
                            boolean updated = false;
                            int[] outerSubsetArray = outerSubset.getSubset();

                            for (int k = 0; k < 3; k++)
                            {
                                for (int l = 0; l < 3; l++)
                                {
                                    if (!compareArrays(outerSubsetArray, 
                                        possibleValues.getPossibleValues(x + k, y + l)))
                                    {
                                        for (int n = 0; n < outerSubsetArray.length; n++)
                                        {
                                            if (possibleValues.isValuePossible(x + k, y + l, 
                                                outerSubsetArray[n]))
                                            {
                                                updated = true;
                                                possibleValues.removePossibleValue(x + k, y + l, 
                                                    outerSubsetArray[n]);
                                            }
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
            
            // Check rows
            for (int y = 0; y < 9; y++)
            {
                Vector<Subset> rowSubsets = new Vector<Subset>();
                
                for (int x = 0; x < 9; x++)
                {
                    int[] cellPossibles = possibleValues.getPossibleValues(x, y);

                    if ((cellPossibles == null) || (cellPossibles.length != subset))
                    {
                        continue;
                    }

                    Subset newSubset = new Subset(x, y, cellPossibles);

                    rowSubsets.addElement(newSubset);
                }
                
                if (rowSubsets.isEmpty())
                {
                    continue;
                }
                
                int noOfSubsets = rowSubsets.size();
                
                for (int i = 0; i < noOfSubsets; i++)
                {
                    int subsetCount = 0;
                    Subset outerSubset = (Subset)rowSubsets.elementAt(i);
                    
                    for (int j = 0; j < noOfSubsets; j++)
                    {
                        if (i == j)
                        {
                            continue;
                        }
                        
                        if (((Subset)rowSubsets.elementAt(j)).equals(outerSubset))
                        {
                            subsetCount++;
                        }
                    }
                    
                    if (subsetCount == (subset - 1))
                    {
                        // Matching subset(s) found in row
                        boolean updated = false;
                        int[] outerSubsetArray = outerSubset.getSubset();
                        
                        for (int x = 0; x < 9; x++)
                        {
                            if (!compareArrays(outerSubsetArray,
                                possibleValues.getPossibleValues(x, y)))
                            {
                                for (int n = 0; n < outerSubsetArray.length; n++)
                                {
                                    if (possibleValues.isValuePossible(x, y, outerSubsetArray[n]))
                                    {
                                        updated = true;
                                        possibleValues.removePossibleValue(x, y, outerSubsetArray[n]);
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
            
            // Check columns
            for (int x = 0; x < 9; x++)
            {
                Vector<Subset> columnSubsets = new Vector<Subset>();
                
                for (int y = 0; y < 9; y++)
                {
                    int[] cellPossibles = possibleValues.getPossibleValues(x, y);

                    if ((cellPossibles == null) || (cellPossibles.length != subset))
                    {
                        continue;
                    }

                    Subset newSubset = new Subset(x, y, cellPossibles);

                    columnSubsets.addElement(newSubset);
                }
                
                if (columnSubsets.isEmpty())
                {
                    continue;
                }
                
                int noOfSubsets = columnSubsets.size();
                
                for (int i = 0; i < noOfSubsets; i++)
                {
                    int subsetCount = 0;
                    Subset outerSubset = (Subset)columnSubsets.elementAt(i);
                    
                    for (int j = 0; j < noOfSubsets; j++)
                    {
                        if (i == j)
                        {
                            continue;
                        }
                        
                        if (((Subset)columnSubsets.elementAt(j)).equals(outerSubset))
                        {
                            subsetCount++;
                        }
                    }
                    
                    if (subsetCount == (subset - 1))
                    {
                        // Matching subset(s) found in column
                        boolean updated = false;
                        int[] outerSubsetArray = outerSubset.getSubset();
                        
                        for (int y = 0; y < 9; y++)
                        {
                            if (!compareArrays(outerSubsetArray,
                                possibleValues.getPossibleValues(x, y)))
                            {
                                for (int n = 0; n < outerSubsetArray.length; n++)
                                {
                                    if (possibleValues.isValuePossible(x, y, outerSubsetArray[n]))
                                    {
                                        updated = true;
                                        possibleValues.removePossibleValue(x, y, outerSubsetArray[n]);
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
        
        return (false);
    }
}
