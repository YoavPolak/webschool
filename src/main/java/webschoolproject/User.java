package webschoolproject;
import java.io.IOException;
import java.util.Scanner;

public abstract class User{
    private String id;
    private String password;
    private String firstName;
    private String lastName;
    private String school;




    public static enum Request {add, replace, removeValue};
    public static enum Keys {id, password, firstName, lastName, subject, classesList, studentsList, schedule, grade, grades, 
        teachersList, homeWork, positiveReinforcement, lates, attendance, interuptions};

    static Scanner sc = new Scanner(System.in);

    public User(String id, String password, String firstName, String lastName, String school)  throws IOException{
        this.id = id;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.school = school;
    }



    public abstract void mainMenuView();


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
    public String getSchool() {
        return school;
    }


    public void setSchool(String school) {
        this.school = school;
    }


    @Override
    public String toString() {
        return "User [id=" + id + ", password=" + password + ", firstName=" + firstName + ", lastName=" + lastName
                + ", school=" + school + "]";
    }




    

}
