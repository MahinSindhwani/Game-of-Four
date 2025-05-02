import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectPlayer {
    public SelectPlayer() {
        // Create a new JFrame for the player selection screen
        JFrame select_frame = new JFrame();
        select_frame.setSize(900, 600);
        select_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        select_frame.setLocationRelativeTo(null);

        // Create a custom JPanel with a background image
        JPanel panel = new JPanel() {
            ImageIcon background = new ImageIcon((SelectPlayer.class.getResource("herrou.jpg")));

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(new GridBagLayout()); // Use GridBagLayout for positioning components
        GridBagConstraints gbc = new GridBagConstraints(); // Create constraints for layout

        // Load and scale the logo image
        ImageIcon logoIcon = new ImageIcon(SelectPlayer.class.getResource("/four_logo.png"));
        logoIcon.setImage(logoIcon.getImage().getScaledInstance(240, 160, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logoIcon);

        // Create a button for "PLAYER VS COMPUTER" option
        CustomButton button1 = new CustomButton("PLAYER VS COMPUTER", Color.GREEN);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ColorSelection(); // Open the color selection screen
                select_frame.setVisible(false);
            }
        });

        // Create a button for "PLAYER VS PLAYER" option
        CustomButton button2 = new CustomButton("  PLAYER VS PLAYER  ", Color.BLUE);
        button2.addActionListener(new ActionListener() { // Add action listener for button click
            @Override
            public void actionPerformed(ActionEvent e) {
                new ColorSelection(); // Open the color selection screen
                select_frame.setVisible(false);
            }
        });

        // Set constraints for the logo label's position
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(logoLabel, gbc);

        // Set constraints for the first button
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(button1, gbc); // Add button1 to the panel

        // Set constraints for the second button
        gbc.gridy = 2;
        gbc.weighty = 0.1; // Set vertical weight
        gbc.insets = new Insets(0, 0, 0, 0); // Reset insets
        panel.add(button2, gbc); // Add button2 to the panel
        panel.add(button2, gbc); // This line is duplicated and adds button2 again to the panel

        // Add the panel to the frame
        select_frame.add(panel);
        select_frame.setVisible(true);
    }
}
