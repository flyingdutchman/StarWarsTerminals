import frames.FullscreenFrame;
import panels.ShipPanel;
import panels.TerminalPanel;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FullscreenFrame fullscreenFrame = new FullscreenFrame();
            fullscreenFrame.setLayout(new BorderLayout());
            TerminalPanel panel;

            if(args.length != 0) {
                switch(args[0]) {
                    case "vaisseau":
                        if(args.length != 4) {
                            System.err.println("Veuillez entrer le nom de l'équipage, le nom du vaisseau et le nom du capitaine");
                            return;
                        }
                        panel = new ShipPanel(args[1], args[2], args[3]);
                        break;
                    default:
                        System.err.println("Veuillez préciser un paramètre valide");
                        return;
                }

                fullscreenFrame.setVisible(true);
                fullscreenFrame.add(panel, BorderLayout.CENTER);

            } else {
                System.err.println("Veuillez préciser un paramètre");
                return;
            }
        });
    }

}
