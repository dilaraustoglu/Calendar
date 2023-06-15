package com.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegistrationInterface extends JFrame {
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField tcField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JComboBox<String> userTypeComboBox;
    private JButton registerButton;

    public RegistrationInterface() {
        setTitle("Kayıt Ol");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2));

        JLabel nameLabel = new JLabel("Ad:");
        nameField = new JTextField(20);
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel surnameLabel = new JLabel("Soyad:");
        surnameField = new JTextField(20);
        panel.add(surnameLabel);
        panel.add(surnameField);

        JLabel usernameLabel = new JLabel("Kullanıcı Adı:");
        usernameField = new JTextField(20);
        panel.add(usernameLabel);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Şifre:");
        passwordField = new JPasswordField(20);
        panel.add(passwordLabel);
        panel.add(passwordField);

        JLabel tcLabel = new JLabel("TC Kimlik No:");
        tcField = new JTextField(20);
        panel.add(tcLabel);
        panel.add(tcField);

        JLabel phoneLabel = new JLabel("Telefon:");
        phoneField = new JTextField(20);
        panel.add(phoneLabel);
        panel.add(phoneField);

        JLabel emailLabel = new JLabel("E-posta:");
        emailField = new JTextField(20);
        panel.add(emailLabel);
        panel.add(emailField);

        JLabel addressLabel = new JLabel("Adres:");
        addressField = new JTextField(20);
        panel.add(addressLabel);
        panel.add(addressField);

        JLabel userTypeLabel = new JLabel("Kullanıcı Tipi:");
        String[] userTypes = {"Admin", "Kullanıcı"};
        userTypeComboBox = new JComboBox<>(userTypes);
        panel.add(userTypeLabel);
        panel.add(userTypeComboBox);

        registerButton = new JButton("Kayıt Ol");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        panel.add(registerButton);

        add(panel);
    }

    private void register() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String tc = tcField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String userType = (String) userTypeComboBox.getSelectedItem();

        // Kayıt olma işlemi burada gerçekleştirilir
        saveUser(name, surname, username, password, tc, phone, email, address, userType);

        JOptionPane.showMessageDialog(this, "Kayıt olma başarılı!");
        dispose();
    }

    private void saveUser(String name, String surname, String username, String password, String tc, String phone,
                          String email, String address, String userType) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true));
            writer.write(name + "," + surname + "," + username + "," + password + "," + tc + "," + phone + ","
                    + email + "," + address + "," + userType);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationInterface().setVisible(true));
    }
}
