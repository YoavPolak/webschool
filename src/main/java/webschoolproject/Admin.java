package webschoolproject;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class Admin extends User{
    
    
    public Admin(String id, String password, String firstName, String lastName, String school)  throws IOException {
        super(id, password, firstName, lastName, school);
    }


    @Override
    public void mainMenuView() {

        Gui.frameInit(Gui.adminFrame);
        JPanel adminSidePanel = Gui.PanelCreator("red");
        adminSidePanel.setName("adminSidePanel");

        JLabel label = new JLabel();
        Gui.adminFrame.add(label);





        JButton createUser = new JButton("createUser");
        createUser.setFocusable(false);
        createUser.setBounds(25, 100, 150, 50);
        createUser.setBackground(Gui.secondary);
        createUser.setForeground(Color.white);
        createUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterForm(getSchool());
            }
        });
        createUser.setVisible(true);
        adminSidePanel.add(createUser);



        JButton submit = new JButton("Submit");
        submit.setFocusable(false);
        submit.setBounds(525, 600, 150, 50);
        submit.setBackground(Gui.secondary);
        submit.setForeground(Color.white);
        submit.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {


                JLabel label = new JLabel();
                DefaultTableModel model = new DefaultTableModel();
                Component[] components = Gui.adminFrame.getContentPane().getComponents();
                for (Component c : components) {
                    if (c instanceof JScrollPane && ((JScrollPane)c).getViewport().getView() instanceof JTable) {
                        JTable table = (JTable) ((JScrollPane) c).getViewport().getView();
                        model = (DefaultTableModel) table.getModel();
                    }
                    if(c instanceof JLabel){
                        label = (JLabel)c;
                    }
                }

                String[] gradeName = label.getText().split(" ");
                if(gradeName[1].equals("scheduleS")) {
                    scheduleUpdateSaver(model, gradeName[0]);
                }                
                JOptionPane.showMessageDialog(Gui.adminFrame, "Apllied changes!");
            }
        });

        submit.setVisible(false);
        Gui.adminFrame.add(submit);


        

        
        JButton scheduleButton = new JButton("change class Schedule");
        scheduleButton.setFocusable(false);
        scheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                String reciever = classChooser();
                label.setText(reciever + " scheduleS");

                if(reciever != null) {
                    changeSchedulePane(reciever);
                }

            }
        });
        scheduleButton.setBounds(25, 175, 150, 50);
        scheduleButton.setBackground(Gui.secondary);
        scheduleButton.setForeground(Color.white);
        adminSidePanel.add(scheduleButton);



        
        
        JButton dashboard = new JButton("dashboard");
        dashboard.setFocusable(false);
        dashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Dashboard(Gui.adminFrame, getSchool());
                submit.setVisible(false);
            }
        });
        dashboard.setBounds(25, 250, 150, 50);
        dashboard.setBackground(Gui.secondary);
        dashboard.setForeground(Color.white);
        adminSidePanel.add(dashboard);

        Gui.adminFrame.add(adminSidePanel);

        new Dashboard(Gui.adminFrame, getSchool());
    }







    public void scheduleUpdateSaver(DefaultTableModel model, String grade){


        Object obId = (Object) Backendw.getObjectIdFromInfo("classesSchedule" + grade , this.getSchool());


        for(int i = 0; i<5; i++){
            for(int j = 0; j<5; j++){
                String value = (String) model.getValueAt(i, j);

                Backendw.changeData(obId, User.Request.replace, "classesSchedule" + grade + "." + i + "." + j , value, this.getSchool() + "_info");

            }
        }
    }






    public void changeSchedulePane(String reciever){
        Arrays.stream(Gui.adminFrame.getContentPane().getComponents())
        .filter(component -> component instanceof JScrollPane || component instanceof JButton || component instanceof JPanel)
        .forEach(component -> {
            if(!(component instanceof JButton)) {
                if(component.getName() == null || !component.getName().equals("adminSidePanel")) {
                    if(component.getName() == null || !component.getName().equals("headLine")) {
                        component.setVisible(false);
                        Gui.adminFrame.remove(component);
                    }

                }
            } else {
                if(((JButton) component).getText() == "Submit") {
                    component.setVisible(true);
                }
            }
        });

     
        ArrayList<ArrayList<String>> scheduleList =  Backendw.getSchedule(reciever, this.getSchool());
       
            
        

        String[][] schedule = new String[scheduleList.size()][];
        for (int i = 0; i < scheduleList.size(); i++) {
            ArrayList<String> row = scheduleList.get(i);
            schedule[i] = row.toArray(new String[row.size()]);
        }

        String[] column = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
        JScrollPane sp = Gui.arrayToTable(column, schedule, true,50);
        sp.setBounds(230, 150, 730, 435);
        sp.setVisible(true);
        Gui.adminFrame.add(sp);
        
    }





    public String classChooser(){
        String[] optionsToChoose =(String[]) Backendw.allClasses.toArray();
        String classChoice = (String) JOptionPane.showInputDialog(
                null,
                "Choose a class",
                "Choose Class",
                JOptionPane.QUESTION_MESSAGE,
                null,
                optionsToChoose,
                optionsToChoose[0]
        );
        return classChoice;
    }


}