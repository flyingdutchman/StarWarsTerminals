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
                        //TODO Effacer les paramètres d'entrées et lire directement le CSV
                        if(args.length != 2) {
                            System.err.println("Veuillez entrer le code de l'équipage");
                            return;
                        }
                        panel = new ShipPanel(args[1]);
                        break;
                    case "init" : new DataInit();
                        return;
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
