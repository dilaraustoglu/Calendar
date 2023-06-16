package com.calendar;

import com.github.lgooddatepicker.components.DateTimePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javazoom.jl.player.Player;

public class CalendarApp extends JFrame {
    private JButton createEventButton;
    private JButton editEventButton;
    private JButton deleteEventButton;
    private JButton refreshButton;
    private JTable eventTable;
    private String currentUserName;

    public CalendarApp(String username) {
        currentUserName = username;
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
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        eventTable = new JTable(model);
        refreshEventTable(username);

        JScrollPane scrollPane = new JScrollPane(eventTable);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        createEventButton.addActionListener(e -> createEventWindow(currentUserName));
        editEventButton.addActionListener(e -> editSelectedEvent());
        deleteEventButton.addActionListener(e -> deleteSelectedEvent(username));
        refreshButton.addActionListener(e -> refreshEventTable(currentUserName));
    }

    private void createEventWindow(String username) {
        JFrame createEventFrame = new JFrame("Olay Oluştur");
        createEventFrame.setSize(550, 400);
        createEventFrame.setLocationRelativeTo(null);

        JPanel createEventPanel = new JPanel();
        createEventPanel.setLayout(new GridLayout(7, 2));

        JLabel userNameLabel = new JLabel("Kullanıcı Adı: " + username);

        JLabel dateTimeLabel = new JLabel("Tarih ve Saat:");
        DateTimePicker dateTimePicker = new DateTimePicker();
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
                LocalDateTime dateTime = dateTimePicker.getDateTimeStrict();
                String eventName = eventNameField.getText();
                String eventType = eventTypeField.getText();
                String eventDescription = eventDescriptionField.getText();

                saveEventToFile(username, dateTime, eventName, eventType, eventDescription);
                playBipSound(dateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                createEventFrame.dispose();

                refreshEventTable(username);
            }
        });

        createEventPanel.add(userNameLabel);
        createEventPanel.add(new JLabel()); // Boş etiket
        createEventPanel.add(dateTimeLabel);
        createEventPanel.add(dateTimePicker);
        createEventPanel.add(eventNameLabel);
        createEventPanel.add(eventNameField);
        createEventPanel.add(eventTypeLabel);
        createEventPanel.add(eventTypeField);
        createEventPanel.add(eventDescriptionLabel);
        createEventPanel.add(eventDescriptionField);

        createEventPanel.add(new JLabel()); // Boş etiket
        createEventPanel.add(new JLabel()); // Boş etiket
        createEventPanel.add(createButton);

        createEventFrame.add(createEventPanel);
        createEventFrame.setVisible(true);
    }

    private void editSelectedEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) eventTable.getModel();
            LocalDateTime transactionTime = (LocalDateTime) model.getValueAt(selectedRow, 0);
            LocalDateTime eventStartTime = (LocalDateTime) model.getValueAt(selectedRow, 1);
            String eventName = (String) model.getValueAt(selectedRow, 2);
            String eventType = (String) model.getValueAt(selectedRow, 3);
            String eventDescription = (String) model.getValueAt(selectedRow, 4);

            // Düzenleme penceresini aç
            openEditEventWindow(transactionTime, eventStartTime, eventName, eventType, eventDescription);
        }
    }

    private void openEditEventWindow(LocalDateTime transactionTime, LocalDateTime eventStartTime, String eventName, String eventType, String eventDescription) {
        JFrame editEventFrame = new JFrame("Olay Düzenle");
        editEventFrame.setSize(550, 400);
        editEventFrame.setLocationRelativeTo(null);

        JPanel editEventPanel = new JPanel();
        editEventPanel.setLayout(new GridLayout(7, 2));

        JLabel transactionTimeLabel = new JLabel("İşlem Zamanı: " + transactionTime);
        JLabel eventStartTimeLabel = new JLabel("Olayın Başlangıç Zamanı:");
        DateTimePicker eventStartDateTimePicker = new DateTimePicker();
        eventStartDateTimePicker.setDateTimeStrict(eventStartTime);
        JLabel eventNameLabel = new JLabel("Olayın Tanımlanması:");
        JTextField eventNameField = new JTextField(eventName);
        JLabel eventTypeLabel = new JLabel("Olayın Tipi:");
        JTextField eventTypeField = new JTextField(eventType);
        JLabel eventDescriptionLabel = new JLabel("Olayın Açıklaması:");
        JTextField eventDescriptionField = new JTextField(eventDescription);

        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDateTime newEventStartTime = eventStartDateTimePicker.getDateTimeStrict();
                String newEventName = eventNameField.getText();
                String newEventType = eventTypeField.getText();
                String newEventDescription = eventDescriptionField.getText();

                // Dosyadaki ilgili satırı güncelle
                updateEventInFile(currentUserName, transactionTime, eventStartTime, eventName, newEventStartTime, newEventName, newEventType, newEventDescription);

                // Düzenleme penceresini kapat
                editEventFrame.dispose();

                // Tabloyu yenile
                refreshEventTable(currentUserName);
            }
        });

        editEventPanel.add(transactionTimeLabel);
        editEventPanel.add(new JLabel());
        editEventPanel.add(eventStartTimeLabel);
        editEventPanel.add(eventStartDateTimePicker);
        editEventPanel.add(eventNameLabel);
        editEventPanel.add(eventNameField);
        editEventPanel.add(eventTypeLabel);
        editEventPanel.add(eventTypeField);
        editEventPanel.add(eventDescriptionLabel);
        editEventPanel.add(eventDescriptionField);

        editEventPanel.add(new JLabel());
        editEventPanel.add(new JLabel());
        editEventPanel.add(saveButton);

        editEventFrame.add(editEventPanel);
        editEventFrame.setVisible(true);
    }

    private void playBipSound(String startTime) {
        Thread countdownThread = new Thread(() -> {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime eventStartTime = LocalTime.parse(startTime, formatter);

                LocalTime currentTime = LocalTime.now();

                long delayInMillis = eventStartTime.toSecondOfDay() - currentTime.toSecondOfDay();

                if (delayInMillis > 0) {
                    Thread.sleep(delayInMillis * 1000);
                }

                playSound("bip.mp3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        countdownThread.start();
    }

    private void playSound(String soundFile) {
        try {
            URL resourceUrl = getClass().getResource("/sound/" + soundFile);
            if (resourceUrl == null) {
                System.err.println("Ses dosyası bulunamadı: " + soundFile);
                return;
            }

            InputStream inputStream = resourceUrl.openStream();
            Player player = new Player(inputStream);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEventInFile(String username, LocalDateTime transactionTime, LocalDateTime eventStartTime, String eventName, LocalDateTime newEventStartTime, String newEventName, String newEventType, String newEventDescription) {
        try {
            File inputFile = new File("events.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] eventDetails = line.split(",");
                String eventUsername = eventDetails[0].trim();
                LocalDateTime transactionTimestamp = LocalDateTime.parse(eventDetails[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime startTimestamp = LocalDateTime.parse(eventDetails[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String name = eventDetails[3].trim();

                // Kullanıcı adı, işlem zamanı, olay başlangıç zamanı ve olay adı eşleşen satırı güncelle
                if (eventUsername.equals(username) && transactionTimestamp.equals(transactionTime) && startTimestamp.equals(eventStartTime) && name.equals(eventName)) {
                    line = eventUsername + "," + transactionTimestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "," + newEventStartTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "," + newEventName + "," + newEventType + "," + newEventDescription;
                }

                writer.write(line + "\n");
            }

            reader.close();
            writer.close();

            // Eski dosyayı sil
            inputFile.delete();

            // Geçici dosyayı yeni dosya olarak adlandır
            tempFile.renameTo(inputFile);

            System.out.println("Olay başarıyla güncellendi.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedEvent(String username) {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) eventTable.getModel();
            LocalDateTime eventStartTime = (LocalDateTime) model.getValueAt(selectedRow, 1);
            String eventName = (String) model.getValueAt(selectedRow, 2);

            // Tablodan olayı kaldır
            model.removeRow(selectedRow);

            // events.txt dosyasından olayı sil
            deleteEventFromFile(username, eventStartTime, eventName);
        }
    }

    private void deleteEventFromFile(String username, LocalDateTime eventStartTime, String eventName) {
        try {
            File inputFile = new File("events.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] eventDetails = line.split(",");
                String eventUsername = eventDetails[0].trim();
                LocalDateTime startTimestamp = LocalDateTime.parse(eventDetails[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String name = eventDetails[3].trim();

                // Kullanıcı adı, olay başlangıç zamanı ve olay adı eşleşmeyen satırları geçici dosyaya yaz
                if (!(eventUsername.equals(username) && startTimestamp.equals(eventStartTime) && name.equals(eventName))) {
                    writer.write(line + "\n");
                }
            }

            reader.close();
            writer.close();

            // Eski dosyayı sil
            inputFile.delete();

            // Geçici dosyayı yeni dosya olarak adlandır
            tempFile.renameTo(inputFile);

            System.out.println("Olay başarıyla silindi.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveEventToFile(String username, LocalDateTime dateTime, String eventName, String eventType, String eventDescription) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("events.txt", true));
            writer.write(username + "," + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "," + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "," + eventName + "," + eventType + "," + eventDescription + "\n");
            writer.close();
            System.out.println("Olay başarıyla kaydedildi.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshEventTable(String username) {
        DefaultTableModel model = (DefaultTableModel) eventTable.getModel();
        model.setRowCount(0); // Tabloyu temizle

        try {
            BufferedReader reader = new BufferedReader(new FileReader("events.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] eventDetails = line.split(",");
                String eventUsername = eventDetails[0].trim();

                // Kullanıcı adı eşleşen olayları tabloya ekle
                if (eventUsername.equals(username)) {
                    LocalDateTime transactionTimestamp = LocalDateTime.parse(eventDetails[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    LocalDateTime startTimestamp = LocalDateTime.parse(eventDetails[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String name = eventDetails[3].trim();
                    String type = eventDetails[4].trim();
                    String description = eventDetails[5].trim();

                    model.addRow(new Object[]{transactionTimestamp, startTimestamp, name, type, description});
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String username = "kullanici123"; // Kullanıcı adını buraya girin
        SwingUtilities.invokeLater(() -> {
            CalendarApp app = new CalendarApp(username);
            app.setVisible(true);
        });
    }
}