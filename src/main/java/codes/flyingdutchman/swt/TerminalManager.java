package codes.flyingdutchman.swt;

import codes.flyingdutchman.swt.panels.TerminalPanel;

import javax.swing.*;
import java.awt.*;

public class TerminalManager {

    private static TerminalManager instance = null;
    private static JFrame frame;
    private static TerminalPanel rootPanel;

    private TerminalManager(JFrame frame, TerminalPanel rootPanel) {
        frame.add(new JPanel(), BorderLayout.CENTER);
        TerminalManager.frame = frame;
        TerminalManager.rootPanel = rootPanel;
        setPanel(rootPanel);
    }
    public static TerminalManager getInstance(JFrame frame, TerminalPanel rootPanel) {
        if(instance == null) {
            instance = new TerminalManager(frame, rootPanel);
        }
        return instance;
    }

    public static void setPanel(TerminalPanel tp) {
        //SwingUtilities.invokeLater(() -> {
            frame.setContentPane(tp);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
        //});
    }

    public static void resetTerminal() {
        setPanel(rootPanel);
    }

}
