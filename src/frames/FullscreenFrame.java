package frames;

import javax.swing.*;
import java.awt.*;

public class FullscreenFrame extends JFrame{

    public FullscreenFrame() {
        super("");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Make it fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setSize(new Dimension(200,200));
        setAlwaysOnTop(true);
        setUndecorated(true);
    }

}
