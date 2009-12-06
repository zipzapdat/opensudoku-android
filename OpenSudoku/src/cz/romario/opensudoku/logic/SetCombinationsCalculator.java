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

import java.util.Vector;

public class SetCombinationsCalculator
{
    private long total;
    private int[] indices;
    private int[] initialSet;
    
    private long combinationsLeft;

    public SetCombinationsCalculator(int[] set, int combinationLength)
    {
        if ((combinationLength < 1) || (combinationLength > set.length))
        {
            throw new IllegalArgumentException(
                "Combination length - " + combinationLength + " - must be between 1 and the set length (" + 
                set.length + ")");
        }
        
        total = initialiseTotal(set.length, combinationLength);
        
        initialSet = new int[set.length];
        System.arraycopy(set, 0, initialSet, 0, set.length);
        
        indices = new int[combinationLength];
    }
    
    public Vector getCombinations()
    {
        reset();
        
        Vector combinations = new Vector();
        
        while (hasNext())
        {
            combinations.addElement(next());
        }
        
        return (combinations);
    }
    
    private void reset()
    {
        initialiseIndices();
        
        combinationsLeft = total;
    }

    private boolean hasNext()
    {
        return (combinationsLeft > 0);
    }
    
    private int[] next()
    {
        if (combinationsLeft != total)
        {
            computeNext();
        }
        
        combinationsLeft--;
        
        return (getResult(indices));
    }

    private int[] getResult(int[] indices)
    {
        int[] results = new int[indices.length];
        
        for (int i = 0; i < results.length; i++)
        {
            results[i] = initialSet[indices[i]];
        }
        
        return (results);
    }
    
    private void computeNext()
    {
        // From Kenneth H. Rosen, Discrete Mathematics and Its Applications, 
        // 2nd edition (NY: McGraw-Hill, 1991), pp. 282-284
        int r = indices.length;
        int i = r - 1;
        int n = initialSet.length;

        while (indices[i] == n - r + i)
        {
            i--;
        }
        
        indices[i] = indices[i] + 1;
        
        for (int j = i + 1; j < r; j++)
        {
            indices[j] = indices[i] + j - i;
        }
    }
    
    private void initialiseIndices()
    {
        for (int i = 0; i < indices.length; i++)
        {
            indices[i] = i;
        }
    }
    
    private long initialiseTotal(int setLength, int combinationLength) 
    {
        long setLengthFactorial = getFactorial(setLength);
        long combinationLengthFactorial = getFactorial(combinationLength);
        long totalFactorial = getFactorial(setLength - combinationLength);
        
        return (setLengthFactorial / (combinationLengthFactorial * totalFactorial));
    }
    
    private long getFactorial(int integer)
    {
        long factorial = 1;
        
        for (int i = integer; i > 1; i--)
        {
            factorial *= i;
        }
        
        return (factorial);
    }
}
