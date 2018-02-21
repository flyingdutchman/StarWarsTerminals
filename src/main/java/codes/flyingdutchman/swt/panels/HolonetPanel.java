package codes.flyingdutchman.swt.panels;

import codes.flyingdutchman.swt.TerminalManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class HolonetPanel extends TerminalPanel{

    private final File root;
    private File[] previousDir;
    private File[] currentDir;

    HolonetPanel(File root) {

        this.root = root;

        currentDir = root.listFiles();

        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                //At each type, update view (delete and redo the calculus)
                updateView(commandPane.getText());
                //Afficher les résultats avec des chiffres
                //Detect enter

                //If escape, exit
                if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.err.println("reset");
                    TerminalManager.resetTerminal();
                }
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        };

        commandPane.addKeyListener(kl);

        updateView("");
    }

    private void updateView(String s) {
        String toPrint = "";
        for(int i = 1; i <= currentDir.length; i++) {
            String line = "["+i+"] "+ currentDir[i-1]+"\n";
            toPrint += line;
        }
    }



    @Override
    public void writeHeader() {
    }

}
