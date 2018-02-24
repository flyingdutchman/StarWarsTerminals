package codes.flyingdutchman.swt.tools;

import org.json.JSONObject;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import java.util.List;
import java.util.Scanner;

/**
 * A small class to quickly write on all cards
 */
public class writeCards {

    public static void main(String[] args) {
        System.out.println("Place card on Scanner");

        try {
            // Display the list of terminals
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();

            // Use the first terminal
            CardTerminal terminalA = terminals.get(0);
            CardTerminal terminalB = terminals.get(1);

            // Connect with the card A
            System.out.print("Card Slot 1: ");
            Card card = terminalA.connect("*");
            System.out.println(card); //Je laisse le print pour le RP
            CardChannel channelA = card.getBasicChannel();

            JSONObject json = new JSONObject();

            String s;
            while(true) {
                Scanner keyboard = new Scanner(System.in);
                System.out.println("Enter next key/value seperated with a space");
                s = keyboard.nextLine();
                if(s.equals("stop"))
                    break;
                String[] split = s.split(" ");
                json.put(split[0], split[1]);
            }

            System.out.println(json.toString());
            NFCTools.write(channelA, json.toString());

            // Disconnect the card
            card.disconnect(false);

        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("\n\nUne erreur a été détectée, veuiller recommencer votre opération");
        }

    }

}
