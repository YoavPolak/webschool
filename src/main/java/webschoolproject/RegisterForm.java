package webschoolproject;

import javax.swing.*;
import java.awt.*;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;



public class RegisterForm extends JFrame implements ActionListener {

    private JFrame registerFormFrame;
    private String school;
    private String role;
    private JLabel idField;
    private JTextField firstNameField = new JTextField();
    private JTextField lastNameField = new JTextField();
    private JTextField passwordField = new JTextField();
    private JComboBox<String> gradeField;
    private JTextField subjectField = new JTextField();
    private ArrayList<String> classesList = new ArrayList<String>();
    private ArrayList<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();

    public RegisterForm(String school ) {
        this.school = school;
        
        this.registerFormFrame = new JFrame("Register Form");
        registerFormFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFormFrame.setSize(475, 600);
        registerFormFrame.setVisible(true);
        registerFormFrame.setTitle("Registration Form");
        registerFormFrame.setResizable(false);
        registerFormFrame.getContentPane().setBackground(Color.white);
        registerFormFrame.setLayout(null);
        registerFormFrame.getContentPane().setBackground(new Color(225,225,210));

        JButton submit = new JButton("Submit");
        submit.setFocusable(false);
        submit.setBounds(162, 500,150, 50);
        submit.setBackground(Gui.secondary);
        submit.setForeground(Color.white);
        submit.addActionListener(this);
        registerFormFrame.add(submit);
        this.formBuilder();
        
    }







    public void formBuilder(){
        
        //generate id
        Clock clock = Clock.systemUTC();
        String newUserId =  String.valueOf(clock.millis()).substring(5);

        idField = new JLabel("Id: " + newUserId);
        idField.setFont(new Font("", Font.BOLD, 15));
        idField.setBounds(200, 50, 100, 50);
        registerFormFrame.add(idField);

        firstNameField.setBounds(200, 112, 100, 25);

        JLabel firstNamelabel = new JLabel("First Name");
        firstNamelabel.setBounds(100,100,100,50);


        lastNameField.setBounds(200, 162, 100, 25);

        JLabel lastNamelabel = new JLabel("Last Name");
        lastNamelabel.setBounds(100,150,100,50);


        passwordField.setBounds(200, 212, 100, 25);

        JLabel passwordlabel = new JLabel("Password");
        passwordlabel.setBounds(100,200,100,50);
        

        // firstNameField.setBounds(200, 100, 100, 50);
        // lastNameField.setBounds(200, 150, 100, 50);
        // passwordField.setBounds(200, 200, 100, 50);
        registerFormFrame.add(firstNameField);
        registerFormFrame.add(lastNameField);
        registerFormFrame.add(passwordField);
        registerFormFrame.add(lastNamelabel);
        registerFormFrame.add(passwordlabel);
        registerFormFrame.add(firstNamelabel);

        

        CheckboxGroup checkboxGroup = new CheckboxGroup();
        Checkbox checkbox1 = new Checkbox("teacher", checkboxGroup, false);
        checkbox1.setBounds(175,250,70,50);
        Checkbox checkbox2 = new Checkbox("student", checkboxGroup, false);
        checkbox2.setBounds(250,250,70,50);
        
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    role = (String) e.getItem();
                    
                    if(role.equals("student")){
                        Arrays.stream(registerFormFrame.getContentPane().getComponents())
                        .filter(component -> component instanceof JComboBox || component instanceof JTextField || component instanceof JComboBox || component instanceof JPanel || component instanceof JLabel)
                        .forEach(component -> {
                            if(!(component instanceof JButton) && !(component instanceof JTextField) && !(component instanceof JLabel)) {
                            component.setVisible(false);
                            registerFormFrame.remove(component);

                            } else if (component instanceof JTextField) {

                                JTextField comp = (JTextField) component;

                                if(comp.getName() != null && comp.getName().equals("subject")) {
                                    component.setVisible(false);
                                    registerFormFrame.remove(component);
                                }
                            } else if (component instanceof JLabel ){
                                JLabel comp = (JLabel) component;

                                if (comp.getName() != null && comp.getName().equals("subjectLabel")){
                                    component.setVisible(false);
                                    registerFormFrame.remove(component);
                                }
                            }
                        });
                        gradeField = classChooser();
                        

                    } else if(role.equals("teacher")) {
                        Arrays.stream(registerFormFrame.getContentPane().getComponents())
                        .filter(component -> component instanceof JComboBox)
                        .forEach(component -> {
                            if(!(component instanceof JButton)) {
                            component.setVisible(false);
                            registerFormFrame.remove(component);
                            }
                        });

                        teacherDropDownMenu();
                        JLabel subjectLabel = new JLabel("Subject:");
                        subjectLabel.setName("subjectLabel");
                        subjectLabel.setBounds(125,300,100,50);
                        subjectLabel.setVisible(true);
                        registerFormFrame.add(subjectLabel);
                        subjectField.setBounds(200, 313, 100, 25);
                        subjectField.setVisible(true);
                        subjectField.setName("subject");
                        registerFormFrame.add(subjectField);
                        registerFormFrame.update(registerFormFrame.getGraphics());
                    }
                };
            };        
        };

        checkbox1.addItemListener(itemListener);
        checkbox2.addItemListener(itemListener);
        registerFormFrame.add(checkbox1);
        registerFormFrame.add(checkbox2);
    }

    public void teacherDropDownMenu() {
        
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel listOfFiles = new JPanel();
        listOfFiles.setLayout(new BoxLayout(listOfFiles, BoxLayout.Y_AXIS));

        for (int i = 0; i < Backendw.allClasses.size(); i++) {
            JCheckBox checkBox1 = new JCheckBox(Backendw.allClasses.get(i));
            checkBoxList.add(checkBox1);
            checkBox1.setFocusable(false);
            listOfFiles.add(checkBox1);
        }

        JScrollPane jScrollPane = new JScrollPane(listOfFiles);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setPreferredSize(new Dimension(200, 100));

        contentPane.add(jScrollPane);
        contentPane.setBounds(200, 350, 100, 100); // Set bounds for custom positioning
        registerFormFrame.add(contentPane);
        registerFormFrame.setVisible(true);
    }



    


    public JComboBox<String> classChooser(){
        String country[]= Backendw.allClasses.toArray(new String[0]);        
        JComboBox<String> cb= new JComboBox<String>(country);    
        cb.setBounds(200, 300,90,20);    
        registerFormFrame.add(cb);
        registerFormFrame.update(registerFormFrame.getGraphics());

        return cb;
    }








    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();

        if(button.getText() == "Submit") {
            


            String id = idField.getText().substring(4, idField.getText().length());
            String password = passwordField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();   
            
            
            if(role.equals("teacher")){
                ArrayList<String> classes = classesList;
                String subject = subjectField.getText();

                for(int i=0;i<Backendw.allClasses.size();i++){
                   if((checkBoxList.get(i)).isSelected()){
                        classes.add(Backendw.allClasses.get(i));
                    }
                }
                
                try {
                    Teacher teacher;
                    teacher = new Teacher(id, password, firstName, lastName, this.school, subject);
                    teacher.setClassesList(classes);
                    Backendw.addUser(teacher);
                    System.out.println(teacher);
                } catch (IOException e1) {};
                
                
            } else if (role.equals("student")) {
                String grade = gradeField.getItemAt(gradeField.getSelectedIndex());


                try {
                    Student student;
                    student = new Student(id, password, firstName, lastName, this.school, grade);
                    Backendw.addUser(student);
                } catch (IOException e1) {};
                
            }
            
            registerFormFrame.dispose();
            JOptionPane.showMessageDialog(Gui.adminFrame, "new User has been created, ID: " + id);

        }
    }
    
}