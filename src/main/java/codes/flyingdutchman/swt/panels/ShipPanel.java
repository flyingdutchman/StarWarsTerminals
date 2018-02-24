package codes.flyingdutchman.swt.panels;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Scanner;

import codes.flyingdutchman.swt.TerminalManager;
import codes.flyingdutchman.swt.tools.Crypto;
import com.github.lalyos.jfiglet.FigletFont;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthAbsoluteEven;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;
import de.vandermeer.asciitable.v2.*;

public class ShipPanel extends TerminalPanel {

    private final String LOG_FILE_NAME = "logs.html.encrypted";
    private final String EQUIPEMENT_FILE_NAME = "equipement.csv.encrypted";
    private final String SHIP_TERMINAL_PATH = "data/terminal_vaisseau";

    private String nomVaisseau;
    private String codeEquipage;

    public ShipPanel(String codeEquipage) {

        super();

        this.codeEquipage = codeEquipage;

        File tmpCSV = createReadableTemp(new File(SHIP_TERMINAL_PATH +"/spaceship_list.csv.encrypted"));

        Scanner scanner;
        try {
            scanner = new Scanner(tmpCSV);
            while(scanner.hasNextLine()){
                String next = scanner.nextLine();
                String[] s = next.split(",");
                if(s[0].equals(codeEquipage))  {
                    nomVaisseau = s[2];
                    break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Could not open crypted CSV");
            e.printStackTrace();
        }

        tmpCSV.delete();

        writeHeader();
    }

    @Override
    public void writeHelp() {
        System.out.println(
                "\nCOMMANDES DISPONIBLES :\n" +
                "- [1] Journal de bord\n" +
                "- [2] Accèder à l'Holonet\n\n");
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
            case "1" :
                showLog();
                break;
            case "2" :
                TerminalManager.setPanel(new HolonetPanel(new File(SHIP_TERMINAL_PATH+"/holonet")));
                break;
            case "ferailleur4life":
                break;
            default: super.parseCommand(s);
        }
    }

    private void showLog() {
        setTempDisplay(createReadableTemp(new File(SHIP_TERMINAL_PATH+"/vaisseaux/"+codeEquipage+"/"+LOG_FILE_NAME)));
    }

//    private void showShipEquipment() {
//
//        //Tester si accès authorisé
//        //TODO Faire mdp d'accès Mécano
//        //Capture CommandPane answer
//
//        // Etape 1 Trouver le fichier
//        File tmpEquip = createReadableTemp(new File(SHIP_TERMINAL_PATH+"/vaisseaux/"+codeEquipage+"/"+EQUIPEMENT_FILE_NAME));
//
//        //Afficher dans console
//        System.out.println("État de l'équipement de \""+nomVaisseau+"\"");
//        V2_AsciiTable at = new V2_AsciiTable();
//        at.addRule();
//
//        Scanner scanner;
//        try {
//            scanner = new Scanner(tmpEquip);
//            while(scanner.hasNextLine()) {
//                String next = scanner.nextLine();
//                if(!next.isEmpty()) {
//                    String[] s = next.split(",");
//                    at.addRow(s[0], s[1]);
//                    at.addRule();
//                }
//            }
//            scanner.close();
//        } catch (FileNotFoundException e) {
//            System.err.println("Could not open crypted CSV");
//            e.printStackTrace();
//        }
//
//        tmpEquip.delete();
//
//        V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
//        rend.setTheme(V2_E_TableThemes.UTF_LIGHT.get());
//        rend.setWidth(new WidthAbsoluteEven(76));
//        RenderedTable rt = rend.render(at);
//        System.out.println(rt);
//    }
}
