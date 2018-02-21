package codes.flyingdutchman.swt;

import codes.flyingdutchman.swt.frames.FullscreenFrame;
import codes.flyingdutchman.swt.panels.ShipPanel;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FullscreenFrame fullscreenFrame = new FullscreenFrame();
            fullscreenFrame.setLayout(new BorderLayout());

            if(args.length != 0) {
                switch(args[0]) {
                    case "vaisseau":
                        //TODO Effacer les paramètres d'entrées et lire directement le CSV
                        if(args.length != 2) {
                            System.err.println("Veuillez entrer le code de l'équipage");
                            return;
                        }
                        TerminalManager.getInstance(fullscreenFrame, new ShipPanel(args[1]));
                        break;
                    case "init" : new DataInit();
                        return;
                    default:
                        System.err.println("Veuillez préciser un paramètre valide");
                        return;
                }

                fullscreenFrame.setVisible(true);

            } else {
                System.err.println("Veuillez préciser un paramètre");
                return;
            }
        });
    }

}
