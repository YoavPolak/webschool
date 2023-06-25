package webschoolproject;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static com.mongodb.client.model.Filters.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Student extends User {

    private String grade;
    private Map<String, List<String>> grades;
    private List<String> positiveReinforcement;
    private List<String> lates;
    private List<String> attendence;
    private List<String> interuptions;



    public Student(String id, String password, String firstName, String lastName, String school, String grade, Map<String, List<String>> grades, 
    List<String> positiveReinforcement, List<String> lates, List<String> attendence,  List<String> interuptions)  throws IOException {

        super(id, password, firstName, lastName, school);

        this.grade = grade;
        this.grades = grades;
        this.positiveReinforcement = positiveReinforcement;
        this.lates = lates;
        this.attendence = attendence;
        this.interuptions = interuptions;
    }

    public Student(String id, String password, String firstName, String lastName, String school, String grade)  throws IOException{

        this(id, password, firstName, lastName, school, grade, new HashMap<>(), 
        new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    }


    @Override
    public void mainMenuView(){
        Student s1 = (Student) this;

        Gui.frameInit(Gui.studentFrame);
        JPanel studentSidePanel = Gui.PanelCreator("blue");



        JButton gradesButton = new JButton("Grades");
        gradesButton.setFocusable(false);
        gradesButton.setBounds(25, 250, 150, 50);
        gradesButton.setBackground(Gui.secondary);
        gradesButton.setForeground(Color.white);
        gradesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JScrollPane sp = s1.gradesPane();
                sp.setBounds(230, 200, 730, 435);
                Gui.studentFrame.add(sp);
            }
        });
        
        gradesButton.setVisible(true);
        studentSidePanel.add(gradesButton);


        JButton classEvents = new JButton("ClassEvents");
        classEvents.setFocusable(false);
        classEvents.setBounds(25, 175, 150, 50);
        classEvents.setBackground(Gui.secondary);
        classEvents.setForeground(Color.white);
        classEvents.addActionListener(new ActionListener() {
    
            public void actionPerformed(ActionEvent evt) {

                JScrollPane sp = s1.classEventsPane();
                sp.setBounds(230, 200, 730, 435);
                Gui.studentFrame.add(sp);
            }

            }
        );
        classEvents.setVisible(true);
        studentSidePanel.add(classEvents);

        JButton teachersButton = new JButton("Teachers");
        teachersButton.setFocusable(false);
        teachersButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                JScrollPane sp = s1.teachersPane();
                sp.setBounds(230, 200, 730, 435);
                Gui.studentFrame.add(sp);

            }
        });

        teachersButton.setBounds(25, 325, 150, 50);
        teachersButton.setBackground(Gui.secondary);
        teachersButton.setForeground(Color.white);
        studentSidePanel.add(teachersButton);

        JButton scheduleButton = new JButton("Schedule");
        scheduleButton.setFocusable(false);
        scheduleButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                showSchedule(s1);
            }   
        });

        scheduleButton.setBounds(25, 400, 150, 50);
        scheduleButton.setBackground(Gui.secondary);
        scheduleButton.setForeground(Color.white);
        studentSidePanel.add(scheduleButton);



        JButton homeworkButton = new JButton("Homework");
        homeworkButton.setFocusable(false);
        homeworkButton.setBounds(25, 100, 150, 50);
        homeworkButton.setBackground(Gui.secondary);
        homeworkButton.setForeground(Color.white);
        homeworkButton.addActionListener((new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
               homeworkPane();                 
            }
        }));
        homeworkButton.setVisible(true);
        studentSidePanel.add(homeworkButton);

        Gui.studentFrame.add(studentSidePanel);
        showSchedule(s1);
    }


    public void homeworkPane(){
        Arrays.stream(Gui.studentFrame.getContentPane().getComponents())
            .filter(component -> component instanceof JScrollPane)
            .forEach(component -> {
                component.setVisible(false);
                Gui.studentFrame.remove(component);
            });            


        String[] column = {"Homework"};
        List<String> homework = Backendw.getHomeworkClass(this.getGrade(),this.getSchool());
        String[][] data = new String[homework.size()][1];
        
        int len = 50;
        for(int i = 0; i<homework.size(); i++){
            data[i][0] = homework.get(i);
            if(len < homework.get(i).length()){
                len = (int) (homework.get(i).length()/2.5);
            }
        } 

        JScrollPane sp = Gui.arrayToTable(column, data, false,len);
        sp.setBounds(230, 200, 730, 435);
        Gui.studentFrame.add(sp);
    }

    public void showSchedule(Student s1) {
        JScrollPane sp = s1.schedulePane();
        sp.setBounds(230, 200, 730, 435);
        Gui.studentFrame.add(sp);
    }




    public JScrollPane classEventsPane(){
        Arrays.stream(Gui.studentFrame.getContentPane().getComponents())
            .filter(component -> component instanceof JScrollPane)
            .forEach(component -> {
                component.setVisible(false);
                Gui.studentFrame.remove(component);
            });                

        updateStudent();



        // String[][] data={ {this.getAttendence() + "",this.getInteruptions() + "",this.getLates() + "", this.getPositiveReinforcement() +""}    };    
        List<String> attendenceList = this.getAttendence();
        List<String> interuptionList = this.getInteruptions();
        List<String> latesList = this.getLates();
        List<String> positiveReinforcementList = this.getPositiveReinforcement();

        String[] column={"Attendence","Interuptions","Lates", "P.R"};   
        int numCols= column.length;


        int numRows = 0;
        for (List<String> list : Arrays.asList(attendenceList, interuptionList, latesList, positiveReinforcementList)) {
            numRows = Math.max(list.size(), numRows);
        }
        
        String[][] data = new String[numRows][numCols];
        for (int i=0;i<numRows;i++) {

            data[i][1] = (interuptionList.size() > i ? interuptionList.get(i) : "");
            data[i][0] = (attendenceList.size() > i ? attendenceList.get(i) : "");
            data[i][2] = (latesList.size() > i ? latesList.get(i) : "");
            data[i][3] = (positiveReinforcementList.size() > i ? positiveReinforcementList.get(i) : "");
        }
        JScrollPane sp = Gui.arrayToTable(column, data,false,50);

        return sp;
    }


    
    

    public JScrollPane schedulePane(){
        Arrays.stream(Gui.studentFrame.getContentPane().getComponents())
        .filter(component -> component instanceof JScrollPane)
        .forEach(component -> {
            component.setVisible(false);
            Gui.studentFrame.remove(component);
        });
        String[] column = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};

        ArrayList<ArrayList<String>> dataPrep = Backendw.getSchedule(this.getGrade(), this.getSchool());
        
        String[][] data = dataPrep.stream()
            .map(innerList -> innerList.toArray(new String[0]))
            .toArray(String[][]::new);

        JScrollPane sp = Gui.arrayToTable(column, data,false,50);
        return sp;
    }




    public JScrollPane teachersPane(){
        Arrays.stream(Gui.studentFrame.getContentPane().getComponents())
            .filter(component -> component instanceof JScrollPane)
            .forEach(component -> {
                component.setVisible(false);
                Gui.studentFrame.remove(component);
            });    

        String[] column = {"Name", "Subject"};
        String[][] data = new String[Backendw.getTeachersList(this).size()][Backendw.getTeachersList(this).size() + 1];
        
        int counter = 0;
        for(String teacher : Backendw.getTeachersList(this)){
            String[] listedTeacher = teacher.split(" ");
            data[counter][0] = listedTeacher[0] + " " + listedTeacher[1];
            data[counter][1] = listedTeacher[2].replace("(", "").replace(")","");
            counter ++;

        }
        JScrollPane sp = Gui.arrayToTable(column, data,false,50);
        return sp;
    }




    public JScrollPane gradesPane(){
        Arrays.stream(Gui.studentFrame.getContentPane().getComponents())
            .filter(component -> component instanceof JScrollPane)
            .forEach(component -> {
                component.setVisible(false);
                Gui.studentFrame.remove(component);
            });

        updateStudent();//need to work on this

            int longestLength = this.getGrades().values().stream()
                    .mapToInt( list -> list.size())
                    .max()
                    .orElse(0);    
            String[][] data = new String[longestLength][this.getGrades().size()];
            
            Object[] columnPrep =   this.getGrades().keySet().toArray();
            String[] column =  Arrays.copyOf(columnPrep, columnPrep.length, String[].class);


            for(int i = 0; i < column.length; i++){
                int counter = 0;
                for(String subject:this.getGrades().keySet()){
                    if(column[i].equals(subject)){
                        for(String grade : this.getGrades().get(subject)){
                            data[counter][i] = grade;
                            counter ++;
                        }
                    }
                }
            }
        JScrollPane sp = Gui.arrayToTable(column, data,false,50);
        return sp;
    }




    public Student updateStudent() {
        Student s1 = (Student) Backendw.getUser(eq("id", this.getId()));//need to work on this
        this.setAttendence(s1.getAttendence());
        this.setGrades(s1.getGrades());
        this.setLates(s1.getLates());
        this.setPositiveReinforcement(s1.getPositiveReinforcement());
        this.setInteruptions(s1.getInteruptions());

        return s1;
    }





























    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Map<String, List<String>> getGrades() {
        return grades;
    }

    public void setGrades(Map<String, List<String>> grades) {
        this.grades = grades;
    }

    public List<String> getPositiveReinforcement() {
        return positiveReinforcement;
    }

    public void setPositiveReinforcement(List<String> positiveReinforcement) {
        this.positiveReinforcement = positiveReinforcement;
    }

    public List<String> getLates() {
        return lates;
    }

    public void setLates(List<String> lates) {
        this.lates = lates;
    }

    public List<String> getAttendence() {
        return attendence;
    }

    public void setAttendence(List<String> attendence) {
        this.attendence = attendence;
    }

    public List<String> getInteruptions() {
        return interuptions;
    }

    public void setInteruptions(List<String> interuptions) {
        this.interuptions = interuptions;
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
    public void setId(String id) {
        super.setId(id);
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
        return "Student [grade=" + grade + ", grades=" + grades + ", positiveReinforcement=" + positiveReinforcement
                + ", lates=" + lates + ", attendence=" + attendence + ", interuptions=" + interuptions + "]";
    }
    
}