package panels;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Scanner;
import com.github.lalyos.jfiglet.FigletFont;
import de.vandermeer.asciitable.*;
import tools.Crypto;


public class ShipPanel extends TerminalPanel {

    private final String LOG_FILE_NAME = "logs.html.encrypted";
    private final String EQUIPEMENT_FILE_NAME = "equipement.csv.encrypted";
    private final String TEMP_PATH = "data/temp/temp";
    private final String SHIP_TERMINAL_PATH = "data/terminal_vaisseau";

    private String nomVaisseau;
    private String nomEquipage;
    private String codeEquipage;

    public ShipPanel(String codeEquipage) {
        super();

        this.codeEquipage = codeEquipage;

        File encryptedCsv = new File(SHIP_TERMINAL_PATH +"/spaceship_list.csv.encrypted");
        File temp = new File(TEMP_PATH);
        Crypto.fileProcessor(Cipher.DECRYPT_MODE, encryptedCsv, temp);

        Scanner scanner;
        try {
            scanner = new Scanner(temp);
            scanner.useDelimiter(",");
            while(scanner.hasNextLine()){
                String next = scanner.nextLine();
                String[] s = next.split(",");
                if(s[0].equals(codeEquipage))  {
                    nomEquipage = s[1];
                    nomVaisseau = s[2];
                    break;
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
            asciiArt = FigletFont.convertOneLine(new File("flf/small.flf"), "Panneau de contrôle");
            //Retirer les accents et caractères spéciaux
            nomVaisseauArt = Normalizer.normalize(nomVaisseau, Normalizer.Form.NFD);
            nomVaisseauArt = nomVaisseauArt.replaceAll("\\p{M}", "");
            nomVaisseauArt = FigletFont.convertOneLine(new File("flf/cybersmall.flf"), nomVaisseauArt);
            //TODO Polices figlet custom impossibles
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(asciiArt+"\n"+nomVaisseauArt);
        System.out.println("################################################################################");
        writeHelp();
    }

    //Define new commands for the main menu
    @Override
    public void parseCommand(String s) {
        clearCommand();
        switch (s) {
            case "1" : showLog();
                break;
            case "2" : showShipEquipment();
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

    private void showLog() {

        // Etape 1 Trouver le fichier
        File toDecipher = new File(SHIP_TERMINAL_PATH+"/vaisseaux/"+codeEquipage+"/"+LOG_FILE_NAME);

        // Etape 2 le decipher
        File temp = new File(TEMP_PATH);
        Crypto.fileProcessor(Cipher.DECRYPT_MODE, toDecipher, temp);

        // Etape 3 l'afficher dans DisplayPane
        setTempDisplay(temp);
    }

    private void showShipEquipment() {

        // Etape 1 Trouver le fichier
        File toDecipher = new File(SHIP_TERMINAL_PATH+"/vaisseaux/"+codeEquipage+"/"+EQUIPEMENT_FILE_NAME);

        // Etape 2 le decipher
        File temp = new File(TEMP_PATH);
        Crypto.fileProcessor(Cipher.DECRYPT_MODE, toDecipher, temp);

        //Afficher dans console
        /*AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("row 1 col 1", "row 1 col 2");
        at.addRule();
        at.addRow("row 2 col 1", "row 2 col 2");
        at.addRule();
        System.out.println(at.render());*/
    }
}
