package webschoolproject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import static com.mongodb.client.model.Filters.*;


public class Dashboard {

    private static JFrame frame;
    private static String school;

    public Dashboard(JFrame frame, String school) {

        Dashboard.frame = frame;
        Dashboard.school = school;

        SwingUtilities.invokeLater(() -> {
            try {createAndShowGUI();
            } catch (IOException e) {}
        });
    }

    private static void createAndShowGUI() throws IOException {
        Arrays.stream(Gui.adminFrame.getContentPane().getComponents())
        .filter(component -> component instanceof JPanel || component instanceof JScrollPane)
        .forEach(component -> {
            if(!(component instanceof JButton)) {
                if(component.getName() == null || !component.getName().equals("adminSidePanel")) {
                    if(component.getName() == null || !component.getName().equals("headLine")) {
                        component.setVisible(false);
                        Gui.adminFrame.remove(component);
                    }
                }
            }
        });




        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"ID", "Password", "First Name", "Last Name", "Role", "Edit", "Delete"};
        Object[][] data = {};


        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6; // Make all cells non-editable
            }
        };




        JTable table = new JTable(model);
        table.setDefaultRenderer(Object.class, new ButtonRenderer());
        table.setDefaultEditor(Object.class, new ButtonEditor());


        ArrayList<Document> users = (ArrayList<Document>) Backendw.viewAllSchool(school);

        // Create buttons and add them to specific cells using a loop
        for (int row = 0; row < users.size(); row++) {
            model.addRow(data);

            String role;
            Document doc = users.get(row);

            User user;
            if (doc.containsKey("subject")) {
                user = Backendw.gson.fromJson(doc.toJson(), Teacher.class);
                role = "Teacher";
            } else if (doc.containsKey("grade")) {
                user = Backendw.gson.fromJson(doc.toJson(), Student.class);
                role = "Student";
            } else {
                user = Backendw.gson.fromJson(doc.toJson(), Admin.class);
                role = "Admin";
            }

            table.setValueAt(user.getId(), row, 0);table.setValueAt(user.getPassword(), row, 1);table.setValueAt(user.getFirstName(), row, 2);
            table.setValueAt(user.getLastName(), row, 3);table.setValueAt(role, row, 4);


            addButtonToCell(table, row, 5, "Edit", user);
            addButtonToCell(table, row, 6, "Delete", user);
        }





        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBounds(230, 150, 730, 435);


        frame.add(panel);
        frame.setVisible(true);
    }




    static class ButtonRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JButton) {
                return (JButton) value;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }





    static class ButtonEditor extends DefaultCellEditor {
        private JButton button;

        public ButtonEditor() {
            super(new JTextField());
            setClickCountToStart(1);

            button = new JButton();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof JButton) {
                button = (JButton) value;
            }
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button;
        }
    }



    private static void addButtonToCell(JTable table, int row, int column, String buttonText, User user) {
        JButton button = new JButton(buttonText);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(button.getText().equals("Delete")) {
                    if(!(user instanceof Admin)) {
                        try {
                            Backendw.deleteUser(eq("id", user.getId()), school);
                            ((DefaultTableModel) table.getModel()).removeRow(row);
                            new Dashboard(frame, school);
    
                        } catch (IOException e1) {}
                        
                        JOptionPane.showMessageDialog(Gui.adminFrame, "The user: " + user.getFirstName() + " " + user.getLastName() + "\nhas been deleted from the System");
                    }
                } else if(button.getText().equals("Edit")) {
                    if(!(user instanceof Admin)){
                        EditUser.runEditUser(user);
                    }
                }
                System.out.println(user);

            }
        });

        table.setValueAt(button, row, column);
    }
}
