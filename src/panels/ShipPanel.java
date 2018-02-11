package panels;

import javax.swing.text.rtf.RTFEditorKit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import com.github.lalyos.jfiglet.FigletFont;


public class ShipPanel extends TerminalPanel {

    private String nomVaisseau;
    private String nomEquipage;

    public ShipPanel(String nomEquipage, String nomVaisseau) {
        super();
        this.nomEquipage = nomEquipage;
        this.nomVaisseau = nomVaisseau;
        writeHeader();
    }

    //Code pour loader un fichier rtf
    public void loadFile() {

        String page = "test.rtf";

        mainPane.setEditorKit(new RTFEditorKit());
        try {
            mainPane.read(new FileInputStream(page), mainPane.getDocument());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Code pour loader un fichier HTML
    public void loadHTML() {

        File file = new File("test.html");

        try {
            mainPane.setPage(file.toURI().toURL());
            //TODO Set page sur un nouveau JTextPane
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeHelp() {
        System.out.println(
                "\nCOMMANDES DISPONIBLES :\n" +
                "- [1] Journal de bord\n" +
                "- [2] Consulter équipement du vaisseau\n" +
                "- [3] Modifier équipement du vaisseau\n" +
                "- [4] Accèder à l'Holonet\n" +
                "- [5] Demande Authorisation de décollage\n\n");
        super.writeHelp();
    }

    @Override
    public void writeHeader() {
        String asciiArt = null;
        String nomVaisseauArt = null;
        try {
            asciiArt = FigletFont.convertOneLine("Panneau de contrôle");
            nomVaisseauArt = FigletFont.convertOneLine(nomVaisseau);
            //TODO Polices figlet custom impossibles
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(asciiArt+"\n"+nomVaisseauArt);
        System.out.println("################################################################################");
        writeHelp();
    }

    public void showLog() {

    }

    //Define new commands for the main menu
    @Override
    public void parseCommand(String s) {
        commandPane.setText("");
        switch (s) {
            case "1" : showLog();
                break;
            case "2" :
                break;
            case "3" :
                break;
            case "4" :
                break;
            case "5" :
                break;
            default: super.parseCommand(s);
        }
    }
}
