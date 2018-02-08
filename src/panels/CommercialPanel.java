package panels;

import javax.swing.text.rtf.RTFEditorKit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CommercialPanel extends TerminalPanel{

    public CommercialPanel() {
        super();
    }

    public void loadFile() {

        String page = "test.rtf";

        mainPane.setEditorKit(new RTFEditorKit());
        try {
            mainPane.read(new FileInputStream(page), mainPane.getDocument());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHTML() {

        File file = new File("test.html");

        try {
            mainPane.setPage(file.toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Define new commands for the main menu
    @Override
    public void parseCommand(String s) {
        commandPane.setText("");
        switch (s) {
            case "test" :
                System.out.println("This is my first command");
                break;
            case "load rtf" : loadFile();
                break;
            case "load html" : loadHTML();
                break;
            default: super.parseCommand(s);
        }
    }
}
