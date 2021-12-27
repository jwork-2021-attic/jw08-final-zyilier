package com.anish.screen;

import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;

public abstract class RestartScreen implements Screen {

    @Override
    public abstract void displayOutput(AsciiPanel terminal);

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                return new WorldScreen(0);
            
            case 76:
                return new WorldScreen(1);
            default:
                return this;
        }
    }

}
