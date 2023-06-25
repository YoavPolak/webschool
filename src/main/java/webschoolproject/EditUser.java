package webschoolproject;

import static com.mongodb.client.model.Filters.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;


public class EditUser extends JFrame implements ActionListener{
    private static JFrame editUserFrame;
    private static JTextField firstNameField;
    private static JTextField lastNameField;
    private static JTextField passwordField;
    private static JTextField subjectField;
    private static JComboBox<String> gradeField;
    private static ArrayList<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
    private static User currentUser;
    static Boolean trigered = false;

    public EditUser(){}

    public static  User runEditUser(User user){
        EditUser.editUserFrame = new JFrame();

        currentUser = user;
        editUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editUserFrame.setLayout(null);
        // editUserFrame.setSize(1000, 800);
        editUserFrame.setVisible(true);
        editUserFrame.setTitle("Edit User");
        editUserFrame.setResizable(false);
        editUserFrame.getContentPane().setBackground(new Color(225,225,210));;
        

        JButton save = new JButton("Save"); 
        save.setFocusable(false);
        save.setBackground(new Color(153,153,153));
        save.setForeground(Color.white);
        save.addActionListener(new EditUser());
        save.setBounds(150, 450,150, 50);
        editUserFrame.add(save);
        


        formBuilder();
        return currentUser;
    }


    public static void formBuilder(){
        firstNameField = new JTextField(currentUser.getFirstName());
        firstNameField.setBounds(200, 112, 100, 25);

        JLabel firstNamelabel = new JLabel("First Name");
        firstNamelabel.setBounds(100,100,100,50);


        lastNameField = new JTextField(currentUser.getLastName());
        lastNameField.setBounds(200, 162, 100, 25);

        JLabel lastNamelabel = new JLabel("Last Name");
        lastNamelabel.setBounds(100,150,100,50);


        passwordField = new JTextField(currentUser.getPassword());
        passwordField.setBounds(200, 212, 100, 25);

        JLabel passwordlabel = new JLabel("Password");
        passwordlabel.setBounds(100,200,100,50);


        
        editUserFrame.add(lastNamelabel);

        editUserFrame.add(passwordlabel);

        editUserFrame.add(firstNamelabel);

        editUserFrame.add(firstNameField);

        editUserFrame.add(lastNameField);

        editUserFrame.add(passwordField);

        if(currentUser instanceof Teacher){
            editUserFrame.setSize(750, 550);
            // save.setBounds(300, 450,150, 50);
            Teacher localTeacher = (Teacher) currentUser;
            subjectField = new JTextField( localTeacher.getSubject());
            subjectField.setBounds(200, 262, 100, 25);

            JLabel subjectlabel = new JLabel("Subject");
            subjectlabel.setBounds(100,250,100,50);
            

            JScrollPane sp = teacherSchedulePane(localTeacher);

            editUserFrame.add(sp);
            editUserFrame.add(subjectlabel);
            editUserFrame.add(subjectField);
            teacherDropDownMenu(localTeacher);

        }else if (currentUser instanceof Student){
            editUserFrame.setSize(475,550);
            
            Student s1 = (Student) currentUser;
            gradeField = classChooser(s1.getGrade());
        }
        editUserFrame.setVisible(true);


    }

    public static JScrollPane teacherSchedulePane(Teacher teacher){
        
        String[] column = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};

        ArrayList<ArrayList<String>> dataPrep = teacher.getSchedule();
        
        String[][] data = dataPrep.stream()
            .map(innerList -> innerList.toArray(new String[0]))
            .toArray(String[][]::new);

        JScrollPane sp = Gui.arrayToTable(column, data, true,50);
        sp.setBounds(320,112,400,300);
        
        return sp;
    }

    public static void teacherDropDownMenu(Teacher teacher) {
        
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel listOfFiles = new JPanel();
        listOfFiles.setLayout(new BoxLayout(listOfFiles, BoxLayout.Y_AXIS));

        for (int i = 0; i < Backendw.allClasses.size(); i++) {
            JCheckBox checkBox1 = new JCheckBox(Backendw.allClasses.get(i));
            if(teacher.getClassesList().contains(Backendw.allClasses.get(i))){
                checkBox1.setSelected(true);
            }
            checkBoxList.add(checkBox1);
            checkBox1.setFocusable(false);
            listOfFiles.add(checkBox1);
        }

        JScrollPane jScrollPane = new JScrollPane(listOfFiles);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setPreferredSize(new Dimension(200, 100));

        contentPane.add(jScrollPane);
        contentPane.setBounds(200, 300, 90, 100); // Set bounds for custom positioning
        editUserFrame.add(contentPane);
        editUserFrame.setVisible(true);
    }


    public static JComboBox<String> classChooser(String grade){
        String allClasses[]= (String[]) Backendw.allClasses.toArray();        
        JComboBox<String> cb= new JComboBox<String>(allClasses);    
        cb.setBounds(200, 250,90,20);

        for(int i=0;i<allClasses.length;i++) {
            if(allClasses[i].equals(grade)){
                cb.setSelectedIndex(i);
            }
        }
        //SOMEHOW MAKE IT SO THE CURRENT CLASS IS THE DEFAULT IM THE BOX       
        editUserFrame.add(cb);
        editUserFrame.setVisible(true);

        return cb;
    }



    public static ArrayList<ArrayList<String>> scheduleUpdateSaver(){
        //getting a model from the frame is fine because there is only one possible model
        ArrayList<ArrayList<String>> schedule = new ArrayList<>();
        DefaultTableModel model = new DefaultTableModel();
        Component[] components = editUserFrame.getContentPane().getComponents();
            for (Component c : components) {
                if (c instanceof JScrollPane && ((JScrollPane)c).getViewport().getView() instanceof JTable) {
                    JTable table = (JTable) ((JScrollPane) c).getViewport().getView();
                    model = (DefaultTableModel) table.getModel();
                }
            }
        

        for(int i = 0; i<5; i++){
            ArrayList<String> temp = new ArrayList<>();
            for(int j = 0; j<5; j++){
                String value = (String) model.getValueAt(i, j);
                temp.add(value);
            }
            schedule.add(temp);
        }
        return schedule;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        
        if(button.getText() == "Save") {
                // Load the GIF from URL


            try {
                if(!(currentUser instanceof Admin))
                Backendw.deleteUser(eq("id", currentUser.getId()), currentUser.getSchool());
            } catch (IOException e1) {}

            if (currentUser instanceof Teacher){//teacher
            
                String password = passwordField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();   
                String subject = subjectField.getText();
                ArrayList<String> classes = new ArrayList<>();
                ArrayList<ArrayList<String>> schedule = scheduleUpdateSaver();
                //save the grades
                for(int i=0;i<Backendw.allClasses.size();i++){
                    if((checkBoxList.get(i)).isSelected()){
                        classes.add(Backendw.allClasses.get(i));
                        
                    }
                }

                Teacher updatedUser = (Teacher) EditUser.currentUser;
                updatedUser.setPassword(password);updatedUser.setFirstName(firstName);updatedUser.setLastName(lastName);
                updatedUser.setClassesList(classes);updatedUser.setSubject(subject);updatedUser.setSchedule(schedule);

                EditUser.currentUser = updatedUser;
           
            //Student
            }else if (currentUser instanceof Student){
                String password = passwordField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText(); 
                String grade = (String) gradeField.getItemAt(gradeField.getSelectedIndex());
                
                Student updatedUser = (Student) EditUser.currentUser;
                updatedUser.setPassword(password);updatedUser.setFirstName(firstName);updatedUser.setLastName(lastName);updatedUser.setGrade(grade);
                EditUser.currentUser = updatedUser;
            }

            Backendw.addUser(currentUser);
            EditUser.editUserFrame.dispose();
            new Dashboard(Gui.adminFrame, currentUser.getSchool());
            EditUser.editUserFrame.getContentPane().removeAll();        
            
            JOptionPane.showMessageDialog(Gui.adminFrame, "Saved changes!");
        }                

    }
}