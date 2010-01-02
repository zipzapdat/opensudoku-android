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
import cz.romario.opensudoku.logic.SetCombinationsCalculator;
import cz.romario.opensudoku.logic.Strategy;

/**
 *
 * @author  mparker
 */
public class HiddenSubset extends Strategy
{
    public int getDifficulty()
    {
        return (4);
    }
    
    public boolean applyStrategy(PossibleValues possibleValues, int[][] gameData)
    {
        for (int subsetSize = 2; subsetSize <= 4; subsetSize++)
        {
            for (int x = 0; x < 9; x += 3)
            {
                for (int y = 0; y < 9; y += 3)
                {
                    Vector blockSubsets = new Vector();

                    for (int i = 0; i < 3; i++)
                    {
                        for (int j = 0; j < 3; j++)
                        {
                            int[] cellPossibles = possibleValues.getPossibleValues(x + i, y + j);

                            if (cellPossibles == null)
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
                    
                    if (compareSubsets(subsetSize, blockSubsets, possibleValues))
                    {
                        return (true);
                    }
                }
            }
        
            // Check rows
            for (int y = 0; y < 9; y++)
            {
                Vector rowSubsets = new Vector();
                
                for (int x = 0; x < 9; x++)
                {
                    int[] cellPossibles = possibleValues.getPossibleValues(x, y);
    
                    if (cellPossibles == null)
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
                
                if (compareSubsets(subsetSize, rowSubsets, possibleValues))
                {
                    return (true);
                }
            }
                
            // Check columns
            for (int x = 0; x < 9; x++)
            {
                Vector columnSubsets = new Vector();
                
                for (int y = 0; y < 9; y++)
                {
                    int[] cellPossibles = possibleValues.getPossibleValues(x, y);
    
                    if (cellPossibles == null)
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
                
                if (compareSubsets(subsetSize, columnSubsets, possibleValues))
                {
                    return (true);
                }
            }
        }
        
        return (false);
    }
    
    private boolean compareSubsets(int subsetSize, Vector subsets, PossibleValues possibleValues)
    {
        if (subsetSize > subsets.size())
        {
            return (false);
        }
        
        Vector possibleSubsets = getAllPossibleSubsetsForBlockRowOrColumn(subsetSize, 
            subsets);;

        if (possibleSubsets.isEmpty())
        {
            return (false);
        }

        // Loop through the subsets and the possible subsets
        // to see if there are matches for the subset size
        for (int i = 0; i < possibleSubsets.size(); i++)
        {
            Subset possibleSubset = (Subset)possibleSubsets.elementAt(i);
            Vector foundSubsets = new Vector();
            
            for (int j = 0; j < subsets.size(); j++)
            {
                Subset blockSubset = (Subset)subsets.elementAt(j);
                
                if (blockSubset.containsOr(possibleSubset))
                {
                    foundSubsets.addElement(blockSubset);
                }
            }
            
            if (foundSubsets.size() == subsetSize)
            {
                boolean updated = false;

                for (int k = 0; k < foundSubsets.size(); k++)
                {
                    Subset nextFoundSubset = (Subset)foundSubsets.elementAt(k);

                    for (int l = 0; l < nextFoundSubset.getSubset().length; l++)
                    {
                        int value = nextFoundSubset.getSubset()[l];
                        
                        if (!possibleSubset.containsValue(value))
                        {
                            if (possibleValues.isValuePossible(nextFoundSubset.getX(), 
                                nextFoundSubset.getY(), value))
                            {
                                updated = true;
                                possibleValues.removePossibleValue(nextFoundSubset.getX(), 
                                    nextFoundSubset.getY(), value);
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
        
        return (false);
    }
    
    private Vector getAllPossibleSubsetsForBlockRowOrColumn(int subsetSize, Vector subsets)
    {
        Vector possibleSubsets = new Vector();
        Vector numberListVector = new Vector();
        
        for (int i = 0; i < subsets.size(); i++)
        {
            int[] possibleValues = ((Subset)subsets.elementAt(i)).getSubset();
            
            for (int j = 0; j < possibleValues.length; j++)
            {
                Integer nextValue = new Integer(possibleValues[j]);
                
                if (!numberListVector.contains(nextValue))
                {
                    numberListVector.addElement(nextValue);
                }
            }
        }
        
        if (numberListVector.isEmpty() || (numberListVector.size() < 2))
        {
            return (possibleSubsets);
        }
        
        int[] numberList = new int[numberListVector.size()];
        
        for (int i = 0; i < numberList.length; i++)
        {
            numberList[i] = ((Integer)numberListVector.elementAt(i)).intValue();
        }
        
        SetCombinationsCalculator calculator = new SetCombinationsCalculator(numberList, subsetSize);
        Vector combinations = calculator.getCombinations();
        
        for (int i = 0; i < combinations.size(); i++)
        {
            possibleSubsets.addElement(new Subset(0, 0, (int[])combinations.elementAt(i)));
        }
        
        return (possibleSubsets);
    }
}
