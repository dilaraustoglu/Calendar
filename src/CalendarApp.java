import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalendarApp extends JFrame {
    private JButton createEventButton;
    private JButton editEventButton;
    private JButton deleteEventButton;
    private JButton refreshButton;
    private JTable eventTable;

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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CalendarApp().setVisible(true);
            }
        });
    }
}
