package codes.flyingdutchman.swt.panels;

import codes.flyingdutchman.swt.tools.Crypto;

import javax.crypto.Cipher;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public abstract class TerminalPanel extends JPanel {

    private final Color terminalColor = new Color(20,20,27);
    private final Font consoleFont = new Font("Monospaced", Font.PLAIN, 20);
    private final Color fontColor = Color.LIGHT_GRAY;
    private final String TEMP_PATH = "data/temp/temp";


    JTextField commandPane;
    JTextPane mainPane;
    private JTextPane displayPane;
    private JScrollPane scrollPane;
    private SimpleAttributeSet set;
    private StyledDocument doc;
    private File tmp;

    private boolean readMode;

    public TerminalPanel() {

        setLayout(new BorderLayout());

        MouseListener changeFocus = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                requestPanelFocus();
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {}

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {}

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {}

            @Override
            public void mouseExited(MouseEvent mouseEvent) {}
        };

        //Affichage du texte de la console
        mainPane = new JTextPane();
        mainPane.setCaretPosition(0);
        mainPane.setEditable(false);
        mainPane.setHighlighter(null);
        mainPane.setBackground(terminalColor);
        mainPane.setFont(consoleFont);
        mainPane.addMouseListener(changeFocus);
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
        displayPane.addMouseListener(changeFocus);

        //Parametrage de la zone d'entrée
        commandPane = new JTextField();
        commandPane.addKeyListener(new KeyAdapter() {
            //Permet de simuler le comportement d'une console
            @Override
            public void keyTyped(KeyEvent e) {
                commandPane.setCaretPosition(commandPane.getText().length());
                super.keyTyped(e);
            }
        });
        commandPane.addActionListener(e -> parseCommand(commandPane.getText())); //Parse command when hit enter


        commandPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
            }

            public void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE && readMode) {
                    setReadMode(false);
                }
            }
        });

        SwingUtilities.invokeLater(() -> {
            commandPane.setBackground(terminalColor);
            commandPane.setFont(consoleFont);
            commandPane.setForeground(fontColor);
            commandPane.requestFocus();
        });

        set = new SimpleAttributeSet();
        set.addAttribute(StyleConstants.CharacterConstants.Foreground, fontColor);

        // Set the attributes before adding text
        mainPane.setCharacterAttributes(set, true);


        requestPanelFocus();

        readMode = false;

        add(scrollPane, BorderLayout.CENTER);
        add(commandPane, BorderLayout.PAGE_END);
    }

    public void writeHelp() {
        System.out.println(
                "- Pour réafficher les commandes tapez \"aide\"\n" +
                "- Pour effacer la console tapez \"effacer\"\n\n");
    }

    //Code pour loader un fichier rtf
    public void loadFile() {

        String page = "test.rtf";

        mainPane.setEditorKit(new RTFEditorKit());
        try {
            mainPane.read(new FileInputStream(page), mainPane.getDocument());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    File createReadableTemp(File toDecipher) {
        File temp = new File(TEMP_PATH);
        Crypto.fileProcessor(Cipher.DECRYPT_MODE, toDecipher, temp);
        return temp;
    }

    //TODO détecter hack ici fichier.hack.encrypted
    void setTempDisplay(File file) {
        tmp = file;
        setReadMode(true);
        try {
            displayPane.setPage(file.toURI().toURL());
            scrollPane.setViewportView(displayPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setReadMode(boolean b) {
        if(b) {
            readMode = true;
            commandPane.setEditable(false);
            commandPane.setText("Appuyez sur Esc pour quitter");

        } else {
            readMode = false;
            commandPane.setEditable(true);
            clearCommand();
            scrollPane.setViewportView(mainPane);
            tmp.delete();
        }
    }

    public void requestPanelFocus() {

        // Permet d'utiliser le JTextPane comme une sortie console classique
        PrintStream printStream = new PrintStream(new codes.flyingdutchman.swt.tools.CustomOutputStream(mainPane, set));
        System.setOut(printStream);

        SwingUtilities.invokeLater(() -> {
            commandPane.requestFocus();
        });
    }

    public abstract void writeHeader();

    void clearCommand() {
        commandPane.setText("");
    }

    public void parseCommand(String s) {
        //Commandes générales
        switch (s) {
            case "aide" : writeHelp();
                break;
            case "effacer" : mainPane.setText(""); writeHeader();
                break;
            case "lr" : loadFile();
                break;
            case "lh" : setTempDisplay(new File("test.html"));
                break;
            case "gamemaster wins" : System.exit(0);
            default:
                System.out.println("Unknown Command");
        }
    }
}
