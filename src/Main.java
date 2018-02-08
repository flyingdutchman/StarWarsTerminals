import frames.FullscreenFrame;
import panels.CommercialPanel;
import panels.TerminalPanel;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FullscreenFrame fullscreenFrame = new FullscreenFrame();
            fullscreenFrame.setLayout(new BorderLayout());
            TerminalPanel panel = null;

            if(args.length != 0) {
                switch(args[0]) {
                    case "commercial": panel = new CommercialPanel();
                        break;
                }
                fullscreenFrame.setVisible(true);
            } else {
                System.err.println("Please enter argument");
                return;
            }
        });
    }

}
