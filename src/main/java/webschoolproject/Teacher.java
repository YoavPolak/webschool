package webschoolproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.mongodb.client.model.Filters.*;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class Teacher extends User{

    private List<String> classesList;
    private String subject;
    private ArrayList<ArrayList<String>> schedule;
    

    //notes: check what is going on with subject (none or something)
    public Teacher(String id, String password, String firstName, String lastName, String school, String subject, 
        List<String> classesList, ArrayList<ArrayList<String>> schedule)  throws IOException {
    //-------------------------------------------

        super(id, password, firstName, lastName, school);
        
        this.subject = subject;
        this.classesList = classesList;
        this.schedule = schedule;

    }

    public Teacher(String id, String password, String firstName, String lastName, String school, String subject) throws IOException  {

        this(id, password, firstName, lastName, school, subject ,new ArrayList<String>(), Backendw.defaultSchedule() );
        
    }

    @Override
    public void mainMenuView() {
        Gui.frameInit(Gui.teacherFrame);
        JPanel teacherSidePanel = Gui.PanelCreator("green");

        JLabel label = new JLabel();
        label.setVisible(false);
        

        JButton submit = new JButton("Submit");
        submit.setFocusable(false);
        submit.setBounds(525, 600, 150, 50);
        submit.setBackground(Gui.secondary);
        submit.setForeground(Color.white);
        submit.addActionListener(new ActionListener() { 
            
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = new DefaultTableModel();

                JLabel label = new JLabel();
                label.setVisible(false);
                JEditorPane editorPane = new JEditorPane();

                Component[] components = Gui.teacherFrame.getContentPane().getComponents();
                for (Component c : components) {
                    
                    if (c instanceof JScrollPane && ((JScrollPane)c).getViewport().getView() instanceof JTable) {
                        JTable table = (JTable) ((JScrollPane) c).getViewport().getView();
                        model = (DefaultTableModel) table.getModel();

                    }else if(c instanceof JLabel){
                        label = (JLabel) c;

                    } else if(c instanceof JScrollPane && ((JScrollPane)c).getViewport().getView() instanceof JEditorPane) {
                        editorPane = (JEditorPane) ((JScrollPane) c).getViewport().getView();
                    }
                }

                String[] gradeName = label.getText().split(" ");
                if(gradeName[1].equals("Grades")) {
                    handleGrades(model, gradeName[0]);
                } else if (gradeName[1].equals("ClassEvents")){
                    handleCheckMark(model, gradeName[0]);
                } else if(gradeName[1].equals("homework")){
                    handleHomework(gradeName[0], editorPane);
                    editorPane.setText("<html><body><p></p></body></html>");
                }                
                JOptionPane.showMessageDialog(Gui.teacherFrame, "Apllied changes!");
            }
        });
        submit.setVisible(false);
        Gui.teacherFrame.add(submit);


        JButton homeworkButton = new JButton("Enter Homework");
        homeworkButton.setFocusable(false);
        homeworkButton.setBounds(25, 100, 150, 50);
        homeworkButton.setBackground(Gui.secondary);
        homeworkButton.setForeground(Color.white);
        homeworkButton.addActionListener((new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                String chosenClass = classChooser();
                if(chosenClass != null) {
                    homeworkPane();
                    label.setText(chosenClass + " homework");
                    label.setBounds(255,600, 100, 50);
                    Gui.teacherFrame.add(label);
                    submit.setVisible(true);
                }
            }
        }));
        homeworkButton.setVisible(true);
        teacherSidePanel.add(homeworkButton);


        JButton gradesButton = new JButton("Enter Grades");
        gradesButton.setFocusable(false);
        gradesButton.setBounds(25, 175, 150, 50);
        gradesButton.setBackground(Gui.secondary);
        gradesButton.setForeground(Color.white);
        gradesButton.addActionListener((new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                String chosenClass = classChooser();
                if(chosenClass != null) {
                    gradesPane(chosenClass);
                    label.setText(chosenClass + " Grades");
                    label.setBounds(255,600, 100, 50);
                    label.setVisible(false);
                    Gui.teacherFrame.add(label);
                    submit.setVisible(true);
                }
            }
        }));
        gradesButton.setVisible(true);
        teacherSidePanel.add(gradesButton);


        
        JButton classEvents = new JButton("Class Events");
        classEvents.setFocusable(false);
        classEvents.setBounds(25, 250, 150, 50);
        classEvents.setBackground(Gui.secondary);
        classEvents.setForeground(Color.white);
        classEvents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chosenClass = classChooser();
                if(chosenClass != null) {
                    classEventPane(chosenClass);
                    label.setText(chosenClass + " ClassEvents");
                    label.setBounds(255,600, 100, 50);
                    label.setVisible(false);
                    Gui.teacherFrame.add(label);
                    submit.setVisible(true);
                }

            }
        });
        classEvents.setVisible(true);
        Gui.teacherFrame.add(classEvents);

        JButton studentsButton = new JButton("Students");
        studentsButton.setFocusable(false);
        studentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.teacherFrame.add(studentsListPane());
            }
        });
        studentsButton.setBounds(25, 325, 150, 50);
        studentsButton.setBackground(Gui.secondary);
        studentsButton.setForeground(Color.white);
        Gui.teacherFrame.add(studentsButton);


        JButton scheduleButton = new JButton("Schedule");
        scheduleButton.setFocusable(false);
        scheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTeacher();
                Gui.teacherFrame.add(schedulePane());
            }
        });
        scheduleButton.setBounds(25, 400, 150, 50);
        scheduleButton.setBackground(Gui.secondary);
        scheduleButton.setForeground(Color.white);
        teacherSidePanel.add(scheduleButton);
        
        
        Gui.teacherFrame.add(teacherSidePanel);
        Gui.teacherFrame.add(schedulePane());
    }



    public void handleHomework(String chosenClass, JEditorPane editorPane){
        String homework = editorPane.getText();
        String msg =  homework;//can make in the homeworkpane the (fullname + subject)

        Object obId = Backendw.getObjectIdFromInfo("classes", this.getSchool());
        Backendw.changeData(obId, User.Request.add, "classes.0." + chosenClass + ".0.homework", msg, this.getSchool()+"_info");
    }






    public void homeworkPane(){
        Arrays.stream(Gui.teacherFrame.getContentPane().getComponents())
        .filter(component -> component instanceof JScrollPane || component instanceof JButton)
        .forEach(component -> {
            if(!(component instanceof JButton)) {
            component.setVisible(false);
            Gui.teacherFrame.remove(component);
            } else {
                if(((JButton) component).getText() == "Submit") {
                    component.setVisible(false);
                }
            }
        });

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        
        editorPane.setText("<html><body><div><p></p></div></body></html>");
        editorPane.setBounds(230, 100, 730, 435);
        editorPane.setVisible(true);
        JScrollPane sp = new JScrollPane(editorPane);
        sp.setVisible(true);
        sp.setBounds(230, 100, 730, 435);
        Gui.teacherFrame.add(sp, BorderLayout.CENTER);
    }







    public void handleGrades(DefaultTableModel model, String chosenClass) {
        ArrayList<String> arr = new ArrayList<>();
        arr.add(chosenClass);
        
        ArrayList<String> students = Backendw.getStudentsList(arr, this.getSchool()).get(arr.get(0));

        for (int i = 0; i < students.size(); i++) {


                        

            model.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    // Object value = model.getValueAt(row, column);
                    model.getValueAt(row, column);
                }
            });


            String[] studentName = (String[]) ((String) model.getValueAt(i,0)).split(" ");
            String grade = (String) model.getValueAt(i,1);
            Student student = (Student) Backendw.getUser(and(eq("grade", arr.get(0)),eq("firstName", studentName[0]), 
            eq("lastName", studentName[1]), eq("school", this.getSchool())));
            
            Object obId = Backendw.getObjectIdOfUser("id", student.getId(), student.getSchool());
            
            Backendw.changeData(obId, User.Request.add, "grades." + this.getSubject(), grade , student.getSchool());
        }
    }



    
    public void gradesPane(String chosenClass){
        Arrays.stream(Gui.teacherFrame.getContentPane().getComponents())
        .filter(component -> component instanceof JScrollPane || component instanceof JButton)
        .forEach(component -> {
            if(!(component instanceof JButton)) {
            component.setVisible(false);
            Gui.teacherFrame.remove(component);
            } else {
                if(((JButton) component).getText() == "Submit") {
                    component.setVisible(false);
                }
            }
        });
        ArrayList<String> arr = new ArrayList<>();
        arr.add(chosenClass);
        //two columns (name,grade)
        //rows by students size
        ArrayList<String> students = Backendw.getStudentsList(arr, this.getSchool()).get(arr.get(0));

        String data[][] = new String[students.size()][2];
        String[] column = {"Studnet's Name","Student's Grade"};
        int counter = 0;
        for(String student:students){
            data[counter][0] = student;
            data[counter][1] = "";
            counter ++;
        }
        
        JScrollPane sp = Gui.arrayToTable(column, data, true, 50);
        sp.setBounds(230, 150, 730, 435);
        Gui.teacherFrame.add(sp);    

        

                

    }






    public JScrollPane studentsListPane() {
        Arrays.stream(Gui.teacherFrame.getContentPane().getComponents())
        .filter(component -> component instanceof JScrollPane || component instanceof JButton)
        .forEach(component -> {
            if(!(component instanceof JButton)) {
            component.setVisible(false);
            Gui.teacherFrame.remove(component);
            } else {
                if(((JButton) component).getText() == "Submit") {
                    component.setVisible(false);
                }
            }
        });



        String[] column = new String[this.getClassesList().size()];
        ArrayList<ArrayList<String>> dataPrep = new ArrayList<ArrayList<String>>();

        Map<String, ArrayList<String>> map = Backendw.getStudentsList(new ArrayList<>(this.getClassesList()), this.getSchool());
        int highestColumn = 0;

        for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
            if (entry.getValue().size() > highestColumn) {
                highestColumn = entry.getValue().size();
            }
        }

        for(int i=0;i<highestColumn;i++) {
            dataPrep.add(new ArrayList<String>());
        }



        int i=0;
        for (Map.Entry<String, ArrayList<String>> entry : Backendw.getStudentsList(new ArrayList<>(this.getClassesList()), this.getSchool()).entrySet()) {

            for(int j=0;j<entry.getValue().size();j++) {
                dataPrep.get(j).add(entry.getValue().get(j));
            }
            

            column[i] = entry.getKey();
            i++;
        }
        
        


        String[][] data = dataPrep.stream()
            .map(innerList -> innerList.toArray(new String[0]))
            .toArray(String[][]::new);

        JScrollPane sp = Gui.arrayToTable(column, data, false,50);
        sp.setBounds(230, 150, 730, 435);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        return sp;
    }




    public void handleCheckMark(DefaultTableModel model, String chosenClass){
        ArrayList<String> arr = new ArrayList<>();
        arr.add(chosenClass);
        
        ArrayList<String> students = Backendw.getStudentsList(arr, this.getSchool()).get(arr.get(0));

        for (int i = 0; i < students.size(); i++) {

            for (int j = 1; j < model.getColumnCount(); j++) {
                
                if (!model.getColumnClass(j).equals(String.class)) {

                    Boolean selected = (Boolean) model.getValueAt(i, j);
                    
                    if (selected) {
                        
                        String message = this.getSubject();
                        
                        String studentName = students.get(i);

                        Student student = (Student) Backendw.getUser(and(eq("grade", arr.get(0)),eq("firstName", studentName.split(" ")[0]), eq("lastName", studentName.split(" ")[1]), eq("school", this.getSchool())));
                        Object obId = Backendw.getObjectIdOfUser("id", student.getId(), student.getSchool());
                        

                        if(j == 1){
                            //backend work - attendence
                            
                            Backendw.changeData(obId, User.Request.add, "attendence", message + " (Absent)", student.getSchool());
                        
                        }else if(j == 2){
                            //backend work - Interuption
                            Backendw.changeData(obId, User.Request.add, "interuptions", message + " (Interupted)", student.getSchool());
                        
                        }else if(j == 3){
                            //backend work - Late
                            Backendw.changeData(obId, User.Request.add, "lates", message + " (Late)", student.getSchool());
                        
                        }else if(j == 4){
                            //backend work - P.R
                            Backendw.changeData(obId, User.Request.add, "positiveReinforcement", message + " (P.R)", student.getSchool());
                        }

                    }
                }
            }
        }
    }


    public void classEventPane(String chosenClass){
        Arrays.stream(Gui.teacherFrame.getContentPane().getComponents())
        .filter(component -> component instanceof JScrollPane || component instanceof JButton)
        .forEach(component -> {
            if(!(component instanceof JButton)) {
            component.setVisible(false);
            Gui.teacherFrame.remove(component);
            } else {
                if(((JButton) component).getText() == "Submit") {
                    component.setVisible(false);
                }
            }
        });


    
        //create the table with check marks
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Student", "Absent", "Interuption", "Late", "P.R"}, 0);

        ArrayList<String> arr = new ArrayList<>();
        arr.add(chosenClass);

        ArrayList<String> students = Backendw.getStudentsList(arr, this.getSchool()).get(arr.get(0));
        for(String student : students){
            model.addRow(new Object[]{student, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE});

        }

        JTable table = new JTable(model);
        for (int i = 1; i < 5; i++) {
            TableColumn checkboxColumn = table.getColumnModel().getColumn(i);
            checkboxColumn.setCellRenderer(table.getDefaultRenderer(Boolean.class));
            checkboxColumn.setCellEditor(table.getDefaultEditor(Boolean.class));
        }
        

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(230, 150, 730, 435);
        Gui.teacherFrame.add(sp);    
    }





    
    public String classChooser(){
        String[] optionsToChoose = this.getClassesList().toArray(new String[0]);

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







    //allows to view schedule
    public JScrollPane schedulePane(){
        Arrays.stream(Gui.teacherFrame.getContentPane().getComponents())
        .filter(component -> component instanceof JScrollPane || component instanceof JButton)
        .forEach(component -> {
            if(!(component instanceof JButton)) {
            component.setVisible(false);
            Gui.teacherFrame.remove(component);
            } else {
                if(((JButton) component).getText() == "Submit") {
                    component.setVisible(false);
                }
            }
        });

        String[] column = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};

        ArrayList<ArrayList<String>> dataPrep = schedule;
        
        String[][] data = dataPrep.stream()
            .map(innerList -> innerList.toArray(new String[0]))
            .toArray(String[][]::new);

        JScrollPane sp = Gui.arrayToTable(column, data, false,50);
        sp.setBounds(230, 150, 730, 435);
        
        return sp;
    }







    public void updateSchedule(int row, int column, String grade) throws IOException {
        ArrayList<String> hour = this.schedule.get(row);
        hour.set(column, grade);
    }





    public Teacher updateTeacher() {
        Teacher t1 = (Teacher) Backendw.getUser(eq("id", this.getId()));//need to work on this
        this.subject = t1.getSubject();
        this.classesList = t1.getClassesList();
        this.schedule = t1.getSchedule();
        return t1;
    }


































    public ArrayList<ArrayList<String>> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<ArrayList<String>> schedule) {
        this.schedule = schedule;
    }



    public List<String> getClassesList() {
        return classesList;
    }



    public void setClassesList(List<String> classesList) {
        this.classesList = classesList;
    }



    public String getSubject() {
        return subject;
    }



    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        super.setLastName(lastName);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Override
    public String toString() {
        return "Teacher [first name=" + this.getFirstName()  + " last name=" + this.getLastName() + " classesList=" + classesList + ", subject=" + subject
                + ", schedule=" + "]";
    }
}
