package com.anish.screen;

import asciiPanel.AsciiPanel;

public class StartScreen extends RestartScreen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("start!.", 0, 0);
        terminal.write("Press ENTER to  start the game!", 0, 1);
        terminal.write("Press 'L' to load map", 0, 2);
    }

    @Override
    public void load(int[][] m, int[] h) {
        // TODO Auto-generated method stub
        
    }

}