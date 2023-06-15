import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginInterface extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginInterface() {
        setTitle("Giriş Yap");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Kullanıcı Adı:");
        usernameField = new JTextField(20);
        panel.add(usernameLabel);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Şifre:");
        passwordField = new JPasswordField(20);
        panel.add(passwordLabel);
        panel.add(passwordField);

        loginButton = new JButton("Giriş Yap");
        loginButton.addActionListener(this);
        panel.add(loginButton);

        registerButton = new JButton("Kayıt Ol");
        registerButton.addActionListener(this);
        panel.add(registerButton);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            login();
        } else if (e.getSource() == registerButton) {
            openRegistrationWindow();
        }
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (checkCredentials(username, password)) {
            JOptionPane.showMessageDialog(this, "Giriş başarılı!");
            openCalendarForm();
        } else {
            JOptionPane.showMessageDialog(this, "Geçersiz kullanıcı adı veya şifre!");
        }
    }

    private boolean checkCredentials(String username, String password) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String storedUsername = userData[2];
                String storedPassword = userData[3];

                if (username.equals(storedUsername) && password.equals(storedPassword)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openRegistrationWindow() {
        RegistrationInterface registrationInterface = new RegistrationInterface();
        registrationInterface.setVisible(true);
    }

    private void openCalendarForm() {
        CalendarApp calendarApp = new CalendarApp();
        calendarApp.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginInterface();
            }
        });
    }
}
