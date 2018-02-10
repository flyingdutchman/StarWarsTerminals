package tools;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

/**
 * This class extends from OutputStream to redirect output to a JTextArrea
 * @author www.codejava.net
 *
 *  
 */
public class CustomOutputStream extends OutputStream {
    private JTextPane textPane;
    private SimpleAttributeSet set;

    public CustomOutputStream(JTextPane textPane, SimpleAttributeSet set) {
        super();
        this.textPane = textPane;
        this.set = set;
    }


    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException
    {
        final String text = new String (buffer, offset, length);
        StyledDocument doc = textPane.getStyledDocument();

            try {
                doc.insertString(doc.getLength(), text, set);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        // scrolls the text area to the end of data
        textPane.setCaretPosition(textPane.getDocument().getLength());
    }

    @Override
    public void write(int b) throws IOException
    {
        write (new byte [] {(byte)b}, 0, 1);
    }
}