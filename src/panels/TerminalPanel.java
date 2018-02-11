package panels;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;

public abstract class TerminalPanel extends JPanel {

    private final Color terminalColor = new Color(20,20,27);
    private final Font consoleFont = new Font("Monospaced", Font.PLAIN, 20);
    private final Color fontColor = Color.LIGHT_GRAY;

    JTextPane mainPane;
    protected JTextPane displayPane;
    JTextField commandPane;
    protected JScrollPane scrollPane;
    protected SimpleAttributeSet set;
    protected StyledDocument doc;

    public TerminalPanel() {

        setLayout(new BorderLayout());

        //Affichage du texte de la console
        mainPane = new JTextPane();
        mainPane.setCaretPosition(0);
        mainPane.setEditable(false);
        mainPane.setHighlighter(null);
        mainPane.setBackground(terminalColor);
        mainPane.setFont(consoleFont);
        doc = mainPane.getStyledDocument();

        //Ajout du scroll
        scrollPane = new JScrollPane(mainPane);
        scrollPane.setBackground(terminalColor);
        scrollPane.setBorder(null);
        // Rendre barre de scroll invisible
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));

        //Affichage de nouvelle fenêtre
        displayPane = new JTextPane();
        displayPane.setEditable(false);
        displayPane.setHighlighter(null);
        displayPane.setBackground(terminalColor);
        displayPane.setFont(consoleFont);

        //Parametrage de la zone d'entrée
        commandPane = new JTextField();
        commandPane.setBackground(terminalColor);
        commandPane.setFont(consoleFont);
        commandPane.setForeground(fontColor);
        commandPane.addKeyListener(new KeyAdapter() {
            //Permet de simuler le comportement d'une console
            @Override
            public void keyTyped(KeyEvent e) {
                commandPane.setCaretPosition(commandPane.getText().length());
                super.keyTyped(e);
            }
        });
        commandPane.addActionListener(e -> parseCommand(commandPane.getText())); //Parse command when hit enter

        set = new SimpleAttributeSet();
        set.addAttribute(StyleConstants.CharacterConstants.Foreground, fontColor);

        // Set the attributes before adding text
        mainPane.setCharacterAttributes(set, true);

        // Permet d'utiliser le JTextPane comme une sortie console classique
        PrintStream printStream = new PrintStream(new tools.CustomOutputStream(mainPane, set));
        System.setOut(printStream);

        add(scrollPane, BorderLayout.CENTER);
        add(commandPane, BorderLayout.PAGE_END);
    }

    public void writeHelp() {
        System.out.println(
                "- Pour réafficher les commandes tapez \"aide\"\n" +
                "- Pour effacer la console tapez \"clear\"\n\n");
    }

    public abstract void writeHeader();

    public void parseCommand(String s) {
        //Commandes générales
        switch (s) {
            case "aide" : writeHelp();
                break;
            case "clear" : mainPane.setText(""); writeHeader();
                break;
            default:
                System.out.println("Unknown Command");
        }
    }
}
