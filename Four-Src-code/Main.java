import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFrame four_frame = new JFrame();
        four_frame.setSize(900, 600);
        four_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        four_frame.setLocationRelativeTo(null); // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

        // Create a custom JPanel with a background
        JPanel panel = new JPanel() {
            ImageIcon backgroundGif = new ImageIcon(Main.class.getResource("herrou.jpg"));

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background
                g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Load the logo image
        ImageIcon logoIcon = new ImageIcon(Main.class.getResource("four_logo.png"));
        logoIcon.setImage(logoIcon.getImage().getScaledInstance(240, 160, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logoIcon);
        CustomButton button1 = new CustomButton("New Game", Color.RED);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new SelectPlayer();
                four_frame.setVisible(false);
            }
        });

        CustomButton button2 = new CustomButton("Load Game", Color.RED);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GameSaver.loadGame("Save.txt");
                four_frame.setVisible(false);
            }
        });

        // Set constraints for the logo label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(logoLabel, gbc);

        // Set constraints for button1
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(button1, gbc);

        // Set constraints for button2
        gbc.gridy = 2;
        panel.add(button2, gbc);

        // Add panel to the frame
        four_frame.add(panel);
        four_frame.setVisible(true);

    }
}
