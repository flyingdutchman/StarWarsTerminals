package panels;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;

public class TerminalPanel extends JPanel {

    private final Color terminalColor = new Color(20,20,27);
    private final Font consoleFont = new Font("Courier New", Font.PLAIN, 20);
    private final Color fontColor = Color.LIGHT_GRAY;

    private JTextPane displayPane;
    private JTextField commandPane;
    private SimpleAttributeSet set;

    public TerminalPanel() {

        setLayout(new BorderLayout());

        //Affichage du texte de la console
        displayPane = new JTextPane();
        displayPane.setCaretPosition(0);
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
        displayPane.setCharacterAttributes(set, true);

        // Permet d'utiliser le JTextPane comme une sortie console classique
        PrintStream printStream = new PrintStream(new tools.CustomOutputStream(displayPane, set));
        System.setOut(printStream);

        add(displayPane, BorderLayout.CENTER);
        add(commandPane, BorderLayout.SOUTH);
    }

    public void parseCommand(String s) {
        commandPane.setText("");
    }
}
