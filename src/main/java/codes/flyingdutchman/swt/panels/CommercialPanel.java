package codes.flyingdutchman.swt.panels;

import codes.flyingdutchman.swt.tools.NFCTools;
import com.github.lalyos.jfiglet.FigletFont;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthAbsoluteEven;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;
import org.json.JSONObject;

import javax.smartcardio.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;

public class CommercialPanel extends TerminalPanel {

    //TODO Abandonner Terminal Panel et faire un truc tout con dans un vrai terminal (problèmes de threading)

    public enum State {
        D_WELCOME, P_WELCOME, CONSULTING, D_TRADE_J1, P_TRADE_J1, D_TRADE_J2, P_TRADE_J2, D_CONFIRMATION, P_CONFIRMATION, WRITE
    }

    private final String[] ressources = new String[] {"credits", "charbon", "minerai", "or", "uranium"};

    private State nextState;
    private JSONObject jsonA;
    private JSONObject jsonB;
    private Card cardA;
    private Card cardB;
    private int askedQuantityA;
    private int askedQuantityB;
    private String resourceNameA;
    private String resourceNameB;

    public CommercialPanel() {
        nextState = State.D_WELCOME;

        commandPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {

                    parseCommand(commandPane.getText());
                }
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        print_welcome();
    }

    private void reset() {
        nextState = State.D_WELCOME;
        jsonA = null;
        jsonB = null;
        cardA = null;
        cardB = null;
        askedQuantityA = 0;
        askedQuantityB = 0;
        resourceNameA = "";
        resourceNameB = "";
    }

    //Define new commands for the main menu
    public void parseCommand(String s) {
        clearCommand();

        if(s.equals("annuler")) {
            nextState = State.D_WELCOME;
            reset();
        }

        System.err.println(nextState);
        switch (nextState) {
            case D_WELCOME:
                print_welcome();
                break;
            case P_WELCOME:
                parse_welcome(s);
                break;
            case CONSULTING:
                print_consulting();
                break;
            case D_TRADE_J1:
                print_trade_J1();
                break;
            case P_TRADE_J1:
                parse_trade_J1(s);
                break;
            case D_TRADE_J2:
                print_trade_J2();
                break;
            case P_TRADE_J2:
                parse_trade_J2(s);
                break;
            case D_CONFIRMATION:
                print_confirmation();
                break;
            case P_CONFIRMATION:
                parse_confirmation(s);
                break;
        }
    }

    private void print_welcome() {
        mainPane.setText("");
        writeHeader();
        nextState = State.P_WELCOME;
    }

    private void parse_welcome(String s) {
        switch (s) {
            case "1" :
                nextState = State.CONSULTING;
                print_consulting();
                break;
            case "2" :
                nextState = State.D_TRADE_J1;
                print_trade_J1();
                break;
            default:
                nextState = State.D_WELCOME;
                print_welcome();
        }
    }

    private void print_consulting() {

        SwingUtilities.invokeLater(() -> {

            System.out.println("Consultation des avoirs :");

            JSONObject jsonObject = null;

            try {
                TerminalFactory factory = TerminalFactory.getDefault();
                System.out.print("Card Slot 1: ");
                List<CardTerminal> terminals = factory.terminals().list();
                System.out.println(terminals.toString());


                CardTerminal terminal = terminals.get(0);

                // Connect wit the card
                Card card = terminal.connect("*");
                CardChannel channel = card.getBasicChannel();

                // Read Card
                String s = NFCTools.read(channel);
                s = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
                jsonObject = new JSONObject(s);

                // Disconnect the card
                card.disconnect(false);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("\n\nUne erreur a été détectée, veuiller recommencer votre opération");
            }

            V2_AsciiTable at = new V2_AsciiTable();
            at.addRule();

            for (String key : jsonObject.keySet()) {
                at.addRow(key, jsonObject.get(key).toString());
                at.addRule();
            }

            V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
            rend.setTheme(V2_E_TableThemes.UTF_LIGHT.get());
            rend.setWidth(new WidthAbsoluteEven(76));
            RenderedTable rt = rend.render(at);
            System.out.println(rt);

            System.out.println("\n\n Appuyer sur la touche entrée pour continuer");
        });

        nextState = State.D_WELCOME;
    }




    private void print_trade_J1() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("********** Nouvel Échange **********\n");

            System.out.println("Veuillez placer vos cartes sur les lecteurs\n\n");

            System.out.println("Éléments échangeables :");

            for(String s : ressources) {
                System.out.print(s+" ");
            }

            System.out.println("\n\nClient n°1 veuillez décrire ce que vous souhaiter échanger sous le format :");
            System.out.println("\"item quantité\"");
        });

        nextState = State.P_TRADE_J1;
    }

    private void parse_trade_J1(String s) {

        readScanners();

        String[] input = s.split(" ");
        resourceNameA = input[0];

        boolean correct = false;
        for(String str : ressources) {
            if(resourceNameA.equals(str)) {
                correct = true;
                break;
            }
        }

        if(!correct) {
            SwingUtilities.invokeLater(() -> {
                System.out.println("Veuillez entrer un nom de ressource valide");
            });
            nextState = State.P_TRADE_J1;
            return;
        }

        try {
            askedQuantityA = Integer.parseInt(input[1]);
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                System.out.println("Veuillez entrer une quantité de ressource valide");
            });
            nextState = State.P_TRADE_J1;
            return;
        }
        int trueValue = jsonA.has(resourceNameA) ? jsonA.getInt(resourceNameA) :0;



        if(askedQuantityA > trueValue) {
            SwingUtilities.invokeLater(() -> {
                System.out.println("Vous n'avez pas assez pas de "+resourceNameA);
            });
            nextState = State.P_TRADE_J1;
            return;
        }

        //NFCTools.write(channelB, json.toString());

        nextState = State.D_TRADE_J2;
        print_trade_J2();
    }

    private void print_trade_J2() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("\n\nClient n°2 veuillez décrire ce que vous souhaiter échanger sous le format :");
            System.out.println("\"item quantité\"");
        });
        nextState = State.P_TRADE_J2;
    }

    private void parse_trade_J2(String s) {

        readScanners();

        String[] input = s.split(" ");
        resourceNameB = input[0];

        boolean correct = false;
        for(String str : ressources) {
            if(resourceNameB.equals(str)) {
                correct = true;
                break;
            }
        }

        if(!correct) {
            SwingUtilities.invokeLater(() -> {
                System.out.println("Veuillez entrer un nom de ressource valide");
            });
            nextState = State.P_TRADE_J2;
            return;
        }


        int trueValue = jsonB.has(resourceNameB) ? jsonB.getInt(resourceNameB) :0;
        try {
            askedQuantityB = Integer.parseInt(input[1]);
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                System.out.println("Veuillez entrer une quantité de ressource valide");
            });
            nextState = State.P_TRADE_J2;
            return;
        }

        if(askedQuantityB > trueValue) {
            SwingUtilities.invokeLater(() -> {
                System.out.println("Vous n'avez pas assez pas de "+resourceNameB);
            });
            nextState = State.P_TRADE_J2;
            return;
        }

        nextState = State.D_CONFIRMATION;
        print_confirmation();
    }

    private void readScanners() {

        try {
            // Display the list of terminals
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();

            // Use the first terminal
            CardTerminal terminalA = terminals.get(0);
            CardTerminal terminalB = terminals.get(1);

            // Connect with the card A
            cardA = terminalA.connect("*");
            SwingUtilities.invokeLater(() -> {
                System.out.print("\nCard Slot 1: ");
                System.out.println(cardA); //Je laisse le print pour le RP
            });
            CardChannel channelA = cardA.getBasicChannel();

            // Connect with the card B
            cardB = terminalB.connect("*");
            SwingUtilities.invokeLater(() -> {
                System.out.print("Card Slot 2: ");
                System.out.println(cardB); //Je laisse le print pour le RP
            });
            CardChannel channelB = cardB.getBasicChannel();

            String stringJson = NFCTools.read(channelA);
            stringJson = stringJson.substring(stringJson.indexOf("{"), stringJson.lastIndexOf("}") + 1);
            jsonA = new JSONObject(stringJson);

            stringJson = NFCTools.read(channelB);
            stringJson = stringJson.substring(stringJson.indexOf("{"), stringJson.lastIndexOf("}") + 1);
            jsonB = new JSONObject(stringJson);
        } catch (CardException e) {
            e.printStackTrace();
        }
    }

    private void print_confirmation() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("\n\nConfirmer la transaction");
            System.out.println("Client 1 : "+resourceNameA+" "+askedQuantityA);
            System.out.println("Client 2 : "+resourceNameB+" "+askedQuantityB);
            System.out.println("\n\n" +
                    "- [1] Oui\n" +
                    "- [2] Non (recommencer la transaction)\n\n");
        });

        nextState = State.P_CONFIRMATION;
    }

    private void parse_confirmation(String s) {
        switch (s) {
            case "1" :
                nextState = State.WRITE;
                write();
                break;
            case "2" :
                nextState = State.D_TRADE_J1;
                print_trade_J1();
                break;
            default:
                nextState = State.D_CONFIRMATION;
                print_confirmation();
        }
    }

    private void write() {

        int currentAA = jsonA.getInt(resourceNameA);
        int currentBA = jsonB.getInt(resourceNameA);

        currentAA -= askedQuantityA;
        currentBA += askedQuantityA;

        jsonA.put(resourceNameA, currentAA);
        jsonB.put(resourceNameA, currentBA);


        int currentBB = jsonB.getInt(resourceNameB);
        int currentAB = jsonA.getInt(resourceNameB);

        currentBB -= askedQuantityB;
        currentAB += askedQuantityB;

        jsonB.put(resourceNameB, currentBB);
        jsonA.put(resourceNameB, currentAB);


        CardChannel channelA = cardA.getBasicChannel();
        NFCTools.write(channelA, jsonA.toString());

        CardChannel channelB = cardB.getBasicChannel();
        NFCTools.write(channelB, jsonB.toString());

        SwingUtilities.invokeLater(() -> {
            System.out.println("\n\n********** Fin Échange **********\n\n");
            System.out.println("Veuillez appuyer sur la touche entrée pour continuer\n");
        });

        // Disconnect the card
        try {
            cardA.disconnect(false);
            cardB.disconnect(false);
        } catch (CardException e) {
            e.printStackTrace();
        }

        nextState = State.D_WELCOME;
        reset();
    }

    @Override
    public void writeHelp() {
        System.out.println(
                "\nCOMMANDES DISPONIBLES :\n" +
                        "- [1] Consulter possessions\n" +
                        "- [2] Faire un échange\n\n");
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
        System.out.println("\nPour annuler la transaction à tout moment tapez \"annuler\"");
        System.out.println("################################################################################\n");
        writeHelp();
    }
}
