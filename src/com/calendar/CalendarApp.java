package com.calendar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CalendarApp extends JFrame {
    private JButton createEventButton;
    private JButton editEventButton;
    private JButton deleteEventButton;
    private JButton refreshButton;
    private JTable eventTable;
    private String currentUserName;

    public CalendarApp() {
        setTitle("Takvim");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        createEventButton = new JButton("Olay Oluştur");
        editEventButton = new JButton("Olay Düzenle");
        deleteEventButton = new JButton("Olay Sil");
        refreshButton = new JButton("Yenile");
        buttonPanel.add(createEventButton);
        buttonPanel.add(editEventButton);
        buttonPanel.add(deleteEventButton);
        buttonPanel.add(refreshButton);

        String[] columnNames = {"İşlem Zamanı", "Olayın Başlangıç Zamanı", "Olayın Tanımlanması", "Olayın Tipi", "Olayın Açıklaması"};
        Object[][] data = {
                {"2023-06-13 15:00", "2023-06-14 10:00", "Toplantı", "İş", "Proje sunumu için toplantı"},
                {"2023-06-15 09:30", "2023-06-15 10:30", "Randevu", "Sağlık", "Doktor randevusu"},
                {"2023-06-16 14:00", "2023-06-16 16:00", "Etkinlik", "Sosyal", "Kitap okuma etkinliği"}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        eventTable = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(eventTable);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        createEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createEventWindow();
            }
        });
    }

    private void createEventWindow() {
        JFrame createEventFrame = new JFrame("Olay Oluştur");
        createEventFrame.setSize(400, 300);
        createEventFrame.setLocationRelativeTo(null);

        JPanel createEventPanel = new JPanel();
        createEventPanel.setLayout(new GridLayout(6, 2));

        JLabel userNameLabel = new JLabel("Kullanıcı Adı: " + currentUserName);

        JLabel dateTimeLabel = new JLabel("Tarih ve Saat (yyyy-MM-dd HH:mm):");
        JTextField dateTimeField = new JTextField();
        JLabel startTimeLabel = new JLabel("Başlangıç Saati (HH:mm):");
        JTextField startTimeField = new JTextField();
        JLabel eventNameLabel = new JLabel("Olayın Tanımlanması:");
        JTextField eventNameField = new JTextField();
        JLabel eventTypeLabel = new JLabel("Olayın Tipi:");
        JTextField eventTypeField = new JTextField();
        JLabel eventDescriptionLabel = new JLabel("Olayın Açıklaması:");
        JTextField eventDescriptionField = new JTextField();

        JButton createButton = new JButton("Oluştur");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateTime = dateTimeField.getText();
                String startTime = startTimeField.getText();
                String eventName = eventNameField.getText();
                String eventType = eventTypeField.getText();
                String eventDescription = eventDescriptionField.getText();

                // Olay bilgilerini dosyaya kaydet
                saveEventToFile(currentUserName, dateTime, startTime, eventName, eventType, eventDescription);

                // Oluşturma penceresini kapat
                createEventFrame.dispose();
            }
        });

        createEventPanel.add(userNameLabel);
        createEventPanel.add(new JLabel()); // Boş etiket
        createEventPanel.add(dateTimeLabel);
        createEventPanel.add(dateTimeField);
        createEventPanel.add(startTimeLabel);
        createEventPanel.add(startTimeField);
        createEventPanel.add(eventNameLabel);
        createEventPanel.add(eventNameField);
        createEventPanel.add(eventTypeLabel);
        createEventPanel.add(eventTypeField);
        createEventPanel.add(eventDescriptionLabel);
        createEventPanel.add(eventDescriptionField);

        createEventPanel.add(new JLabel()); // Boş etiket
        createEventPanel.add(createButton);

        createEventFrame.add(createEventPanel);
        createEventFrame.setVisible(true);
    }

    private void saveEventToFile(String userName, String dateTime, String startTime, String eventName, String eventType, String eventDescription) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = now.format(formatter);

        try {
            FileWriter writer = new FileWriter("events.txt", true);
            writer.write("Kullanıcı Adı: " + userName + "\n");
            writer.write("İşlem Zamanı: " + currentDateTime + "\n");
            writer.write("Olayın Başlangıç Zamanı: " + dateTime + " " + startTime + "\n");
            writer.write("Olayın Tanımlanması: " + eventName + "\n");
            writer.write("Olayın Tipi: " + eventType + "\n");
            writer.write("Olayın Açıklaması: " + eventDescription + "\n");
            writer.write("--------------------------------------\n");
            writer.close();
            System.out.println("Olay başarıyla kaydedildi.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalendarApp calendarApp = new CalendarApp();
            calendarApp.currentUserName = "Kullanıcı Adı";
            calendarApp.setVisible(true);
        });
    }
}