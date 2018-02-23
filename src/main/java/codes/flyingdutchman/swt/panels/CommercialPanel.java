package codes.flyingdutchman.swt.panels;

import codes.flyingdutchman.swt.TerminalManager;
import codes.flyingdutchman.swt.tools.NFCTools;
import com.github.lalyos.jfiglet.FigletFont;
import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthAbsoluteEven;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;
import org.json.JSONObject;

import javax.smartcardio.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class CommercialPanel extends TerminalPanel {

    public CommercialPanel() {
        writeHeader();

    }

    //Define new commands for the main menu
    @Override
    public void parseCommand(String s) {
        clearCommand();
        switch (s) {
            case "1" :
                consulting();
                break;
            case "2" :
                trade();
                break;
            default: super.parseCommand(s);
        }
    }

    private void consulting() {

        System.out.println("Consultation des avoirs :");

        JSONObject jsonObject = null;

        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            System.err.println(terminals.toString());


            CardTerminal terminal = terminals.get(0);

            // Connect wit the card
            Card card = terminal.connect("*");
            CardChannel channel = card.getBasicChannel();

            // Read Card
            String s = NFCTools.read(channel);
            s = s.substring(s.indexOf("{"), s.lastIndexOf("}")+1);
            jsonObject = new JSONObject(s);

            // Disconnect the card
            card.disconnect(false);

        } catch(Exception e) {

            System.out.println("Ouch: " + e.toString());

        }

        V2_AsciiTable at = new V2_AsciiTable();
        at.addRule();

        for(String key : jsonObject.keySet()) {
            at.addRow(key, jsonObject.get(key).toString());
            at.addRule();
        }

        V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
        rend.setTheme(V2_E_TableThemes.UTF_LIGHT.get());
        rend.setWidth(new WidthAbsoluteEven(76));
        RenderedTable rt = rend.render(at);
        System.out.println(rt);

    }


    private void trade() {

        System.out.println("New Trade\n");

        System.out.println("Please place the two cards on the lectors\n\n");

        JSONObject json = new JSONObject();
        json.put("Tibor", 42);

        try {
            // Display the list of terminals
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();

            // Use the first terminal
            CardTerminal terminalA = terminals.get(0);
            CardTerminal terminalB = terminals.get(1);

            // Connect with the card A
            Card cardA = terminalA.connect("*");
            System.out.println("Card: " + cardA);
            CardChannel channelA = cardA.getBasicChannel();

            // Connect with the card B
            Card cardB = terminalA.connect("*");
            System.out.println("Card: " + cardA);
            CardChannel channelB = cardA.getBasicChannel();

            NFCTools.write(channelB, json.toString());

            // Disconnect the card
            cardA.disconnect(false);

        } catch(Exception e) {

            System.out.println(e.getMessage());
            System.out.println("Please retry");

        }
    }

    @Override
    public void writeHelp() {
        System.out.println(
                "\nCOMMANDES DISPONIBLES :\n" +
                        "- [1] Consulter possessions\n" +
                        "- [2] Faire un échange\n\n");
        super.writeHelp();
    }

    @Override
    public void writeHeader() {
        String asciiArt = null;
        try {
            asciiArt = FigletFont.convertOneLine("Plateforme")+"\n";
            asciiArt += FigletFont.convertOneLine("Commerciale");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(asciiArt);
        System.out.println("Bienvenus sur la platforme d'échange commerciale !");
        System.out.println("Que souhaitez-vous faire ?\n");
        System.out.println("/!\\ Veuillez poser les cartes sur les lecteurs avant toute opération /!\\\n");
        System.out.println("################################################################################\n");
        writeHelp();
    }
}
