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

import java.util.Enumeration;
import java.util.Vector;

import cz.romario.opensudoku.logic.PossibleValues;
import cz.romario.opensudoku.logic.Strategy;

public class GoldenChain extends Strategy
{
    public int getDifficulty()
    {
        return (5);
    }
    
    public boolean applyStrategy(PossibleValues possibleValues, int[][] gameData)
    {   
        int x = 0;
        int y = 1;
        
        //for (int x = 0; x < 9; x++)
        //{
            //for (int y = 0; y < 9; y++)
            //{
                int[] cellPossibles = possibleValues.getPossibleValues(x, y);
                
                if ((cellPossibles != null) && (cellPossibles.length == 2))
                {
                    Chain chain = new Chain();
                }
            //}
        //}
        
        return (false);
    }
    
    private boolean buildChain(PossibleValues possibleValues, Chain chain, int[] cellPossibles, int x, int y)
    {   
        // Look for a link in the current block
        for (int i = (x / 3); i < ((x / 3) + 3); i++)
        {
            for (int j = (y / 3); j < ((y / 3) + 3); j++)
            {
                if ((i != x) && (j != y))
                {
                    int[] foundPossibles = possibleValues.getPossibleValues(i, j);
                }
            }
        }
        
        // Look for a link in the current row
        for (int i = 0; i < 9; i++)
        {
            if (i != x)
            {
                int[] foundPossibles = possibleValues.getPossibleValues(i, y);
            }
        }
        
        // Look for a link in the current column
        for (int i = 0; i < 9; i++)
        {
            if (i != y)
            {
                int[] foundPossibles = possibleValues.getPossibleValues(x, i);
            }
        }
        
        return (false);
    }
    
    private class Chain
    {
        private Vector linkVector;
        
        public Chain()
        {
            linkVector = new Vector();
        }
        
        public boolean isLinkValid(Link link)
        {
            int cellCount = 0;
            
            for (Enumeration enumeration = linkVector.elements(); enumeration.hasMoreElements();)
            {
                Link nextLink = (Link)enumeration.nextElement();
                
                if (((link.getCell1X() == nextLink.getCell1X()) && (link.getCell1Y() == nextLink.getCell1Y())) ||
                    ((link.getCell2X() == nextLink.getCell2X()) && (link.getCell2Y() == nextLink.getCell2Y())))
                {
                    cellCount++;
                    
                    if (cellCount > 2)
                    {
                        return (false);
                    }
                }
            }
            
            return (true);
        }
        
        public void addLink(Link link)
        {
            linkVector.addElement(link);
        }
        
        public int getEndPoint1()
        {
            if (linkVector.size() > 1)
            {
                return (((Link)linkVector.elementAt(0)).getEndPoint1());
            }
            
            return (-1);
        }
        
        public int getEndPoint2()
        {
            if (linkVector.size() > 1)
            {
                return (((Link)linkVector.elementAt(linkVector.size() - 1)).getEndPoint2());
            }
            
            return (-1);
        }
        
        public Link getLastLink()
        {
            if (linkVector.size() > 1)
            {
                return ((Link)linkVector.elementAt(linkVector.size() - 1));
            }
            
            return (null);
        }
    }
    
    private class Link
    {
        private int endPoint1;
        private int cell1X;
        private int cell1Y;
        private int bridge;
        private int endPoint2;
        private int cell2X;
        private int cell2Y;
        
        public Link(int endPoint1, int cell1X, int cell1Y, int bridge, 
            int endPoint2, int cell2X, int cell2Y)
        {
            this.endPoint1 = endPoint1;
            this.cell1X = cell1X;
            this.cell1Y = cell1Y;
            this.bridge = bridge;
            this.endPoint2 = endPoint2;
            this.cell2X = cell2X;
            this.cell2Y = cell2Y;
        }
        
        public int getEndPoint1()
        {
            return (endPoint1);
        }
        
        public int getCell1X()
        {
            return (cell1X);
        }
        
        public int getCell1Y()
        {
            return (cell1Y);
        }
        
        public int getBridge()
        {
            return (bridge);
        }
        
        public int getEndPoint2()
        {
            return (endPoint2);
        }
        
        public int getCell2X()
        {
            return (cell2X);
        }
        
        public int getCell2Y()
        {
            return (cell2Y);
        }
    }
}
