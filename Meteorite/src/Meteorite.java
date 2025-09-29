import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Meteorite extends JFrame {
    private JTextField inputField;
    private JButton okButton, cancelButton;

    public Meteorite() {
        setTitle("Enter Meteorite Count");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter Meteorite: "));
        inputField = new JTextField(10);
        inputPanel.add(inputField);
        add(inputPanel, BorderLayout.CENTER);

        // ปุ่ม
        JPanel buttonPanel = new JPanel();
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // กด OK
        okButton.addActionListener(e -> {
            String text = inputField.getText().trim();
            try {
                int asteroidCount = Integer.parseInt(text);
                dispose();
                new PanelMeteorite(asteroidCount);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Input Number Only!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        Meteorite frame = new Meteorite();
        frame.setVisible(true);
    }
}
