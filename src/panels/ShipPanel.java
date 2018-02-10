package panels;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import com.github.lalyos.jfiglet.FigletFont;


public class ShipPanel extends TerminalPanel{

    String nomVaisseau;
    String nomEquipage;
    String nomCapitaine;

    public ShipPanel(String nomEquipage, String nomVaisseau, String nomCapitaine) {
        super();
        this.nomEquipage = nomEquipage;
        this.nomVaisseau = nomVaisseau;
        this.nomCapitaine = nomCapitaine;
        init();
    }

    public void init() {
        String asciiArt = FigletFont.convertOneLine("Panneau de contrôle");
        String nomVaisseauArt = FigletFont.convertOneLine(nomVaisseau);
        //TODO Màj le JFiglet de 0.0.3 à 0.0.8 (pour changer de police)
        System.out.println(asciiArt+"\n"+nomVaisseauArt);
        System.out.println("################################################################################");
        System.out.println("COMMANDES DISPONIBLES :\n" +
                "- [1] Journal de bord\n" +
                "- [2] Consulter équipement du vaisseau\n" +
                "- [3] Modifier équipement du vaisseau\n" +
                "- [4] Accèder à l'Holonet\n" +
                "- [5] Demande Authorisation de décollage\n\n");

    }

    public void loadFile() {

        String page = "test.rtf";

        mainPane.setEditorKit(new RTFEditorKit());
        try {
            mainPane.read(new FileInputStream(page), mainPane.getDocument());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHTML() {

        File file = new File("test.html");

        try {
            mainPane.setPage(file.toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Define new commands for the main menu
    @Override
    public void parseCommand(String s) {
        commandPane.setText("");
        switch (s) {
            case "test" :
                System.out.println("This is my first command");
                break;
            case "load rtf" : loadFile();
                break;
            case "load html" : loadHTML();
                break;
            default: super.parseCommand(s);
        }
    }
}
