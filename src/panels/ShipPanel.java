package panels;

import javax.crypto.Cipher;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import com.github.lalyos.jfiglet.FigletFont;
import tools.Crypto;


public class ShipPanel extends TerminalPanel {

    private final String LOG_FILE_NAME = "logs.html.encrypted";

    private String nomVaisseau;
    private String nomEquipage;
    private String codeEquipage;

    public ShipPanel(String codeEquipage) {
        super();

        this.codeEquipage = codeEquipage;

        File encryptedCsv = new File("data/vaisseau/spaceship_list.csv.encrypted");
        File temp = new File("data/temp/temp");
        Crypto.fileProcessor(Cipher.DECRYPT_MODE, encryptedCsv, temp);

        Scanner scanner;
        try {
            scanner = new Scanner(temp);
            scanner.useDelimiter(",");
            while(scanner.hasNext()){
                String next = scanner.next();
                if(next.equals(codeEquipage))  {
                    nomEquipage = scanner.next();
                    nomVaisseau = scanner.next();
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Could not open crypted CSV");
            e.printStackTrace();
        }

        temp.delete();

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
        // Etape 1 Trouver le fichier

        File toDecipher = new File("data/vaisseau/"+nomVaisseau+"/"+LOG_FILE_NAME);

        // Etape 2 le decipher

        // Etape 3 l'afficher dans DisplayPane
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
