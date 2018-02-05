package panels;

public class MainMenuPanel extends TerminalPanel{

    public MainMenuPanel() {
        super();
    }

    //Define new commands for the main menu
    @Override
    public void parseCommand(String s) {
        switch (s) {
            case "test" :
                System.out.println("This is my first command");
                break;
        }
        super.parseCommand(s);
    }
}
