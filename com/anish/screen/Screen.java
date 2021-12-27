package com.anish.screen;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;

public interface Screen {

    public void displayOutput(AsciiPanel terminal);

    public void load(int[][]m,int[]h);

    public Screen respondToUserInput(KeyEvent key);
}
