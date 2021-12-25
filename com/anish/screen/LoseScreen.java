package com.anish.screen;

import asciiPanel.AsciiPanel;

public class LoseScreen extends RestartScreen {
    
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("Lose!",0,0) ;
        terminal.write("Press enter to try again!", 0, 1);
        terminal.write("Press 'L' to load map", 0, 2);
        
    }
}
