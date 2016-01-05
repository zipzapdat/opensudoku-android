OpenSudoku is a simple open source sudoku game. It's designed to be controlled both by finger and keyboard. It's preloaded with 90 puzzles in 3 difficulty levels, more puzzles can be downloaded from the web and it also allows you to enter your own puzzles.

You can install OpenSudoku via Android Market.

More puzzles for OpenSudoku can be found in new [Puzzles](Puzzles.md) page.

You can [enter bug reports here](http://code.google.com/p/opensudoku-android/issues/entry).

Unfortunately,  I don't have time to actively maintain this project anymore, so **only bug-reports are accepted**.  Sorry about that.

### What's new ###

#### 1.1.1 ####
  * Android 2.2 - move application to SD card support.
  * Fixes few bugs reported via new Android 2.2 "Report" button.
  * Few visual tweaks for Nexus One.
  * "Fill in notes" menu item can now be enabled in game settings.

#### 1.1.0 ####
  * Italian translation (thanks to diego.pierotto)
  * Few visual tweaks (thicker lines, some changes in Paper themes)
  * New game helper - shows how many times number has been used (thanks to Todd Southen)
  * Fixed bugs
    * QVGA - Sceen is sometimes shifted down ([issue #82](https://code.google.com/p/opensudoku-android/issues/detail?id=#82))
    * Undo should be disabled for a completed puzzle ([issue #84](https://code.google.com/p/opensudoku-android/issues/detail?id=#84))
    * List of games does not update ([issue #73](https://code.google.com/p/opensudoku-android/issues/detail?id=#73))\
    * Other small fixes

#### 1.0.0 ####
  * Export to sdcard
  * Theme support
  * Fixes [issue #42](https://code.google.com/p/opensudoku-android/issues/detail?id=#42) - Disable completed values - Can't edit note when number button is disabled
  * Optional border around puzzle ([issue #7](https://code.google.com/p/opensudoku-android/issues/detail?id=#7))
  * Highlighting of cell and rows when touched by finger can be disabled ([issue #33](https://code.google.com/p/opensudoku-android/issues/detail?id=#33))
  * Swedish translation (thanks to possan2)
  * German translation (thanks to mhelff)

#### 0.7.0 ####
  * Support for Android 1.6
  * Support for QVGA screens
  * Solves [issue #11](https://code.google.com/p/opensudoku-android/issues/detail?id=#11) - Disable numbers when used 9-times
  * Solves [issue #16](https://code.google.com/p/opensudoku-android/issues/detail?id=#16) - Remember input mode between games

#### 0.6.3 ####
  * French translation (thanks to pierre.hanselmann)
  * Chinese translation (thanks to Xue Kai)
  * Czech translation
  * Fixes [issue #22](https://code.google.com/p/opensudoku-android/issues/detail?id=#22) - A Wrong number in last open field leads to a successful solved Sudoku

#### 0.6.2 ####
  * Ability to import new puzzles from web (.opensudoku and .sdm files).
  * Filtering by game state in the sudoku list view.

#### 0.6.1 ####
Solves issues:
  * #1 - Request that Numpad cell value mode not auto-move to the right
  * #2 - Push trackball in SN / Pop modes to enter value
  * #3 - Pencil marks for 1, 2, and 3 get cut off in landscape mode
  * #4 - Option to turn timer on/off
  * #6 - Back arrow on Sudoku creation screen cancels input

#### 0.6.0 ####
  * Several new input modes in addition to popup edit.
  * Support for landscape orientation.
  * It's running without any glitches on Cupcake.
  * Simple help system.

### Screenshots ###

|List of puzzle collections|List of collection's puzzles|
|:-------------------------|:---------------------------|
|![http://opensudoku-android.googlecode.com/svn/trunk/OpenSudoku/wiki/folder_list.png](http://opensudoku-android.googlecode.com/svn/trunk/OpenSudoku/wiki/folder_list.png)|![http://opensudoku-android.googlecode.com/svn/trunk/OpenSudoku/wiki/sudoku_list.png](http://opensudoku-android.googlecode.com/svn/trunk/OpenSudoku/wiki/sudoku_list.png)|

|Single number input mode|Finger-friendly number selection (Popup input mode)|
|:-----------------------|:--------------------------------------------------|
|![http://opensudoku-android.googlecode.com/svn/trunk/OpenSudoku/wiki/gameplay_sn.png](http://opensudoku-android.googlecode.com/svn/trunk/OpenSudoku/wiki/gameplay_sn.png)|![http://opensudoku-android.googlecode.com/svn/trunk/OpenSudoku/wiki/select_number.png](http://opensudoku-android.googlecode.com/svn/trunk/OpenSudoku/wiki/select_number.png)|