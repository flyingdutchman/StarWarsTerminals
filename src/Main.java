import frames.FullscreenFrame;
import panels.MainMenuPanel;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FullscreenFrame fullscreenFrame = new FullscreenFrame();
            fullscreenFrame.setLayout(new BorderLayout());
            MainMenuPanel mainMenuPanel = new MainMenuPanel();
            fullscreenFrame.add(mainMenuPanel, BorderLayout.CENTER);
        });
    }

}
