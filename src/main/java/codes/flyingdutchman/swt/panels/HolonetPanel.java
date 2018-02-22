package codes.flyingdutchman.swt.panels;

import codes.flyingdutchman.swt.TerminalManager;
import com.github.lalyos.jfiglet.FigletFont;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class HolonetPanel extends TerminalPanel{

    private final File root;
    private File[] previousDir;
    private File[] currentDir;
    private File currentFile;
    private ArrayList<File> shownList;
    private int cursor;

    HolonetPanel(File root) {

        this.root = root;

        currentDir = root.listFiles();
        currentFile = root;
        previousDir = currentDir[0].getParentFile().getParentFile().listFiles();
        shownList = new ArrayList<>();
        cursor = 0;

        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                //Detect CTRL+C
                if (keyEvent.isControlDown() && keyEvent.getKeyChar() != 'c' && keyEvent.getKeyCode() == 67) {
                    TerminalManager.resetTerminal();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //At each type, update view (delete and redo the calculus)
                //Afficher les résultats avec des chiffres
                //Detect enter

                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER || keyEvent.getKeyCode() == KeyEvent.VK_RIGHT)
                    pressEnter();

                if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                    pressReturn();
                }

                if(keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                    if(cursor > 0)
                        cursor -= 1;
                }

                if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                    if(cursor < shownList.size() -1)
                        cursor += 1;
                }

                updateView(commandPane.getText());
            }
        };

        commandPane.addKeyListener(kl);


        updateView("");
    }

    private void pressReturn() {
        if(Arrays.asList(previousDir).contains(root))
            return;
        currentFile = currentFile.getParentFile();
        currentDir = previousDir;
        previousDir = previousDir[0].getParentFile().getParentFile().listFiles();
        shownList.clear();
        cursor=0;
        clearCommand();
        updateView("");
    }

    private void pressEnter() {
        if(shownList.isEmpty())
            return;
        File first = shownList.get(cursor);
        if(first.isDirectory()) {
            previousDir = currentDir;
            currentDir = first.listFiles();
            currentFile = first;
        } else {
            setTempDisplay(createReadableTemp(first));
        }

        shownList.clear();
        cursor=0;
        clearCommand();
    }

    @Override
    public void parseCommand(String s) {

    }

    private void updateView(String s) {
        mainPane.setText("");
        shownList.clear();
        writeHeader();
        int cnt = 0;
        for(int i = 0; i < currentDir.length; i++) {
            File file = currentDir[i];
            if(file.getName().startsWith(s)) {
                String line = "";
                if(cnt == cursor) {
                    line += "> ";
                }
                if(file.isDirectory()) {
                    line += "[d] " + file.getName();
                    shownList.add(file);
                }
                else {
                    line += "[f] "+ file.getName().substring(0,file.getName().indexOf("."));
                    shownList.add(file);
                }

                cnt++;
                System.out.println(line);
            }

        }
    }



    @Override
    public void writeHeader() {
        String asciiArt = null;
        String text = "Bienvenue sur l'Holonet";
        try {
            asciiArt = FigletFont.convertOneLine(new File("flf/cybersmall.flf"), "Holonet");
            text = FigletFont.convertOneLine(new File("flf/small.flf"), text);
            //Retirer les accents et caractères spéciaux
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(asciiArt+"\n"+text+"\n");
        System.out.println("Écrivez les termes de votre recherches.");
        System.out.println("Appuyer sur enter pour ouvrir une catégorie ou ouvrir un fichier");
        System.out.println("Pour remonter l'arborescence appuyer sur la flèche haut");
        System.out.println("Pout quitter l'Holonet, appuyer sur CTRL+C");
        System.out.println("############################# RECHERCHE ################################\n\n");
        String currentPos;
        if(Arrays.asList(previousDir).contains(root)) {
            currentPos = "/";
        } else {
            currentPos = currentFile.getPath().substring(root.getPath().length());
        }
        System.out.println("Position actuelle : "+ currentPos);
    }

}
