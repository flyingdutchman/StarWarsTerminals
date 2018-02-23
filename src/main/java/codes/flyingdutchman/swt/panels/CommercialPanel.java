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

            // Send test command
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

        System.out.println("Please place the two cards on the lectors");

        try {
            // Display the list of terminals
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            System.out.println("Terminals: " + terminals);

            // Use the first terminal
            CardTerminal terminalA = terminals.get(0);
            CardTerminal terminalB = terminals.get(1);

            // Connect with the card
            Card card = terminalA.connect("*");
            System.out.println("Card: " + card);
            CardChannel channel = card.getBasicChannel();

            // Send test command
            ResponseAPDU response = channel.transmit(new CommandAPDU( new byte[] { (byte) 0xFF, (byte) 0xB0, (byte) 0x00, (byte) 0x05, (byte) 0x10   }));
            //ResponseAPDU response = channel.transmit(new CommandAPDU( new byte[] { (byte) 0xFF, (byte) 0xD6, (byte) 0x00, (byte) 0x05, (byte) 0x04, (byte) 0x42, (byte) 0x42, (byte) 0x42, (byte) 0x42   }));
            System.out.println("Response: " + response.toString());

            if (response.getSW1() == 0x63 && response.getSW2() == 0x00)  System.out.println("Failed");

            System.out.println("UID: " + bin2hex(response.getData()));

            // Disconnect the card
            card.disconnect(false);

        } catch(Exception e) {

            System.out.println("Ouch: " + e.toString());

        }
    }

    private String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
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
