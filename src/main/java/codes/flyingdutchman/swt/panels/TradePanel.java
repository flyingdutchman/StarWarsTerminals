package codes.flyingdutchman.swt.panels;

import codes.flyingdutchman.swt.tools.NFCTools;
import org.json.JSONObject;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TradePanel extends TerminalPanel {

    private final List<String> holder = new ArrayList<>();

    public TradePanel() {


        ActionListener al = actionEvent -> {
            synchronized (holder) {
                holder.add(commandPane.getText());
                holder.notify();
            }
        };


        commandPane.addActionListener(al);


        System.out.println("********** New Trade **********\n");

        System.out.println("Please place the two cards on the lectors\n\n");

        try {
            // Display the list of terminals
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();

            // Use the first terminal
            CardTerminal terminalA = terminals.get(0);
            CardTerminal terminalB = terminals.get(1);

            // Connect with the card A
            System.out.print("Card Slot 1: ");
            Card cardA = terminalA.connect("*");
            System.out.println(cardA); //Je laisse le print pour le RP
            CardChannel channelA = cardA.getBasicChannel();

            // Connect with the card B
            System.out.print("Card Slot 2: ");
            Card cardB = terminalB.connect("*");
            System.out.println(cardB);
            CardChannel channelB = cardB.getBasicChannel();

            String s = NFCTools.read(channelA);
            s = s.substring(s.indexOf("{"), s.lastIndexOf("}")+1);
            JSONObject jsonA = new JSONObject(s);

            s = NFCTools.read(channelB);
            s = s.substring(s.indexOf("{"), s.lastIndexOf("}")+1);
            JSONObject jsonB = new JSONObject(s);

            System.err.println("Yo");

            String input;
            //Wait for user input
            synchronized (holder) {

                // wait for input from field
                while (holder.isEmpty())
                    holder.wait();

                input = holder.remove(0);
            }


            System.out.println(input);

            //NFCTools.write(channelB, json.toString());

            // Disconnect the card
            cardA.disconnect(false);

        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("\n\nUne erreur a été détectée, veuiller recommencer votre opération");
        }

        System.out.println("\n\n********** End Trade **********\n\n");

        commandPane.removeActionListener(al);
    }

    @Override
    public void writeHeader() {
        System.out.println("TRADE INTERFACE");
    }
}
