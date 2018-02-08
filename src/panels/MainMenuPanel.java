package panels;

import rtf.AdvancedRTFEditorKit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainMenuPanel extends TerminalPanel{

    public MainMenuPanel() {
        super();
    }

    public void loadFile() {

        String page = "test.html";
        File file = new File("test.html");

        /*displayPane.setEditorKit(new AdvancedRTFEditorKit());
        try {
            displayPane.read(new FileInputStream(page), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            displayPane.setPage(file.toURI().toURL());
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
            case "load test" : loadFile();
                break;
            default: super.parseCommand(s);
        }
    }
}
