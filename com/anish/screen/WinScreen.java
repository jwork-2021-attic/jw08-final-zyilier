package com.anish.screen;

import asciiPanel.AsciiPanel;

public class WinScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("Won! Press enter to play again!", 0, 0);
        terminal.write("Press 'L' to load map", 0, 2);
    }

    @Override
    public void load(int[][] m, int[] h) {
        // TODO Auto-generated method stub
        
    }

}