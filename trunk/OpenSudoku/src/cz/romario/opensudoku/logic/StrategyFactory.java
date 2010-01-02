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

import java.util.Enumeration;
import java.util.Vector;

import cz.romario.opensudoku.logic.strategies.*;

/**
 *
 * @author Matt Parker
 */
public class StrategyFactory
{
    private static Vector strategies;
    
    static
    {
        strategies = new Vector();
        strategies.addElement(new NakedSingle(false));
        strategies.addElement(new HiddenSingle());
        strategies.addElement(new NakedSubset());
        strategies.addElement(new BlockAndColumnRowInteractions());
        strategies.addElement(new BlockBlockInteractions());
        //strategies.addElement(new HiddenSubset());
        strategies.addElement(new Swordfish());
        strategies.addElement(new Nishio());
        //strategies.addElement(new GoldenChain());
    }
    
    private StrategyFactory() {}
    
    public static Enumeration getStrategies()
    {
        return (strategies.elements());
    }
}
