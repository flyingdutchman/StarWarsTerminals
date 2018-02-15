package frames;

import javax.swing.*;
import java.awt.*;

public class FullscreenFrame extends JFrame{

    public FullscreenFrame() {
        super("");

        // Make it fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}
