import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Student {
    private String name;
    private String rollNumber;
    private String grade;

    public Student(String name, String rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }
}

class StudentManagementSystemGUI {
    private List<Student> students;
    private JFrame frame;
    private JTextField nameField, rollNumberField, gradeField, searchField, removeField;
    private DefaultTableModel tableModel;

    public StudentManagementSystemGUI() {
        students = new ArrayList<>();
        loadStudentsFromFile("students.txt");
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());

        // Set frame background color
        frame.getContentPane().setBackground(new Color(230, 240, 255));

        // Title Label
        JLabel titleLabel = new JLabel("Student Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 51, 102));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 2),
                "Student Details", 0, 0, new Font("Arial", Font.BOLD, 16), Color.BLUE));
        inputPanel.setBackground(new Color(200, 220, 255));

        nameField = createTextField();
        rollNumberField = createTextField();
        gradeField = createTextField();
        searchField = createTextField();
        removeField = createTextField();

        JButton addButton = createButton("Add Student");
        JButton searchButton = createButton("Search");
        JButton removeButton = createButton("Remove");
        JButton saveButton = createButton("Save to File");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Roll Number:"));
        inputPanel.add(rollNumberField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeField);
        inputPanel.add(addButton);
        inputPanel.add(saveButton);
        inputPanel.add(new JLabel("Search Roll Number:"));
        inputPanel.add(searchField);
        inputPanel.add(searchButton);
        inputPanel.add(new JLabel("Remove Roll Number:"));
        inputPanel.add(removeField);
        inputPanel.add(removeButton);

        frame.add(inputPanel, BorderLayout.CENTER);

        // Table Panel
        tableModel = new DefaultTableModel(new String[]{"Name", "Roll Number", "Grade"}, 0);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setBackground(new Color(230, 240, 250));
        table.setForeground(new Color(0, 51, 102));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 2),
                "Student List", 0, 0, new Font("Arial", Font.BOLD, 16), Color.BLUE));
        tableScrollPane.setBackground(new Color(200, 220, 255));
        frame.add(tableScrollPane, BorderLayout.SOUTH);

        // Add Button Actions
        addButton.addActionListener(e -> addStudent());
        saveButton.addActionListener(e -> saveStudentsToFile("students.txt"));
        searchButton.addActionListener(e -> searchStudent());
        removeButton.addActionListener(e -> removeStudent());

        // Display all students initially
        updateTable();

        frame.setVisible(true);
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(new Color(0, 51, 102));
        textField.setBackground(new Color(255, 255, 255));
        return textField;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 102, 204));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 102), 2));
        return button;
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();
        String grade = gradeField.getText().trim();

        if (name.isEmpty() || rollNumber.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        students.add(new Student(name, rollNumber, grade));
        JOptionPane.showMessageDialog(frame, "Student added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        updateTable();
        clearInputFields();
    }

    private void searchStudent() {
        String rollNumber = searchField.getText().trim();
        if (rollNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Enter a roll number to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Student student : students) {
            if (student.getRollNumber().equals(rollNumber)) {
                JOptionPane.showMessageDialog(frame, "Student Found:\nName: " + student.getName() + "\nGrade: " + student.getGrade(),
                        "Search Result", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Student not found.", "Search Result", JOptionPane.ERROR_MESSAGE);
    }

    private void removeStudent() {
        String rollNumber = removeField.getText().trim();
        if (rollNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Enter a roll number to remove.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean removed = students.removeIf(student -> student.getRollNumber().equals(rollNumber));
        if (removed) {
            JOptionPane.showMessageDialog(frame, "Student removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateTable();
        } else {
            JOptionPane.showMessageDialog(frame, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[]{student.getName(), student.getRollNumber(), student.getGrade()});
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        rollNumberField.setText("");
        gradeField.setText("");
        searchField.setText("");
        removeField.setText("");
    }

    private void saveStudentsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Student student : students) {
                writer.write(student.getName() + "," + student.getRollNumber() + "," + student.getGrade());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(frame, "Students saved to file successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStudentsFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    students.add(new Student(data[0], data[1], data[2]));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementSystemGUI::new);
    }
}
