package codes.flyingdutchman.swt.frames;

import javax.swing.*;

public class FullscreenFrame extends JFrame{

    public FullscreenFrame() {
        super("");

        // Make it fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setAlwaysOnTop(true);
        setUndecorated(true);
        //TODO CODE POUR EMPECHER ALTF4
        //setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
