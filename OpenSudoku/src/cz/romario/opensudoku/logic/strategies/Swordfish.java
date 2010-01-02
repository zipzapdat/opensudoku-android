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

public class Swordfish extends Strategy
{
    public int getDifficulty()
    {
        return (4);
    }
    
    public boolean applyStrategy(PossibleValues possibleValues, int[][] gameData)
    {
        for (int n = 1; n <= 9; n++)
        {
            for (int noOfCandidates = 2; noOfCandidates <= 3; noOfCandidates++)
            {
                // Check number of columns that have <noOfCandidates> of <n>
                Vector<Vector<Candidate>> columnCandidates = new Vector<Vector<Candidate>>();
                
                for (int x = 0; x < 9; x++)
                {
                    Vector<Candidate> candidates = new Vector<Candidate>();
                    
                    for (int y = 0; y < 9; y++)
                    {
                        if (possibleValues.isValuePossible(x, y, n))
                        {
                            candidates.addElement(new Candidate(x, y, n));
                        }
                    }
                    
                    if (candidates.size() != noOfCandidates)
                    {
                        continue;
                    }
                    
                    columnCandidates.addElement(candidates);
                }
                
                if (columnCandidates.size() == 3)
                {
                    // Check that candidates are on 3 common rows
                    Vector<Integer> commonRows = new Vector<Integer>();
                    
                    for (int i = 0; i < columnCandidates.size(); i++)
                    {
                        Vector<Candidate> vector = (Vector<Candidate>)columnCandidates.elementAt(i);
                        
                        for (int j = 0; j < vector.size(); j++)
                        {
                            Integer nextRow = new Integer(((Candidate)vector.elementAt(j)).getY());

                            if (!commonRows.contains(nextRow))
                            {
                                commonRows.addElement(nextRow);
                            }
                        }
                    }
                    
                    if (commonRows.size() == 3)
                    {
                        boolean updated = false;
                        
                        for (int i = 0; i < commonRows.size(); i++)
                        {
                            int row = ((Integer)commonRows.elementAt(i)).intValue();
                            
                            // Get the 3 protected columns for the candidates in row <row>
                            Vector<Integer> protectedColumns = new Vector<Integer>();
                            
                            for (int k = 0; k < columnCandidates.size(); k++)
                            {
                                Vector<Candidate> vector = columnCandidates.elementAt(k);
                                
                                for (int l = 0; l < vector.size(); l++)
                                {
                                    Candidate nextCandidate = (Candidate)vector.elementAt(l);
                                    
                                    if (nextCandidate.getY() == row)
                                    {
                                        protectedColumns.addElement(new Integer(nextCandidate.getX()));
                                    }
                                }
                            }
                            
                            for (int x = 0; x < 9; x++)
                            {
                                if (possibleValues.isValuePossible(x, row, n) &&
                                    !protectedColumns.contains(new Integer(x)))
                                {
                                    updated = true;
                                    possibleValues.removePossibleValue(x, row, n);
                                }
                            }
                        }
                        
                        if (updated)
                        {
                            return (true);
                        }
                    }
                }
                
                // Check number of rows that have <noOfCandidates> of <n>
                Vector<Vector<Candidate>> rowCandidates = new Vector<Vector<Candidate>>();
                
                for (int y = 0; y < 9; y++)
                {
                    Vector<Candidate> candidates = new Vector<Candidate>();
                    
                    for (int x = 0; x < 9; x++)
                    {
                        if (possibleValues.isValuePossible(x, y, n))
                        {
                            candidates.addElement(new Candidate(x, y, n));
                        }
                    }
                    
                    if (candidates.size() != noOfCandidates)
                    {
                        continue;
                    }
                    
                    rowCandidates.addElement(candidates);
                }
                
                if (rowCandidates.size() == 3)
                {
                    // Check that candidates are on 3 common columns
                    Vector<Integer> commonColumns = new Vector<Integer>();
                    
                    for (int i = 0; i < rowCandidates.size(); i++)
                    {
                        Vector<Candidate> vector = rowCandidates.elementAt(i);
                        
                        for (int j = 0; j < vector.size(); j++)
                        {
                            Integer nextColumn = new Integer(((Candidate)vector.elementAt(j)).getX());
                            
                            if (!commonColumns.contains(nextColumn))
                            {
                                commonColumns.addElement(nextColumn);
                            }
                        }
                    }
                    
                    if (commonColumns.size() == 3)
                    {
                        boolean updated = false;
                        
                        for (int i = 0; i < commonColumns.size(); i++)
                        {
                            int column = ((Integer)commonColumns.elementAt(i)).intValue();
                            
                            // Get the 3 protected rows for the candidates in column <column>
                            Vector<Integer> protectedRows = new Vector<Integer>();
                            
                            for (int k = 0; k < rowCandidates.size(); k++)
                            {
                                Vector<Candidate> vector = rowCandidates.elementAt(k);
                                
                                for (int l = 0; l < vector.size(); l++)
                                {
                                    Candidate nextCandidate = (Candidate)vector.elementAt(l);
                                    
                                    if (nextCandidate.getX() == column)
                                    {
                                        protectedRows.addElement(new Integer(nextCandidate.getY()));
                                    }
                                }
                            }
                            
                            for (int y = 0; y < 9; y++)
                            {
                                if (possibleValues.isValuePossible(column, y, n) &&
                                    !protectedRows.contains(new Integer(y)))
                                {
                                    updated = true;
                                    possibleValues.removePossibleValue(column, y, n);
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
