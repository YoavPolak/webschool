package webschoolproject;

import static com.mongodb.client.model.Filters.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.Font;


public class Gui extends JFrame  implements ActionListener{
    protected static JFrame teacherFrame = new JFrame();
    protected static JFrame studentFrame = new JFrame();
    protected static JFrame adminFrame = new JFrame();
    protected static JFrame guiFrame = new JFrame();

    private User currentUser;
    String happend = null;
    String view = "";
    JButton login;
    JTextField username;
    JPasswordField password;
    JLabel main = new JLabel();

    JLayeredPane mainMenu = new JLayeredPane();
    JLayeredPane loginPage = new JLayeredPane();
    static JButton logoutButton = new JButton("LOG OUT");

    public static enum Errors {NonExistentUserError, NonExistentClassError, CreatedClassError, GradeError, NonExistentSubjectError, NonExistentHourError, NonExistentDayError};

    final static Color background = new Color(0,0,20); // blue/black
    final static Color secondary = new Color(80,80,80); //secondary
    final static Color purple = new Color(75,0,130);//purple
    
    Gui() {
        initGui();
    }


    public void initGui() {
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setLayout(null);
        guiFrame.setSize(1000, 700);
        guiFrame.setVisible(true);
        guiFrame.setTitle("WebSchool APP");
        guiFrame.setResizable(false);
        guiFrame.getContentPane().setBackground(background);

        logoutButton.setFocusable(false);
        logoutButton.setBounds(25, 550, 150, 50);
        logoutButton.setBackground(Gui.secondary);
        logoutButton.setForeground(Color.white);
        logoutButton.setVisible(true);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Gui.studentFrame.dispose();
                Gui.adminFrame.dispose();
                Gui.teacherFrame.dispose();
                Gui.teacherFrame = new JFrame();
                Gui.studentFrame = new JFrame();
                Gui.adminFrame = new JFrame();
                JOptionPane.showMessageDialog(Gui.guiFrame, "You have been Logged out");
                guiFrame.setVisible(true);
            }
        });



        JLabel headLine = new JLabel("WELCOME TO WEBSCHOOL");
        headLine.setFont(new Font("Serif", Font.PLAIN, 20));
        headLine.setBounds(350, 10, 800, 100);
        headLine.setForeground(Color.WHITE);
        guiFrame.add(headLine);

        
        this.pageLoader();
        this.page(loginPage, null);
    }
    



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==this.login) {
            User user = Backendw.getUser(and(eq("id", username.getText()), eq("password", String.valueOf(password.getPassword()))));

            if(user != null && user instanceof User) {
                System.out.println(user.getSchool());
                JOptionPane.showMessageDialog(Gui.guiFrame, "Welcome to " + user.getSchool() + " school :)");

                user.mainMenuView();
                guiFrame.setVisible(false);
                username.setText("");
                password.setText("");


            } else {
                ErrorMessage(Errors.NonExistentUserError, this);
            }
        }
    }


    public static JPanel PanelCreator(String color){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0,0,200,1000);
        if(color.equals("green")){
            panel.setBackground(new Color(162,255,181));
        }else if(color.equals("red")){
            panel.setBackground(new Color(255,102,118));
        }else if(color.equals("blue")){
            panel.setBackground(new Color(82,119,175));
        }



        return panel;
    }
    



    public static JScrollPane arrayToTable(String[] column, Object[][] data, boolean editable, int RowHeight) {
        DefaultTableModel model = new DefaultTableModel(data, column) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return editable;
            }
        };


    
        JTable table = new JTable(model);
        table.setBackground(Color.white);
        table.setVisible(true);
        table.setOpaque(true);
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.white);
        table.setForeground(Color.BLACK);
        table.setRowHeight(RowHeight);
    
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Apply the custom font and font size to all cells
                cellComponent.setFont(new Font("", Font.BOLD, 16));
                return cellComponent;
            }
        };        
    
        // Apply the custom cell renderer to all columns
        table.setDefaultRenderer(Object.class, cellRenderer);
    
        JScrollPane sp = new JScrollPane(table);
        sp.setVisible(true);
        return sp;
    }
    
    


    
    public void page(JLayeredPane nextPage,JLayeredPane prevPage) {

        if(prevPage != null) {
            guiFrame.remove(prevPage);
        }

        guiFrame.add(nextPage);
        guiFrame.update(guiFrame.getGraphics());
    }






    public void loginPageSetup() {
        //remeber to add an image
        username = new JTextField("");
        username.setFont(new Font("", Font.PLAIN, 25));
        username.setBounds(330, 300, 300, 50);

        JLabel uiUser = new JLabel("ID: ");
        uiUser.setForeground(Color.white);
        uiUser.setFont(new Font("", Font.BOLD, 18));
        uiUser.setBounds(330,255, 300, 50);
        guiFrame.add(uiUser);



        password = new JPasswordField("");
        password.setFont(new Font("", Font.PLAIN, 25));
        password.setBounds(330, 390, 300, 50);

        JLabel uiPassword = new JLabel("Password");
        uiPassword.setForeground(Color.white);
        uiPassword.setFont(new Font("", Font.BOLD, 18));
        uiPassword.setBounds(330,345, 300, 50);
        guiFrame.add(uiPassword);

        //need to add an image
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setFont(new Font("Serif", Font.PLAIN, 12));
        showPassword.setFocusable(false);
        showPassword.setBounds(645, 405, 102, 20);
        showPassword.setBackground(null);
        showPassword.setForeground(Color.white);
        showPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JCheckBox c = (JCheckBox) e.getSource();
                password.setEchoChar(c.isSelected() ? '\u0000' : (Character) UIManager.get("PasswordField.echoChar"));
            }
        });
        guiFrame.add(showPassword);



        login = new JButton("Log in");
        login.setFont(new Font("", Font.BOLD, 15));
        login.setFocusable(false);
        login.setBounds(400, 450, 150, 50);
        login.setBackground(secondary);
        login.setForeground(Color.white);
        login.addActionListener(this);
        

        this.loginPage.setBounds(0, 0, 1000, 800);
        this.login.setBorder(BorderFactory.createEmptyBorder(2, 3, 4, 5));
        this.loginPage.add(login);
        this.loginPage.add(username);
        this.loginPage.add(password);
    }


    

    public void pageLoader() {
        this.loginPageSetup();
        guiFrame.update(guiFrame.getGraphics());
    }




    public static void ErrorMessage(Errors error, JFrame frame){
        String errorMessage = "";

        switch(error){
            case NonExistentUserError:
                errorMessage = "NonExistentUserError: a non existent User has been entered please try again";
                break;
            case CreatedClassError:
                errorMessage = "CreatedClassError: created class already exists";
                break;
            case GradeError:
                errorMessage = "GradeError : please enter a grade between 1 to 100";
                break;
            case NonExistentClassError:
                errorMessage = "NonExistentClassError: a non existent class has been entered";
                break;
            case NonExistentDayError:
                errorMessage = "NonExistentDayError: a non existent schedule day had been entered";
                break;
            case NonExistentHourError:
                errorMessage = "NonExistentHourError: a non existent schedule hour had been entered";
                break;
            case NonExistentSubjectError:
                errorMessage = "NonExistentSubjectError: a non existent subject had been entered";
                break;
            default:
                break;
        }


        JOptionPane.showMessageDialog(frame, errorMessage,
        "Error 505", JOptionPane.ERROR_MESSAGE);
    }


    public static void frameInit(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(1000, 700);
        frame.setVisible(true);
        frame.setTitle("WebSchool APP");
        frame.setResizable(false);
        frame.getContentPane().setBackground(Gui.background);
        frame.add(Gui.logoutButton);


        JPanel headLine = new JPanel();
        headLine.setFont(new Font("Serif", Font.PLAIN, 20));
        headLine.setBounds(200, 10, 800, 100);
        headLine.setForeground(Color.WHITE);
        headLine.setBackground(Color.black);
        headLine.setVisible(true);
        headLine.setName("headLine");
        JLabel text = new JLabel("<html>WELCOME TO WEBSCHOOL<br>School Attendance Management System</html>");
        text.setFont(new Font("Serif", Font.PLAIN, 30));
        text.setBounds(350, 10, 800, 100);
        text.setForeground(Color.WHITE);
        headLine.add(text);
        frame.add(headLine);


    }


    public User getCurrentUser() {
        return currentUser;
    }


    public String getHappend() {
        return happend;
    }


    public String getView() {
        return view;
    }


    public JButton getLogin() {
        return login;
    }


    public JTextField getUsername() {
        return username;
    }


    public JTextField getPassword() {
        return password;
    }


    public JLabel getMain() {
        return main;
    }


    public JLayeredPane getMainMenu() {
        return mainMenu;
    }


    public JLayeredPane getLoginPage() {
        return loginPage;
    }


    public JButton getlogoutButton() {
        return logoutButton;
    }


    public Color getBackground() {
        return background;
    }


    public Color getSecondary() {
        return secondary;
    }
}