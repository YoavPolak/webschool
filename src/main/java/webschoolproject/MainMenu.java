package webschoolproject;

import java.io.IOException;

public class MainMenu {
    
    public MainMenu() throws IOException{
        new Backendw();
    }


    //runs the program
    public void runProgram() throws IOException {
        new Gui();
    }

}