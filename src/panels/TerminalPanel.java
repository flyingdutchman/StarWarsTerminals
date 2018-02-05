package panels;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TerminalPanel extends JPanel {

    private final Color terminalColor = new Color(20,20,27);
    private final Font consoleFont = new Font("Courier New", Font.PLAIN, 20);
    private final Color fontColor = Color.LIGHT_GRAY;

    private JTextPane displayPane;
    private JTextField commandPane;
    private Document doc;
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
        doc = displayPane.getStyledDocument();

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
        String initString[] =
                { "Bienvenue sur le système d'échange commercial",
                    "Veuillez lire les conditions bla bla bcp de texte !"};

        try {
            for (int i = 0; i < initString.length; i ++) {
                doc.insertString(doc.getLength(), initString[i] + "\n",
                        set);
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text.");
        }

        // Set the attributes before adding text
        displayPane.setCharacterAttributes(set, true);

        add(displayPane, BorderLayout.CENTER);
        add(commandPane, BorderLayout.SOUTH);
    }

    public void parseCommand(String s) {
        commandPane.setText("");
    }
}
