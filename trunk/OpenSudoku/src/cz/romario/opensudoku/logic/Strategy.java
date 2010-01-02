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

/**
 *
 * @author Matt Parker
 */
public abstract class Strategy
{
    public abstract int getDifficulty();
    public abstract boolean applyStrategy(PossibleValues possibleValues, int[][] gameData);

    protected boolean rowContains(int[][] gameData, int y, int value)
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
    
    protected boolean columnContains(int[][] gameData, int x, int value)
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
    
    protected boolean blockContains(int[][] gameData, int x, int y, int value)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (gameData[x + i][y + j] == value)
                {
                    return (true);
                }
            }
        }
        
        return (false);
    }
    
    protected Vector<Candidate> getCandidatesForBlock(PossibleValues possibleValues, int[][]gameData, 
        int x, int y, int candidateValue)
    {
        Vector<Candidate> candidates = new Vector<Candidate>();
        
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if ((gameData[x + i][y + j] < 1) && 
                    possibleValues.isValuePossible(x + i, y + j, candidateValue))
                {
                    candidates.addElement(new Candidate(x + i, y + j, candidateValue));
                }
            }
        }
        
        return (candidates);
    }
    
    protected boolean compareArrays(int[] array1, int[] array2)
    {
        if ((array1 == null) && (array2 == null))
        {
            return (true);
        }
        else if ((array1 == null) || (array2 == null))
        {
            return (false);
        }
        
        if (array1.length != array2.length)
        {
            return (false);
        }

        for (int i = 0; i < array1.length; i++)
        {
            if (array1[i] != array2[i])
            {
                return (false);
            }
        }
        
        return (true);
    }
    
    public class CountContainer
    {
        private int[] rowCounts;
        private int[] columnCounts;
        
        public CountContainer(int[] rowCounts, int[] columnCounts)
        {
            this.rowCounts = rowCounts;
            this.columnCounts = columnCounts;
        }
        
        public int[] getRowCounts()
        {
            return (rowCounts);
        }
        
        public int[] getColumnCounts()
        {
            return (columnCounts);
        }
    }
    
    public class Candidate
    {
        private int x;
        private int y;
        private int value;

        public Candidate(int x, int y, int value)
        {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        public int getX()
        {
            return (x);
        }

        public int getY()
        {
            return (y);
        }

        public int getValue()
        {
            return (value);
        }
        
        public String toString()
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Coords - ");
            buffer.append(x);
            buffer.append(',');
            buffer.append(y);
            buffer.append(" - Value - ");
            buffer.append(value);
            
            return (buffer.toString());
        }
    }

    public class Subset
    {
        private int x;
        private int y;
        private int[] subset;
        
        public Subset(int x, int y, int[] subset)
        {
            this.x = x;
            this.y = y;
            this.subset = subset;
        }
        
        public int getX()
        {
            return (x);
        }
        
        public int getY()
        {
            return (y);
        }
        
        public int[] getSubset()
        {
            return (subset);
        }

        public boolean containsValue(int value)
        {
            for (int i = 0; i < subset.length; i++)
            {
                if (subset[i] == value)
                {
                    return (true);
                }
            }
            
            return (false);
        }
        
        /**
         * Returns true if this subset contains any of the values
         * in the other subset
         * 
         * @param otherSubset The other subset
         * @return Whether this subset contains any of the values
         * int the other subset
         */
        public boolean containsOr(Subset otherSubset)
        {
            int[] targetSubset = otherSubset.getSubset();
            
            for (int i = 0; i < targetSubset.length; i++)
            {
                for (int j = 0; j < subset.length; j++)
                {
                    if (targetSubset[i] == subset[j])
                    {
                        return (true);
                    }
                }
            }
            
            return (false);
        }
        
        /**
         * Returns true if this subset contains all of the values
         * in the other subset
         *  
         * @param otherSubset The other subset
         * @return Whether this subset contains all of the values
         * in the other subset
         */
        public boolean containsAnd(Subset otherSubset)
        {
            if (this.equals(otherSubset))
            {
                return (true);
            }
            
            int[] targetSubset = otherSubset.getSubset();
            
            if (targetSubset.length > subset.length)
            {
                return (false);
            }
            
            for (int i = 0; i < targetSubset.length; i++)
            {
                boolean foundTarget = false;
                
                for (int j = 0; j < subset.length; j++)
                {
                    if (targetSubset[i] == subset[j])
                    {
                        foundTarget = true;
                    }
                }
                
                if (!foundTarget)
                {
                    return (false);
                }
            }
            
            return (true);
        }
        
        public String toString()
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Coords - ");
            buffer.append(x);
            buffer.append(',');
            buffer.append(y);
            buffer.append(" - Values - ");
            
            for (int i = 0; i < subset.length; i++)
            {
                buffer.append(subset[i]);
                
                if (i < (subset.length - 1))
                {
                    buffer.append(',');
                }
            }
            
            return (buffer.toString());
        }
        
        public boolean equals(Object object)
        {
            if ((object == null) || !(object instanceof Subset))
            {
                return (false);
            }

            return (compareArrays(subset, ((Subset)object).getSubset()));
        }
    }
}
